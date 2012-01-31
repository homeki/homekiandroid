package com.homeki.android.trigger;

import com.homeki.android.communication.json.JsonTimerTrigger;

public class TimerTrigger extends Trigger {
	private Integer time;
	private Integer repeatType;
	private Integer days;
	
	public TimerTrigger(JsonTimerTrigger t) {
		super(t);
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	public Integer getRepeatType() {
		return repeatType;
	}

	public void setRepeatType(Integer repeatType) {
		this.repeatType = repeatType;
	}

	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}
}
