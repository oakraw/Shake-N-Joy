package com.oakraw.shakenjoy.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.oakraw.shakenjoy.json.ShopInfo;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BannerAdapter extends PagerAdapter {
    private List<ShopInfo.Venue.Photos.Groups.Items> GalImages;
    Context context;
    Activity activity;
    private String imgSize = "width500";

    public BannerAdapter(Context context, List<ShopInfo.Venue.Photos.Groups.Items> GalImage){
        this.context=context;
        activity = (Activity)context;
        this.GalImages = GalImage;
    }
    @Override
    public int getCount() {
        return GalImages.size();
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        //int padding = context.getResources().getDimensionPixelSize(R.dimen.padding_medium);
        //imageView.setPadding(padding, padding, padding, padding);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Picasso.with(context).load(GalImages.get(position).prefix+imgSize+GalImages.get(position).suffix).into(imageView);
        //imageView.setImageResource(GalImages[position]);
        ((ViewPager) container).addView(imageView, 0);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activity.getActionBar().isShowing()){
                    activity.getActionBar().hide();
                }
                else{
                    activity.getActionBar().show();
                }

            }
        });
        return imageView;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((ImageView) object);
    }
}