package com.jcrspace.ui_account.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.utils.RegexUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.jcrspace.common.view.BaseAppCompatActivity;
import com.jcrspace.ui_account.R;

public class ChangePasswordActivity extends BaseAppCompatActivity {

    private EditText etOldPassword;
    private EditText etNewPassword;
    private EditText etConfirmPassword;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.change_password);
        initView();
        initData();
    }

    private void initView() {
        etOldPassword = (EditText) findViewById(R.id.et_old_password);
        etNewPassword = (EditText) findViewById(R.id.et_new_password);
        etConfirmPassword = (EditText) findViewById(R.id.et_confirm_password);
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        showSoftInputFromWindow(etOldPassword);
    }

    private void initData(){
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInputInfo()){
                    startChange();
                }
            }
        });
    }

    private void startChange(){

    }

    /**
     * 验证输入信息
     * @return
     */
    private boolean validateInputInfo(){
        if (!etConfirmPassword.getText().toString().equals(etNewPassword.getText().toString())){
            ToastUtils.showShortToast(R.string.password_not_equal);
            return false;
        }
        if (etNewPassword.getText().toString().length()<6){
            ToastUtils.showShortToast(R.string.password_too_short);
            return false;
        }

        if (etOldPassword.getText().toString().length()<6){
            ToastUtils.showShortToast(R.string.password_too_short);
            return false;
        }
        return true;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_change_password;
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
