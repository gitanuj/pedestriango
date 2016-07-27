package go.pedestrian;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

public class ActivityRecognizedService extends IntentService {
    public ActivityRecognizedService() {
        super("ActivityRecognizedService");
    }

    public ActivityRecognizedService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            handleDetectedActivities(result.getMostProbableActivity());
        }
    }

    private void handleDetectedActivities(DetectedActivity activity) {
        switch (activity.getType()) {
            case DetectedActivity.ON_FOOT:
                break;
            case DetectedActivity.ON_BICYCLE:
                break;
            case DetectedActivity.STILL:
                break;
            case DetectedActivity.IN_VEHICLE:
                break;
        }

        Log.d("ACTIVITY_REC_SERV", activity.toString());

        App.getInstance().setDetectedActivity(activity);
    }
}
