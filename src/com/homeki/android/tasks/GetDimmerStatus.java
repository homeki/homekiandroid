package com.homeki.android.tasks;

import java.io.IOException;

import android.os.AsyncTask;

import com.google.gson.GsonBuilder;
import com.homeki.android.HomekiApplication;
import com.homeki.android.commands.Commands;
import com.homeki.android.communication.json.JsonStatus;
import com.homeki.android.device.Dimmer;

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
