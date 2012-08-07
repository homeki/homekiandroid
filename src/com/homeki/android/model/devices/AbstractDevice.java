package com.homeki.android.model.devices;

import java.util.Date;

public abstract class AbstractDevice<T> {
	protected DeviceTypes mType;
	protected int mId;
	protected String mName;
	protected String mDescription;
	protected Date mAdded;
	protected boolean mActive;
	protected T mState;

	public DeviceTypes getType() {
		return mType;
	}

	public void setType(DeviceTypes type) {
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

	public Date getAdded() {
		return mAdded;
	}

	public void setAdded(Date added) {
		this.mAdded = added;
	}

	public boolean isActive() {
		return mActive;
	}

	public void setActive(boolean active) {
		this.mActive = active;
	}

	public abstract T getState();
	
	public abstract void setState(T state);
}
