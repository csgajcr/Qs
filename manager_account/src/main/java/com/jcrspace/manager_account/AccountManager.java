package com.jcrspace.manager_account;
import com.jcrspace.common.lander.UserLander;
import com.jcrspace.common.manager.BaseManager;
import com.jcrspace.manager_account.model.AccountDO;
import com.jcrspace.manager_account.model.AccountSO;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by jiangchaoren on 2017/2/27.
 */

public class AccountManager extends BaseManager{
    private DbManager dbManager;
    private UserLander lander;
    private static final int CURRENT_INDEX_ID=1;

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
    public void deleteUserInfo() throws DbException{
        dbManager.deleteById(AccountDO.class,CURRENT_INDEX_ID);
    }

    public AccountDO readUserInfo() throws DbException {
        AccountDO accountDO = dbManager.findById(AccountDO.class,CURRENT_INDEX_ID);
        return accountDO;
    }

    public void updateUserInfo(AccountDO accountDO) throws DbException {
        accountDO.id = CURRENT_INDEX_ID;
        dbManager.update(accountDO);
    }

    public void createUserInfo(AccountDO accountDO) throws DbException{
        dbManager.save(accountDO);
    }

    public void register(AccountDO accountDO,String password,SaveListener saveListener){
        AccountSO so = convert(accountDO);
        so.password = password;
        so.save(saveListener);
    }

    public void login(String name,FindListener<AccountSO> listener){
        BmobQuery<AccountSO> query = new BmobQuery<>();
        query.addWhereEqualTo("name",name);
        query.setLimit(1);
        query.findObjects(listener);
    }

    public void resetPassword(int id,String password){

    }


    public AccountSO convert(AccountDO accountDO){
        AccountSO so = new AccountSO();
        so.device_token = accountDO.device_token;
        so.last_login_time = accountDO.last_login_time;
        so.name = accountDO.name;
        so.nick_name = accountDO.nick_name;
        so.phone = accountDO.phone;
        so.register_time = accountDO.register_time;
        so.sex = accountDO.sex;
        so.status = accountDO.status;
        return so;
    }
}
