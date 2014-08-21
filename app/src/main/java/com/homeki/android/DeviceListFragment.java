package com.homeki.android;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.ListFragment;

import android.widget.Toast;
import com.homeki.android.model.DeviceListModel;
import com.homeki.android.model.devices.Device;
import com.homeki.android.server.ActionPerformer;
import com.homeki.android.server.ServerActionPerformer;

import java.util.List;

public class DeviceListFragment extends ListFragment {
	private ActionPerformer actionPerformer;
	private DeviceListModel model;
	private DeviceListAdapter listAdapter;
	private ProgressDialog progressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		model = DeviceListModel.getModel();
		actionPerformer = new ServerActionPerformer(getActivity());
		listAdapter = new DeviceListAdapter(getActivity(), model, actionPerformer);

		model = DeviceListModel.getModel();

		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setIndeterminate(true);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage("Loading...");
	}

	@Override
	public void onResume() {
		progressDialog.show();

		ActionPerformer actionPerformer = new ServerActionPerformer(getActivity());
		actionPerformer.requestDeviceList(new ActionPerformer.OnDeviceListReceivedListener() {
			@Override
			public void onDeviceListReceived(List<Device> devices) {
				if (devices != null && devices.size() > 0) {
					model.setDeviceList(devices);
				} else {
					Toast.makeText(getActivity(), "Failed to get device list. Check server settings.", Toast.LENGTH_LONG).show();
				}

				progressDialog.dismiss();
			}
		});

		super.onResume();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setListAdapter(listAdapter);
	}
}
