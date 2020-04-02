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
    public void onReceive(Context context, Intent intent){///会重复调用两次
//        Log.i("broadcast","广播启动2");
//        System.out.println("SMSReceiver, isOrderdeBroadcast()="+isOrderedBroadcast());
        Object[] objects=(Object[])intent.getExtras().get("pdus");
        for(Object obj:objects){
            byte[] data=(byte[])obj;
            SmsMessage sms=SmsMessage.createFromPdu(data);
            String body=sms.getDisplayMessageBody();
            String num=sms.getDisplayOriginatingAddress();
            String date=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(sms.getTimestampMillis()));
//            Log.d("之前数组长度","Mes"+Infos.messageList.size());
            if(mark==0){
                Infos.insert_Mess(num,date,body);
                MessageFragment.mesRecycleAdapter.notifyDataSetChanged();
                mark=1;
            }
            else mark=0;
//            Log.d("之后数组长度","Mes"+Infos.messageList.size());
//            Log.d("test","拦截成功");
//            Log.i("TTT","number:"+num);
//            Log.i("TTT","body:"+body);
            abortBroadcast();
        }
    }
}
