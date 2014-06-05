package com.wanda.ccs.member.segment.service;

import java.util.Map;


/**
 *
 * @author Charlie Zhang
 * @since 2013-12-21
 */
public interface CodeListService  {
	
	/**
	 * 得到sourceId的列表
	 * @return 其中Map.key是sourceId, 其中Map.value是sourceId的说明。 
	 */
	public Map<String, String> getSourceIdInfos();

	/**
	 *
	 * @param sourceId  来源的标示字符串。如：dimdef 表示来自
	 * @param typeId    类型ID
	 * @return
	 */
	public CodeList getCodeList(String sourceId, String typeId);

	/**
	 * 清除对应CodeList的缓存，让其可被重新加载. 
	 * @param name
	 * @since
	 */
	void flush(String sourceId, String typeId);
	
	/**
	 * 方法功能：清除所有CodeList的缓存，让其可被重新加载. 
	 * @param name
	 * @since
	 */
	public void flush(String sourceId);

}
