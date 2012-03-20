package com.homeki.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.homeki.android.tasks.GetLinkedDevicesTask;
import com.homeki.android.tasks.LinkTriggerDevice;

public class LinkDeviceTriggerActivity extends ListActivity {
	private MyArrayAdapter adapter;
	private int id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.id = getIntent().getIntExtra("id", -1);
		
		ArrayList<String> devices = new ArrayList<String>();
		adapter = new MyArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, devices);
		getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		setListAdapter(adapter);

		new GetLinkedDevicesTask(getListView(), id).execute();
	}
	
	public class MyArrayAdapter extends ArrayAdapter<String> {
		private Map<String, Integer> map = new HashMap<String, Integer>();
		
		public MyArrayAdapter(Context context, int textViewResourceId, List<String> objects) {
			super(context, textViewResourceId, objects);
		}
		
		public void add(String object, int id) {
			super.add(object);
			map.put(object, id);
		}

		public int getId(String name) {
			return map.get(name);
		}
		
		@Override
		public void clear() {
			super.clear();
			map.clear();
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		new LinkTriggerDevice(adapter.getId(adapter.getItem(position)), this.id, getListView().isItemChecked(position)).execute();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		IntentFilter filter = new IntentFilter(getString(R.string.server_not_found_action));
		registerReceiver(serverTimeoutReceiver, filter);
		// HomekiApplication.getInstance().registerListWatcher(adapter);
		// new GetDevicesTask().execute();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(serverTimeoutReceiver);
	}
	
	BroadcastReceiver serverTimeoutReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			new AlertDialog.Builder(LinkDeviceTriggerActivity.this).setMessage("OMG THE SERVER IS BROKEN?").setPositiveButton("Jahapp...", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					/* User clicked OK so do some stuff */
				}
			}).show();
		}
	};
}
