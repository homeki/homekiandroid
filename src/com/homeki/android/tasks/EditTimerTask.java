package com.homeki.android.tasks;

import java.io.IOException;

import android.os.AsyncTask;

import com.google.gson.GsonBuilder;
import com.homeki.android.HomekiApplication;
import com.homeki.android.communication.json.JsonTimerTrigger;

public class EditTimerTask extends AsyncTask<Void, Void, String> {
	private JsonTimerTrigger trigger;
	private int id;
	
	public EditTimerTask(){
		this.trigger = new JsonTimerTrigger();
	}
	
	public EditTimerTask setName(CharSequence name) {
		trigger.name = name.toString();
		return this;
	}
	
	public EditTimerTask setValue(CharSequence value) {
		trigger.newValue = Integer.parseInt(value.toString());
		return this;
	}
	
	public EditTimerTask setTime(int time) {
		trigger.time = time;
		return this;
	}
	
	public EditTimerTask setRepeatType(CharSequence value) {
		trigger.repeatType = Integer.parseInt(value.toString());
		return this;
	}
	
	public EditTimerTask setDays(CharSequence value) {
		trigger.days = Integer.parseInt(value.toString());
		return this;
	}
	
	public EditTimerTask setId(int id) {
		this.id = id;
		return this;
	}
	
	@Override
	protected String doInBackground(Void... params) {
		String s = new GsonBuilder().create().toJson(trigger);
		
		try {
			if (id != -1) {
				HomekiApplication.getInstance().remote().editTimer(s, id);
			} else {
				HomekiApplication.getInstance().remote().addTimer(s);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return s;
	}
}
