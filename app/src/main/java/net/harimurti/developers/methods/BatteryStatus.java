package net.harimurti.developers.methods;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

public class BatteryStatus {
    private String Plugged;
    private int Level;

    public BatteryStatus(Context context) {
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, filter);

        Level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        switch (batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)) {
            case BatteryManager.BATTERY_PLUGGED_USB:
                Plugged = "USB Source";
                break;
            case BatteryManager.BATTERY_PLUGGED_AC:
                Plugged = "AC Charger";
                break;
            case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                Plugged = "Wireless Charger";
                break;
            default:
                Plugged = "Unknown Source";
                break;
        }
    }

    public String getPlugged() {
        return Plugged;
    }

    public int getLevel() {
        return Level;
    }
}
