package device;

import java.util.Date;

public class JsonDevice {
	public String type;
	public int id;
	public String name;
	public Date added;
	public boolean active;
	
	@Override
	public String toString() {
		return type;
	}
}
