package com.wanda.ccs.film.service.impl;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.basemgt.service.TPublisherService;
import com.wanda.ccs.film.dao.ITFilmDao;
import com.wanda.ccs.film.service.TFilmService;
import com.wanda.ccs.film.service.TFilmSlotService;
import com.wanda.ccs.model.TFilm;
import com.wanda.ccs.model.TFilmSlot;
import com.wanda.ccs.model.TPriceBase;
import com.wanda.ccs.model.TPublisher;
import com.wanda.ccs.model.UtilType;
import com.wanda.ccs.schedule.service.TScheduleInfoBService;
import com.wanda.ccs.service.SpringCommonService;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.dao.util.QLUtil;
import com.xcesys.extras.core.exception.ApplicationException;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;
import com.xcesys.extras.core.util.Assert;
import com.xcesys.extras.core.util.DateUtil;
import com.xcesys.extras.core.util.StringUtil;
import com.xcesys.extras.util.PinYinUtil;
import com.xcesys.extras.util.SecurityUtil;

@Service
public class TFilmServiceImpl extends BaseCrudServiceImpl<TFilm> implements
		TFilmService {

	@Autowired
	private ITFilmDao dao = null;

	@Autowired
	private TFilmSlotService slotService;

	@Autowired
	private TPublisherService publisherService;
	@Autowired
	private TScheduleInfoBService tScheduleInfoBService;

	@Override
	public boolean checkExisted(String code) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from TFilm c  ";
		if (!StringUtil.isNullOrBlank(code)) {
			hql += " where c.filmCode=:code";
			params.put("code", code);
		}
		int count = super.queryCountHql(hql, params, null);
		return count > 0;
	}

	@Override
	public void createOrUpdate(Collection<TFilm> oList) {
		Map<String, List<String>> settingMap = SpringCommonService
				.getAllTSettings();
		for (TFilm model : oList) {
			preSave(model, settingMap);
		}
		super.createOrUpdate(oList);
	}

	@Override
	public void createOrUpdate(TFilm model) {
		Map<String, List<String>> settingMap = SpringCommonService
				.getAllTSettings();
		preSave(model, settingMap);
		super.createOrUpdate(model);

	}

	/**
	 * 删除影片的票价协议数据
	 * 
	 * @return
	 */
	@Override
	public void deleteFilmSlot(Long[] ids) {
		for (Long id : ids) {
			String deleteClause = " delete from TFilmSlot t where t.id=:id";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("id", id);
			getDao().updateHql(deleteClause, params);
		}
	}

	@Override
	public List<TFilm> findByCodeName(String like) {
		if (!StringUtil.isNullOrBlank(like)) {
			like = like.toLowerCase();
		}
		String hql = "from TFilm c where c.deleted=:deleted and (c.pinCode like :like or c.filmName like :like) order by c.filmName";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("deleted", Boolean.FALSE);
		params.put("like", "%" + like + "%");
		PageResult<TFilm> result = super.queryHql(hql,
				QLUtil.generateCountQL(hql, null), params, 0, 100);
		return result.getContents();
	}

	@Override
	public PageResult<TFilm> findByCriteria(QueryCriteria<String, Object> query) {
		String fromClause = " select c from TFilm c left join fetch c.tPublisher p";
		String[] whereBodies = new String[] { "c.deleted = :deleted",
				"c.modified=:modified", "c.paiCiHao like :paiCiHao",
				"c.filmName like :filmName", "lower(c.pinCode) like :pinCode",
				"c.filmCate like :filmCate",
				"UPPER(p.publishername) like UPPER(:publishername)",
				"c.premiereDate>=:beginDate", "c.premiereDate<=:endDate",
				"c.yearMonth>=:ymBeginDate", "c.yearMonth<=:ymEndDate" };
		String joinClause = "";
		String orderClause = null;
		Object s_filmName = query.get("filmName");
		Object s_filmCode = query.get("paiCiHao");
		if ((s_filmName == null || "".equals(s_filmName))
				&& (s_filmCode == null || "".equals(s_filmCode))) {
			query.put("deleted", Boolean.FALSE);
		}else{
			query.put("deleted", null);
		}
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			// orderClause = "c.updatedDate desc";
		}
		PageResult<TFilm> result = getDao().queryQueryCriteria(fromClause,
				joinClause, orderClause, whereBodies, query);
		return result;
	}

	@Override
	public TFilm findByName(String filmName) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from TFilm c where c.filmName=:filmName order by c.filmCode";
		params.put("filmName", filmName);
		TFilm film = getDao().queryHqlTopOne(hql, params);
		return film;
	}

	@Override
	public TFilm findByDisplayName(String displayName) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from TFilm c where c.filmName=:displayName or c.displayName=:displayName ";
		hql += " order by c.filmName ";
		params.put("displayName", displayName);
		TFilm film = getDao().queryHqlTopOne(hql, params);
		return film;
	}

	/** 编辑不可用规则时选择影片 */
	@Override
	public Map<String, String> findLatelyFilms(String str) {
		Map<String, String> filmMap = new HashMap<String, String>();
		String hql = "from TFilm where endDate >= :filmDate or filmCode in :codes order by filmName ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("filmDate", new Date());
		String[] codes = str.split("!#!");
		params.put("codes", codes);
		List<TFilm> list = super.queryHql(hql, params);
		for (TFilm f : list) {
			filmMap.put(f.getFilmCode(), f.getFilmName());
		}
		return filmMap;
	}

	/**
	 * 查询所有的影片名称
	 */
	@Override
	public Map<Long, String> getAllFilm() {
		Map<Long, String> map = new Hashtable<Long, String>();
		TFilm tFilm = new TFilm();
		tFilm.setDeleted(false);
		List<TFilm> list = getDao().findExample(tFilm);

		if (list != null && list.size() > 0) {
			for (TFilm film : list) {
				if (film.getFilmName() != null) {
					map.put(film.getId(), film.getFilmName());
				}
			}
		}
		return map;
	}

	@Override
	public IBaseDao<TFilm> getDao() {
		return dao;
	}

	/**
	 * 读取XML文件时获取所有的影片
	 * 
	 * @author lujx
	 * @return
	 */
	@Override
	public Map<String, TFilm> getFilmList() {
		Map<String, TFilm> map = new HashMap<String, TFilm>();
		String hql = "from TFilm where deleted = :deleted";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("deleted", Boolean.FALSE);
		setQueryCacheable(true);
		List<TFilm> list = getDao().queryHql(hql, params);
		if (list != null && list.size() > 0) {
			for (TFilm film : list) {
				String filmCode = film.getFilmCode();
				String filmName = film.getFilmName();
				if (!StringUtil.isNullOrBlank(filmCode)) {
					map.put(filmCode, film);
				} else if (!StringUtil.isNullOrBlank(filmName)) {
					map.put(filmName, film);
				}
			}
		}
		return map;
	}

	@Override
	public List<TFilm> getFilmMap(Date begin, Date end, String like,Long infoHId) {
		Assert.notNull(begin);
		Assert.notNull(end);
		String hql = "from TFilm f where (f.endDate >= :begin and f.premiereDate <= :end or f.premiereDate IS NULL or f.endDate IS NULL) and f.deleted = :deleted";
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(like)) {
			params.put("like", "%" + like.toUpperCase() + "%");
			hql += " and (UPPER(f.pinCode) like :like or UPPER(f.filmName) like :like) ";
		}
		if(infoHId != null){
			hql += " and not exists(select distinct filmId from TScheduleInfoB where tScheduleInfoHID=:infoHId and filmId=f.id)";
			params.put("infoHId", infoHId);
		}
		hql += " order by filmName";
		params.put("begin", begin);
		params.put("end", end);
		params.put("deleted", Boolean.FALSE);

