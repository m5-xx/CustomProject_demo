package com.mayday.xy.customproject;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import io.vov.vitamio.Vitamio;

/**
 * Created by xy-pc on 2017/5/22.
 */

public class MyApplication extends Application {
    private Context context;
    //是否处于播放状态
    public boolean isYes=false;

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        Vitamio.isInitialized(getApplicationContext());
    }
    public Context getContext(){
        return  context;
    }
}
