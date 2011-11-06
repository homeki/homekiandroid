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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.homeki.android.device.Device;
import com.homeki.android.device.Dimmer;
import com.homeki.android.device.Switch;
import com.homeki.android.device.Thermometer;
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
	
	private class MyAdapter extends ArrayAdapter<Device> implements OnCheckedChangeListener, OnSeekBarChangeListener {
		public MyAdapter(Context context, List<Device> objects) {
			super(context, -1, objects);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Device dev = getItem(position);
			
			// set specific properties
			if (dev.getClass() == Switch.class) {
				convertView = mInflater.inflate(R.layout.listitem_switch, null);
				Switch sw = (Switch)dev;
				
				TextView tv = (TextView)convertView.findViewById(R.id.switch_title);
				tv.setText(sw.toString());
				
				CheckBox cb = (CheckBox)convertView.findViewById(R.id.switch_toggle);
				cb.setChecked(sw.getStatus());
				cb.setTag(position);
				cb.setOnCheckedChangeListener(this);
			} else if (dev.getClass() == Dimmer.class) {
				convertView = mInflater.inflate(R.layout.listitem_dimmer, null);
				Dimmer dim = (Dimmer)dev;
				
				TextView tv = (TextView)convertView.findViewById(R.id.dimmer_title);
				tv.setText(dim.toString());
				
				SeekBar sb = (SeekBar)convertView.findViewById(R.id.dimmer_seekbar);
				sb.setMax(255);
				sb.setProgress(dim.getLevel());
				sb.setTag(position);
				sb.setOnSeekBarChangeListener(this);
			} else if (dev.getClass() == Thermometer.class) {
				convertView = mInflater.inflate(R.layout.listitem_thermometer, null);
				Thermometer therm = (Thermometer)dev;
			}
			
			return convertView;
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			int loc = (Integer)buttonView.getTag();
			Switch s = (Switch)list.get(loc);
			
			if (isChecked)
				s.switchOn(buttonView.getContext());
			else
				s.switchOff(buttonView.getContext());
		}

		@Override
		public void onProgressChanged(SeekBar sb, int progress, boolean fromUser) {

		}

		@Override
		public void onStartTrackingTouch(SeekBar sb) {

		}

		@Override
		public void onStopTrackingTouch(SeekBar sb) {
			int loc = (Integer)sb.getTag();
			Dimmer d = (Dimmer)list.get(loc);
			d.dim(sb.getContext(), sb.getProgress());
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
