package com.homeki.android.tasks;

import java.io.IOException;

import android.content.Context;
import android.os.AsyncTask;

import com.homeki.android.commands.Commands;

public class SwitchOn extends AsyncTask<Void, Void, String> {

	private final Context c;
	private int id;
	
	public SwitchOn(Context c, int id){
		this.c = c;
		this.id = id;
	}
	
	@Override
	protected String doInBackground(Void... params) {
		
		String s = "";
		try {
			s = Commands.switchOn(c, id);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return s;
	}
}
