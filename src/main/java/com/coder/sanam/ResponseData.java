package com.coder.sanam;

public class ResponseData {

	private long fileSize;
	public ResponseData() {
	
	}


	private String downloadURL;
	private String fileName;
	private String fileType;
	

	public ResponseData(long fileSize, String downloadURL, String fileName, String fileType) {
		this.fileSize = fileSize;
		this.downloadURL = downloadURL;
		this.fileName = fileName;
		this.fileType = fileType;
	}


	public long getFileSize() {
		return fileSize;
	}


	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}


	public String getDownloadURL() {
		return downloadURL;
	}


	public void setDownloadURL(String downloadURL) {
		this.downloadURL = downloadURL;
	}


	public String getFileName() {
		return fileName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public String getFileType() {
		return fileType;
	}


	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
}
