package com.example.infosecure;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class MessageService extends Service {
    private ReceiveSmsReceiver receiver;
    @Override
    public void onCreate(){
        super.onCreate();
        receiver=new ReceiveSmsReceiver();
        IntentFilter filter=new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        this.registerReceiver(receiver,filter);
        Log.i("broadcast","广播启动1");
        Log.d("MyService",	"onCreate executed");
    }
    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        Log.d("MyService",	"onStartCommand executed");
        return super.onStartCommand(intent,flags,startId);
    }
    public MessageService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy(){
        this.unregisterReceiver(receiver);
        Log.d("MyService","onDestroy executed");
        super.onDestroy();
    }
}
