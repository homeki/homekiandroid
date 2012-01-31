package com.homeki.android;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.Application;
import android.widget.ArrayAdapter;

import com.homeki.android.communication.HttpApi;
import com.homeki.android.device.Device;
import com.homeki.android.trigger.Trigger;

public class HomekiApplication extends Application {
	private static HomekiApplication instance;
	
	private final HttpApi api;
	private List<Device> list;
	private List<Trigger> tList;
	
	private List<ArrayAdapter<Device>> watchers;
	private List<ArrayAdapter<Trigger>> triggerWatchers;
	
	public HomekiApplication() {
		if (instance != null)
			throw new RuntimeException("Instance not null in HomekiApplication, should not happen!");
		
		instance = this;
		list = new ArrayList<Device>();
		tList = new ArrayList<Trigger>();
		watchers = new LinkedList<ArrayAdapter<Device>>();
		triggerWatchers = new LinkedList<ArrayAdapter<Trigger>>();
		api = new HttpApi(this);
	}
	
	public static HomekiApplication getInstance() {
		if (instance == null)
			instance = new HomekiApplication();
		
		return instance;
	}
	
	public HttpApi remote() {
		return api;
	}
	
	public synchronized List<Device> getList() {
		return list;
	}
	
	public synchronized List<Trigger> getTriggerList() {
		return tList;
	}
	
	public synchronized Device getDevice(int id) {
		for (Device d: list) {
			if (d.getId() ==  id) {
				return d;
			}
		}
		return null;
	}
	
	public synchronized Trigger getTrigger(int id) {
		for (Trigger t: tList) {
			if (t.getId() ==  id) {
				return t;
			}
		}
		return null;
	}
	
	public synchronized void updateList(List<Device> newList){
		for (ArrayAdapter<Device> aa: watchers) {
			aa.notifyDataSetInvalidated();
		}
		list.clear();
		list.addAll(newList);
		notifyChanged();
	}
	
	public synchronized void updateTriggerList(List<Trigger> newList){
		for (ArrayAdapter<Trigger> aa: triggerWatchers) {
			aa.notifyDataSetInvalidated();
		}
		tList.clear();
		tList.addAll(newList);
		notifyChanged();
	}
	
	public synchronized void notifyChanged() {
		for (ArrayAdapter<Device> aa: watchers) {
			aa.notifyDataSetChanged();
		}
	}
	
	public synchronized void notifyTriggerChanged() {
		for (ArrayAdapter<Trigger> aa: triggerWatchers) {
			aa.notifyDataSetChanged();
		}
	}
	
	public synchronized void registerListWatcher(ArrayAdapter<Device> aa) {
		watchers.add(aa);
	}
	
	public synchronized void registerTriggerWatcher(ArrayAdapter<Trigger> aa) {
		triggerWatchers.add(aa);
	}
	
	public synchronized void unregisterListWatcher(ArrayAdapter<Device> aa) {
		watchers.remove(aa);
	}
	
	public synchronized void unregisterTriggerWatcher(ArrayAdapter<Trigger> aa) {
		triggerWatchers.remove(aa);
	}
}
