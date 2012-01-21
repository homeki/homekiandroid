package com.homeki.android.tasks;

import java.io.IOException;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.homeki.android.HomekiApplication;
import com.homeki.android.communication.json.JsonHistory;
import com.homeki.android.device.Device;

public class GetHistoryTask extends AsyncTask<Void, Void, List<JsonHistory>> {
	private Device d;
	private long end;
	private long start;
	private final ImageView iv;
	
	public GetHistoryTask(Device d, long start, long end, ImageView iv) {
		this.d = d;
		this.start = start;
		this.end = end;
		this.iv = iv;
	}
	
	@Override
	protected List<JsonHistory> doInBackground(Void... params) {
		String s = "";
		
		try {
			s = HomekiApplication.getInstance().remote().getHistory(d.getId(), start, end + 1000 * 3600 * 24);
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<JsonHistory> history = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().fromJson(s, new TypeToken<List<JsonHistory>>() {}.getType());
		return history;
	}
	
	class Pixel {
		int x, y;
	}
	
	@Override
	protected void onPostExecute(List<JsonHistory> historyList) {
		
		Bitmap bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bitmap);
		
		Paint paint = new Paint();
		paint.setColor(Color.GREEN);
		
		Pixel last = new Pixel();
		last.x = 0;
		last.y = 0;
		
		for (JsonHistory jh : historyList) {
			Log.d("LOG", "percentage: " + ((jh.registered.getTime() - start) / (double) (end - start)));
			int x = (int) (200 * ((jh.registered.getTime() - start) / (double) (end - start)));
			c.drawLine(last.x, last.y, x, last.y, paint);
			if (jh.value) {
				c.drawLine(x, last.y, x, 50, paint);
				last.y = 50;
			} else {
				c.drawLine(x, last.y, x, 150, paint);
				last.y = 150;
			}
			last.x = x;
		}
		c.drawLine(last.x, last.y, 200, last.y, paint);
		
		iv.setImageBitmap(bitmap);
	}
}
