package com.homeki.android.view.devicegrid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.homeki.android.R;
import com.homeki.android.model.devices.DimmerDevice;
import com.homeki.android.server.ActionPerformer;

public class DeviceGridItemDimmerView extends AbstractDeviceGridView<DimmerDevice> {
	private SeekBar valueBar;
	private SeekBarChangedListener seekBarChangedListener;

	private TextView nameView;
	private ImageView indicatorView;

	private boolean isOn;

	public DeviceGridItemDimmerView(Context context, ActionPerformer actionPerformer) {
		super(context, actionPerformer);

		isOn = false;
		seekBarChangedListener = new SeekBarChangedListener();
	}

	@Override
	protected void inflate(LayoutInflater layoutInflater) {
		layoutInflater.inflate(R.layout.device_grid_dimmer, this);
		valueBar = (SeekBar) findViewById(R.id.device_grid_dimmer_value_bar);
		valueBar.setOnSeekBarChangeListener(seekBarChangedListener);

		nameView = (TextView) findViewById(R.id.switch_text);
		indicatorView = (ImageView) findViewById(R.id.switch_indicator_view);

	}

	@Override
	protected void onDeviceSet(DimmerDevice device) {
		valueBar.setOnSeekBarChangeListener(null);
		valueBar.setProgress(device.getLevel());
		valueBar.setOnSeekBarChangeListener(seekBarChangedListener);

		nameView.setText(device.getName());

		isOn = device.getValue();

		setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isOn = !isOn;
				DimmerDevice device = (DimmerDevice) DeviceGridItemDimmerView.this.device;
				device.setValue(isOn);
				setChannelValue("Switch", isOn ? 1 : 0);

				updateView();
			}
		});

		updateView();
	}

	private void updateView() {
		if (isOn) {
			indicatorView.setImageResource(R.drawable.switch_indicator_on);
			this.setBackgroundResource(R.drawable.switch_background_on);
		} else {
			indicatorView.setImageResource(R.drawable.switch_indicator_off);
			this.setBackgroundResource(R.drawable.switch_background_off);
		}
	}

	private class SeekBarChangedListener implements OnSeekBarChangeListener {
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
		}

		@Override
		public void onStopTrackingTouch(final SeekBar seekBar) {
			DimmerDevice device = (DimmerDevice) DeviceGridItemDimmerView.this.device;
			device.setLevel(seekBar.getProgress());

			setChannelValue("Level", seekBar.getProgress());
		}
	}
}
