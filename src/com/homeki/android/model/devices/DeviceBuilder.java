package com.homeki.android.model.devices;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class DeviceBuilder {

	public static AbstractDevice build(String type, int id, String name, String description, String added, boolean active, ArrayList<ChannelValue> channels) {
		AbstractDevice device = null;
				
		if ("dimmer".equals(type)) {
			device = new DimmerDevice(DeviceType.DIMMER, id, name, description, added, active);
		} else if ("switch".equals(type)) {
			device = new SwitchDevice(DeviceType.SWITCH, id, name, description, added, active);
		} else if ("thermometer".equals(type)) {
			device = new ThermometerDevice(DeviceType.THERMOMETER, id, name, description, added, active);
		}

		for (int i = 0; i < channels.size(); i++) {
			device.setChannelValue(channels.get(i).id, channels.get(i).lastValue);
		}
		return device;
	}
	
	public class Device {
		@SerializedName("type")
		public String type;

		@SerializedName("id")
		public int id;

		@SerializedName("names")
		public String name;

		@SerializedName("description")
		public String description;

		@SerializedName("added")
		public String added;

		@SerializedName("active")
		public boolean active;

		@SerializedName("channelValues")
		public ArrayList<ChannelValue> channelValues;

	}

	private class ChannelValue {
		@SerializedName("id")
		public int id;

		@SerializedName("lastValue")
		public String lastValue;
	}
}
