package com.donkka.planviewer.click;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.EditText;

import com.donkka.planviewer.connection.LoginConnection;
import com.donkka.planviewer.connection.LoginHandler;
import com.donkka.planviewer.connection.ProgressHandler;
import com.donkka.planviewer.connection.Worker;


public class CancelClick implements OnClickListener{

	@Override
	public void onClick(DialogInterface dialog, int which) {
		dialog.dismiss();
	}
	
}
