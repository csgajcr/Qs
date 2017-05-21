package com.jcrspace.ui_bill.fragment;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.jcrspace.common.config.ActivityUrls;
import com.jcrspace.common.lander.UserLander;
import com.jcrspace.common.router.UrlBuilder;
import com.jcrspace.common.view.BaseFragment;
import com.jcrspace.ui_bill.R;
import com.jcrspace.ui_bill.adapter.BillAdapter;
import com.jcrspace.ui_bill.facade.BillFacade;

import java.util.List;

/**
 * Created by jiangchaoren on 2017/3/27.
 */

public class BillFragment extends BaseFragment{

    private XRecyclerView rvBill;
    private BillFacade billFacade;
    private FloatingActionButton fabAddBill;

    public BillFragment(UserLander lander) {
        super(lander);
        billFacade = new BillFacade(getActivity(),lander);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_bill;
    }

    @Override
    protected void initView() {
        rvBill = findViewById(R.id.rv_bill);
        rvBill.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvBill.setPullRefreshEnabled(false);
        fabAddBill = findViewById(R.id.fab_add_bill);
    }

    @Override
    protected void initListener() {
        fabAddBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UrlBuilder.build(getActivity(), ActivityUrls.ADDBILL).putParams("test","123").startActivity();
            }
        });
    }

    @Override
    protected void initData() {

        BillAdapter adapter = new BillAdapter(getActivity(),billFacade.getBillList());
        rvBill.setAdapter(adapter);
    }
}
