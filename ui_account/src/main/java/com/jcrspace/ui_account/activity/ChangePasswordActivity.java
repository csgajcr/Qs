package com.jcrspace.ui_account.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.utils.EncryptUtils;
import com.blankj.utilcode.utils.RegexUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.google.gson.Gson;
import com.jcrspace.common.dialog.LoadingDialog;
import com.jcrspace.common.view.BaseAppCompatActivity;
import com.jcrspace.manager_account.AccountManager;
import com.jcrspace.manager_account.event.LoginCompleteEvent;
import com.jcrspace.manager_account.model.AccountSO;
import com.jcrspace.manager_account.model.AccountSOList;
import com.jcrspace.ui_account.R;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.xutils.ex.DbException;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class ChangePasswordActivity extends BaseAppCompatActivity {

    private EditText etOldPassword;
    private EditText etNewPassword;
    private EditText etConfirmPassword;
    private Button btnSubmit;
    private AccountManager accountManager;
    private LoadingDialog dialog;

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
        dialog = new LoadingDialog(this);
        dialog.setCancelable(false);
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
        accountManager = AccountManager.getInstance(getLander());
    }

    private void startChange(){
        dialog.show();
        final String oldPassword = etOldPassword.getText().toString();
        final String newPassword = etNewPassword.getText().toString();
        try {
            accountManager.findAccountFromServer(accountManager.readUserInfo().mobile, new QueryListener<JSONArray>() {
                @Override
                public void done(JSONArray jsonArray, BmobException e) {
                    dialog.dismiss();
                    if (e!=null){
                        ToastUtils.showShortToast(R.string.network_connect_failed);
                        return;
                    }
                    Gson gson = new Gson();
                    final AccountSOList accountList = gson.fromJson("{accountSOList:"+jsonArray.toString()+"} ",AccountSOList.class);
                    List<AccountSO> list = accountList.accountSOList;
                    if (list.size()==0){
                        ToastUtils.showShortToast(R.string.unknown_error);
                        return;
                    }
                    final AccountSO accountSO = list.get(0);
                    if (accountSO.password.equals(EncryptUtils.encryptMD5ToString(oldPassword))){
                        accountManager.updatePassword(newPassword, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e==null){
                                    ToastUtils.showShortToast(R.string.change_password_success);
                                    finish();
                                } else {
                                    ToastUtils.showShortToast(R.string.network_connect_failed);
                                }
                            }
                        });
                    } else {
                        ToastUtils.showShortToast(R.string.old_password_incorrect);
                    }
                }
            });
        } catch (DbException e) {
            e.printStackTrace();
        }
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
