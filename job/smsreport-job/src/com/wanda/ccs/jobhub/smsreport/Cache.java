package com.wanda.ccs.jobhub.smsreport;

import java.util.HashMap;
import java.util.Map;

public class Cache {
	
	public final static Map<String, Object> cache = new HashMap<String, Object>();
	
	/*public static void setCache(Map<String, Object> map) {
		if(cache == null) 
			cache = map;
	}*/
	
	public static synchronized void remove(String key, boolean isCinema) {
		if(cache.containsKey(key+(isCinema?"-cinema":"-line"))) {
			cache.remove(key+(isCinema?"-cinema":"-line"));
		}
	}
	
	public static synchronized String getValue(String key, boolean isCinema) {
		return cache.get(key+(isCinema?"-cinema":"-line")).toString();
	}
	
	public static synchronized void addValue(String key, boolean isCinema) {
		if(!cache.containsKey(key+(isCinema?"-cinema":"-line"))) {
			cache.put(key+(isCinema?"-cinema":"-line"), "0");
		}
	}
	
	public static synchronized void setFailValue(String key, boolean isCinema) {
		if(cache.containsKey(key+(isCinema?"-cinema":"-line"))) {
			cache.put(key+(isCinema?"-cinema":"-line"), "2");
		}
	}
	
	public static synchronized void setSuccessValue(String key, boolean isCinema) {
		if(cache.containsKey(key+(isCinema?"-cinema":"-line"))) {
			cache.put(key+(isCinema?"-cinema":"-line"), "1");
		}
	}
	
}
