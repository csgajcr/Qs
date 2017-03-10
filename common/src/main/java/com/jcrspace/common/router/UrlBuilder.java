package com.jcrspace.common.router;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.jcrspace.common.config.QsCommonConfig;

/**
 * Created by jiangchaoren on 2017/2/27.
 */

public class UrlBuilder {

    private Activity activity;
    private static String rootPath = QsCommonConfig.SCHEME+"://"+QsCommonConfig.HOST+"/";
    public static UrlBuilder build(Activity activity,String path){
        String url = rootPath + path;
        UrlBuilder urlBuilder = new UrlBuilder(url);
        urlBuilder.activity = activity;
        return urlBuilder;
    }

    private Uri.Builder uriBuilder;
    public UrlBuilder() {
        uriBuilder = new Uri.Builder();
    }

    public UrlBuilder(String url){
        uriBuilder = Uri.parse(url).buildUpon();
    }

    public UrlBuilder setUrl(String url) {
        uriBuilder = Uri.parse(url).buildUpon();
        return this;
    }

    public void startActivity(){
        Uri uri = uriBuilder.build();
        final Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.startActivity(intent);
            }
        });
    }
}
