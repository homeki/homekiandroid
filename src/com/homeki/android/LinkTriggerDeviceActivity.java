package com.homeki.android;

import java.util.ArrayList;

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

import com.homeki.android.communication.json.JsonTriggerDeviceLink;
import com.homeki.android.tasks.GetLinkedDevicesTask;
import com.homeki.android.tasks.GetLinkedTriggersTask;
import com.homeki.android.tasks.LinkTriggerDevice;

public class LinkTriggerDeviceActivity extends ListActivity {
	private ArrayAdapter<JsonTriggerDeviceLink> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int triggerId = getIntent().getIntExtra("triggerId", -1);
		int deviceId = getIntent().getIntExtra("deviceId", -1);
		ArrayList<JsonTriggerDeviceLink> devices = new ArrayList<JsonTriggerDeviceLink>();
		adapter = new ArrayAdapter<JsonTriggerDeviceLink>(this, android.R.layout.simple_list_item_multiple_choice, devices);
		getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		setListAdapter(adapter);
		if (triggerId != -1){
			new GetLinkedDevicesTask(getListView(), triggerId).execute();
		} else if (deviceId != -1){
			new GetLinkedTriggersTask(getListView(), deviceId).execute();
		}
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		JsonTriggerDeviceLink link = (JsonTriggerDeviceLink) adapter.getItem(position);
		new LinkTriggerDevice(link.deviceId, link.triggerId, getListView().isItemChecked(position)).execute();
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
			new AlertDialog.Builder(LinkTriggerDeviceActivity.this).setMessage("OMG THE SERVER IS BROKEN?").setPositiveButton("Jahapp...", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					/* User clicked OK so do some stuff */
				}
			}).show();
		}
	};
}
