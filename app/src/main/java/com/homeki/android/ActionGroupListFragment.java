package com.homeki.android;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.widget.Toast;
import com.homeki.android.server.ApiClient;

import java.util.List;

public class ActionGroupListFragment extends ListFragment {
	private static final String TAG = ActionGroupListFragment.class.getSimpleName();

	private ApiClient apiClient;
	private ActionGroupListAdapter listAdapter;
	private ProgressDialog progressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		apiClient = new ApiClient(getActivity());
		listAdapter = new ActionGroupListAdapter(getActivity(), apiClient);

		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setIndeterminate(true);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage("Loading...");
	}

	@Override
	public void onResume() {
		super.onResume();

		progressDialog.show();

		new AsyncTask<Void, Void, List<ApiClient.JsonActionGroup>>() {
			@Override
			protected List<ApiClient.JsonActionGroup> doInBackground(Void... params) {
				try {
					return apiClient.getActionGroups();
				} catch (Exception e) {
					Log.e(TAG, "Failed to get devices.", e);
					return null;
				}
			}

			@Override
			protected void onPostExecute(List<ApiClient.JsonActionGroup> jsonActionGroups) {
				progressDialog.dismiss();

				if (jsonActionGroups == null) {
					Toast.makeText(getActivity(), "Failed to get action groups. Check server settings.", Toast.LENGTH_LONG).show();
					return;
				}

				listAdapter.setActionGroups(jsonActionGroups);
			}
		}.execute();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setListAdapter(listAdapter);
	}
}
