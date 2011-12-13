package com.homeki.android;

import com.homeki.android.device.Device;
import com.homeki.android.tasks.SetDevice;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class EditSwitchActivity extends Activity implements OnClickListener {
	private int id;
	private TextView name;
	private Device d;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_switch);
		
		findViewById(R.id.undo_button).setOnClickListener(this);
		findViewById(R.id.done_button).setOnClickListener(this);
		this.name = (TextView) findViewById(R.id.edit_name);
		
		this.id = getIntent().getIntExtra("id", -1);
		this.d = HomekiApplication.getInstance().getDevice(id);
		name.setText(d.getName());
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.undo_button:
			finish();
			break;
		case R.id.done_button:
			d.setName(name.getText().toString());
			new SetDevice(id).setName(name.getText().toString()).execute();
			finish();
			break;
		}
	}
}
