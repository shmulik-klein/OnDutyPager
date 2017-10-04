package com.sklein.ubimo_pager;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent smsIntent = getIntent();
        Bundle smsBundle = smsIntent.getExtras();
        if (null != smsBundle) {
            setContentView(R.layout.activity_main);

            final MediaPlayer player = MediaPlayer.create(this, R.raw.demo);
            AudioAttributes attributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build();
            player.setAudioAttributes(attributes);
            player.setLooping(true);
            final ImageButton btnStopAlert = (ImageButton) findViewById(R.id.btn_stop);
            btnStopAlert.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    player.stop();
                    finish();
                }
            });

            player.start();
            TextView txtMessage = (TextView) findViewById(R.id.txt_msg);
            txtMessage.setText(smsBundle.getString("sms_body"));
        }
    }
}
