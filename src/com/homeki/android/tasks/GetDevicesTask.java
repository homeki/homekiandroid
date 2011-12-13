package com.homeki.android.tasks;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.homeki.android.HomekiApplication;
import com.homeki.android.R;
import com.homeki.android.communication.json.JsonDevice;
import com.homeki.android.device.Device;
import com.homeki.android.device.Dimmer;
import com.homeki.android.device.Switch;
import com.homeki.android.device.Thermometer;

public class GetDevicesTask extends AsyncTask<Void, Void, List<JsonDevice>> {
	public GetDevicesTask() {
		
	}
	
	@Override
	protected List<JsonDevice> doInBackground(Void... params) {
		String s = "";
		try {
			s = HomekiApplication.getInstance().remote().getDevices();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return null;
		}
		Type listType = new TypeToken<List<JsonDevice>>() {}.getType();
		return new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().fromJson(s, listType);
	}
	
	@Override
	protected void onPostExecute(List<JsonDevice> result) {
		if (result != null) {
			List<Device> list = new ArrayList<Device>();
			
			for (JsonDevice d : result) {
				if (d.type.contains("Dimmer")) {
					Dimmer s = new Dimmer(d);
					new GetDimmerStatus(s).execute();
					list.add(s);
				} else if (d.type.contains("Switch")) {
					Switch s = new Switch(d);
					new GetSwitchStatus(s).execute();
					list.add(s);
				} else if (d.type.contains("Temp")) {
					list.add(new Thermometer(d));
				}
			}
			HomekiApplication.getInstance().updateList(list);
		} else {
			Context context = HomekiApplication.getInstance().getApplicationContext();
			context.sendBroadcast(new Intent(context.getString(R.string.server_not_found_action)));
			HomekiApplication.getInstance().updateList(new ArrayList<Device>());
		}
	}
}
