package com.sklein.ubimo_pager;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private final int NOTIFICATION_ID = 19980419;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        Intent smsIntent = getIntent();
        Bundle smsBundle = smsIntent.getExtras();
        if (null != smsBundle) {
            setContentView(R.layout.activity_main);

            SharedPreferences sharedPref = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
            boolean disabled = sharedPref.getBoolean(getString(R.string.disabled), false);

            final AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            // TODO: handle isVolumeFixed == true
            final int currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
            audio.setStreamVolume(AudioManager.STREAM_MUSIC,
                    audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
            // TODO: increase volume until max every sec

            NotificationManager notificationManager = (NotificationManager) this.getSystemService(this.NOTIFICATION_SERVICE);
            NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(this);
            nBuilder.setContentTitle("Enter Duty alarm");
            nBuilder.setSmallIcon(R.drawable.logo);
            nBuilder.setOngoing(true);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, smsIntent, 0);
            nBuilder.setContentIntent(pendingIntent);
            notificationManager.notify(NOTIFICATION_ID, nBuilder.build());

            final Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            final MediaPlayer player = MediaPlayer.create(this, R.raw.demo);

            final ImageButton btnStopAlert = (ImageButton) findViewById(R.id.btn_stop);
            btnStopAlert.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    player.stop();
                    vib.cancel();

                    audio.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);

                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancel(NOTIFICATION_ID);
                    finish();
                }
            });

            if (disabled) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vib.vibrate(VibrationEffect.createWaveform(new long[]{400, 400, 400}, 3));
                }else {
                    vib.vibrate(1200);
                }
            } else {
                AudioAttributes attributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build();
                player.setAudioAttributes(attributes);
                player.setLooping(true);
                player.start();
            }

            TextView txtMessage = (TextView) findViewById(R.id.txt_msg);
            txtMessage.setText(smsBundle.getString("sms_body"));
        }
    }
}
