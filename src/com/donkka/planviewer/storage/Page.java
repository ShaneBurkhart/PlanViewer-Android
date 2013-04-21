package com.donkka.planviewer.storage;

public class Page {
	public int fileID;
	public int pageID;
	public int version;
	
	public Page(int fileID, int pageID, int version){
		this.fileID = fileID;
		this.pageID = pageID;
		this.version = version;
	}
}
