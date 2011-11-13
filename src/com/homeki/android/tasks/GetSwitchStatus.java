package com.homeki.android.tasks;

import java.io.IOException;

import android.os.AsyncTask;

import com.google.gson.GsonBuilder;
import com.homeki.android.HomekiApplication;
import com.homeki.android.commands.Commands;
import com.homeki.android.communication.json.JsonStatus;
import com.homeki.android.device.Switch;

public class GetSwitchStatus extends AsyncTask<Void, Void, JsonStatus> {
	private final HomekiApplication ha;
	private Switch d;
	
	public GetSwitchStatus(HomekiApplication ha, Switch d) {
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
		boolean on = (Boolean)s.status;
		d.setStatus(on);
		ha.notifyChanged();
	}
}
