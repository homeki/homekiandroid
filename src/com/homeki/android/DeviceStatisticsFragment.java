package com.homeki.android;

import java.util.Date;
import java.util.List;

import com.homeki.android.model.devices.AbstractDevice;
import com.homeki.android.server.ActionPerformer;
import com.homeki.android.server.ActionPerformer.OnChannelHistoryReceivedListener;
import com.homeki.android.server.ServerActionPerformer;

import android.app.Fragment;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DeviceStatisticsFragment extends Fragment implements OnChannelHistoryReceivedListener {

	private AbstractDevice mDevice;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.device_statistics, container, false);

		return view;
	}

	public void setDevice(AbstractDevice device) {
		mDevice = device;

		ActionPerformer performer = new ServerActionPerformer(getActivity());
		performer.getChannelHistoryForDevice(device.getId(), 0, new Date(System.currentTimeMillis() - 1000 * 60 * 30), new Date(), this);
	}

	@Override
	public void onChannelHistoryReceived(List<Integer> data) {
		
	}
}
