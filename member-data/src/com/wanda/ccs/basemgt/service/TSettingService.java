package com.wanda.ccs.basemgt.service;

import java.util.List;
import java.util.Map;

import com.wanda.ccs.model.TSetting;
import com.xcesys.extras.core.service.ICrudService;

/**
 * 系统参数设定Service.
 * 
 * @author Chenxm
 * 
 */
public interface TSettingService extends ICrudService<TSetting> {
	/**
	 * 查询所有的系统 参数
	 * @return
	 */
	public Map<String, List<String>> getAllTSettings();
	
	public TSetting getByName(String name);
	/**
	 * 校验系统参数名称是否存在
	 * @param code
	 * @return
	 */
	boolean checkExisted(String name);

}
