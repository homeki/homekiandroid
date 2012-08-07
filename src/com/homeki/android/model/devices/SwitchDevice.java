package com.homeki.android.model.devices;

import java.util.Date;


public class SwitchDevice extends AbstractDevice<SwitchDevice.State> {

	public SwitchDevice(DeviceTypes type, int id, String name, String description, Date added, boolean active, int value) {
		mType = type;
		mId = id;
		mName = name;
		mDescription = description;
		mAdded = added;
		mActive = active;
		mState = new State(value);
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
		public int value;

		public State(int value) {
			this.value = value;
		}
	}
}
