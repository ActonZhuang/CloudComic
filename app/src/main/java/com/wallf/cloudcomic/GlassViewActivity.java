package com.wallf.cloudcomic;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.wallf.cloudcomic.views.GlassView;

/**
 * @author acton
 */
public class GlassViewActivity extends AppCompatActivity {

    private int mCurImgResId = R.mipmap.lolipop_bg;

    private GlassView mTopGlassView;
    private GlassView mBottomGlassView;

    private ImageView mBgImg;
    private SeekBar mSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glassview);

        mTopGlassView = (GlassView) findViewById(R.id.top_glass_view);
        mBottomGlassView = (GlassView) findViewById(R.id.bottom_glass_view);

        mBgImg = (ImageView) findViewById(R.id.bg_img);
        mSeekBar = (SeekBar) findViewById(R.id.seek_bar);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // allow blur radius is 0 < r <= 25
                if (progress > 0) {
                    mTopGlassView.setBlurRadius(progress);
                    mBottomGlassView.setBlurRadius(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_glassview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_img_switch) {
            if (mCurImgResId == R.mipmap.lolipop_bg) {
                mCurImgResId = R.mipmap.jellybean_bg;
            } else {
                mCurImgResId = R.mipmap.lolipop_bg;
            }
            mBgImg.setImageResource(mCurImgResId);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
