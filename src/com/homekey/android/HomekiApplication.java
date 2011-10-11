package com.homekey.android;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.Application;
import android.widget.ArrayAdapter;
import device.Device;

public class HomekiApplication extends Application {
	List<Device> list = new ArrayList<Device>();
	List<ArrayAdapter<Device>> watchers = new LinkedList<ArrayAdapter<Device>>();
	
	public List<Device> getList() {
		return list;
	}
	
	public void updateList(List<Device> newList){
		for (ArrayAdapter<Device> aa: watchers) {
			aa.notifyDataSetInvalidated();
		}
		list.clear();
		list.addAll(newList);
		for (ArrayAdapter<Device> aa: watchers) {
			aa.notifyDataSetChanged();
		}
	}
	
	public void registerListWatcher(ArrayAdapter<Device> aa) {
		watchers.add(aa);
	}
	
	public void unregisterListWatcher(ArrayAdapter<Device> aa) {
		watchers.remove(aa);
	}
}
