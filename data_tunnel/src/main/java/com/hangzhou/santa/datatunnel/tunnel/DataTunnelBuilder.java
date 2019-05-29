package com.hangzhou.santa.datatunnel.tunnel;

import com.hangzhou.santa.datatunnel.DataTunnel;

/**
 * Created by santa on 2019/5/27.
 */
public class DataTunnelBuilder {
    String name = "";

    public DataTunnelBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public DataTunnel builder() {
        return new DataTunnel(this);
    }
}
