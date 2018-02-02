package com.sklein.ubimo_pager;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;

/**
 * Created by Shmulik Klein
 */

public class SMSReceiver extends BroadcastReceiver {
    private static final String CABOT_NUMBER = "13476479587";

    @Override
    public void onReceive(Context context, Intent intent) {
        SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        String smsNumber = messages[0].getOriginatingAddress();
        String smsBody = messages[0].getMessageBody();

        // ignores SMSs which were sent outside of thw whitelist
        if (!smsNumber.equalsIgnoreCase(CABOT_NUMBER) && !smsNumber.equalsIgnoreCase("+" + CABOT_NUMBER)) {
            return;
        }

        Intent smsIntent = new Intent(context, MainActivity.class);
        smsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        smsIntent.putExtra("sms_number", smsNumber);
        smsIntent.putExtra("sms_body", smsBody);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(context);
        nBuilder.setContentTitle("DUTY CALLS");
        nBuilder.setSmallIcon(R.drawable.logo);
        nBuilder.setOngoing(true);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, smsIntent, 0);
        nBuilder.setContentIntent(pendingIntent);

        notificationManager.notify(19980419, nBuilder.build());
        context.startActivity(smsIntent);
    }
}
