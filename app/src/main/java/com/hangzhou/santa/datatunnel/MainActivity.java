package com.hangzhou.santa.datatunnel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

@DataTunnelFilter(key = "MainActivity", accepts = {})
public class MainActivity extends AppCompatActivity implements DataTunnelProtocol {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        startActivity(new Intent(this, BActivity.class));
    }

    @Override
    public void dataFromTunnel(Map<String, Object> map) {

    }

    @Override
    public Object dataToTunnel() {
        Map<String, Object> map = new HashMap<>();
        map.put("from", "a");
        map.put("value", 1);
        return map;
    }
}
