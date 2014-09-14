package com.homeki.android.view;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.homeki.android.R;
import com.homeki.android.server.ApiClient;

public class ActionGroupListItem extends LinearLayout {
	private static final String TAG = ActionGroupListItem.class.getSimpleName();

	public ActionGroupListItem(final Context context, final ApiClient apiClient, final ApiClient.JsonActionGroup jsonActionGroup) {
		super(context);

		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(R.layout.action_group_list_item, this);
		TextView nameView = (TextView) findViewById(R.id.action_group_list_item_name);
		nameView.setText(jsonActionGroup.name);
		nameView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new AsyncTask<Void, Void, Boolean>() {
					@Override
					protected Boolean doInBackground(Void... params) {
						try {
							apiClient.triggerActionGroup(jsonActionGroup.actionGroupId);
							return true;
						} catch (Exception e) {
							Log.e(TAG, "Failed to trigger action group " + jsonActionGroup.name + ".", e);
							return false;
						}
					}

					@Override
					protected void onPostExecute(Boolean succeeded) {
						if (succeeded) {
							Toast.makeText(context, "Action group triggered successfully.", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(context, "Failed to trigger action group.", Toast.LENGTH_LONG).show();
						}
					}
				}.execute();
			}
		});
	}
}
