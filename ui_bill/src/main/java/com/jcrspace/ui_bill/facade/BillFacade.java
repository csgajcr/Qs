package com.jcrspace.ui_bill.facade;

import android.content.Context;

import com.jcrspace.common.facade.BaseFacade;
import com.jcrspace.common.lander.UserLander;
import com.jcrspace.manager_bill.BillManager;
import com.jcrspace.manager_bill.model.BillDO;
import com.jcrspace.ui_bill.model.BillVO;

import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Jcr on 2017/5/20 0020.
 */

public class BillFacade extends BaseFacade {

    private BillManager billManager;
    public List<BillVO> billVOList;

    public BillFacade(Context context, UserLander lander) {
        super(context, lander);
        billManager = BillManager.getInstance(lander);
    }

    public List<BillVO> getBillList(){
        try {
            List<BillDO> billDOs = billManager.getBillList();
            if (billDOs==null){
                billDOs = new ArrayList<>();
            }
            List<BillVO> billVOs = new ArrayList<>();
            for (BillDO billDO:billDOs){
                BillVO billVO = new BillVO(billDO);
                billVOs.add(billVO);
            }
            billVOList = billVOs;

            Collections.sort(billVOList, new Comparator<BillVO>() {
                @Override
                public int compare(BillVO billVO, BillVO t1) {
                    return (int) (t1.createTime-billVO.createTime);
                }
            });
            return billVOList;
        } catch (DbException e) {
            e.printStackTrace();
            billVOList = new ArrayList<>();
            return billVOList;
        }
    }

    public boolean deleteBill(BillVO vo){
        try {
            billManager.deleteBill(vo.billDO);
            return true;
        } catch (DbException e) {
            e.printStackTrace();
            return false;
        }
    }
}
