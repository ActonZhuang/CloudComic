package com.wallf.cloudcomic;

import android.app.Application;

import com.wallf.WallF;

/**
 * @author acton
 */
public class CloudComicApp extends Application {

    @Override
    public void onCreate() {
        WallF.singleton().init(this, "zhuangt2004@163.com");
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        WallF.singleton().destroy();
        super.onTerminate();
    }
}
