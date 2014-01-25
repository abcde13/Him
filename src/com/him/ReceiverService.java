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
	   
	  if((MainActivity.receiving) == 0){
	      Toast.makeText(this, "He is now monitoring your texts", Toast.LENGTH_LONG).show();
	      
	      TextMessageReceiver mSmsReceiver = new TextMessageReceiver();
	      IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
	      filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
	      this.registerReceiver(mSmsReceiver, filter);
	      MainActivity.receiving = 1;
	  } else {
		  Toast.makeText(this, "He is already monitoring your texts", Toast.LENGTH_LONG).show();
	  }
      
      return START_STICKY;
      
      
   }
   @Override
   public void onDestroy() {
      super.onDestroy();
      Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
   }
}