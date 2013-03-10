package com.homeki.android;

import java.util.List;

import com.homeki.android.model.DeviceListModel;
import com.homeki.android.model.DeviceListProvider;
import com.homeki.android.model.devices.AbstractDevice;
import com.homeki.android.server.ActionPerformer;
import com.homeki.android.server.ActionPerformer.OnDeviceListReceivedListener;
import com.homeki.android.server.ServerActionPerformer;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

public class DeviceGridFragment extends Fragment {

	private DeviceListModel mModel;
	private ActionPerformer mActionPerformer;
	private GridView mGridView;

	private Context mContext;

	private DeviceGridAdapter mGridAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mContext = getActivity();

		mModel = DeviceListModel.getModel(mContext);
		mActionPerformer = new ServerActionPerformer(mContext);

		mGridAdapter = new DeviceGridAdapter(mContext, mModel, mActionPerformer);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.devices_grid, container, false);

		mGridView = (GridView) view.findViewById(R.id.devices_grid_view);
		mGridView.setAdapter(mGridAdapter);

		return view;
	}
}
