package com.donkka.planviewer.storage;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Environment;

import com.donkka.planviewer.click.ExternalStorageDialogClick;

public class Storage {
	
	public static boolean checkExternalStorage(){
		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
		    mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
		    mExternalStorageAvailable = true;
		    mExternalStorageWriteable = false;
		} else {
		    mExternalStorageAvailable = mExternalStorageWriteable = false;
		}
		
		return mExternalStorageAvailable && mExternalStorageWriteable;
	}
	
	public static void launchExternalError(final Activity a){
		AlertDialog.Builder builder = new Builder(a);
		builder.setTitle("Storage Not Available!")
			.setMessage("The application needs access to your device's external storage.")
			.setPositiveButton("OK", new ExternalStorageDialogClick(a));
		AlertDialog dial = builder.create();
		dial.setCancelable(false);
		dial.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface arg0) {
				a.finish();
			}
		});
		dial.show();
	}
}
