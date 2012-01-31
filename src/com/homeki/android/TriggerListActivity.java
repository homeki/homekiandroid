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
import android.widget.ListView;
import android.widget.TextView;

import com.homeki.android.tasks.GetDevicesTask;
import com.homeki.android.tasks.GetTriggerTask;
import com.homeki.android.trigger.Trigger;

public class TriggerListActivity extends ListActivity {
	private ArrayAdapter<Trigger> adapter;
	private LayoutInflater inflater;
	private List<Trigger> list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		inflater = getLayoutInflater();
		list = HomekiApplication.getInstance().getTriggerList();
		
		adapter = new MyAdapter(this, list);
		setListAdapter(adapter);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		IntentFilter filter = new IntentFilter(getString(R.string.server_not_found_action));
		registerReceiver(serverTimeoutReceiver, filter);
		HomekiApplication.getInstance().registerTriggerWatcher(adapter);
		new GetTriggerTask().execute();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(serverTimeoutReceiver);
		HomekiApplication.getInstance().unregisterTriggerWatcher(adapter);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		Trigger t = adapter.getItem(position);
		if (t instanceof Trigger) {
			Intent intent = new Intent(this, TimerTriggerActivity.class);
			intent.putExtra("id", t.getId());
			startActivity(intent);
		}
		
	}
	
	private class MyAdapter extends ArrayAdapter<Trigger> {
		public MyAdapter(Context context, List<Trigger> objects) {
			super(context, -1, objects);
		}
		
		@Override
		public int getViewTypeCount() {
			return 1;
		}
		
		@Override
		public int getItemViewType(int position) {
			/*
			 * Device dev = getItem(position); if (dev.getClass() ==
			 * Switch.class) { return 0; } else if (dev.getClass() ==
			 * Dimmer.class) { return 1; } else if (dev.getClass() ==
			 * Thermometer.class) { return 2; } return -1;
			 */
			return 0;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder vh;
			Trigger trigger = getItem(position);

			if (convertView == null) {
				vh = new ViewHolder();
				convertView = inflater.inflate(R.layout.listitem_switch, null);
				vh.tv = (TextView) convertView.findViewById(R.id.switch_title);
				vh.cb = (CheckBox) convertView.findViewById(R.id.switch_toggle);
				convertView.setTag(vh);
			}
			
			vh = (ViewHolder) convertView.getTag();
			vh.tv.setText(trigger.getName());
			vh.cb.setTag(position);
			return convertView;
		}
		
		class ViewHolder {
			CheckBox cb;
			TextView tv;
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, Menu.FIRST, Menu.NONE, "Edit Prefs").setIcon(android.R.drawable.ic_menu_preferences);
		menu.add(Menu.NONE, 2, Menu.NONE, "Refresh List").setIcon(android.R.drawable.ic_menu_rotate);
		menu.add(Menu.NONE, 3, Menu.NONE, "Add trigger").setIcon(android.R.drawable.ic_input_add);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case Menu.FIRST:
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		case 2:
			new GetTriggerTask().execute();
			return true;
		case 3:
			startActivity(new Intent(this, AddTriggerActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	BroadcastReceiver serverTimeoutReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			new AlertDialog.Builder(TriggerListActivity.this).setMessage("OMG THE SERVER IS BROKEN?").setPositiveButton("Jahapp...", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					/* User clicked OK so do some stuff */
				}
			}).show();
		}
	};
}
