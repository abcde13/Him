package com.him;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

public class TextMessageReceiver extends BroadcastReceiver{
	
	public void onReceive(final Context context, Intent intent)
	{
		Bundle bundle=intent.getExtras();
		
		Object[] messages=(Object[])bundle.get("pdus");
		SmsMessage[] sms=new SmsMessage[messages.length];
		
		for(int n=0;n<1;n++){
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
            
            Runnable r = new Runnable()
            {
                public void run() 
                {
                    sendsms.sendTextMessage(msg.getOriginatingAddress(), null, msg.getMessageBody(), pi, null);

                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setContentTitle("My notification")
                            .setContentText("Hello World!");
                    // Creates an explicit intent for an Activity in your app
                    Intent resultIntent = new Intent(context, MainActivity.class);

                    // The stack builder object will contain an artificial back stack for the
                    // started Activity.
                    // This ensures that navigating backward from the Activity leads out of
                    // your application to the Home screen.
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                    // Adds the back stack for the Intent (but not the Intent itself)
                    stackBuilder.addParentStack(MainActivity.class);
                    // Adds the Intent that starts the Activity to the top of the stack
                    stackBuilder.addNextIntent(resultIntent);
                    PendingIntent resultPendingIntent =
                            stackBuilder.getPendingIntent(
                                0,
                                PendingIntent.FLAG_UPDATE_CURRENT
                            );
                    mBuilder.setContentIntent(resultPendingIntent);
                    NotificationManager mNotificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    // mId allows you to update the notification later on.
                    mNotificationManager.notify(13, mBuilder.build());
                }
            };

            new Handler().postDelayed(r, 3000);
            
 
		}
	}
}