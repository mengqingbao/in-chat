package pro.chinasoft.activity;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.xmpp.client.util.XmppTool;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;


public class InChatLogin extends Activity implements OnClickListener {


	private EditText useridText, pwdText;
	private LinearLayout layout1, layout2;

	@Override

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.in_chat_login);
		SharedPreferences sharedPref = InChatLogin.this
				.getSharedPreferences(
						getString(R.string.in_chat_store),
						Context.MODE_PRIVATE);
		String username=sharedPref.getString(getString(R.string.username_store_key), null);
		String password=sharedPref.getString(getString(R.string.password_store_key), null);
		// 获取用户和密码
		this.useridText = (EditText) findViewById(R.id.formlogin_userid);
		this.pwdText = (EditText) findViewById(R.id.formlogin_pwd);
		if(username!=null)
		useridText.setText(username);
		if(password!=null){
			pwdText.setText(password);
		}
		// 正在登录
		this.layout1 = (LinearLayout) findViewById(R.id.formlogin_layout1);
		// 登录界面

		this.layout2 = (LinearLayout) findViewById(R.id.formlogin_layout2);

		Button btsave = (Button) findViewById(R.id.formlogin_btsubmit);
		btsave.setOnClickListener(this);
		Button btcancel = (Button) findViewById(R.id.formlogin_btcancel);
		btcancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// 根据ID来进行提交或者取消
		switch (v.getId()) {
		case R.id.formlogin_btsubmit:
			// 取得填入的用户和密码
			final String USERID = this.useridText.getText().toString();
			final String PWD = this.pwdText.getText().toString();

			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					// sendEmptyMessage:发送一条消息
					handler.sendEmptyMessage(1);
					try {
						// 连接
						XmppTool.getConnection().login(USERID, PWD);
						// Log.i("XMPPClient", "Logged in as " +
						// XmppTool.getConnection().getUser());

						// 状态
						Presence presence = new Presence(
								Presence.Type.available);
						XmppTool.getConnection().sendPacket(presence);

						Intent intent = new Intent();
						intent.setClass(InChatLogin.this, MainTabActivity.class);
						intent.putExtra("USERID", USERID);
						// 保存用户名密码到本地

						SharedPreferences sharedPref = InChatLogin.this
								.getSharedPreferences(
										getString(R.string.in_chat_store),
										Context.MODE_PRIVATE);
						
						//将用户名密码保存
						SharedPreferences.Editor editor = sharedPref.edit();
						editor.putString(
								getString(R.string.username_store_key), USERID);
						editor.putString(getString(R.string.password_store_key), PWD);
						editor.commit();
						InChatLogin.this.startActivity(intent);
						
						InChatLogin.this.finish();
					} catch (XMPPException e) {
						XmppTool.closeConnection();

						handler.sendEmptyMessage(2);
					}

				}
			});
			t.start();
			break;
		case R.id.formlogin_btcancel:
			finish();
			break;
		}
	}


	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {

			if (msg.what == 1) {
				layout1.setVisibility(View.VISIBLE);
				layout2.setVisibility(View.GONE);
				

				
			} else if (msg.what == 2) {
				layout1.setVisibility(View.GONE);
				layout2.setVisibility(View.VISIBLE);
				Toast.makeText(InChatLogin.this, "登录失败！", Toast.LENGTH_SHORT)
						.show();
			}
		};
	};
}