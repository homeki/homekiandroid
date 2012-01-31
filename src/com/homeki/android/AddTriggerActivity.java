package com.homeki.android;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.homeki.android.trigger.Trigger;

public class AddTriggerActivity extends Activity implements OnItemSelectedListener {
	private TextView name;
	private Trigger t;
	private Spinner s;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_trigger);
		
		this.name = (TextView) findViewById(R.id.devicename);
		s = (Spinner) findViewById(R.id.trigger_type);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.trigger_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		s.setAdapter(adapter);
		s.setOnItemSelectedListener(this);
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
}
