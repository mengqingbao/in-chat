package pro.chinasoft.adapter;
import java.util.List;

import org.xmpp.client.util.FaceConversionUtil;

import pro.chinasoft.activity.R;
import pro.chinasoft.model.InSmiley;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class SmileyAdapter extends BaseAdapter {
	
	private LayoutInflater inflater;
	
	private List<InSmiley> simleyList;
	
	private int size;
		
	public SmileyAdapter(Context context, int pageNum){
		 this.inflater=LayoutInflater.from(context);
		 simleyList=FaceConversionUtil.getInstace().parseData((pageNum)*20, (pageNum+1)*20, context);
		 size=simleyList.size();
	}

	@Override
	public int getCount() {
		return size;
	}

	@Override
	public Object getItem(int position) {
		return simleyList.get(position);
	}

	@Override
	public long getItemId(int postion) {
		return postion;
	}

	@Override
	public View getView(int postion, View convertView, ViewGroup vg) {
		 ViewHolder viewHolder=null;
		if(convertView==null){
			viewHolder=new ViewHolder();
			convertView=this.inflater.inflate(R.layout.item_smiley, null);
			viewHolder.iv_face=(ImageView)convertView.findViewById(R.id.item_iv_smiley);
			convertView.setTag(viewHolder);
		} else {
            viewHolder=(ViewHolder)convertView.getTag();
        }
		
		viewHolder.iv_face.setTag(simleyList.get(postion));
        viewHolder.iv_face.setImageResource(simleyList.get(postion).getId());
        
		return convertView;
	}
	
    class ViewHolder {
        public ImageView iv_face;
    }
}
