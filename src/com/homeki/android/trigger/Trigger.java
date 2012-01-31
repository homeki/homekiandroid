package com.homeki.android.trigger;

import com.homeki.android.communication.json.JsonTrigger;

public class Trigger {
	protected int id;
	protected String name;
	private Integer newValue;
	
	public Trigger(JsonTrigger t) {
		id = t.id;
		name = t.name;
		setNewValue(t.newValue);
	}
	
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

	public Integer getNewValue() {
		return newValue;
	}

	public void setNewValue(Integer newValue) {
		this.newValue = newValue;
	}
}
