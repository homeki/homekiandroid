package com.homeki.android;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;
import com.homeki.android.model.DeviceListModel;
import com.homeki.android.model.devices.Device;
import com.homeki.android.server.ActionPerformer;
import com.homeki.android.server.ActionPerformer.OnDeviceListReceivedListener;
import com.homeki.android.server.ServerActionPerformer;

import java.util.List;

public class DeviceListActivity extends BaseActivity {
	private ProgressDialog progressDialog;
	private DeviceListModel model;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		model = DeviceListModel.getModel();

		progressDialog = new ProgressDialog(this);
		progressDialog.setIndeterminate(true);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage("Loading...");
	}

	@Override
	protected Fragment getFragment() {
		return new DeviceListFragment();
	}

	@Override
	protected void onResume() {
		progressDialog.show();

		ActionPerformer actionPerformer = new ServerActionPerformer(this);
		actionPerformer.requestDeviceList(new OnDeviceListReceivedListener() {
			@Override
			public void onDeviceListReceived(List<Device> devices) {
				if (devices != null && devices.size() > 0) {
					model.setDeviceList(devices);
				} else {
					Toast.makeText(DeviceListActivity.this, "Failed to get device list. Check server settings.", Toast.LENGTH_LONG).show();
				}

				progressDialog.dismiss();
			}
		});
		
		super.onResume();
	}
}
