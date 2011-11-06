package com.homeki.android.device;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.homeki.android.R;
import com.homeki.android.tasks.Dim;
import com.homeki.android.tasks.SetName;
import com.homeki.android.tasks.SwitchOff;
import com.homeki.android.tasks.SwitchOn;

public class Lamp extends Device {
	enum Status {
		ON, OFF, LIMBO
	}
	
	Status status;
	View b = null;
	
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
	
	public void setStatus(boolean status) {
		if (status) {
			this.status = Status.ON;
			if (b!=null)
				b.getBackground().setLevel(1);			
		} else {
			this.status = Status.OFF;
			if (b!=null)
				b.getBackground().setLevel(0);
		}
	}
	
	public boolean getStatus() {
		return Status.ON == status;
	}
	
	public void setName(Context context, String name) {
		this.name = name;
		new SetName(context, id, name).execute();
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
	
	private void switchOff(Context context) {
		new SwitchOff(context, id).execute();
		setStatus(false);		
	}

	private void switchOn(Context context) {
		new SwitchOn(context, id).execute();
		setStatus(true);
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

		final EditText et = (EditText) v.findViewById(R.id.lamp_name);
		Button button = (Button) v.findViewById(R.id.lamp_save);
		
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setName(v.getContext(), et.getText().toString());
			}
		});
		
	}
}
