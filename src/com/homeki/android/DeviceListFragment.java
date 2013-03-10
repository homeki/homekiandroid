package com.homeki.android;

import java.util.List;

import com.homeki.android.model.DeviceListModel;
import com.homeki.android.model.devices.AbstractDevice;
import com.homeki.android.server.ActionPerformer;
import com.homeki.android.server.ActionPerformer.OnDeviceListReceivedListener;
import com.homeki.android.server.ServerActionPerformer;
import android.support.v4.app.ListFragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

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
