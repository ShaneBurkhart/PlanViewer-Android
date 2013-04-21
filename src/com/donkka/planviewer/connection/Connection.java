package com.donkka.planviewer.connection;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Connection {
	Activity a;
	public Connection(Activity a){
		this.a = a;
	}
	
	public static boolean checkForConnection(Activity a){
		ConnectivityManager conMgr = (ConnectivityManager) a.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo i = conMgr.getActiveNetworkInfo();
		if (i == null)
			return false;
		if (!i.isConnected())
			return false;
		if (!i.isAvailable())
			return false;
		return true;
	}
	
	public static void launchConnectionError(final Activity a){
		AlertDialog.Builder builder = new Builder(a);
		builder.setTitle("Internet Not Available")
			.setMessage("The application needs access to the internet to update files.  Please enable wifi for fastest connection")
			.setPositiveButton("OK", new OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					arg0.dismiss();
				}
			});
		AlertDialog dial = builder.create();
		dial.setCancelable(false);
		dial.show();
	}
}
