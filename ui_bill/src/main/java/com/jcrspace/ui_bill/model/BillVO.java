package com.jcrspace.ui_bill.model;

import com.jcrspace.manager_bill.model.BillDO;

/**
 * Created by jiangchaoren on 2017/3/28.
 */

public class BillVO {
    public BillDO billDO;

    public BillVO() {
    }

    public BillVO(BillDO billDO) {
        this.billDO = billDO;
        this.id = billDO.id;
        this.title = billDO.title;
        this.status = billDO.status;
        this.createTime = billDO.create_time;
        this.money = String.valueOf(billDO.money);
        this.comment = billDO.comment;
        this.type = billDO.type;
    }

    public int id;

    public String title;

    public int status;

    public long createTime;

    public String money;

    public String comment;

    public int type;

}
