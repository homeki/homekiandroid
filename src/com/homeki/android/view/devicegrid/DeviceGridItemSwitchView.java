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
	private ToggleButton mOnOffSwitch;
	private OnOffChangedListener mOnOffChangedListener;

	public DeviceGridItemSwitchView(Context context, ActionPerformer actionPerformer) {
		super(context, actionPerformer);
		mOnOffChangedListener = new OnOffChangedListener();
	}

	@Override
	protected void inflate(LayoutInflater layoutInflater) {
		layoutInflater.inflate(R.layout.device_grid_switch, this);
		mOnOffSwitch = (ToggleButton) findViewById(R.id.device_grid_switch_button);

		mOnOffSwitch.setOnCheckedChangeListener(mOnOffChangedListener);
	}

	@Override
	protected void onDeviceSet(SwitchDevice device) {
		mOnOffSwitch.setTextOff(device.getName());
		mOnOffSwitch.setTextOn(device.getName());
		mOnOffSwitch.setOnCheckedChangeListener(null);
		mOnOffSwitch.setChecked(device.getValue());
		mOnOffSwitch.setOnCheckedChangeListener(mOnOffChangedListener);
	}

	private class OnOffChangedListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
			SwitchDevice device = (SwitchDevice) mDevice;
			device.setValue(isChecked);

			setChannelValue(SwitchDevice.CHANNEL_ID_VALUE, isChecked ? "1" : "0");
		}
	}
}