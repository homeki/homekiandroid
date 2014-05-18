package com.homeki.android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.homeki.android.model.DeviceListModel;
import com.homeki.android.model.devices.Device;
import com.homeki.android.server.ActionPerformer;
import com.homeki.android.server.ActionPerformer.OnDeviceListReceivedListener;
import com.homeki.android.server.ServerActionPerformer;

import java.util.List;

public class DeviceCollectionActivity extends FragmentActivity {
	private static String PREFERENCES = "com.homeki.android.ui.preferences";
	private static String PREFFERED_PAGE = "com.homeki.android.ui.preferences.preffered_page";
	
	private DeviceFragmentsPagerAdapter pagerAdapter;
	private ViewPager viewPager;
	private ProgressDialog progressDialog;
	private DeviceListModel model;
	private Context context;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		context = this;
		
		model = DeviceListModel.getModel(this);

		progressDialog = new ProgressDialog(context);
		progressDialog.setIndeterminate(true);
		progressDialog.setCanceledOnTouchOutside(false);
		
		final SharedPreferences prefs = getSharedPreferences(PREFERENCES, 0);
		
		pagerAdapter = new DeviceFragmentsPagerAdapter(getSupportFragmentManager());
		viewPager = (ViewPager) findViewById(R.id.main_pager);
		viewPager.setAdapter(pagerAdapter);
		viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
      @Override
      public void onPageSelected(int position) {
        viewPager.getAdapter().notifyDataSetChanged();
        Editor editor = prefs.edit();
        editor.putInt(PREFFERED_PAGE, position);
        editor.commit();
      }
    });
		
		int prefferedPage = prefs.getInt(PREFFERED_PAGE, 0);
		viewPager.setCurrentItem(prefferedPage);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
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
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			startActivity(new Intent(this, SettingsActivity.class));
			break;
		default:
			break;
		}

		return true;
	}

	public class DeviceFragmentsPagerAdapter extends FragmentPagerAdapter {
		private static final int LIST_INDEX = 0;
		private static final int GRID_INDEX = 1;

		public DeviceFragmentsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int index) {
			switch (index) {
			case LIST_INDEX:
				return new DeviceListFragment();
			case GRID_INDEX:
				return new DeviceGridFragment();
			}
			return null;
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		@Override
		public int getCount() {
			return 2;
		}
	}
}
