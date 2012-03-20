package com.homeki.android;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.homeki.android.device.Device;
import com.homeki.android.device.Dimmer;
import com.homeki.android.device.Switch;
import com.homeki.android.device.Thermometer;
import com.homeki.android.tasks.GetDevicesTask;

public class DeviceListActivity extends ListActivity implements OnItemLongClickListener {
	private ArrayAdapter<Device> adapter;
	private LayoutInflater inflater;
	private List<Device> list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflater = getLayoutInflater();
		list = HomekiApplication.getInstance().getList();
		
		adapter = new MyAdapter(this, list);
		setListAdapter(adapter);
		getListView().setOnItemLongClickListener(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		IntentFilter filter = new IntentFilter(getString(R.string.server_not_found_action));
		registerReceiver(serverTimeoutReceiver, filter);
		HomekiApplication.getInstance().registerListWatcher(adapter);
		new GetDevicesTask().execute();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(serverTimeoutReceiver);
		HomekiApplication.getInstance().unregisterListWatcher(adapter);
	}
	
	@Override
	public boolean onItemLongClick(AdapterView<?> l, View v, int position, long id) {
		Device d = adapter.getItem(position);
		if (d instanceof Switch) {
			Intent intent = new Intent(this, SwitchActivity.class);
			intent.putExtra("id", d.getId());
			startActivity(intent);
		}
		return false;
	}
	
	private class MyAdapter extends ArrayAdapter<Device> implements OnCheckedChangeListener, OnSeekBarChangeListener {
		public MyAdapter(Context context, List<Device> objects) {
			super(context, -1, objects);
		}
		
		@Override
		public int getViewTypeCount() {
			return 3;
		}
		
		@Override
		public int getItemViewType(int position) {
			Device dev = getItem(position);
			if (dev.getClass() == Switch.class) {
				return 0;
			} else if (dev.getClass() == Dimmer.class) {
				return 1;
			} else if (dev.getClass() == Thermometer.class) {
				return 2;
			}
			return -1;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			int type = getItemViewType(position);
			Device dev = getItem(position);
			ViewHolder vh;
			
			if (convertView == null) {
				vh = new ViewHolder();
				
				switch (type) {
				case 0:
					convertView = inflater.inflate(R.layout.listitem_switch, null);
					vh.tv = (TextView) convertView.findViewById(R.id.switch_title);
					vh.cb = (CheckBox) convertView.findViewById(R.id.switch_toggle);
					vh.cb.setOnCheckedChangeListener(this);
					break;
				case 1:
					convertView = inflater.inflate(R.layout.listitem_dimmer, null);
					vh.tv = (TextView) convertView.findViewById(R.id.dimmer_title);
					vh.sb = (SeekBar) convertView.findViewById(R.id.dimmer_seekbar);
					vh.sb.setOnSeekBarChangeListener(this);
					
					vh.cb = (CheckBox) convertView.findViewById(R.id.dimmer_toggle);
					vh.cb.setOnCheckedChangeListener(this);
					break;
				case 2:
					convertView = inflater.inflate(R.layout.listitem_thermometer, null);
					vh.tv = (TextView) convertView.findViewById(R.id.thermometer_title);
					break;
				}
				convertView.setTag(vh);
			}
			
			vh = (ViewHolder) convertView.getTag();
			
			switch (type) {
			case 0:
				vh.tv.setText(dev.getName());
				vh.cb.setTag(position);
				vh.cb.setChecked(((Switch) dev).getStatus());
				break;
			case 1:
				Dimmer d = (Dimmer) dev;
				vh.tv.setText(d.toString());
				vh.sb.setTag(position);
				vh.sb.setProgress(d.getLevel());
				vh.cb.setTag(position);
				vh.cb.setChecked(d.getStatus());
				break;
			case 2:
				vh.tv.setText(((Thermometer) dev).getStatus() + "" + (char) 0x00B0);
				break;
			}
			
			return convertView;
		}
		
		class ViewHolder {
			CheckBox cb;
			TextView tv;
			SeekBar sb;
		}
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			int loc = (Integer) buttonView.getTag();
			Switch s = (Switch) list.get(loc);
			
			if (s instanceof Dimmer) {
				Dimmer d = (Dimmer) s;
				d.dim(d.getLevel(), isChecked ? 1 : 0);
			} else {
				if (isChecked) {
					s.switchOn();
				} else {
					s.switchOff();
				}
			}
			
		}
		
		@Override
		public void onProgressChanged(SeekBar sb, int progress, boolean fromUser) {}
		
		@Override
		public void onStartTrackingTouch(SeekBar sb) {}
		
		@Override
		public void onStopTrackingTouch(SeekBar sb) {
			int loc = (Integer) sb.getTag();
			Dimmer d = (Dimmer) list.get(loc);
			d.dim(sb.getProgress(), d.getStatus() ? 1 : 0);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, Menu.FIRST, Menu.NONE, "Edit Prefs").setIcon(android.R.drawable.ic_menu_preferences);
		menu.add(Menu.NONE, 2, Menu.NONE, "Refresh List").setIcon(android.R.drawable.ic_menu_rotate);
		menu.add(Menu.NONE, 3, Menu.NONE, "Timers").setIcon(android.R.drawable.btn_star);
		menu.add(Menu.NONE, 4, Menu.NONE, "broadcast").setIcon(android.R.drawable.btn_star_big_on);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case Menu.FIRST:
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		case 2:
			new GetDevicesTask().execute();
			return true;
		case 3:
			startActivity(new Intent(this, TriggerListActivity.class));
			return true;
		case 4:
			testBroadCast();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	InetAddress getBroadcastAddress() throws IOException {
		WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		DhcpInfo dhcp = wifi.getDhcpInfo();
		// handle null somehow
		
		int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
		byte[] quads = new byte[4];
		for (int k = 0; k < 4; k++)
			quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
		Log.d("LOG", InetAddress.getByAddress(quads) + "");
		return InetAddress.getByAddress(quads);
	}
	
	private void testBroadCast() {
		DatagramSocket socket;
		try {
			socket = new DatagramSocket(53005);
			socket.setBroadcast(true);
			byte[] data = "trolol".getBytes();
			DatagramPacket packet = new DatagramPacket(data, data.length, getBroadcastAddress(), 53005);
			socket.send(packet);
			socket.disconnect();
			socket.close();
			Log.d("LOG", "waiting for reply");
			dialog = ProgressDialog.show(this, "", "Searching for homekiserver...", true, true);
			IntentFilter filter = new IntentFilter("FOUNDSERVER");
			filter.addAction("NOSERVER");
			registerReceiver(serverReceiver, filter);
			startService(new Intent(this, ServerDetectorService.class));
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	ProgressDialog dialog;
	BroadcastReceiver serverReceiver = new BroadcastReceiver() {
		
		public void onReceive(Context context, Intent intent) {
			unregisterReceiver(this);
			dialog.cancel();
			if (intent.getAction().equals("FOUNDSERVER")){
				SettingsHelper.putStringValue(DeviceListActivity.this, "server", intent.getStringExtra("ip") + ":5000");
				new GetDevicesTask().execute();
			} else {
				new AlertDialog.Builder(DeviceListActivity.this).setMessage("Couldnt find any server on local network").setPositiveButton("Jahapp...", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						/* User clicked OK so do some stuff */
					}
				}).show();
			}
		};
	};
	
	BroadcastReceiver serverTimeoutReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			new AlertDialog.Builder(DeviceListActivity.this).setMessage("OMG THE SERVER IS BROKEN?").setPositiveButton("Jahapp...", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					/* User clicked OK so do some stuff */
				}
			}).show();
		}
	};
}
