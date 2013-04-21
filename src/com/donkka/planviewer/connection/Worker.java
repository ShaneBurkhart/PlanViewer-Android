package com.donkka.planviewer.connection;

import android.os.Handler;
import android.os.Looper;

public class Worker {

	Handler handler = null;
	Thread singleton = null;
	
	public Worker(){
		createThread();
	}
	
	private void createThread(){
		singleton = new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("Background thread started");
				Looper.prepare();
				handler = new Handler();
				Looper.loop();
			}
		});
		singleton.start();
	}
	
	public void addProcess(Runnable run){
		if(handler != null)
			handler.post(run);
	}
	
	public void finish(){
		addProcess(new Runnable() {
			@Override
			public void run() {
				Looper.myLooper().quit();
				handler = null;
			}
		});
	}
}
