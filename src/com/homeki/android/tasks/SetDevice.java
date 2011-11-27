package com.homeki.android.tasks;

import java.io.IOException;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.GsonBuilder;
import com.homeki.android.commands.Commands;
import com.homeki.android.communication.json.JsonDevice;
import com.homeki.android.communication.json.JsonStatus;

public class SetDevice extends AsyncTask<Void, Void, String> {
	private final Context c;
	private int id;
	private JsonDevice device;
	
	public SetDevice(Context c, int id){
		this.c = c;
		this.id = id;
		this.device = new JsonDevice();
	}
	
	public void setName(String name) {
		device.name = name;
	}
	
	public void setDescription(String description) {
		device.description = description;
	}
	
	@Override
	protected String doInBackground(Void... params) {
		String s = new GsonBuilder().create().toJson(device);
		
		try {
			s = Commands.setDevice(c, id, s);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return s;
	}
}