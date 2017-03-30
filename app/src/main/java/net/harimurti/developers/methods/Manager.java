package net.harimurti.developers.methods;

import android.util.Log;

import com.stericson.RootShell.RootShell;
import com.stericson.RootShell.execution.Command;
import com.stericson.RootShell.exceptions.RootDeniedException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeoutException;

public class Manager {
    private static final String TAG = "ChargingManager";
    private static String pathFile = "/sys/class/power_supply/battery/charging_enabled";

    public static boolean isSupported() {
        return new File(pathFile).isFile();
    }

    public static boolean isChargingEnabled() {
        String retval = "";

        try {
            FileInputStream inputStream = new FileInputStream(new File(pathFile));

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String receiveString;

            while ( (receiveString = bufferedReader.readLine()) != null ) {
                stringBuilder.append(receiveString);
            }

            inputStream.close();
            retval = stringBuilder.toString();
        }
        catch (FileNotFoundException e) {
            Log.e(TAG, "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e(TAG, "Can't read file: " + e.toString());
        }

        return retval.contains("1");
    }

    public static void setChargingEnabled(Boolean enabled) {
        if (isChargingEnabled() != enabled) {
            RootCommand("echo " + (enabled ? "1" : "0") + " > " + pathFile);
            Log.i(TAG, "set charging " + enabled.toString());
        }
    }

    private static void RootCommand(String cmd) {
        Command command = new Command(0, cmd);
        try {
            RootShell.getShell(true).add(command);
        } catch (TimeoutException | RootDeniedException | IOException e) {
            Log.e(TAG, "RootShell: " + e.getMessage());
        }
    }
}
