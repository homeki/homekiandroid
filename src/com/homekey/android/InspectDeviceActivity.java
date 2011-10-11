package com.homekey.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import device.Device;

public class InspectDeviceActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        Intent i = getIntent();
        Device d = (Device)i.getParcelableExtra("device");
        setContentView(d.getView(this));
        
        setContentView(R.layout.inspectdevice);
    }
}