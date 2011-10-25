package com.homeki.android.device;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.homeki.android.R;
import com.homeki.android.communication.CommandSendingService;

public class Lamp extends Device {
	enum Status {
		ON, OFF, LIMBO
	}
	
	Status status;
	View b;
	
	public Lamp() {
		status = Status.LIMBO;
	}
	
	public Lamp(JsonDevice d) {
		this();
		active = d.active;
		added = d.added;
		id = d.id;
		name = d.name;
	}
	
	public void switchOn(Context context) {
		status = Status.ON;
		Intent intent = new Intent(context, CommandSendingService.class);
		intent.setAction(CommandSendingService.turnLampOn);
		intent.putExtra("id", id);
		context.startService(intent);
		b.getBackground().setLevel(1);
	}
	
	public void switchOff(Context context) {
		status = Status.OFF;
		Intent intent = new Intent(context, CommandSendingService.class);
		intent.setAction(CommandSendingService.turnLampOff);
		intent.putExtra("id", id);
		context.startService(intent);
		b.getBackground().setLevel(0);
	}
	
	public void downloadStatus(Context context) {
		Intent intent = new Intent(context, CommandSendingService.class);
		intent.setAction(CommandSendingService.turnLampOff);
		intent.putExtra("id", id);
		context.startService(intent);
		status = Status.OFF;
	}

	public void setStatus(boolean status) {
		if (status)
			this.status = Status.ON;
		else
			this.status = Status.OFF;
	}
	
	public boolean getStatus() {
		return Status.ON == status;
	}
	
	public boolean toggle(Context context) {
		if (status == Status.OFF) {
			switchOn(context);
			return true;
		} else {
			switchOff(context);
			return false;
		}
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + " " + id;
	}
	
	@Override
	public View getView(Context context) {
		View v = LayoutInflater.from(context).inflate(R.layout.lamp, null);
		setUpView(v);
		return v;
	}

	protected void setUpView(View v) {
		TextView deviceName = (TextView) v.findViewById(R.id.lamp_devicename);
		TextView deviceDesc = (TextView) v.findViewById(R.id.lamp_devicedesc);
		b = v.findViewById(R.id.lamp_switch);
		b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				toggle(v.getContext());
			}
		});
		
		deviceName.setText(name);
		b.getBackground().setLevel(getStatus() ? 1 : 0);
		deviceDesc.setText("Lamp");
	}
}
