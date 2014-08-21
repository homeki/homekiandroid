package com.homeki.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends ActionBarActivity {
	private DrawerLayout drawerLayout;
	private ActionBarDrawerToggle drawerToggle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		drawerLayout = (DrawerLayout) findViewById(R.id.main_layout);
		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, R.string.app_name, R.string.app_name);
		drawerLayout.setDrawerListener(drawerToggle);
		drawerToggle.setDrawerIndicatorEnabled(true);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);

		findViewById(R.id.devices_menu).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				drawerLayout.closeDrawers();
				v.setSelected(true);
				changeFragment(new DeviceListFragment());
			}
		});

		findViewById(R.id.settings_menu).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				drawerLayout.closeDrawers();
				startActivity(new Intent(MainActivity.this, SettingsActivity.class));
			}
		});

		findViewById(R.id.devices_menu).setSelected(true);
		changeFragment(new DeviceListFragment());
	}

	private void changeFragment(Fragment fragment) {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.main_frame, fragment, "Fragment");
		transaction.commit();
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
