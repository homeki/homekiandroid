package com.homeki.android.model;

import java.util.Date;

public class DataPoint {
	private Date time;
	private double value;

	public DataPoint(Date time, double value) {
		super();
		this.time = time;
		this.value = value;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return time.getTime() + " : " + value;
	}
}
