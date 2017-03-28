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

    @Column(name = "title")
    public String title;

    @Column(name = "status")
    public int status;

    @Column(name = "money")
    public float money;

    @Column(name = "user_id")
    public int user_id;

    @Column(name = "create_time")
    public long create_time;

    @Column(name = "comment")
    public String comment;
}
