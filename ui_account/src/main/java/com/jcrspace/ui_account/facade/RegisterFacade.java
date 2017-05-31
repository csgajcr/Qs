package com.jcrspace.ui_account.facade;

import android.content.Context;

import com.google.gson.Gson;
import com.jcrspace.common.facade.BaseFacade;
import com.jcrspace.common.lander.UserLander;
import com.jcrspace.manager_account.AccountManager;
import com.jcrspace.manager_account.event.LoginCompleteEvent;
import com.jcrspace.manager_account.event.RegisterCompleteEvent;
import com.jcrspace.manager_account.model.AccountSO;
import com.jcrspace.manager_account.model.AccountSOList;
import com.jcrspace.ui_account.R;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
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

    public void register(final String userName, final String password){
        accountManager.findAccountFromServer(userName, new QueryListener<JSONArray>() {
            @Override
            public void done(JSONArray jsonArray, BmobException e) {
                if (e!=null){
                    EventBus.getDefault().post(new RegisterCompleteEvent(false, context.getString(R.string.network_connect_failed),userName));
                    return;
                }
                Gson gson = new Gson();
                AccountSOList accountList = gson.fromJson("{accountSOList:"+jsonArray.toString()+"} ",AccountSOList.class);
                List<AccountSO> list = accountList.accountSOList;
                if (list.size()>0){
                    //用户名已存在
                    EventBus.getDefault().post(new RegisterCompleteEvent(false, context.getString(R.string.username_already_exist),userName));
                    return;
                } else {
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
        });


    }
}
