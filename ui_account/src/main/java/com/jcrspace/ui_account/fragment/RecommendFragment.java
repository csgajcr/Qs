package com.jcrspace.ui_account.fragment;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blankj.utilcode.utils.ToastUtils;
import com.jcrspace.common.lander.UserLander;
import com.jcrspace.common.manager.RecommendManager;
import com.jcrspace.common.model.WeatherSO;
import com.jcrspace.common.view.BaseFragment;
import com.jcrspace.ui_account.R;

import org.xutils.x;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
    private TextView tvWindDirection;
    private TextView tvWindPower;
    private TextView tvHumidity;
    private TextView tvPM2_5;
    private TextView tvQuality;
    private TextView tvHelloWorld;
    private LinearLayout llWeather;
    private TextView tvAuth;

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
        tvWindDirection = findViewById(R.id.tv_wind_direction);
        tvWindPower = findViewById(R.id.tv_wind_power);
        tvHumidity = findViewById(R.id.tv_humidity);
        tvPM2_5 = findViewById(R.id.tv_pm2_5);
        tvQuality = findViewById(R.id.tv_quality);
        tvHelloWorld = findViewById(R.id.tv_hello_world);
        llWeather = findViewById(R.id.ll_weather);
    }

    @Override
    protected void initListener() {
        onGetWeatherCompleteListener = new RecommendManager.OnGetWeatherCompleteListener() {
            @Override
            public void onSuccess(WeatherSO weatherSO) {
                flEmptyView.setVisibility(View.GONE);
                llWeather.setVisibility(View.VISIBLE);
                renderWeatherItem(weatherSO);
            }

            @Override
            public void onFailed(Throwable throwable) {
                flEmptyView.setVisibility(View.VISIBLE);
                llWeather.setVisibility(View.GONE);
//                cvMain.setVisibility(View.GONE);
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
        renderHelloWords();
    }

    private void startGetRecommendInfo(){
        recommendManager.getWeatherInfoFromServer(onGetWeatherCompleteListener);
    }

    private void renderWeatherItem(WeatherSO weatherSO){
        cvMain.setVisibility(View.VISIBLE);
        x.image().bind(ivWeatherIcon,weatherSO.resultBody.now.weatherPic);
        tvTemperature.setText(weatherSO.resultBody.now.temperature);
        tvWeather.setText(weatherSO.resultBody.now.weather);
        tvQuality.setText(getString(R.string.aqi_quality) + weatherSO.resultBody.now.aqiDetail.quality);
        tvWindDirection.setText(getString(R.string.wind_direction) + weatherSO.resultBody.now.windDirection);
        tvWindPower.setText(getString(R.string.wind_power) + weatherSO.resultBody.now.windPower);
        tvHumidity.setText(getString(R.string.humidity) + weatherSO.resultBody.now.sd);
        tvPM2_5.setText(getString(R.string.PM2_5) + weatherSO.resultBody.now.aqiDetail.pm2_5);

    }

    private void renderHelloWords(){
        List<String> wordList = getWordsFromAssets("hello_words");
        Random random = new Random();
        int randomNumber =  random.nextInt(wordList.size()-1);
        tvHelloWorld.setText(getString(R.string.hello_words).replace("{words}",wordList.get(randomNumber)));
    }

    public List<String> getWordsFromAssets(String fileName){
        try {
            InputStreamReader inputReader = new InputStreamReader( getResources().getAssets().open(fileName) );
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line="";
            List<String> list = new ArrayList<>();
            while((line = bufReader.readLine()) != null)
                list.add(line);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
