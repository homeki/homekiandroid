package com.homeki.android;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.widget.Toast;
import com.homeki.android.server.ApiClient;

import java.util.List;

public class DeviceListFragment extends ListFragment {
	private static final String TAG = DeviceListFragment.class.getSimpleName();

	private ApiClient apiClient;
	private DeviceListAdapter listAdapter;
	private ProgressDialog progressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		apiClient = new ApiClient(getActivity());
		listAdapter = new DeviceListAdapter(getActivity(), apiClient);

		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setIndeterminate(true);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage("Loading...");
	}

	@Override
	public void onResume() {
		super.onResume();

		progressDialog.show();

		new AsyncTask<Void, Void, List<ApiClient.JsonDevice>>() {
			@Override
			protected List<ApiClient.JsonDevice> doInBackground(Void... params) {
				try {
					return apiClient.getDevices();
				} catch (Exception e) {
					Log.e(TAG, "Failed to get devices.", e);
					return null;
				}
			}

			@Override
			protected void onPostExecute(List<ApiClient.JsonDevice> jsonDevices) {
				progressDialog.dismiss();

				if (jsonDevices == null) {
					Toast.makeText(getActivity(), "Failed to get device list. Check server settings.", Toast.LENGTH_LONG).show();
					return;
				}

				listAdapter.setDevices(jsonDevices);
			}
		}.execute();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setListAdapter(listAdapter);
	}
}
