package com.donkka.planviewer.connection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;

import com.donkka.planviewer.storage.JobInfo;
import com.donkka.planviewer.storage.Page;
import com.donkka.planviewer.storage.ShaneDataParser;
import com.donkka.planviewer.storage.Storage;

public class FileConnection extends Connection implements Runnable{

	int contentSize = 0;
	ProgressHandler handler;
	int userId;
	private final String PAGE_FILENAME = "pages.txt";
	private final String VERSION_FILENAME = "version.txt";
	
	@Override
	public void run() {
		updateFiles(userId, handler);
	}
	
	public FileConnection(Activity a, int userId, ProgressHandler handler) {
		super(a);
		this.userId = userId;
		this.handler = handler;
	}
	boolean download = true;

	public void updateFiles(int userId, ProgressHandler handler){
		
		if(!Storage.checkExternalStorage()){
			Storage.launchExternalError(a);
			return;
		}
		
		try {
			sendMsgToHandler(handler, "show");
			sendMsgToHandler(handler, "Getting files");
			BufferedReader br = getReader("http://vedesigngroup.com/PlanViewer/retrieve?method=file&uid=" + userId);
			JobInfo[] jobs = ShaneDataParser.parse(retriveData(br).toString());
			Page[] versions = getVersions();
			for(JobInfo j: jobs){
				int total = j.pages.length;
				int i = 1;
				createDir(a.getExternalFilesDir(null) + File.separator + j.name);
				for(Page p: j.pages){
					download = true;
					if(p != null){
						for(Page versionPage: versions){
							if(versionPage.fileID == p.fileID){
								if(versionPage.version == p.version){
									download = false;
									break;
								}
							}
						}
						if(download){
							sendMsgToHandler(handler, "This may take a minute...\nDownloading: " + j.name + "\nPage " + i + " of " + total);
							download(j.name, p);
						}
					}
					i++;
				}
				savePages(j);
			}
			removeNonExistingJobs(jobs);
			saveVersions(jobs);
			br.close();
			sendMsgToHandler(handler, "refresh");
		} catch (MalformedURLException e) {
			sendMsgToHandler(handler, e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			sendMsgToHandler(handler, e.getMessage());
			e.printStackTrace();
		}
	}	
	
	private void removeNonExistingJobs(JobInfo[] jobs){
		//Removes jobs that no longer belong to the user
		boolean delete = true;
		File[] files = a.getExternalFilesDir(null).listFiles();
		for(File f : files){
			delete = true;
			if(f.isDirectory()){
				for(JobInfo j : jobs){
					if(j.name.equals(f.getName())){
						delete = false;
						break;
					}
				}
			}
			if(delete && !f.getName().equals(LoginConnection.LOGIN_FILENAME))
				delete(f);
		}
	}
	
	private void saveVersions(JobInfo[] jobs) throws IOException{
		String data = "";
		for(JobInfo j: jobs){
			for(Page p: j.pages){
				data += "|";
				data += p.fileID;
				data += ";";
				data += p.version;
				data += "|";
			}
		}
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(a.getExternalFilesDir(null), VERSION_FILENAME))));
		writer.write(data);
		writer.flush();
		writer.close();
	}
	
	private Page[] getVersions() throws IOException{
		File f = new File(a.getExternalFilesDir(null), VERSION_FILENAME);
		if(!f.exists())
			return new Page[0];
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
		String data = "";
		String temp;
		while((temp = reader.readLine()) != null)
			data += temp;
		reader.close();
		return ShaneDataParser.parseVersions(data);
	}
	
	private void download(String jobName, Page page) throws IOException{
		String d = a.getExternalFilesDir(null) + File.separator + jobName;
		DataInputStream dis = getDataIS("http://vedesigngroup.com/PlanViewer/files/" + page.fileID + ".pdf");			
		FileOutputStream fos = new FileOutputStream(new File(d + File.separator + page.pageID + ".pdf"));
		getAndWrite(dis, fos);
	}
	
	private void getAndWrite(DataInputStream dis, FileOutputStream fos) throws IOException{
		byte[] buf = new byte[contentLength];
		dis.readFully(buf);
		fos.write(buf);
		fos.flush();
		dis.close();
		fos.close();
	}
	
	int contentLength = 0;

	private DataInputStream getDataIS(String u) throws IOException{
		URL url = new URL(u);
		URLConnection con = url.openConnection();
		con.connect();
		contentLength = con.getContentLength();
		InputStream is = con.getInputStream();
		return new DataInputStream(is);
	}
	
	private BufferedReader getReader(String u) throws IOException{
		URL url = new URL(u);
		URLConnection con = url.openConnection();
		con.connect();
		InputStreamReader is = new InputStreamReader(con.getInputStream());
		contentSize = con.getContentLength();
		return new BufferedReader(is);
	}
	
	private void createDir(String path){
		File f = new File(path);
		if(!f.exists()){
			f.mkdirs();
		}
	}
	
	private void delete(File dir){
		if(dir.isDirectory()){
			for(File f: dir.listFiles()){
				if(f.isDirectory())
					delete(f);
				else
					f.delete();
			}
		}
		dir.delete();
	}
	
	private void savePages(JobInfo job) throws IOException{
		String data = "";
		for(int i = 0; i < job.pageNames.length; i++){
			data += job.pageNames[i];
			if(i < job.pageNames.length - 1)
				data += ";";
		}
		FileOutputStream fos = new FileOutputStream(a.getExternalFilesDir(null) + File.separator + job.name + File.separator + PAGE_FILENAME);
		fos.write(data.getBytes());
		fos.flush();
		fos.close();
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

	
}
