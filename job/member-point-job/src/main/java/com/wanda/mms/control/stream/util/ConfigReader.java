package com.wanda.mms.control.stream.util;

import java.util.ResourceBundle;

public class ConfigReader {

	private ResourceBundle bundle = null; 
	
	public ConfigReader(String configFileName) {
		bundle = ResourceBundle.getBundle(configFileName);
	}
	
	public String getValue(String key) {
		return bundle.getString(key);
	}
}

