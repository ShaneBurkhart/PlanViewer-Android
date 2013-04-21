package com.donkka.planviewer.connection;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;
import android.os.Message;

public class LoginHandler extends Handler{
	Activity a;
	Worker worker;
	public LoginHandler(Activity a, Worker worker){
		this.a = a;
		this.worker = worker;
	}
	
	AlertDialog dial;
	
	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		int r = msg.what;
		if(r == 0)
			dial = createDialog("Login Failed", "Invalid Username or Password");
		else 
			update(worker, a, r);
			
	}
	
	public void update(Worker worker, Activity a, int id){
		ProgressHandler handler = new ProgressHandler(a);
		worker.addProcess(new FileConnection(a, id, handler));
	}
	
	public AlertDialog createDialog(String title, String cont){
		AlertDialog.Builder builder = new Builder(a);
		builder.setTitle(title)
			.setMessage(cont)
			.setPositiveButton("OK", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
		AlertDialog dial = builder.create();
		dial.setCancelable(false);
		dial.show();
		return dial;
	}
}
