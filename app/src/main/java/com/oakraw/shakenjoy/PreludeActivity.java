package com.oakraw.shakenjoy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.oakraw.shakenjoy.adapter.ViewPagerAdapter;
import com.viewpagerindicator.UnderlinePageIndicator;


public class PreludeActivity extends Activity implements ViewPager.OnPageChangeListener{

    private FrameLayout bg;
    private bg_color first_page;
    private bg_color second_page;
    private bg_color third_page;

    class bg_color{
        public int red;
        public int green;
        public int blue;

        bg_color(int red, int green, int blue) {
            this.red = red;
            this.green = green;
            this.blue = blue;
        }

        public int getColor(){
            return Color.rgb(red,green,blue);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prelude);

        ViewPager viewpager = (ViewPager)findViewById(R.id.viewPager);
        viewpager.setAdapter(new ViewPagerAdapter(this));
        viewpager.setOnPageChangeListener(this);

        UnderlinePageIndicator indicator = (UnderlinePageIndicator)findViewById(R.id.titles);
        indicator.setViewPager(viewpager);
        indicator.setOnPageChangeListener(this);

        //indicator.setSelectedColor(0xFFCC0000);
        indicator.setFades(false);

        //bg = (FrameLayout) findViewById(R.id.background);
        first_page = new bg_color(220,117,97);
        second_page = new bg_color(69,123,143);
        third_page = new bg_color(102, 175, 157);

        ((Button)findViewById(R.id.btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MyActivity.class));
                finish();
            }
        });

        SharedPreferences prefs = this.getSharedPreferences(
                "shake_n_joy", Context.MODE_PRIVATE);

        prefs.edit().putBoolean("isFirstTime",false).apply();
    }

    float h_l = 1;
    float s_l = 1;
    float v_l = 1;
    float h_p = 1;
    float s_p = 1;
    float v_p = 1;

    float[] hsvp;
    float[] hsvf;
    float[] hsvs;

    float temp_v;


    @Override
    public void onPageScrolled(int i, float v, int i2) {
        /*if(i == 0 && v == 0.0){
            bg.setBackgroundColor(first_page.getColor());
            hsvf = new float[3];
            Color.colorToHSV(first_page.getColor(),hsvf);
            hsvs = new float[3];
            Color.colorToHSV(second_page.getColor(),hsvs);

            h_l = hsvs[0] - hsvf[0];
            s_l = hsvs[1] - hsvf[1];
            v_l = hsvs[2] - hsvf[2];

        }

        if(i == 1 && v == 0.0){

            bg.setBackgroundColor(second_page.getColor());
            hsvp = new float[3];
            Color.colorToHSV(first_page.getColor(),hsvf);
            hsvf = new float[3];
            Color.colorToHSV(second_page.getColor(),hsvf);
            hsvs = new float[3];
            Color.colorToHSV(third_page.getColor(),hsvs);

            h_l = hsvs[0] - hsvf[0];
            s_l = hsvs[1] - hsvf[1];
            v_l = hsvs[2] - hsvf[2];

            h_p = hsvp[0] - hsvf[0];
            s_p = hsvp[1] - hsvf[1];
            v_p = hsvp[2] - hsvf[2];

        }

        if(i == 2 && v == 0.0){
            bg.setBackgroundColor(third_page.getColor());
            hsvf = new float[3];
            Color.colorToHSV(third_page.getColor(),hsvf);
            hsvs = new float[3];
            Color.colorToHSV(second_page.getColor(),hsvs);

            h_l = hsvs[0] - hsvf[0];
            s_l = hsvs[1] - hsvf[1];
            v_l = hsvs[2] - hsvf[2];

        }

        if(v != 0.0){

            float h = h_l * v;
            float s = s_l * v;
            float vl = v_l * v;
            int color = Color.HSVToColor(new float[]{hsvf[0] + h, hsvf[1] + s, hsvf[2] + vl});
            bg.setBackgroundColor(color);

            //Log.d("OakTag", h + " " + s + " " + vl);

        }

        if(i == 1 && v != 0.0) {
            if (temp_v < v) {
                float h = h_l * v;
                float s = s_l * v;
                float vl = v_l * v;
                int color = Color.HSVToColor(new float[]{hsvf[0] + h, hsvf[1] + s, hsvf[2] + vl});
                bg.setBackgroundColor(color);
                Log.d("swipe","to right");
            }else{
                float h = h_p * v;
                float s = s_p * v;
                float vl = v_p * v;
                int color = Color.HSVToColor(new float[]{hsvf[0] - h, hsvf[1] - s, hsvf[2] - vl});
                bg.setBackgroundColor(color);
                Log.d("swipe","to left");

            }
        }


        if(i == 0 && v != 0.0) {
            if (temp_v > v) {
                v = 1 - v;
                float h = h_p * v;
                float s = s_p * v;
                float vl = v_p * v;
                int color = Color.HSVToColor(new float[]{hsvf[0] + h, hsvf[1] + s, hsvf[2] + vl});
                bg.setBackgroundColor(color);
                Log.d("swipe","to left");

            }
        }

        temp_v = v;

        Log.d("OakTag", i + " " + v + " " + i2);
*/
    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
