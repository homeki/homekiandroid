package com.homeki.android.model.devices;

import java.util.HashMap;
import java.util.Map;

public abstract class Device {
	protected DeviceType type;
	protected int id;
	protected String name;
	protected String description;
	protected boolean active;
  protected Map<Integer, ChannelValue> channelsById;
  protected Map<String, ChannelValue> channelsByName;
	private DeviceOwner owner;

	public Device(DeviceType type, int id, String name, String description, boolean active)  {
		this.type = type;
		this.id = id;
		this.name = name;
		this.description = description;
		this.active = active;
    this.channelsById = new HashMap<Integer, ChannelValue>();
    this.channelsByName = new HashMap<String, ChannelValue>();
	}

	public DeviceType getType() {
		return type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public boolean isActive() {
		return active;
	}

	public void setChannelValue(int id, String value) {
		channelsById.get(id).value = value;
		notifyOwnerOfChange();
	}

  public void setChannelValue(String name, String value) {
    channelsByName.get(name).value = value;
    notifyOwnerOfChange();
  }

  public void putChannel(int id, String name, String value) {
    ChannelValue cv = new ChannelValue(id, value);
    channelsById.put(id, cv);
    channelsByName.put(name, cv);
  }

  public int getChannelId(String name) {
    ChannelValue cv = channelsByName.get(name);
    return cv.id;
  }
	
	public String getChannelValue(int id) {
		return channelsById.get(id).value;
	}

  public String getChannelValue(String name) {
    return channelsByName.get(name).value;
  }
	
	public void setOwner(DeviceOwner owner) {
		this.owner = owner;
	}
	
	protected void notifyOwnerOfChange() {
    if (owner == null) return;
    owner.deviceDidChange(this);
	}
	
	public interface DeviceOwner {
		public void deviceDidChange(Device device);
	}

  public class ChannelValue {
    public int id;
    public String value;

    public ChannelValue(int id, String value) {
      this.id = id;
      this.value = value;
    }
  }
}
