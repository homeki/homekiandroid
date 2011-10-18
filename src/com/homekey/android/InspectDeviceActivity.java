package com.homekey.android;

import android.app.Activity;
import android.os.Bundle;
import device.Device;

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