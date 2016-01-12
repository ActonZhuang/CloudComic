package com.wallf.cloudcomic.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.SeekBar;
import android.widget.Toast;

import com.wallf.cloudcomic.R;
import com.wallf.cloudcomic.adapter.ComicPagerFragmentAdapter;
import com.wallf.cloudcomic.entity.ComicPage;
import com.wallf.cloudcomic.fragment.ComicPageFragment;
import com.wallf.cloudcomic.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fei.Liu on 2016/1/8.
 */
public class ComicPagerActivity extends FragmentActivity implements ComicPageFragment.ComicPageFragmentListener {


    ViewPager mPager;
    ComicPagerFragmentAdapter mAdapter;
    SeekBar mSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pager);
        setupViewPager();


    }

    final static String URI = "http://n.kukudm.com/newkuku/2014/201412/1205b/%E6%BA%90%E5%90%9B%E7%89%A9%E8%AF%AD/131-149/131001GG3.jpg";

    @Override
    protected void onStart() {
        super.onStart();
        ScreenUtil.hideSystemUI(this);
    }

    void setupViewPager() {
        mSeekBar = (SeekBar) findViewById(R.id.seekBar);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mSeekBar.setProgress(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mPager.setCurrentItem(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //set up data
        List<ComicPage> pages = new ArrayList<>();

        for (int i = 0; i <= 2; i++) {
            ComicPage page = new ComicPage();
            page.setUrl(URI);
            pages.add(page);
        }
        mAdapter = new ComicPagerFragmentAdapter(getSupportFragmentManager(), pages);
        mPager.setAdapter(mAdapter);

        //set up seek bar min/max value
        mSeekBar.setMax(mAdapter.getCount() - 1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCenterAreaClicked() {
        Toast.makeText(this, "Center", Toast.LENGTH_SHORT).show();
        ScreenUtil.hideSystemUI(this);
        switchOptionalLayout(!isOptionalLayoutHidding);
    }

    @Override
    public void onLeftAreaClicked() {
//        Toast.makeText(this, "Left", Toast.LENGTH_SHORT).show();
        int currentItem = mPager.getCurrentItem();
        if (currentItem <= 0)
            return;
        else {
            mPager.setCurrentItem(currentItem - 1);
        }
        ScreenUtil.hideSystemUI(this);
    }

    @Override
    public void onRightAreaClicked() {
//        Toast.makeText(this, "Right", Toast.LENGTH_SHORT).show();
        int currentItem = mPager.getCurrentItem();
        if (currentItem >= mAdapter.getCount() - 1)
            return;
        else {
            mPager.setCurrentItem(currentItem + 1);
        }
        ScreenUtil.hideSystemUI(this);
    }

    boolean isOptionalLayoutHidding = false;

    void switchOptionalLayout(final boolean hide) {

        int downAnimResId = (hide) ? R.anim.translate_down_out : R.anim.translate_down_in;
        Animation anim = AnimationUtils.loadAnimation(this, downAnimResId);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isOptionalLayoutHidding = hide;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mSeekBar.startAnimation(anim);
    }
}
