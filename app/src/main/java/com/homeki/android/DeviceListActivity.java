package com.homeki.android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.homeki.android.model.DeviceListModel;
import com.homeki.android.model.devices.Device;
import com.homeki.android.server.ActionPerformer;
import com.homeki.android.server.ActionPerformer.OnDeviceListReceivedListener;
import com.homeki.android.server.ServerActionPerformer;

import java.util.List;

public class DeviceListActivity extends ActionBarActivity {
	private ProgressDialog progressDialog;
	private DeviceListModel model;
	private DrawerLayout drawerLayout;
	private ActionBarDrawerToggle drawerToggle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.device_list);
		
		model = DeviceListModel.getModel();

		progressDialog = new ProgressDialog(this);
		progressDialog.setIndeterminate(true);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage("Loading...");

		drawerLayout = (DrawerLayout)findViewById(R.id.main_layout);
		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, R.string.app_name, R.string.app_name);
		drawerLayout.setDrawerListener(drawerToggle);
		drawerToggle.setDrawerIndicatorEnabled(true);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);

		findViewById(R.id.main_devices_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(DeviceListActivity.this, DeviceListActivity.class));
			}
		});

		findViewById(R.id.main_settings_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(DeviceListActivity.this, SettingsActivity.class));
			}
		});

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.main_frame, new DeviceListFragment(), "Device List Fragment");
		transaction.commit();
	}
	
	@Override
	protected void onResume() {
		Log.i("DeviceCollectionActivity", "onResume()");

		progressDialog.show();

		ActionPerformer actionPerformer = new ServerActionPerformer(this);
		actionPerformer.requestDeviceList(new OnDeviceListReceivedListener() {
			@Override
			public void onDeviceListReceived(List<Device> devices) {
				Log.i("DeviceCollectionActivity", "onResume() -> onDeviceListReceived()");
				if (devices != null && devices.size() > 0) {
					model.setDeviceList(devices);
				} else {
					Toast.makeText(DeviceListActivity.this, "Failed to get device list. Check server settings.", Toast.LENGTH_LONG).show();
				}

				progressDialog.dismiss();
			}
		});
		
		super.onResume();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (drawerToggle.onOptionsItemSelected(item)) return true;
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerToggle.syncState();
	}
}
