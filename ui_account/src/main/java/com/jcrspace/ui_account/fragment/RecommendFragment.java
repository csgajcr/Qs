package com.jcrspace.ui_account.fragment;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcrspace.common.lander.UserLander;
import com.jcrspace.common.manager.RecommendManager;
import com.jcrspace.common.model.WeatherSO;
import com.jcrspace.common.view.BaseFragment;
import com.jcrspace.ui_account.R;

import org.xutils.x;

/**
 * Created by Jcr on 2017/5/18 0018.
 */

public class RecommendFragment extends BaseFragment {

    private RecommendManager recommendManager;

    private ImageView ivWeatherIcon;
    private TextView tvWeather;
    private TextView tvTemperature;
    private FrameLayout flEmptyView;
    private CardView cvMain;

    private RecommendManager.OnGetWeatherCompleteListener onGetWeatherCompleteListener;


    public RecommendFragment(UserLander lander) {
        super(lander);
        recommendManager = RecommendManager.getInstance(lander);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_recommend;
    }

    @Override
    protected void initView() {
        ivWeatherIcon = findViewById(R.id.iv_weather_icon);
        tvWeather = findViewById(R.id.tv_weather);
        tvTemperature = findViewById(R.id.tv_temp);
        flEmptyView = findViewById(R.id.fl_empty_view);
        cvMain = findViewById(R.id.cv_main);
    }

    @Override
    protected void initListener() {
        onGetWeatherCompleteListener = new RecommendManager.OnGetWeatherCompleteListener() {
            @Override
            public void onSuccess(WeatherSO weatherSO) {
                flEmptyView.setVisibility(View.GONE);
                cvMain.setVisibility(View.VISIBLE);
                x.image().bind(ivWeatherIcon,weatherSO.resultBody.now.weatherPic);
                tvTemperature.setText(weatherSO.resultBody.now.temperature);
                tvWeather.setText(weatherSO.resultBody.now.weather);
            }

            @Override
            public void onFailed(Throwable throwable) {
                flEmptyView.setVisibility(View.VISIBLE);
                cvMain.setVisibility(View.GONE);
            }
        };

        flEmptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGetRecommendInfo();
            }
        });
    }

    @Override
    protected void initData() {
        startGetRecommendInfo();
    }

    private void startGetRecommendInfo(){
        recommendManager.getWeatherInfoFromServer(onGetWeatherCompleteListener);
    }
}
