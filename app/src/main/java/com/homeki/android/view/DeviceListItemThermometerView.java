package com.homeki.android.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;
import com.homeki.android.R;
import com.homeki.android.model.devices.ThermometerDevice;
import com.homeki.android.server.ActionPerformer;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class DeviceListItemThermometerView extends AbstractDeviceListView<ThermometerDevice> {
	private TextView temperatureView;
	
	public DeviceListItemThermometerView(Context context, ActionPerformer actionPerformer) {
		super(context, actionPerformer);
	}

	@Override
	protected void inflate(LayoutInflater layoutInflater) {
		layoutInflater.inflate(R.layout.device_list_thermometer, this);
		
		nameView = (TextView) findViewById(R.id.device_list_thermometer_name);
		descriptionView = (TextView) findViewById(R.id.device_list_thermometer_description);
		
		temperatureView = (TextView) findViewById(R.id.device_list_thermometer_temperature);
	}
	
	@Override
	protected void onDeviceSet(ThermometerDevice device) {
		double temp = device.getTemperature();
		DecimalFormat df = new DecimalFormat("0.0");
		df.setDecimalSeparatorAlwaysShown(true);
		df.setRoundingMode(RoundingMode.HALF_UP);
		temperatureView.setText(df.format(temp) + " °C");
	}
}
