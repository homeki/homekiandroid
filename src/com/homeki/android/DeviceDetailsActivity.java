package com.homeki.android;

import com.homeki.android.model.DeviceListModel;
import com.homeki.android.model.devices.AbstractDevice;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

public class DeviceDetailsActivity extends Activity {

	public static final String EXTRA_DEVICE_ID = "deviceId";

	private AbstractDevice mDevice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();
		if (extras != null && extras.containsKey(EXTRA_DEVICE_ID)) {
			mDevice = DeviceListModel.getModel().getDeviceWithId(extras.getInt(EXTRA_DEVICE_ID));
		}

		if (mDevice == null) {
			Toast toast = Toast.makeText(this, "No device id provided", Toast.LENGTH_SHORT);
			toast.show();
			finish();
		}

		setContentView(R.layout.details_activity);
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.add(R.id.device_details_root, new DeviceDetailsFragment(mDevice), "details");
		transaction.commit();
	}
}
