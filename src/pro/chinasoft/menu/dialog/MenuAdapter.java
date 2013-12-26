package pro.chinasoft.menu.dialog;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 
 * @author maylian7700@126.com
 *
 */
public class MenuAdapter extends BaseAdapter
{
	private ArrayList<MenuItem> mMenuItems;
	public MenuAdapter(ArrayList<MenuItem> menuItems)
	{
		mMenuItems = menuItems;
	}
	
	@Override
	public int getCount()
	{
		return mMenuItems.size();
	}

	@Override
	public Object getItem(int position)
	{
		return position;
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (null == convertView)
		{
			convertView = mMenuItems.get(position).getView();
		}
		
		return convertView;
	}

}
