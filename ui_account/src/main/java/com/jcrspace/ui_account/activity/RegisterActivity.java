package com.jcrspace.ui_account.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.utils.RegexUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.jcrspace.common.dialog.LoadingDialog;
import com.jcrspace.common.view.BaseAppCompatActivity;
import com.jcrspace.manager_account.event.RegisterCompleteEvent;
import com.jcrspace.ui_account.R;
import com.jcrspace.ui_account.facade.RegisterFacade;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class RegisterActivity extends BaseAppCompatActivity {

    private EditText etMobile;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private Button btnRegister;
    private LoadingDialog loadingDialog;

    private RegisterFacade facade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
        initListener();
        initData();
    }

    private void initView(){
        etMobile = (EditText) findViewById(R.id.et_mobile);
        etPassword = (EditText) findViewById(R.id.et_password);
        etConfirmPassword = (EditText) findViewById(R.id.et_confirm_password);
        btnRegister = (Button) findViewById(R.id.btn_register);
        loadingDialog = new LoadingDialog(this);
        loadingDialog.setCancelable(false);
        showSoftInputFromWindow(etMobile);
    }

    private void initListener(){
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInputInfo()){
                    startRegister();
                }
            }
        });
    }

    private void initData(){
        facade = new RegisterFacade(this,getLander());
        EventBus.getDefault().register(this);
    }

    private void startRegister(){
        loadingDialog.show();
        facade.register(etMobile.getText().toString(),etPassword.getText().toString());
    }

    /**
     * 验证输入信息
     * @return
     */
    private boolean validateInputInfo(){
        if (!RegexUtils.isMobileExact(etMobile.getText().toString())){
            ToastUtils.showShortToast(R.string.mobile_format_error);
            return false;
        }
        if (!etConfirmPassword.getText().toString().equals(etPassword.getText().toString())){
            ToastUtils.showShortToast(R.string.password_not_equal);
            return false;
        }
        if (etPassword.getText().toString().length()<6){
            ToastUtils.showShortToast(R.string.password_too_short);
            return false;
        }
        return true;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_register;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRegisterCompleteEvent(RegisterCompleteEvent event){
        loadingDialog.dismiss();
        if (event.isSuccess){
            ToastUtils.showShortToast(R.string.register_success_please_login);
            finish();
        } else {
            ToastUtils.showShortToast(event.errorMessage);
        }
    }
}
