package com.wanda.ccs.schedule.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.wanda.ccs.auth.CcsUserProfile;
import com.wanda.ccs.model.TScheduleInfoB;
import com.wanda.ccs.model.TScheduleInfoH;
import com.xcesys.extras.core.service.ICrudService;

/**
 * 排片信息表明细信息的Service.
 * 
 * @author Benjamin
 * 
 */
public interface TScheduleInfoBService extends ICrudService<TScheduleInfoB> {

	/**
	 * 根据影院与放映日期，从排片信息表中获取相关的影片信息。
	 * 
	 * @param cinemaId
	 *            影院id
	 * @param date
	 *            放映日期
	 * @return 排片信息表象系列表。
	 */
	List<TScheduleInfoB> findByCinemaAndDate(Long cinemaId, Date date);

	/**
	 * 根据适用日期范围，查询已审核且上映日期在适用日期范围内， 被调用在排片指导制定时，选择适用日期后jquery ajax调用返回select下拉.
	 * 
	 * @param startDate
	 * @param endDate
	 * @param suitRegions
	 * @return
	 */
	Map<String, String> findSuitFilmsBySuitDate(Date startDate, Date endDate,String...suitRegions);

	/**
	 * 查询 distinct film from TScheduleInfoB where infoHId = :infoHId
	 * 
	 * @param infoHId
	 * @return
	 */
	List findDistinctFilmByInfoHId(Long infoHId);

	/**
	 * 根据排片信息头ID和影片ID，查询特定影片在个影城的body
	 * 
	 * @param infoHId
	 * @param filmId
	 * @param user
	 * @return
	 */
	List<TScheduleInfoB> findByHidAndFilmId(Long infoHId, Long filmId,
			CcsUserProfile user);

	/**
	 * 查询上个月最新版已审批的body
	 * 
	 * @param tScheduleInfoH
	 *            排片Header
	 * @return
	 */
	List<TScheduleInfoB> findLastMonthLatelyApproved(TScheduleInfoH tScheduleInfoH);

}
