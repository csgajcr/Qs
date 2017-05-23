package com.jcrspace.manager_plan.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by jiangchaoren on 2017/3/30.
 */

public class PlanSO extends BmobObject {

    public int id;

    public int user_id;

    public String description;

    public long end_time;

    public String money;

    public long start_time;

    public int status;

    public String title;

    public int type;

    public long create_time;
}
