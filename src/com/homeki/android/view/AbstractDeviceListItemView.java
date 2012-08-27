package com.homeki.android.view;

import java.util.HashMap;

import com.homeki.android.DeviceDetailsActivity;
import com.homeki.android.model.devices.AbstractDevice;
import com.homeki.android.model.devices.DeviceType;
import com.homeki.android.server.ActionPerformer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class AbstractDeviceListItemView<T extends AbstractDevice> extends LinearLayout implements DeviceListItemView {

	private static HashMap<DeviceType, Integer> TYPE_COUNT_MAP = new HashMap<DeviceType, Integer>();

	protected TextView mNameView;
	protected TextView mDescriptionView;
	protected View mOpenDetailsView;
	protected AbstractDevice mDevice;
	protected Context mContext;
	protected ActionPerformer mActionPerformer;

	public AbstractDeviceListItemView(Context context, ActionPerformer actionPerformer) {
		super(context);

		mActionPerformer = actionPerformer;

		mContext = context;
		inflate((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE));

		mOpenDetailsView.setOnClickListener(new OpenDetailsClickListener());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setDevice(AbstractDevice device) {
		mDevice = device;

		String name = getDeviceNameToShow(mDevice);
		mNameView.setText(name);
		mDescriptionView.setText(mDevice.getDescription());

		onDeviceSet((T) device);
	}

	protected abstract void inflate(LayoutInflater inflater);

	protected abstract void onDeviceSet(T device);

	private class OpenDetailsClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(mContext, DeviceDetailsActivity.class);
			intent.putExtra(DeviceDetailsActivity.EXTRA_DEVICE_ID, mDevice.getId());
			mContext.startActivity(intent);
		}
	}

	private static String getDeviceNameToShow(AbstractDevice device) {
		String name = device.getName();

		if (name == null || name.isEmpty()) {
			synchronized (TYPE_COUNT_MAP) {
				int number = 0;
				if (!TYPE_COUNT_MAP.containsKey(device.getType())) {
					number = 1;
				} else {
					number = TYPE_COUNT_MAP.get(device.getType()) + 1;
				}
				TYPE_COUNT_MAP.put(device.getType(), 1);
				name = device.getType().toString() + " " + number;
			}
		}

		return name;
	}
}
