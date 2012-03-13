package com.homeki.android.tasks;

import java.io.IOException;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.homeki.android.HomekiApplication;
import com.homeki.android.communication.json.JsonTimerTrigger;
import com.homeki.android.trigger.TimerTrigger;

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
		Log.d("LOG", "asdf" + s);
		return new GsonBuilder().create().fromJson(s, JsonTimerTrigger.class);
	}
	
	@Override
	protected void onPostExecute(JsonTimerTrigger result) {
		Log.d("LOG", result.toString());
		t.loadTimerStuff(result);
		HomekiApplication.getInstance().notifyTriggerChanged();
	}
}
