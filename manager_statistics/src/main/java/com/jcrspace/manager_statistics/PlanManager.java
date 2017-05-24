package com.jcrspace.manager_statistics;

import com.jcrspace.common.lander.UserLander;
import com.jcrspace.common.manager.BaseManager;
import com.jcrspace.manager_statistics.model.PlanDO;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

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

    public void savePlan(PlanDO planDO) throws DbException {
        dbManager.saveOrUpdate(planDO);
    }

    public List<PlanDO> getPlanDOList() throws DbException {
        List<PlanDO> planDOs = dbManager.selector(PlanDO.class).findAll();
        if (planDOs==null){
            planDOs = new ArrayList<PlanDO>();
        }
        return planDOs;
    }





}
