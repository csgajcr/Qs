package com.jcrspace.manager_bill.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by jiangchaoren on 2017/3/28.
 */

public class BillSO extends BmobObject {

    public int id;

    public int type;

    public String title;

    public int status;

    public float money;

    public String usermobile;

    public long create_time;

    public String comment;

    public BillSO() {
        this.setTableName("bill");
    }
}
