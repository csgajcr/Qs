package com.jcrspace.ui_bill.fragment;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.FrameLayout;

import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.jcrspace.common.config.ActivityUrls;
import com.jcrspace.common.dialog.ConfirmDialog;
import com.jcrspace.common.lander.UserLander;
import com.jcrspace.common.router.UrlBuilder;
import com.jcrspace.common.view.BaseFragment;
import com.jcrspace.manager_account.event.LoginCompleteEvent;
import com.jcrspace.manager_bill.event.AddBillSuccessEvent;
import com.jcrspace.manager_bill.event.BillListRefreshEvent;
import com.jcrspace.ui_bill.R;
import com.jcrspace.ui_bill.adapter.BillAdapter;
import com.jcrspace.ui_bill.facade.BillFacade;
import com.jcrspace.ui_bill.model.BillVO;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by jiangchaoren on 2017/3/27.
 */

public class BillFragment extends BaseFragment{

    private XRecyclerView rvBill;
    private BillFacade billFacade;
    private FloatingActionButton fabAddBill;
    private BillAdapter adapter;
    private FrameLayout flEmptyView;



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
        flEmptyView = findViewById(R.id.fl_empty_view);
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

        adapter = new BillAdapter(getActivity(),billFacade.getBillList());
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setAddDuration(1000);
        rvBill.setItemAnimator(defaultItemAnimator);
        rvBill.setAdapter(adapter);
        adapter.setOnItemLongClickListener(new BillAdapter.OnItemLongClickListener() {
            @Override
            public void onLongClick(final BillVO billVO, final int position) {
                ConfirmDialog dialog = new ConfirmDialog(getActivity(),getString(R.string.confirm_delete_bill));
                dialog.setOnPositiveClickListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        billFacade.deleteBill(billVO);
                        billFacade.billVOList.remove(billVO);
                        adapter.notifyDataSetChanged();
                    }
                });
                dialog.show();
            }
        });
        rvBill.setEmptyView(flEmptyView);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddBillSuccessEvent(AddBillSuccessEvent event){
        LogUtils.e("onAddBillSuccessEvent");
        billFacade.billVOList.add(0,new BillVO(event.billDO));
        if (billFacade.billVOList.size()==1){
            adapter.notifyDataSetChanged();
        }
        adapter.notifyItemInserted(0);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBillListRefreshEvent(BillListRefreshEvent event){
        billFacade.getBillList();
        adapter.setBillVOList(billFacade.billVOList);
        adapter.notifyDataSetChanged();
    }
}
