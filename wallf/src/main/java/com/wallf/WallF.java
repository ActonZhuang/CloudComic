package com.wallf;

import android.app.Application;
import android.text.TextUtils;

import org.acra.ACRA;
import org.acra.ACRAConfiguration;

/**
 * @author acton
 */
public final class WallF {

    public static final String VERSION = "2.0.0";

    private static WallF sInstance;

    public static WallF singleton() {
        if (sInstance == null) {
            sInstance = new WallF();
        }
        return sInstance;
    }

    private WallF() {
    }

    public void init(Application application, String toEmail) {
        if (!TextUtils.isEmpty(toEmail)) {
            ACRAConfiguration defaultConfig = ACRA.getNewDefaultConfig(application);
            defaultConfig.setMailTo(toEmail);
            ACRA.init(application, defaultConfig);
        }
    }

    public void destroy() {

    }
}
