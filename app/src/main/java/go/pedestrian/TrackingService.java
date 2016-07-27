package go.pedestrian;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class TrackingService extends Service {

    public TrackingService() {
    }

    @Override
    public void onCreate() {
        Log.i("Service", "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
