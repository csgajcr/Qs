package com.jcrspace.ui_account.facade;

import android.content.Context;

import com.blankj.utilcode.utils.LogUtils;
import com.jcrspace.common.Qs;
import com.jcrspace.common.facade.BaseFacade;
import com.jcrspace.common.lander.UserLander;
import com.jcrspace.manager_account.AccountManager;
import com.jcrspace.manager_account.model.AccountDO;
import com.jcrspace.manager_account.model.AccountSO;
import com.jcrspace.ui_account.model.AccountVO;

import org.xutils.ex.DbException;

/**
 * Created by jiangchaoren on 2017/3/31.
 */

public class HomeFacade extends BaseFacade {
    private AccountManager accountManager;
    private AccountVO currentAccount;


    public HomeFacade(Context context, UserLander lander) {
        super(context, lander);
        accountManager = AccountManager.getInstance(lander);
        try {
            AccountDO accountDO = accountManager.readUserInfo();
            //无法读取到数据则取消自动登录用户
            if (accountDO==null){
                accountManager.setAutoLoginUser(UserLander.DEFAULT_LOCAL_USER_ID);
                lander.changeAccount(UserLander.DEFAULT_LOCAL_USER_ID);
            }
            currentAccount = new AccountVO(accountDO);
        } catch (DbException e) {
            e.printStackTrace();
            currentAccount = new AccountVO(new AccountDO());
            Qs.lander.changeAccount(UserLander.DEFAULT_LOCAL_USER_ID);
        }
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
            Qs.lander.changeAccount(UserLander.DEFAULT_LOCAL_USER_ID);
            return new AccountVO(new AccountDO());
        }
    }

    public AccountVO getCurrentAccount(){
        return currentAccount;
    }

    public void refreshCurrentAccount(){
        getAccountInformation();
    }
}
