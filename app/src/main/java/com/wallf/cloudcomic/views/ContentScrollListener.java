package com.wallf.cloudcomic.views;

/**
 * Created by Fei.Liu on 2015/12/14.
 */
public interface ContentScrollListener {
    public final static int UP = 1;
    public final static int DOWN = 2;

    public void onScroll(int direction, int offset);
}

