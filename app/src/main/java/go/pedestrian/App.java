package go.pedestrian;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.SQLException;
import android.location.Location;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.location.DetectedActivity;

import java.io.IOException;
import java.util.LinkedList;

public class App extends Application {

    private static final int BUFFER_SIZE = 10;

    private static final App INSTANCE = new App();

    private static Context sContext;

    private DetectedActivity detectedActivity;

    private LinkedList<DetectedActivity> activities = new LinkedList<>();

    private Location location;

    private LinkedList<Location> locations = new LinkedList<>();

    // might want to refactor where these go, like as a service or whatever
    private static DataBaseHelper HazardDB;

    private BroadcastReceiver screenUpdatesReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Intent.ACTION_SCREEN_ON:
                    break;
                case Intent.ACTION_SCREEN_OFF:
                    stopService();
                    break;
                case Intent.ACTION_USER_PRESENT:
                    startService();
                    break;
            }
        }
    };

    public static App getInstance() {
        return INSTANCE;
    }

    private void startService() {
        startService(new Intent(this, TrackingService.class));
    }

    private void stopService() {
        stopService(new Intent(this, TrackingService.class));
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
        registerReceiver(screenUpdatesReceiver, filter);
        initDB();
    }

    private void initDB() {
        HazardDB = new DataBaseHelper(sContext);
        try {
            HazardDB.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        try {
            HazardDB.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }
    }

    public static Context getContext() {
        return sContext;
    }

    public DetectedActivity getDetectedActivity() {
        return detectedActivity;
    }

    public void setDetectedActivity(DetectedActivity detectedActivity) {
        this.detectedActivity = detectedActivity;

        if (detectedActivity.getType() == DetectedActivity.ON_FOOT && checkForHazardsNearby()) {
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(getContext())
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("Stay alert")
                            .setContentText("Hazard found nearby")
                            .setDefaults(Notification.DEFAULT_VIBRATE)
                            .setPriority(NotificationCompat.PRIORITY_HIGH);

            NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(NOTIFICATION_SERVICE);
            notificationManager.cancel(1);
            notificationManager.notify(1, builder.build());
        }

        if (detectedActivity != null) {
            add(activities, detectedActivity);
            Intent intent = new Intent("activity");
            intent.putExtra("data", detectedActivity);
            LocalBroadcastManager.getInstance(sContext).sendBroadcast(intent);
        }
    }

    private boolean checkForHazardsNearby() {
        Location latest = null;
        if (locations.size() > 0) {
            latest = locations.getLast();
        }
        if (latest != null) {
            boolean hazardsNearby = HazardDB.nearHazard(latest.getLatitude(), latest.getLongitude(), latest.getAccuracy());
            if (hazardsNearby) {
                Log.i("App", "Hazards found nearby " + location);
            } else {
                Log.i("App", "No Hazards found nearby " + location);
            }
            return hazardsNearby;
        }
        return false;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;

        if (location != null) {
            add(locations, location);
            Intent intent = new Intent("location");
            intent.putExtra("data", location);
            LocalBroadcastManager.getInstance(sContext).sendBroadcast(intent);
        }
    }

    private <T> void add(LinkedList<T> list, T element) {
        if (list.size() == BUFFER_SIZE) {
            list.pop();
        }
        list.push(element);
    }
}
