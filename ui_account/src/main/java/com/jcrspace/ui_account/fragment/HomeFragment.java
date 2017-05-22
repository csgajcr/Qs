package com.jcrspace.ui_account.fragment;


import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.blankj.utilcode.utils.ActivityUtils;
import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.jcrspace.common.config.ActivityUrls;
import com.jcrspace.common.lander.UserLander;
import com.jcrspace.common.router.UrlBuilder;
import com.jcrspace.common.view.BaseFragment;
import com.jcrspace.manager_account.event.LogoutEvent;
import com.jcrspace.ui_account.R;
import com.jcrspace.ui_account.facade.HomeFacade;
import com.jcrspace.ui_account.model.AccountVO;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

    private HomeFacade facade;

    public HomeFragment(UserLander lander) {
        super(lander);
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
    }

    @Override
    protected void initListener() {
        rbMale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){

                }

            }
        });
        rbFemale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){

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

        } else if (id == R.id.rl_password){

        } else if (id == R.id.rl_setting){
            UrlBuilder.build(getActivity(), ActivityUrls.SETTING).startActivity();
        } else if (id == R.id.fl_not_login){
            UrlBuilder.build(getActivity(), ActivityUrls.LOGIN).startActivity();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutEvent(LogoutEvent event){
        /**
         * 退出登录
         */
        renderMenu();
    }


}
