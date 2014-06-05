package com.wanda.ccs.schedule.service;

import java.util.List;
import java.util.Map;

import com.wanda.ccs.model.TScheduleGuideH;
import com.wanda.ccs.service.IStateUVService;

/**
 * 排片指导
 * 
 * @author Benjamin
 * @date 2011-10-20
 */
public interface TScheduleGuideHService extends IStateUVService<TScheduleGuideH> {

	/**
	 * 查询指定区域的排片指导
	 * 
	 * @param regions
	 *            区域
	 * @return
	 */
	public Map<String, String> findAllGuideH(String... regions);

	/**
	 * 根据期数查询
	 * 
	 * @param issue
	 *            期数
	 * @return
	 */
	List<TScheduleGuideH> findByIssue(String issue);

	/**
	 * 获得最新的版本号
	 * 
	 * @param issue
	 *            期数
	 * @return
	 */
	/**
	 * @param issue
	 * @return
	 */
	public int obtainLatelyVer(String issue);

}
