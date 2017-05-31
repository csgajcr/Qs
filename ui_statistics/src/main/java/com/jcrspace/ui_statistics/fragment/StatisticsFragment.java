package com.jcrspace.ui_statistics.fragment;

import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.jcrspace.common.Qs;
import com.jcrspace.common.lander.UserLander;
import com.jcrspace.common.view.BaseFragment;
import com.jcrspace.manager_bill.event.AddBillSuccessEvent;
import com.jcrspace.manager_statistics.event.ChartAnimateEvent;
import com.jcrspace.manager_statistics.event.RefreshChartEvent;
import com.jcrspace.ui_statistics.R;
import com.jcrspace.ui_statistics.facade.StatisticsFacade;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangchaoren on 2017/3/27.
 */

public class StatisticsFragment extends BaseFragment {

    private LineChart chartSevenDay;
    private TextView tvIncome;
    private TextView tvExpenditure;

    private StatisticsFacade facade;

    public StatisticsFragment() {
        lander = Qs.lander;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_statistics;
    }

    @Override
    protected void initView() {
        chartSevenDay = findViewById(R.id.chart_seven_day);
        tvIncome = findViewById(R.id.tv_income);
        tvExpenditure = findViewById(R.id.tv_expenditure);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        facade = new StatisticsFacade(getActivity(),getLander());
        chartSevenDay.getDescription().setEnabled(false);
        renderHeader();
        render7dayChart();
    }

    private void renderHeader(){
        tvIncome.setText(getString(R.string.income)+" + "+facade.calcTotalIncome());
        tvExpenditure.setText(getString(R.string.expenditure)+" - "+facade.calcTotalExpenditure());
    }

    /**
     * 绘制7天内数据的图表
     */
    private void render7dayChart(){
        List<Entry> entries = facade.getLastSevenDayChartEntryList();
        LineDataSet dataSet = new LineDataSet(entries,getString(R.string.money));
        LineData lineData = new LineData(dataSet);
        chartSevenDay.setData(lineData);
        chartSevenDay.setScaleEnabled(false);
        chartSevenDay.invalidate();

    }

    /**
     * 添加账单成功Event
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddBillSuccessEvent(AddBillSuccessEvent event){
        LogUtils.e("onAddBillSuccessEvent");
        facade.refreshBillList();
        renderHeader();
        render7dayChart();
    }

    /**
     * 需要图表重新显示动画事件
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChartAnimateEvent(ChartAnimateEvent event){
        chartSevenDay.animateY(3000);
    }

    /**
     * 刷新图表数据事件
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshChartEvent(RefreshChartEvent event){
        facade.refreshBillList();
        renderHeader();
        render7dayChart();
    }
}
