package net.harimurti.developers.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import net.harimurti.developers.methods.BatteryStatus;
import net.harimurti.developers.services.PreService;

public class PowerReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        BatteryStatus pm = new BatteryStatus(context);

        if (action.equals(Intent.ACTION_POWER_CONNECTED)) {
            if (pm.getPlugged().contains("USB")) {
                PreService.Start(context);
            }
        } else if (action.equals(Intent.ACTION_POWER_DISCONNECTED)) {
            PreService.Stop(context);
        }
    }
}
