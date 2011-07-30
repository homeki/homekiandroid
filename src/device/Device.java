package device;

import java.util.Date;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public abstract class Device {
	public int id;
	public String name;
	public Date added;
	public boolean active;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public abstract View getView(Context context);
	
}
