package pro.chinasoft.layout;

import java.util.ArrayList;
import java.util.List;

import pro.chinasoft.activity.R;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class SmileyRelativeLayout extends RelativeLayout implements
		OnItemClickListener, OnClickListener {
	
	private ViewPager pa;
	private List<View> views;
	private ArrayList<ImageView> pointViews;
	
	/** 游标显示布局 */
	private LinearLayout layout_point;
	
	public SmileyRelativeLayout(Context context) {
		super(context);
		initPaper();
		Init_Point();
	}
	
	public SmileyRelativeLayout(Context context, AttributeSet attrs){
		super(context, attrs);
		initPaper();
		Init_Point();
	}
	public SmileyRelativeLayout(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		initPaper();
		Init_Point();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_send:
			this.setVisibility(View.GONE);
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
	}
	
	
	private void initPaper(){
		 pa= (ViewPager) findViewById(R.id.vp_contains);
		 views = new ArrayList<View>();
		 LayoutInflater mLi = LayoutInflater.from(this.getContext());
		 views.add(mLi.inflate(R.layout.item_smiley, null));
		 views.add(mLi.inflate(R.layout.item_smiley, null));
		 views.add(mLi.inflate(R.layout.item_smiley, null));

		 PagerAdapter mPagerAdapter = new PagerAdapter(){

			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return false;
			}
			public Object instantiateItem(View container, int position) {
			                ((ViewPager)container).addView(views.get(position));
			                return views.get(position);
			}
			 @Override 
	         public void destroyItem(View arg0, int arg1, Object arg2) { 
	             ((ViewPager) arg0).removeView(views.get(arg1)); 
	         } 


		 };
		 pa.setAdapter(mPagerAdapter);
		
	}
	
	private void Init_Point() {
		layout_point = (LinearLayout) findViewById(R.id.iv_image);
		pointViews = new ArrayList<ImageView>();
		ImageView imageView;
		for (int i = 0; i < views.size(); i++) {
			imageView = new ImageView(this.getContext());
			imageView.setBackgroundResource(R.drawable.d1);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));
			layoutParams.leftMargin = 10;
			layoutParams.rightMargin = 10;
			layoutParams.width = 8;
			layoutParams.height = 8;
			layout_point.addView(imageView, layoutParams);
			if (i == 0 || i == views.size() - 1) {
				imageView.setVisibility(View.GONE);
			}else{
				imageView.setBackgroundResource(R.drawable.d2);
			}
			pointViews.add(imageView);

		}
	}
	

}
