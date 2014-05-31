package com.homeki.android;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import com.homeki.android.model.DeviceListModel;
import com.homeki.android.model.devices.Device;
import com.homeki.android.server.ActionPerformer;
import com.homeki.android.server.ActionPerformer.OnDeviceListReceivedListener;
import com.homeki.android.server.ServerActionPerformer;

public class DeviceCollectionActivity extends ActionBarActivity {
	private ProgressDialog progressDialog;
	private DeviceListModel model;
	private Context context;
	private ActionBarDrawerToggle drawerToggle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		context = this;

		model = DeviceListModel.getModel(this);

		DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.main_root);
		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, R.string.app_name, R.string.app_name);
		drawerLayout.setDrawerListener(drawerToggle);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		
		progressDialog = new ProgressDialog(context);
		progressDialog.setIndeterminate(true);
		progressDialog.setCanceledOnTouchOutside(false);

		Fragment gridFragment = new DeviceGridFragment();
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.add(R.id.main_content_frame, gridFragment).commit();

		ViewGroup drawerView = (ViewGroup) findViewById(R.id.main_drawer_frame);
		drawerView.addView(getLayoutInflater().inflate(R.layout.settings, null));
	}

	@Override
	protected void onResume() {
		Log.i("DeviceCollectionActivity", "onResume()");
		ActionPerformer actionPerformer = new ServerActionPerformer(this);
		actionPerformer.requestDeviceList(new OnDeviceListReceivedListener() {
			@Override
			public void onDeviceListReceived(List<Device> devices) {
				Log.i("DeviceCollectionActivity", "onResume() -> onDeviceListReceived()");
				if (devices != null && devices.size() > 0) {
					model.setDeviceList(devices);
				} else {
					Toast.makeText(context, "Failed to get device list. Check server settings.", Toast.LENGTH_LONG).show();
				}

				if (progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
			}
		});

		super.onResume();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		drawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Pass the event to ActionBarDrawerToggle, if it returns
		// true, then it has handled the app icon touch event
		if (drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle your other action bar items...
		return super.onOptionsItemSelected(item);
	}
}
