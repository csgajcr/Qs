package com.jcrspace.manager_bill;

import android.util.Log;

import com.blankj.utilcode.utils.LogUtils;
import com.google.gson.Gson;
import com.jcrspace.common.Qs;
import com.jcrspace.common.lander.UserLander;
import com.jcrspace.common.manager.BaseManager;
import com.jcrspace.manager_bill.listener.SyncCompleteListener;
import com.jcrspace.manager_bill.model.BillDO;
import com.jcrspace.manager_bill.model.BillSO;
import com.jcrspace.manager_bill.model.BillSOList;

import org.json.JSONArray;
import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by jiangchaoren on 2017/3/28.
 */

public class BillManager extends BaseManager {
    public static BillManager getInstance(UserLander userLander) {
        try {
            return userLander.getManager(BillManager.class);
        } catch (ClassNotFoundException e) {
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
        dbManager.saveOrUpdate(billDO);
    }

    public List<BillDO> getBillList() throws DbException {
        List<BillDO> billDOs = dbManager.selector(BillDO.class).findAll();
        if (billDOs == null) {
            return new ArrayList<>();
        }
        return billDOs;
    }

    public void saveBillList(List<BillDO> billDOs) throws DbException {
        dbManager.save(billDOs);
    }

    public void deleteBill(BillDO billDO) throws DbException {
        dbManager.delete(billDO);
    }

    /**
     * 同步数据
     * 规则：
     * 从服务器上拉去该用户的账单List
     * 与本地List对比，如果未改变并且已存在，则不更改。
     * 通过对比得出需要删除的和需要新增的，执行批量操作
     *
     * 注：此方法不支持50条以上的同时操作，这是一个BUG
     * 当上传、删除50条以上时，可能会有异常发生
     *
     * @param syncCompleteListener
     */
    public void syncBillFromServer(final SyncCompleteListener syncCompleteListener) {

        final List<BillDO> billDOList;
        try {
            billDOList = getBillList();
        } catch (DbException e) {
            e.printStackTrace();
            if (syncCompleteListener != null) {
                syncCompleteListener.onError(e);
            }
            return;
        }
        final List<BmobObject> billSOList = new ArrayList<>();
        for (BillDO billDO : billDOList) {
            billSOList.add(convert(billDO));
        }


        QueryListener queryListener = new QueryListener<JSONArray>() {
            @Override
            public void done(JSONArray jsonArray, BmobException e) {
                if (e != null) {
                    if (syncCompleteListener != null) {
                        syncCompleteListener.onError(e);
                    }
                    return;
                }
                Gson gson = new Gson();
                final BillSOList bmobBillSOList = gson.fromJson("{billSOList:" + jsonArray.toString() + "} ", BillSOList.class);
                List<BillSO> list = bmobBillSOList.billSOList;
                /**
                 * 这里对比与云数据库的信息，有的就不管，多的就删，少的就上传
                 */
                final List<BmobObject> insertList; //需要上传的列表
                List<BmobObject> deleteList = new ArrayList<>(); //需要删除的列表
                List<BmobObject> equalWebList = new ArrayList<>();//无需变动的列表
                List<BmobObject> equalLocalList = new ArrayList<>();//无需变动的列表
                for (BmobObject so : list) {
                    for (BmobObject localSO : billSOList) {
                        if (so.getObjectId().equals(localSO.getObjectId())) { //如果相等，就在两个列表中都去除，代表不需要任何操作
                            equalWebList.add(so);
                            equalLocalList.add(localSO);
                        }
                    }
                }
                /**
                 * 删除相同项
                 */
                for (BmobObject object : equalWebList) {
                    list.remove(object);
                }
                for (BmobObject object : equalLocalList) {
                    billSOList.remove(object);
                }
                insertList = billSOList; //确定最终新增总列表
                /**
                 * 这里注意：
                 * 下面遍历list赋值给deleteList，而不是直接deleteList=list，原因是因为BmobSDK的坑，他的批量操作并没有
                 * 设置表名，表名是通过List中的泛型的子类来获得的。也就是你不能直接给deleteList里面add BmobObject类型，
                 * 而是需要add BillSO(extend BmobObject)这样，SDK才可以根据这个deleteList来确定表明进行操作。
                 */
                for (BillSO so : list) {//确定最终删除总列表，为什么需要这样进行添加呢，因为List的泛型类型不同
                    deleteList.add(so);
                }
                if (insertList.size()>0){//如果新增列表不为空，则新增
                    new BmobBatch().insertBatch(insertList).doBatch(new QueryListListener<BatchResult>() {
                        @Override
                        public void done(List<BatchResult> list, BmobException e) {
                            if (e == null) {
                                /**
                                 * 将已同步的本地账单列表设置ObjectId
                                 */
                                try {
                                    /**
                                     * 遍历新增list，将新增结果的ObjectId存到本地
                                     */
                                    for (int i=0;i<list.size();i++){
                                        BmobObject object = insertList.get(i);
                                        BillDO billDO = new BillDO((BillSO) object);
                                        billDO.id = ((BillSO) object).id;
                                        KeyValue kv = new KeyValue("objectId",list.get(i).getObjectId());
                                        dbManager.update(BillDO.class, WhereBuilder.b("id","=",billDO.id),kv);
                                    }
                                    if (syncCompleteListener != null) {
                                        syncCompleteListener.onSuccess();
                                    }
                                } catch (DbException e1) {
                                    e1.printStackTrace();
                                    if (syncCompleteListener != null) {
                                        syncCompleteListener.onError(e1);
                                    }
                                }
                            } else {
                                if (syncCompleteListener != null) {
                                    syncCompleteListener.onError(e);
                                }
                            }
                        }
                    });
                } else {
                    /**
                     * 无需新增操作
                     */
                    if (syncCompleteListener != null) {
                        syncCompleteListener.onSuccess();
                    }
                }
                /**
                 * 删除操作
                 */
                if (deleteList.size()>0){
                    new BmobBatch().deleteBatch(deleteList).doBatch(new QueryListListener<BatchResult>() {
                        @Override
                        public void done(List<BatchResult> list, BmobException e) {
                            if (e==null){
                                LogUtils.e(list.get(0).getError().getMessage());
                            } else {
                                LogUtils.e(e.getMessage());
                            }
                        }
                    });
                }
            }
        };
        getAllBillFromServer(queryListener);

    }

    /**
     * 获取当前用户在服务器上的账单数据
     * @param listener
     */
    public void getAllBillFromServer(QueryListener<JSONArray> listener) {
        BmobQuery query = new BmobQuery("bill");
        query.addWhereEqualTo("usermobile", Qs.lander.getId());
        query.findObjectsByTable(listener);
    }

    /**
     * DO转SO
     * bid是bill在服务器的id，上传时不赋值
     *
     * @param billDO
     * @return
     */
    public BillSO convert(BillDO billDO) {
        BillSO so = new BillSO();
        so.comment = billDO.comment;
        so.create_time = billDO.create_time;
        so.money = billDO.money;
        so.status = billDO.status;
        so.title = billDO.title;
        so.type = billDO.type;
        so.id = billDO.id;
        /**
         * 如果该账单有在服务器上有对应账单，则直接覆盖
         */
        if (billDO.objectId != null) {
            so.setObjectId(billDO.objectId);
        }
        so.usermobile = Qs.lander.getId();
        return so;
    }

}
