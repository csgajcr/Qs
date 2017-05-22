package com.jcrspace.ui_account.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.jcrspace.common.lander.UserLander;
import com.jcrspace.common.view.BaseAppCompatActivity;
import com.jcrspace.manager_account.AccountManager;
import com.jcrspace.manager_account.event.LogoutEvent;
import com.jcrspace.ui_account.R;

import org.greenrobot.eventbus.EventBus;

public class SettingActivity extends BaseAppCompatActivity {

    private Switch swAutoLogin;
    private Switch swSync;
    private Switch swRecommend;
    private Button btnExitLogin;
    private AccountManager accountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.setting);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        accountManager = AccountManager.getInstance(getLander());
        initView();
        initListener();
        initData();

    }

    private void initView(){
        swAutoLogin = (Switch) findViewById(R.id.sw_auto_login);
        swRecommend = (Switch) findViewById(R.id.sw_recommend);
        swSync = (Switch) findViewById(R.id.sw_sync);
        btnExitLogin = (Button) findViewById(R.id.btn_exit_login);

    }

    private void initListener(){
        swAutoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        swRecommend.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        swSync.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        btnExitLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountManager.logout();
                EventBus.getDefault().post(new LogoutEvent());
            }
        });
    }

    private void initData(){
        /**
         * 未登录则不显示退出登录
         */
        if (getLander().getId().equals(UserLander.DEFAULT_LOCAL_USER_ID)){
            btnExitLogin.setVisibility(View.GONE);
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_setting;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
