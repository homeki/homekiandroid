package com.homeki.android.communication.json;

import java.util.Date;

public class JsonDevice {
	public String type;
	public Integer id;
	public String name;
	public String description;
	public Date added;
	public Boolean active;
	public JsonState state;
	
	public JsonDevice() {
		
	}
}
