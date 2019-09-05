package com.hangzhou.santa.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.hangzhou.santa.datatunnel.DataTunnelFilter;
import com.hangzhou.santa.datatunnel.DataTunnelProtocol;
import com.hangzhou.santa.datatunnel.app.R;


import java.util.Map;

@DataTunnelFilter(key = "BActivity", accepts = {"MainActivity"})
public class BActivity extends AppCompatActivity implements DataTunnelProtocol {

    TextView mTextView;
    String content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);
        mTextView = findViewById(R.id.b_text);
        mTextView.setText(content);

    }

    @Override
    public void dataFromTunnel(Map<String, Object> map) {
        content = map.get("MainActivity").toString();
    }

    @Override
    public Object dataReadyToTunnel() {
        return null;
    }

    @Override
    public void afterDataToTunnel() {

    }
}
