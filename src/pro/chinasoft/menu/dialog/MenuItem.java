package pro.chinasoft.menu.dialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 菜单�?
 * @author maylian7700@126.com
 *
 */
public class MenuItem
{
	private LinearLayout mLayout;
	
	/**
	 * 文本在图片的下方显示
	 */
	public static final int TEXT_BOTTOM = 0x0;
	
	/**
	 * 文本在图片的上方显示
	 */
	public static final int TEXT_TOP = 0x1;
	
	/**
	 * 文本在图片的左边显示
	 */
	public static final int TEXT_LEFT = 0x2;
	
	/**
	 * 文本在图片的右边显示
	 */
	public static final int TEXT_RIGHT = 0x3;
	
	/**
	 * 文本的对齐方�?
	 */
	private int mAlign = TEXT_BOTTOM;
	
	/**
	 * 文本
	 */
	private String mText;
	
	/**
	 * 文本颜色
	 */
	private int mTextColor;
	
	/**
	 * 文本大小
	 */
	private int mTextSize;
	
	/**
	 * 图片的资源ID
	 */
	private int mImgRes;
	private Context mContext;
	
	public MenuItem(Context context)
	{
		this(context, 0, null, 0, 0, TEXT_BOTTOM);
	}
	
	public MenuItem(Context context, int imgRes, String text, int textColor, int textSize, int align)
	{
		mImgRes = imgRes;
		mText = text;
		mTextColor = textColor;
		mTextSize = textSize;
		mAlign = align;
		mContext = context;
	}
	
	public MenuItem(Context context, MenuItem item)
	{
		this(context, 0, null, item.getTextColor(), item.getTextSize(), item.getAlign());
	}
	
	/**
	 * 初始化菜单项
	 * @param context
	 */
	private void initlayout()
	{
		Context context = mContext;
		mLayout = new LinearLayout(context);
		mLayout.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		mLayout.setGravity(Gravity.CENTER);
		TextView textView = getTextView(context);
		ImageView imageView = getImageView(context);
		if (null != textView && null != imageView)
		{
			Point point = getImageDimension(context, mImgRes);
			switch (mAlign)
			{
			case TEXT_BOTTOM: // 文本居下
				mLayout.setOrientation(LinearLayout.VERTICAL);
				mLayout.addView(imageView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, point.y));
				mLayout.addView(textView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
				break;
			case TEXT_TOP:// 文本居上
				mLayout.setOrientation(LinearLayout.VERTICAL);
				mLayout.addView(textView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
				mLayout.addView(imageView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, point.y));
				break;
			case TEXT_LEFT:// 文本居左
				mLayout.setOrientation(LinearLayout.HORIZONTAL);
				mLayout.addView(textView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
				mLayout.addView(imageView, new ViewGroup.LayoutParams(point.x, ViewGroup.LayoutParams.MATCH_PARENT));
				break;
			case TEXT_RIGHT:// 文本居右
				mLayout.setOrientation(LinearLayout.HORIZONTAL);
				mLayout.addView(imageView, new ViewGroup.LayoutParams(point.x, ViewGroup.LayoutParams.MATCH_PARENT));
				mLayout.addView(textView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
				break;
			}
		}
		else
		{
			if (null != textView)
			{
				mLayout.addView(textView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
			}
			else if (null != imageView)
			{
				Point point = getImageDimension(context, mImgRes);
				mLayout.addView(imageView, new ViewGroup.LayoutParams(point.x, point.y));
			}
		}
	}
	
	/**
	 * 获取图片的尺�?
	 * @param context
	 * @param res
	 * @return
	 */
	private Point getImageDimension(Context context, int res)
	{
		Point point = new Point();
		Bitmap bm = BitmapFactory.decodeResource(context.getResources(), mImgRes);
		point.x = bm.getWidth();
		point.y = bm.getHeight();
		bm.recycle();
		bm = null;
		return point;
	}
	
	/**
	 * 设置图片资源
	 * @param imgRes
	 */
	public void setImageRes(int imgRes)
	{
		mImgRes = imgRes;
	}
	
	/**
	 * 设置文本大小
	 * @param size
	 */
	public void setTextSize(int size)
	{
		mTextSize = size;
	}
	
	/**
	 * 设置文本颜色
	 * @param color
	 */
	public void setTextColor(int color)
	{
		mTextColor = color;
	}
	
	/**
	 * 设置文本内容
	 * @param text
	 */
	public void setText(String text)
	{
		mText = text;
	}
	
	/**
	 * 设置文本的对齐方�?
	 * @param align
	 */
	public void setTextAlign(int align)
	{
		mAlign = align;
	}
	
	public String getText()
	{
		return mText;
	}
	
	public int getTextColor()
	{
		return mTextColor;
	}
	
	public int getTextSize()
	{
		return mTextSize;
	}
	
	/**
	 * 创建TextView
	 * @param context
	 * @return
	 */
	private TextView getTextView(Context context)
	{
		if (TextUtils.isEmpty(mText))
		{
			return null;
		}
		TextView txtView = new TextView(context);
		if (mTextColor != 0)
		{
			txtView.setTextColor(mTextColor);
		}
		
		if (mTextSize != 0)
		{
			txtView.setTextSize(mTextSize);
		}
		txtView.setText(mText);
		txtView.setGravity(Gravity.CENTER);
		return txtView;
	}
	
	public int getAlign()
	{
		return mAlign;
	}
	
	/**
	 * 创建ImageView
	 * @param context
	 * @return
	 */
	private ImageView getImageView(Context context)
	{
		if (mImgRes == 0)
		{
			return null;
		}
		ImageView view = new ImageView(context);
		view.setImageResource(mImgRes);
		return view;
	}
	
	/**
	 * 获取菜单项目的内�?
	 * @return
	 */
	public View getView()
	{
		initlayout();
		return mLayout;
	}

}
