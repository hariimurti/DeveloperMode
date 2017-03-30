package net.harimurti.developers.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.annotation.Nullable;
import android.util.Log;

import net.harimurti.developers.methods.BatteryStatus;
import net.harimurti.developers.methods.Manager;
import net.harimurti.developers.methods.ConfigManager;

public class BackgroundService extends Service {
    private static final String TAG = "BackgroundService";
    private static final String CHARGING_SERVICE = "CHARGING_SERVICE";
    private static final String KEEP_SCREEN_ON = "KEEP_SCREEN_ON";
    private static final String LEVEL = "LEVEL";
    private static final String FULL_STOP = "FULL_STOP";

    private WakeLock wakeLock;
    private int lastLevel = -1;
    private int logId = -1;
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
            wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, KEEP_SCREEN_ON);
            wakeLock.acquire();
        }

        if (config.getBoolean(CHARGING_SERVICE) && Manager.isSupported()) {
            IntentFilter percentage = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            registerReceiver(charging, percentage);
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (wakeLock != null)
            wakeLock.release();

        Manager.setChargingEnabled(true);
        unregisterReceiver(charging);
    }

    BroadcastReceiver charging = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            BatteryStatus Battery = new BatteryStatus(context);

            if (lastLevel != Battery.getLevel()) {
                lastLevel = Battery.getLevel();
                Log.i(TAG, "power: " + Battery.getPlugged().toLowerCase() +
                        ", level: " + Integer.toString(Battery.getLevel()) + "%");

                if (config.getBoolean(CHARGING_SERVICE) && Battery.getPlugged().contains("USB")) {
                    if (!config.getBoolean(FULL_STOP)) {
                        if (Battery.getLevel() >= config.getInteger(LEVEL)) {
                            if (logId != 1) {
                                Log.i(TAG, "condition: reach level maximum = not allowed to charging");
                                logId = 1;
                            }
                            if (Manager.isChargingEnabled()) {
                                Manager.setChargingEnabled(false);
                            }
                        } else {
                            if (logId != 0) {
                                Log.i(TAG, "condition: under level maximum = allowed to charging");
                                logId = 0;
                            }
                            if (!Manager.isChargingEnabled()) {
                                Manager.setChargingEnabled(true);
                            }
                        }
                    } else {
                        if (Manager.isChargingEnabled()) {
                            Log.i(TAG, "condition: usb source = not allowed to charging");
                            Manager.setChargingEnabled(false);
                        }
                    }
                }
            }

            if (!config.getBoolean(CHARGING_SERVICE)) {
                if (config.getBoolean(KEEP_SCREEN_ON)) {
                    Log.i(TAG, "stop receiving battery level, because rules charging is disable");
                    unregisterReceiver(charging);
                } else {
                    Log.i(TAG, "kill service, because no rules is set");
                    stopSelf();
                }
            }
        }
    };
}
