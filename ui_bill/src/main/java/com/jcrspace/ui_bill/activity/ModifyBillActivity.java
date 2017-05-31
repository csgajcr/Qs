package com.jcrspace.ui_bill.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.blankj.utilcode.utils.ToastUtils;
import com.jcrspace.common.view.BaseAppCompatActivity;
import com.jcrspace.manager_bill.BillManager;
import com.jcrspace.manager_bill.event.AddBillSuccessEvent;
import com.jcrspace.manager_bill.event.ModifyBillSuccessEvent;
import com.jcrspace.manager_bill.model.BillDO;
import com.jcrspace.ui_bill.R;

import org.greenrobot.eventbus.EventBus;
import org.xutils.ex.DbException;

public class ModifyBillActivity extends BaseAppCompatActivity {

    private EditText etTitle;
    private EditText etPrice;
    private EditText etComment;
    private Button btnSave;
    private BillManager billManager;
    private RadioButton rbExpenditure;
    private RadioButton rbIncome;
    private BillDO currentBillDO = new BillDO();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        String test = getIntent().getData().getQueryParameter("test");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.modify_bill);
        billManager = BillManager.getInstance(getLander());
        initView();
        initListener();
        initData();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_modify_bill;
    }

    private void initView(){
        etTitle = (EditText) findViewById(R.id.et_title);
        etPrice = (EditText) findViewById(R.id.et_price);
        btnSave = (Button) findViewById(R.id.btn_save);
        etComment = (EditText) findViewById(R.id.et_comment);
        rbExpenditure = (RadioButton) findViewById(R.id.rb_expenditure);
        rbIncome = (RadioButton) findViewById(R.id.rb_income);
        showSoftInputFromWindow(etTitle);
    }

    private void initListener(){
        /**
         * 验证金额准确性
         */
        etPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                if (text.equals(".")){
                    editable.delete(text.length()-1,text.length());
                    return;
                }
                if (text.contains(".") && text.length()-text.indexOf(".")>3){
                    editable.delete(text.indexOf(".")+3,text.length());
                    ToastUtils.showShortToast(R.string.price_max_length_2);
                    return;
                }
                if (text.length()>1 && Float.parseFloat(text)> 9999999L){
                    editable.delete(text.length()-1,text.length());
                    ToastUtils.showShortToast(R.string.price_max_value);
                    return;
                }
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveBill();
            }
        });
    }

    private void initData(){
        currentBillDO.id = Integer.parseInt(getIntent().getData().getQueryParameter("id"));
        currentBillDO.type = Integer.parseInt(getIntent().getData().getQueryParameter("type"));
        currentBillDO.title = getIntent().getData().getQueryParameter("title");
        currentBillDO.status = Integer.parseInt(getIntent().getData().getQueryParameter("status"));
        currentBillDO.bid = Integer.parseInt(getIntent().getData().getQueryParameter("bid"));
        currentBillDO.comment = getIntent().getData().getQueryParameter("comment");
        currentBillDO.create_time = Long.parseLong(getIntent().getData().getQueryParameter("create_time"));
        currentBillDO.money = Float.parseFloat(getIntent().getData().getQueryParameter("money"));
        currentBillDO.objectId = getIntent().getData().getQueryParameter("objectId");
        currentBillDO.username = getIntent().getData().getQueryParameter("username");
        etTitle.setText(currentBillDO.title);
        etPrice.setText(Float.toString(currentBillDO.money));
        if (currentBillDO.comment!=null){
            etComment.setText(currentBillDO.comment);
        }
        if (currentBillDO.type==BillDO.TYPE.INCOME){
            rbIncome.setChecked(true);
            rbExpenditure.setChecked(false);
        } else {
            rbIncome.setChecked(false);
            rbExpenditure.setChecked(true);
        }
    }

    private void saveBill(){
        BillDO billDO = currentBillDO;
        billDO.comment = etComment.getText().toString();
        if (!etPrice.getText().toString().equals("")){
            billDO.money = Float.parseFloat(etPrice.getText().toString());
        } else {
            ToastUtils.showShortToast(R.string.price_not_null);
            return;
        }
        if (!etTitle.getText().toString().equals("")){
            billDO.title = etTitle.getText().toString();
        } else {
            ToastUtils.showShortToast(R.string.bill_title_not_null);
            return;
        }
        if (rbIncome.isChecked()){
            billDO.type = BillDO.TYPE.INCOME;
        } else {
            billDO.type = BillDO.TYPE.EXPENDITURE;
        }
        //保存订单过后置空objectId便于下次同步上传
        billDO.objectId = "";
        try {
            billManager.updateBill(billDO);
            EventBus.getDefault().post(new ModifyBillSuccessEvent(billDO));
            ToastUtils.showShortToast(R.string.modify_bill_success);
            finish();
        } catch (DbException e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
