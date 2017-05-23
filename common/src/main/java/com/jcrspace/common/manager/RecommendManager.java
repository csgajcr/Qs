package com.jcrspace.common.manager;

import com.jcrspace.common.lander.UserLander;

import org.xutils.HttpManager;
import org.xutils.x;

/**
 * Created by jiangchaoren on 2017/5/24.
 */

public class RecommendManager extends BaseManager {

    private UserLander lander;
    private HttpManager httpManager;

    public static RecommendManager getInstance(UserLander userLander){
        try {
            return userLander.getManager(RecommendManager.class);
        } catch (ClassNotFoundException e){
            return userLander.putManager(new RecommendManager(userLander));
        }
    }

    public RecommendManager(UserLander lander) {
        this.lander = lander;
        httpManager = x.http();
    }
    
}
