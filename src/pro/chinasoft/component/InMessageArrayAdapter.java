package pro.chinasoft.component;

import java.util.ArrayList;
import java.util.List;

import org.xmpp.client.util.DateUtil;
import org.xmpp.client.util.FaceConversionUtil;

import pro.chinasoft.activity.R;
import pro.chinasoft.model.InMessage;
import android.content.Context;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class InMessageArrayAdapter extends BaseAdapter {

	private List<InMessage> coll;// 消息对象数组
	private LayoutInflater mInflater;
	private Context context;
	public InMessageArrayAdapter(Context context,List<InMessage> coll){
		if(coll!=null){
		this.coll=coll;
		}else{
			coll=new ArrayList<InMessage>();
		}
		mInflater = LayoutInflater.from(context);
		this.context=context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		InMessage item = coll.get(position);
		System.out.println(item.getCreateDate());
		ViewHolder viewHolder = null;
		if (convertView == null) {
			if (item.isType()) {
				convertView = mInflater.inflate(
						R.layout.chatting_item_msg_text_left, null);
			} else {
				convertView = mInflater.inflate(
						R.layout.chatting_item_msg_text_right, null);
			}
			viewHolder = new ViewHolder();
			viewHolder.tvSendTime = (TextView) convertView
					.findViewById(R.id.tv_sendtime);
			viewHolder.tvUserName = (TextView) convertView
					.findViewById(R.id.tv_username);
			viewHolder.tvContent = (TextView) convertView
					.findViewById(R.id.tv_chatcontent);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.tvSendTime.setText(DateUtil.toString(item.getCreateDate()));
		//viewHolder.tvUserName.setText(item.getInUser().getNick());
		if(!TextUtils.isEmpty(item.getContent())){
		SpannableString spannableString = FaceConversionUtil.getInstace().getExpressionString(context, item.getContent());
		viewHolder.tvContent.setText(spannableString);
		}else{
			viewHolder.tvContent.setText(item.getContent());
		}
		return convertView;
	}
	
	static class ViewHolder {
		public TextView tvSendTime;
		public TextView tvUserName;
		public TextView tvContent;
	}

	@Override
	public int getCount() {
		return coll.size();
	}

	@Override
	public Object getItem(int position) {
		return coll.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}
