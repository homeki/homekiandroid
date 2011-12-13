package com.homeki.android.tasks;

import java.io.IOException;

import android.os.AsyncTask;

import com.google.gson.GsonBuilder;
import com.homeki.android.HomekiApplication;
import com.homeki.android.communication.json.JsonDevice;

public class SetDevice extends AsyncTask<Void, Void, String> {
	private int id;
	private JsonDevice device;
	
	public SetDevice(int id){
		this.id = id;
		this.device = new JsonDevice();
	}
	
	public SetDevice setName(String name) {
		device.name = name;
		return this;
	}
	
	public void setDescription(String description) {
		device.description = description;
	}
	
	@Override
	protected String doInBackground(Void... params) {
		String s = new GsonBuilder().create().toJson(device);
		
		try {
			s = HomekiApplication.getInstance().remote().setDevice(id, s);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return s;
	}
}
