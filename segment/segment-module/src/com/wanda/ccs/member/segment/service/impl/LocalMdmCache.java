package com.wanda.ccs.member.segment.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.wanda.ccs.member.segment.service.MdmCache;


public class LocalMdmCache<T> implements MdmCache<T> {
	
	private Map<String, T> chageMap;

	public LocalMdmCache() {
		this.chageMap = new HashMap<String, T>();
	}

	public synchronized T get(String key) {
		return chageMap.get(key);
	}

	public synchronized T put(String key, T value) {
		return chageMap.put(key, value);
	}

	public synchronized T remove(String key) {
		return chageMap.remove(key);
	}

	public int size() {
		return chageMap.size();
	}

	public void clear() {
		chageMap.clear();
	}
}
