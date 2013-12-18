package pro.chinasoft.component;

import java.util.List;

import pro.chinasoft.activity.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class InArrayAdapter<T> extends ArrayAdapter<T> {
	



	public InArrayAdapter(Context context, int resource) {
		super(context, resource);
		this.init(context, resource);
	}

	private List<T> objects;

/*	public InArrayAdapter(Context context, int resource,
			int textViewResourceId, List<T> objects) {
		super(context, resource, textViewResourceId, objects);
		this.init(context, resource,objects);
	}*/

	LayoutInflater mInflater;


   private void init(Context context, int resource) {
       mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       
   }
   
	@Override
	public View getView(int position, View convertView, 
    		ViewGroup parent) { 
			View view;
			LayoutInflater View;
		    view = mInflater.inflate(R.layout.list_item, parent, false);
    		TextView label=(TextView)view.findViewById(R.id.list_item_text);
    		T item = getItem(position);
    		label.setText((String)item);
    		ImageView icon=(ImageView)view.findViewById(R.id.list_item_image); 
    		//icon.setImageResource(R.drawable.delete); 
    		return view; 
    		} 


/*	public InArrayAdapter(Context context, int resource, int textViewResourceId, T[] objects){
		super(context, resource, textViewResourceId, objects);
		this.init(context, resource,objects);
	}*/
	
}
