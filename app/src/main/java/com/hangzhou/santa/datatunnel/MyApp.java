package com.hangzhou.santa.datatunnel;

import android.app.Application;

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
