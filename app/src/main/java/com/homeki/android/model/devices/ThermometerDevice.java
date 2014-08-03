package com.homeki.android.model.devices;

public class ThermometerDevice extends Device {
	public ThermometerDevice(DeviceType type, int id, String name, String description, boolean active) {
		super(type, id, name, description, active);
	}

	public double getTemperature() {
		String value = getChannelValue("Temperature");
		return Double.parseDouble(value);
	}
}
