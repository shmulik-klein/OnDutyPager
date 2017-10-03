package com.sklein.ubimo_pager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;

/**
 * Created by Shmulik Klein
 */

public class SMSReceiver extends BroadcastReceiver {
    private static final String CABOT_NUMBER = "+13476479587";

    @Override
    public void onReceive(Context context, Intent intent) {
        SmsMessage[] messages;
        String smsNumber = null;
        String smsBody = null;

        messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        smsNumber = messages[0].getOriginatingAddress();
        smsBody = messages[0].getMessageBody();

        // ignores SMSs which were sent outside of thw whitelist
        if (!smsNumber.equalsIgnoreCase(CABOT_NUMBER)) {
            return;
        }

        Intent smsIntent = new Intent(context, MainActivity.class);
        smsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        smsIntent.putExtra("sms_number", smsNumber);
        smsIntent.putExtra("sms_body", smsBody);
        context.startActivity(smsIntent);
    }
}
