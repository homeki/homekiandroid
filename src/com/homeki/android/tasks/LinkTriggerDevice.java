package com.homeki.android.tasks;

import java.io.IOException;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.homeki.android.HomekiApplication;
import com.homeki.android.communication.json.JsonHistory;
import com.homeki.android.device.Device;

public class LinkTriggerDevice extends AsyncTask<Void, Void, Void> {
	
	private final int deviceId;
	private final int triggerId;
	private final boolean link;
	
	public LinkTriggerDevice(int deviceId, int triggerId, boolean link) {
		this.deviceId = deviceId;
		this.triggerId = triggerId;
		this.link = link;
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		String s = "";
		
		try {
			s = HomekiApplication.getInstance().remote().linkDeviceTrigger(deviceId, triggerId, link);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
