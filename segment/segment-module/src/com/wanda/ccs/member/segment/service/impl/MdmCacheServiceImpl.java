package com.wanda.ccs.member.segment.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.wanda.ccs.member.segment.service.MdmCache;
import com.wanda.ccs.member.segment.service.MdmCacheService;


public class MdmCacheServiceImpl implements MdmCacheService {

	private Map<String, MdmCache> mdmCaches;
	
	public MdmCacheServiceImpl() {
		this.mdmCaches = new HashMap<String, MdmCache>();
	}
	
	public synchronized <T> MdmCache<T> getCache(String cacheName) {
		MdmCache cache = this.mdmCaches.get(cacheName);
		if(cache == null) {
			cache = new LocalMdmCache();
			this.mdmCaches.put(cacheName, cache);
		}
		
		return cache;
	}

	public synchronized <T> MdmCache<T> removeCache(String cacheName) {
		return this.mdmCaches.remove(cacheName);
	}

}
