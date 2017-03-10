package com.jcrspace.common.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;

import org.xutils.DbManager;
import org.xutils.db.DbManagerImpl;

import java.io.File;

/**
 * Created by jiangchaoren on 2017/3/10.
 */

public class DbUtils {
    private static int getVersionCode(Context context) throws PackageManager.NameNotFoundException {
        PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        return packageInfo.versionCode;
    }

    /**
     * 返回xutils3.0的数据库管理类
     *
     * @param context
     * @param name
     * @return
     */
    public static DbManager getDbManager(Context context, String name) {
        return DbManagerImpl.getInstance(getDbManagerDaoConfig(context, null, name));
    }

    /**
     * 返回xutils3.0的数据库管理类
     *
     * @param context
     * @param name
     * @return
     */
    public static DbManager getDbManager(Context context, String rootName, String name) {
        return DbManagerImpl.getInstance(getDbManagerDaoConfig(context, rootName, name));
    }

    public static DbManager.DaoConfig getDbManagerDaoConfig(Context context, String rootName, String name) {
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                .setDbName(name);//创建数据库的名称
        daoConfig.setDbOpenListener(new DbManager.DbOpenListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onDbOpened(DbManager db) {
                // 开启WAL, 对写入加速提升巨大
                db.getDatabase().enableWriteAheadLogging();
            }
        })
                .setDbUpgradeListener(new DbUpgradeListener());//数据库更新操作
        if (!TextUtils.isEmpty(rootName)) {
            File dir;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                rootName = context.getDataDir().getPath() + File.separator + rootName;
                dir = new File(rootName);
            } else {
                dir =  context.getDir("databases", Context.MODE_PRIVATE);
                rootName = dir.getPath() + File.separator + rootName;
                dir = new File(rootName);
            }
            daoConfig.setDbDir(dir);
        }
        try {
            int versionCode = getVersionCode(context);
            daoConfig.setDbVersion(versionCode);//数据库版本号
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return daoConfig;

    }

    static class DbUpgradeListener implements DbManager.DbUpgradeListener {
        @Override
        public void onUpgrade(DbManager dbManager, int oldVersion, int newVersion) {

        }
    }
}
