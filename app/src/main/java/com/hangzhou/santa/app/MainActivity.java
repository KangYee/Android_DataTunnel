package com.hangzhou.santa.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.hangzhou.santa.datatunnel.DataTunnelFilter;
import com.hangzhou.santa.datatunnel.DataTunnelProtocol;
import com.hangzhou.santa.datatunnel.app.R;


import java.util.HashMap;
import java.util.Map;

@DataTunnelFilter(key = "MainActivity", accepts = {})
public class MainActivity extends AppCompatActivity implements DataTunnelProtocol {

    private Map<String, Object> map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        startActivity(new Intent(this, BActivity.class));
        finish();
    }

    @Override
    public void dataFromTunnel(Map<String, Object> map) {

    }

    @Override
    public Object dataReadyToTunnel() {
        map = new HashMap<>();
        map.put("from", "a");
        map.put("value", 1);
        return map;
    }

    @Override
    public void afterDataToTunnel() {
        map = null;
    }
}
