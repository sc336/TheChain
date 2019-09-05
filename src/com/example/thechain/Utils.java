package com.example.thechain;

import android.util.Log;

public final class Utils {
	private Utils() {
		throw new RuntimeException("Why are you trying to instantiate the Utils class?  How did you even get here?");
	}
	
	public static boolean isClass(Object x, Class c) {
		//Log.i("Utils.isClass", "Is " + x.toString() + " a " + c.toString() + "?");
		//Log.i("Utils.isClass", c.isAssignableFrom(x.getClass())? "Yes":"No, it's a " + x.getClass().toString());
		return c.isAssignableFrom(x.getClass());
	}
}
