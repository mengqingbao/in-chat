package pro.chinasoft.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
/**
 * 
 ******************************************
 * @author 廖乃波
 * @文件名称	:  ViewPagerAdapter.java
 * @创建时间	: 2013-1-27 下午02:35:27
 * @文件描述	: ViewPager 数据填充器，切记做其他操作！！！只填充View！！！！
 ******************************************
 */
public class ViewPagerAdapter extends PagerAdapter {

    private List<View> pageViews;

    public ViewPagerAdapter(List<View> pageViews) {
        super();
        this.pageViews=pageViews;
    }

    // 显示数目
    @Override
    public int getCount() {
        return pageViews.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager)arg0).removeView(pageViews.get(arg1));
    }

    /***
     * 获取每一个item�?类于listview中的getview
     */
    @Override
    public Object instantiateItem(View arg0, int arg1) {
        ((ViewPager)arg0).addView(pageViews.get(arg1));
        return pageViews.get(arg1);
    }
}