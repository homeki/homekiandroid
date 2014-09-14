package com.homeki.android.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;
import com.homeki.android.R;
import com.homeki.android.server.ApiClient;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class ThermometerDeviceListItem extends DeviceListItem {
	private TextView temperatureView;
	
	public ThermometerDeviceListItem(Context context, ApiClient apiClient, ApiClient.JsonDevice jsonDevice) {
		super(context, apiClient, jsonDevice);
	}

	@Override
	protected void inflate(LayoutInflater layoutInflater) {
		layoutInflater.inflate(R.layout.thermometer_device_list_item, this);
		nameView = (TextView) findViewById(R.id.thermometer_device_list_item_name);
		temperatureView = (TextView) findViewById(R.id.thermometer_device_list_item_temperature);
	}
	
	@Override
	protected void updateView() {
		double temp = getChannelValue("Temperature").doubleValue();
		DecimalFormat df = new DecimalFormat("0.0");
		df.setDecimalSeparatorAlwaysShown(true);
		df.setRoundingMode(RoundingMode.HALF_UP);
		temperatureView.setText(df.format(temp) + " °C");
	}
}
