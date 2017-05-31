package com.jcrspace.common;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.jcrspace.common.config.QsCommonConfig;
import com.jcrspace.common.lander.UserLander;
import com.jcrspace.common.utils.QsThreadPool;

import java.util.concurrent.ExecutorService;

/**
 * Created by jiangchaoren on 2017/2/27.
 */

public class Qs {
    public static final ExecutorService threadPool = QsThreadPool.newPauseThreadPool(); //用户执行后台任务
    public static UserLander lander; //全局用户数据管理器
    public static Application app;
    private static SharedPreferences configSharedPreferences;

    public static SharedPreferences getConfigSharedPreferences(){
        configSharedPreferences = app.getSharedPreferences(QsCommonConfig.CONFIG_SP_FILE_NAME,Context.MODE_PRIVATE);
        return configSharedPreferences;
    }

    public static void init(Application application){
        app = application;
    }

}
