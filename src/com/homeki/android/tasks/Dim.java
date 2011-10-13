package com.homeki.android.tasks;

import java.io.IOException;

import android.content.Context;
import android.os.AsyncTask;

import com.homeki.android.commands.Commands;

public class Dim extends AsyncTask<Void, Void, String> {

	private final Context c;
	private int id;
	private int level;
	
	public Dim(Context c, int id, int level){
		this.c = c;
		this.id = id;
		this.level = level;
	}
	
	@Override
	protected String doInBackground(Void... params) {
		
		String s = "";
		try {
			s = Commands.dim(c, id, level);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return s;
	}
}
