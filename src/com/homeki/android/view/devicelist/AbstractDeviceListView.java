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
	protected TextView nameView;
	protected TextView descriptionView;
	protected View openDetailsView;
	protected AbstractDevice device;
	protected Context context;
	protected ActionPerformer actionPerformer;

	public AbstractDeviceListView(Context context, ActionPerformer actionPerformer) {
		super(context);
		this.actionPerformer = actionPerformer;
		this.context = context;
		inflate((LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
		openDetailsView.setOnClickListener(new OpenDetailsClickListener());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setDevice(AbstractDevice device) {
		this.device = device;

		nameView.setText(device.getName());
		descriptionView.setText(this.device.getDescription());

		onDeviceSet((T) device);
	}

	protected abstract void inflate(LayoutInflater inflater);

	protected abstract void onDeviceSet(T device);

	protected void setChannelValue(int channel, String value) {
		actionPerformer.setChannelValueForDevice(device.getId(), channel, value, new OnChannelValueSetListener() {
      @Override
      public void result(boolean success) {
        if (!success && device != null) {
          Toast.makeText(context, "Failed to set value for " + device.getName(), Toast.LENGTH_SHORT).show();
        }
      }
    });
	}
	
	private class OpenDetailsClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(context, DeviceDetailsActivity.class);
			intent.putExtra(DeviceDetailsActivity.EXTRA_DEVICE_ID, device.getId());
			context.startActivity(intent);
		}
	}
}
