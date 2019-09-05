package com.hangzhou.santa.app;

import android.app.Application;

import com.hangzhou.santa.datatunnel.DataTunnel;

/**
 * Created by santa on 2019/5/27.
 */
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DataTunnel.install(this);
    }


}
