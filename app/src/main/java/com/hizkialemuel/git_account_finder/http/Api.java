package com.hizkialemuel.git_account_finder.http;

import android.os.Build;
import android.text.TextUtils;

import com.hizkialemuel.git_account_finder.BuildConfig;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Api {
    public static final String API_HOST = "https://api.github.com/";
    public static final String DEV_API_HOST = "https://api.github.com/";

    public static final int DEFAULT_COUNT_PER_PAGE = 20;
    public static final int MAX_REQUEST_COUNT = 20;
    public static final TimeUnit MAX_REQUEST_TIMEUNIT = TimeUnit.MINUTES;

    public static String getHost(int hostType) {
        boolean isDevEnv = BuildConfig.TARGET_ENV.equals("development");
        if (isDevEnv)
            return DEV_API_HOST;
        return API_HOST;
    }

    public static HashMap<String, Object> getBasicParam() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("per_page", DEFAULT_COUNT_PER_PAGE);

        KLog.v("--------------------------------------------Request Basic Param Start----------------------------------------------------");
        KLog.json(map.toString());
        KLog.v("--------------------------------------------Response Basic Param End----------------------------------------------------");

        return map;
    }


}
