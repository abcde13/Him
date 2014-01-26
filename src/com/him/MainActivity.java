package com.him;

import org.w3c.dom.Document;


import android.os.Bundle;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity {

	static TextView messageBox;
	static Button button;
	public static int receiving = 0;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	    messageBox=(TextView)findViewById(R.id.messageBox);
	    button = (Button)findViewById(R.id.btnStartService);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private boolean isMyServiceRunning() {
	    ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (ReceiverService.class.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
	
	// Method to start the service
	public void startService(View view) {
		if(isMyServiceRunning())
		{
			stopService(new Intent(getBaseContext(), ReceiverService.class));
			button.setText("Hell yeah baby!");
		} else {
			button.setText("Stop him now");
			startService(new Intent(getBaseContext(), ReceiverService.class));
		}
	}
	
	public static void updateMessageBox(String msg)
    {
    	messageBox.append(msg);
    }
	
	

}
