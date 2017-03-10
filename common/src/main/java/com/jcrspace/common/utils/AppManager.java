package com.jcrspace.common.utils;

import android.app.Activity;
import android.content.Context;

import java.util.Stack;

/**
 * Created by jiangchaoren on 2017/3/10.
 */

public class AppManager {

    private static Stack<Activity> activityStack;
    private static AppManager instance;

    public AppManager() {
    }

    public static synchronized AppManager getInstance() {
        if (instance==null){
            instance=new AppManager();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 从堆栈中移除Activity
     */
    public void removeActivity(Activity activity) {
        if (activityStack != null) {
            activityStack.remove(activity);
        }
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public static Activity currentActivity() {
        Activity activity = null;
        if (activityStack != null && !activityStack.isEmpty()) {
            activity = activityStack.lastElement();
        }
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
        }
    }

    public Activity findActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                return activity;
            }
        }
        return null;
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        for (Activity aty : activityStack) {
            if (aty != null) {
                aty.finish();
            }
        }
//		for (int i = 0, size = activityStack.size(); i < size; i++) {
//			if (null != activityStack.get(i)) {
//				activityStack.get(i).finish();
//			}
//		}
        activityStack.clear();
    }

    /**
     * 结束所有其他Activity
     */
    public void finishAllOtherActivity(Activity act) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        for (int i = 0; i < activityStack.size(); i++) {
            Activity aty = activityStack.get(i);
            if (aty != null && !act.equals(aty)) {
                aty.finish();
                activityStack.remove(i);
                i--;
            }
        }
    }

    /**
     * 退出应用程序
     */

    @SuppressWarnings("deprecation")
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            android.app.ActivityManager activityMgr = (android.app.ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            if (android.os.Build.VERSION.SDK_INT < 8) {
                activityMgr.restartPackage(context.getPackageName());
            } else {
                activityMgr.killBackgroundProcesses(context.getPackageName());
            }
            System.exit(0);
        } catch (Exception e) {
        }
    }

    /**
     * 获取Acitivity栈中指定索引处的Activity (最顶部为0)
     */
    public Activity get(int index) {
        int stackIndex = size() - index - 1;
        if (stackIndex < 0) {
            stackIndex = 0;
        }
        return activityStack.get(stackIndex);
    }

    /**
     * 获取Acitivity栈中Activity数量
     */
    public int size() {
        return activityStack.size();
    }


}
