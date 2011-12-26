package com.homeki.android.device;

import com.homeki.android.communication.json.JsonDevice;


public class Thermometer extends Device {
	float value;
	
	public Thermometer() {
		value = -1f;
	}
	
	public Thermometer(JsonDevice d) {
		this();
		added = d.added;
		id = d.id;
		name = d.name;
	}
	
	public float getStatus() {
		return value;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + " " + id;
	}
}
