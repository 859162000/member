package com.wanda.ccs.schedule.service;

import java.util.List;
import java.util.Map;

import com.wanda.ccs.model.TFilm;
import com.wanda.ccs.model.TScheduleInfoB;
import com.wanda.ccs.model.TScheduleInfoH;
import com.wanda.ccs.service.IStateUVService;

/**
 * 排片信息
 * 
 * @author Benjamin
 * @date 2011-11-17
 */
public interface TScheduleInfoHService extends IStateUVService<TScheduleInfoH> {

	/**
	 * 获得最新的版本的排片信息
	 * 
	 * @param month
	 *            年月
	 * @return
	 */
	public TScheduleInfoH getLatestInfoH(String month);

	/**
	 * 根据年月和版本，检查是否数据库中已经存在对应记录。
	 * 
	 * @param month
	 *            年月
	 * @param ver
	 *            版本
	 * 
	 * @return 返回true，如果记录存在，否则返回false.
	 */
	boolean checkExisted(String month, String ver);

	/**
	 * 保存单个影片排片信息时，若影片为数字制式，则更新影片的拷贝数影院数
	 * 
	 * @param infoH
	 * @param film
	 * @param copyCount
	 */
	public void saveFilmSchAndMayUpdateDigitalFilmCopyCount(
			TScheduleInfoH infoH, TFilm film, int copyCount);

	/**
	 * 同步影片在影城的上映与落幕日期
	 * 
	 * @param syncInfoBs
	 *            将要同步之数据
	 * @param ver
	 *            版本
	 */
	public void syncFilmDate(Map<String, List<TScheduleInfoB>> syncInfoBs,
			String ver);

}
