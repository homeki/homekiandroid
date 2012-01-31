package com.homeki.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.homeki.android.tasks.DeleteTriggerTask;
import com.homeki.android.trigger.Trigger;

public class TimerTriggerActivity extends Activity {
	private int id;
	private ImageView iv;
	private TextView name;
	private Trigger t;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_switch);
		
		this.iv = (ImageView) findViewById(R.id.switch_graph);
		this.name = (TextView) findViewById(R.id.devicename);
		
		this.id = getIntent().getIntExtra("id", -1);
	}
	
	@Override
	protected void onResume() {
		iv.setImageDrawable(getResources().getDrawable(R.drawable.icon));
		t = HomekiApplication.getInstance().getTrigger(id);
//		long oneWeek = 1000 * 3600 * 24 * 7L;
//		long twoDays = 1000 * 3600 * 24 * 2L;
//		long now = System.currentTimeMillis();
//		new GetHistoryTask(d, now - twoDays, now, iv).execute();
//		name.setText(d.getName());
		super.onResume();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		menu.add(Menu.NONE, Menu.FIRST, Menu.NONE, "Edit Device").setIcon(android.R.drawable.ic_menu_edit);
		menu.add(Menu.NONE, 2, Menu.NONE, "Delete trigger").setIcon(android.R.drawable.ic_menu_edit);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case Menu.FIRST:
			Intent intent = new Intent(this, EditSwitchActivity.class);
			intent.putExtra("id", id);
			startActivity(intent);
			return true;
		case 2:
			new DeleteTriggerTask(id).execute();
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
