package com.homeki.android;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.homeki.android.tasks.AddTimerTask;
import com.homeki.android.tasks.SetDeviceTask;
import com.homeki.android.trigger.Trigger;

public class AddTriggerActivity extends Activity implements OnItemSelectedListener, OnClickListener {
	private TextView name;
	private Trigger t;
	private Spinner s;
	private TextView days;
	private TextView repeatType;
	private TextView time;
	private TextView value;
	
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
			new AddTimerTask().setName(name.getText()).setValue(value.getText()).setTime(time.getText()).
			setRepeatType(repeatType.getText()).setDays(days.getText()).execute();
			finish();
			break;
		}
	}
}
