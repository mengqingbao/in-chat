package pro.chinasoft.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.XMPPException;
import org.xmpp.client.util.DateUtil;
import org.xmpp.client.util.InMessageStore;
import org.xmpp.client.util.XmppTool;

import pro.chinasoft.adapter.SmileyAdapter;
import pro.chinasoft.adapter.ViewPagerAdapter;
import pro.chinasoft.component.InMessageArrayAdapter;
import pro.chinasoft.listener.SmileyOnItemClickListener;
import pro.chinasoft.model.InMessage;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class InChatActivity extends Activity implements OnClickListener{

	private ListView listView;
	private View faceView;
	private View menuView;
	private ViewPager pa;
	private InMessageArrayAdapter iadapter;
	private List<View> views;
	private ArrayList<ImageView> pointViews;
	/** 游标显示布局 */
	private LinearLayout layout_point;

	private ChatManager cm;
	private Chat chat;
	private String friendId;
	private String userId;
	private List<InMessage> msgs;
	private Button sendBtn;
	private Button btn1,camerabtn;
	private Button smileyBtn1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.in_chat_activity);

		listView = (ListView) this.findViewById(R.id.listview);
		String defaultValue = getResources().getString(
				R.string.username_store_key);
		friendId = getIntent().getStringExtra(defaultValue);
		SharedPreferences sharedPref = this.getSharedPreferences(
				getString(R.string.in_chat_store), Context.MODE_PRIVATE);
		userId = sharedPref.getString(getString(R.string.username_store_key),
				"");
		// get chat history data from db
		msgs = InMessageStore.getMessages(userId, friendId, 0,5, this);
		if(msgs==null){
			msgs=new ArrayList<InMessage>();
		}
		iadapter = new InMessageArrayAdapter(this,msgs);
		listView.setAdapter(iadapter);
		cm = XmppTool.getConnection().getChatManager();
		chat = cm.createChat(friendId, null);

		// 注册广播接收器，接受最新信息
		IntentFilter intentFilter = new IntentFilter(
				"pro.chinasoft.activity.InChatActivity");
		registerReceiver(mReceiver, intentFilter);
		
		init();
	}

	private void init(){
	   //this.findViewById(R.id.rl_bottom_more).setVisibility(View.GONE);
	   TextView tv = (TextView) this.findViewById(R.id.in_chat_activity_title);
	   tv.setText(friendId);
	   faceView=this.findViewById(R.id.in_chat_activity_smiley_ll_facechoose);
	   menuView=this.findViewById(R.id.in_chat_activity_smiley_ll_menu);
	   sendBtn=(Button) this.findViewById(R.id.btn_send);
	   camerabtn=(Button) this.findViewById(R.id.button3);
	   camerabtn.setOnClickListener(this);
	   sendBtn.setOnClickListener(this);
	   btn1=(Button) this.findViewById(R.id.button1);
	   btn1.setOnClickListener(this);
	   smileyBtn1=(Button) this.findViewById(R.id.simfyer_btn);
	   smileyBtn1.setOnClickListener(this);
	   //listView.setOnTouchListener(this);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.in_chat, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		unregisterReceiver(mReceiver);
		InMessageStore.close();
		this.finish();
	}

	//type:true message from yourself,false:msg from friend
	private void refresh(String content,boolean type) {
		InMessage msg = new InMessage();
		msg.setContent(content);
		msg.setCreateDate(new Date());
		msg.setType(type);
		msgs.add(msg);
		iadapter.notifyDataSetChanged();
		listView.setSelection(msgs.size()-1);
	}
	BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 刷新主Activity界面
			String friendId = intent.getStringExtra("friendId");
			if (friendId.equals(friendId)) {
				String content = intent.getStringExtra("content");
				refresh(content,true);
				
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1:
			btn1Action();
			break;
		case R.id.simfyer_btn:
			smileyAction();	
			break;
		case R.id.btn_send:
			sendMessage();	
			break;	
		case R.id.listview:
			if (faceView.getVisibility() == View.VISIBLE) {
				faceView.setVisibility(View.GONE);
			}
			if(menuView.getVisibility()==View.VISIBLE){
				menuView.setVisibility(View.GONE);
			}
			break;
		case R.id.button3:
			Intent intent = new Intent();
			intent.setClass(this, CameraActivity.class);
			startActivity(intent);
			break;
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (menuView != null && menuView.getVisibility()==View.VISIBLE) {
				menuView.setVisibility(View.GONE);
		}
		if (faceView != null && faceView.getVisibility()==View.VISIBLE){
			faceView.setVisibility(View.GONE);
		}

		return super.onTouchEvent(event);

		}
	
	//react to buttons's on clicks below
	private void btn1Action(){
		if(menuView.getVisibility()==View.VISIBLE){
			menuView.setVisibility(View.GONE);
		}
		if (faceView.getVisibility() == View.GONE) {
			if(pa==null){
				initPaper();
				Init_Point();
			}
			faceView.setVisibility(View.VISIBLE);
		}
	}
	
	private void initPaper(){
		 pa= (ViewPager) findViewById(R.id.vp_contains);
		 views = new ArrayList<View>();		 //GridView gridView = (GridView) findViewById(R.layout.in_chat_activity_face);///(GridView)this.getLayoutInflater().inflate(R.layout.in_chat_activity_face,null);
		 for(int i=0;i<3;i++){
			 GridView gridView = new GridView(this);
			 SmileyAdapter adapter = new SmileyAdapter(this,i);
			 gridView.setOnItemClickListener(new SmileyOnItemClickListener(this));
			 gridView.setNumColumns(7);
			 gridView.setBackgroundColor(Color.TRANSPARENT);
			 gridView.setHorizontalSpacing(1);
			 gridView.setVerticalSpacing(1);
			 gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
			 gridView.setCacheColorHint(0);
			 gridView.setPadding(5, 0, 5, 0);
			 gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
			 gridView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT));
			 gridView.setGravity(Gravity.CENTER);
			 gridView.setAdapter(adapter);
			 views.add(gridView);
		 }
		 pa.setAdapter(new ViewPagerAdapter(views));
		 pa.setOnPageChangeListener(new OnPageChangeListener() {
				@Override
				public void onPageSelected(int position) {
					draw_Point(position);
					pa.setCurrentItem(position);
				}
				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
				}
				@Override
				public void onPageScrollStateChanged(int arg0) {
				}
			});
		 
		
	}
	
	private void Init_Point() {
		layout_point = (LinearLayout) findViewById(R.id.iv_image);
		pointViews = new ArrayList<ImageView>();
		ImageView imageView;
		System.out.println(views.size()+"init_point");
		for (int i = 0; i < views.size(); i++) {
			imageView = new ImageView(this);
			imageView.setBackgroundResource(R.drawable.d1);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));
			layoutParams.leftMargin = 10;
			layoutParams.rightMargin = 10;
			layoutParams.width = 8;
			layoutParams.height = 8;
			layout_point.addView(imageView, layoutParams);
			if (i == 0) {
				imageView.setBackgroundResource(R.drawable.d2);
			}else{
				imageView.setBackgroundResource(R.drawable.d1);
			}
			pointViews.add(imageView);

		}
	}
	
	//redraw point when pages change.
	public void draw_Point(int index) {
		for (int i = 0; i < pointViews.size(); i++) {
			if (index == i) {
				pointViews.get(i).setBackgroundResource(R.drawable.d2);
			} else {
				pointViews.get(i).setBackgroundResource(R.drawable.d1);
			}
		}
	}
	
	private void smileyAction(){
		if (faceView.getVisibility() == View.VISIBLE) {
			faceView.setVisibility(View.GONE);
		}
		if(menuView.getVisibility()==View.GONE){
			menuView.setVisibility(View.VISIBLE);
		}else{
			menuView.setVisibility(View.GONE);
		}
	}
	//发送消息
	public void sendMessage() {
		EditText text = (EditText) this.findViewById(R.id.et_sendmessage);
		String message = text.getText().toString();
		if(message==null||message==""){
			return;
		}
		// 刷新内容
		refresh(message,false);
		// 保存到sqlite
		InMessageStore.saveOrUpdate(userId, friendId, message,false,this);
		try {
			chat.sendMessage(message);
		} catch (XMPPException e) {
			System.out.println(e.getMessage()+"exception");
		}
		// 发送完消息后清空原来的数据
		text.setText("");
	}
	
	public void cancel(View v){
		this.unregisterReceiver(mReceiver);
		this.finish();
	}

	
}
