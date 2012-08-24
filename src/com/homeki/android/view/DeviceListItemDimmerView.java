package com.homeki.android.view;

import com.homeki.android.R;
import com.homeki.android.model.devices.DimmerDevice;
import com.homeki.android.server.ActionPerformer;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;
import android.widget.TextView;

public class DeviceListItemDimmerView extends AbstractDeviceListItemView<DimmerDevice> {

	private SeekBar mValueBar;
	private TextView mValueText;
	private Switch mOnOffSwitch;

	public DeviceListItemDimmerView(Context context, ActionPerformer actionPerformer) {
		super(context, actionPerformer);
	}

	@Override
	protected void inflate(LayoutInflater layoutInflater) {
		layoutInflater.inflate(R.layout.device_list_dimmer, this);
		mNameView = (TextView) findViewById(R.id.device_list_dimmer_name);
		mDescriptionView = (TextView) findViewById(R.id.device_list_dimmer_description);
		mValueBar = (SeekBar) findViewById(R.id.device_list_dimmer_value_bar);
		mValueText = (TextView) findViewById(R.id.device_list_dimmer_value_text);
		mOnOffSwitch = (Switch) findViewById(R.id.device_list_dimmer_onoff);

		mOpenDetailsView = (ImageView) findViewById(R.id.device_list_dimmer_details_button);

		mValueBar.setOnSeekBarChangeListener(new SeekBarChangedListener());
		mOnOffSwitch.setOnCheckedChangeListener(new OnOffChangedListener());
	}

	@Override
	protected void onDeviceSet(DimmerDevice device) {
		mValueBar.setProgress(device.getLevel());
	}

	private class SeekBarChangedListener implements OnSeekBarChangeListener {
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			mValueText.setText("" + progress);
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			mActionPerformer.setChannelValueForDevice(mDevice.getId(), DimmerDevice.CHANNEL_ID_LEVEL, String.valueOf(seekBar.getProgress()));
		}
	}

	private class OnOffChangedListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			mActionPerformer.setChannelValueForDevice(mDevice.getId(), DimmerDevice.CHANNEL_ID_VALUE, isChecked ? "1" : "0");
		}
	}
}
