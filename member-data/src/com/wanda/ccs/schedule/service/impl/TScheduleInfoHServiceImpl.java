package com.wanda.ccs.schedule.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.model.ScheduleInfoVer;
import com.wanda.ccs.model.TFilm;
import com.wanda.ccs.model.TScheduleInfoB;
import com.wanda.ccs.model.TScheduleInfoH;
import com.wanda.ccs.schedule.dao.ITScheduleInfoHDao;
import com.wanda.ccs.schedule.service.TScheduleInfoHService;
import com.wanda.ccs.service.impl.BaseStateUVServiceImpl;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.exception.ApplicationException;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.util.StringUtil;

/**
 * 排片信息
 * 
 * @author Benjamin
 * @date 2011-11-17
 */
@Service
public class TScheduleInfoHServiceImpl extends
		BaseStateUVServiceImpl<TScheduleInfoH> implements TScheduleInfoHService {

	@Autowired
	private ITScheduleInfoHDao dao = null;

	@Override
	public void approve(Long id) {
		TScheduleInfoH h = findById(id);
		h.setEditable(Boolean.TRUE);
		super.approve(id);
		if (!ScheduleInfoVer.FIRST.getOrdinal().equals(h.getVer())) {
			String month = h.getMonth();
			String ver = ScheduleInfoVer.fromOrdinal(h.getVer()).prev()
					.getOrdinal();
			;
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("month", month);
			params.put("ver", ver);
			String hql = "from TScheduleInfoH h where h.month=:month and h.ver=:ver";
			TScheduleInfoH prev = getDao().queryHqlTopOne(hql, params);
			if (prev != null)
				prev.setEditable(Boolean.FALSE);
		}
	}

	@Override
	public void cancelApprove(Long id) {
		TScheduleInfoH h = findById(id);
		h.setEditable(Boolean.TRUE);
		super.cancelApprove(id);
	}

	@Override
	public void cancelSubmit(Long id) {
		TScheduleInfoH h = findById(id);
		h.setEditable(Boolean.TRUE);
		super.cancelSubmit(id);
	}

	@Override
	public boolean checkExisted(String month, String ver) {

		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from TScheduleInfoH h where ";
		if (!StringUtil.isNullOrBlank(month) && !StringUtil.isNullOrBlank(ver)) {
			hql += " h.month=:month and h.ver=:ver";
			params.put("month", month);
			params.put("ver", ver);
		} else {
			return false;
		}
		int count = super.queryCountHql(hql, params, null);
		return count > 0;
	}

	@Override
	public PageResult<TScheduleInfoH> findByCriteria(
			QueryCriteria<String, Object> criteria) {
		String fromClause = " select c from TScheduleInfoH c";
		String[] whereBodies = new String[] { "c.month like :month",
				"c.status in (:status)", "c.ver=:ver" };
		String joinClause = "";
		String orderClause = null;
		if (criteria.getSort() == null
				|| criteria.getSort().trim().length() == 0) {
			orderClause = "c.month desc,c.ver desc,c.status asc";
		}
		PageResult<TScheduleInfoH> result = getDao().queryQueryCriteria(
				fromClause, joinClause, orderClause, whereBodies, criteria);
		return result;
	}

	@Override
	public IBaseDao<TScheduleInfoH> getDao() {
		return dao;
	}

	@Override
	public TScheduleInfoH getLatestInfoH(String month) {
		String hql = "";
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtil.isNullOrBlank(month)) {
			hql = "from TScheduleInfoH c order by c.month desc, c.ver desc";
		} else {
			hql = "from TScheduleInfoH c where c.month = :month order by c.ver desc";
			params.put("month", month);
		}
		return getDao().queryHqlTopOne(hql, params);
	}

	@Override
	public void saveFilmSchAndMayUpdateDigitalFilmCopyCount(
			TScheduleInfoH infoH, TFilm film, int copyCount) {

		boolean isfilm = film.getIsFilm() == null ? Boolean.FALSE : film
				.getIsFilm();
		boolean isimax = film.getIsIMax() == null ? Boolean.FALSE : film
				.getIsIMax();
		boolean isDigital = film.getIsDigital() == null ? Boolean.FALSE : film
				.getIsDigital();
		if ((isfilm || isimax) && !isDigital
				&& film.getCopyCount().intValue() < copyCount) {
			throw new ApplicationException("胶片或IMAX影片拷贝数不足,影片拷贝数为："
					+ film.getCopyCount() + ",要排片的影城数为：" + copyCount);
		}
		if (isDigital) {
			film.setCopyCount(Integer.valueOf(copyCount).longValue());
		}
		super.update(infoH);
	}

	/**
	 * 1.9版本之前验证影片必须指定发行商与上映日期
	 */
	@Override
	public void submit(Long id) {
		TScheduleInfoH h = findById(id);
		h.setEditable(Boolean.FALSE);
		super.submit(id);
	}

	@Override
	public void syncFilmDate(Map<String, List<TScheduleInfoB>> syncInfoBs,
			String ver) {
		StringBuilder countHql = new StringBuilder(
				"from TScheduleInfoB b left join b.tScheduleInfoH h where h.month =:month and b.cinemaId=:cinemaId and b.filmId=:filmId");
		Map<String, Object> parameters = new HashMap<String, Object>();
		String insertHal = "from TScheduleInfoH where month=:month";
		// 非修正稿
		if (!ScheduleInfoVer.REVISED.getOrdinal().equals(ver)) {
			countHql.append(" and b.tScheduleInfoH.ver > :ver");
			parameters.put("ver", ver);
			insertHal += " and ver > :ver";
		}
		String updateHql = "update TScheduleInfoB b set b.endDate=:endDate where b.tScheduleInfoHID in (select h.id from TScheduleInfoH h where h.month=:month) and b.cinemaId=:cinemaId and b.filmId=:filmId";
		String nm_endDate = "endDate", nm_month = "month", nm_cinemaId = "cinemaId", nm_filmId = "filmId";
		Map<String, Object> updateParams = new HashMap<String, Object>();
		for (String month : syncInfoBs.keySet()) {
			parameters.put(nm_month, month);
			for (TScheduleInfoB b : syncInfoBs.get(month)) {
				parameters.put(nm_cinemaId, b.getCinemaId());
				parameters.put(nm_filmId, b.getFilmId());
				int count = super.queryCountHql(countHql.toString(),
						parameters, null);
				if (count > 0) {
					// 更新
					updateParams.put(nm_endDate, b.getEndDate());
					updateParams.put(nm_month, month);
					updateParams.put(nm_cinemaId, b.getCinemaId());
					updateParams.put(nm_filmId, b.getFilmId());
					getDao().updateHql(updateHql, updateParams);
					updateParams.clear();
				} else {
					// 新增
					List<TScheduleInfoH> toInfoHs = getDao().queryHql(
							insertHal, parameters);
					for (TScheduleInfoH h : toInfoHs) {
						TScheduleInfoB n = new TScheduleInfoB();
						BeanUtils.copyProperties(b, n);
						n.setId(null);
						n.settScheduleInfoH(h);
						n.settScheduleInfoHID(h.getId());
						h.gettScheduleInfoBs().add(n);
					}
				}
			}
		}
	}
}
