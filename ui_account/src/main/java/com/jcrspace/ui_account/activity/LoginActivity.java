package com.jcrspace.ui_account.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.utils.SpannableStringUtils;
import com.jcrspace.common.config.ActivityUrls;
import com.jcrspace.common.router.UrlBuilder;
import com.jcrspace.common.view.BaseAppCompatActivity;
import com.jcrspace.ui_account.R;

public class LoginActivity extends BaseAppCompatActivity {

    private EditText etMobile;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
        initListener();
        initData();
    }

    private void initView(){
        etMobile = (EditText) findViewById(R.id.et_mobile);
        etPassword = (EditText) findViewById(R.id.et_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        tvRegister = (TextView) findViewById(R.id.tv_register);
    }

    private void initListener(){
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UrlBuilder.build(LoginActivity.this, ActivityUrls.REGISTER).startActivity();
            }
        });
    }

    private void initData(){
        SpannableString spannableString = new SpannableString(getString(R.string.havent_mobile_goto_register));
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)),
                spannableString.length()-4,spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvRegister.setText(spannableString);
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_login;
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
