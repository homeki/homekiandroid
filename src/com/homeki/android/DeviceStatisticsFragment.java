package com.homeki.android;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.homeki.android.model.DataPoint;
import com.homeki.android.model.devices.AbstractDevice;
import com.homeki.android.server.ActionPerformer;
import com.homeki.android.server.ActionPerformer.OnChannelHistoryReceivedListener;
import com.homeki.android.server.ServerActionPerformer;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DeviceStatisticsFragment extends Fragment implements OnChannelHistoryReceivedListener {

	private AbstractDevice mDevice;
	private TextView mList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.device_statistics, container, false);
		mList = (TextView) view.findViewById(R.id.device_statistics_list);
		return view;
	}

	public void setDevice(AbstractDevice device) {
		mDevice = device;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		if (mDevice != null) {
			ActionPerformer performer = new ServerActionPerformer(activity);
			performer.getChannelHistoryForDevice(mDevice.getId(), 0, new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 2), new Date(), this);
		}
	}

	@Override
	public void onChannelHistoryReceived(List<DataPoint> data) {
		StringBuilder textBuilder = new StringBuilder();

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		for (DataPoint dataPoint : data) {
			textBuilder.append(format.format(dataPoint.getTime()));
			textBuilder.append(": ");
			textBuilder.append(dataPoint.getValue());
			textBuilder.append("\n");

		}

		mList.setText(textBuilder.toString());
	}
}
