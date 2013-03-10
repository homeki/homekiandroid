package com.homeki.android.view.devicegrid;

import com.homeki.android.model.devices.AbstractDevice;
import com.homeki.android.model.devices.SwitchDevice;
import com.homeki.android.server.ActionPerformer;
import com.homeki.android.server.ActionPerformer.OnChannelValueSetListener;
import com.homeki.android.view.DeviceItemView;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

	protected void setChannelValue(int channel, String value) {
		mActionPerformer.setChannelValueForDevice(mDevice.getId(), channel, value, new OnChannelValueSetListener() {
			@Override
			public void result(boolean success) {
				if (!success && mDevice != null) {
					Toast.makeText(mContext, "Failed to set value for " + mDevice.getName(), Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	protected abstract void inflate(LayoutInflater inflater);

	protected abstract void onDeviceSet(T device);
}
