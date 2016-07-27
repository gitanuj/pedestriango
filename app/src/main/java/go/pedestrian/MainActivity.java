package go.pedestrian;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;

public class MainActivity extends AppCompatActivity {

    private SwitchCompat switchCompat;

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
    }

    @Override
    protected void onResume() {
        super.onResume();

        bindSwitch(SharedPrefs.getInstance().getBoolean(SharedPrefs.KEY_ON_OFF, false));
    }

    private void bindSwitch(boolean on) {
        switchCompat.setChecked(on);
    }
}
