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
import com.homeki.android.server.ApiClient;

public class DimmerDeviceListItem extends DeviceListItem {
	private SeekBar valueBar;
	private Switch onOffSwitch;
	private OnOffChangedListener onOffChangedListener;
	private SeekBarChangedListener seekBarChangedListener;

	public DimmerDeviceListItem(Context context, ApiClient apiClient, ApiClient.JsonDevice jsonDevice) {
		super(context, apiClient, jsonDevice);
	}

	@Override
	protected void inflate(LayoutInflater layoutInflater) {
		onOffChangedListener = new OnOffChangedListener();
		seekBarChangedListener = new SeekBarChangedListener();

		layoutInflater.inflate(R.layout.dimmer_device_list_item, this);
		nameView = (TextView) findViewById(R.id.dimmer_device_list_item_name);
		valueBar = (SeekBar) findViewById(R.id.dimmer_device_list_item_level);
		onOffSwitch = (Switch) findViewById(R.id.dimmer_device_list_item_onoff);

		valueBar.setOnSeekBarChangeListener(seekBarChangedListener);
		onOffSwitch.setOnCheckedChangeListener(onOffChangedListener);
	}

	@Override
	protected void updateView() {
		valueBar.setOnSeekBarChangeListener(null);
		valueBar.setProgress(getChannelValue("Level").intValue());
		valueBar.setOnSeekBarChangeListener(seekBarChangedListener);
		
		onOffSwitch.setOnCheckedChangeListener(null);
		onOffSwitch.setChecked(getChannelValue("Switch").intValue() > 0);
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
		public void onStopTrackingTouch(SeekBar seekBar) {
			setChannelValue("Level", seekBar.getProgress());
		}
	}

	private class OnOffChangedListener implements OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			setChannelValue("Switch", isChecked ? 1 : 0);
		}
	}
}
