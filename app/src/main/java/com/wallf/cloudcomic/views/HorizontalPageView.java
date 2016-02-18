package com.wallf.cloudcomic.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author acton
 */
public class HorizontalPageView extends ViewGroup {
    public HorizontalPageView(Context context) {
        super(context);
    }

    public HorizontalPageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizontalPageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HorizontalPageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        
    }

}