//		setQueryCacheable(true);
//		return super.queryHql(hql, params);
		PageResult<TFilm> result = super.queryHql(hql,
				QLUtil.generateCountQL(hql, null), params, 0, 100);
		return result.getContents();
	}

	@Override
	public int getLowestPrice(Long filmId, String area, Long cityId, Date date) {
		List<TFilmSlot> list = slotService.getLowestPrice(filmId, area, cityId,
				date, date);
		int lowestPrice = 0;
		if (list != null && !list.isEmpty()
				&& list.get(0).gettPriceBase() != null) {
			lowestPrice = list.get(0).gettPriceBase().getPrice();
		}
		if (lowestPrice <= 0) {
			TFilm film = super.getById(filmId);
			TPriceBase lp = film.getLowestPrice();
			lowestPrice = (lp == null ? 0 : lp.getPrice());
//			if (lowestPrice == 0) {
//				throw new ApplicationException("影片[" + film.getFilmName()
//						+ "]未设定最低票价.");
//			}
		}
		return lowestPrice;
	}

	/** 删除发行商时判断是否与影片关联 */
	public boolean isExistFilm(long id) {
		String hql = "from TFilm where PUBLISHER_ID =:id or PUBLISHER_ID2 =:id";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		int count = super.queryCountHql(hql, params, null);
		if (count > 0)
			return true;
		else
			return false;
	}

	private void preSave(TFilm model, Map<String, List<String>> settingMap) {
		// 保存前处理数据
		// 显示名称
		String displayName = model.getFilmName();
		String[] split = displayName.split("(\\(|（)");
		if (split != null && split.length > 0) {
			displayName = split[0];
		}
		if (StringUtil.isNullOrBlank(model.getDisplayName())) {
			model.setDisplayName(displayName);
		}

		// 拼码
		if (StringUtil.isNullOrBlank(model.getPinCode())) {
			String temp = model.getFilmName();
			if (!StringUtil.isNullOrBlank(temp)) {
				temp = temp.replaceAll("(\\(|\\)|（|）)", "");
				String pinCode = PinYinUtil.toPinYin(temp);
				model.setPinCode(pinCode);
			}
		}
		if (!StringUtil.isNullOrBlank(model.getPinCode())) {
			model.setPinCode(model.getPinCode().toLowerCase());
		}

		// 排次号与影片编码等同
		if (!StringUtil.isNullOrBlank(model.getFilmCode())) {
			model.setPaiCiHao(model.getFilmCode());
		}

		// 唯一片名
		resetUniqueFilm(model);
		// 发行商1
		Long publisherId = model.getPublisherId();
		if (publisherId != null && publisherId != 0) {
			TPublisher publisher = publisherService.getById(publisherId);
			model.settPublisher(publisher);
		}
		// 发行商2
		publisherId = model.getPublisherId2();
		if (publisherId != null && publisherId != 0) {
			TPublisher publisher = publisherService.getById(publisherId);
			model.settPublisher2(publisher);
		}

		/************** 根据放映制式，设置该影厅是否是3D、IMAX、数字等标记 *******************************/
		model.setIs3d(false);
		model.setIsDigital(false);
		model.setIsFilm(false);
		model.setIsIMax(false);

		String showSet = model.getShowSet();
		if (!StringUtil.isNullOrBlank(showSet)) {
			List<String> val = null;

			val = settingMap.get(UtilType.FILM_DIGITAL);
			if (val != null) {
				model.setIsDigital(val.contains(showSet));
			}
			val = settingMap.get(UtilType.FILM_FILM);
			if (val != null) {
				model.setIsFilm(val.contains(showSet));
			}
			val = settingMap.get(UtilType.FILM_THREE_D);
			if (val != null) {
				model.setIs3d(val.contains(showSet));
			}
			val = settingMap.get(UtilType.FILM_IMAX);
			if (val != null) {
				model.setIsIMax(val.contains(showSet));
			}
		}

	}

	@Override
	public void updateUniqueFilm() {
		String hql = "from TFilm c where c.uniqueFilmId IS NULL";
		List<TFilm> list = getDao().queryHql(hql, null);
		for (TFilm model : list) {
			resetUniqueFilm(model);
		}
		super.update(list);
	}

	private void resetUniqueFilm(TFilm model) {
		// 唯一片名
		String displayName = model.getFilmName();
		String[] split = displayName.split("(\\(|（)");
		if (split != null && split.length > 0) {
			displayName = split[0];
		}
		Long uniqueFilmId = model.getUniqueFilmId();
		if (uniqueFilmId == null && !displayName.equals(model.getFilmName())) {
			TFilm f = findByDisplayName(displayName);
			getDao().evict(f);
			if (f != null && !f.getId().equals(model.getId())) {
				uniqueFilmId = f.getId();
				model.setUniqueFilmId(uniqueFilmId);
				model.setUniqueFilm(f);
			}
			f = null;
		} else if (uniqueFilmId != null && !uniqueFilmId.equals(model.getId())) {
			TFilm uf = getById(uniqueFilmId);
			model.setUniqueFilm(uf);
		}
		if (uniqueFilmId == null || uniqueFilmId.equals(model.getId())) {
			model.setUniqueFilmId(model.getId());
			model.setUniqueFilm(model);
		}
	}

	/**
	 * 上传XML文件中的数据修改原有影片的数据(根据影片编码)
	 * 
	 * @return
	 */
	@Override
	public void updateFilmData(List<TFilm> list) {
		for (TFilm tFilm : list) {
			String updateClause = " update TFilm t set t.filmName=:filmName,t.yearMonth=:yearMonth where t.filmCode=:filmCode";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("filmName", tFilm.getFilmName());
			params.put("yearMonth", tFilm.getYearMonth());
			params.put("filmCode", tFilm.getFilmCode());
			getDao().updateHql(updateClause, params);
		}
	}

	@Override
	public void updateFilmSlot(List<TFilmSlot> list) {
		for (TFilmSlot filmSlot : list) {
			String updateClause = " update TFilmSlot t set t.tFilm=:film ";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("film", filmSlot.gettFilm());
			if (0 != filmSlot.getFilmRound()) {
				params.put("filmRound", filmSlot.getFilmRound());
				updateClause += ",t.filmRound=:filmRound";
			}
			if (!"".equals(filmSlot.getArea())) {
				params.put("area", filmSlot.getArea());
				updateClause += ",t.area=:area";
			}
			if (0 != filmSlot.getCityId()) {
				params.put("cityId", filmSlot.getCityId());
				updateClause += ",t.cityId=:cityId";
			}
			if (0 != filmSlot.getPriceBaseId()) {
				params.put("priceBaseId", filmSlot.getPriceBaseId());
				updateClause += ",t.priceBaseId=:priceBaseId";
			}
			if (null != filmSlot.getStartDate()) {
				params.put("startDate", filmSlot.getStartDate());
				updateClause += ",t.startDate=:startDate";
			}
			if (null != filmSlot.getStartDate()) {
				params.put("endDate", filmSlot.getEndDate());
				updateClause += ",t.endDate=:endDate";
			}
			params.put("slotId", filmSlot.getId());
			updateClause += " where t.id=:slotId";
			getDao().updateHql(updateClause, params);
		}
	}

	@Override
	public List<TFilm> findFilmCodeIsNull() {
		String fromClause = "from TFilm f where f.filmCode IS NULL and f.deleted = '0' order by filmName";
		return queryHql(fromClause, null);
	}

	@Override
	public List<TFilm> findNotMatch(String searchFilmName,
			String... filterFilmCodes) {
		StringBuilder hql = new StringBuilder();
		hql.append("from TFilm f").append(" where f.filmCode IS NOT NULL ")
				.append(" and f.deleted = :deleted ")
				.append(" and UPPER(f.filmName) like UPPER(:filmName) ");
		if (filterFilmCodes != null) {
			hql.append(" and f.filmCode not in (:filmCode) ");
		}
		hql.append(" order by filmName ");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("deleted", Boolean.FALSE);
		params.put("filmName", "%" + searchFilmName + "%");
		params.put("filmCode", filterFilmCodes);
		return queryHql(hql.toString(), params);
	}

	@Override
	public void saveMatchedFilmCode(List<TFilm> nonCodeFilms,
			Set<String> matchedFilmCodes) {
		//将被匹配影片删除
		if (matchedFilmCodes != null && matchedFilmCodes.size() > 0) {
			String hql = "from TFilm f where f.filmCode in (:filmCode) and f.deleted = :deleted";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("deleted", Boolean.FALSE);
			params.put("filmCode", matchedFilmCodes.toArray(new String[matchedFilmCodes.size()]));
			delete(queryHql(hql, params));
		}
		//将排次号与片名加入要匹配的影片中
		update(nonCodeFilms);
	}
	@Override
	public void delete(Collection<TFilm> list) {
		String month = DateUtil.formatDate(DateUtil.getCurrentDate(), "yyyyMM");
		String hql = "select count(*) from TScheduleInfoB b where b.tScheduleInfoH.month >= '"+month+"' and b.filmId = ";
		for (TFilm tFilm : list) {
			int count = tScheduleInfoBService.getDao().queryCountHql(hql + tFilm.getId(), null);
			if(count > 0){
				throw new ApplicationException("您所做的操作会删除"+tFilm+",但此影片已做排期表不可删除!");
			}
			tFilm.setDeleted(Boolean.TRUE);
			tFilm.setDeletedDate(DateUtil.getCurrentDate());
			tFilm.setDeletedBy(SecurityUtil.getLoginUser());
		}
		super.update(list);
	}
	
}
