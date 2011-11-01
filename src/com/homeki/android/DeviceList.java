package com.homeki.android;

import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.homeki.android.device.Device;
import com.homeki.android.device.Dimmer;
import com.homeki.android.device.Lamp;
import com.homeki.android.tasks.GetDevicesTask;

public class DeviceList extends ListActivity {
	private ArrayAdapter<Device> myAdapter;
	private LayoutInflater mInflater;
	private List<Device> list;
	private HomekiApplication mApplication;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mApplication = (HomekiApplication) getApplication();
		mInflater = getLayoutInflater();
		
		list = mApplication.getList();
		
		myAdapter = new MyAdapter(this, list);
		setListAdapter(myAdapter);
		
		new GetDevicesTask(mApplication).execute();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		mApplication.registerListWatcher(myAdapter);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mApplication.unregisterListWatcher(myAdapter);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		Intent intent = new Intent(this, InspectDeviceActivity.class);
		intent.putExtra("device", position);
		startActivity(intent);
	}
	
	private class MyAdapter extends ArrayAdapter<Device> {
		public MyAdapter(Context context, List<Device> objects) {
			super(context, -1, objects);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Device dev = getItem(position);
			int type = getItemViewType(position);
			
			// ensure we have a view
			if (convertView == null) {
				if (type == 1) {
					convertView = mInflater.inflate(R.layout.listitem_switch, null);
				}
			}
			
			// set common properties
			TextView tv = (TextView) convertView.findViewById(R.id.switch_title);
			tv.setText(dev.toString());
			
			if (type == 1) {
				Lamp sw = (Lamp)dev;
				ToggleButton tb = (ToggleButton)convertView.findViewById(R.id.switch_toggle);
				tb.setChecked(sw.getStatus());
			}
			
			return convertView;
		}
		
		@Override
		public int getItemViewType(int position) {
			Device dev = getItem(position);
			
			if (dev.getClass() == Lamp.class) {
				return 1;
			} else if (dev.getClass() == Dimmer.class) {
				return 2;
			}
			
			return 99;
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, Menu.FIRST, Menu.NONE, "Edit Prefs").setIcon(android.R.drawable.ic_menu_preferences);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case Menu.FIRST:
			startActivity(new Intent(this, EditPreferences.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
