package com.jcrspace.common.manager;

import com.jcrspace.common.config.ApiUrls;
import com.jcrspace.common.config.QsCommonConfig;
import com.jcrspace.common.lander.UserLander;

import org.xutils.HttpManager;
import org.xutils.http.RequestParams;
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

    public void getWeatherInfoFromServer(){
        RequestParams params = new RequestParams(ApiUrls.API_WEATHER_URL);
        params.addBodyParameter("cityname","重庆");
        params.addBodyParameter("key", QsCommonConfig.HAO_SERVICE_API_KEY);
    }

}
