package pro.chinasoft.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.XMPPException;
import org.xmpp.client.util.InMessageStore;
import org.xmpp.client.util.XmppTool;

import pro.chinasoft.component.InMessageArrayAdapter;
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
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class InChatActivity extends Activity implements OnClickListener{

	private ListView listView;
	private View faceView;
	private View menuView;
	private ViewPager pa;
	private InMessageArrayAdapter iadapter;
	private List<View> views;

	private ChatManager cm;
	private Chat chat;
	private String friendId;
	private String userId;
	private List<InMessage> msgs;
	private Button sendBtn;
	private Button btn1;
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
	   sendBtn.setOnClickListener(this);
	   btn1=(Button) this.findViewById(R.id.button1);
	   btn1.setOnClickListener(this);
	   smileyBtn1=(Button) this.findViewById(R.id.simfyer_btn);
	   smileyBtn1.setOnClickListener(this);
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
		System.out.println("注销监听 关闭数据库");
		this.finish();
	}

	private void saveOrUpdate(String userId, String firendId, String content,String type) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("content", content);
		map.put("userId", userId);
		map.put("friendId", friendId);
		map.put("type", type);
		InMessageStore.add("InMessage", map, this);
	}

	//type:true message from yourself,false:msg from friend
	private void refresh(String content,boolean type) {
		InMessage msg = new InMessage();
		msg.setContent(content);
		msg.setReviceDate(new Date());
		msg.setType(type);
		msgs.add(msg);
		iadapter.notifyDataSetChanged();
	}
	BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 刷新主Activity界面
			String friendId = intent.getStringExtra("friendId");
			if (friendId.equals(friendId)) {
				String content = intent.getStringExtra("content");
				//saveOrUpdate(userId, friendId, content);
				refresh(content,true);
				
			}
			System.out.println("//"+friendId);
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
			System.out.println(R.id.btn_send);
			sendMessage();	
			break;			
		}
	}
	
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
			}
			faceView.setVisibility(View.VISIBLE);
		}
	}
	
	private void initPaper(){
		 pa= (ViewPager) findViewById(R.id.vp_contains);
		 views = new ArrayList<View>();		 LayoutInflater mLi = LayoutInflater.from(this);
		 views.add(mLi.inflate(R.layout.list_item, null));
		 views.add(mLi.inflate(R.layout.item_face, null));
		 views.add(mLi.inflate(R.layout.list_item, null));

		 PagerAdapter mPagerAdapter = new PagerAdapter(){

			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return false;
			}
			public Object instantiateItem(View container, int position) {
			                ((ViewPager)container).addView(views.get(position));
			                return views.get(position);
			}
			 @Override 
	         public void destroyItem(View arg0, int arg1, Object arg2) { 
	             ((ViewPager) arg0).removeView(views.get(arg1)); 
	         } 


		 };
		 pa.setAdapter(mPagerAdapter);
		
	}
	
	
	private void smileyAction(){
		if (faceView.getVisibility() == View.VISIBLE) {
			faceView.setVisibility(View.GONE);
		}
		if(menuView.getVisibility()==View.GONE){
			menuView.setVisibility(View.VISIBLE);
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
		saveOrUpdate(userId, friendId, message,"false");
		try {
			chat.sendMessage(message);
		} catch (XMPPException e) {
			System.out.println(e.getMessage());
		}
		// 发送完消息后清空原来的数据
		text.setText("");
	}
	
	public void cancel(View v){
		this.unregisterReceiver(mReceiver);
		this.finish();
	}

}
