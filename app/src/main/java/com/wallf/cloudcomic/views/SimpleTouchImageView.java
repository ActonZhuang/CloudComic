package com.wallf.cloudcomic.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by Fei.Liu on 2016/1/12.
 */
public class SimpleTouchImageView extends ImageView {

    public SimpleTouchImageView(Context context) {
        super(context);
        init(context);
    }

    public SimpleTouchImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    void init(Context context) {
        detector = new GestureDetector(context, new GestureTap());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        return super.onTouchEvent(event);
        detector.onTouchEvent(event);
        return true;
    }

    GestureDetector detector;

    class GestureTap extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (tapListener != null)
                return tapListener.onSingleTap(e);

            return super.onSingleTapUp(e);
        }
    }

    OnSimpleTouchedListener tapListener;

    public void setOnSimpleTouchedListener(OnSimpleTouchedListener listener) {
        this.tapListener = listener;
    }

    public interface OnSimpleTouchedListener {
        public boolean onSingleTap(MotionEvent event);
    }
}
