package com.homeki.android;

import android.app.Activity;
import android.content.Intent;
import android.media.audiofx.BassBoost.Settings;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

public class MainActivity extends Activity {

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			startActivity(new Intent(this, SettingsActivity.class));
			break;
		case R.id.menu_reload:
			Toast.makeText(this, "Menu item 2 selected", Toast.LENGTH_SHORT).show();
			break;

		default:
			break;
		}

		return true;
	}
}
