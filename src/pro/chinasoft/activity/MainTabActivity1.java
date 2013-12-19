package pro.chinasoft.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;
import org.xmpp.client.util.InMessageStore;
import org.xmpp.client.util.XmppTool;

import pro.chinasoft.activity.FragmentPage1;
import pro.chinasoft.activity.FragmentPage2;
import pro.chinasoft.activity.FragmentPage3;
import pro.chinasoft.activity.FragmentPage4;
import pro.chinasoft.activity.R;
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
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

/**
 * @author yangyu
 *	�����������Զ���TabHost
 */
public class MainTabActivity1 extends FragmentActivity{	
	//����FragmentTabHost����
	private FragmentTabHost mTabHost;
	
	//����һ������
	private LayoutInflater layoutInflater;
		
	//�������������Fragment����
	private Class fragmentArray[] = {FragmentPage1.class,FragmentPage2.class,FragmentPage3.class,FragmentPage4.class};
	
	//������������Ű�ťͼƬ
	private int mImageViewArray[] = {R.drawable.tab_message_btn,R.drawable.tab_selfinfo_btn,
									 R.drawable.tab_square_btn,R.drawable.tab_more_btn};
	
	//Tabѡ�������
	private String mTextviewArray[] = {"��Ϣ", "����", "�ճ�", "����"};
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
		
       //��Ϣ����
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
							Log.v("--tags--", "--tags-form--���ܵ� "+friendId);
							Log.v("--tags--", "--tags-message--��Ϣ "+message.getBody());
						}
						try {
							Map<String, String> map = new HashMap<String, String>();
							map.put("content", message.getBody());
							map.put("userId", userid);
							map.put("friendId", friendId);
							InMessageStore.add("InMessage", map,MainTabActivity1.this);
						} catch (Exception e) {
							Log.i("--tags--", e.getMessage());
						}
						//���͹㲥֪ͨ��������ҳ������
						Intent intent = new Intent("pro.chinasoft.activity.InChatActivity");
						intent.putExtra("content", message.getBody());
						intent.putExtra("friendId", friendId);
				        sendBroadcast(intent);
					}
				});
			}
		});
       
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
	 * ��ʼ�����
	 */
	private void initView(){
		//ʵ�������ֶ���
		layoutInflater = LayoutInflater.from(this);
				
		//ʵ����TabHost���󣬵õ�TabHost
		mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);	
		
		//�õ�fragment�ĸ���
		int count = fragmentArray.length;	
				
		for(int i = 0; i < count; i++){	
			//Ϊÿһ��Tab��ť����ͼ�ꡢ���ֺ�����
			TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));
			//��Tab��ť���ӽ�Tabѡ���
			mTabHost.addTab(tabSpec, fragmentArray[i], null);
			//����Tab��ť�ı���
			mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_tab_background);
		}
	}
				
	/**
	 * ��Tab��ť����ͼ�������
	 */
	private View getTabItemView(int index){
		View view = layoutInflater.inflate(R.layout.tab_item_view, null);
		
		ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
		imageView.setImageResource(mImageViewArray[index]);
		
		TextView textView = (TextView) view.findViewById(R.id.textview);		
		textView.setText(mTextviewArray[index]);
	
		return view;
	}
	
//==============================�����ķָ���================================================
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) 
		{
							
			switch (msg.what) {
			case 1:
				//��ȡ��Ϣ����ʾ
				String[] args = (String[]) msg.obj;
				listMsg.add(new Msg(args[0], args[1], args[2], args[3]));
				//ˢ��������
				//adapter.notifyDataSetChanged();
				break;			
			/*case 2:
				//����������
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
				
				//��ʾ��
				AlertDialog.Builder builder = new AlertDialog.Builder(FormClient.this);
				
				builder.setTitle("������")
						.setCancelable(false)
						.setMessage("�Ƿ�����ļ���"+file.getName()+"?")
						.setPositiveButton("����",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										try 
										{
											infiletransfer.recieveFile(file);
										} 
										catch (XMPPException e)
										{
											Toast.makeText(FormClient.this,"����ʧ��!",Toast.LENGTH_SHORT).show();
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
													Toast.makeText(FormClient.this,"�������!",Toast.LENGTH_SHORT).show();
												}
											}
										};
										timer.scheduleAtFixedRate(updateProgessBar, 10L, 10L);
										dialog.dismiss();
									}
								})
						.setNegativeButton("ȡ��",
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

	//�˳�
	@Override
	public void onBackPressed()
	{
		
		AlertDialog.Builder logoutDialog = new AlertDialog.Builder(MainTabActivity1.this);//(FragmentPage2.this);  
        logoutDialog.setTitle("ȷ���˳�IN-CHAT��");  
        logoutDialog.setIcon(R.drawable.icon_home_nor);  
        logoutDialog.setPositiveButton("ȷ��", new DialogInterface.OnClickListener()  
                {           
                    @Override    
                    public void onClick(DialogInterface dialog, int which)   
                        {  

                          // �����<strong>ȷ��</strong>����Ĳ���    
                   		XmppTool.closeConnection();
                   		//�ر����ݿ�
                   		InMessageStore.close();
                   		System.exit(0);
                   		android.os.Process.killProcess(android.os.Process.myPid());
                                            
                        }    
                    });    
        logoutDialog.setNegativeButton("����", new DialogInterface.OnClickListener()   
                        {           
                            @Override    
                            public void onClick(DialogInterface dialog, int which){    
                                // �����<strong>����</strong>����Ĳ���,���ﲻ����û���κβ���    
                            }    
                        });
        logoutDialog.show();
        
         
	}

/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.fragment_2_menu, menu);
	    return super.onCreateOptionsMenu(menu);
	}*/
	
}