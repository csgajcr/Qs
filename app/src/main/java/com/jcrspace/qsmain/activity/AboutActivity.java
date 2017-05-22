package com.jcrspace.qsmain.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.jcrspace.common.view.BaseAppCompatActivity;
import com.jcrspace.qsmain.R;

public class AboutActivity extends BaseAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.about);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_about;
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
