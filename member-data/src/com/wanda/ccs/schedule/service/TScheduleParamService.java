package com.wanda.ccs.schedule.service;

import java.util.Map;

import com.wanda.ccs.model.TScheduleParam;
import com.xcesys.extras.core.service.ICrudService;

/**
 * 排片基准价格与广告时间设定
 * 
 * @author Benjamin
 * @date 2011-11-10
 */
public interface TScheduleParamService extends ICrudService<TScheduleParam> {

	/**
	 * 检查唯一键是否存在。
	 * 
	 * @param region
	 * @param cityId
	 * @param cinemaId
	 * @param hallType
	 * @param filmId
	 * @return
	 */
	boolean checkExisted(String region, Long cityId, Long cinemaId,
			String hallType, Long filmId);

	/**
	 * 读取影片排片参数设定Map，Key为影片ID。
	 * 
	 * @param cinemaId
	 *            影院ID
	 * @param hallType
	 *            场次所在影厅类型
	 * @param filmIds
	 *            影片ID数组
	 * 
	 * @return Map<影片ID, TScheduleParam>
	 */
	Map<Long, TScheduleParam> getParamsMap(Long cinemaId, String hallType,
			Long[] filmIds);

	/**
	 * 读取影片排片参数设定Map，Key为影片ID，内嵌Map.Key为影厅类型
	 * 
	 * @param cinemaId
	 *            影院ID
	 * @param filmIds
	 *            影片ID数组
	 * @return
	 */
	Map<Long, Map<String, TScheduleParam>> getParamsMap(Long cinemaId,
			Long[] filmIds);
}
