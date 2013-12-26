package pro.chinasoft.menu.dialog;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
    
public class MenuPopupWindow{
	


	private PopupWindow mPopup;
	private Context mContext;
	
	/**
	 * 图片资源
	 */
	private int[] mImgRes = new int[0];
	
	/**
	 * 文字资源
	 */
	private String[] mTexts = new String[0];
	
	/**
	 * 菜单背景
	 */
	private int mBg;
	
	/**
	 * 菜单显示消失的动画
	 */
	private int mAnimStyle;
	
	/**
	 * 文字大小
	 */
	private float mTxtSize = -1;
	
	/**
	 * 文字颜色
	 */
	private int mTxtColor = -1;
	
	/**
	 * 文本相对图片的对齐方式
	 */
	private int mAlign = MenuItem.TEXT_BOTTOM;
	
	/**
	 * 菜单项选中的效果
	 */
	private int mSelector = -1;
	
	/**
	 * 菜单的宽
	 */
	private int mWidth;
	
	/**
	 * 菜单的高
	 */
	private int mHeight;
	
	/**
	 * 存放菜单项
	 */
	private GridView mGridView;
	
	/**
	 * 设置文字的最大长度，超过则会以"..."替代
	 */
	private int mMaxStrLength = 4;
	
	/**
	 * 菜单项点击事件
	 */
	private OnMenuItemClickListener mListener;
	
	/**
	 * 是否对过长字符串采取优化
	 */
	private boolean mIsOptimizeTxt = true;
	
	/**
	 * 保存菜单项
	 */
	private ArrayList<MenuItem> mMenuItems = new ArrayList<MenuItem>();
	
	public MenuPopupWindow(Context context)
	{
		if (null == context)
		{
			throw new IllegalArgumentException();
		}
		
		mContext = context;
	}
	
	/**
	 * 设置图片资源
	 * @param imgRes
	 */
	public void setImageRes(int[] imgRes)
	{
		if (null != imgRes)
		{
			mImgRes = imgRes;
		}
	}
	
	/**
	 * 设置菜单背景
	 * @param bgRes
	 */
	public void setBackgroundResource(int bgRes)
	{
		mBg = bgRes;
	}
	
	/**
	 * 设置菜单项的文字
	 * @param txtRes 资源数组
	 */
	public void setText(int[] txtRes)
	{
		if (null == txtRes)
		{
			return;
		}
		
		final Resources res = mContext.getResources();
		final int length = txtRes.length;
		mTexts = new String[length];
		for (int i = 0; i < length; i++)
		{
			mTexts[i] = res.getString(txtRes[i]);
		}
	}
	
	/**
	 * 设置菜单项的文字
	 * @param txtRes
	 */
	public void setText(String[] texts)
	{
		mTexts = texts;
	}
	
	/**
	 * 设置文字大小
	 * @param txtSize
	 */
	public void setTextSize(float txtSize)
	{
		mTxtSize = txtSize;
	}
	
	/**
	 * 设置文字颜色
	 * @param color
	 */
	public void setTextColor(int color)
	{
		mTxtColor = color;
	}
	
	/**
	 * 设置文本相对图片的对齐方式
	 * @param align
	 */
	public void setTextAlign(int align)
	{
		mAlign = align;
	}
	
	/**
	 * 允许文本的最大长度
	 * @param length
	 */
	public void setMaxTextLength(int length)
	{
		mMaxStrLength = length;
	}
	
	/**
	 * 设置是否对过长文本进行优化
	 * @param isOptimize
	 */
	public void isOptimizeText(boolean isOptimize)
	{
		mIsOptimizeTxt = isOptimize;
	}
	
	/**
	 * 设置菜单动画
	 * @param animStyle
	 */
	public void setAnimStyle(int animStyle)
	{
		mAnimStyle = animStyle;
	}
	
	/**
	 * 设置菜单的宽度
	 * @param width
	 */
	public void setWidth(int width)
	{
		mWidth = width;
	}
	
	/**
	 * 设置菜单的高度
	 * @param height
	 */
	public void setHeight(int height)
	{
		mHeight = height;
	}
	
	/**
	 * 设置菜单被项被选中的效果
	 * @param selector
	 */
	public void setSelector(int selector)
	{
		mSelector = selector;
	}
	
	/**
	 * 设置装载菜单内容的载体
	 * @param view
	 */
	public void setMenuConentView(GridView view)
	{
		mGridView = view;
	}
	
