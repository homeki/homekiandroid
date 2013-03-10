package com.homeki.android;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.homeki.android.model.DeviceListProvider;
import com.homeki.android.model.DeviceListProvider.OnDeviceListChangedListener;
import com.homeki.android.model.devices.AbstractDevice;
import com.homeki.android.server.ActionPerformer;
import com.homeki.android.view.devicelist.AbstractDeviceListView;
import com.homeki.android.view.devicelist.DeviceListItemDimmerView;
import com.homeki.android.view.devicelist.DeviceListItemSwitchView;
import com.homeki.android.view.devicelist.DeviceListItemThermometerView;

public abstract class DeviceCollectionAdapter extends BaseAdapter {

	private DeviceListProvider mListProvider;
	private ActionPerformer mActionPerformer;
	private Context mContext;

	public DeviceCollectionAdapter(Context context, DeviceListProvider listProvider, ActionPerformer actionPerformer) {
		mContext = context;
		mActionPerformer = actionPerformer;
		mListProvider = listProvider;

		mListProvider.addOnDeviceListChangedListener(new OnDeviceListChangedListener() {
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
	public View getView(int pos, View view, ViewGroup parent) {
		return getView(mContext, mActionPerformer, pos, view, parent);
	}

	protected abstract View getView(Context context, ActionPerformer actionPerformer, int position, View convertView, ViewGroup parent);

}
