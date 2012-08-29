package com.homeki.android;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.homeki.android.model.DeviceListModel;
import com.homeki.android.model.devices.AbstractDevice;

public class DeviceDetailsActivity extends Activity {

	public static final String EXTRA_DEVICE_ID = "deviceId";

	private static final String FRAGMENT_TAG_SETTINGS = "settings";
	private static final String FRAGMENT_TAG_STATISTICS = "statistics";

	private AbstractDevice mDevice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();
		if (extras != null && extras.containsKey(EXTRA_DEVICE_ID)) {
			mDevice = DeviceListModel.getModel(this).getDeviceWithId(extras.getInt(EXTRA_DEVICE_ID));
		}

		if (mDevice == null) {
			Toast toast = Toast.makeText(this, "No device id provided", Toast.LENGTH_SHORT);
			toast.show();
			finish();
		}

		setContentView(R.layout.details_activity);

		addTabs();

		if (savedInstanceState != null) {
			resetFragments();
		}
	}

	/**
	 * When fragments are recreated by the system (e.g. on orientation changes) they need to have their device set again.
	 */
	private void resetFragments() {
		Fragment detailsFragment = getFragmentManager().findFragmentByTag(FRAGMENT_TAG_SETTINGS);
		if (detailsFragment != null) {
			((DeviceSettingsFragment) detailsFragment).setDevice(mDevice);
		}
	}

	private void addTabs() {
		TabClickedListener tabListener = new TabClickedListener();

		ActionBar actionBar = getActionBar();
		actionBar.setTitle(mDevice.getName());
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		Tab tabStats = actionBar.newTab();
		tabStats.setText("Statistics");
		tabStats.setTag(FRAGMENT_TAG_STATISTICS);
		tabStats.setTabListener(tabListener);

		Tab tabSettings = actionBar.newTab();
		tabSettings.setText("Settings");
		tabSettings.setTag(FRAGMENT_TAG_SETTINGS);
		tabSettings.setTabListener(tabListener);

		actionBar.addTab(tabStats);
		actionBar.addTab(tabSettings);
	}

	private class TabClickedListener implements TabListener {

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {

		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			if (tab.getTag().equals(FRAGMENT_TAG_SETTINGS)) {
				DeviceSettingsFragment deviceFragment = new DeviceSettingsFragment();
				deviceFragment.setDevice(mDevice);
				ft.replace(R.id.device_details_root, deviceFragment, FRAGMENT_TAG_SETTINGS);
			} else {
				DeviceStatisticsFragment deviceFragment = new DeviceStatisticsFragment();
				ft.replace(R.id.device_details_root, deviceFragment, FRAGMENT_TAG_STATISTICS);
			}
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {

		}

	}
}
