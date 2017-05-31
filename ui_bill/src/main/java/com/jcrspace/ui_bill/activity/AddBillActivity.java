package com.jcrspace.ui_bill.activity;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.jcrspace.common.view.BaseAppCompatActivity;
import com.jcrspace.manager_bill.BillManager;
import com.jcrspace.manager_bill.event.AddBillSuccessEvent;
import com.jcrspace.manager_bill.model.BillDO;
import com.jcrspace.ui_bill.R;

import org.greenrobot.eventbus.EventBus;
import org.xutils.ex.DbException;

public class AddBillActivity extends BaseAppCompatActivity {

    private EditText etTitle;
    private EditText etPrice;
    private EditText etComment;
    private Button btnSave;
    private BillManager billManager;
    private RadioButton rbExpenditure;
    private RadioButton rbIncome;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        String test = getIntent().getData().getQueryParameter("test");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.add_bill);
        billManager = BillManager.getInstance(getLander());
        initView();
        initData();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_add_bill;
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

    private void initData(){
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
                addBill();
            }
        });
    }

    private void addBill(){
        BillDO billDO = new BillDO();
        billDO.comment = etComment.getText().toString();
        billDO.create_time = System.currentTimeMillis();
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

        try {
            billManager.insertBill(billDO);
            EventBus.getDefault().post(new AddBillSuccessEvent(billDO));
            ToastUtils.showShortToast(R.string.add_bill_success);
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
