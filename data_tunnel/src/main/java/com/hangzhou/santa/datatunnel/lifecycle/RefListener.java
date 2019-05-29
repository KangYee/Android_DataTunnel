package com.hangzhou.santa.datatunnel.lifecycle;

/**
 * Created by santa on 2019/5/27.
 */
public interface RefListener<T> {

    void onCreated(T activity);
    void onPaused(T activity);
    void onDestroyed(T activity);


}
