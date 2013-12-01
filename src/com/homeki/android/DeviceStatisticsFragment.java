package com.homeki.android;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import com.homeki.android.model.DataPoint;
import com.homeki.android.model.devices.AbstractDevice;
import com.homeki.android.server.ActionPerformer;
import com.homeki.android.server.ActionPerformer.OnChannelHistoryReceivedListener;
import com.homeki.android.server.ServerActionPerformer;
import com.homeki.android.view.chart.ChartView;
import com.homeki.android.view.chart.TimeChartView;

import java.util.Date;
import java.util.List;

public class DeviceStatisticsFragment extends Fragment implements OnChannelHistoryReceivedListener {
	private AbstractDevice device;
	private ChartView chart;
	private Context context;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.device_statistics, container, false);

		context = view.getContext();

		chart = new TimeChartView(view.getContext());
		LinearLayout chartLayout = (LinearLayout) view.findViewById(R.id.device_statistics_chart);
		chartLayout.addView(chart, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		return view;
	}

	public void setDevice(AbstractDevice device) {
		this.device = device;
	}

	private void requestData(Date start, Date end) {
		if (device != null) {
			ActionPerformer performer = new ServerActionPerformer(context);
			for (Integer channel : device.getChannels()) {
				performer.getChannelHistoryForDevice(device.getId(), channel, start, end, this);
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		requestData(new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24), new Date());
	}

	@Override
	public void onChannelHistoryReceived(int deviceId, int channelId, List<DataPoint> data) {
		for (DataPoint dataPoint : data) {
			chart.putValue(channelId, dataPoint.getTime(), dataPoint.getValue());
		}
	}
}
