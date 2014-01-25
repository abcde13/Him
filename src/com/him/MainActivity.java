package com.him;

import org.w3c.dom.Document;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

	static TextView messageBox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	    messageBox=(TextView)findViewById(R.id.messageBox);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// Method to start the service
	public void startService(View view) {
	   startService(new Intent(getBaseContext(), ReceiverService.class));
	}
	
	public static void updateMessageBox(String msg)
    {
    	messageBox.append(msg);
    }
	
	

}
