package com.jcrspace.qsmain.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.jcrspace.common.lander.UserLander;
import com.jcrspace.common.view.BaseAppCompatActivity;
import com.jcrspace.common.view.BaseFragment;
import com.jcrspace.common.view.BottomNavigationViewEx;
import com.jcrspace.manager_account.AccountManager;
import com.jcrspace.manager_account.model.AccountDO;
import com.jcrspace.manager_account.model.AccountModel;
import com.jcrspace.qsmain.R;
import com.jcrspace.ui_account.fragment.HomeFragment;
import com.jcrspace.ui_bill.fragment.RecentFragment;
import com.jcrspace.ui_plan.fragment.PlanFragment;

import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseAppCompatActivity {

    private TextView mTextMessage;
    private BottomNavigationViewEx navigation;
    private ViewPager vpContent;
    private ViewPagerAdapter pagerAdapter;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_dashboard:

                    return true;
                case R.id.navigation_notifications:

                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
        initData();



    }

    private void initView(){
        navigation = (BottomNavigationViewEx) findViewById(R.id.navigation);
        vpContent = (ViewPager) findViewById(R.id.vp_content);
    }

    private void initListener(){
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void initData(){
        test();
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new RecentFragment());
        fragments.add(new PlanFragment());
        fragments.add(new HomeFragment());
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),fragments);
        vpContent.setAdapter(pagerAdapter);
        navigation.setupWithViewPager(vpContent,true);
    }

    private void test(){
        UserLander lander = new UserLander(this,"user0001");
        AccountDO model = new AccountDO();
        model.device_token = "1232131313";
        model.last_login_time = 14333333333L;
        model.name = "123";
        model.nick_name = "asdsadasd";
        try {
            AccountManager.getInstance(lander).testCreate(model);
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        List<Fragment> fragmentList;
        public ViewPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }

}
