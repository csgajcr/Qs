package com.jcrspace.ui_account.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.utils.ToastUtils;
import com.jcrspace.common.dialog.LoadingDialog;
import com.jcrspace.common.view.BaseAppCompatActivity;
import com.jcrspace.manager_account.AccountManager;
import com.jcrspace.manager_account.event.ChangeNicknameEvent;
import com.jcrspace.manager_account.model.AccountDO;
import com.jcrspace.ui_account.R;

import org.greenrobot.eventbus.EventBus;
import org.xutils.ex.DbException;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class ChangeNickNameActivity extends BaseAppCompatActivity {

    private EditText etNickname;
    private Button btnSubmit;
    private LoadingDialog dialog;

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
        dialog = new LoadingDialog(this);
        dialog.setCancelable(false);
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

        String nickname = getIntent().getData().getQueryParameter("nickname");
        if (nickname!=null){
            etNickname.setText(nickname);
        }

    }

    private void startChange(){
        dialog.show();
        final String nickname = etNickname.getText().toString();
        final AccountManager accountManager = AccountManager.getInstance(getLander());
        accountManager.updateUserNickname(nickname, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                dialog.dismiss();
                if (e==null){
                    try {
                        AccountDO accountDO = accountManager.readUserInfo();
                        accountDO.nick_name = nickname;
                        accountManager.updateUserInfo(accountDO);
                        ToastUtils.showShortToast(R.string.change_success);
                        EventBus.getDefault().post(new ChangeNicknameEvent(accountDO));
                        finish();
                    } catch (DbException e1) {
                        e1.printStackTrace();
                        ToastUtils.showShortToast(R.string.network_connect_failed);
                    }
                } else {
                    ToastUtils.showShortToast(R.string.network_connect_failed);
                }
            }
        });
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
