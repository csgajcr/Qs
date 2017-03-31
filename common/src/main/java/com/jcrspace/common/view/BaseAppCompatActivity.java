package com.jcrspace.common.view;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jcrspace.common.Qs;
import com.jcrspace.common.lander.UserLander;
import com.jcrspace.common.utils.AppManager;

/**
 * Created by jiangchaoren on 2017/3/10.
 */

public class BaseAppCompatActivity extends AppCompatActivity {

    private UserLander lander;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        AppManager.getInstance().addActivity(this);
        lander = Qs.lander;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        AppManager.getInstance().removeActivity(this);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void runOnBackgroundThread(Runnable runnable){
        Qs.threadPool.submit(runnable);
    }

    protected UserLander getLander(){
        return lander;
    }


}
