package com.homeki.android.communication.json;

public class JsonTriggerDeviceLink {
	public Integer deviceId;
	public Integer triggerId;
	public String name;
	public Boolean linked;
	
	@Override
	public String toString() {
		return name;
	}
}
