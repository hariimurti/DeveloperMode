package net.harimurti.developers;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import net.harimurti.developers.methods.ConfigManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String KEEP_SCREEN_ON = "KEEP_SCREEN_ON";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ConfigManager config = new ConfigManager(this);

        Switch switch1 = findViewById(R.id.switch1);
        switch1.setChecked(config.getBoolean(KEEP_SCREEN_ON));
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                config.setBoolean(KEEP_SCREEN_ON, isChecked);
            }
        });
    }

    public void onClick(View view) {
        if (view.getId() == R.id.author) {
            Intent openWebsite = new Intent(Intent.ACTION_VIEW);
            openWebsite.setData(Uri.parse("http://harimurti.net"));
            startActivity(openWebsite);
        }
    }
}
