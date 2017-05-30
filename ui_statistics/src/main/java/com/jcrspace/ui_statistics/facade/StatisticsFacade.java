package com.jcrspace.ui_statistics.facade;

import android.content.Context;

import com.blankj.utilcode.utils.ConstUtils;
import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.TimeUtils;
import com.github.mikephil.charting.data.Entry;
import com.jcrspace.common.facade.BaseFacade;
import com.jcrspace.common.lander.UserLander;
import com.jcrspace.manager_bill.BillManager;
import com.jcrspace.manager_bill.model.BillDO;

import org.xutils.ex.DbException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jcr on 2017/5/26 0026.
 */

public class StatisticsFacade extends BaseFacade {

    private BillManager billManager;
    public List<BillDO> billDOList;

    public StatisticsFacade(Context context, UserLander lander) {
        super(context, lander);
        billManager = BillManager.getInstance(lander);
        try {
            billDOList = billManager.getBillList();
        } catch (DbException e) {
            e.printStackTrace();
            billDOList = new ArrayList<>();
        }
    }

    public float calcTotalIncome(){
        float total=0;
        for (BillDO billDO : billDOList){
            if (billDO.type==BillDO.TYPE.INCOME){
                total+=billDO.money;
            }
        }
        return total;
    }

    public float calcTotalExpenditure(){
        float total=0;
        for (BillDO billDO : billDOList){
            if (billDO.type==BillDO.TYPE.EXPENDITURE){
                total+=billDO.money;
            }
        }
        return total;
    }

    public List<Entry> getLastSevenDayChartEntryList(){
        /**
         * 定义总的列表。List有7个元素，分别代表7天每一天的账单List
         */
        List<List<BillDO>> allList = new ArrayList<>();
        /**
         * 获取7天前的开始时间
         */
        long sevenDayAgoTime = (System.currentTimeMillis() / ConstUtils.DAY )* ConstUtils.DAY -8 * ConstUtils.HOUR - 6 * ConstUtils.DAY;
        /**
         * 得出每一天的时间戳区间，并判断该时段是否有账单
         * 判断结果整合至啊allList
         */
        for (int i=1;i<=7;++i){
            List<BillDO> dayBillDOs = new ArrayList<>();
            long dayLeft = sevenDayAgoTime + (i)*ConstUtils.DAY; //天左区间
            long dayRight = sevenDayAgoTime + (i+1)*ConstUtils.DAY;//天右区间
            LogUtils.e(TimeUtils.millis2String(dayLeft));
            LogUtils.e(TimeUtils.millis2String(dayRight));
            for (BillDO billDO:billDOList){
                if (billDO.create_time<=dayRight && billDO.create_time>=dayLeft){
                    dayBillDOs.add(billDO);
                }
            }
            allList.add(dayBillDOs);
        }

        LogUtils.e(TimeUtils.millis2String(System.currentTimeMillis()));
        /**
         * 转换allList至EntryList
         */
        List<Entry> entries = new ArrayList<>();
        for (int i=0;i<allList.size();++i){
            entries.add(new Entry(i+1,calcBillListMoneyAverage(allList.get(i))));
        }
        return entries;
    }

    public float calcBillListMoneyAverage(List<BillDO> billDOs){
        float average = 0;
        for (BillDO billDO:billDOs){
            if (billDO.type==BillDO.TYPE.INCOME){
                average += billDO.money;
            } else if (billDO.type==BillDO.TYPE.EXPENDITURE){
                average -= billDO.money;
            }
        }
        return average;
    }

    public void refreshBillList(){
        try {
            billDOList = billManager.getBillList();
        } catch (DbException e) {
            e.printStackTrace();
            billDOList = new ArrayList<>();
        }
    }
}
