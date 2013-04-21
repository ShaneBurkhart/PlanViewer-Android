package com.donkka.planviewer.storage;

public class FileInfo {
	public String id, userID, jobID, jobName, pageID, version;
	public FileInfo(String id, String userID, String jobID, String jobName, String pageID, String version){
		this.id = id;
		this.userID = userID;
		this.jobID = jobID;
		this.jobName = jobName;
		this.pageID = pageID;
		this.version = version;
	}
}
