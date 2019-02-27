package com.kasite.client.crawler.modules.utils;

import java.io.File;

public class ZipFile {
	
	private String encryptPwd;
	
	private File file;
	
	private String dataDirectory;
	
	private String directory;

	public String getEncryptPwd() {
		return encryptPwd;
	}

	public void setEncryptPwd(String encryptPwd) {
		this.encryptPwd = encryptPwd;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getDataDirectory() {
		return dataDirectory;
	}

	public void setDataDirectory(String dataDirectory) {
		this.dataDirectory = dataDirectory;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}
	
}
