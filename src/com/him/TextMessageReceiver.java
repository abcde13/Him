package com.him;


import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;

import org.apache.http.client.utils.URIUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.orchestr8.android.api.AlchemyAPI;
import com.orchestr8.android.api.AlchemyAPI_NamedEntityParams;

import simplenlg.framework.NLGElement;
import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.realiser.english.Realiser;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
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
	
	private String simpleInitiators[] = new String[]{"hi ","hello ","hey ","yo "};
	private String formalTimes[] = new String[]{"morn", "afternoon", "evening", "night", "nite"};
	private String closers[] = new String[]{"bye","l8er","later","cya","ttyl","bb"};
	private String testSupInitiators[] = new String[]{"sup","whassup","whatsup","how's it going"};
	private String supInitiators[] = new String[]{"nothing much. u?","bored as fuck","wassup","fucking your girlfriend","sup"};
	private String timeFrames[] = new String[]{"at night","in the evening","in the morning","on the morrow","at twilight","before the break of dawn"};
	private String END_OF_THE_FUCKING_CONVERSATION[] = new String[]{"lol","haha"};
	private boolean alchemyFlag =true;
	private static final String TELEPHON_NUMBER_FIELD_NAME = "address";
	private static final String MESSAGE_BODY_FIELD_NAME = "body";
	private static final Uri SENT_MSGS_CONTET_PROVIDER = Uri.parse("content://sms/sent");
	private ArrayList<String> words;

	private int hour;
	
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
		
		if((msg.getMessageBody().toLowerCase().contains(END_OF_THE_FUCKING_CONVERSATION[0]) || 
				(msg.getMessageBody().toLowerCase().contains(END_OF_THE_FUCKING_CONVERSATION[1]))) && 
				msg.getMessageBody().length() < 6){
			return;
		}
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
			for(int i = 0; i < formalTimes.length; i++){
				if(msg.getMessageBody().toLowerCase().contains(formalTimes[i])){					
					if(i == 0){
						output = "morning";
					} else if (i == 3 || i == 4){
						output = "Gdnite";
					} else {
						Random rand = new Random();
						int index = rand.nextInt(2);
						output = formalTimes[index+1];
					}
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
		
		if(output.equals("Default")){
			if(msg.getMessageBody().toLowerCase().equals("really?")) {
				Random rand = new Random();
				int index = rand.nextInt(2);
				if(index == 0)
					output = "Really";
				else
					output = "No...I was clearly just making it up...no shit Sherlock";
				alchemyFlag = false;
			}
		}
		
		if(alchemyFlag){
			
			words = SendAlchemyCall(AlchemyAPI_Key, msg.getMessageBody());
			for(int i =0; i < words.size(); i++){
				System.out.println(words.get(i));
			}	
			output = generateMessage(words); 
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
                    sendsms.sendTextMessage(msg.getOriginatingAddress(), null, out, pi, null);
                    handleNotification(context,msg,out);
                    addMessageToSent(context,msg.getOriginatingAddress(),out);                   
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
	

	private String generateMessage(ArrayList<String> words){
		 Lexicon lexicon = Lexicon.getDefaultLexicon();
         NLGFactory nlgFactory = new NLGFactory(lexicon);
         Realiser realiser = new Realiser(lexicon);
        // NLGElement s1 = nlgFactory.createSentence(msg);
        // String output = realiser.realiseSentence(s1);
         SPhraseSpec p = nlgFactory.createClause();
         p.setSubject("The " + words.get(2));
         p.setVerb("is ");
         
         Random rand = new Random();
		 int index = rand.nextInt(timeFrames.length);
		 String str_time = timeFrames[index];

         p.setObject(str_time);
         
         String output = realiser.realiseSentence(p);
         return output;
	}
	private void addMessageToSent(Context context,String telNumber, String messageBody) {
        ContentValues sentSms = new ContentValues();
        sentSms.put(TELEPHON_NUMBER_FIELD_NAME, telNumber);
        sentSms.put(MESSAGE_BODY_FIELD_NAME, messageBody);

        ContentResolver contentResolver = context.getContentResolver();
        contentResolver.insert(SENT_MSGS_CONTET_PROVIDER, sentSms);
    }
	
	 private ArrayList<String> SendAlchemyCall(String call, String msg)
	    {
	    	Document doc = null;
	    	AlchemyAPI api = null;
	    	ArrayList<String> al;
	    	try
	    	{
	    		api = AlchemyAPI.GetInstanceFromString(AlchemyAPI_Key);
	    	}
	    	catch( IllegalArgumentException ex )
	    	{
	    		System.out.println("Error loading AlchemyAPI.  Check that you have a valid AlchemyAPI key set in the AlchemyAPI_Key variable.  Keys available at alchemyapi.com.");
	    		return null;
	    	}

	    	try{
	    			doc = api.TextGetRankedKeywords(msg);
	    			al= ShowDocInTextView(doc, false);
	    	}
	    	catch(Throwable t)
	    	{
	    		System.out.println("Error: " + t.getMessage());
	    		return null;
	    	}
	    	return al;
	    }
	 
	 private ArrayList<String> ShowDocInTextView(Document doc, boolean showSentiment)
	    {
		 	ArrayList<String> array = new ArrayList<String>();
	    	
	    	if( doc == null)
	    		return null;
	    	Element root = doc.getDocumentElement();
	        NodeList items = root.getElementsByTagName("text");
	        if( showSentiment )
	        {
	        	NodeList sentiments = root.getElementsByTagName("sentiment");
		        for (int i=0;i<items.getLength();i++){
		        	Node concept = items.item(i);
		        	String astring = concept.getNodeValue();
		        	astring = concept.getChildNodes().item(0).getNodeValue(); 
		        	//textview.append("\n" + astring);
		        	array.add(astring);
		        	if( i < sentiments.getLength() )
		        	{
		        		Node sentiment = sentiments.item(i);
		        		Node aNode = sentiment.getChildNodes().item(1);
		        		Node bNode = aNode.getChildNodes().item(0);
		        		array.add(bNode.getNodeValue());
		        	}
		        }       	
	        }
	        else
	        {
		        for (int i=0;i<items.getLength();i++){
		        	Node concept = items.item(i);
		        	String astring = concept.getNodeValue();
		        	astring = concept.getChildNodes().item(0).getNodeValue(); 
		        	array.add(astring);
		        }
	        }
	        return array;
	    }
}

