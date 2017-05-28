package com.jcrspace.manager_account;
import android.content.SharedPreferences;

import com.blankj.utilcode.utils.EncryptUtils;
import com.jcrspace.common.Qs;
import com.jcrspace.common.config.QsCommonConfig;
import com.jcrspace.common.lander.UserLander;
import com.jcrspace.common.manager.BaseManager;
import com.jcrspace.common.manager.TokenManager;
import com.jcrspace.manager_account.model.AccountDO;
import com.jcrspace.manager_account.model.AccountSO;

import org.json.JSONArray;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by jiangchaoren on 2017/2/27.
 */

public class AccountManager extends BaseManager{

    private UserLander lander;
    //存储当前信息的INDEX，因为这个表是在该用户的数据库中，所以user_info表只有一条记录，这一条记录就是当前用户的信息
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
        dbManager.saveOrUpdate(accountDO);
    }

    public void register(String userName,String password,SaveListener saveListener){
        AccountSO so = new AccountSO();
        so.mobile = userName;
        so.register_time = System.currentTimeMillis();
        so.password = EncryptUtils.encryptMD5ToString(password);
        so.device_token = TokenManager.calcToken(userName);
        so.save(saveListener);
    }

    public void findAccountFromServer(String name, QueryListener<JSONArray> listener){
        BmobQuery query = new BmobQuery("user");
        query.addWhereEqualTo("mobile",name);
        query.setLimit(1);
        query.findObjectsByTable(listener);
    }

    public void resetPassword(int id,String password){

    }

    public void logout(){
        lander.changeAccount(UserLander.DEFAULT_LOCAL_USER_ID);
    }

    public void setAutoLoginUser(String username){
        SharedPreferences sharedPreferences = Qs.getConfigSharedPreferences();
        sharedPreferences.edit().putString(QsCommonConfig.SP_AUTO_LOGIN_NAME,username).apply();
    }


    public AccountSO convert(AccountDO accountDO){
        AccountSO so = new AccountSO();
        so.device_token = accountDO.device_token;
        so.last_login_time = accountDO.last_login_time;
        so.mobile = accountDO.mobile;
        so.nick_name = accountDO.nick_name;
        so.register_time = accountDO.register_time;
        so.sex = accountDO.sex;
        so.status = accountDO.status;
        return so;
    }
}
