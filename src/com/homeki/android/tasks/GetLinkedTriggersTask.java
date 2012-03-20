package com.homeki.android.tasks;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.homeki.android.HomekiApplication;
import com.homeki.android.communication.json.JsonTriggerDeviceLink;

public class GetLinkedTriggersTask extends AsyncTask<Void, Void, List<JsonTriggerDeviceLink>> {
	private int id;
	private final ListView lv;

	public GetLinkedTriggersTask(ListView lv, int id) {
		this.lv = lv;
		this.id = id;
	}
	

	@Override
	protected List<JsonTriggerDeviceLink> doInBackground(Void... params) {
		String s = "";
		try {
			s = HomekiApplication.getInstance().remote().getLinkedTriggers(id);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return null;
		}
		Type listType = new TypeToken<List<JsonTriggerDeviceLink>>() {}.getType();
		Log.d("LOG", s);
		return new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().fromJson(s, listType);
	}
	
	@Override
	protected void onPostExecute(List<JsonTriggerDeviceLink> result) {
		if (result != null) {
			@SuppressWarnings("unchecked")
			ArrayAdapter<JsonTriggerDeviceLink> aa = (ArrayAdapter<JsonTriggerDeviceLink>) lv.getAdapter();
			aa.clear();
			int i = 0;
			for (JsonTriggerDeviceLink jdl: result) {
				aa.add(jdl);
				lv.setItemChecked(i++, jdl.linked);
			}
			aa.notifyDataSetChanged();
		}
	}
}
