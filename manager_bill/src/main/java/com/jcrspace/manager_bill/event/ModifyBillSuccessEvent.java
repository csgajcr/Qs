package com.jcrspace.manager_bill.event;

import com.jcrspace.manager_bill.model.BillDO;

/**
 * Created by jiangchaoren on 2017/5/31.
 */

public class ModifyBillSuccessEvent {
    public BillDO billDO;

    public ModifyBillSuccessEvent(BillDO billDO) {
        this.billDO = billDO;
    }
}
