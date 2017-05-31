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
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by jiangchaoren on 2017/2/27.
 */

/**
 * 用户信息管理类
 * 规则：
 * 1、在用户登录过后，会创建该用户的一个数据库名为account_13333333333
 * 这个数据库中有一张表为user_info，这张表只有一条数据，这条数据存储该用户的详细信息。
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

    /**
     * 从本地数据库读取用户信息
     * @return
     * @throws DbException
     */
    public AccountDO readUserInfo() throws DbException {
        AccountDO accountDO = dbManager.findById(AccountDO.class,CURRENT_INDEX_ID);
        return accountDO;
    }

    /**
     * 更新信息
     * @param accountDO
     * @throws DbException
     */
    public void updateUserInfo(AccountDO accountDO) throws DbException {
        dbManager.update(accountDO);
    }


    /**
     * 创建用户信息
     * @param accountDO
     * @throws DbException
     */
    public void createUserInfo(AccountDO accountDO) throws DbException{
        dbManager.dropTable(AccountDO.class);
        dbManager.saveOrUpdate(accountDO);
    }

    /**
     * 从服务器注册
     * @param userName
     * @param password
     * @param saveListener
     */
    public void register(String userName,String password,SaveListener saveListener){
        AccountSO so = new AccountSO();
        so.mobile = userName;
        so.register_time = System.currentTimeMillis();
        so.password = EncryptUtils.encryptMD5ToString(password);
        so.device_token = TokenManager.calcToken(userName);
        so.save(saveListener);
    }

    public void updateUserSex(String sex,UpdateListener listener){
        AccountDO accountDO = null;
        try {
            accountDO = readUserInfo();
        } catch (DbException e) {
            e.printStackTrace();
        }
        accountDO.sex = sex;
        AccountSO accountSO = convert(accountDO);
        accountSO.update(accountDO.objectID,listener);
    }

    /**
     * 从服务器上拉去所有数据
     * @param name
     * @param listener
     */
    public void findAccountFromServer(String name, QueryListener<JSONArray> listener){
        BmobQuery query = new BmobQuery("user");
        query.addWhereEqualTo("mobile",name);
        query.setLimit(1);
        query.findObjectsByTable(listener);
    }

    /**
     * 修改密码
     * @param newPassword
     * @param listener
     */
    public void updatePassword(String newPassword,UpdateListener listener){
        AccountDO accountDO = null;
        try {
            accountDO = readUserInfo();
        } catch (DbException e) {
            e.printStackTrace();
        }
        AccountSO accountSO = convert(accountDO);
        accountSO.password = EncryptUtils.encryptMD5ToString(newPassword);
        accountSO.update(accountDO.objectID,listener);
    }

    /**
     * 退出登录
     */
    public void logout(){
        lander.changeAccount(UserLander.DEFAULT_LOCAL_USER_ID);
        Qs.getConfigSharedPreferences().edit().putString(QsCommonConfig.SP_AUTO_LOGIN_NAME,UserLander.DEFAULT_LOCAL_USER_ID).apply();
    }

    /**
     * 设置自动登录用户
     * @param username
     */
    public void setAutoLoginUser(String username){
        SharedPreferences sharedPreferences = Qs.getConfigSharedPreferences();
        sharedPreferences.edit().putString(QsCommonConfig.SP_AUTO_LOGIN_NAME,username).apply();
    }

    /**
     * 更新用户昵称
     * @param nickname
     * @param listener
     */
    public void updateUserNickname(String nickname,UpdateListener listener){
        AccountDO accountDO = null;
        try {
            accountDO = readUserInfo();
        } catch (DbException e) {
            e.printStackTrace();
        }
        accountDO.nick_name = nickname;
        AccountSO accountSO = convert(accountDO);
        accountSO.update(accountDO.objectID,listener);
    }


    /**
     * 转换类
     * @param accountDO
     * @return
     */
    public AccountSO convert(AccountDO accountDO){
        AccountSO so = new AccountSO();
        so.id = accountDO.aid;
        so.device_token = accountDO.device_token;
        so.last_login_time = accountDO.last_login_time;
        so.mobile = accountDO.mobile;
        so.nick_name = accountDO.nick_name;
        so.register_time = accountDO.register_time;
        so.sex = accountDO.sex;
        so.status = accountDO.status;
        so.setObjectId(accountDO.objectID);
        return so;
    }


}
