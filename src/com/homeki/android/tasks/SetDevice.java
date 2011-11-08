package com.homeki.android.tasks;

import java.io.IOException;

import android.content.Context;
import android.os.AsyncTask;

import com.homeki.android.commands.Commands;

public class SetDevice extends AsyncTask<Void, Void, String> {
	private final Context c;
	private int id;
	private String name;
	
	public SetDevice(Context c, int id, String name){
		this.c = c;
		this.id = id;
		this.name = name;
	}
	
	@Override
	protected String doInBackground(Void... params) {
		String s = "";
		
		try {
			s = Commands.setName(c, id, name);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return s;
	}
}
