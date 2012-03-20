package com.homeki.android.device;

import com.homeki.android.communication.json.JsonDevice;
import com.homeki.android.tasks.DimTask;

public class Dimmer extends Switch {
	private int level;
	
	public Dimmer() {
		super();
		this.level = -1;
	}
	
	public Dimmer(JsonDevice d) {
		super(d);
	}
	
	public void dim(int level) {
		new DimTask(id, level).execute();
		this.setLevel(level);
	}
	
	public int getLevel() {
		return level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
}
