package com.homeki.android.view.devicegrid;

import com.homeki.android.model.devices.AbstractDevice;
import com.homeki.android.server.ActionPerformer;
import com.homeki.android.view.DeviceItemView;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class AbstractDeviceGridView<T extends AbstractDevice> extends LinearLayout implements DeviceItemView {

	protected TextView mNameView;
	protected AbstractDevice mDevice;
	protected Context mContext;
	protected ActionPerformer mActionPerformer;

	public AbstractDeviceGridView(Context context, ActionPerformer actionPerformer) {
		super(context);

		mActionPerformer = actionPerformer;

		mContext = context;
		inflate((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setDevice(AbstractDevice device) {
		mDevice = device;

		mNameView.setText(device.getName());

		onDeviceSet((T) device);
	}

	protected abstract void inflate(LayoutInflater inflater);
	protected abstract void onDeviceSet(T device);
}
