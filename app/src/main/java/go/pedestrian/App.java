package go.pedestrian;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class App extends Application {

    private static Context sContext;

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
}
