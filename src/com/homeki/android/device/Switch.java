package com.homeki.android.device;

import android.content.Context;

import com.homeki.android.communication.json.JsonDevice;
import com.homeki.android.tasks.SwitchOffTask;
import com.homeki.android.tasks.SwitchOnTask;

public class Switch extends Device {
	enum Status {
		ON, OFF, LIMBO
	}
	
	protected Status status;
	
	public Switch() {
		status = Status.LIMBO;
	}
	
	public Switch(JsonDevice d) {
		this();
		added = d.added;
		id = d.id;
		name = d.name;
	}
	
	public boolean getStatus() {
		return Status.ON == status;
	}
	
	public void setName(Context context, String name) {
		this.name = name;
		// new SetName(context, id, name).execute();
	}
	
	public void setStatus(Integer value) {
		status = value > 0 ? Status.ON : Status.OFF;
	}
	
	public void switchOff() {
		if (getStatus() != false) {
			new SwitchOffTask(id).execute();
			setStatus(0);
		}
	}
	
	public void switchOn() {
		if (getStatus() != true) {
			new SwitchOnTask(id).execute();
			setStatus(1);
		}
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + " " + id;
	}
}
