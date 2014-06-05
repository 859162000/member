package com.wanda.ccs.auth.service;
import java.util.List;

import com.aggrepoint.adk.IModuleRequest;
import com.wanda.ccs.model.TInfoPage;
import com.xcesys.extras.core.service.ICrudService;

/**
 * Service.
 * 
 * @author Chenxm
 * 
 */

public interface TInfoPageService extends ICrudService<TInfoPage>{
	/**
	 * 撤回已发布信息
	 * @param infoPage
	 */
	public void recallInfoPage(TInfoPage infoPage,IModuleRequest req);
	/**
	 * 发布信息
	 * @param infoPage
	 */
	public void publishInfoPage(TInfoPage infoPage,IModuleRequest req);
	/**
	 * 校验标题是否已存在
	 * @param title
	 * @return
	 */
	public Boolean checkTitle(String title);
	
	/**
	 * 从数据库TInfoPage表获取数据
	 * @return
	 */
	public List<TInfoPage> getMessage(Long infoChannelId);
}
