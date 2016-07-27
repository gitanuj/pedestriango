package go.pedestrian;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.android.gms.location.DetectedActivity;

public class MainActivity extends AppCompatActivity {

    private SwitchCompat switchCompat;

    private TextView locationTv;

    private TextView activityTv;

    private BroadcastReceiver locationUpdatesReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location location = (Location) intent.getParcelableExtra("data");

            if (locationTv != null) {
                locationTv.setText(location.toString());
            }
        }
    };

    private BroadcastReceiver activityUpdatesReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            DetectedActivity activity = (DetectedActivity) intent.getParcelableExtra("data");

            if (activityTv != null) {
                activityTv.setText(activity.toString());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        switchCompat = (SwitchCompat) findViewById(R.id.serviceswitch);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                SharedPrefs.getInstance().putBoolean(SharedPrefs.KEY_ON_OFF, checked);
            }
        });

        locationTv = (TextView) findViewById(R.id.locationtv);
        activityTv = (TextView) findViewById(R.id.activitytv);
    }

    @Override
    protected void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(locationUpdatesReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(activityUpdatesReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();

        bindSwitch(SharedPrefs.getInstance().getBoolean(SharedPrefs.KEY_ON_OFF, false));
        LocalBroadcastManager.getInstance(this).registerReceiver(locationUpdatesReceiver, new IntentFilter("location"));
        LocalBroadcastManager.getInstance(this).registerReceiver(activityUpdatesReceiver, new IntentFilter("activity"));
    }

    private void bindSwitch(boolean on) {
        switchCompat.setChecked(on);
    }
}
