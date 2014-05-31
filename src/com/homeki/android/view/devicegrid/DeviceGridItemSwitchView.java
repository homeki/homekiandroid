package com.homeki.android.view.devicegrid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.homeki.android.R;
import com.homeki.android.model.devices.SwitchDevice;
import com.homeki.android.server.ActionPerformer;

public class DeviceGridItemSwitchView extends AbstractDeviceGridView<SwitchDevice> {

	private TextView nameView;
	private ImageView indicatorView;

	private boolean isOn;

	public DeviceGridItemSwitchView(Context context, ActionPerformer actionPerformer) {
		super(context, actionPerformer);
		isOn = false;
	}

	@Override
	protected void inflate(LayoutInflater layoutInflater) {
		layoutInflater.inflate(R.layout.device_grid_switch, this);
		nameView = (TextView) findViewById(R.id.switch_text);
		indicatorView = (ImageView) findViewById(R.id.switch_indicator_view);

	}

	@Override
	protected void onDeviceSet(SwitchDevice device) {
		nameView.setText(device.getName());
		isOn = device.getValue();
		this.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isOn = !isOn;

				SwitchDevice device = (SwitchDevice) DeviceGridItemSwitchView.this.device;
				device.setValue(isOn);

				setChannelValue("Switch", isOn ? 1 : 0);

				updateView();
			}
		});
		
		updateView(); 
	}

	private void updateView() {
		if (isOn) {
			indicatorView.setImageResource(R.drawable.switch_indicator_on);
			this.setBackgroundResource(R.drawable.switch_background_on);
		} else {
			indicatorView.setImageResource(R.drawable.switch_indicator_off);
			this.setBackgroundResource(R.drawable.switch_background_off);
		}
	}

}
