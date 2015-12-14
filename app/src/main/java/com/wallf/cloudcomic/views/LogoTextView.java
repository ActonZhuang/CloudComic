package com.wallf.cloudcomic.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Fei.Liu on 2015/12/11.
 */
public class LogoTextView extends TextView {
    public LogoTextView(Context context) {
        super(context);
        init(context);
    }

    public LogoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LogoTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        Typeface t = Typeface.createFromAsset(context.getAssets(), "Lobster-Regular.ttf");
        this.setTypeface(t);
    }
}

