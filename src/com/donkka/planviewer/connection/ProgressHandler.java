package com.donkka.planviewer.connection;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

public class ProgressHandler extends Handler{
	
	ProgressDialog dial;
	Activity a;
	public ProgressHandler(Activity a){
		this.dial = new ProgressDialog(a);
		this.dial.setCancelable(false);
		this.a = a;
	}
	
	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		String s = msg.getData().getString("msg");
		if(s == "refresh")
			refresh();
		else if(s == "show")
			this.dial.show();
		else if(s == "close")
			this.dial.dismiss();
		else
			this.dial.setMessage(s);
		
	}
	
	public void refresh(){
		dial.dismiss();
		Intent i = a.getIntent();
		a.startActivity(i);
		a.finish();
	}
}
