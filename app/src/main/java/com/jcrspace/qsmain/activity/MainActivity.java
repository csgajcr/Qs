package com.jcrspace.qsmain.activity;

import android.content.DialogInterface;
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

import com.blankj.utilcode.utils.ConstUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.google.gson.Gson;
import com.jcrspace.common.Qs;
import com.jcrspace.common.config.ActivityUrls;
import com.jcrspace.common.config.QsCommonConfig;
import com.jcrspace.common.dialog.LoadingDialog;
import com.jcrspace.common.dialog.TipDialog;
import com.jcrspace.common.lander.UserLander;
import com.jcrspace.common.manager.TokenManager;
import com.jcrspace.common.router.UrlBuilder;
import com.jcrspace.common.view.BaseAppCompatActivity;
import com.jcrspace.common.view.BottomNavigationViewEx;
import com.jcrspace.manager_account.AccountManager;
import com.jcrspace.manager_account.event.LoginCompleteEvent;
import com.jcrspace.manager_account.event.LogoutEvent;
import com.jcrspace.manager_account.model.AccountSO;
import com.jcrspace.manager_account.model.AccountSOList;
import com.jcrspace.manager_bill.BillManager;
import com.jcrspace.manager_bill.listener.SyncCompleteListener;
import com.jcrspace.manager_bill.model.BillDO;
import com.jcrspace.manager_statistics.event.ChartAnimateEvent;
import com.jcrspace.qsmain.R;
import com.jcrspace.manager_bill.event.SyncCompleteEvent;
import com.jcrspace.ui_account.fragment.HomeFragment;
import com.jcrspace.ui_account.fragment.RecommendFragment;
import com.jcrspace.ui_bill.fragment.BillFragment;
import com.jcrspace.ui_statistics.fragment.StatisticsFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class MainActivity extends BaseAppCompatActivity {

    private TextView mTextMessage;
    private BottomNavigationViewEx navigation;
    private ViewPager vpContent;
    private ViewPagerAdapter pagerAdapter;
    private LoadingDialog loadingDialog;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_bill:
                    setTitle(R.string.bill);
                    return true;
                case R.id.navigation_plan:
                    setTitle(R.string.statistics);
                    EventBus.getDefault().post(new ChartAnimateEvent());
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
        if (id==R.id.action_sync){
            /**
             * 判断是否登录
             */
            if (getLander().getId().equals(UserLander.DEFAULT_LOCAL_USER_ID)){
                TipDialog dialog = new TipDialog(this,getString(R.string.not_login_cant_sync));
                dialog.show();
            } else {
                startSync();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView(){
        navigation = (BottomNavigationViewEx) findViewById(R.id.navigation);
        vpContent = (ViewPager) findViewById(R.id.vp_content);
        loadingDialog = new LoadingDialog(this);
    }

    private void initListener(){
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void initData(){
        test();
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new BillFragment());
        fragments.add(new StatisticsFragment());
        fragments.add(new RecommendFragment());
        fragments.add(new HomeFragment());
        EventBus.getDefault().register(fragments.get(0)); //为BillFragment绑定EventBus
        EventBus.getDefault().register(fragments.get(1));
        EventBus.getDefault().register(fragments.get(3));
        EventBus.getDefault().register(this);
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),fragments);
        vpContent.setAdapter(pagerAdapter);
        navigation.setupWithViewPager(vpContent,true);
        navigation.enableItemShiftingMode(false);
        navigation.enableShiftingMode(false);
        vpContent.setOffscreenPageLimit(3);
        setTitle(R.string.bill);
        /**
         * 判断自动同步
         */
        if (Qs.getConfigSharedPreferences().getBoolean(QsCommonConfig.SP_IS_AUTO_SYNC_BILL,true) && !getLander().getId().equals(UserLander.DEFAULT_LOCAL_USER_ID)){
            /**
             * 同步时间间隔大于一天
             */
            if (System.currentTimeMillis() - Qs.getConfigSharedPreferences().getLong(QsCommonConfig.SP_LAST_SYNC_TIME,0)> ConstUtils.DAY){
                startSync();
                Qs.getConfigSharedPreferences().edit().putLong(QsCommonConfig.SP_LAST_SYNC_TIME,System.currentTimeMillis()).apply();
            }
        }
        /**
         * 判断多终端登录
         */
        if (!getLander().getId().equals(UserLander.DEFAULT_LOCAL_USER_ID)){
            final AccountManager accountManager = new AccountManager(getLander());
            accountManager.findAccountFromServer(getLander().getId(), new QueryListener<JSONArray>() {
                @Override
                public void done(JSONArray jsonArray, BmobException e) {
                    if (e!=null){
                        return;
                    }
                    Gson gson = new Gson();
                    final AccountSOList accountList = gson.fromJson("{accountSOList:"+jsonArray.toString()+"} ",AccountSOList.class);
                    List<AccountSO> list = accountList.accountSOList;
                    if (list.size()>0){
                        AccountSO accountSO = list.get(0);
                        if (!accountSO.device_token.equals(TokenManager.calcToken(getLander().getId()))){
                            //Token验证失败，强制退出登录
                            TipDialog dialog = new TipDialog(MainActivity.this,getString(R.string.mutidevice_login));
                            dialog.setCancelable(false);
                            dialog.setOnClickListener(new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    UrlBuilder.build(MainActivity.this, ActivityUrls.LOGIN).startActivity();
                                }
                            });
                            dialog.show();
                            accountManager.logout();
                            EventBus.getDefault().post(new LogoutEvent());

                        }
                    }
                }
            });
        }

    }

    private void startSync(){
        loadingDialog.show();
        BillManager manager = BillManager.getInstance(getLander());
        manager.syncBillFromServer(new SyncCompleteListener() {
            @Override
            public void onSuccess() {
                onSyncCompleteEvent(new SyncCompleteEvent(true,""));
            }

            @Override
            public void onError(Exception e) {
                if (e instanceof DbException){
                    onSyncCompleteEvent(new SyncCompleteEvent(false,getString(R.string.local_database_error)));
                } else if (e instanceof BmobException){
                    onSyncCompleteEvent(new SyncCompleteEvent(false,getString(R.string.network_connect_failed)));
                }
            }
        });
    }

    private void test(){

//        getLander().changeAccount("13101375734");
//        DbManager dbManager = getLander().getDbManager();
//        try {
//            dbManager.delete(BillDO.class, WhereBuilder.b("id","=",3));
//            dbManager.delete(BillDO.class, WhereBuilder.b("id","=",4));
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
//        UserLander lander = new UserLander(this,"user0001");
//        AccountDO model = new AccountDO();
//        model.device_token = "189dasodh218";
//        model.last_login_time = 14333333333L;
//        model.mobile = "123333";
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

    @Override
    protected void onDestroy() {
        //取消注册
        EventBus.getDefault().unregister(pagerAdapter.fragmentList.get(0));
        EventBus.getDefault().unregister(pagerAdapter.fragmentList.get(1));
        EventBus.getDefault().unregister(pagerAdapter.fragmentList.get(3));
        EventBus.getDefault().unregister(this);
        super.onDestroy();
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSyncCompleteEvent(SyncCompleteEvent event){
        loadingDialog.dismiss();
        if (event.isSuccess){
            ToastUtils.showShortToast(R.string.sync_bill_success);
        } else {
            ToastUtils.showShortToast(event.errorMessage);
        }
    }


}
