package go.pedestrian;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.location.DetectedActivity;

public class App extends Application {

    private static final App INSTANCE = new App();

    private static Context sContext;

    private DetectedActivity detectedActivity;

    private Location location;

    public static App getInstance() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        sContext = this;

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        registerReceiver(new ScreenReceiver(), filter);

        startService(new Intent(this, TrackingService.class));
    }

    public static Context getContext() {
        return sContext;
    }

    public DetectedActivity getDetectedActivity() {
        return detectedActivity;
    }

    public void setDetectedActivity(DetectedActivity detectedActivity) {
        this.detectedActivity = detectedActivity;

        if (detectedActivity != null) {
            Intent intent = new Intent("activity");
            intent.putExtra("data", detectedActivity);
            LocalBroadcastManager.getInstance(sContext).sendBroadcast(intent);
        }
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;

        if (location != null) {
            Intent intent = new Intent("location");
            intent.putExtra("data", location);
            LocalBroadcastManager.getInstance(sContext).sendBroadcast(intent);
        }
    }
}
