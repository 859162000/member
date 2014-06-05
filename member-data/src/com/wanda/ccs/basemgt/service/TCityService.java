package com.wanda.ccs.basemgt.service;

import java.util.List;

import com.wanda.ccs.model.TCity;
import com.xcesys.extras.core.service.ICrudService;

/**
 * 城市相关的业务逻辑Service.
 * 
 * @author Chen
 * @date 2011-10-18
 */
public interface TCityService extends ICrudService<TCity> {

	/**
	 * 查询未删除状态之城市
	 * 
	 * @return
	 */
	public List<TCity> findUnDeletedCity();

	/**
	 * 根据省id查询属于该省的所有城市的列表
	 * 
	 * @param provinceId
	 * @return
	 */
	public List<TCity> findByProvinceId(Long provinceId);

	/**
	 * 根据区域编码获取城市列表。
	 * 
	 * @param area
	 * @return
	 */
	public List<TCity> findByArea(String area);

	/**
	 * 根据城市名称获取城市列表
	 * 
	 * @param names
	 *            城市名称的数据
	 * @return
	 */
	public List<TCity> findByNames(String[] names);

}
