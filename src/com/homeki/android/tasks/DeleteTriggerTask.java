package com.homeki.android.tasks;

import java.io.IOException;

import android.os.AsyncTask;

import com.homeki.android.HomekiApplication;

public class DeleteTriggerTask extends AsyncTask<Void, Void, Void> {
	private final int id;
	
	public DeleteTriggerTask(int id) {
		this.id = id;
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		try {
			HomekiApplication.getInstance().remote().deleteTrigger(id);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
