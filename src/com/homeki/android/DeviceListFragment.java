package com.homeki.android;

import android.os.Bundle;
import android.support.v4.app.ListFragment;

import com.homeki.android.model.DeviceListModel;
import com.homeki.android.server.ActionPerformer;
import com.homeki.android.server.ServerActionPerformer;

public class DeviceListFragment extends ListFragment {

	private ActionPerformer mActionPerformer;
	private DeviceListModel mModel;
	private DeviceListAdapter mListAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mModel = DeviceListModel.getModel(getActivity());
		mActionPerformer = new ServerActionPerformer(getActivity());
		mListAdapter = new DeviceListAdapter(getActivity(), mModel, mActionPerformer);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setListAdapter(mListAdapter);
	}
}
