package com.homeki.android.tasks;

import java.io.IOException;

import android.os.AsyncTask;

import com.google.gson.GsonBuilder;
import com.homeki.android.HomekiApplication;
import com.homeki.android.communication.json.JsonTimerTrigger;

public class AddTimerTask extends AsyncTask<Void, Void, String> {
	private JsonTimerTrigger trigger;
	
	public AddTimerTask(){
		this.trigger = new JsonTimerTrigger();
	}
	
	public AddTimerTask setName(CharSequence name) {
		trigger.name = name.toString();
		return this;
	}
	
	public AddTimerTask setValue(CharSequence value) {
		trigger.newValue = Integer.parseInt(value.toString());
		return this;
	}
	
	public AddTimerTask setTime(CharSequence value) {
		trigger.time = Integer.parseInt(value.toString());
		return this;
	}
	
	public AddTimerTask setRepeatType(CharSequence value) {
		trigger.repeatType = Integer.parseInt(value.toString());
		return this;
	}
	
	public AddTimerTask setDays(CharSequence value) {
		trigger.days = Integer.parseInt(value.toString());
		return this;
	}
	
	@Override
	protected String doInBackground(Void... params) {
		String s = new GsonBuilder().create().toJson(trigger);
		
		try {
			HomekiApplication.getInstance().remote().addTimer(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return s;
	}
}
