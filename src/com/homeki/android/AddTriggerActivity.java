package com.homeki.android;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.homeki.android.tasks.EditTimerTask;
import com.homeki.android.trigger.TimerTrigger;

public class AddTriggerActivity extends Activity implements OnItemSelectedListener, OnClickListener, OnTimeSetListener {
	private TextView name;
	private TimerTrigger t;
	private Spinner s1;
	private Spinner repeatType;
	private TextView time;
	private TextView value;
	private int id;
	private ArrayAdapter<CharSequence> adapter;
	private ArrayAdapter<CharSequence> adapter2;
	private LinearLayout v;
	private CheckBox[] daysBoxes;
	private Spinner s2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_trigger);
		
		this.name = (TextView) findViewById(R.id.timer_name);
		this.value = (TextView) findViewById(R.id.timer_value);
		this.time = (TextView) findViewById(R.id.timer_time);
		this.repeatType = (Spinner) findViewById(R.id.timer_repeat_type);
		
		s1 = (Spinner) findViewById(R.id.trigger_type);
		adapter = ArrayAdapter.createFromResource(this, R.array.trigger_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		s1.setAdapter(adapter);
		s1.setOnItemSelectedListener(this);
		
		s2 = (Spinner) findViewById(R.id.timer_repeat_type);
		adapter2 = ArrayAdapter.createFromResource(this, R.array.repeat_array, android.R.layout.simple_spinner_item);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		s2.setAdapter(adapter2);
		s2.setOnItemSelectedListener(this);
		
		LayoutInflater inflater =  (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		String[] text = new String[]{"M", "T", "W", "T", "F", "S", "S"};
		daysBoxes = new CheckBox[7];
		v = (LinearLayout) findViewById(R.id.timer_days_stuff);
		for (int i = 0; i < 7; i++){
			View ll = inflater.inflate(R.layout.text_checkbox, null);
			TextView tv = (TextView) ll.findViewById(R.id.text);
			tv.setText(text[i]);
			CheckBox ch = (CheckBox) ll.findViewById(R.id.checkbox);
			daysBoxes[i] = ch;
			v.addView(ll);
		}
		
		id = getIntent().getIntExtra("id", -1);
		if (id != -1) {
			t = (TimerTrigger) HomekiApplication.getInstance().getTrigger(id);
			name.setText(t.getName());
			value.setText(String.valueOf(t.getNewValue()));
			time.setText(getTime(t.getTime()));
			setDays(t.getDays());
			s2.setSelection(t.getRepeatType());
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
		if (arg0.getAdapter() == adapter){
			CharSequence seleted = (CharSequence) arg0.getItemAtPosition(position);
			if (seleted.equals("Timer")) {
				findViewById(R.id.timer_stuff).setVisibility(View.VISIBLE);
			} else {
				findViewById(R.id.timer_stuff).setVisibility(View.GONE);
			}
		} else if (arg0.getAdapter() == adapter2){
			if (position == 0) {
				findViewById(R.id.timer_days_stuff).setVisibility(View.GONE);
			} else {
				findViewById(R.id.timer_days_stuff).setVisibility(View.VISIBLE);
			}
		}
		
	}
	
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}
	
	@Override
	public void onClick(View v) {
		EditTimerTask ett;
		switch (v.getId()) {
		case R.id.undo_button:
			finish();
			break;
		case R.id.done_button:
			ett = new EditTimerTask().setName(name.getText()).setValue(value.getText()).setTime(getTime(time.getText())).
			setRepeatType(repeatType.getSelectedItemId()).setDays(getDays()).setId(id);
			
			ett.execute();
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
			intent.putExtra("triggerId", id);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private int getDays(){
		int value = 0;
		for (int i = 0; i < daysBoxes.length; i++){
			if (daysBoxes[i].isChecked()){
				value |= (1 << i);
			}
		}
		return value;
	}
	
	private void setDays(int days) {
		for (int i = 0; i < daysBoxes.length; i++){
			if ((days >> i & 1) == 1){
				daysBoxes[i].setChecked(true);
			}
		}
	}
}
