package com.anchor.erp.myfuelapp.BroadcastRecievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.anchor.erp.myfuelapp.Fragments.VerifyPhone;
import com.anchor.erp.myfuelapp.Utils.Config;

import static android.content.ContentValues.TAG;

public class SmsListener extends BroadcastReceiver {

    private Config.SmsListener listener;

    public SmsListener() {
        listener = new VerifyPhone();
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e(TAG, "onReceive: THE BROADCASTLISTENER HAS RECIEVED AN SMS" );

        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){

            Bundle bundle = intent.getExtras();
            try{

                Object[] pdus = (Object[]) bundle.get("pdus");
                String[] messages = new String[pdus.length];
                SmsMessage[] smsMessages = new SmsMessage[pdus.length];
                for (int i = 0;i < smsMessages.length; i++){

                    smsMessages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    messages[i] = smsMessages[i].getMessageBody();

                }

                listener.messagesRecieved(messages);

            }catch (Exception e){
                e.printStackTrace();
            }

        }

    }

}
