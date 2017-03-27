package com.jcrspace.manager_account;

import android.os.UserManager;

import com.jcrspace.common.lander.UserLander;
import com.jcrspace.common.manager.BaseManager;
import com.jcrspace.manager_account.model.AccountDO;
import com.jcrspace.manager_account.model.AccountModel;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

/**
 * Created by jiangchaoren on 2017/2/27.
 */

public class AccountManager extends BaseManager{
    private DbManager dbManager;
    private UserLander lander;

    public static AccountManager getInstance(UserLander userLander){
        try {
            return userLander.getManager(AccountManager.class);
        } catch (ClassNotFoundException e){
            return userLander.putManager(new AccountManager(userLander));
        }
    }

    public AccountManager(UserLander lander) {
        this.lander = lander;
        dbManager = lander.getDbManager();
    }

    public void testCreate(AccountDO accountDO) throws DbException {
//        dbManager.saveOrUpdate(accountModel);
        dbManager.save(accountDO);
    }

}
