package com.homeki.android;

import java.util.List;

import com.homeki.android.model.DeviceListModel;
import com.homeki.android.model.devices.AbstractDevice;
import com.homeki.android.server.ActionPerformer;
import com.homeki.android.server.ActionPerformer.OnDeviceListReceivedListener;
import com.homeki.android.server.ServerActionPerformer;
import com.homeki.android.view.devicelist.AbstractDeviceListItemView;

import android.app.ListFragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

public class DeviceListFragment extends ListFragment implements OnDeviceListReceivedListener {
	private ActionPerformer mActionPerformer;

	private ProgressDialog mProgressDialog;
	private DeviceListModel mModel;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mModel = DeviceListModel.getModel();
		mActionPerformer = new ServerActionPerformer(getActivity());

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
	public void onDeviceListReceived(List<AbstractDevice> devices) {
		AbstractDeviceListItemView.resetUnNamedTypeCount();
		
		if (devices != null && devices.size() > 0) {
			mModel.setDeviceList(devices);			
		} else {
			Toast.makeText(getActivity(), "Failed to get device list. Check server settings.", Toast.LENGTH_LONG).show();
		}
		mProgressDialog.dismiss();
	}
}
