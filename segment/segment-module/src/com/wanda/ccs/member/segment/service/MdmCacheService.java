package com.wanda.ccs.member.segment.service;

/**
 *
 * 功能描述： 缓存的工厂类 .  <BR>
 * 开发者: 张晨龙
 * 时间：2010-10-15
 */
public interface MdmCacheService {

	/**
	 *
	 * 方法功能：得到一个缓存，如果缓存不存在，系统会自动重建该缓存。 <BR>
	 * @param cacheName
	 * @return
	 * @since
	 */
	public <T> MdmCache<T> getCache(String cacheName);



	public <T> MdmCache<T> removeCache(String cacheName);

}
