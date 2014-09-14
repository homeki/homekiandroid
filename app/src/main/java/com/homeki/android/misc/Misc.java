package com.homeki.android.misc;

public class Misc {
	public static void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException ignore) { }
	}
}
