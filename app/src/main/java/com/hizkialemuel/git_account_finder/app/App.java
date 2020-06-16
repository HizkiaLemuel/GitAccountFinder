package com.hizkialemuel.git_account_finder.app;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import com.hizkialemuel.git_account_finder.BuildConfig;
import com.socks.library.KLog;

public class App extends Application {

    private static Context mApplicationContext;
    private Thread.UncaughtExceptionHandler mExceptionHandler;

    public static App getApplication() {
        return (App) mApplicationContext;
    }

    public static Context getContext() {
        return mApplicationContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationContext = this;

        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                    new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
            StrictMode.setVmPolicy(
                    new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
        }

        if (!BuildConfig.DEBUG) {
            mExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
            Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

                @Override
                public void uncaughtException(final Thread thread, final Throwable ex) {
                    mExceptionHandler.uncaughtException(thread, ex);
                }
            });
        }

        KLog.v("=========== BaseApplication onCreate ===========");


        KLog.init(BuildConfig.DEBUG);
    }
}
