package device;

import com.homekey.android.R;
import com.homekey.android.communication.CommandSendingService;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class Lamp extends Device {
	enum Status {
		ON, OFF, LIMBO
	}
	
	Status status;
	
	public Lamp() {
		status = Status.LIMBO;
	}
	
	public Lamp(JsonDevice d) {
		this();
		active = d.active;
		added = d.added;
		id = d.id;
		name = d.name;
	}
	
	public void switchOn(Context context) {
		status = Status.ON;
		Intent intent = new Intent(context, CommandSendingService.class);
		intent.setAction(CommandSendingService.turnLampOn);
		intent.putExtra("id", 1);
		context.startService(intent);
	}
	
	public void switchOff(Context context) {
		status = Status.OFF;
		Intent intent = new Intent(context, CommandSendingService.class);
		intent.setAction(CommandSendingService.turnLampOff);
		intent.putExtra("id", 1);
		context.startService(intent);
	}
	
	public boolean getStatus() {
		return Status.ON == status;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + " " + id;
	}
	
	@Override
	public View getView(Context context) {
		View v = LayoutInflater.from(context).inflate(R.layout.lamp, null);
		TextView deviceName = (TextView) v.findViewById(R.id.lamp_devicename);
		TextView deviceDesc = (TextView) v.findViewById(R.id.lamp_devicedesc);
		TextView deviceTemp = (TextView) v.findViewById(R.id.lamp_temp);
		
		deviceTemp.setText(String.format("The lamp is %s.", getStatus() ? "on" : "off"));
		
		deviceName.setText(name);
		deviceDesc.setText("Temperature");
		
		return v;
	}
	
}
