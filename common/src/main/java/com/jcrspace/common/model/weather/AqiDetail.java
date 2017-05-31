package com.jcrspace.common.model.weather;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jiangchaoren on 2017/5/24.
 */

public class AqiDetail {
    @SerializedName("pm2_5")
    public String pm2_5;

    @SerializedName("pm10")
    public String pm10;

    @SerializedName("quality")
    public String quality;
}
