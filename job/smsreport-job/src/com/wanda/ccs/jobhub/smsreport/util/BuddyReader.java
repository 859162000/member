package com.wanda.ccs.jobhub.smsreport.util;

import java.io.IOException;
import java.io.InputStream;

import com.google.code.pathlet.util.ClassPathResource;
import com.google.code.pathlet.util.ClassUtils;
import com.google.code.pathlet.util.IOUtils;

public abstract class BuddyReader {
	public final static String DEFAULT_CHARSET = "UTF-8";
	
	private Class buddyClazz;
	
	private String fileName;
	
	private String fileCharset;
	
	public BuddyReader(Class buddyClazz, String fileName) {
		this(buddyClazz, fileName, null);
	}
	
	public BuddyReader(Class buddyClazz, String fileName, String fileCharset) {
		this.buddyClazz = buddyClazz;
		this.fileName = fileName;
		if(fileCharset != null) {
			this.fileCharset = fileCharset;
		}
		else {
			this.fileCharset = DEFAULT_CHARSET;
		}
		
		this.readData();
	}
	
	public Class getBuddyClazz() {
		return buddyClazz;
	}

	public String getFileName() {
		return fileName;
	}

	public String getFileCharset() {
		return fileCharset;
	}

	protected InputStream getFileInputStream() throws IOException  {
		ClassLoader cl = buddyClazz.getClassLoader();
		String packagePath = ClassUtils.getPackageName(buddyClazz).replace('.', '/');
		return (new ClassPathResource(packagePath + "/" + fileName, cl)).getInputStream();
	}
	
	protected String getFileText() throws IOException  {
		return IOUtils.toString(getFileInputStream(), fileCharset);
	}
	
	
	abstract public String get(String propName);
	
	abstract protected void readData();

}
