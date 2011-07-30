package com.homekey.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.homekey.android.communication.CommandSender;
import com.homekey.android.tasks.GetDevicesTask;

import device.Device;
import device.Dimmer;
import device.JsonDevice;
import device.Lamp;

public class DeviceList extends ListActivity {
	ArrayAdapter<Device> myAdapter;
	private LayoutInflater mInflater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mInflater = getLayoutInflater();
		myAdapter = new MyAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, new ArrayList<Device>());
		
		setListAdapter(myAdapter);
		
		new GetDevicesTask(this, myAdapter).execute();
	}
	
	private class MyAdapter extends ArrayAdapter<Device> implements OnClickListener {
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
			convertView.setOnClickListener(this);
			// set the text... not important
			TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
			tv.setText(text);
			// !!! and this is the most important part: you are settin listener
			// for the whole row
			convertView.setOnClickListener(new OnItemClickListener(position));
			return convertView;
		}
		
		@Override
		public void onClick(View v) {
			Log.v("LGO", "Row button clicked");
		}
		
		class OnItemClickListener implements OnClickListener {
			private int mPosition;
			
			OnItemClickListener(int position) {
				mPosition = position;
				
			}
			
			@Override
			public void onClick(View arg0) {
				Log.d("ASDF", getItem(mPosition).toString());
//				Log.v("LOG", "onItemClick at position" + mPosition);
			}
		}
	}	
}
