package com.homeki.android.model;

import java.util.Date;

public class DataPoint {
	
	private Date mTime;
	private double mValue;
	
	public DataPoint(Date time, double value) {
		super();
		this.mTime = time;
		this.mValue = value;
	}
	public Date getTime() {
		return mTime;
	}
	public void setTime(Date time) {
		this.mTime = time;
	}
	public double getValue() {
		return mValue;
	}
	public void setValue(double value) {
		this.mValue = value;
	}	
}
