package com.homeki.android;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.homeki.android.model.DeviceListProvider;
import com.homeki.android.model.DeviceListProvider.OnDeviceListChangedListener;
import com.homeki.android.server.ActionPerformer;

public abstract class DeviceCollectionAdapter extends BaseAdapter {
	private DeviceListProvider listProvider;
	private ActionPerformer actionPerformer;
	private Context context;

	public DeviceCollectionAdapter(Context context, DeviceListProvider listProvider, ActionPerformer actionPerformer) {
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
	public View getView(int pos, View view, ViewGroup parent) {
		return getView(context, actionPerformer, pos, view, parent);
	}

	protected abstract View getView(Context context, ActionPerformer actionPerformer, int position, View convertView, ViewGroup parent);
}
