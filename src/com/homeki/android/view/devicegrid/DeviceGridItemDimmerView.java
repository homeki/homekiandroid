package com.homeki.android.view.devicegrid;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.ToggleButton;

import com.homeki.android.R;
import com.homeki.android.model.devices.DimmerDevice;
import com.homeki.android.server.ActionPerformer;

public class DeviceGridItemDimmerView extends AbstractDeviceGridView<DimmerDevice> {
	private SeekBar valueBar;
	private ToggleButton onOffSwitch;
	private OnOffChangedListener onOffChangedListener;
	private SeekBarChangedListener seekBarChangedListener;

	public DeviceGridItemDimmerView(Context context, ActionPerformer actionPerformer) {
		super(context, actionPerformer);
		onOffChangedListener = new OnOffChangedListener();
		seekBarChangedListener = new SeekBarChangedListener();
	}

	@Override
	protected void inflate(LayoutInflater layoutInflater) {
		layoutInflater.inflate(R.layout.device_grid_dimmer, this);
		valueBar = (SeekBar) findViewById(R.id.device_grid_dimmer_value_bar);
		onOffSwitch = (ToggleButton) findViewById(R.id.device_grid_dimmer_button);

		valueBar.setOnSeekBarChangeListener(seekBarChangedListener);
		onOffSwitch.setOnCheckedChangeListener(onOffChangedListener);
	}

	@Override
	protected void onDeviceSet(DimmerDevice device) {
		onOffSwitch.setTextOff(device.getName());
		onOffSwitch.setTextOn(device.getName());
		
		valueBar.setOnSeekBarChangeListener(null);
		valueBar.setProgress(device.getLevel());
		valueBar.setOnSeekBarChangeListener(seekBarChangedListener);
		
		onOffSwitch.setOnCheckedChangeListener(null);
		onOffSwitch.setChecked(device.getValue());
		onOffSwitch.setOnCheckedChangeListener(onOffChangedListener);
	}

	private class SeekBarChangedListener implements OnSeekBarChangeListener {
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
		}

		@Override
		public void onStopTrackingTouch(final SeekBar seekBar) {
			DimmerDevice device = (DimmerDevice) DeviceGridItemDimmerView.this.device;
			device.setLevel(seekBar.getProgress());
			
			setChannelValue("Level", seekBar.getProgress());
		}
	}

	private class OnOffChangedListener implements OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
			DimmerDevice device = (DimmerDevice) DeviceGridItemDimmerView.this.device;
			device.setValue(isChecked);
			setChannelValue("Switch", isChecked ? 1 : 0);
		}
	}
}
