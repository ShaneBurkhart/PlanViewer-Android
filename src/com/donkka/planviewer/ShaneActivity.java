package com.donkka.planviewer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.donkka.planviewer.click.CancelClick;
import com.donkka.planviewer.click.LoginClick;
import com.donkka.planviewer.connection.Connection;
import com.donkka.planviewer.connection.LoginConnection;
import com.donkka.planviewer.storage.Version;

public class ShaneActivity extends WorkerActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflate = new MenuInflater(this);
		inflate.inflate(R.menu.option_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		String s = item.getTitle().toString();
		if(s.equals("About"))
			launchAbout(this, "This app allows users to download and view building plans.\n\nNote: It is recommended to use Adobe Reader for viewing PDFs. \n\nFor more information contact us.\nCreator: Shane Burkhart\nEmail:shaneburkhart@gmail.com\n\nVersion: " + Version.VERSION);
		else if(s.equals("Change Login")){
			new File(getExternalFilesDir(null) + File.separator + LoginConnection.LOGIN_FILENAME).delete();
			showResetPasswordDialog();
		}else{
			if(!Connection.checkForConnection(this)){
				Connection.launchConnectionError(this);
				return true;
			}
			File login = new File(this.getExternalFilesDir(null), LoginConnection.LOGIN_FILENAME);
			if(login.exists())
				executeClick(login);
			else
				showDialog();
		}
		return true;
	}
	
	public void executeClick(File login){
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(login)));
			String user = reader.readLine();
			String pass = reader.readLine();
			new LoginClick(this, worker, null, null).click(user, pass);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void launchAbout(final Activity a, String s){
		AlertDialog.Builder builder = new Builder(a);
		builder.setTitle("About")
			.setMessage(s)
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
	
	private void showDialog(){
		AlertDialog.Builder builder = new Builder(this);
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		EditText username = new EditText(this);
		EditText password = new EditText(this);
		password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		TextView tvu = new TextView(this);
		tvu.setText("Username");
		tvu.setTextSize(20);
		TextView tvp = new TextView(this);
		tvp.setText("Password");
		tvp.setTextSize(20);
		layout.addView(tvu);
		layout.addView(username);
		layout.addView(tvp);
		layout.addView(password);
		LinearLayout.LayoutParams params = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		username.setLayoutParams(params);
		password.setLayoutParams(params);
		builder.setView(layout)
			.setCancelable(false)
			.setPositiveButton("Login", new LoginClick(this, worker, username, password))
			.setNegativeButton("Cancel", new CancelClick())
			.show();
	}
	

	private void showResetPasswordDialog(){
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("Your Login Can Now Be Changed").setMessage("Next time you update, you will be asked for a username and password.")
			.setCancelable(true)
			.setPositiveButton("Ok", new OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					arg0.dismiss();
				}
			}).show();
	}
	
	public void refresh(){
		Intent i = this.getIntent();
		this.startActivity(i);
		this.finish();
	}
}
