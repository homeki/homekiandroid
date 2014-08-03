package com.homeki.android;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.homeki.android.model.DeviceListProvider;
import com.homeki.android.model.devices.Device;
import com.homeki.android.server.ActionPerformer;
import com.homeki.android.view.devicegrid.AbstractDeviceGridView;
import com.homeki.android.view.devicegrid.DeviceGridItemDimmerView;
import com.homeki.android.view.devicegrid.DeviceGridItemSwitchView;
import com.homeki.android.view.devicegrid.DeviceGridItemThermometerView;

public class DeviceGridAdapter extends DeviceCollectionAdapter {
	public DeviceGridAdapter(Context context, DeviceListProvider listProvider, ActionPerformer actionPerformer) {
		super(context, listProvider, actionPerformer);
	}

	@Override
	public View getView(Context context, ActionPerformer actionPerformer, int position, View convertView, ViewGroup parent) {
		Device item = (Device) getItem(position);
		AbstractDeviceGridView<?> view;

		switch (item.getType()) {
		case DIMMER:
			view = new DeviceGridItemDimmerView(context, actionPerformer);
			break;
		case SWITCH:
			view = new DeviceGridItemSwitchView(context, actionPerformer);
			break;
		case THERMOMETER:
			view = new DeviceGridItemThermometerView(context, actionPerformer);
			break;
		default:
			throw new RuntimeException("Found no corresponding view to device of type " + item.getType() + ".");
		}

		view.setDevice(item);
		return view;
	}
}
