package org.riderun.app;

import android.app.Application;
import android.content.Context;

/**
 * Main purpose of this class is to make the application context statically available
 */
public class RideRunApplication extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        RideRunApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return RideRunApplication.context;
    }
}
