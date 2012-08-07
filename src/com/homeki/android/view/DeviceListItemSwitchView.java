package com.homeki.android.view;

import com.homeki.android.R;
import com.homeki.android.model.devices.SwitchDevice;
import com.homeki.android.server.ActionPerformer;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

public class DeviceListItemSwitchView extends AbstractDeviceListItemView<SwitchDevice> {

	public DeviceListItemSwitchView(Context context, ActionPerformer actionPerformer) {
		super(context, actionPerformer);
	}
	
	@Override
	protected void inflate(LayoutInflater layoutInflater) {
		layoutInflater.inflate(R.layout.device_list_switch, this);
		mNameView = (TextView) findViewById(R.id.device_list_switch_name);
		mDescriptionView = (TextView) findViewById(R.id.device_list_switch_description);	
		
		mOpenDetailsView = (ImageView)findViewById(R.id.device_list_switch_details_button);
	}

	@Override
	protected void onDeviceSet(SwitchDevice device) {

	}
	
}
