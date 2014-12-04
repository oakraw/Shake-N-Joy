package com.oakraw.shakenjoy.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.oakraw.shakenjoy.R;

/**
 * Created by Rawipol on 11/17/14 AD.
 */
public class ViewPagerAdapter extends PagerAdapter {


    private Activity activity;

    public ViewPagerAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == (View)o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        Log.d("myTag", "position" + position);

        LinearLayout layout;
        switch (position){
            case 0: layout = (LinearLayout)inflater.inflate(R.layout.fragment1, null); break;
            case 1: layout = (LinearLayout)inflater.inflate(R.layout.fragment2, null); break;
            default: layout = (LinearLayout)inflater.inflate(R.layout.fragment3, null);
        }

        ((ViewPager)container).addView(layout,0);

        return layout;
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);

    }


}
