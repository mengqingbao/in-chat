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
import pro.chinasoft.menu.BottonMenuView;
import pro.chinasoft.menu.dialog.MenuPopupWindow;
import pro.chinasoft.model.InMessage;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class InChatActivity extends Activity{

	private ListView listView;


	private InMessageArrayAdapter iadapter;

	private ChatManager cm;
	private Chat chat;
	private String friendId;
	private String userId;
	private List<InMessage> msgs;
	private MenuPopupWindow menuView;
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
		// 聊天记录的listview 的数据原
	
		msgs = InMessageStore.getMessages(userId, friendId, 0,
				5, this);
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
	  // popupWindow = makePopupWindow(MainTabActivity.this); 
	   menuView = new MenuPopupWindow(InChatActivity.this, null);
	   
	   //findViewById(R.id.simfyer_btn).setOnClickListener(this);
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

	//发送消息
	public void sendMessage(View view) {
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
	
	public void operateMenu(View v){
		  LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
		   View layout = inflater.inflate(R.id.simfyer_btn,null);
		   menuView.showAtLocation(layout, Gravity.TOP|Gravity.CENTER, 150, 340);
	}
	
	public void cancel(View v){
		this.unregisterReceiver(mReceiver);
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
		System.out.println("refresh"+msgs.size());
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

}
