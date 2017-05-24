package com.jcrspace.manager_statistics.model;

import com.jcrspace.common.model.BaseDO;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by jiangchaoren on 2017/3/30.
 */
@Table(name = "plan")
public class PlanDO extends BaseDO {
    @Column(name = "id",isId = true)
    public int id;
    @Column(name = "pid")
    public int pid;
    @Column(name = "user_id")
    public int user_id;
    @Column(name = "description")
    public String description;
    @Column(name = "end_time")
    public long end_time;
    @Column(name = "money")
    public String money;
    @Column(name = "start_time")
    public long start_time;
    @Column(name = "status")
    public int status;
    @Column(name = "title")
    public String title;
    @Column(name = "type")
    public int type;
    @Column(name = "create_time")
    public long create_time;
}
