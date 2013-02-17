package com.homeki.android.view.devicegrid;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.homeki.android.DeviceDetailsActivity;
import com.homeki.android.R;
import com.homeki.android.model.devices.ThermometerDevice;
import com.homeki.android.server.ActionPerformer;

public class DeviceGridItemThermometerView extends AbstractDeviceGridView<ThermometerDevice> {
	private TextView mTemperatureView;

	public DeviceGridItemThermometerView(Context context, ActionPerformer actionPerformer) {
		super(context, actionPerformer);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Intent intent = new Intent(mContext, DeviceDetailsActivity.class);
		intent.putExtra(DeviceDetailsActivity.EXTRA_DEVICE_ID, mDevice.getId());
		mContext.startActivity(intent);
		return super.onTouchEvent(event);
	}

	@Override
	protected void inflate(LayoutInflater layoutInflater) {
		layoutInflater.inflate(R.layout.device_grid_thermometer, this);

		mNameView = (TextView) findViewById(R.id.device_grid_thermometer_name);
		mTemperatureView = (TextView) findViewById(R.id.device_grid_thermometer_temperature);
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
