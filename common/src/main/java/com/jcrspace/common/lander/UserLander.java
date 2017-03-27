package com.jcrspace.common.lander;
import android.content.Context;
import com.jcrspace.common.utils.DbUtils;
import org.xutils.DbManager;
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

    public UserLander(Context context, String aid) {
        this.context = context;
        this.aid = aid;
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
