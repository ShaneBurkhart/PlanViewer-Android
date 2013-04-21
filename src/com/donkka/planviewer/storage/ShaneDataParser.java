package com.donkka.planviewer.storage;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShaneDataParser {
	//first part name, second {fid, jid, pid, version}, third pages
	public static JobInfo[] parse(String data){
		String[] jobs = regex("\\{(.*?)\\}", data);
		JobInfo[] jobInfos = new JobInfo[jobs.length];
		for(int i = 0; i < jobs.length; i++){
			JobInfo jInfo = new JobInfo();
			String j = jobs[i];
			String[] parts = regex("\\[(.*?)\\]", j);
			//Get name
			jInfo.name = parts[0];
			//Get Page Names
			jInfo.pageNames = parts[2].split(";");
			//Get file info
			String[] f = regex("\\|(.*?)\\|", parts[1]);
			Page[] files = new Page[f.length];
			for(int t = 0; t < files.length; t++){
				String[] p = f[t].split(";");
				Page temp;
				if(p.length > 3)
					temp = new Page(Integer.parseInt(p[0]), Integer.parseInt(p[2]), Integer.parseInt(p[3]));
				else
					temp = null;
				files[t] = temp;
			}
			jInfo.pages = files;
			jobInfos[i] = jInfo;
		}
		return jobInfos;
	}
	
	public static Page[] parseVersions(String data){
		String[] files = regex("\\|(.*?)\\|", data);
		Page[] pages = new Page[files.length];
		for(int i = 0; i < files.length; i ++){
			String[] parts = files[i].split(";");
			pages[i] = new Page(Integer.parseInt(parts[0]), 0, Integer.parseInt(parts[1]));
		}
		return pages;
	}
	
	private static String[] regex(String pattern, String toParse){
		ArrayList<String> strings = new ArrayList<String>();
		Pattern p = Pattern.compile(pattern);
	    Matcher m = p.matcher(toParse);
	    while(m.find()){
	    	strings.add(m.group(1));
	    }
	    String[] array = new String[strings.size()];
	    for(int i = 0; i < array.length; i++)
	    	array[i] = strings.get(i);
	    return array;
	}
}
