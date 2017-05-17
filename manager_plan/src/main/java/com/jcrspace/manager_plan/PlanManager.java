package com.jcrspace.manager_plan;

import com.jcrspace.common.lander.UserLander;
import com.jcrspace.common.manager.BaseManager;

import org.xutils.DbManager;

/**
 * Created by jiangchaoren on 2017/3/28.
 */

public class PlanManager extends BaseManager {
    private DbManager dbManager;

    public static PlanManager getInstance(UserLander userLander){
        try {
            return userLander.getManager(PlanManager.class);
        } catch (ClassNotFoundException e){
            return userLander.putManager(new PlanManager(userLander));
        }
    }
    public PlanManager(UserLander lander) {
        dbManager = lander.getDbManager();
    }



}
