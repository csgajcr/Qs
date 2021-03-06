package com.jcrspace.ui_account.activity;

import android.app.Dialog;
import android.content.DialogInterface;
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

import com.blankj.utilcode.utils.RegexUtils;
import com.blankj.utilcode.utils.SpannableStringUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.jcrspace.common.Qs;
import com.jcrspace.common.config.ActivityUrls;
import com.jcrspace.common.dialog.ConfirmDialog;
import com.jcrspace.common.dialog.LoadingDialog;
import com.jcrspace.common.router.UrlBuilder;
import com.jcrspace.common.view.BaseAppCompatActivity;
import com.jcrspace.manager_account.event.LoginCompleteEvent;
import com.jcrspace.manager_account.event.RegisterCompleteEvent;
import com.jcrspace.manager_bill.event.BillListRefreshEvent;
import com.jcrspace.ui_account.R;
import com.jcrspace.ui_account.facade.LoginFacade;
import com.jcrspace.ui_account.listener.DownloadBillListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class LoginActivity extends BaseAppCompatActivity {

    private EditText etMobile;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvRegister;

    private LoginFacade facade;
    private LoadingDialog dialog;

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
        dialog = new LoadingDialog(this);
        dialog.setCancelable(false);
    }

    private void initListener(){
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UrlBuilder.build(LoginActivity.this, ActivityUrls.REGISTER).startActivity();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInputInfo()){
                    startLogin();
                }
            }
        });
    }

    private void initData(){
        facade = new LoginFacade(this,getLander());
        EventBus.getDefault().register(this);
        SpannableString spannableString = new SpannableString(getString(R.string.havent_mobile_goto_register));
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)),
                spannableString.length()-4,spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvRegister.setText(spannableString);
        showSoftInputFromWindow(etMobile);
    }

    private void startLogin(){
        dialog.show();
        facade.login(etMobile.getText().toString(),etPassword.getText().toString());
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
        if (etPassword.getText().toString().length()<6){
            ToastUtils.showShortToast(R.string.password_too_short);
            return false;
        }
        return true;
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

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /**
     * 从服务器上拉取数据到本地
     * 和同步不同
     */
    public void downloadBillFromServer(){
        final LoadingDialog dialog = new LoadingDialog(this);
        dialog.setCancelable(false);
        dialog.setLoadingText(getString(R.string.downloading_bill));
        dialog.show();
        facade.getAllBillFromServer(new DownloadBillListener() {
            @Override
            public void onSuccess() {
                EventBus.getDefault().post(new BillListRefreshEvent());
                dialog.dismiss();
                finish();
            }

            @Override
            public void onError(Exception e) {
                dialog.dismiss();
                finish();
            }
        });
    }

    /**
     * 登录完成,根据当前状态判断是否需要合并账单
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginCompleteEvent(LoginCompleteEvent event){
        dialog.dismiss();
        if (event.isSuccess){
            ToastUtils.showShortToast(R.string.login_success);
            if (facade.isNeedMergeBill()){
                /**
                 * 需要合并账单，提示用户是否需要合并
                 * 如果合并，则以本地账单为准
                 */
                ConfirmDialog dialog = new ConfirmDialog(this, getString(R.string.is_need_merge_bill));
                dialog.setOnPositiveClickListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        facade.mergeBill();
                        EventBus.getDefault().post(new BillListRefreshEvent());
                        finish();
                    }
                });
                /**
                 * 如果不合并，则从服务器拉取刚用户的账单
                 */
                dialog.setOnNegativeClickListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        downloadBillFromServer();
                    }
                });
                dialog.setCancelable(false);
                dialog.show();
            } else {
                /**
                 * 如果不需要合并账单，那么判断是否已含有本地数据
                 * 如果没有则从服务器拉取
                 */
                if (!facade.isHaveLocalData()){
                    downloadBillFromServer();
                } else {
                    finish();
                }
            }
        } else {
            ToastUtils.showShortToast(event.errorMessage);
        }
    }

    /**
     * 注册完成，提示用户登录
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRegisterCompleteEvent(RegisterCompleteEvent event){
        if (event.isSuccess){
            etMobile.setText(event.userName);
        }
    }
}
