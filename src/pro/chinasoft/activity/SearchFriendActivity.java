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
import android.view.View;
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

	// loading dialog==========================================
	// define variable
	public ProgressDialog pd;
	// define Handler Ojbect
	final Handler handler = new Handler() {
		@Override
		// execute the method of Handler,when message came.
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			dialog.cancel();
		}
	};

	public void search(View v) {
		
		//loading dialog
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
	
	public void cancel(View v){
		this.finish();
	}
	
	class SearchThread extends Thread{
		private String keyword;
		@Override
		public void run() {
			// here execute processions take long time.
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
