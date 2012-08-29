package com.homeki.android.view.devicelist;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.homeki.android.R;
import com.homeki.android.model.devices.ThermometerDevice;
import com.homeki.android.server.ActionPerformer;

public class DeviceListItemThermometerView extends AbstractDeviceListItemView<ThermometerDevice> {
	private TextView mTemperatureView;
	
	public DeviceListItemThermometerView(Context context, ActionPerformer actionPerformer) {
		super(context, actionPerformer);
	}

	@Override
	protected void inflate(LayoutInflater layoutInflater) {
		layoutInflater.inflate(R.layout.device_list_thermometer, this);
		
		mNameView = (TextView) findViewById(R.id.device_list_thermometer_name);
		mDescriptionView = (TextView) findViewById(R.id.device_list_thermometer_description);
		mOpenDetailsView = (ImageView) findViewById(R.id.device_list_thermometer_details_button);
		
		mTemperatureView = (TextView) findViewById(R.id.device_list_thermometer_temperature);
	}
	
	@Override
	protected void onDeviceSet(ThermometerDevice device) {
		double temp = device.getTemperature();
		DecimalFormat df = new DecimalFormat("0.0");
		df.setDecimalSeparatorAlwaysShown(true);
		df.setRoundingMode(RoundingMode.HALF_UP);
		mTemperatureView.setText(df.format(temp) + " °C");
	}
}
