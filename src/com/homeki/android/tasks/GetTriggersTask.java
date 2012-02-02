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
import com.homeki.android.communication.json.JsonTrigger;
import com.homeki.android.trigger.TimerTrigger;
import com.homeki.android.trigger.Trigger;

public class GetTriggersTask extends AsyncTask<Void, Void, List<JsonTrigger>> {
	public GetTriggersTask() {
		
	}
	
	@Override
	protected List<JsonTrigger> doInBackground(Void... params) {
		String s = "";
		try {
			s = HomekiApplication.getInstance().remote().getTriggers();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return null;
		}
		Type listType = new TypeToken<List<JsonTrigger>>() {}.getType();
		return new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().fromJson(s, listType);
	}
	
	@Override
	protected void onPostExecute(List<JsonTrigger> result) {
		if (result != null) {
			List<Trigger> list = new ArrayList<Trigger>();
			
			for (JsonTrigger t : result) {
				TimerTrigger tr = new TimerTrigger(t);
				new GetTimerTask(tr).execute();
				list.add(tr);
			}
			HomekiApplication.getInstance().updateTriggerList(list);
		} else {
			Context context = HomekiApplication.getInstance().getApplicationContext();
			context.sendBroadcast(new Intent(context.getString(R.string.server_not_found_action)));
			HomekiApplication.getInstance().updateTriggerList(new ArrayList<Trigger>());
		}
	}
}
