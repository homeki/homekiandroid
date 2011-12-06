package com.homeki.android;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.Application;
import android.widget.ArrayAdapter;

import com.homeki.android.communication.HttpApi;
import com.homeki.android.device.Device;

public class HomekiApplication extends Application {
	private static HomekiApplication instance;
	
	private final HttpApi api;
	private List<Device> list;
	private List<ArrayAdapter<Device>> watchers;
	
	public HomekiApplication() {
		if (instance != null)
			throw new RuntimeException("Instance not null in HomekiApplication, should not happen!");
		
		instance = this;
		list = new ArrayList<Device>();
		watchers = new LinkedList<ArrayAdapter<Device>>();
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
	
	public synchronized void updateList(List<Device> newList){
		for (ArrayAdapter<Device> aa: watchers) {
			aa.notifyDataSetInvalidated();
		}
		list.clear();
		list.addAll(newList);
		notifyChanged();
	}
	
	public synchronized void notifyChanged() {
		for (ArrayAdapter<Device> aa: watchers) {
			aa.notifyDataSetChanged();
		}
	}
	
	public synchronized void registerListWatcher(ArrayAdapter<Device> aa) {
		watchers.add(aa);
	}
	
	public synchronized void unregisterListWatcher(ArrayAdapter<Device> aa) {
		watchers.remove(aa);
	}
}
