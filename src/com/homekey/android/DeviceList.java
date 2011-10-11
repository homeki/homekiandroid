package com.homekey.android;

import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.homekey.android.tasks.GetDevicesTask;

import device.Device;
import device.Lamp;

public class DeviceList extends ListActivity {
	ArrayAdapter<Device> myAdapter;
	private LayoutInflater mInflater;
	private List<Device> list;
	private HomekiApplication mApplication;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mApplication = (HomekiApplication)getApplication();
		mInflater = getLayoutInflater();

		list = mApplication.getList();
		
		myAdapter = new MyAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, list);
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
		list.add(new Lamp());
		myAdapter.notifyDataSetChanged();
		Log.d("LOG", "You clicked: " + myAdapter.getItem(position));
	}
	
	private class MyAdapter extends ArrayAdapter<Device> {
		public MyAdapter(Context context, int resource, int textViewResourceId, List<Device> objects) {
			super(context, resource, textViewResourceId, objects);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			String text = getItem(position).toString();
			if (null == convertView) {
				convertView = mInflater.inflate(android.R.layout.simple_list_item_1, null);
			}
			// take the Button and set listener. It will be invoked when you
			// click the button.
			// set the text... not important
			TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
			tv.setText(text);
			// !!! and this is the most important part: you are settin listener
			// for the whole row
			return convertView;
		}
	}	
}
