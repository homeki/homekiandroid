package com.homeki.android.view.devicelist;

import com.homeki.android.DeviceDetailsActivity;
import com.homeki.android.model.devices.AbstractDevice;
import com.homeki.android.server.ActionPerformer;
import com.homeki.android.server.ActionPerformer.OnChannelValueSetListener;
import com.homeki.android.view.DeviceItemView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public abstract class AbstractDeviceListView<T extends AbstractDevice> extends LinearLayout implements DeviceItemView {

	protected TextView mNameView;
	protected TextView mDescriptionView;
	protected View mOpenDetailsView;
	protected AbstractDevice mDevice;
	protected Context mContext;
	protected ActionPerformer mActionPerformer;

	public AbstractDeviceListView(Context context, ActionPerformer actionPerformer) {
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

		mNameView.setText(device.getName());
		mDescriptionView.setText(mDevice.getDescription());

		onDeviceSet((T) device);
	}

	protected abstract void inflate(LayoutInflater inflater);

	protected abstract void onDeviceSet(T device);

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
	
	private class OpenDetailsClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(mContext, DeviceDetailsActivity.class);
			intent.putExtra(DeviceDetailsActivity.EXTRA_DEVICE_ID, mDevice.getId());
			mContext.startActivity(intent);
		}
	}
}
