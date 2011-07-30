package com.homekey.android.tasks;

import java.io.IOException;

import android.content.Context;
import android.os.AsyncTask;

import com.homekey.android.commands.Commands;

public class SwitchOff extends AsyncTask<Void, Void, String> {

	private final Context c;
	private int id;
	
	public SwitchOff(Context c, int id){
		this.c = c;
		this.id = id;
	}
	
	@Override
	protected String doInBackground(Void... params) {
		
		String s = "";
		try {
			s = Commands.switchOff(c, id);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return s;
	}
}
