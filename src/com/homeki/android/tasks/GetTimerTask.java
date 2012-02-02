package com.homeki.android.tasks;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.homeki.android.HomekiApplication;
import com.homeki.android.R;
import com.homeki.android.communication.json.JsonStatus;
import com.homeki.android.communication.json.JsonTimerTrigger;
import com.homeki.android.communication.json.JsonTrigger;
import com.homeki.android.trigger.TimerTrigger;
import com.homeki.android.trigger.Trigger;

public class GetTimerTask extends AsyncTask<Void, Void, JsonTimerTrigger> {
	private final TimerTrigger t;

	public GetTimerTask(TimerTrigger t) {
		this.t = t;
	}
	
	@Override
	protected JsonTimerTrigger doInBackground(Void... params) {
		String s = "";
		try {
			s = HomekiApplication.getInstance().remote().getTimer(t.getId());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return null;
		}
		return new GsonBuilder().create().fromJson(s, JsonTimerTrigger.class);
	}
	
	@Override
	protected void onPostExecute(JsonTimerTrigger result) {
		t.loadTimerStuff(result);
		HomekiApplication.getInstance().notifyTriggerChanged();
	}
}
