package com.jcrspace.ui_account.facade;

import android.content.Context;

import com.jcrspace.common.facade.BaseFacade;
import com.jcrspace.common.lander.UserLander;
import com.jcrspace.manager_account.AccountManager;
import com.jcrspace.ui_account.model.AccountVO;

import org.xutils.ex.DbException;

/**
 * Created by jiangchaoren on 2017/3/31.
 */

public class HomeFacade extends BaseFacade {
    private AccountManager accountManager;

    public HomeFacade(Context context, UserLander lander) {
        super(context, lander);
        accountManager = AccountManager.getInstance(lander);
    }

    public boolean isUserLogin(){
        if (lander.getId().equals(UserLander.DEFAULT_LOCAL_USER_ID)){
            return false;
        } else {
            return true;
        }
    }

    public AccountVO getAccountInformation(){
        try {
            AccountVO accountVO = new AccountVO(accountManager.readUserInfo());
            return accountVO;
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }
    }

}
