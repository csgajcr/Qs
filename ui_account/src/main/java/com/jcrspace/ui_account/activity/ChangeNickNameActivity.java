package com.jcrspace.ui_account.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.utils.ToastUtils;
import com.jcrspace.common.view.BaseAppCompatActivity;
import com.jcrspace.ui_account.R;

public class ChangeNickNameActivity extends BaseAppCompatActivity {

    private EditText etNickname;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.change_nickname);
        initView();
        initData();
    }

    private void initView() {
        etNickname = (EditText) findViewById(R.id.et_nickname);
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        showSoftInputFromWindow(etNickname);
    }

    private void initData(){
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etNickname.getText().toString().length()>0){
                    startChange();
                } else {
                    ToastUtils.showShortToast(R.string.nickname_not_null);
                }

            }
        });
    }

    private void startChange(){

    }


    @Override
    protected int getContentView() {
        return R.layout.activity_change_nick_name;
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
