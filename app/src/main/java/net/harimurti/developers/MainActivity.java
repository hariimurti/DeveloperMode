package net.harimurti.developers;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.stericson.RootShell.RootShell;

import net.harimurti.developers.methods.ConfigManager;
import net.harimurti.developers.services.PreService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String CHARGING_SERVICE = "CHARGING_SERVICE";
    private static final String KEEP_SCREEN_ON = "KEEP_SCREEN_ON";
    private static final String LEVEL = "LEVEL";
    private static final String FULL_STOP = "FULL_STOP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean isRooted = RootShell.isAccessGiven();

        final ConfigManager config = new ConfigManager(this);

        Switch switch1 = (Switch) findViewById(R.id.switch1);
        switch1.setChecked(config.getBoolean(KEEP_SCREEN_ON));
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                config.setBoolean(KEEP_SCREEN_ON, isChecked);
            }
        });

        Switch switch2 = (Switch) findViewById(R.id.switch2);
        switch2.setChecked(config.getBoolean(CHARGING_SERVICE));
        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                config.setBoolean(CHARGING_SERVICE, isChecked);
            }
        });

        final TextView label_usb = (TextView) findViewById(R.id.label_usb);
        if (!config.getBoolean(FULL_STOP))
            label_usb.setText(customLabel(config.getInteger(LEVEL)));
        else
            label_usb.setText(getString(R.string.label_stop_usb));

        final SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setMax(90 - 10);
        seekBar.setProgress(config.getInteger(LEVEL) - 10);
        seekBar.setEnabled(!config.getBoolean(FULL_STOP));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = 10 + progress;
                label_usb.setText(customLabel(progress));
                config.setInteger(LEVEL, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        Switch switch3 = (Switch) findViewById(R.id.switch3);
        switch3.setChecked(!config.getBoolean(FULL_STOP));
        switch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                config.setBoolean(FULL_STOP, !isChecked);
                seekBar.setEnabled(isChecked);
                if (!isChecked)
                    label_usb.setText(getString(R.string.label_stop_usb));
                else
                    label_usb.setText(customLabel(config.getInteger(LEVEL)));
            }
        });

        FloatingActionButton fabStart = (FloatingActionButton) findViewById(R.id.fabStart);
        fabStart.setOnClickListener(this);
        fabStart.setEnabled(isRooted);

        FloatingActionButton fabStop = (FloatingActionButton) findViewById(R.id.fabStop);
        fabStop.setOnClickListener(this);
        fabStop.setEnabled(isRooted);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.fabStart) {
            PreService.Start(this);
        }
        if (view.getId() == R.id.fabStop) {
            if (!PreService.Stop(this)) {
                Toast.makeText(this, R.string.toast_notstop, Toast.LENGTH_SHORT).show();
            }
        }
        if (view.getId() == R.id.author) {
            Intent openWebsite = new Intent(Intent.ACTION_VIEW);
            openWebsite.setData(Uri.parse("http://harimurti.net"));
            startActivity(openWebsite);
        }
    }

    private String customLabel(int level) {
        return String.format(getString(R.string.label_custom_level), level) + "%";
    }
}
