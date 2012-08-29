package com.homeki.android.view.devicelist;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;
import android.widget.TextView;

import com.homeki.android.R;
import com.homeki.android.model.devices.DimmerDevice;
import com.homeki.android.server.ActionPerformer;
import com.homeki.android.server.ActionPerformer.OnChannelValueSetListener;

public class DeviceListItemDimmerView extends AbstractDeviceListItemView<DimmerDevice> {
	private SeekBar mValueBar;
	private Switch mOnOffSwitch;
	private OnOffChangedListener mOnOffChangedListener;
	private SeekBarChangedListener mSeekBarChangedListener;

	public DeviceListItemDimmerView(Context context, ActionPerformer actionPerformer) {
		super(context, actionPerformer);
		mOnOffChangedListener = new OnOffChangedListener();
		mSeekBarChangedListener = new SeekBarChangedListener();
	}

	@Override
	protected void inflate(LayoutInflater layoutInflater) {
		layoutInflater.inflate(R.layout.device_list_dimmer, this);
		mNameView = (TextView) findViewById(R.id.device_list_dimmer_name);
		mDescriptionView = (TextView) findViewById(R.id.device_list_dimmer_description);
		mValueBar = (SeekBar) findViewById(R.id.device_list_dimmer_value_bar);
		mOnOffSwitch = (Switch) findViewById(R.id.device_list_dimmer_onoff);

		mOpenDetailsView = (ImageView) findViewById(R.id.device_list_dimmer_details_button);

		mValueBar.setOnSeekBarChangeListener(mSeekBarChangedListener);
		mOnOffSwitch.setOnCheckedChangeListener(mOnOffChangedListener);
	}

	@Override
	protected void onDeviceSet(DimmerDevice device) {
		mValueBar.setOnSeekBarChangeListener(null);
		mValueBar.setProgress(device.getLevel());
		mValueBar.setOnSeekBarChangeListener(mSeekBarChangedListener);
		
		mOnOffSwitch.setOnCheckedChangeListener(null);
		mOnOffSwitch.setChecked(device.getValue());
		mOnOffSwitch.setOnCheckedChangeListener(mOnOffChangedListener);
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
			mActionPerformer.setChannelValueForDevice(mDevice.getId(), DimmerDevice.CHANNEL_ID_LEVEL, String.valueOf(seekBar.getProgress()), new OnChannelValueSetListener() {
				@Override
				public void result(boolean success) {
					if (success && mDevice != null) {
						DimmerDevice device = (DimmerDevice) mDevice;
						device.setLevel(seekBar.getProgress());
					}
				}
			});
		}
	}

	private class OnOffChangedListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
			mActionPerformer.setChannelValueForDevice(mDevice.getId(), DimmerDevice.CHANNEL_ID_VALUE, isChecked ? "1" : "0", new OnChannelValueSetListener() {
				@Override
				public void result(boolean success) {
					if (success && mDevice != null) {
						DimmerDevice device = (DimmerDevice) mDevice;
						device.setValue(isChecked);
					}
				}
			});
		}
	}
}
