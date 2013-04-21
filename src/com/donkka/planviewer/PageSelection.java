package com.donkka.planviewer;

import java.io.File;
import java.util.ArrayList;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.donkka.planviewer.storage.PagesHelper;
import com.donkka.planviewer.storage.Storage;

public class PageSelection extends ShaneActivity{
	
	public String jobName = "";
	PagesHelper ph;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		jobName = getIntent().getStringExtra("jobName");
		ph = new PagesHelper(this, jobName);
		setContentView(R.layout.selection);
		
		TextView header = (TextView) findViewById(R.id.header);
		header.setText(jobName);
		
		File dir = new File(getExternalFilesDir(null), jobName);
		if(Storage.checkExternalStorage() && dir.exists()){
			ArrayAdapter<String>adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.list_item_text, getAlias(sort(exempt(dir.list())))); //getAlias(sort(dir.list()))
			ListView selection = (ListView) findViewById(R.id.lv_selection);
			selection.setAdapter(adapter);
			selection.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					TextView tv = (TextView) arg1;
					openPage(ph.getPageID(tv.getText().toString()) + ".pdf");
				}
			});
		}else{
			Storage.launchExternalError(this);
		}
		
	}
	
	private String[] exempt(String[] files){
		String[] out = new String[files.length - 1];
		int j = 0;
		for(int i = 0; i < files.length; i++){
			if(!files[i].equals("pages.txt")){
				out[j++] = files[i];
			}
		}
		return out;
	}
	
	private ArrayList<String> getAlias(String[] s){
		ArrayList<String> aliases = new ArrayList<String>();
		for(int i = 0; i < s.length; i++){
			String[] parts = s[i].split("\\.");
			String ss = ph.getPageName(Integer.parseInt(parts[0]));
			if(ss != "")
				aliases.add(ss);
		}
		return aliases;
	}

	private void openPage(String index){
		File file = new File(getExternalFilesDir(null), File.separator + jobName + File.separator + index);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),"application/pdf");
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		PageSelection.this.startActivity(intent);
	}
	
	private String[] sort(String[] strings){
		String[] sorted = new String[strings.length];
		int high = 0;
		int index = 0;
		for(int i = strings.length - 1; i >= 0; i--){
			high = 0;
			index = 0;
			for(int j = 0; j < strings.length; j++){
				if(strings[j] != null){
					String[] parts = strings[j].split("\\.");
					int val = Integer.parseInt(parts[0]);
					if(val > high){
						high = val;
						index = j;
					}
				}
			}
			strings[index] = null;
			sorted[i] = high + ".pdf";
		}
		return sorted;
	}
	
}
