package com.wanda.ccs.jobhub.member.utils;

import java.util.Properties;

import com.google.code.pathlet.config.ConfigException;

public class BuddyXmlReader extends BuddyReader {

	private Properties properties;
	
	public BuddyXmlReader(Class buddyClazz, String fileName) {
		super(buddyClazz, fileName);
	}
	
	public BuddyXmlReader(Class buddyClazz, String fileName, String fileCharset) {
		super(buddyClazz, fileName, fileCharset);
	}
	
	public String get(String key) {
		return this.properties.getProperty(key);
	}
	
	protected void readData() {
		try {
			this.properties = new Properties();
			this.properties.loadFromXML(getFileInputStream());
		} 
		catch (Exception e) {
			throw new ConfigException("Failed to parse XML content! ", e);
		} 
	}
	
}
