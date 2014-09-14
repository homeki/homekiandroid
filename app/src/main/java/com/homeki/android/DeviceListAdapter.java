package com.homeki.android;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.homeki.android.server.ApiClient;
import com.homeki.android.view.DimmerDeviceListItem;
import com.homeki.android.view.SwitchDeviceListItem;
import com.homeki.android.view.ThermometerDeviceListItem;

import java.util.ArrayList;
import java.util.List;

public class DeviceListAdapter extends BaseAdapter {
	private final Context context;
	private final ApiClient apiClient;
	private List<ApiClient.JsonDevice> jsonDevices;

	public DeviceListAdapter(Context context, ApiClient apiClient) {
		this.context = context;
		this.apiClient = apiClient;
		this.jsonDevices = new ArrayList<>();
	}

	public void setDevices(List<ApiClient.JsonDevice> jsonDevices) {
		this.jsonDevices = jsonDevices;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return jsonDevices.size();
	}

	@Override
	public Object getItem(int position) {
		return jsonDevices.get(position);
	}

	@Override
	public long getItemId(int position) {
		return jsonDevices.get(position).deviceId;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		ApiClient.JsonDevice jsonDevice = jsonDevices.get(pos);

		switch (jsonDevice.type) {
			case DIMMER:
				return new DimmerDeviceListItem(context, apiClient, jsonDevice);
			case SWITCH:
			case SWITCH_METER:
				return new SwitchDeviceListItem(context, apiClient, jsonDevice);
			case THERMOMETER:
				return new ThermometerDeviceListItem(context, apiClient, jsonDevice);
			default:
				throw new RuntimeException("Found no view for device of type " + jsonDevice.type + ".");
		}
	}
}
