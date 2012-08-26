package com.homeki.android.model.devices;

public class ThermometerDevice extends AbstractDevice {
	public static final int CHANNEL_ID_VALUE = 0;

	public ThermometerDevice(DeviceType type, int id, String name, String description, String added, boolean active) {
		super(type,id,name,description,added,active);
	}

	public double getTemperature() {
		String value = getChannelValue(CHANNEL_ID_VALUE);
		return Double.parseDouble(value);
	}
}
