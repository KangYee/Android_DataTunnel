package com.hangzhou.santa.datatunnel;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import com.hangzhou.santa.datatunnel.lifecycle.DataTunnelActivityLifecycle;
import com.hangzhou.santa.datatunnel.lifecycle.DataTunnelFragmentLifecycle;
import com.hangzhou.santa.datatunnel.lifecycle.RefListener;
import com.hangzhou.santa.datatunnel.tunnel.DataTunnelBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by santa on 2019/5/27.
 */
public class DataTunnel {

    private static Map<String, DataTunnel> mTunnels = new ConcurrentHashMap<>();

    private DataTunnel(String key) {
        DataTunnel dataTunnel = mTunnels.get(key);
        if (dataTunnel == null) {
            dataTunnel = new DataTunnelBuilder().setName(key).builder();
            mTunnels.put(key, dataTunnel);
        }
    }

    public static DataTunnelBuilder builder() {
        return new DataTunnelBuilder();
    }

    public static DataTunnel getDefault() {
        return new DataTunnel("");
    }

    public static DataTunnel getTunnel(String key) {
        return new DataTunnel(key);
    }

    public DataTunnel(DataTunnelBuilder builder) {

    }

    private static Map<String, Object> dataCacheMap = new ConcurrentHashMap<>();
    private static Map<String, Object> dataMap = new ConcurrentHashMap<>();
    private static List<String> keyList = new CopyOnWriteArrayList<>();
    private static DataTunnelActivityLifecycle mDataTunnelActivityLifecycle;
    private static Map<Activity, DataTunnelFragmentLifecycle> mDataTunnelFragmentLifecycles = new ConcurrentHashMap<>();

    public static void install(Application application) {
        final RefListener<Fragment> fragmentRefListener = new RefListener<Fragment>() {
            @Override
            public void onCreated(Fragment fragment) {
                DataTunnelFilter dataTunnelFilter = getFilter(fragment.getClass());
                if (dataTunnelFilter != null && fragment instanceof DataTunnelProtocol) {
                    DataTunnel.onCreated(dataTunnelFilter, (DataTunnelProtocol) fragment);
                }
            }

            @Override
            public void onPaused(Fragment fragment) {
                DataTunnelFilter dataTunnelFilter = getFilter(fragment.getClass());
                if (dataTunnelFilter != null && fragment instanceof DataTunnelProtocol) {
                    DataTunnel.onPaused(dataTunnelFilter, (DataTunnelProtocol) fragment);
                    removeDataCache(dataTunnelFilter.key(), (DataTunnelProtocol) fragment);
                }
            }

            @Override
            public void onDestroyed(Fragment fragment) {
                DataTunnelFilter dataTunnelFilter = getFilter(fragment.getClass());
                if (dataTunnelFilter != null && fragment instanceof DataTunnelProtocol) {
                    DataTunnel.onDestroyed(dataTunnelFilter);
                }
            }
        };

        mDataTunnelActivityLifecycle = new DataTunnelActivityLifecycle(application, new RefListener<Activity>() {
            @Override
            public void onCreated(Activity activity) {
                if (mDataTunnelFragmentLifecycles.get(activity) == null) {
                    DataTunnelFragmentLifecycle dataTunnelFragmentLifecycle = new DataTunnelFragmentLifecycle(activity, fragmentRefListener);
                    dataTunnelFragmentLifecycle.watchFragments();
                    mDataTunnelFragmentLifecycles.put(activity, dataTunnelFragmentLifecycle);
                }

                DataTunnelFilter dataTunnelFilter = getFilter(activity.getClass());
                if (dataTunnelFilter != null && activity instanceof DataTunnelProtocol) {
                    DataTunnel.onCreated(dataTunnelFilter, (DataTunnelProtocol) activity);
                }
            }

            @Override
            public void onPaused(Activity activity) {
                DataTunnelFilter dataTunnelFilter = getFilter(activity.getClass());
                if (dataTunnelFilter != null && activity instanceof DataTunnelProtocol) {
                    DataTunnel.onPaused(dataTunnelFilter, (DataTunnelProtocol) activity);
                    removeDataCache(dataTunnelFilter.key(), (DataTunnelProtocol) activity);
                }
            }

            @Override
            public void onDestroyed(Activity activity) {
                if (mDataTunnelFragmentLifecycles.get(activity) != null) {
                    DataTunnelFragmentLifecycle dataTunnelFragmentLifecycle = mDataTunnelFragmentLifecycles.get(activity);
                    if (dataTunnelFragmentLifecycle != null) {
//                        dataTunnelFragmentLifecycle.stopWatchingFragments();
                        mDataTunnelFragmentLifecycles.remove(activity);
                    }
                }
                DataTunnelFilter dataTunnelFilter = getFilter(activity.getClass());
                if (dataTunnelFilter != null && activity instanceof DataTunnelProtocol) {
                    DataTunnel.onDestroyed(dataTunnelFilter);
                }
            }
        });
        mDataTunnelActivityLifecycle.watchActivities();

    }


