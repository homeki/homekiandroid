package com.homeki.android.device;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.homeki.android.R;
import com.homeki.android.tasks.SwitchOff;
import com.homeki.android.tasks.SwitchOn;

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
		active = d.active;
		added = d.added;
		id = d.id;
		name = d.name;
	}
	
	public boolean getStatus() {
		return Status.ON == status;
	}
	
	public void setStatus(boolean on) {
		status = on ? Status.ON : Status.OFF;
	}
	
	public void switchOff(Context context) {
		new SwitchOff(context, id).execute();
		setStatus(false);
	}

	public void switchOn(Context context) {
		new SwitchOn(context, id).execute();
		setStatus(true);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " " + id;
	}
}
