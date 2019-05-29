package com.hangzhou.santa.datatunnel.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * Created by santa on 2019/5/27.
 */
public class DataTunnelActivityLifecycle {

    private final Application.ActivityLifecycleCallbacks lifecycleCallbacks =
            new Application.ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(Activity activity, Bundle bundle) {
                    refListener.onCreated(activity);
                }

                @Override
                public void onActivityStarted(Activity activity) {

                }

                @Override
                public void onActivityResumed(Activity activity) {

                }

                @Override
                public void onActivityPaused(Activity activity) {
                    refListener.onPaused(activity);
                }

                @Override
                public void onActivityStopped(Activity activity) {

                }

                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

                }

                @Override
                public void onActivityDestroyed(Activity activity) {
                    refListener.onDestroyed(activity);
                }
            };

    private final Application application;
    private final RefListener<Activity> refListener;

    public DataTunnelActivityLifecycle(Application application, RefListener<Activity> refListener) {
        this.application = application;
        this.refListener = refListener;
    }

    public void watchActivities() {
        stopWatchingActivities();
        application.registerActivityLifecycleCallbacks(lifecycleCallbacks);
    }

    public void stopWatchingActivities() {
        application.unregisterActivityLifecycleCallbacks(lifecycleCallbacks);
    }
}
