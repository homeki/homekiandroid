package com.homeki.android.view.devicegrid;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

import com.homeki.android.R;
import com.homeki.android.model.devices.SwitchDevice;
import com.homeki.android.server.ActionPerformer;

public class DeviceGridItemSwitchView extends AbstractDeviceGridView<SwitchDevice> {
	private ToggleButton onOffSwitch;
	private OnOffChangedListener onOffChangedListener;

	public DeviceGridItemSwitchView(Context context, ActionPerformer actionPerformer) {
		super(context, actionPerformer);
		onOffChangedListener = new OnOffChangedListener();
	}

	@Override
	protected void inflate(LayoutInflater layoutInflater) {
		layoutInflater.inflate(R.layout.device_grid_switch, this);
		onOffSwitch = (ToggleButton) findViewById(R.id.device_grid_switch_button);

		onOffSwitch.setOnCheckedChangeListener(onOffChangedListener);
	}

	@Override
	protected void onDeviceSet(SwitchDevice device) {
		onOffSwitch.setTextOff(device.getName());
		onOffSwitch.setTextOn(device.getName());
		onOffSwitch.setOnCheckedChangeListener(null);
		onOffSwitch.setChecked(device.getValue());
		onOffSwitch.setOnCheckedChangeListener(onOffChangedListener);
	}

	private class OnOffChangedListener implements OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
			SwitchDevice device = (SwitchDevice) DeviceGridItemSwitchView.this.device;
			device.setValue(isChecked);

			setChannelValue(SwitchDevice.CHANNEL_ID_VALUE, isChecked ? 1 : 0);
		}
	}
}
