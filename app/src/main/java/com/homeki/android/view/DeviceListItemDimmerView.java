package com.homeki.android.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import com.homeki.android.R;
import com.homeki.android.model.devices.DimmerDevice;
import com.homeki.android.server.ActionPerformer;

public class DeviceListItemDimmerView extends AbstractDeviceListView<DimmerDevice> {
	private SeekBar valueBar;
	private Switch onOffSwitch;
	private OnOffChangedListener onOffChangedListener;
	private SeekBarChangedListener seekBarChangedListener;

	public DeviceListItemDimmerView(Context context, ActionPerformer actionPerformer) {
		super(context, actionPerformer);
		onOffChangedListener = new OnOffChangedListener();
		seekBarChangedListener = new SeekBarChangedListener();
	}

	@Override
	protected void inflate(LayoutInflater layoutInflater) {
		layoutInflater.inflate(R.layout.device_list_dimmer, this);
		nameView = (TextView) findViewById(R.id.device_list_dimmer_name);
		valueBar = (SeekBar) findViewById(R.id.device_list_dimmer_value_bar);
		onOffSwitch = (Switch) findViewById(R.id.device_list_dimmer_onoff);

		valueBar.setOnSeekBarChangeListener(seekBarChangedListener);
		onOffSwitch.setOnCheckedChangeListener(onOffChangedListener);
	}

	@Override
	protected void onDeviceSet(DimmerDevice device) {		
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
			DimmerDevice device = (DimmerDevice) DeviceListItemDimmerView.this.device;
			device.setLevel(seekBar.getProgress());
			
			setChannelValue("Level", seekBar.getProgress());
		}
	}

	private class OnOffChangedListener implements OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
			DimmerDevice device = (DimmerDevice) DeviceListItemDimmerView.this.device;
			device.setValue(isChecked);
			setChannelValue("Switch", isChecked ? 1 : 0);
		}
	}
}
