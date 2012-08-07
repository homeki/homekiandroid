package com.homeki.android.model.devices;

import java.util.Date;

public class DimmerDevice extends AbstractDevice<DimmerDevice.State> {

	public DimmerDevice(DeviceTypes type, int id, String name, String description, Date added, boolean active, int level, int value) {
		mType = type;
		mId = id;
		mName = name;
		mDescription = description;
		mAdded = added;
		mActive = active;
		mState = new State(level, value);
	}

	@Override
	public State getState() {
		return mState;
	}

	@Override
	public void setState(State state) {
		mState = state;
	}

	public class State {
		public int level;
		public int value;

		public State(int level, int value) {
			this.level = level;
			this.value = value;
		}
	}
}
