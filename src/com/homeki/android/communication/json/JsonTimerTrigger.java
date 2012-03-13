package com.homeki.android.communication.json;

public class JsonTimerTrigger extends JsonTrigger {
	public Integer time;
	public Integer repeatType;
	public Integer days;
	
	@Override
	public String toString() {
		String s = time + "<-time ";
		s += repeatType + "<-repeat ";
		s += days + "<-days ";
		
		// TODO Auto-generated method stub
		return s;
	}
}
