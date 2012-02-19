package com.homeki.android;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.homeki.android.tasks.EditTimerTask;
import com.homeki.android.trigger.TimerTrigger;

public class AddTriggerActivity extends Activity implements OnItemSelectedListener, OnClickListener, OnTimeSetListener {
	private TextView name;
	private TimerTrigger t;
	private Spinner s;
	private TextView days;
	private TextView repeatType;
	private TextView time;
	private TextView value;
	private int id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_trigger);
		
		this.name = (TextView) findViewById(R.id.timer_name);
		this.value = (TextView) findViewById(R.id.timer_value);
		this.time = (TextView) findViewById(R.id.timer_time);
		this.repeatType = (TextView) findViewById(R.id.timer_repeat_type);
		this.days = (TextView) findViewById(R.id.timer_days);
		
		s = (Spinner) findViewById(R.id.trigger_type);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.trigger_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		s.setAdapter(adapter);
		s.setOnItemSelectedListener(this);
		
		id = getIntent().getIntExtra("id", -1);
		if (id != -1) {
			t = (TimerTrigger) HomekiApplication.getInstance().getTrigger(id);
			name.setText(t.getName());
			value.setText(String.valueOf(t.getNewValue()));
			time.setText(getTime(t.getTime()));
			repeatType.setText(String.valueOf(t.getRepeatType()));
			days.setText(String.valueOf(t.getDays()));
		} else {
			time.setText("00:00");
		}
		time.setFocusable(false);
		time.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TimePickerDialog tp = new TimePickerDialog(AddTriggerActivity.this, AddTriggerActivity.this, getTime(time.getText()) / 3600, getTime(time.getText()) % 3600 / 60, true);
				tp.show();
			}
		});
		
		
		findViewById(R.id.undo_button).setOnClickListener(this);
		findViewById(R.id.done_button).setOnClickListener(this);
	}
	
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
		CharSequence seleted = (CharSequence) arg0.getItemAtPosition(position);
		if (seleted.equals("Timer")) {
			findViewById(R.id.timer_stuff).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.timer_stuff).setVisibility(View.GONE);
		}
	}
	
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.undo_button:
			finish();
			break;
		case R.id.done_button:
			new EditTimerTask().setName(name.getText()).setValue(value.getText()).setTime(getTime(time.getText())).
			setRepeatType(repeatType.getText()).setDays(days.getText()).setId(id).execute();
			finish();
			break;
		}
	}

	public int getTime(CharSequence s){
		String[] strings = s.toString().split(":");
		int hours = Integer.valueOf(strings[0]);
		int minutes = Integer.valueOf(strings[1]);
		return hours * 3600 + minutes * 60;
	}
	
	private String getTime(int value) {
		return String.format("%02d:%02d", value / 3600, (value % 3600) / 60);
	}
	
	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		time.setText(String.format("%02d:%02d", hourOfDay, minute));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (id != -1) {
			menu.add(Menu.NONE, Menu.FIRST, Menu.NONE, "Link Devices").setIcon(android.R.drawable.ic_menu_preferences);
		}
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case Menu.FIRST:
			Intent intent = new Intent(this, LinkTriggerDeviceActivity.class);
			intent.putExtra("id", id);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
