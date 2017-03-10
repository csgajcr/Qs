package com.jcrspace.common;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.jcrspace.common.config.QsCommonConfig;

import org.xutils.x;

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

        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.

    }

    private void openDevMode(){
        Stetho.initializeWithDefaults(this);
    }
}
