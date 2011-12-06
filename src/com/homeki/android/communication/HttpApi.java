package com.homeki.android.communication;

import java.io.IOException;

import android.content.Context;

import com.homeki.android.SettingsHelper;

public class HttpApi {
	private final HttpCommunication comm;
	private final Context context;
	
	public HttpApi(Context c) {
		this.context = c;
		this.comm = new HttpCommunication();
	}
	
	private String sendCommand(String command) throws IOException {
		String address = String.format("http://%s", SettingsHelper.getStringValue(context, "server"));
		return comm.sendCommand(String.format("%s/%s", address, command));
	}
	
	private String postCommand(String command, String values) throws IOException {
		String address = String.format("http://%s/%s", SettingsHelper.getStringValue(context, "server"), command);		
		return comm.postCommand(address, values);
	}
	
	public String getDevices() throws IOException {
		return sendCommand("get/devices");
	}
	
	public String switchOn(int id) throws IOException {
		String command = String.format("set/on?id=%d", id);
		return sendCommand(command);
	}
	
	public String switchOff(int id) throws IOException {
		String command = String.format("set/off?id=%d", id);
		return sendCommand(command);
	}
	
	public String dim(int id, int level) throws IOException {
		String command = String.format("set/dim?id=%d&level=%d", id, level);
		return sendCommand(command);
	}

	public String getDeviceStatus(int id) throws IOException {
		String command = String.format("get/status?id=%d", id);
		return sendCommand(command);
	}

	public String setDevice(int id, String json) throws IOException {
		String command = String.format("set/device?id=%d", id);
		return postCommand(command, json);
	}
}
