package com.homeki.android;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.homeki.android.model.DeviceListProvider;
import com.homeki.android.model.DeviceListProvider.OnDeviceListChangedListener;
import com.homeki.android.model.devices.Device;
import com.homeki.android.server.ActionPerformer;
import com.homeki.android.view.AbstractDeviceListView;
import com.homeki.android.view.DeviceListItemDimmerView;
import com.homeki.android.view.DeviceListItemSwitchView;
import com.homeki.android.view.DeviceListItemThermometerView;

public class DeviceListAdapter extends BaseAdapter {
	private DeviceListProvider listProvider;
	private ActionPerformer actionPerformer;
	private Context context;

	public DeviceListAdapter(Context context, DeviceListProvider listProvider, ActionPerformer actionPerformer) {
		this.context = context;
		this.actionPerformer = actionPerformer;
		this.listProvider = listProvider;
		this.listProvider.addOnDeviceListChangedListener(new OnDeviceListChangedListener() {
      @Override
      public void onDeviceListChanged() {
        notifyDataSetChanged();
      }
    });
	}

	@Override
	public int getCount() {
		return listProvider.getDeviceCount();
	}

	@Override
	public Object getItem(int position) {
		return listProvider.getDeviceAtPosition(position);
	}

	@Override
	public long getItemId(int position) {
		return listProvider.getDeviceAtPosition(position).getId();
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		Device item = (Device) getItem(pos);
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
