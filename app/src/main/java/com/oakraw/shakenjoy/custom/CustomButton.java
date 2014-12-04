package com.oakraw.shakenjoy.custom;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by Rawipol on 11/27/14 AD.
 */
public class CustomButton extends Button {
    public CustomButton(Context context) {
        super(context);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), TextViewImpact.font));

    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), TextViewImpact.font));

    }

    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), TextViewImpact.font));

    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), TextViewImpact.font));

    }
}
