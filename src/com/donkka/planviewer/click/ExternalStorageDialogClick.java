package com.donkka.planviewer.click;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class ExternalStorageDialogClick implements OnClickListener{

	Activity a;
	
	public ExternalStorageDialogClick(Activity a){
		this.a = a;
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
		a.finish();
	}
}
