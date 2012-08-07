package com.homeki.android;

import java.util.List;

import com.homeki.android.model.DeviceListModel;
import com.homeki.android.model.devices.AbstractDevice;
import com.homeki.android.server.ActionPerformer;
import com.homeki.android.server.ActionPerformer.OnDeviceListReceivedListener;
import com.homeki.android.server.ServerActionPerformer;

import android.app.ListFragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.test.mock.MockApplication;

public class DeviceListFragment extends ListFragment implements OnDeviceListReceivedListener {
	private ActionPerformer mActionPerformer;
	
	private ProgressDialog mProgressDialog;
	private DeviceListModel mModel;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mModel = DeviceListModel.getModel();
		mActionPerformer = new ServerActionPerformer();
		
		mProgressDialog = new ProgressDialog(getActivity());
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setCanceledOnTouchOutside(false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);		
		
		mActionPerformer.setOnDeviceListReceivedListener(this);
		setListAdapter(new DeviceListAdapter(getActivity(), mModel, mActionPerformer));
	}
	
	@Override
	public void onResume() {
		super.onResume();		

		mProgressDialog.setMessage("Loading devices");
		mProgressDialog.show();
		
		mActionPerformer.requestDeviceList();	
	}

	@Override
	public void onDeviceListReceived(List<AbstractDevice<?>> devices) {
		mModel.setDeviceList(devices);
		mProgressDialog.dismiss();
	}
}
