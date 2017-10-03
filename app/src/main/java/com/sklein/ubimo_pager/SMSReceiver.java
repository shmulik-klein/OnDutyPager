package com.sklein.ubimo_pager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

/**
 * Created by Shmulik Klein
 */

public class SMSReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage[] smsm = null;
        String smsNumber = null;
        String smsBody = null;

        if (null != bundle) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            smsm = new SmsMessage[pdus.length];
            for (int i = 0; i < smsm.length; i++) {
                smsm[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                smsNumber = smsm[i].getOriginatingAddress();
                smsBody = smsm[i].getMessageBody().toString();
            }
        }

        Intent smsIntent = new Intent(context, MainActivity.class);
        smsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        smsIntent.putExtra("sms_number", smsNumber);
        smsIntent.putExtra("sms_body", smsBody);
        context.startActivity(smsIntent);
    }
}
