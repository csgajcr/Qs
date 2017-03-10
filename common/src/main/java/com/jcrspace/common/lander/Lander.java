package com.jcrspace.common.lander;

import android.content.Context;

import org.xutils.DbManager;

/**
 * Created by jiangchaoren on 2017/3/10.
 */

public interface Lander {
    String getId();

    DbManager getDbManager();

    <T> T getManager(Class<?> managerType) throws ClassNotFoundException;

    <T> T putManager(T manager);

    Context getContext();
}
