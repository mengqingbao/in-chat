package pro.chinasoft.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.xmpp.client.util.InMessageStore;
import org.xmpp.client.util.XmppTool;

import pro.chinasoft.service.InSmsService;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

/**
 * @author yangyu
 *	功能描述：自定义TabHost
 */
public class MainTabActivity extends FragmentActivity{	
	//定义FragmentTabHost对象
	private FragmentTabHost mTabHost;
	
	//定义一个布局
	private LayoutInflater layoutInflater;
		
	//定义数组来存放Fragment界面
	private Class fragmentArray[] = {FragmentPage1.class,FragmentPage2.class,FragmentPage3.class,FragmentPage4.class};
	
	//定义数组来存放按钮图片
	private int mImageViewArray[] = {R.drawable.tab_message_btn,R.drawable.tab_selfinfo_btn,
									 R.drawable.tab_square_btn,R.drawable.tab_more_btn};
	
	//Tab选项卡的文字
	private String mTextviewArray[] = {"消息", "好友", "日程", "设置"};
	private String userid;
	private List<Msg> listMsg = new ArrayList<Msg>();

	
	public class Msg {
		String userid;
		String msg;
		String date;
		String from;

		public Msg(String userid, String msg, String date, String from) {
			this.userid = userid;
			this.msg = msg;
			this.date = date;
			this.from = from;
		}
	}
	
	@Override
	@SuppressLint("NewApi")
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       this.userid = getIntent().getStringExtra("USERID");
       
       	Intent intent = new Intent(this,InSmsService.class);
		intent.putExtra("USERID", userid);
		this.startService(intent);
		
       //消息监听
       ChatManager cm = XmppTool.getConnection().getChatManager();
       cm.addChatListener(new ChatManagerListener() {
			@Override
			public void chatCreated(Chat chat, boolean able) 
			{
				chat.addMessageListener(new MessageListener() {
					@Override
					public void processMessage(Chat chat2, Message message)
					{
	
						
						String from = message.getFrom();
						String friendId=null;
						if(from.contains("/")){
							friendId=from.substring(0,from.lastIndexOf("/"));
							Log.v("--tags--", "--tags-form--接受到 "+friendId);
							Log.v("--tags--", "--tags-message--信息 "+message.getBody());
						}
						try {
							Map<String, String> map = new HashMap<String, String>();
							map.put("content", message.getBody());
							map.put("userId", userid);
							map.put("friendId", friendId);
							InMessageStore.add("InMessage", map,MainTabActivity.this);
						} catch (Exception e) {
							Log.i("--tags--", e.getMessage());
						}
						//发送广播通知更新聊天页面内容
						Intent intent = new Intent("pro.chinasoft.activity.InChatActivity");
						intent.putExtra("content", message.getBody());
						intent.putExtra("friendId", friendId);
				        sendBroadcast(intent);
					}
				});
			}
		});
       
		  PacketFilter filter = new AndFilter(new PacketTypeFilter(  
                  Presence.class));  
          PacketListener listener = new PacketListener() {  

              @Override  
              public void processPacket(Packet packet) {  
                  Log.i("Presence", "PresenceService------" + packet.toXML());  
                  //看API可知道   Presence是Packet的子类  
                  if (packet instanceof Presence) {  
                      Log.i("Presence", packet.toXML());  
                      Presence presence = (Presence) packet;  
                      //Presence还有很多方法，可查看API   
                      String from = presence.getFrom();//发送方  
                      String to = presence.getTo();//接收方  
                      //Presence.Type有7中状态  
                      if (presence.getType().equals(Presence.Type.subscribe)) {//好友申请  
                            System.out.println("好友申请 +++++");
						/*   确认                         Presence presence = new Presence(
						                                    Presence.Type.subscribed);//同意是
						subscribed   拒绝是unsubscribe
						                    presence.setTo(...);//接收方jid
						                    presence.setFrom(...);//发送方jid
						connection.sendPacket(presence);//connection是你自己的XMPPConnection链接
*/                      } else if (presence.getType().equals(  
                              Presence.Type.subscribed)) {//同意添加好友  
                            
                      } else if (presence.getType().equals(  
                              Presence.Type.unsubscribe)) {//拒绝添加好友  和  删除好友  
                            
                      } else if (presence.getType().equals(  
                              Presence.Type.unsubscribed)) {//这个我没用到  
                      	
                      } else if (presence.getType().equals(  
                              Presence.Type.unavailable)) {//好友下线   要更新好友列表，可以在这收到包后，发广播到指定页面   更新列表  
                            
                      } else {//好友上线  
                            
                      }  
                  }  
              }  
          };  
          XmppTool.getConnection().addPacketListener(listener, filter);  
       setContentView(R.layout.main_tab_layout);
        
