package com.jcrspace.qsmain.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.jcrspace.common.view.BaseAppCompatActivity;
import com.jcrspace.common.view.BottomNavigationViewEx;
import com.jcrspace.qsmain.R;
import com.jcrspace.ui_account.fragment.HomeFragment;
import com.jcrspace.ui_bill.fragment.BillFragment;
import com.jcrspace.ui_plan.fragment.PlanFragment;

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
                case R.id.navigation_bill:
                    setTitle(R.string.bill);
                    return true;
                case R.id.navigation_plan:
                    setTitle(R.string.plan);
                    return true;
                case R.id.navigation_recommend:
                    setTitle(R.string.recommend);
                    return true;
                case R.id.navigation_home:
                    setTitle(R.string.home);
                    return true;
            }
            return false;
        }

    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
        initData();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.action_settings){

        }

        return super.onOptionsItemSelected(item);
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
        fragments.add(new BillFragment(getLander()));
        fragments.add(new PlanFragment(getLander()));
        fragments.add(new HomeFragment(getLander()));
        fragments.add(new HomeFragment(getLander()));
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),fragments);
        vpContent.setAdapter(pagerAdapter);
        navigation.setupWithViewPager(vpContent,true);
        navigation.enableItemShiftingMode(false);
        navigation.enableShiftingMode(false);
    }

    private void test(){
//        UserLander lander = new UserLander(this,"user0001");
//        AccountDO model = new AccountDO();
//        model.device_token = "189dasodh218";
//        model.last_login_time = 14333333333L;
//        model.name = "123333";
//        model.nick_name = "asdsadasd";


//        try {
//            AccountManager.getInstance(lander).updateUserInfo(model);
//          AccountManager.getInstance(lander).deleteUserInfo();
//          AccountManager.getInstance(lander).setUserInfo(model);

//            AccountDO accountDO = AccountManager.getInstance(lander).readUserInfo();
//            accountDO.id=1;

//        } catch (DbException e) {
//            e.printStackTrace();
//        }

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
