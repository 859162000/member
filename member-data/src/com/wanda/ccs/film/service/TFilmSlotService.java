package com.wanda.ccs.film.service;

import java.util.Date;
import java.util.List;

import com.wanda.ccs.model.TFilmSlot;
import com.xcesys.extras.core.service.ICrudService;

/**
 * 影片最低票价协议Service.
 * 
 * @author Danne Leung
 * 
 */
public interface TFilmSlotService extends ICrudService<TFilmSlot> {
	/**
	 * 根据参数获取影片的最低票价。
	 * 
	 * @param filmId
	 *            影片ID
	 * @param area
	 *            区域
	 * @param cityId
	 *            城市
	 * @param startDate
	 *            开始日期
	 * @param endDate
	 *            结束日期
	 * @return
	 */
	List<TFilmSlot> getLowestPrice(Long filmId, String area, Long cityId,
			Date startDate, Date endDate);
}
