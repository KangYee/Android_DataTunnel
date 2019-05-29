package com.hangzhou.santa.datatunnel.lifecycle;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

/**
 * Created by santa on 2019/5/27.
 */
public class DataTunnelFragmentLifecycle {

    private final FragmentManager.FragmentLifecycleCallbacks lifecycleCallbacks =
            new FragmentManager.FragmentLifecycleCallbacks(){
                @Override
                public void onFragmentCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @Nullable Bundle savedInstanceState) {
                    refListener.onCreated(f);
                }

                @Override
                public void onFragmentPaused(@NonNull FragmentManager fm, @NonNull Fragment f) {
                    refListener.onPaused(f);

                }

                @Override
                public void onFragmentDestroyed(@NonNull FragmentManager fm, @NonNull Fragment f) {
                    refListener.onDestroyed(f);

                }
            };

    private final Activity activity;
    private final RefListener<Fragment> refListener;

    public DataTunnelFragmentLifecycle(Activity activity, RefListener<Fragment> refListener) {
        this.activity = activity;
        this.refListener = refListener;
    }

    public void watchFragments() {
        if (!(activity instanceof FragmentActivity)) {
            Log.e("FragmentLifecycle", "if want watch fragments, activity need extend FragmentActivity");
            return;
        }
        stopWatchingFragments();
        ((FragmentActivity) activity).getSupportFragmentManager().registerFragmentLifecycleCallbacks(lifecycleCallbacks, true);
    }

    public void stopWatchingFragments() {
        if (!(activity instanceof FragmentActivity)) {
            return;
        }
        ((FragmentActivity) activity).getSupportFragmentManager().unregisterFragmentLifecycleCallbacks(lifecycleCallbacks);
    }
}
