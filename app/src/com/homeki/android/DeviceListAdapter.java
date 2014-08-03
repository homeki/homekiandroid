package com.homeki.android;

import com.homeki.android.model.DeviceListProvider;
import com.homeki.android.model.devices.Device;
import com.homeki.android.server.ActionPerformer;
import com.homeki.android.view.devicelist.AbstractDeviceListView;
import com.homeki.android.view.devicelist.DeviceListItemDimmerView;
import com.homeki.android.view.devicelist.DeviceListItemSwitchView;
import com.homeki.android.view.devicelist.DeviceListItemThermometerView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public class DeviceListAdapter extends DeviceCollectionAdapter {
	public DeviceListAdapter(Context context, DeviceListProvider listProvider, ActionPerformer actionPerformer) {
		super(context, listProvider, actionPerformer);
	}

	@Override
	public View getView(Context context, ActionPerformer actionPerformer, int position, View convertView, ViewGroup parent) {
		Device item = (Device) getItem(position);
		AbstractDeviceListView<?> view;

		switch (item.getType()) {
		case DIMMER:
			view = new DeviceListItemDimmerView(context, actionPerformer);
			break;
		case SWITCH:
			view = new DeviceListItemSwitchView(context, actionPerformer);
			break;
		case THERMOMETER:
			view = new DeviceListItemThermometerView(context, actionPerformer);
			break;
		default:
			throw new RuntimeException("Found no corresponding view to device of type " + item.getType() + ".");
		}

		view.setDevice(item);
		return view;
	}
}
