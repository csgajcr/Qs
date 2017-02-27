package com.jcrspace.common;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.jcrspace.common.config.QsCommonConfig;

/**
 * Created by jiangchaoren on 2017/2/27.
 */

public class QsApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG && QsCommonConfig.DEBUG){
            openDevMode();
        }


    }

    private void openDevMode(){
        Stetho.initializeWithDefaults(this);
    }
}
