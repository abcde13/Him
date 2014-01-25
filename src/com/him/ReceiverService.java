package com.him;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.widget.Toast;

public class ReceiverService extends Service {
   @Override
   public IBinder onBind(Intent arg0) {
      return null;
   }

   @Override
   public int onStartCommand(Intent intent, int flags, int startId) {
      // Let it continue running until it is stopped.
      Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
      
      /*TextMessageReceiver mSmsReceiver = new TextMessageReceiver();
      IntentFilter filter = new IntentFilter();
      filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
      filter.addAction(SMS_RECEIVE_ACTION); // SMS
      filter.addAction(WAP_PUSH_RECEIVED_ACTION); // MMS
      this.registerReceiver(mSmsReceiver, filter);*/
      
      return START_STICKY;
      
      
   }
   @Override
   public void onDestroy() {
      super.onDestroy();
      Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
   }
}