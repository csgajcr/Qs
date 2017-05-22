package com.jcrspace.ui_account.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.jcrspace.common.view.BaseAppCompatActivity;
import com.jcrspace.ui_account.R;

public class RegisterActivity extends BaseAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_register;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
