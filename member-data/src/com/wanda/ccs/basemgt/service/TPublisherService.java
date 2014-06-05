package com.wanda.ccs.basemgt.service;

import java.util.List;

import com.wanda.ccs.model.TPublisher;
import com.xcesys.extras.core.service.ICrudService;

/**
 * 发行商相关的业务逻辑Service.
 * 
 * @author Benjamin
 * @date 2011-10-18
 */
public interface TPublisherService extends ICrudService<TPublisher> {

	/**
	 * 查询未删除状态之发行商
	 * 
	 * @return
	 */
	public List<TPublisher> findUnDeletedPublishers();

	/**
	 * 根据发行商名称模糊查询发行商
	 * 
	 * @param like
	 * @return
	 */
	public List<TPublisher> findByName(String like);

}
