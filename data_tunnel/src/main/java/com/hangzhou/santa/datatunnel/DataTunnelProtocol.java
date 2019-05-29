package com.hangzhou.santa.datatunnel;

import java.util.Map;

/**
 * Created by santa on 2019/5/27.
 */
public interface DataTunnelProtocol {

    void dataFromTunnel(Map<String, Object> map) throws Exception;

    Object dataToTunnel();

}
