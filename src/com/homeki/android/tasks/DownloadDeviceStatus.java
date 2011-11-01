package com.homeki.android.tasks;

import java.io.IOException;

import android.os.AsyncTask;

import com.homeki.android.HomekiApplication;
import com.homeki.android.commands.Commands;
import com.homeki.android.device.Lamp;

public class DownloadDeviceStatus extends AsyncTask<Void, Void, String> {
	private final HomekiApplication ha;
	private Lamp d;
	
	public DownloadDeviceStatus(HomekiApplication ha, Lamp d) {
		this.ha = ha;
		this.d = d;
	}
	
	@Override
	protected String doInBackground(Void... params) {
		String s = "";
		try {
			s = Commands.getDeviceStatus(ha, d.id);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return s;
	}
	
	@Override
	protected void onPostExecute(String s) {
		if (s.contains("true")) {
			d.setStatus(true);
		} else {
			d.setStatus(false);
		}
	}
}