    public static void unInstall() {
        if (mDataTunnelActivityLifecycle != null) {
            mDataTunnelActivityLifecycle.stopWatchingActivities();
        }
        for (DataTunnelFragmentLifecycle dataTunnelFragmentLifecycle : mDataTunnelFragmentLifecycles.values()) {
            dataTunnelFragmentLifecycle.stopWatchingFragments();
            mDataTunnelFragmentLifecycles.clear();
        }

    }

    private static DataTunnelFilter getFilter(Class clazz) {
        boolean hasAnnotation = clazz.isAnnotationPresent(DataTunnelFilter.class);
        if (hasAnnotation) {
            return (DataTunnelFilter) clazz.getAnnotation(DataTunnelFilter.class);
        }
        return null;
    }


    private synchronized static void onCreated(DataTunnelFilter filter, DataTunnelProtocol protocol) {
        Map<String, Object> data = new HashMap<>();
        for (String accept : filter.accepts()) {
            Object o = dataMap.get(accept);
            if (o != null) {
                data.put(accept, o);
            }
        }
        try {
            protocol.dataFromTunnel(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized static void onPaused(DataTunnelFilter filter, DataTunnelProtocol protocol) {
        Object o = protocol.dataReadyToTunnel();
        if (o != null) {
            dataMap.put(filter.key(), o);
            if (!keyList.isEmpty() && filter.key().equals(keyList.get(keyList.size() - 1))) {
                return;
            }

            for (String key : keyList) {
                if (key.equals(filter.key())) {
                    keyList.remove(key);
                }
            }
            keyList.add(filter.key());
        }
    }


    private synchronized static void onDestroyed(DataTunnelFilter filter) {
        dataMap.remove(filter.key());
        keyList.remove(filter.key());
    }


    synchronized static Object getNewestBySubKey(String subKey, Map<String, Object> maps) {
        if (keyList.isEmpty()) {
            throw new IllegalStateException("getNewestByKey can not call after pause -> resume");
        }
        for (int i = keyList.size() - 1; i >= 0; i--) {
            for (String mapKey : maps.keySet()) {
                if (keyList.get(i).equals(mapKey)) {
                    Object o = maps.get(mapKey);
                    if (o instanceof Map) {
                        Object subVal = ((Map)o).get(subKey);
                        if (subVal != null) {
                            return subVal;
                        }
                    }
                }
            }
        }
        throw new IllegalStateException("getNewestByKey can not call after pause -> resume");
    }


    static void putDataCache(String key, Object object) {
        dataCacheMap.put(key, object);
    }

    static Object getDataCache(String key) {
        return dataCacheMap.get(key);
    }

    private static void removeDataCache(String key, DataTunnelProtocol dataTunnelProtocol) {
        dataCacheMap.remove(key);
        dataTunnelProtocol.afterDataToTunnel();
    }
}
