package com.jcrspace.common.model.weather;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jiangchaoren on 2017/5/24.
 */

public class Now {

    @SerializedName("aqiDetail")
    public AqiDetail aqiDetail;

    @SerializedName("wind_direction")
    public String windDirection;

    @SerializedName("wind_power")
    public String windPower;

    @SerializedName("sd")
    public String sd; //湿度

    @SerializedName("weather_pic")
    public String weatherPic;

    @SerializedName("weather")
    public String weather;

    @SerializedName("temperature")
    public String temperature;
}
