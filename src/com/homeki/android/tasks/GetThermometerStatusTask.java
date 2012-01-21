package com.homeki.android.tasks;

import java.io.IOException;
import java.math.BigDecimal;

import android.os.AsyncTask;

import com.google.gson.GsonBuilder;
import com.homeki.android.HomekiApplication;
import com.homeki.android.communication.json.JsonStatus;
import com.homeki.android.device.Dimmer;
import com.homeki.android.device.Thermometer;

public class GetThermometerStatusTask extends AsyncTask<Void, Void, JsonStatus> {
	private Thermometer t;
	
	public GetThermometerStatusTask(Thermometer t) {
		this.t = t;
	}
	
	@Override
	protected JsonStatus doInBackground(Void... params) {
		String s = "";
		
		try {
			s = HomekiApplication.getInstance().remote().getDeviceStatus(t.getId());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new GsonBuilder().create().fromJson(s, JsonStatus.class);
	}
	
	@Override
	protected void onPostExecute(JsonStatus s) {
		float temperature = ((BigDecimal)s.status).floatValue();
		t.setStatus(temperature);
		HomekiApplication.getInstance().notifyChanged();
	}
}
