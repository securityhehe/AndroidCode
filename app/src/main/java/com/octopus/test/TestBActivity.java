package com.octopus.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.octopus.test.app.OctopusApplication;

public class TestBActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        initView();
    }

    private void initView() {
        View view = findViewById(R.id.startService);
        final View stop = findViewById(R.id.stopService);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OctopusApplication instance = OctopusApplication.instance();
                OctopusApplication.MainProcess processContext = (OctopusApplication.MainProcess)instance.getProcessContext(OctopusApplication.MAIN_PROCESS);
                processContext.getLoginClient().initLogService(instance);
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OctopusApplication instance = OctopusApplication.instance();
                OctopusApplication.MainProcess processContext = (OctopusApplication.MainProcess)instance.getProcessContext(OctopusApplication.MAIN_PROCESS);
                processContext.getLoginClient().destroyService();
            }
        });
    }

}
