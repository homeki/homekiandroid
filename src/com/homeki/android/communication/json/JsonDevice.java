package com.homeki.android.communication.json;

import java.util.Date;

public class JsonDevice {
	public String type;
	public Integer id;
	public String name;
	public Date added;
	public Object status;

	@Override
	public String toString() {
		return type;
	}
}
