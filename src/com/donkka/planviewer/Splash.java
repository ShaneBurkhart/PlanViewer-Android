package com.donkka.planviewer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.splash);
		
		Handler handler = new Handler();
		handler.postDelayed(new Runner(), 3000);
	}
	
	private class Runner implements Runnable{

		@Override
		public void run() {
			Intent i = new Intent(Splash.this, JobSelection.class);
			startActivity(i);
			finish();
		}	
	}
}
