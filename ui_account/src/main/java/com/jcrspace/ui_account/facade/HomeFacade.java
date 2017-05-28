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

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

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
            //未登录默认则不去数据库读取
            if (lander.getId().equals(UserLander.DEFAULT_LOCAL_USER_ID)){
                return;
            }
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

    public void updateUserSex(final String sex){
        accountManager.updateUserSex(sex, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    AccountDO accountDO = currentAccount.accountDO;
                    accountDO.sex = sex;
                    try {
                        accountManager.updateUserInfo(accountDO);
                    } catch (DbException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }
}
