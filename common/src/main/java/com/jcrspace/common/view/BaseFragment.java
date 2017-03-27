package com.jcrspace.common.view;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jiangchaoren on 2017/3/27.
 */

public abstract class BaseFragment extends Fragment {
    private View fragmentView;

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

    protected abstract @LayoutRes int getLayoutResource();

    protected abstract void initView();

    protected abstract void initListener();

    protected abstract void initData();
}
