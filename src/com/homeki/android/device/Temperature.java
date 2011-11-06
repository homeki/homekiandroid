package com.homeki.android.device;

import com.homeki.android.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class Temperature extends Device {
	float value;
	
	public Temperature() {
		value = -1f;
	}
	
	public Temperature(JsonDevice d) {
		this();
		active = d.active;	
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
