package net.harimurti.developers.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import net.harimurti.developers.methods.BatteryStatus;
import net.harimurti.developers.services.PreService;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        BatteryStatus bs = new BatteryStatus(context);
        if (bs.getPlugged().contains("USB")) {
            PreService.Start(context);
        }
    }
}
