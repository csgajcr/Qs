package com.jcrspace.common.model;

import com.google.gson.annotations.SerializedName;
import com.jcrspace.common.model.weather.ResultBody;

/**
 * Created by jiangchaoren on 2017/5/24.
 */

public class WeatherSO {

    @SerializedName("showapi_res_code")
    public int resultCode;

    @SerializedName("showapi_res_error")
    public String resultError;

    @SerializedName("showapi_res_body")
    public ResultBody resultBody;


}
