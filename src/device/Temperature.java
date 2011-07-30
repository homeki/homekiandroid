package device;

import com.homekey.android.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class Temperature extends Device {
	float value;
	
	public Temperature() {
		value = -1f;
	}
	
	public Temperature(JsonDevice d) {
		this();
		active = d.active;	
		added = d.added;
		id = d.id;
		name = d.name;
	}
	
	public float getStatus() {
		return value;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + " " + id;
	}

	@Override
	public View getView(Context context) {
		View v = LayoutInflater.from(context).inflate(R.layout.temp, null);
		TextView deviceName = (TextView)v.findViewById(R.id.inspdev_devicename);
		TextView deviceDesc = (TextView)v.findViewById(R.id.inspdev_devicedesc);
		TextView deviceTemp = (TextView)v.findViewById(R.id.inspdev_temp);
		
		deviceTemp.setText("The temperature here is " + value + " degrees.");
		
		deviceName.setText(name);
		deviceDesc.setText("Temperature");
		
		return v;
	}
}
