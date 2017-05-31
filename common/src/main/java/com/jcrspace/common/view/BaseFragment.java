package com.jcrspace.common.view;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcrspace.common.Qs;
import com.jcrspace.common.R;
import com.jcrspace.common.lander.UserLander;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by jiangchaoren on 2017/3/27.
 */

public abstract class BaseFragment extends Fragment {
    protected View fragmentView;
    protected UserLander lander;

    public BaseFragment() {
        lander = Qs.lander;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        fragmentView = inflater.inflate(getLayoutResource(),container,false);
        initView();
        initListener();
        initData();
        return fragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    protected abstract @LayoutRes int getLayoutResource();

    protected abstract void initView();

    protected abstract void initListener();

    protected abstract void initData();

    protected <T extends View> T  findViewById(@IdRes int id){
        return (T) fragmentView.findViewById(id);
    }

    protected UserLander getLander(){
        return lander;
    }
}
