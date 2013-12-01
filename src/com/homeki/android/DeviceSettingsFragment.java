package com.homeki.android;

import com.homeki.android.model.devices.AbstractDevice;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DeviceSettingsFragment extends Fragment {
	private AbstractDevice device;
	private TextView titleView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.device_settings, container, false);
		titleView = (TextView) view.findViewById(R.id.device_details_title);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (device != null) {
			String deviceName = device.getName();
			titleView.setText(deviceName);
		} else {
			throw new RuntimeException("Device needs to be set.");
		}
	}

	public void setDevice(AbstractDevice device) {
		this.device = device;
	}
}
