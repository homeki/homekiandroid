package com.homeki.android;

import com.homeki.android.model.DeviceListProvider;
import com.homeki.android.model.DeviceListProvider.OnDeviceListChangedListener;
import com.homeki.android.model.devices.AbstractDevice;
import com.homeki.android.server.ActionPerformer;
import com.homeki.android.view.AbstractDeviceListItemView;
import com.homeki.android.view.DeviceListItemDimmerView;
import com.homeki.android.view.DeviceListItemSwitchView;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class DeviceListAdapter extends BaseAdapter {

	private DeviceListProvider mListProvider;
	private ActionPerformer mActionPerformer;
	private Context mContext;

	public DeviceListAdapter(Context context, DeviceListProvider listProvider, ActionPerformer actionPerformer) {
		mContext = context;
		mActionPerformer = actionPerformer;
		mListProvider = listProvider;
		mListProvider.setOnDeviceListChangedListener( new OnDeviceListChangedListener() {			
			@Override
			public void onDeviceListChanged() {
				notifyDataSetChanged();
			}
		});
	}

	@Override
	public int getCount() {
		return mListProvider.getDeviceCount();
	}

	@Override
	public Object getItem(int position) {
		return mListProvider.getDeviceAtPosition(position);
	}

	@Override
	public long getItemId(int position) {
		return mListProvider.getDeviceAtPosition(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AbstractDevice<?> item = (AbstractDevice<?>) getItem(position);
		AbstractDeviceListItemView<?> view = null;
		switch (item.getType()) {
		case DIMMER:
			view = new DeviceListItemDimmerView(mContext, mActionPerformer);
			break;
		case SWITCH:
			view = new DeviceListItemSwitchView(mContext, mActionPerformer);
			break;
		default:
			break;
		}
		view.setDevice(item);
		return view;
	}
}
