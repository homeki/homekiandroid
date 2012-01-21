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
	
	public void setStatus(boolean on) {
		status = on ? Status.ON : Status.OFF;
	}
	
	public void switchOff() {
		if (getStatus() != false) {
			new SwitchOffTask(id).execute();
			setStatus(false);
		}
	}
	
	public void switchOn() {
		if (getStatus() != true) {
			new SwitchOnTask(id).execute();
			setStatus(true);
		}
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + " " + id;
	}
}
