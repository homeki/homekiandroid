package com.homeki.android.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.homeki.android.R;
import com.homeki.android.model.devices.SwitchDevice;
import com.homeki.android.server.ActionPerformer;
import com.homeki.android.server.ActionPerformer.OnChannelValueSetListener;

public class DeviceListItemSwitchView extends AbstractDeviceListItemView<SwitchDevice> {

	private Switch mOnOffSwitch;
	private OnOffChangedListener mOnOffChangedListener;

	public DeviceListItemSwitchView(Context context, ActionPerformer actionPerformer) {
		super(context, actionPerformer);
		mOnOffChangedListener = new OnOffChangedListener();
	}

	@Override
	protected void inflate(LayoutInflater layoutInflater) {
		layoutInflater.inflate(R.layout.device_list_switch, this);
		mNameView = (TextView) findViewById(R.id.device_list_switch_name);
		mDescriptionView = (TextView) findViewById(R.id.device_list_switch_description);
		mOnOffSwitch = (Switch) findViewById(R.id.device_list_switch_onoff);

		mOpenDetailsView = (ImageView) findViewById(R.id.device_list_switch_details_button);

		mOnOffSwitch.setOnCheckedChangeListener(mOnOffChangedListener);
	}
	
	@Override
	protected void onDeviceSet(SwitchDevice device) {
		mOnOffSwitch.setOnCheckedChangeListener(null);
		mOnOffSwitch.setChecked(device.getValue());
		mOnOffSwitch.setOnCheckedChangeListener(mOnOffChangedListener);
	}

	private class OnOffChangedListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
			mActionPerformer.setChannelValueForDevice(mDevice.getId(), SwitchDevice.CHANNEL_ID_VALUE, isChecked ? "1" : "0", new OnChannelValueSetListener() {
				@Override
				public void result(boolean success) {
					if (success && mDevice != null) {
						SwitchDevice device = (SwitchDevice) mDevice;
						device.setValue(isChecked);
					}
				}
			});
		}
	}
}
