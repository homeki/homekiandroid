package com.homekey.android.tasks;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.homekey.android.commands.Commands;

import device.Device;
import device.Dimmer;
import device.JsonDevice;
import device.Lamp;

public class GetDevicesTask extends AsyncTask<Void, Void, List<JsonDevice>> {

	private final ArrayAdapter<Device> mListAdapter;
	private final Context c;
	
	public GetDevicesTask(Context c, ArrayAdapter<Device> listAdapter){
		mListAdapter = listAdapter;
		this.c = c;
	}
	
	@Override
	protected List<JsonDevice> doInBackground(Void... params) {
		
		String s = "";
		try {
			s = Commands.getDevices(c);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Type listType = new TypeToken<List<JsonDevice>>() {}.getType();
		return new Gson().fromJson(s, listType);
	}
	
	@Override
	protected void onPostExecute(List<JsonDevice> result) {
		super.onPostExecute(result);
		mListAdapter.clear();
		for (JsonDevice d : result) {
			if (d.type.contains("Dimmer")) {
				mListAdapter.add(new Dimmer(d));
			} else if (d.type.contains("Switch")) {
				mListAdapter.add(new Lamp(d));
			}		
		}
	}
}
