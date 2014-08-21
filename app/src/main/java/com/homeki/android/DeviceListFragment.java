package com.homeki.android;

import android.os.Bundle;
import android.support.v4.app.ListFragment;

import com.homeki.android.model.DeviceListModel;
import com.homeki.android.server.ActionPerformer;
import com.homeki.android.server.ServerActionPerformer;

public class DeviceListFragment extends ListFragment {
	private ActionPerformer actionPerformer;
	private DeviceListModel model;
	private DeviceListAdapter listAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		model = DeviceListModel.getModel();
		actionPerformer = new ServerActionPerformer(getActivity());
		listAdapter = new DeviceListAdapter(getActivity(), model, actionPerformer);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setListAdapter(listAdapter);
	}
}
