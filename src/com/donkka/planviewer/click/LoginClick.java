package com.donkka.planviewer.click;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.EditText;

import com.donkka.planviewer.connection.LoginConnection;
import com.donkka.planviewer.connection.LoginHandler;
import com.donkka.planviewer.connection.ProgressHandler;
import com.donkka.planviewer.connection.Worker;

public class LoginClick implements OnClickListener{
	EditText etuser = null;
	EditText etpass = null;
	Activity a;
	Worker w;
	
	public LoginClick(Activity a, Worker worker, EditText user, EditText pass){
		this.etuser = user;
		this.etpass = pass;
		this.a = a;
		this.w = worker;
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
		click(etuser.getText().toString(), etpass.getText().toString());
	}
	
	public void click(String u, String p){
		LoginHandler login = new LoginHandler(a, w);
		ProgressHandler handler = new ProgressHandler(a);
		w.addProcess(new LoginConnection(a, u, p, login, handler));
	}
	
}
