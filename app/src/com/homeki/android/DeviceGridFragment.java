package com.homeki.android;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.homeki.android.model.DeviceListModel;
import com.homeki.android.server.ActionPerformer;
import com.homeki.android.server.ServerActionPerformer;

public class DeviceGridFragment extends Fragment {
	private DeviceListModel model;
	private ActionPerformer actionPerformer;
	private GridView gridView;
	private Context context;
	private DeviceGridAdapter gridAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = getActivity();

		model = DeviceListModel.getModel(context);
		actionPerformer = new ServerActionPerformer(context);

		gridAdapter = new DeviceGridAdapter(context, model, actionPerformer);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.devices_grid, container, false);

		gridView = (GridView) view.findViewById(R.id.devices_grid_view);
		gridView.setAdapter(gridAdapter);

		return view;
	}
}
