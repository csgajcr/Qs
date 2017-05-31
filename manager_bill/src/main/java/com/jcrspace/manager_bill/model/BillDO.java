package com.jcrspace.manager_bill.model;

import com.jcrspace.common.model.BaseDO;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by jiangchaoren on 2017/3/28.
 */
@Table(name = "bill")
public class BillDO extends BaseDO {

    @Column(name = "id",isId = true)
    public int id;

    @Column(name = "bid")
    public int bid;

    @Column(name = "type")
    public int type;

    @Column(name = "objectId")
    public String objectId;

    @Column(name = "title")
    public String title;

    @Column(name = "status")
    public int status;

    @Column(name = "money")
    public float money;

    @Column(name = "username")
    public String username;

    @Column(name = "create_time")
    public long create_time;

    @Column(name = "comment")
    public String comment;

    public BillDO() {
    }

    public BillDO(BillSO billSO) {
        this.bid = billSO.id;
        this.type = billSO.type;
        this.objectId = billSO.getObjectId();
        this.title = billSO.title;
        this.status = billSO.status;
        this.money = billSO.money;
        this.username = billSO.usermobile;
        this.create_time = billSO.create_time;
        this.comment = billSO.comment;
    }

    public static class TYPE{
        public static final int UNSET = 0;
        public static final int EXPENDITURE = 1;
        public static final int INCOME = 2;
    }
}
