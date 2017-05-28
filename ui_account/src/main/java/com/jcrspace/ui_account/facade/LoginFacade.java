package com.jcrspace.ui_account.facade;

import android.content.Context;

import com.blankj.utilcode.utils.EncryptUtils;
import com.jcrspace.common.facade.BaseFacade;
import com.jcrspace.common.lander.UserLander;
import com.jcrspace.common.manager.TokenManager;
import com.jcrspace.manager_account.AccountManager;
import com.jcrspace.manager_account.event.LoginCompleteEvent;
import com.jcrspace.manager_account.model.AccountSO;
import com.jcrspace.ui_account.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Jcr on 2017/5/28 0028.
 */

public class LoginFacade extends BaseFacade {

    private AccountManager accountManager;
    public LoginFacade(Context context, UserLander lander) {
        super(context, lander);
        accountManager = AccountManager.getInstance(lander);
    }

    public void login(final String userName, final String password){
        accountManager.findAccountFromServer(userName, new FindListener<AccountSO>() {
            @Override
            public void done(List<AccountSO> list, BmobException e) {
                if (e!=null){
                    EventBus.getDefault().post(new LoginCompleteEvent(false,context.getString(R.string.network_connect_failed),null));
                }
                if (list.size()==0){
                    EventBus.getDefault().post(new LoginCompleteEvent(false,context.getString(R.string.username_password_invalid),null));
                    return;
                }
                final AccountSO accountSO = list.get(0);
                if (accountSO.password.equals(EncryptUtils.encryptMD5ToString(password))){
                    accountSO.device_token = TokenManager.calcToken(userName);
                    accountSO.last_login_time = System.currentTimeMillis();
                    accountSO.update(accountSO.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e==null){
                                EventBus.getDefault().post(new LoginCompleteEvent(true,"",accountSO));
                            } else {
                                EventBus.getDefault().post(new LoginCompleteEvent(false,context.getString(R.string.network_connect_failed),null));
                            }
                        }
                    });
                }
            }
        });
    }
}
