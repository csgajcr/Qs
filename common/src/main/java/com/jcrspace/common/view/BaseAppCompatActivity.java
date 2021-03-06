package com.jcrspace.common.view;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.EditText;

import com.jcrspace.common.Qs;
import com.jcrspace.common.lander.UserLander;
import com.jcrspace.common.utils.AppManager;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by jiangchaoren on 2017/3/10.
 */

public abstract class BaseAppCompatActivity extends AppCompatActivity {

    private UserLander lander;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        AppManager.getInstance().addActivity(this);
        lander = Qs.lander;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected abstract int getContentView();

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

    /**
     * EditText获取焦点并显示软键盘
     */
    public void showSoftInputFromWindow(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

}
