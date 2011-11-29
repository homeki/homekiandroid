package com.homeki.android.tasks;

import java.io.IOException;

import android.os.AsyncTask;

import com.homeki.android.HomekiApplication;

public class SwitchOn extends AsyncTask<Void, Void, String> {
	private int id;
	
	public SwitchOn(int id){
		this.id = id;
	}
	
	@Override
	protected String doInBackground(Void... params) {
		String s = "";
		try {
			s = HomekiApplication.getInstance().remote().switchOn(id);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return s;
	}
}
