package com.wanda.ccs.film.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.wanda.ccs.model.TFilm;
import com.wanda.ccs.model.TFilmSlot;
import com.xcesys.extras.core.service.ICrudService;

/**
 * 影片管理的业务逻辑Service.
 * 
 * @author lujx
 * 
 */
public interface TFilmService extends ICrudService<TFilm> {

	/**
	 * 根据编码或者名称，检查是否数据库中已经存在对应记录。
	 * 
	 * @param code
	 *            编码
	 * @return 返回true，如果记录存在，否则返回false.
	 */
	boolean checkExisted(String code);

	/**
	 * 删除影片的票价协议数据
	 * 
	 * @return
	 */
	public void deleteFilmSlot(Long[] ids);

	/**
	 * 根据拼码或名称模糊查询影片。
	 * 
	 * @param like
	 * @return
	 */
	public List<TFilm> findByCodeName(String like);

	/** 编辑不可用规则时选择影片 */
	public Map<String, String> findLatelyFilms(String str);

	/**
	 * 查询所有未被删除的影片
	 * 
	 * @return
	 */
	public Map<Long, String> getAllFilm();

	/**
	 * 根据影片名称查找影片。
	 * 
	 * @param name
	 *            影片名称
	 * @return
	 */
	public TFilm findByName(String name);

	/**
	 * 根据影片实际名称查找影片。
	 * 
	 * @param displayName
	 * @return
	 */
	public TFilm findByDisplayName(String displayName);

	/**
	 * 读取XML文件时获取所有的影片
	 * 
	 * @return
	 */
	public Map<String, TFilm> getFilmList();

	/**
	 * 获取指定日期开始和结束范围内上映的影片列表。
	 * 
	 * @param begin 上映日期
	 * @param end 落幕日期
	 * @param filmLike 影片名称或拼码检索
	 * @param infoHId 排片信息header
	 * @return
	 */
	public List<TFilm> getFilmMap(Date begin, Date end, String filmLike,
			Long infoHId);

	/**
	 * 根据影片ID，区域城市获取最低票价。
	 * 
	 * @param filmId
	 * @param date
	 * @return
	 */
	public int getLowestPrice(Long filmId, String area, Long cityId, Date date);

	/** 删除发行商时判断是否与影片关联 */
	public boolean isExistFilm(long id);

	/**
	 * 上传XML文件中的数据修改原有影片的数据(根据影片编码)
	 * 
	 * @return
	 */
	public void updateFilmData(List<TFilm> list);

	/** 更新票价协议 */
	public void updateFilmSlot(List<TFilmSlot> list);

	/**
	 * 更新影片关联唯一影片
	 */
	public void updateUniqueFilm();

	/**
	 * 查询没有排次号的影片
	 * 
	 * @return
	 */
	public List<TFilm> findFilmCodeIsNull();

	/**
	 * 查询未找到匹配的下发影片 (P.S：下发影片是指上传影片文件，下发影片过程中会根据filmCode和filmName匹配系统中已存在的影片)
	 * 
	 * @return
	 */
	public List<TFilm> findNotMatch(String filmName, String... filterFilmCodes);

	/**
	 * 保存匹配排次号
	 * 
	 * @param nonCodeFilms
	 *            没有排次号影片
	 * @param matchedFilmCodes
	 *            匹配影片排次号
	 */
	public void saveMatchedFilmCode(List<TFilm> nonCodeFilms,
			Set<String> matchedFilmCodes);
}
