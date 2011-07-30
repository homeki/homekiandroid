package com.homekey.android;

import device.Device;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class InspectDeviceActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
//        SharedPreferenceHelper.putStringValue(this, "server", "192.168.0.65:5000");
        Intent i = getIntent();
        Device d = (Device)i.getParcelableExtra("device");
        setContentView(d.getContentView());
        
//        d.startActivity();
//        startActivity(new Intent(this, DeviceList.class));
//        setContentView(R.layout.inspectdevice);
    }
}