package com.homeki.android;

import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.homeki.android.device.Device;
import com.homeki.android.device.Dimmer;
import com.homeki.android.device.Switch;
import com.homeki.android.device.Thermometer;
import com.homeki.android.tasks.GetDevicesTask;

public class DeviceListActivity extends ListActivity {
	private ArrayAdapter<Device> adapter;
	private LayoutInflater inflater;
	private List<Device> list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		inflater = getLayoutInflater();
		list = HomekiApplication.getInstance().getList();
		
		adapter = new MyAdapter(this, list);
		setListAdapter(adapter);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		IntentFilter filter = new IntentFilter(getString(R.string.server_not_found_action));
		registerReceiver(serverTimeoutReceiver, filter);
		HomekiApplication.getInstance().registerListWatcher(adapter);
		new GetDevicesTask().execute();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(serverTimeoutReceiver);
		HomekiApplication.getInstance().unregisterListWatcher(adapter);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
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
			int type = getItemViewType(position);
			Device dev = getItem(position);
			ViewHolder vh;
			
			if (convertView == null) {
				vh = new ViewHolder();
				
				switch (type) {
				case 0:
					convertView = inflater.inflate(R.layout.listitem_switch, null);
					vh.tv = (TextView) convertView.findViewById(R.id.switch_title);
					vh.cb = (CheckBox) convertView.findViewById(R.id.switch_toggle);
					vh.cb.setOnCheckedChangeListener(this);
					break;
				case 1:
					convertView = inflater.inflate(R.layout.listitem_dimmer, null);
					vh.tv = (TextView) convertView.findViewById(R.id.dimmer_title);
					vh.sb = (SeekBar) convertView.findViewById(R.id.dimmer_seekbar);
					vh.sb.setOnSeekBarChangeListener(this);
					break;
				case 2:
					convertView = inflater.inflate(R.layout.listitem_thermometer, null);
					vh.tv = (TextView) convertView.findViewById(R.id.thermometer_title);
					break;
				}
				convertView.setTag(vh);
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
				s.switchOn();
			else
				s.switchOff();
		}
		
		@Override
		public void onProgressChanged(SeekBar sb, int progress, boolean fromUser) {}
		
		@Override
		public void onStartTrackingTouch(SeekBar sb) {}
		
		@Override
		public void onStopTrackingTouch(SeekBar sb) {
			int loc = (Integer) sb.getTag();
			Dimmer d = (Dimmer) list.get(loc);
			d.dim(sb.getProgress());
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
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	BroadcastReceiver serverTimeoutReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			new AlertDialog.Builder(DeviceListActivity.this).setMessage("OMG THE SERVER IS BROKEN?").setPositiveButton("Jahapp...", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					/* User clicked OK so do some stuff */
				}
			}).show();
		}
	};
}
