package com.homeki.android.device;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.homeki.android.R;
import com.homeki.android.tasks.Dim;

public class Dimmer extends Switch {
	int level;
	
	public Dimmer() {
		super();
		level = -1;
	}	
	public Dimmer(JsonDevice d) {
		super(d);
	}
	
	public void dim(Context context, int level) {
		this.level = level;
		new Dim(context, id, level).execute();
//		Intent intent = new Intent(context, CommandSendingService.class);
//		intent.setAction(CommandSendingService.dim);
//		intent.putExtra("level", level);
//		intent.putExtra("id", id);
//		context.startService(intent);
		
	}
	
	public boolean getStatus() {
		return Status.ON == status;
	}
	
	@Override
	public View getView(Context context) {
		View v = LayoutInflater.from(context).inflate(R.layout.dimmer, null);
		setUpView(v);
		return v;
	}
	
	@Override
	protected void setUpView(View v) {
		super.setUpView(v);
		SeekBar sb = (SeekBar) v.findViewById(R.id.dim_bar);
		sb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				dim(seekBar.getContext(), seekBar.getProgress());
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			}
		});
	}
}
