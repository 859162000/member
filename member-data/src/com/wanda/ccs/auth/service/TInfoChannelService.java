package com.wanda.ccs.auth.service;
import java.util.List;

import com.wanda.ccs.model.TInfoChannel;
import com.xcesys.extras.core.service.ICrudService;

/**
 * Service.
 * 
 * @author Chenxm
 * 
 */

public interface TInfoChannelService extends ICrudService<TInfoChannel>{
	/**
	 * 检查频道名称是否已存在
	 * @param channelName
	 * 频道名称
	 * @return
	 */
	public int checkChannelName(String channelName);
	/**
	 * 检查url是否已存在
	 * @param homeUrl
	 * @return
	 */
	public int checkHomeUrl(String homeUrl);
	/**
	 * 查询所有频道
	 * @return
	 */
	public List<TInfoChannel> findAllChannels();
	
}
