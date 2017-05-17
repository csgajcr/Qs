package com.jcrspace.common.facade;

import android.content.Context;

import com.jcrspace.common.lander.UserLander;

/**
 * Created by jiangchaoren on 2017/3/31.
 */

public abstract class BaseFacade {
    protected UserLander lander;
    protected Context context;

    public BaseFacade(Context context,UserLander lander) {
        this.lander = lander;
        this.context = context;
    }


}
