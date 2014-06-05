package com.wanda.ccs.schedule.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.wanda.ccs.model.TScheduleGuideB;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.ICrudService;

/**
 * 排片指导制定Body service interface
 * 
 * @author Benjamin
 * @date 2011-10-20
 */
public interface TScheduleGuideBService extends ICrudService<TScheduleGuideB> {
	
	List<TScheduleGuideB> findByCinemaAndDate(Long cinemaId, Date date);
	
	/**
	 * 查询FilmId与其个数，并组装成Map
	 * @param infoHId headId
	 * @return
	 */
	Map<Long,Integer> findFilmIdAndCount(Long infoHId);
	
	/**
	 * 查询排片报警记录实时日志
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	PageResult findAlarmInformations(QueryCriteria criteria);
	
	/**
	 * 查询报警次数月度统计
	 * @return Map<Long,Map<String,Integer>>:<影城ID,<年月,数量>>
	 */
	@SuppressWarnings("rawtypes")
	Map<Long,Map<String,Integer>> findAlarmMonthStatistics(QueryCriteria criteria);
	
	
}
