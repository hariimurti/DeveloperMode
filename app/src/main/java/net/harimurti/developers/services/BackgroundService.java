package net.harimurti.developers.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.annotation.Nullable;

import net.harimurti.developers.methods.ConfigManager;

public class BackgroundService extends Service {
    private static final String TAG = "Developers:KeepScreenOn";
    private static final String KEEP_SCREEN_ON = "KEEP_SCREEN_ON";

    private WakeLock wakeLock;
    ConfigManager config;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    @SuppressWarnings("deprecation")
    public int onStartCommand(Intent intent, int flags, int startId) {
        config = new ConfigManager(this);

        if (config.getBoolean(KEEP_SCREEN_ON)) {
            PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
            wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, TAG);
            wakeLock.acquire(30);
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (wakeLock != null)
            wakeLock.release();
    }
}
