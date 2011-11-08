package com.homeki.android.device;

import java.util.Date;

public abstract class Device {
	protected int id;
	protected String name;
	protected Date added;
	protected boolean active;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
