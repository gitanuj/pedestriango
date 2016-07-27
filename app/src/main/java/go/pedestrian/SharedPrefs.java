package go.pedestrian;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs {

    private static final String STORE = "store";

    public static final String KEY_ON_OFF = "on_off";

    private static final SharedPrefs INSTANCE = new SharedPrefs();

    public static SharedPrefs getInstance() {
        return INSTANCE;
    }

    public void putBoolean(String key, boolean value) {
        SharedPreferences sharedPreferences = App.getContext().getSharedPreferences(STORE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        SharedPreferences sharedPreferences = App.getContext().getSharedPreferences(STORE, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public void putString(String key, String value) {
        SharedPreferences sharedPreferences = App.getContext().getSharedPreferences(STORE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key, String defaultValue) {
        SharedPreferences sharedPreferences = App.getContext().getSharedPreferences(STORE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, defaultValue);
    }
}
