package com.homeki.android.communication;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.Log;

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
		return sendCommand("device/list");
	}
	
	public String getTriggers() throws IOException {
		return sendCommand("trigger/list");
	}

	public String deleteTrigger(int id) throws IOException {
		String command = String.format("trigger/delete?triggerid=%d", id);
		return sendCommand(command);
	}
	
	public String switchOn(int id) throws IOException {
		String command = String.format("device/state/set?deviceid=%d&value=%s", id, "on");
		return sendCommand(command);
	}
	
	public String switchOff(int id) throws IOException {
		String command = String.format("device/state/set?deviceid=%d&value=%s", id, "off");
		return sendCommand(command);
	}
	
	public String dim(int id, int level) throws IOException {
		String command = String.format("device/state/set?deviceid=%d&value=%d", id, level);
		return sendCommand(command);
	}
	public String getDeviceStatus(int id) throws IOException {
		String command = String.format("device/state/get?deviceid=%d", id);
		return sendCommand(command);
	}
	public String getHistory(int id, long startTime, long endTime) throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String start = sdf.format(new Date(startTime));
		String end = sdf.format(new Date(endTime));
		String command = String.format("device/state/list?deviceid=%d&from=%s&to=%s", id, start, end);
		return sendCommand(command);
	}

	public String setDevice(int id, String json) throws IOException {
		String command = String.format("device/set?deviceid=%d", id);
		return postCommand(command, json);
	}
	
	public String addTimer(String json) throws IOException {
		String command = String.format("trigger/timer/add");
		return postCommand(command, json);
	}
}
