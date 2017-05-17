package com.jcrspace.manager_account.model;

import com.jcrspace.common.model.BaseDO;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by jiangchaoren on 2017/3/27.
 */
@Table(name = "user_info")
public class AccountDO extends BaseDO {
    @Column(name = "id",isId = true)
    public int id;

    @Column(name = "aid")
    public int aid;

    @Column(name = "name")
    public String name;

//    public String password;

    @Column(name = "register_time")
    public long register_time;

    @Column(name = "last_login_time")
    public long last_login_time;

    @Column(name = "status")
    public int status;

    @Column(name = "device_token")
    public String device_token;

    @Column(name = "sex")
    public String sex;

    @Column(name = "phone")
    public String phone;

    @Column(name = "nick_name")
    public String nick_name;



}
