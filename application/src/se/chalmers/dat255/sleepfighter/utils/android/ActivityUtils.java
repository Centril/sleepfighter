package se.chalmers.dat255.sleepfighter.utils.android;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;

public class ActivityUtils {
	
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	public static void setupStandardActionBar(Activity activity) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
		activity.getActionBar().setHomeButtonEnabled(true);
	}
	
	private ActivityUtils() {
	}
}
