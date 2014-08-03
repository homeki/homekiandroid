package com.homeki.android.model.devices;

import java.util.ArrayList;
import java.util.List;

public class DeviceBuilder {
	private static final String DEVICE_TYPE_DIMMER = "dimmer";
	private static final String DEVICE_TYPE_SWITCH = "switch";
  private static final String DEVICE_TYPE_SWITCHMETER = "switchmeter";
	private static final String DEVICE_TYPE_THERMOMETER = "thermometer";
	
	public static com.homeki.android.model.devices.Device build(String type, int id, String name, String description, boolean active, List<Channel> channels) {
		com.homeki.android.model.devices.Device device = null;
				
		if (DEVICE_TYPE_DIMMER.equals(type)) {
			device = new DimmerDevice(DeviceType.DIMMER, id, name, description, active);
		} else if (DEVICE_TYPE_SWITCH.equals(type) || DEVICE_TYPE_SWITCHMETER.equals(type)) {
			device = new SwitchDevice(DeviceType.SWITCH, id, name, description, active);
		} else if (DEVICE_TYPE_THERMOMETER.equals(type)) {
			device = new ThermometerDevice(DeviceType.THERMOMETER, id, name, description, active);
		}

    for (Channel c : channels) {
      device.putChannel(c.id, c.name, c.lastValue);
    }

		return device;
	}
	
	public class Device {
		public String type;
		public int deviceId;
		public String name;
		public String description;
		public String added;
		public boolean active;
		public ArrayList<Channel> channels;
	}

	private class Channel {
		public int id;
    public String name;
		public String lastValue;
	}
}
