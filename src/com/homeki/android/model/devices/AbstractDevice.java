package com.homeki.android.model.devices;

import java.util.HashMap;

public abstract class AbstractDevice {

	protected DeviceType mType;
	protected int mId;
	protected String mName;
	protected String mDescription;
	protected String mAdded;
	protected boolean mActive;
	protected HashMap<Integer, String> mChannels;

	public AbstractDevice(DeviceType type, int id, String name, String description, String added, boolean active)  {
		mType = type;
		mId = id;
		mName = name;
		mDescription = description;
		mAdded = added;
		mActive = active;
		mChannels = new HashMap<Integer, String>();
	}

	public DeviceType getType() {
		return mType;
	}

	public void setType(DeviceType type) {
		this.mType = type;
	}

	public int getId() {
		return mId;
	}

	public void setId(int id) {
		this.mId = id;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		this.mName = name;
	}

	public String getDescription() {
		return mDescription;
	}

	public void setDescription(String description) {
		this.mDescription = description;
	}

	public String getAdded() {
		return mAdded;
	}

	public void setAdded(String added) {
		this.mAdded = added;
	}

	public boolean isActive() {
		return mActive;
	}

	public void setActive(boolean active) {
		this.mActive = active;
	}

	public void setChannelValue(int key, String value) {
		mChannels.put(key, value);	
	}
	
	public String getChannelValue(int key) {
		return mChannels.get(key);
	}

}
