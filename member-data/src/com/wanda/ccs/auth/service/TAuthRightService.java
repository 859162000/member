package com.wanda.ccs.auth.service;

import java.util.List;
import java.util.Map;

import com.wanda.ccs.model.TAuthRight;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.ICrudService;

/**
 * 
 * 可授予用户的权限相关业务service
 * @author Yang
 * @date 2011-11-29
 */
public interface TAuthRightService extends ICrudService<TAuthRight> {
	/** 可授予用户的权限的列表 */
	public PageResult<TAuthRight> findRightByCriteria(QueryCriteria<String, Object> query);
	
	/**
	 * 根据是否可赋予给院线、区域、影城查找权限
	 * @param right
	 * @return
	 */
	public Map<Long, List<TAuthRight>> getRightsByRight(TAuthRight right);
	
	/**
	 * 根据权限代码获得权限
	 * @param rightName
	 * @return
	 */
	public TAuthRight getRightByCode(String rightCode);
	
	/**
	 * 检测权限代码是否存在
	 * @param id
	 * @param rightCode
	 * @return
	 */
	public boolean checkRightCode(Long id, String rightCode);
}
