package com.homeki.android.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import com.homeki.android.R;
import com.homeki.android.server.ApiClient;

public class SwitchDeviceListItem extends DeviceListItem {
	private Switch onOffSwitch;
	private OnOffChangedListener onOffChangedListener;

	public SwitchDeviceListItem(Context context, ApiClient apiClient, ApiClient.JsonDevice jsonDevice) {
		super(context, apiClient, jsonDevice);
	}

	@Override
	protected void inflate(LayoutInflater layoutInflater) {
		onOffChangedListener = new OnOffChangedListener();

		layoutInflater.inflate(R.layout.switch_device_list_item, this);
		nameView = (TextView) findViewById(R.id.switch_device_list_item_name);
		onOffSwitch = (Switch) findViewById(R.id.switch_device_list_item_onoff);

		onOffSwitch.setOnCheckedChangeListener(onOffChangedListener);
	}

	@Override
	protected void updateView() {
		onOffSwitch.setOnCheckedChangeListener(null);
		onOffSwitch.setChecked(getChannelValue("Switch").intValue() > 0);
		onOffSwitch.setOnCheckedChangeListener(onOffChangedListener);
	}

	private class OnOffChangedListener implements OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			setChannelValue("Switch", isChecked ? 1 : 0);
		}
	}
}
