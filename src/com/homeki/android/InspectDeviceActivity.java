package com.homeki.android;

import com.homeki.android.device.Device;

import android.app.Activity;
import android.os.Bundle;

public class InspectDeviceActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        int i = getIntent().getIntExtra("device", -1);
        Device d = ((HomekiApplication)getApplication()).getList().get(i);
        setContentView(d.getView(this));
    }
}