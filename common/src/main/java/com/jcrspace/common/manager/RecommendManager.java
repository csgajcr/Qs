package com.jcrspace.common.manager;

import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.NetworkUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.google.gson.Gson;
import com.jcrspace.common.config.ApiUrls;
import com.jcrspace.common.config.QsCommonConfig;
import com.jcrspace.common.lander.UserLander;
import com.jcrspace.common.model.WeatherSO;

import org.xutils.HttpManager;
import org.xutils.common.Callback;
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

    public void getWeatherInfoFromServer(final OnGetWeatherCompleteListener onGetWeatherCompleteListener){
        RequestParams params = new RequestParams(ApiUrls.API_WEATHER_URL);
        params.addBodyParameter("area", QsCommonConfig.LOCAL_ADDRESS_CITY_NAME);
        params.addHeader("Authorization","APPCODE " + QsCommonConfig.ALIYUN_API_CODE);
        params.setCacheMaxAge(0);
        httpManager.get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                WeatherSO weatherSO = new Gson().fromJson(result,WeatherSO.class);
                onGetWeatherCompleteListener.onSuccess(weatherSO);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                onGetWeatherCompleteListener.onFailed(ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    public interface OnGetWeatherCompleteListener{

        void onSuccess(WeatherSO weatherSO);

        void onFailed(Throwable throwable);
    }
}
