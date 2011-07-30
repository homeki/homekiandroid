package device;

import com.homekey.android.communication.CommandSendingService;

import android.content.Context;
import android.content.Intent;

public class Lamp extends Device {
	enum Status{
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
	
	public void switchOn(Context context){
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
	
}
