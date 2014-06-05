package com.wanda.ccs.film.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.film.dao.ITFilmCopyDao;
import com.wanda.ccs.film.service.TFilmCopyService;
import com.wanda.ccs.model.TFilmCopy;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;
import com.xcesys.extras.core.util.StringUtil;

@Service
public class TFilmCopyServiceImpl extends BaseCrudServiceImpl<TFilmCopy>
		implements TFilmCopyService {

	@Autowired
	private ITFilmCopyDao dao = null;

	@Override
	public IBaseDao<TFilmCopy> getDao() {
		return dao;
	}
	@Override
	public PageResult<TFilmCopy> findByCriteria(
			QueryCriteria<String, Object> query) {
		String fromClause = " select c from TFilmCopy c";
		String[] whereBodies = new String[] { 
				"UPPER(c.tFilm.filmName) like UPPER(:filmName)",
				"UPPER(c.tFilm.tPublisher.publishername) like UPPER(:publishername)", };
		String joinClause = "";
		String orderClause = null;

		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = "c.id desc";
		}

		PageResult<TFilmCopy> result = getDao().queryQueryCriteria(fromClause,
				joinClause, orderClause, whereBodies, query);
		return result;
	}

	/**
	 * 通过拷贝编号，获得拷贝信息
	 */
	@Override
	public TFilmCopy getByCopyNoAndFilmId(String copyNo, Long filmId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from TFilmCopy c where c.copyNo = :copyNo and c.filmId = :filmId order by c.id desc";
		if (StringUtil.isNullOrBlank(copyNo) || filmId == null) {
			return null;
		}
		params.put("copyNo", copyNo);
		params.put("filmId", filmId);

		List<TFilmCopy> l = getDao().queryHql(hql, params);
		if (l.size() > 0) {
			return l.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 通过拷贝Id，获得是否有外键关联
	 */
	@Override
	public TFilmCopy checkFKFromFilmCopy(Long[] ids) {
		for (int i = 0; i < ids.length; i++) {
			if (super.findById(ids[i]).gettFilmCopySchedules().size() > 0) {
				return super.getById(ids[i]);
			}
		}
		return null;
	}

	@Override
	public List<TFilmCopy> findByFilmId(Long filmId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from TFilmCopy c where c.filmId = :filmId order by c.id desc";
		params.put("filmId", filmId);
		return getDao().queryHql(hql, params);
	}
}
