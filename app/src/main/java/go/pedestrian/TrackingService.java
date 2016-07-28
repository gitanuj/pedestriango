package go.pedestrian;

import android.Manifest;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class TrackingService extends Service implements LocationListener {

    private static final String LOG_TAG = "TRACKING_SERVICE";

    private final PendingIntent activityUpdatesPendingIntent = PendingIntent.getService(App.getContext(), 0, new Intent(App.getContext(), ActivityRecognizedService.class), PendingIntent.FLAG_UPDATE_CURRENT);

    private GoogleApiClient mGoogleApiClient;

    public TrackingService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(LOG_TAG, "onCreate");
        connectApiClient();
    }

    private void disconnectApiClient() {
        if (mGoogleApiClient != null) {
            Log.i(LOG_TAG, "Disconnecting GoogleApiClient");
            mGoogleApiClient.disconnect();
            mGoogleApiClient = null;
        }
    }

    private void connectApiClient() {
        Callbacks callbacks = new Callbacks();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(ActivityRecognition.API)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(callbacks)
                .addOnConnectionFailedListener(callbacks)
                .build();
        mGoogleApiClient.connect();
        Log.i(LOG_TAG, "Connecting GoogleApiClient");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "onDestroy");
        stopLocationUpdates();
        stopActivityUpdates();
        disconnectApiClient();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(LOG_TAG, location.toString());
        App.getInstance().setLocation(location);
    }

    private void startActivityUpdates() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(mGoogleApiClient, 3000, activityUpdatesPendingIntent);
        }
    }

    private void stopActivityUpdates() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            ActivityRecognition.ActivityRecognitionApi.removeActivityUpdates(mGoogleApiClient, activityUpdatesPendingIntent);
        }
    }

    private void startLocationUpdates() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationRequest locationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY).setInterval(3000);
            if (ActivityCompat.checkSelfPermission(TrackingService.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(TrackingService.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                return;
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, TrackingService.this);
        }
    }

    private void stopLocationUpdates() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, TrackingService.this);
        }
    }

    private class Callbacks implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

        @Override
        public void onConnected(@Nullable Bundle bundle) {
            Log.i(LOG_TAG, "onConnected");
            startLocationUpdates();
            startActivityUpdates();
        }

        @Override
        public void onConnectionSuspended(int i) {
            Log.i(LOG_TAG, "onConnectionSuspended");
            stopLocationUpdates();
            stopActivityUpdates();
        }

        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            Log.i(LOG_TAG, "onConnectionFailed");
            stopLocationUpdates();
            stopActivityUpdates();
        }
    }
}
