package device;

import android.content.Context;

public class Dimmer extends Lamp {
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
		
	}
	
	public boolean getStatus() {
		return Status.ON == status;
	}
}
