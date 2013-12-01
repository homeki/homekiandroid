package com.homeki.android.view.devicegrid;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.homeki.android.model.devices.AbstractDevice;
import com.homeki.android.server.ActionPerformer;
import com.homeki.android.server.ActionPerformer.OnChannelValueSetListener;
import com.homeki.android.view.DeviceItemView;

public abstract class AbstractDeviceGridView<T extends AbstractDevice> extends LinearLayout implements DeviceItemView {
	protected AbstractDevice device;
	protected Context context;
	protected ActionPerformer actionPerformer;

	public AbstractDeviceGridView(Context context, ActionPerformer actionPerformer) {
		super(context);
		this.actionPerformer = actionPerformer;
		this.context = context;
		inflate((LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setDevice(AbstractDevice device) {
		this.device = device;
		onDeviceSet((T) device);
	}

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

	protected abstract void inflate(LayoutInflater inflater);
	protected abstract void onDeviceSet(T device);
}
