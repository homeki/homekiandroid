package com.homeki.android;

import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.homeki.android.device.Device;
import com.homeki.android.device.Dimmer;
import com.homeki.android.device.Switch;
import com.homeki.android.device.Thermometer;
import com.homeki.android.tasks.GetDevicesTask;
import com.homeki.android.tasks.SetDevice;

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
		
		new SetDevice(mApplication, 1, "jonas").execute();
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
		Log.d("LOG", "clicky");
	}
	
	private class MyAdapter extends ArrayAdapter<Device> implements OnCheckedChangeListener, OnSeekBarChangeListener {
		public MyAdapter(Context context, List<Device> objects) {
			super(context, -1, objects);
		}
		
		@Override
		public int getViewTypeCount() {
			return 3;
		}
		
		@Override
		public int getItemViewType(int position) {
			Device dev = getItem(position);
			if (dev.getClass() == Switch.class) {
				return 0;
			} else if (dev.getClass() == Dimmer.class) {
				return 1;
			} else if (dev.getClass() == Thermometer.class) {
				return 2;
			}
			return -1;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Device dev = getItem(position);
			int type = getItemViewType(position);
			ViewHolder vh;
			if (convertView == null) {
				Log.d("LOG", "No converView :(");
				vh = new ViewHolder();
				switch (type) {
				case 0:
					convertView = mInflater.inflate(R.layout.listitem_switch, null);
					vh.tv = (TextView) convertView.findViewById(R.id.switch_title);
					vh.cb = (CheckBox) convertView.findViewById(R.id.switch_toggle);
					vh.cb.setOnCheckedChangeListener(this);
					break;
				case 1:
					convertView = mInflater.inflate(R.layout.listitem_dimmer, null);
					vh.tv = (TextView) convertView.findViewById(R.id.dimmer_title);
					vh.sb = (SeekBar) convertView.findViewById(R.id.dimmer_seekbar);
					vh.sb.setOnSeekBarChangeListener(this);
					break;
				case 2:
					convertView = mInflater.inflate(R.layout.listitem_thermometer, null);
					vh.tv = (TextView) convertView.findViewById(R.id.thermometer_title);
					break;
				}
				convertView.setTag(vh);
			} else {
				Log.d("TAG", "YAY CONVERTVIEW IS THE SHIT!");
			}
			vh = (ViewHolder) convertView.getTag();
			switch (type) {
			case 0:
				vh.tv.setText(dev.toString());
				vh.cb.setTag(position);
				vh.cb.setChecked(((Switch) dev).getStatus());
				break;
			case 1:
				vh.tv.setText(dev.toString());
				vh.sb.setTag(position);
				vh.sb.setProgress(((Dimmer) dev).getLevel());
				break;
			case 2:
				vh.tv.setText(((Thermometer) dev).getStatus() + "");
				break;
			}
			return convertView;
		}
		
		class ViewHolder {
			CheckBox cb;
			TextView tv;
			SeekBar sb;
		}
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			int loc = (Integer) buttonView.getTag();
			Switch s = (Switch) list.get(loc);
			
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
			int loc = (Integer) sb.getTag();
			Dimmer d = (Dimmer) list.get(loc);
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
