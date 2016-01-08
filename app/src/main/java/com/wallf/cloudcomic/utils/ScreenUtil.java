package com.wallf.cloudcomic.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Created by Fei.Liu on 2016/1/8.
 */
public class ScreenUtil {

    static int wPX = 0, hPX = 0;
    public static int getWpx(Context context) {
        if (wPX == 0) {
            DisplayMetrics dm = context.getResources()
                    .getDisplayMetrics();
            wPX = dm.widthPixels;
            hPX = dm.heightPixels;
        }
        return wPX;
    }

    public static int getHpx(Context context) {
        if (hPX == 0) {
            DisplayMetrics dm = context.getResources()
                    .getDisplayMetrics();
            wPX = dm.widthPixels;
            hPX = dm.heightPixels;
        }
        return hPX;
    }


    public static void hideSystemUI(Activity activity) {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        activity.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    // This snippet shows the system bars. It does this by removing all the flags
    // except for the ones that make the content appear under the system bars.
    public static void showSystemUI(Activity activity) {
        activity.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

}


