package com.jcrspace.ui_account.facade;

import android.content.Context;

import com.blankj.utilcode.utils.EncryptUtils;
import com.google.gson.Gson;
import com.jcrspace.common.Qs;
import com.jcrspace.common.facade.BaseFacade;
import com.jcrspace.common.lander.UserLander;
import com.jcrspace.common.manager.TokenManager;
import com.jcrspace.manager_account.AccountManager;
import com.jcrspace.manager_account.event.LoginCompleteEvent;
import com.jcrspace.manager_account.model.AccountDO;
import com.jcrspace.manager_account.model.AccountSO;
import com.jcrspace.manager_account.model.AccountSOList;
import com.jcrspace.ui_account.R;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.xutils.ex.DbException;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
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

    /**
     * 先查询列表中是否有该用户，如果没有再添加
     * @param userName
     * @param password
     */
    public void login(final String userName, final String password){
        accountManager.findAccountFromServer(userName, new QueryListener<JSONArray>() {
            @Override
            public void done(JSONArray jsonArray, BmobException e) {
                if (e!=null){
                    EventBus.getDefault().post(new LoginCompleteEvent(false,context.getString(R.string.network_connect_failed),null));
                    return;
                }
                Gson gson = new Gson();
                final AccountSOList accountList = gson.fromJson("{accountSOList:"+jsonArray.toString()+"} ",AccountSOList.class);
                List<AccountSO> list = accountList.accountSOList;
                if (list.size()==0){
                    EventBus.getDefault().post(new LoginCompleteEvent(false,context.getString(R.string.username_password_invalid),null));
                    return;
                }
                final AccountSO accountSO = list.get(0);
                if (accountSO.password.equals(EncryptUtils.encryptMD5ToString(password))){
                    //密码正确，更新登录信息和Token
                    accountSO.device_token = TokenManager.calcToken(userName);
                    accountSO.last_login_time = System.currentTimeMillis();
                    accountSO.update(accountSO.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e==null){
                                /**登录成功，先切换用户，使得dbManager指向该用户的数据库，在进行创建用户信息
                                 */
                                Qs.lander.changeAccount(accountSO.mobile);
                                try {
                                    accountManager.createUserInfo(new AccountDO(accountSO));
                                    accountManager.setAutoLoginUser(accountSO.mobile);
                                } catch (DbException e1) {
                                    e1.printStackTrace();
                                }
                                EventBus.getDefault().post(new LoginCompleteEvent(true,"",accountSO));
                                return;
                            } else {
                                EventBus.getDefault().post(new LoginCompleteEvent(false,context.getString(R.string.network_connect_failed),null));
                                return;
                            }
                        }
                    });
                } else {
                    //密码不正确
                    EventBus.getDefault().post(new LoginCompleteEvent(false,context.getString(R.string.username_password_invalid),null));
                }
            }
        });
    }

}
