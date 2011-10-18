package com.homeki.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        SharedPreferenceHelper.putStringValue(this, "server", "87.96.244.210:5000");
        
        startActivity(new Intent(this, DeviceList.class));
        finish();
    }
}