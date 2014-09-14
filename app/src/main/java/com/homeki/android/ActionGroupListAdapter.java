package com.homeki.android;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.homeki.android.server.ApiClient;
import com.homeki.android.view.ActionGroupListItem;

import java.util.ArrayList;
import java.util.List;

public class ActionGroupListAdapter extends BaseAdapter {
	private final Context context;
	private final ApiClient apiClient;
	private List<ApiClient.JsonActionGroup> jsonActionGroups;

	public ActionGroupListAdapter(Context context, ApiClient apiClient) {
		this.context = context;
		this.apiClient = apiClient;
		this.jsonActionGroups = new ArrayList<>();
	}

	public void setActionGroups(List<ApiClient.JsonActionGroup> jsonActionGroups) {
		this.jsonActionGroups = jsonActionGroups;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return jsonActionGroups.size();
	}

	@Override
	public Object getItem(int position) {
		return jsonActionGroups.get(position);
	}

	@Override
	public long getItemId(int position) {
		return jsonActionGroups.get(position).actionGroupId;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		ApiClient.JsonActionGroup jsonActionGroup = jsonActionGroups.get(pos);
		return new ActionGroupListItem(context, apiClient, jsonActionGroup);
	}
}
