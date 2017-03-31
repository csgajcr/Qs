package com.jcrspace.ui_account.fragment;


import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.jcrspace.common.lander.UserLander;
import com.jcrspace.common.view.BaseFragment;
import com.jcrspace.ui_account.R;
import com.jcrspace.ui_account.facade.HomeFacade;
import com.jcrspace.ui_account.model.AccountVO;

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
            rlPassword.setVisibility(View.VISIBLE);
            rlNickName.setVisibility(View.VISIBLE);
            rlPhone.setVisibility(View.VISIBLE);
            rlUserName.setVisibility(View.VISIBLE);
            AccountVO accountVO = facade.getAccountInformation();
            //TODO render account information
        } else {
            flNotLoginView.setVisibility(View.VISIBLE);
            rlPassword.setVisibility(View.GONE);
            rlNickName.setVisibility(View.GONE);
            rlPhone.setVisibility(View.GONE);
            rlUserName.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.rl_about) {

        } else if (id == R.id.rl_nickname){

        } else if (id == R.id.rl_password){

        } else if (id == R.id.rl_setting){

        }
    }
}
