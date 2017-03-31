package com.jcrspace.ui_account.fragment;


import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.jcrspace.common.view.BaseFragment;
import com.jcrspace.ui_account.R;

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
