package com.homeki.android.model.devices;

import java.util.HashMap;
import java.util.Set;

public abstract class AbstractDevice {
	protected DeviceType type;
	protected int id;
	protected String name;
	protected String description;
	protected String added;
	protected boolean active;
	protected HashMap<Integer, String> channels;
	private DeviceOwner owner;

	public AbstractDevice(DeviceType type, int id, String name, String description, String added, boolean active)  {
		this.type = type;
		this.id = id;
		this.name = name;
		this.description = description;
		this.added = added;
		this.active = active;
		this.channels = new HashMap<Integer, String>();
	}

	public DeviceType getType() {
		return type;
	}

	public void setType(DeviceType type) {
		this.type = type;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAdded() {
		return added;
	}

	public void setAdded(String added) {
		this.added = added;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void setChannelValue(int key, String value) {
		channels.put(key, value);
		notifyOwnerOfChange();
	}
	
	public String getChannelValue(int key) {
		return channels.get(key);
	}
	
	public Set<Integer> getChannels() {
		return channels.keySet();
	}
	
	public void setOwner(DeviceOwner owner) {
		this.owner = owner;
	}
	
	protected void notifyOwnerOfChange() {
		if(owner != null) {
			owner.deviceDidChange(this);
		}
	}
	
	public interface DeviceOwner {
		public void deviceDidChange(AbstractDevice device);
	}
}
