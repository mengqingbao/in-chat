package pro.chinasoft.component;

import java.util.List;

import pro.chinasoft.activity.R;
import pro.chinasoft.model.InMessage;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class InMessageArrayAdapter extends ArrayAdapter<InMessage> {

	public InMessageArrayAdapter(Context context, int resource,
			List<InMessage> objects) {
		super(context, resource, objects);
		this.init(context, resource);
	}
	public InMessageArrayAdapter(Context context, int resource) {
		super(context, resource);
		this.init(context, resource);
	}

	LayoutInflater mInflater;

	private void init(Context context, int resource) {
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		InMessage item = getItem(position);
		View view = mInflater.inflate(R.layout.in_message_list_item, parent, false);
		TextView label = (TextView) view.findViewById(R.id.nick);
		label.setText(item.getContent());
		ImageView icon=(ImageView)view.findViewById(R.id.avatar);
		//icon.setImageResource(R.drawable.icon_home_nor);
		return view;
	}
	
}
