package com.homeki.android.tasks;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import android.os.AsyncTask;
import android.widget.ListView;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.homeki.android.HomekiApplication;
import com.homeki.android.LinkTriggerDeviceActivity.MyArrayAdapter;
import com.homeki.android.communication.json.JsonDeviceLink;

public class GetLinkedDevicesTask extends AsyncTask<Void, Void, List<JsonDeviceLink>> {
	private int id;
	private final ListView lv;

	public GetLinkedDevicesTask(ListView lv, int id) {
		this.lv = lv;
		this.id = id;
	}
	

	@Override
	protected List<JsonDeviceLink> doInBackground(Void... params) {
		String s = "";
		try {
			s = HomekiApplication.getInstance().remote().getLinkedDevices(id);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return null;
		}
		Type listType = new TypeToken<List<JsonDeviceLink>>() {}.getType();
		return new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().fromJson(s, listType);
	}
	
	@Override
	protected void onPostExecute(List<JsonDeviceLink> result) {
		if (result != null) {
			@SuppressWarnings("unchecked")
			MyArrayAdapter aa = ((MyArrayAdapter)lv.getAdapter());
			aa.clear();
			int i = 0;
			for (JsonDeviceLink jdl: result) {
				aa.add(jdl.name, jdl.id);
				lv.setItemChecked(i++, jdl.linked);
			}
			aa.notifyDataSetChanged();
		}
	}
}
