package com.homeki.android.tasks;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import android.os.AsyncTask;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.homeki.android.HomekiApplication;
import com.homeki.android.commands.Commands;
import com.homeki.android.device.Dimmer;
import com.homeki.android.device.JsonDevice;
import com.homeki.android.device.JsonStatus;

public class GetDimmerStatus extends AsyncTask<Void, Void, JsonStatus> {
	private final HomekiApplication ha;
	private Dimmer d;
	
	public GetDimmerStatus(HomekiApplication ha, Dimmer d) {
		this.ha = ha;
		this.d = d;
	}
	
	@Override
	protected JsonStatus doInBackground(Void... params) {
		String s = "";
		
		try {
			s = Commands.getDeviceStatus(ha, d.getId());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new GsonBuilder().create().fromJson(s, JsonStatus.class);
	}
	
	@Override
	protected void onPostExecute(JsonStatus s) {
		int level = (Integer)s.status;
		d.setLevel(level);
		ha.notifyChanged();
	}
}
