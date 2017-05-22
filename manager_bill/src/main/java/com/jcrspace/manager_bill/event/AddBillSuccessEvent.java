package com.jcrspace.manager_bill.event;

import com.jcrspace.manager_bill.model.BillDO;

/**
 * Created by Jcr on 2017/5/22 0022.
 */

public class AddBillSuccessEvent {
    public BillDO billDO;

    public AddBillSuccessEvent(BillDO billDO) {
        this.billDO = billDO;
    }
}
