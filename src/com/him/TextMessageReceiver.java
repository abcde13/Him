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

import simplenlg.features.Feature;
import simplenlg.features.Tense;
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
	
	private String simpleInitiators[] = new String[]{"hi","hello","hey"};
	private String formalTimes[] = new String[]{"morn", "afternoon", "evening", "night", "nite"};
	private String closers[] = new String[]{"bye","l8er","later","cya","ttyl","bb"};
	private String testSupInitiators[] = new String[]{"how are you","how are u","how's it going","sup","whassup","whatsup","whats up", "what's up","whaddup",
			"whatup", "what up", "wuttup","wuzzup", "what's cookin good lookin"};
	private String supInitiators[] = new String[]{"not too bad","really bored right now","bored as fuck","hacking furiously","nothing much","chilling","fucking your girlfriend"};
	private String timeFrames[] = new String[]{"at night","around 5ish","3 in the morning","tomorrow","at about 7","when I say so"};
	private String END_OF_THE_FUCKING_CONVERSATION[] = new String[]{"lol","haha"};
	private String who[] = new String[]{"i'm not sure", "I can't say for sure...", 
			"I don't know. you'll have to ask someone else", "Sorry, I have no idea"};
	private String why[] = new String[]{"Why? Because I've stopped caring, and so should you.", "How should I know?"};
	private String where[] = new String[]{"Anywhere", "I have no clue","Around the corner", "In Santa Monica", "I honestly don't care"};
	private boolean alchemyFlag =true;
	private static final String TELEPHON_NUMBER_FIELD_NAME = "address";
	private static final String MESSAGE_BODY_FIELD_NAME = "body";
	private static final Uri SENT_MSGS_CONTET_PROVIDER = Uri.parse("content://sms/sent");
	private ArrayList<String> words;
	
	private String insultKeywords[] = new String[]{"fuck","cunt","douche","dick","chode","bitch","pussy","fag","slut","bastard",
			"vegan","jizz","tiny","twat","shit","cock","milf","whore"};
	
	private String insultLibrary[] = new String[]{"Lick my shit","You jizz covered asshole","I do not give a flying fuck about you or your problems",
			"I'd trade you for a bag of cunts in an instant","Go jerk your chode","Come at me tiny man - I bench 300","Would you like a side of fucks with that?",
			"Congratulations!,you've been nominated for biggest douche in the universe!","Hey! I heard ur dick has grown to four sand grain radii! Well done!",
			"U mad bro? U should be. Ur getting pwned by a computer program", "Well good morning professor dickweed", "I will wreck you like an Asian driver",
			"Man you must love just getting on your knees","You're as useless as a GTA stoplight"};

