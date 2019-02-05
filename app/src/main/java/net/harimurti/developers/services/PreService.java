package net.harimurti.developers.services;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import net.harimurti.developers.methods.ConfigManager;
import net.harimurti.developers.R;

public class PreService {
    private static final String KEEP_SCREEN_ON = "KEEP_SCREEN_ON";
    private static final Class SERVICES = BackgroundService.class;

    public static void Start(Context context) {
        ConfigManager cm = new ConfigManager(context);
        if (cm.getBoolean(KEEP_SCREEN_ON)) {
            if (!isServiceRunning(context, SERVICES)) {
                Intent background = new Intent(context, SERVICES);
                context.startService(background);
                Toast.makeText(context, R.string.toast_service, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, R.string.toast_already, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static boolean Stop(Context context) {
        if (isServiceRunning(context, SERVICES)) {
            Intent background = new Intent(context, SERVICES);
            context.stopService(background);
            Toast.makeText(context, R.string.toast_stop, Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }
    }

    private static boolean isServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
