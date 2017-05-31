package com.jcrspace.qsmain.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.jcrspace.common.Qs;
import com.jcrspace.common.config.ActivityUrls;
import com.jcrspace.common.config.QsCommonConfig;
import com.jcrspace.common.lander.UserLander;
import com.jcrspace.common.router.UrlBuilder;
import com.jcrspace.qsmain.R;

import org.xutils.x;

import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.Bmob;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        //隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //隐藏状态栏
        //定义全屏参数
        int flag= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //设置当前窗体为全屏显示
        window.setFlags(flag, flag);
        setContentView(R.layout.activity_loading);
        Bmob.initialize(this, QsCommonConfig.BMOB_APP_ID);

        initLander();
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                UrlBuilder.build(LoadingActivity.this, ActivityUrls.MAIN).startActivity();
                finish();
            }
        };
        timer.schedule(task,3000);


    }

    private void initLander(){
        if (Qs.getConfigSharedPreferences().getBoolean(QsCommonConfig.SP_IS_AUTO_LOGIN,true)){
            String autoLoginUserId = Qs.getConfigSharedPreferences().getString(QsCommonConfig.SP_AUTO_LOGIN_NAME,"");
            if (autoLoginUserId.equals("")){//无自动登录用户
                UserLander lander = new UserLander(Qs.app,UserLander.DEFAULT_LOCAL_USER_ID);
                Qs.lander = lander;
            } else { //有自动登录用户
                UserLander lander = new UserLander(Qs.app,autoLoginUserId);
                Qs.lander = lander;
            }
        } else {
            UserLander lander = new UserLander(Qs.app,UserLander.DEFAULT_LOCAL_USER_ID);
            Qs.lander = lander;
        }

    }
}