//	private int hour;
	
	public void onReceive(final Context context, Intent intent)
	{
		alchemyFlag = true;
		Bundle bundle=intent.getExtras();

		Object[] messages=(Object[])bundle.get("pdus");
		SmsMessage[] sms=new SmsMessage[messages.length];
		
		for(int n=0;n<1;n++){
			sms[n]=SmsMessage.createFromPdu((byte[]) messages[n]);
		}
		final SmsMessage msg = sms[0];
		String output = "Talk to u in a bit. Pretty busy";
		
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
		if(output.equals("Talk to u in a bit. Pretty busy")){
			for(int i = 0; i < testSupInitiators.length; i++){
					if(msg.getMessageBody().toLowerCase().contains(testSupInitiators[i])){
						Random rand = new Random();
						if(i >= 0 && i < 3){
							int index = rand.nextInt(3);
							output = supInitiators[index];
						} else {
							int index = rand.nextInt(supInitiators.length-1);
							output = supInitiators[index+1];
						}
						
						alchemyFlag = false;
						break; 
					} 
							
			}
		}
		if(output.equals("Talk to u in a bit. Pretty busy")){
			for(int i = 0; i < formalTimes.length; i++){
					if(msg.getMessageBody().toLowerCase().contains(formalTimes[i])){					
						if(i == 0){
							output = "morning";
						} else if (i == 3 || i == 4){
							if(!msg.getMessageBody().toLowerCase().equals(formalTimes[i])){
								break;
							}
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
		if(output.equals("Talk to u in a bit. Pretty busy")){
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
		
		if(output.equals("Talk to u in a bit. Pretty busy")){
			if(msg.getMessageBody().toLowerCase().equals("really?") || msg.getMessageBody().toLowerCase().equals("rlly?")) {
				Random rand = new Random();
				int index = rand.nextInt(2);
				if(index == 0)
					output = "Really";
				else
					output = "No...I was clearly just making it up...no shit Sherlock";
				alchemyFlag = false;
			}
		}
		
		if(output.equals("Talk to u in a bit. Pretty busy")){
			for(int i = 0; i < insultKeywords.length; i++){
				if(msg.getMessageBody().toLowerCase().contains(insultKeywords[i])){
					Random rand = new Random();
					int index = rand.nextInt(insultLibrary.length);
					output=insultLibrary[index];
					alchemyFlag = false;
					break; 
				} 
						
			}
		} 
		
		if(alchemyFlag){
			try{
				words = SendAlchemyCall(AlchemyAPI_Key, msg.getMessageBody());
				for(int i =0; i < words.size(); i++){
					System.out.println(words.get(i));
				}
			}catch(Exception e){
				output = "I'll talk to you soon enough. Hold on.";
			}
			
			if(msg.getMessageBody().contains("?") && msg.getMessageBody().length() > 5)
			{
				if(msg.getMessageBody().subSequence(0,4).toString().toLowerCase().contains("what"))
				{
					if(msg.getMessageBody().subSequence(5, msg.getMessageBody().length()).toString().contains("time")){
						output = generateMessage(words, "when");
					} else if(words.size()>2) {
						output = generateMessage(words, "what"); 
					} else {
						output = generateMessage(msg.getMessageBody().toLowerCase(),"what");
					}
				} 
				else if(msg.getMessageBody().subSequence(0,3).toString().toLowerCase().contains("who"))
				{
					if(msg.getMessageBody().toLowerCase().equals("who are you?"))
						output = "It doesn't matter who I am. What matters is my plan. You should respect my authoritiauh" ;
					else {
						output = generateMessage(words,"who");
					}					
				}
				else if(msg.getMessageBody().subSequence(0,4).toString().toLowerCase().equals("when"))
				{
					output = generateMessage(words, "when"); 
				}
				else if(msg.getMessageBody().subSequence(0,3).toString().toLowerCase().equals("why"))
				{
					output = generateMessage(words, "why"); 
				}
				else if(msg.getMessageBody().subSequence(0,5).toString().toLowerCase().equals("where"))
				{
					output = generateMessage(msg.getMessageBody().toLowerCase(), "where"); 
				} 
				else {
					output = "??";
				}
			} else {
				Random rand = new Random();
				int index = rand.nextInt(2);
				if(index == 0){
					output = "Good to hear";
				} else {
					output = "Ouch sorry to hear that";
				}
			}
		}

		Intent i= new Intent(context,ReceiverService.class);
		final PendingIntent pi = PendingIntent.getService(context, 0, i, 0);                
        final SmsManager sendsms = SmsManager.getDefault();
        final String out = output;
        // this is the function that does all the magic
            
            /*Runnable r = new Runnable()
            {
                public void run() 
                {*/
                    sendsms.sendTextMessage(msg.getOriginatingAddress(), null, out, pi, null);
                    handleNotification(context,msg,out);
                    addMessageToSent(context,msg.getOriginatingAddress(),out);                   
               /* }
            };

            new Handler().postDelayed(r, 0);*/
	}
	
	private void handleNotification(Context context, final SmsMessage msg,String output){
		NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.him_launcher)
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
	

	private String generateMessage(ArrayList<String> words, String key){
		 Lexicon lexicon = Lexicon.getDefaultLexicon();
         NLGFactory nlgFactory = new NLGFactory(lexicon);
         Realiser realiser = new Realiser(lexicon);
         if(key.equals("what")){

	         SPhraseSpec p = nlgFactory.createClause();
	         p.setSubject("The " + words.get(2));
	         p.setVerb("is ");
	         
	         Random rand = new Random();
			 int index = rand.nextInt(timeFrames.length);
			 String str_time = timeFrames[index];
	
	         p.setObject(str_time);
	         
	         String output = realiser.realiseSentence(p);
	         return output;
         } else if(key.equals("who")){
        	 Random rand = new Random();
        	 int r = rand.nextInt(who.length);
        	 return who[r];
         } else if(key.equals("when")){
        	 Random rand = new Random();
        	 int r = rand.nextInt(timeFrames.length);
        	 return timeFrames[r];
         }else if(key.equals("why")){
        	 Random rand = new Random();
        	 int r = rand.nextInt(why.length);
        	 return why[r];
	     }
         return("I litterally have no clue.");
	}
	
	private String generateMessage(String words, String key){
		Lexicon lexicon = Lexicon.getDefaultLexicon();
        NLGFactory nlgFactory = new NLGFactory(lexicon);
        Realiser realiser = new Realiser(lexicon);
        if(key == "what"){
        	if(words.contains("was") || words.contains("is") || words.contains("what's") || words.contains("whats")){
        		String subject = "";
        		if(words.contains("was"))
        			subject = words.subSequence(words.indexOf("was")+3,words.length()-1).toString();
        		else if(words.contains("is")){
    				subject = words.subSequence(words.indexOf("is")+2,words.length()-1).toString();
        		} else if (words.contains("what's") ) {
        			subject = words.subSequence(words.indexOf("what's")+6,words.length()-1).toString();
        		} else if ( words.contains("whats")){
        			subject = words.subSequence(words.indexOf("whats")+5,words.length()-1).toString();
        		}

        		String verb = "be";
        		SPhraseSpec p = nlgFactory.createClause();
	   	         p.setSubject(subject);
	   	         p.setVerb(verb);
	   	         p.setObject("not worth mentioning");
	   	         
	   	         return realiser.realiseSentence(p);
        	}
        	else if(words.contains("can") || words.contains("would") || words.contains("should") || words.contains("could") || words.contains("will")) {
        		String subject="";
        		String verb="";
        		if(words.contains("can")) {
        			subject = words.subSequence(words.indexOf("can")+3,words.length()-1).toString();
        			verb = "can";
        		}
        		else if(words.contains("would")) {
        			subject = words.subSequence(words.indexOf("would")+5,words.length()-1).toString();
        			verb = "would";
        		}
        		else if(words.contains("should")) {
        			subject = words.subSequence(words.indexOf("should")+6,words.length()-1).toString();
        			verb = "should";
        		}
        		else if(words.contains("could")) {
        			subject = words.subSequence(words.indexOf("could")+5,words.length()-1).toString();
        			verb = "could";
        		}
        		else if(words.contains("will")) {
        			subject = words.subSequence(words.indexOf("will")+4,words.length()-1).toString();
        			verb = "will";
        		}
        		if(subject.contains(" she "))
        			subject = "She";
        		else if(subject.contains(" he "))
        			subject = "He";
        		else if(subject.contains(" a ") || subject.contains(" the "))
       				subject = "It";
       			else if(subject.contains(" u ") || subject.contains(" you ")){
       				subject= "I";
       			} else {
       				subject ="They";
       			}
        			
        		SPhraseSpec p = nlgFactory.createClause();
   	           	p.setSubject(subject);
   	           	p.setVerb(verb);
   	           	if(subject!="I" ){
   	           		p.setObject("lick my shit");
   	           	} else {
   	           		p.setObject("move forward, and never falter");
   	           	}
   	   	         
   	           	return realiser.realiseSentence(p);
        	}	
        } else if (key == "where"){
        	if(words.contains(" u ") || words.contains(" you ")){
        		Random k = new Random();
        		int choose = k.nextInt(2);
        		if(choose == 1){
        			return where[where.length-1];
        		} else {
        			return where[choose];
        		}
        	} else {
        		Random k = new Random();
        		int choose = k.nextInt(3);
        		return(where[choose+1]);
        	}
        }
        return null;
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

