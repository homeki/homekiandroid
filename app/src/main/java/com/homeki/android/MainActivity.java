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
	private MenuOption selectedMenuItem;
	private View devicesMenu;
	private View actionGroupsMenu;

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

		devicesMenu = findViewById(R.id.devices_menu);
		actionGroupsMenu = findViewById(R.id.action_groups_menu);

		devicesMenu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				drawerLayout.closeDrawers();
				selectedMenuItem = MenuOption.DEVICES;
				updateFragment();
			}
		});

		actionGroupsMenu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				drawerLayout.closeDrawers();
				selectedMenuItem = MenuOption.ACTION_GROUPS;
				updateFragment();
			}
		});

		findViewById(R.id.settings_menu).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				drawerLayout.closeDrawers();
				startActivity(new Intent(MainActivity.this, SettingsActivity.class));
			}
		});

		selectedMenuItem = MenuOption.DEVICES;
		if (savedInstanceState != null && savedInstanceState.containsKey("selectedMenuItem")) selectedMenuItem = MenuOption.valueOf(savedInstanceState.getString("selectedMenuItem"));
		updateFragment();
	}

	private void updateFragment() {
		switch (selectedMenuItem) {
			case ACTION_GROUPS:
				showActionGroups();
				break;
			case DEVICES:
			default:
				showDevices();
				break;
		}
	}

	private void showDevices() {
		devicesMenu.setSelected(true);
		actionGroupsMenu.setSelected(false);
		changeFragment(new DeviceListFragment());
		selectedMenuItem = MenuOption.DEVICES;
	}

	private void showActionGroups() {
		devicesMenu.setSelected(false);
		actionGroupsMenu.setSelected(true);
		changeFragment(new ActionGroupListFragment());
		selectedMenuItem = MenuOption.ACTION_GROUPS;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("selectedMenuItem", selectedMenuItem.toString());
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

	protected enum MenuOption {
		DEVICES,
		ACTION_GROUPS
	}
}
