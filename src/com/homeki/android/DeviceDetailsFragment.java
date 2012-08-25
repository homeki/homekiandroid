package com.homeki.android;

import com.homeki.android.model.devices.AbstractDevice;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DeviceDetailsFragment extends Fragment {
	
	private AbstractDevice mDevice;
	
	private TextView mTitleView;
	
	public DeviceDetailsFragment(AbstractDevice device) {
		mDevice = device;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { 
		View view = inflater.inflate(R.layout.details, null);
		
		mTitleView = (TextView)view.findViewById(R.id.device_details_title);
		
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mTitleView.setText(mDevice.getName());
	}
}
