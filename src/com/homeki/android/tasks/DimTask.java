package com.homeki.android.tasks;

import java.io.IOException;

import android.os.AsyncTask;

import com.homeki.android.HomekiApplication;

public class DimTask extends AsyncTask<Void, Void, String> {
	private int id;
	private int level;
	
	public DimTask(int id, int level) {
		this.id = id;
		this.level = level;
	}
	
	@Override
	protected String doInBackground(Void... params) {
		String s = "";
		try {
			s = HomekiApplication.getInstance().remote().dim(id, level);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return s;
	}
}
