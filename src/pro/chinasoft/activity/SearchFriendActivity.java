package pro.chinasoft.activity;

import java.util.Iterator;

import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.ReportedData.Row;
import org.jivesoftware.smackx.search.UserSearchManager;
import org.xmpp.client.util.XmppTool;

import pro.chinasoft.component.InUserArrayAdapter;
import pro.chinasoft.dialog.LoadingDialog;
import pro.chinasoft.model.InUser;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class SearchFriendActivity extends Activity {
	private ArrayAdapter<InUser> friends = null;
	LoadingDialog dialog; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_friend_activity);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem add = menu.add(0, 1, 0, "查找");
		add.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// handle item selection
		switch (item.getItemId()) {
		case 1:
			System.out.println(item.getItemId());
			processThread();//search();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void search() {
		
        //业务处理
		EditText txt = (EditText) this.findViewById(R.id.search_user_name);

		String keyword = txt.getText().toString();
		System.out.println("search " + keyword);
		try {
			UserSearchManager search = new UserSearchManager(
					XmppTool.getConnection());
			Form searchForm = search.getSearchForm("search."
					+ XmppTool.getConnection().getServiceName());

			Form answerForm = searchForm.createAnswerForm();
			answerForm.setAnswer("Username", true);

			answerForm.setAnswer("search", keyword);

			org.jivesoftware.smackx.ReportedData data = search
					.getSearchResults(answerForm, "search."
							+ XmppTool.getConnection().getServiceName());

			if (data.getRows() != null) {

				ListView listView = (ListView) this
						.findViewById(R.id.search_friend_list);
				friends = new InUserArrayAdapter(this.getApplicationContext(),
						R.layout.search_friend_activity);
				Iterator<Row> it = data.getRows();
				while (it.hasNext()) {
					Row row = it.next();
					Iterator iterator = row.getValues("jid");
					if (iterator.hasNext()) {
						String value = iterator.next().toString();
						InUser iu = new InUser();
						iu.setNick(value);
						friends.add(iu);
					}

				}
				listView.setAdapter(friends);
			}
			System.out.println("result ");
		} catch (Exception e) {
			Toast.makeText(this.getApplicationContext(), "超找失败	！",
					Toast.LENGTH_SHORT).show();
			System.out.println(e.getMessage());
			// Toast.makeText(this,e.getMessage()+" "+e.getClass().toString(),
			// Toast.LENGTH_SHORT).show();
		}finally{
			dialog.cancel();
		}
	}

	// 等待画面==========================================
	// 声明变量
	public ProgressDialog pd;
	// 定义Handler对象
	final Handler handler = new Handler() {
		@Override
		// 当有消息发送出来的时候就执行Handler的这个方法
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			/*// 只要执行到这里就关闭对话框
			ListView listView = (ListView) SearchFriendActivity.this
					.findViewById(R.id.search_friend_list);

			listView.setAdapter(friends);
			
			pd.dismiss();*/
			dialog.cancel();
		}
	};

	private void processThread() {
		
		//等待界面
	    dialog = new LoadingDialog(this);  
        dialog.setCanceledOnTouchOutside(false);  
        dialog.show();
        friends = new InUserArrayAdapter(this.getApplicationContext(),
				R.layout.search_friend_activity);
		EditText txt = (EditText) SearchFriendActivity.this
				.findViewById(R.id.search_user_name);
		String keyword = txt.getText().toString();
		SearchThread t=new SearchThread();
		t.setKeyword(keyword);
		t.start();

	}
	
	class SearchThread extends Thread{
		private String keyword;
		@Override
		public void run() {
			// 在这里执行长耗时方法
			try {
				UserSearchManager search = new UserSearchManager(XmppTool
						.getConnection());
				Form searchForm = search.getSearchForm("search."
						+ XmppTool.getConnection().getServiceName());

				Form answerForm = searchForm.createAnswerForm();
				answerForm.setAnswer("Username", true);

				answerForm.setAnswer("search", keyword);

				org.jivesoftware.smackx.ReportedData data = search
						.getSearchResults(answerForm, "search."
								+ XmppTool.getConnection().getServiceName());

				if (data.getRows() != null) {

					Iterator<Row> it = data.getRows();
					if (it.hasNext()) {
						Row row = it.next();
						Iterator iterator = row.getValues("jid");
						if (iterator.hasNext()) {
							handler.sendEmptyMessage(0);
							String value = iterator.next().toString();
							System.out.println("result:"+value);
							Intent intent = new Intent();
							intent.setClass(SearchFriendActivity.this, AddFriendActivity.class);
							intent.putExtra("USERID", value);
							SearchFriendActivity.this.startActivity(intent);
						}
						
					}
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
				// Toast.makeText(this,e.getMessage()+" "+e.getClass().toString(),
				// Toast.LENGTH_SHORT).show();
			}
		}
		public void setKeyword(String keyword) {
			this.keyword = keyword;
		}
		
		
	}

}
