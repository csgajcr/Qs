package com.jcrspace.qsmain.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jcrspace.common.config.QsCommonConfig;
import com.jcrspace.qsmain.R;

import cn.bmob.v3.Bmob;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        Bmob.initialize(this, QsCommonConfig.BMOB_APP_ID);
    }
}
