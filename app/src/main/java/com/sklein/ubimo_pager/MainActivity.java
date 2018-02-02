package com.sklein.ubimo_pager;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        Intent smsIntent = getIntent();
        Bundle smsBundle = smsIntent.getExtras();
        if (null != smsBundle) {
            setContentView(R.layout.activity_main);

            final AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            // TODO: handle isVolumeFixed == true
            final int currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
            audio.setStreamVolume(AudioManager.STREAM_MUSIC,
                    audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
            // TODO: increase volume until max every sec

            final MediaPlayer player = MediaPlayer.create(this, R.raw.demo);
            AudioAttributes attributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build();
            player.setAudioAttributes(attributes);
            player.setLooping(true);

            final ImageButton btnStopAlert = (ImageButton) findViewById(R.id.btn_stop);
            btnStopAlert.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    player.stop();
                    audio.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);

                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancel(19980419);
                    finish();
                }
            });

            player.start();
            TextView txtMessage = (TextView) findViewById(R.id.txt_msg);
            txtMessage.setText(smsBundle.getString("sms_body"));
        }
    }
}
