package com.homeki.android.tasks;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.homeki.android.HomekiApplication;
import com.homeki.android.commands.Commands;
import com.homeki.android.communication.json.JsonDevice;
import com.homeki.android.device.Device;
import com.homeki.android.device.Dimmer;
import com.homeki.android.device.Switch;
import com.homeki.android.device.Thermometer;

public class GetDevicesTask extends AsyncTask<Void, Void, List<JsonDevice>> {
	private final HomekiApplication ha;
	
	public GetDevicesTask(HomekiApplication ha) {
		this.ha = ha;
	}
	
	@Override
	protected List<JsonDevice> doInBackground(Void... params) {
		String s = "";
		try {
			s = Commands.getDevices(ha);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Type listType = new TypeToken<List<JsonDevice>>() {}.getType();
		return new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create().fromJson(s, listType);
	}

	@Override
	protected void onPostExecute(List<JsonDevice> result) {
		if (result != null) {
			List<Device> list = new ArrayList<Device>();
			for (JsonDevice d : result) {
				if (d.type.contains("Dimmer")) {
					Dimmer s = new Dimmer(d);
					new GetDimmerStatus(ha, s).execute();
					list.add(s);
				} else if (d.type.contains("Switch")) {
					Switch s = new Switch(d);
					new GetSwitchStatus(ha, s).execute();
					list.add(s);
				} else if (d.type.contains("Temp")) {
					list.add(new Thermometer(d));
				}
			}
			ha.updateList(list);
		}
	}
}
