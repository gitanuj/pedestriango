package go.pedestrian;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ScreenReceiver extends BroadcastReceiver {
    public ScreenReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        switch (action) {
            case Intent.ACTION_SCREEN_ON:
                break;
            case Intent.ACTION_SCREEN_OFF:
                break;
            case Intent.ACTION_USER_PRESENT:
                break;
        }

        Log.d("SCREEN_RECEIVER", action);
    }
}
