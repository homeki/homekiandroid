package com.homeki.android.communication;

import java.io.IOException;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.homeki.android.SharedPreferenceHelper;

public class CommandSendingService extends IntentService {
	public static String turnLampOn = "switch.lamp.on";
	public static String turnLampOff = "switch.lamp.off";
	public static String dim = "dim.lamp";
	
	public CommandSendingService() {
		this("Whatever");
	}
	
	public CommandSendingService(String name) {
		super(name);
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {		
		String address = String.format("http://%s", SharedPreferenceHelper.getStringValue(this, "server"));		
		String action = intent.getAction();
		int id = intent.getIntExtra("id", -1);
		String command = null;
		if (action.equals(turnLampOn)) {
			command = String.format("%s/set/on?id=%d", address, id);
		} else if (action.equals(turnLampOff)) {
			command = String.format("%s/set/off?id=%d", address, id);
		} else if (action.equals(dim)) {
			int level = intent.getIntExtra("level", -1);
			command = String.format("%s/set/level?id=%d&level=%d", address, id, level);
		}
		
		try {
			CommandSender.sendCommand(command);
		} catch (IOException e) {
			Log.d("LOG", command);
			e.printStackTrace();
		}
	}
}
