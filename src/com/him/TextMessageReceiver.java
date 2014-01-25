package com.him;


import java.net.URL;
import java.util.Arrays;
import java.util.Random;

import org.apache.http.client.utils.URIUtils;
import org.w3c.dom.Document;

import com.orchestr8.android.api.AlchemyAPI;
import com.orchestr8.android.api.AlchemyAPI_NamedEntityParams;

import simplenlg.framework.NLGElement;
import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.realiser.english.Realiser;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;


@SuppressLint("NewApi")
public class TextMessageReceiver extends BroadcastReceiver{
	public String AlchemyAPI_Key = "934d4b23c6ad46ac22f86be02c44aa8937e03ab6";
	
	private String simpleInitiators[] = new String[]{"hi","hello","hey","yo"};
	private String closers[] = new String[]{"bye","l8er","later","cya","ttyl","bb"};
	private String testSupInitiators[] = new String[]{"sup","whassup","whatsup","how's it going"};
	private String supInitiators[] = new String[]{"nothing much. u?","bored as fuck","wassup","fucking your girlfriend","sup"};
	private String END_OF_THE_FUCKING_CONVERSATION[] = new String[]{"lol","haha"};
	private boolean alchemyFlag =true;
	
	public void onReceive(final Context context, Intent intent)
	{
		Bundle bundle=intent.getExtras();
		
		Object[] messages=(Object[])bundle.get("pdus");
		SmsMessage[] sms=new SmsMessage[messages.length];
		
		for(int n=0;n<1;n++){
			sms[n]=SmsMessage.createFromPdu((byte[]) messages[n]);
		}
		final SmsMessage msg = sms[0];
		String output = "Default";
		
		for(int i = 0; i < simpleInitiators.length; i++){
				if(msg.getMessageBody().toLowerCase().contains(simpleInitiators[i])){
					Random rand = new Random();
					int index = rand.nextInt(simpleInitiators.length);
					output = simpleInitiators[index];
					alchemyFlag = false;
					break; 
				} 
		}
		if(output.equals("Default")){
			for(int i = 0; i < testSupInitiators.length; i++){
				if(msg.getMessageBody().toLowerCase().contains(testSupInitiators[i])){
					Random rand = new Random();
					int index = rand.nextInt(supInitiators.length);
					output = supInitiators[index];
					alchemyFlag = false;
					break; 
				} 
						
			}
		}

		if(output.equals("Default")){
			for(int i = 0; i < closers.length; i++){
				if(msg.getMessageBody().toLowerCase().contains(closers[i])){
					Random rand = new Random();
					int index = rand.nextInt(closers.length);
					output=closers[index];
					alchemyFlag = false;
					break; 
				} 
						
			}
		} 
		if(alchemyFlag){
			
			SendAlchemyCall(AlchemyAPI_Key, msg.getMessageBody());
		}

		Intent i= new Intent(context,ReceiverService.class);
		final PendingIntent pi = PendingIntent.getService(context, 0, i, 0);                
        final SmsManager sendsms = SmsManager.getDefault();
        final String out = output;
        // this is the function that does all the magic
            
            Runnable r = new Runnable()
            {
                public void run() 
                {
                	final String output = generateMessage(out);
                    sendsms.sendTextMessage(msg.getOriginatingAddress(), null, output, pi, null);
                    handleNotification(context,msg,output);
                    
                }
            };

            new Handler().postDelayed(r, 3000);
            
 
		}
	
	private void handleNotification(Context context, final SmsMessage msg,String output){
		NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Him to " + msg.getOriginatingAddress())
                .setContentText(output);
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
	

	private String generateMessage(String msg){
		 Lexicon lexicon = Lexicon.getDefaultLexicon();
         NLGFactory nlgFactory = new NLGFactory(lexicon);
         Realiser realiser = new Realiser(lexicon);
         NLGElement s1 = nlgFactory.createSentence(msg);
         String output = realiser.realiseSentence(s1);

         return output;
	}
	
	 private void SendAlchemyCall(String call, String msg)
	    {
	    	Document doc = null;
	    	AlchemyAPI api = null;
	    	try
	    	{
	    		api = AlchemyAPI.GetInstanceFromString(AlchemyAPI_Key);
	    	}
	    	catch( IllegalArgumentException ex )
	    	{
	    		System.out.println("Error loading AlchemyAPI.  Check that you have a valid AlchemyAPI key set in the AlchemyAPI_Key variable.  Keys available at alchemyapi.com.");
	    		return;
	    	}

	    	try{
	    			doc = api.TextGetRankedKeywords(msg);
	    			System.out.println(doc +  " " + msg);
	    	}
	    	catch(Throwable t)
	    	{
	    		System.out.println("Error: " + t.getMessage());
	    	}
	    }
}

