package net.harimurti.developers.methods;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class ConfigManager {
    private SharedPreferences preferences;
    private SharedPreferences.Editor prefEditor;

    public ConfigManager(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean getBoolean(String key) {
        return preferences.getBoolean(key, false);
    }

    public int getInteger(String key) {
        return preferences.getInt(key, 0);
    }

    public void setBoolean(String key, boolean value) {
        prefEditor = preferences.edit();
        prefEditor.putBoolean(key, value);
        prefEditor.apply();
    }

    public void setInteger(String key, int value) {
        prefEditor = preferences.edit();
        prefEditor.putInt(key, value);
        prefEditor.apply();
    }
}
