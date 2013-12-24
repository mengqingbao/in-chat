package pro.chinasoft.activity;

import java.util.Collection;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.xmpp.client.util.XmppTool;

import pro.chinasoft.component.InUserArrayAdapter;
import pro.chinasoft.model.InUser;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class FragmentPage2 extends Fragment{

	private ArrayAdapter<InUser> friends = null;
	private Button btn;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_2, container, false);
		btn=(Button) view.findViewById(R.id.addFriend);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(FragmentPage2.this.getActivity(), SearchFriendActivity.class);
				startActivity(intent);
			} 
			
		});
		ListView listView = (ListView) view.findViewById(R.id.fragment_2_list);
		friends = new InUserArrayAdapter(getActivity(),
				R.layout.in_user_list_item);

		XMPPConnection conn = XmppTool.getConnection();
		Roster roster = conn.getRoster();
		Collection<RosterEntry> it = roster.getEntries();
		for (RosterEntry rosterEnter : it) {
			InUser iu = new InUser();
			iu.setNick(rosterEnter.getUser());
			friends.add(iu);
		}

		/*
		 * for (int i = 0; i < 10; i++) { InUser iu = new InUser();
		 * iu.setNick("ÕÅÈý"); friends.add(iu); listView.setAdapter(files); }
		 */
		listView.setAdapter(friends);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int postion, long arg3) {
				InUser user = friends.getItem(postion);
				Intent intent = new Intent();
				intent.setClass(getActivity(), InChatActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("userid", user.getNick());
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		return view;
	}
}
