package com.oakraw.shakenjoy.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Rawipol on 8/30/14 AD.
 */
public class TextViewRL extends TextView {

    public static final String font = "Roboto-Thin.ttf";

    public TextViewRL(Context context) {
        super(context);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), font));
    }

    public TextViewRL(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),font));

    }

    public TextViewRL(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),font));

    }
}
