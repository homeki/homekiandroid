package com.homeki.android.commands;

import java.io.IOException;

import android.content.Context;

import com.homeki.android.SharedPreferenceHelper;
import com.homeki.android.communication.CommandSender;

public class Commands {
	
	private static String sendCommand(Context c, String command) throws IOException {
		String address = String.format("http://%s", SharedPreferenceHelper.getStringValue(c, "server"));		
		return CommandSender.sendCommand(String.format("%s/%s", address, command));
	}
	
	public static String getDevices(Context c) throws IOException {
		return sendCommand(c, "get/devices");
	}
	
	public static String switchOn(Context c, int id) throws IOException {
		String command = String.format("set/on?id=%d", id);
		return sendCommand(c, command);
	}
	
	public static String switchOff(Context c, int id) throws IOException {
		String command = String.format("set/off?id=%d", id);
		return sendCommand(c, command);
	}
	
	public static String dim(Context c, int id, int level) throws IOException {
		String command = String.format("set/dim?id=%d&level=%d", id, level);
		return sendCommand(c, command);
	}
}
