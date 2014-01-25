package com.him;


import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

public class TextMessageReceiver extends BroadcastReceiver{
	
	public void onReceive(Context context, Intent intent)
	{
		Bundle bundle=intent.getExtras();
		
		Object[] messages=(Object[])bundle.get("pdus");
		SmsMessage[] sms=new SmsMessage[messages.length];
		
		for(int n=0;n<messages.length;n++){
			sms[n]=SmsMessage.createFromPdu((byte[]) messages[n]);
		}
			
		for(final SmsMessage msg:sms){
			/*MainActivity.updateMessageBox("\nFrom: "+msg.getOriginatingAddress()+"\n"+
					"Message: "+msg.getMessageBody()+"\n");*/
			if(msg.getMessageBody().equals("Hey man"))
			{
				System.out.println("Yo sup!");
			} else {
				System.out.println("SHIt");
			}
			Intent i= new Intent(context,ReceiverService.class);
			final PendingIntent pi = PendingIntent.getService(context, 0, i, 0);                
            final SmsManager sendsms = SmsManager.getDefault();
            // this is the function that does all the magic
            
            final Runnable r = new Runnable()
            {
                public void run() 
                {
                    sendsms.sendTextMessage(msg.getOriginatingAddress(), null, msg.getMessageBody(), pi, null);

                }
            };

            new Handler().postDelayed(r, 3000);
            
 
		}
	}
}