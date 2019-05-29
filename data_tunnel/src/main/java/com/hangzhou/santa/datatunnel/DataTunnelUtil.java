package com.hangzhou.santa.datatunnel;

import java.util.Map;

/**
 * Created by santa on 2019/5/28.
 */
public class DataTunnelUtil {

    /**
     * 如果accept多个，里面有多个相同key的值，取最新的
     * @param key
     * @param maps
     * @return
     */
    public static Object getNewestByKey(String key, Map<String, Object> maps) {
        return DataTunnel.getNewestBySubKey(key, maps);
    }

}
