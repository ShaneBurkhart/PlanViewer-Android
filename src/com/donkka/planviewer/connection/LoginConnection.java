package com.donkka.planviewer.connection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.os.Message;

public class LoginConnection extends Connection implements Runnable{
	
	public static final String LOGIN_FILENAME = "login.txt";

	int contentSize = 0;
	
	String user;
	String pass;
	LoginHandler login;
	ProgressHandler handler;

	@Override
	public void run() {
		login(user, pass, login, handler);
	}
	
	public LoginConnection(Activity a, String user, String pass, LoginHandler login, ProgressHandler handler) {
		super(a);
		this.user = user;
		this.pass = pass;
		this.login = login;
		this.handler = handler;
	}

	public void login(String user, String pass, LoginHandler errorHandler, ProgressHandler handler){
		
		try {
			sendMsgToHandler(handler, "show");
			sendMsgToHandler(handler, "Validating Login");
			
			BufferedReader br = getReader("http://vedesigngroup.com/PlanViewer/retrieve?method=login&u=" + user + "&p=" + pass);
			String userID = retriveData(br).toString().trim();
			
			br.close();
		
			sendMsgToHandler(handler, "close");
			if(userID.equals("") || userID.equals("0")){
				File file = new File(a.getExternalFilesDir(null), LOGIN_FILENAME);
				if(file.exists())
					file.delete();
				sendToLogin(login, 0);
			}else{
				saveCredentials(login, user, pass);
				sendToLogin(login, Integer.parseInt(userID));
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	
	private void saveCredentials(LoginHandler login, final String user, final String pass){
		login.post(new Runnable() {
			@Override
			public void run() {
				BufferedWriter writer;
				try {
					writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(a.getExternalFilesDir(null), LOGIN_FILENAME)))) ;
					String data = user + "\n" + pass;
					writer.write(data);
					writer.flush();
					writer.close();
				} catch (FileNotFoundException e) {
					alert(e.getMessage());
					e.printStackTrace();
				} catch (IOException e) {
					alert(e.getMessage());
					e.printStackTrace();
				}
				
			}
		});
	}
	
	private void alert(String msg){
		AlertDialog.Builder builder = new Builder(a);
		builder.setMessage(msg);
		builder.show();
	}

	int contentLength = 0;
	
	private BufferedReader getReader(String u) throws IOException{
		URL url = new URL(u);
		URLConnection con = url.openConnection();
		con.connect();
		InputStreamReader is = new InputStreamReader(con.getInputStream());
		contentSize = con.getContentLength();
		return new BufferedReader(is);
	}
	
	private StringBuffer retriveData(BufferedReader br) throws IOException{
		StringBuffer buffer = new StringBuffer();
		int c = 0;
		if(contentSize <= 0)
			contentSize = 100;
		char[] buf = new char[contentSize];
		while((c = br.read(buf)) != -1){
			buffer.append(buf);
		}
		return buffer;
	}
	
	private void sendMsgToHandler(ProgressHandler handler, String s){
		Bundle b = new Bundle();
		b.putString("msg", s);
		Message m = new Message();
		m.setData(b);
		handler.sendMessage(m);
	}
	
	private void sendToLogin(LoginHandler handler, int n){
		handler.sendEmptyMessage(n);
	}
}
