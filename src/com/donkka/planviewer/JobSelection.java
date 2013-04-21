package com.donkka.planviewer;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.donkka.planviewer.storage.Storage;

public class JobSelection extends ShaneActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.selection);
		
		TextView header = (TextView) findViewById(R.id.header);
		header.setText("Your Jobs");
		
		
		File dir = getExternalFilesDir(null);
		if(Storage.checkExternalStorage() && dir.exists()){	
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.list_item_text, sort(filter(dir.listFiles())));
			ListView selection = (ListView) findViewById(R.id.lv_selection);
			selection.setAdapter(adapter);
			selection.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					TextView tv = (TextView) arg1;
					
					Intent i = new Intent(JobSelection.this, PageSelection.class);
					i.putExtra("jobName", tv.getText().toString());
					JobSelection.this.startActivity(i);
				}
			});
		}else{
			Storage.launchExternalError(this);
		}
		
	}
	
	public ArrayList<String> sort(ArrayList<String> files){
		ArrayList<String> sorted = new ArrayList<String>();
		String first = "";
		int index = -1;
		for(int i = 0 ; i < files.size() ; i ++){
			first = "";
			index = -1;
			for(int j = 0 ; j < files.size() ; j ++){
				String s = files.get(j);
				if(s == null)
					continue;
				if(first == "" && index == -1){
					first = s;
					index = j;
					continue;
				}
				if(s.compareTo(first) < 0){
					first = s;
					index = j;
				}
			
			}
			sorted.add(first);
			files.set(index, null);
		}
		return sorted;
	}
	
	public ArrayList<String> filter(File[] s){
		ArrayList<String> strings = new ArrayList<String>();
		for(File f: s){
			if(f.isDirectory())
				strings.add(f.getName());
		}
		return strings;
	}

	//sorts based on the files name
	public class SortFileName implements Comparator<File> {
	    @Override
	    public int compare(File f1, File f2) {
	          return f1.getName().compareTo(f2.getName());
	    }
	}
}
