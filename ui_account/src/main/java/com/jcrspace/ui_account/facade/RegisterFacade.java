package com.jcrspace.ui_account.facade;

import android.content.Context;

import com.jcrspace.common.facade.BaseFacade;
import com.jcrspace.common.lander.UserLander;
import com.jcrspace.manager_account.AccountManager;
import com.jcrspace.manager_account.event.RegisterCompleteEvent;
import com.jcrspace.ui_account.R;

import org.greenrobot.eventbus.EventBus;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Jcr on 2017/5/28 0028.
 */

public class RegisterFacade extends BaseFacade {

    private AccountManager accountManager;
    public RegisterFacade(Context context, UserLander lander) {
        super(context, lander);
        accountManager = AccountManager.getInstance(lander);
    }

    public void register(final String userName, String password){
        accountManager.register(userName, password, new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e==null){
                    EventBus.getDefault().post(new RegisterCompleteEvent(true,"",userName));
                } else {
                    EventBus.getDefault().post(new RegisterCompleteEvent(false, context.getString(R.string.network_connect_failed),userName));
                }
            }
        });
    }
}