        initView();
       
    }
	
	@SuppressLint("NewApi")
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP, ActionBar.DISPLAY_HOME_AS_UP);
	}

	/**
	 * 初始化组件
	 */
	private void initView(){
		//实例化布局对象
		layoutInflater = LayoutInflater.from(this);
				
		//实例化TabHost对象，得到TabHost
		mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);	
		
		//得到fragment的个数
		int count = fragmentArray.length;	
				
		for(int i = 0; i < count; i++){	
			//为每一个Tab按钮设置图标、文字和内容
			TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));
			//将Tab按钮添加进Tab选项卡中
			mTabHost.addTab(tabSpec, fragmentArray[i], null);
			//设置Tab按钮的背景
			mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_tab_background);
		}
	}
				
	/**
	 * 给Tab按钮设置图标和文字
	 */
	private View getTabItemView(int index){
		View view = layoutInflater.inflate(R.layout.tab_item_view, null);
		
		ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
		imageView.setImageResource(mImageViewArray[index]);
		
		TextView textView = (TextView) view.findViewById(R.id.textview);		
		textView.setText(mTextviewArray[index]);
	
		return view;
	}
	
//==============================淫荡的分割线================================================
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) 
		{
							
			switch (msg.what) {
			case 1:
				//获取消息并显示
				String[] args = (String[]) msg.obj;
				listMsg.add(new Msg(args[0], args[1], args[2], args[3]));
				//刷新适配器
				//adapter.notifyDataSetChanged();
				break;			
			/*case 2:
				//附件进度条
				if(pb.getVisibility()==View.GONE){
					pb.setMax(100);
					pb.setProgress(1);
					pb.setVisibility(View.VISIBLE);
				}
				break;
			case 3:
				pb.setProgress(msg.arg1);
				break;
			case 4:
				pb.setVisibility(View.GONE);
				break;
			case 5:
				final IncomingFileTransfer infiletransfer = request.accept();
				
				//提示框
				AlertDialog.Builder builder = new AlertDialog.Builder(FormClient.this);
				
				builder.setTitle("附件：")
						.setCancelable(false)
						.setMessage("是否接收文件："+file.getName()+"?")
						.setPositiveButton("接受",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										try 
										{
											infiletransfer.recieveFile(file);
										} 
										catch (XMPPException e)
										{
											Toast.makeText(FormClient.this,"接收失败!",Toast.LENGTH_SHORT).show();
											e.printStackTrace();
										}
										
										handler.sendEmptyMessage(2);
										
										Timer timer = new Timer();
										TimerTask updateProgessBar = new TimerTask() {
											public void run() {
												if ((infiletransfer.getAmountWritten() >= request.getFileSize())
														|| (infiletransfer.getStatus() == FileTransfer.Status.error)
														|| (infiletransfer.getStatus() == FileTransfer.Status.refused)
														|| (infiletransfer.getStatus() == FileTransfer.Status.cancelled)
														|| (infiletransfer.getStatus() == FileTransfer.Status.complete)) 
												{
													cancel();
													handler.sendEmptyMessage(4);
												} 
												else
												{
													long p = infiletransfer.getAmountWritten() * 100L / infiletransfer.getFileSize();													
													
													android.os.Message message = handler.obtainMessage();
													message.arg1 = Math.round((float) p);
													message.what = 3;
													message.sendToTarget();
													Toast.makeText(FormClient.this,"接收完成!",Toast.LENGTH_SHORT).show();
												}
											}
										};
										timer.scheduleAtFixedRate(updateProgessBar, 10L, 10L);
										dialog.dismiss();
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id)
									{
										request.reject();
										dialog.cancel();
									}
								}).show();
				break;
*/			default:
				break;
			}
		};
	};	

	//退出
	@Override
	public void onBackPressed()
	{
		
		AlertDialog.Builder logoutDialog = new AlertDialog.Builder(MainTabActivity.this);//(FragmentPage2.this);  
        logoutDialog.setTitle("确定退出IN-CHAT吗？");  
        logoutDialog.setIcon(R.drawable.icon_home_nor);  
        logoutDialog.setPositiveButton("确认", new DialogInterface.OnClickListener()  
                {           
                    @Override    
                    public void onClick(DialogInterface dialog, int which)   
                        {  

                          // 点击“<strong>确认</strong>”后的操作    
                   		XmppTool.closeConnection();
                   		//关闭数据库
                   		InMessageStore.close();
                   		System.exit(0);
                   		android.os.Process.killProcess(android.os.Process.myPid());
                                            
                        }    
                    });    
        logoutDialog.setNegativeButton("返回", new DialogInterface.OnClickListener()   
                        {           
                            @Override    
                            public void onClick(DialogInterface dialog, int which){    
                                // 点击“<strong>返回</strong>”后的操作,这里不设置没有任何操作    
                            }    
                        });
        logoutDialog.show();
        
         
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		/*MenuItem add = menu.add(0, 1, 0, "Save");  
        MenuItem open = menu.add(0, 2, 1, "Open");  
        MenuItem close = menu.add(0, 3, 2, "Close");  
        add.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);  
        open.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);  
        close.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM); */ 
	    return super.onCreateOptionsMenu(menu);
	}
	
}