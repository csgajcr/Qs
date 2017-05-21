package com.jcrspace.ui_bill.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.jcrspace.ui_bill.R;

public class AddBillActivity extends AppCompatActivity {

    private EditText etTitle;
    private EditText etPrice;
    private EditText etComment;
    private Button btnSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);
//        String test = getIntent().getData().getQueryParameter("test");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.add_bill);
        initView();
        initData();
    }

    private void initView(){
        etTitle = (EditText) findViewById(R.id.et_title);
        etPrice = (EditText) findViewById(R.id.et_price);
        btnSave = (Button) findViewById(R.id.btn_save);
        etComment = (EditText) findViewById(R.id.et_comment);
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
                if (text.contains(".") && text.length()-text.indexOf(".")>3){
                    editable.delete(text.indexOf(".")+3,text.length());
                    ToastUtils.showShortToast(R.string.price_max_length_2);
                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
