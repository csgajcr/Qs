package com.jcrspace.manager_bill;

import com.jcrspace.common.lander.UserLander;
import com.jcrspace.common.manager.BaseManager;
import com.jcrspace.manager_bill.model.BillDO;
import com.jcrspace.manager_bill.model.BillSO;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import java.util.ArrayList;
import java.util.List;
import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by jiangchaoren on 2017/3/28.
 */

public class BillManager extends BaseManager {
    private DbManager dbManager;

    public static BillManager getInstance(UserLander userLander){
        try {
            return userLander.getManager(BillManager.class);
        } catch (ClassNotFoundException e){
            return userLander.putManager(new BillManager(userLander));
        }
    }

    public BillManager(UserLander lander) {
        dbManager = lander.getDbManager();
    }

    public void updateBill(BillDO billDO) throws DbException {
        dbManager.update(billDO);
    }

    public void insertBill(BillDO billDO) throws DbException {
        dbManager.save(billDO);
    }

    public List<BillDO> getBillList() throws DbException{
        List<BillDO> billDOs = dbManager.selector(BillDO.class).findAll();
        return billDOs;
    }

    public void deleteBill(BillDO billDO) throws DbException{
        dbManager.delete(billDO);
    }

    public void uploadAllToServer(QueryListListener listener) throws DbException{
        List<BillDO> billDOList = getBillList();
        List<BmobObject> billSOList = new ArrayList<>();
        for (BillDO billDO: billDOList){
            billSOList.add(convert(billDO));
        }
        new BmobBatch().insertBatch(billSOList).doBatch(listener);
    }

    public void uploadToServer(BillDO billDO, SaveListener listener){
        BillSO so = convert(billDO);
        so.save(listener);
    }

    public void syncAllFromServer(){

    }

    public BillSO convert(BillDO billDO){
        BillSO so= new BillSO();
        so.id = billDO.bid;
        so.comment = billDO.comment;
        so.create_time = billDO.create_time;
        so.money = billDO.money;
        so.status = billDO.status;
        so.title = billDO.title;
        so.type = billDO.type;
        so.user_id = billDO.user_id;
        return so;
    }






}
