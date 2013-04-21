package com.donkka.planviewer.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.app.Activity;

public class PagesHelper {
	public static final String FILENAME = "pages.txt";
	
	Activity a;
	ArrayList<PageInfo> pages;
	String jobName;
	
	public PagesHelper(Activity a, String jobName){
		this.a = a;
		this.jobName = jobName;
		this.pages = getPages();
	}
	
	public int getPageID(String name){
		for(PageInfo p: pages){
			if(p.name.equals(name))
				return Integer.parseInt(p.id);
		}
		return 0;
	}
	
	public String getPageName(int id){
		for(PageInfo p: pages){
			if(Integer.parseInt(p.id) == id)
				return p.name;
		}
		return "";
	}
	
	public ArrayList<PageInfo> getPages(){
		try {
			File f = new File(a.getExternalFilesDir(null) + File.separator + jobName + File.separator + FILENAME);
			BufferedReader br;
			if((br = getReader(f)) != null)
				return parseData(retriveData(br));
			else
				return null;
		} catch (IOException e) {
			return null;
		}
	}
	
	private StringBuffer retriveData(BufferedReader br) throws IOException{
		StringBuffer buffer = new StringBuffer();
		String s;
		while((s = br.readLine()) != null){
			buffer.append(s);
		}
		return buffer;
	}
	
	public BufferedReader getReader(File f){
		try {
			FileInputStream fis = new FileInputStream(f);
			return new BufferedReader(new InputStreamReader(fis));
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	private ArrayList<PageInfo> parseData(StringBuffer buffer){
		String s = buffer.toString().trim(); //Trim whitespace
		String[] pages = s.split(";");
	    ArrayList<PageInfo> info = new ArrayList<PageInfo>();
	    for(int i = 0; i < pages.length; i++){
	    	info.add(new PageInfo((i + 1) + "", pages[i]));
	    }
		return info;
	}
	
	public boolean fileExists(){
		if(!Storage.checkExternalStorage()){
			Storage.launchExternalError(a);
			return false;
		}
		
		File f = new File(a.getExternalFilesDir(null), FILENAME);
		return f.exists();
	}
	
}
