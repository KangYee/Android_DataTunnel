package com.hangzhou.santa.datatunnel;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Map;

@DataTunnelFilter(key = "BActivity", accepts = {"MainActivity"})
public class BActivity extends AppCompatActivity implements DataTunnelProtocol{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void dataFromTunnel(Map<String, Object> map) {
        map.get("MainActivity");
    }

    @Override
    public Object dataToTunnel() {
        return null;
    }
}
