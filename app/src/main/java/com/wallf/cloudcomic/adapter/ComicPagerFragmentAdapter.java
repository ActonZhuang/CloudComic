package com.wallf.cloudcomic.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;


import com.wallf.cloudcomic.entity.ComicPage;
import com.wallf.cloudcomic.fragment.ComicPageFragment;

import java.util.List;

/**
 * Created by Fei.Liu on 2016/1/8.
 */
public class ComicPagerFragmentAdapter extends FragmentStatePagerAdapter {


    private final List<ComicPage> mPages;

    public ComicPagerFragmentAdapter(FragmentManager fm, List<ComicPage> files) {
        super(fm);
        this.mPages = files;
    }

    @Override
    public int getCount() {
        return mPages.size();
    }

    public void removeItemAt(int location) {
        this.mPages.remove(location);

    }

    SparseArray<ComicPageFragment> registeredFragments = new SparseArray<>();

    @Override
    public Fragment getItem(int position) {
        ComicPage page = null;
        if (mPages.size() > 0) {
            page = mPages.get(position);
        }

        ComicPageFragment frg = ComicPageFragment.newInstance(page);

        return frg;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ComicPageFragment fragment = (ComicPageFragment) super.instantiateItem(container,
                position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public void clearRegisteredFragments() {
        registeredFragments.clear();
    }

    // @Override
    // public boolean isViewFromObject(View view, Object object) {
    // return view == object;
    // }

    public ComicPageFragment getFragment(int position) {
        return registeredFragments.get(position);
    }

    public ComicPage getComicPage(int position) {
        if (position >= 0 && position < mPages.size())
            return this.mPages.get(position);

        return null;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