	/**
	 * 显示菜单
	 * @return 显示成功返回true, 失败返回false
	 */
	public boolean show()
	{
		if (hide())
		{
			return false;
		}
		
		final Context context = mContext;
		final int length = mImgRes.length;
		final int txtLength = mTexts.length;
		Point point = new Point();
		if (length != 0 && txtLength != 0)
		{
			Point p1 = getTextMaxDimenstion(mTexts);
			Point p2 = getImageMaxDimension(mImgRes);
			switch (mAlign)
			{
			case MenuItem.TEXT_BOTTOM:
			case MenuItem.TEXT_TOP:
				point.x = Math.max(p1.x, p2.x);
				point.y = p1.y + p2.y;
				break;
			case MenuItem.TEXT_LEFT:
			case MenuItem.TEXT_RIGHT:
				point.x = p1.x + p2.x;
				point.y = Math.max(p1.y, p2.y);
				break;
			}
		}
		else
		{
			if (length != 0)
			{
				point = getImageMaxDimension(mImgRes);
			}
			else if (txtLength != 0)
			{
				point = getTextMaxDimenstion(mTexts);
			}
		}
		
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		int width = mWidth == 0 ? metrics.widthPixels : mWidth;
		float density = metrics.density;
		int imgWidth = point.x;
		int height = point.y;
		// 除去5dp的间距一行所能摆放图片的个数
		int columns = (int) ((width - 5 * density) / (imgWidth + 5 * density));
		
		int leng = length != 0 ? length : txtLength;
		int rows = columns == 0 ? 0 : leng / columns;
		if (columns * rows < leng)
		{
			rows += 1;
		}
		
		final LinearLayout layout = initLayout(context);
		GridView gridView = mGridView;
		if (null == gridView)
		{
			gridView = getContentView(context, columns);
		}
		else
		{
			setContentViewListener(gridView);
		}
		
		layout.addView(gridView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		
		// TODO 对高度进行修正
		int h = 0;
		if (mAlign == MenuItem.TEXT_LEFT || mAlign == MenuItem.TEXT_RIGHT)
		{
			h = (int) (height * rows + 5 * density);
		}
		else if (mAlign == MenuItem.TEXT_BOTTOM || mAlign == MenuItem.TEXT_TOP)
		{
			h = (int) ((height + 5 * density) * rows);
		}
		if (txtLength != 0)
		{
			h += 6 * density;
		}
		
		mPopup = new PopupWindow(context);
		mPopup.setWidth(width);
		mPopup.setHeight(mHeight == 0 ? h : mHeight);
		mPopup.setContentView(layout);
		mPopup.setFocusable(true);
		mPopup.setOutsideTouchable(true);
		mPopup.setTouchable(true);
		// 设置背景为null，就不会出现黑色背景，按返回键PopupWindow就会消失
		mPopup.setBackgroundDrawable(null);
		if (mAnimStyle != 0)
		{
			mPopup.setAnimationStyle(mAnimStyle);
		}
		mPopup.showAtLocation(layout, Gravity.BOTTOM | Gravity.CENTER, 0, 0);
		return true;
	}
	
	private LinearLayout initLayout(Context context)
	{
		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setFadingEdgeLength(0);
		layout.setGravity(Gravity.CENTER);
		
		layout.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				if (event.getAction() == MotionEvent.ACTION_DOWN)
				{
					hide();
				}
				return false;
			}
			
		});
		
		return layout;
	}
	
	/**
	 * 初始数据，将数据加载到对应的View中
	 */
	private void initData()
	{
		MenuItem item = new MenuItem(mContext);
		item.setTextAlign(mAlign);
		item.setTextColor(mTxtColor);
		item.setTextSize(mTxtColor);
		int txtLength = mTexts.length;
		int imgLength = mImgRes.length;
		if (txtLength != 0 && imgLength != 0)
		{
			for (int i = 0; i < imgLength; i++)
			{
				MenuItem menuItem = new MenuItem(mContext, item);
				menuItem.setImageRes(mImgRes[i]);
				menuItem.setText(mTexts[i]);
				mMenuItems.add(menuItem);
			}
			
		}
		else
		{
			if (txtLength != 0)
			{
				for (int i = 0; i < txtLength; i++)
				{
					MenuItem menuItem = new MenuItem(mContext, item);
					menuItem.setText(mTexts[i]);
					mMenuItems.add(menuItem);
				}
			}
			else if (imgLength != 0)
			{
				for (int i = 0; i < imgLength; i++)
				{
					MenuItem menuItem = new MenuItem(mContext, item);
					menuItem.setImageRes(mImgRes[i]);
					mMenuItems.add(menuItem);
				}
			}
		}
	}
	
	/**
	 * 初始化菜单内容组件
	 * @param context
	 * @param columns 菜单的列数
	 * @return
	 */
	private GridView getContentView(Context context, int columns)
	{
		if (mMenuItems.isEmpty())
		{
			initData();
		}
		
		if (null != mGridView)
		{
			return mGridView;
		}
		GridView gridView = new GridView(context);
		gridView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		gridView.setAdapter(new MenuAdapter(mMenuItems));
		gridView.setVerticalSpacing(0);
		gridView.setNumColumns(columns);
		gridView.setGravity(Gravity.CENTER);
		gridView.setVerticalScrollBarEnabled(false);
		if (mBg != 0)
		{
			gridView.setBackgroundResource(mBg);
		}
		if (mSelector != -1)
		{
			gridView.setSelector(mSelector);
		}
		gridView.setHorizontalScrollBarEnabled(false);
		setContentViewListener(gridView);
		return gridView;
	}
	
	/**
	 * 注册事件
	 * @param gridView
	 */
	private void setContentViewListener(GridView gridView)
	{
		if (null == gridView.getOnItemClickListener())
		{
			gridView.setOnItemClickListener(new OnItemClickListener()
			{
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id)
				{
					if (null != mListener)
					{
						mListener.onMenuItemClick(parent, view, position);
					}
					hide();
				}
				
			});
		}
		
		gridView.setOnKeyListener(new OnKeyListener()
		{
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event)
			{
				if (event.getAction() == KeyEvent.ACTION_DOWN)
				{
					switch (keyCode)
					{
					case KeyEvent.KEYCODE_BACK:
					case KeyEvent.KEYCODE_MENU:
						hide();
						break;
					}
				}
				return false;
			}
			
		});
	}
	
	/**
	 * 获取所有图片的最大的宽和高
	 * @param imgRes
	 * @return
	 */
	private Point getImageMaxDimension(int[] imgRes)
	{
		final Point point = new Point();
		for (int i = 0, length = imgRes.length; i < length; i++)
		{
			Bitmap tmp = BitmapFactory.decodeResource(mContext.getResources(), imgRes[i]);
			int width = tmp.getWidth();
			int height = tmp.getHeight();
			tmp.recycle();
			tmp = null;
			if (point.x < width)
			{
				point.x = width;
			}
			if (point.y < height)
			{
				point.y = height;
			}
		}
		
		return point;
	}
	
	/**
	 * 计算文本的最大长度
	 * @param txts
	 * @return
	 */
	private Point getTextMaxDimenstion(String[] txts)
	{
		final Point point = new Point();
		final Rect bounds = new Rect();
		final Paint paint = new Paint();
		float size = mTxtSize != -1 ? mTxtSize : mContext.getResources().getDisplayMetrics().density * 16;
		paint.setTextSize(size);
		paint.setColor(mTxtColor != -1 ? mTxtColor : Color.BLACK);
		if (mIsOptimizeTxt)
		{
			for (int i = 0, length = txts.length; i < length; i++)
			{
				String str = txts[i];
				if (null == str)
				{
					str = "";
				}
				else if (str.length() > mMaxStrLength)
				{
					// 对字符串长度进行控制
					str = new StringBuilder().append(str.substring(0, mMaxStrLength)).append("...").toString();
				}
				
				txts[i] = str;
				paint.getTextBounds(str, 0, str.length(), bounds);
				compareDimension(point, bounds.width(), bounds.height());
			}
		}
		else
		{
			for (int i = 0, length = txts.length; i < length; i++)
			{
				String str = txts[i];
				if (null == str)
				{
					str = "";
				}
				
				txts[i] = str;
				paint.getTextBounds(str, 0, str.length(), bounds);
				compareDimension(point, bounds.width(), bounds.height());
			}
		}
		return point;
	}
	
	/**
	 * 比较并改变最大尺寸
	 * @param point 保存最大尺寸的对象
	 * @param width 宽
	 * @param height 高
	 */
	private void compareDimension(Point point, int width, int height)
	{
		if (point.x < width)
		{
			point.x = width;
		}
		
		if (point.y < height)
		{
			point.y = height;
		}
	}
	
	/**
	 * 隐藏菜单
	 * @return 隐藏成功返回true，失败返回false
	 */
	public boolean hide()
	{
		if (null != mPopup && mPopup.isShowing())
		{
			mPopup.dismiss();
			mPopup = null;
			if (null != mListener)
			{
				mListener.hideMenu();
			}
			return true;
		}
		return false;
	}
	
	public void dismiss()
	{
		mMenuItems.clear();
		mGridView = null;
		mTexts = new String[0];
		mImgRes = new int[0];
		mWidth = 0;
		mHeight = 0;
	}
	
	/**
	 * 设置菜单项被选中监听器
	 * @param listener
	 */
	public void setOnMenuItemClickListener(OnMenuItemClickListener listener)
	{
		mListener = listener;
	}
	
	/**
	 * 菜单项目选中监听器
	 * @author maylian.mei
	 *
	 */
	public interface OnMenuItemClickListener
	{
		/**
		 * 菜单项被点击的会调用的方法
		 * @param parent
		 * @param view
		 * @param position
		 */
		public void onMenuItemClick(AdapterView<?> parent, View view, int position);
		
		/**
		 * 菜单隐藏会调用的方法
		 */
		public void hideMenu();
	}

}  