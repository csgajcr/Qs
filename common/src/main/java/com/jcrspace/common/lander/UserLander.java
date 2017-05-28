package com.jcrspace.common.lander;
import android.content.Context;

import com.jcrspace.common.manager.BaseManager;
import com.jcrspace.common.utils.DbUtils;
import org.xutils.DbManager;
import org.xutils.db.DbManagerImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jiangchaoren on 2017/3/10.
 */

public class UserLander implements Lander {
    private Context context;
    protected Map<Class, Object> managerMap = new HashMap<>();
    private DbManager dbManager;
    private String aid;
    public static final String DEFAULT_LOCAL_USER_ID = "-1"; //无登录用户标识

    public UserLander(Context context, String aid) {
        this.context = context;
        this.aid = aid;
        dbManager = getDbManager();
    }

    public void changeAccount(String aid){
        this.aid = aid;
        //TODO 转换DbManager
        dbManager = DbUtils.getDbManager(getContext(), "account_" + aid);
        for (Map.Entry<Class, Object> entry : managerMap.entrySet()) {
            BaseManager manager = (BaseManager) entry.getValue();
            manager.dbManager = dbManager;
        }
    }


    @Override
    public String getId() {
        return aid;
    }

    @Override
    public DbManager getDbManager() {
        if (dbManager == null) {
            dbManager = DbUtils.getDbManager(getContext(), "account_" + aid);
        }
        return dbManager;
    }

    @Override
    public <T> T getManager(Class<?> managerType) throws ClassNotFoundException {
        if(managerMap.containsKey(managerType)){
            return (T) managerMap.get(managerType);
        }
        throw new ClassNotFoundException("");
    }

    @Override
    public <T> T putManager(T manager) {
        managerMap.put(manager.getClass(), manager);
        return manager;
    }

    @Override
    public Context getContext() {
        return context;
    }
}
