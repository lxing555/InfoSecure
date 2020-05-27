package com.example.infosecure;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import com.example.infosecure.entity.Infos;
import com.example.infosecure.entity.Message;
import com.example.infosecure.fragment.MessageFragment;

import java.text.SimpleDateFormat;
import java.util.Date;

public  class ReceiveSmsReceiver extends BroadcastReceiver {
    private static int mark=0;
    @Override
    public void onReceive(Context context, Intent intent){
        Object[] objects=(Object[])intent.getExtras().get("pdus");
        for(Object obj:objects){
            byte[] data=(byte[])obj;
            SmsMessage sms=SmsMessage.createFromPdu(data);
            String body=sms.getDisplayMessageBody();
            String num=sms.getDisplayOriginatingAddress();
            String date=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(sms.getTimestampMillis()));
            int type=1;
            Message mes=new Message(num,date,body,type);
            Infos.insert_Mess(mes);
            MessageFragment.mesRecycleAdapter.notifyDataSetChanged();
            abortBroadcast();
        }
    }
}
