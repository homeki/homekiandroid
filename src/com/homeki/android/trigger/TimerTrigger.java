package com.homeki.android.trigger;

import com.homeki.android.communication.json.JsonTimerTrigger;
import com.homeki.android.communication.json.JsonTrigger;

public class TimerTrigger extends Trigger {
	public TimerTrigger(JsonTrigger t) {
		super(t);
	}

	private Integer time;
	private Integer repeatType;
	private Integer days;

	public void loadTimerStuff(JsonTimerTrigger t) {
		time = t.time;
		repeatType = t.repeatType;
		days = t.days;
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
