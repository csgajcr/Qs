package com.jcrspace.qsmain.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.jcrspace.common.Qs;
import com.jcrspace.common.config.ActivityUrls;
import com.jcrspace.common.config.QsCommonConfig;
import com.jcrspace.common.lander.UserLander;
import com.jcrspace.common.router.UrlBuilder;
import com.jcrspace.qsmain.R;

import org.xutils.x;

import cn.bmob.v3.Bmob;

public class LoadingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        Bmob.initialize(this, QsCommonConfig.BMOB_APP_ID);
        
        findViewById(R.id.btn_jump).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UrlBuilder.build(LoadingActivity.this, ActivityUrls.MAIN).startActivity();
            }
        });

        initLander();
    }

    private void initLander(){
        String autoLoginUserId = Qs.getConfigSharedPreferences().getString("auto_login","");
        if (autoLoginUserId.equals("")){//无自动登录用户
            UserLander lander = new UserLander(Qs.app,"-1");
            Qs.lander = lander;
        } else { //有自动登录用户
            UserLander lander = new UserLander(Qs.app,autoLoginUserId);
            Qs.lander = lander;
        }
    }
}
