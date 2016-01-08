package com.wallf.cloudcomic.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

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
public class ComicPagerActivityFragment extends FragmentActivity implements ComicPageFragment.ComicPageFragmentListener {


    ViewPager mPager;
    ComicPagerFragmentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pager);
        setupViewPager();
    }

    @Override
    protected void onStart() {
        super.onStart();
        ScreenUtil.hideSystemUI(this);
    }

    void setupViewPager() {

        mPager = (ViewPager) findViewById(R.id.pager);
        List<ComicPage> pages = new ArrayList<>();
        ComicPage page = new ComicPage();
        page.setUrl("http://n.kukudm.com/newkuku/2014/201412/1205b/%E6%BA%90%E5%90%9B%E7%89%A9%E8%AF%AD/131-149/131001GG3.jpg");
        pages.add(page);

        mAdapter = new ComicPagerFragmentAdapter(getSupportFragmentManager(), pages);
        mPager.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onViewClicked() {
        ScreenUtil.hideSystemUI(this);

    }
}
