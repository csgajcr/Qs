package com.jcrspace.ui_account.fragment;


import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.ActivityUtils;
import com.blankj.utilcode.utils.ConstUtils;
import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.jcrspace.common.Qs;
import com.jcrspace.common.config.ActivityUrls;
import com.jcrspace.common.lander.UserLander;
import com.jcrspace.common.router.UrlBuilder;
import com.jcrspace.common.view.BaseFragment;
import com.jcrspace.manager_account.event.ChangeNicknameEvent;
import com.jcrspace.manager_account.event.LoginCompleteEvent;
import com.jcrspace.manager_account.event.LogoutEvent;
import com.jcrspace.manager_account.model.AccountDO;
import com.jcrspace.manager_bill.event.BillListRefreshEvent;
import com.jcrspace.ui_account.R;
import com.jcrspace.ui_account.facade.HomeFacade;
import com.jcrspace.ui_account.model.AccountVO;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by jiangchaoren on 2017/3/27.
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener{

    private RelativeLayout rlNickName;
    private RadioButton rbMale;
    private RadioButton rbFemale;
    private RelativeLayout rlSetting;
    private RelativeLayout rlPassword;
    private RelativeLayout rlAbout;
    private RelativeLayout rlUserName;
    private RelativeLayout rlPhone;
    private FrameLayout flNotLoginView;
    private RelativeLayout rlSex;
    private LinearLayout llSex;
    private TextView tvNickName;
    private TextView tvDay;
    private TextView tvMobile;

    private HomeFacade facade;

    public HomeFragment() {
        lander = Qs.lander;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        rlNickName = findViewById(R.id.rl_nickname);
        rbMale = findViewById(R.id.rb_male);
        rbFemale = findViewById(R.id.rb_female);
        rlAbout = findViewById(R.id.rl_about);
        rlSetting = findViewById(R.id.rl_setting);
        rlPassword = findViewById(R.id.rl_password);
        flNotLoginView = findViewById(R.id.fl_not_login);
        rlUserName = findViewById(R.id.rl_username);
        rlPhone = findViewById(R.id.rl_phone);
        rlSex = findViewById(R.id.rl_sex);
        llSex = findViewById(R.id.ll_sex);
        tvDay = findViewById(R.id.tv_day);
        tvMobile = findViewById(R.id.tv_mobile);
        tvNickName = findViewById(R.id.tv_nick_name);
    }

    @Override
    protected void initListener() {
        rbMale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    facade.updateUserSex(getString(R.string.male));
                }

            }
        });
        rbFemale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    facade.updateUserSex(getString(R.string.female));
                }
            }
        });
        flNotLoginView.setOnClickListener(this);
        rlAbout.setOnClickListener(this);
        rlSetting.setOnClickListener(this);
        rlNickName.setOnClickListener(this);
        rlPassword.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        facade = new HomeFacade(getActivity(),getLander());
        renderMenu();
    }

    private void renderMenu(){
        if (facade.isUserLogin()){
            flNotLoginView.setVisibility(View.GONE);
            ((ViewGroup)rlPassword.getParent()).setVisibility(View.VISIBLE);
            ((ViewGroup)rlNickName.getParent()).setVisibility(View.VISIBLE);
            rlPhone.setVisibility(View.VISIBLE);
            rlUserName.setVisibility(View.VISIBLE);
            ((ViewGroup)rlSex.getParent()).setVisibility(View.VISIBLE);
            AccountVO accountVO = facade.getAccountInformation();
            //TODO render account information
            if (accountVO.getNickName()==null){
                tvNickName.setText(R.string.unset);
            } else {
                tvNickName.setText(accountVO.getNickName());
            }
            if (accountVO.getRegisterTime()>1){
                long maxDay = (System.currentTimeMillis() - accountVO.getRegisterTime())/ ConstUtils.DAY + 1;
                tvDay.setText(getString(R.string.one_day).replace("{number}",maxDay+""));
            }
            if (!accountVO.getMobile().equals("")){
                tvMobile.setText(accountVO.getMobile());
            }
            if (accountVO.getSex()!=null && !accountVO.getSex().equals("")){
                if (accountVO.getSex().equals(getString(R.string.male))){
                    rbMale.setChecked(true);
                } else {
                    rbFemale.setChecked(true);
                }
            } else {
                rbFemale.setChecked(false);
                rbMale.setChecked(false);
            }

        } else {
            flNotLoginView.setVisibility(View.VISIBLE);
            ((ViewGroup)rlPassword.getParent()).setVisibility(View.GONE);
            ((ViewGroup)rlNickName.getParent()).setVisibility(View.GONE);
            rlPhone.setVisibility(View.GONE);
            rlUserName.setVisibility(View.GONE);
            ((ViewGroup)rlSex.getParent()).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.rl_about) {
            UrlBuilder.build(getActivity(), ActivityUrls.ABOUT).startActivity();
        } else if (id == R.id.rl_nickname){
            if (facade.getCurrentAccount().getNickName()!=null){
                UrlBuilder.build(getActivity(), ActivityUrls.CHANGE_NICKNAME).putParams("nickname",facade.getCurrentAccount().getNickName()).startActivity();
            } else {
                UrlBuilder.build(getActivity(), ActivityUrls.CHANGE_NICKNAME).startActivity();
            }
        } else if (id == R.id.rl_password){
            UrlBuilder.build(getActivity(), ActivityUrls.CHANGE_PASSWORD).startActivity();
        } else if (id == R.id.rl_setting){
            UrlBuilder.build(getActivity(), ActivityUrls.SETTING).startActivity();
        } else if (id == R.id.fl_not_login){
            UrlBuilder.build(getActivity(), ActivityUrls.LOGIN).startActivity();
        }
    }

    /**
     * 退出登录事件
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutEvent(LogoutEvent event){
        renderMenu();
        EventBus.getDefault().post(new BillListRefreshEvent());
    }

    /**
     * 登录成功或失败后事件
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginCompleteEvent(LoginCompleteEvent event){
        if (event.isSuccess){
            renderMenu();
            facade.refreshCurrentAccount();
            EventBus.getDefault().post(new BillListRefreshEvent());
        }
    }

    /**
     * 昵称变更事件
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChangeNicknameEvent(ChangeNicknameEvent event){
        tvNickName.setText(event.accountDO.nick_name);
        facade.refreshCurrentAccount();
    }


}
