package com.wanda.ccs.film.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.wanda.ccs.film.dao.ITFilmCopyTrackLogDao;
import com.wanda.ccs.film.service.TFilmCopyTrackLogService;
import com.wanda.ccs.model.TFilmCopyTrackLog;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.exception.ApplicationException;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;

@Service
public class TFilmCopyTrackLogServiceImpl extends BaseCrudServiceImpl<TFilmCopyTrackLog>
		implements TFilmCopyTrackLogService {

	@Autowired
	private ITFilmCopyTrackLogDao dao = null;

	@Override
	public IBaseDao<TFilmCopyTrackLog> getDao() {
		return dao;
	}

	@Override
	public PageResult<TFilmCopyTrackLog> findByCriteria(
			QueryCriteria<String, Object> query) {
		
		String fromClause = "from TFilmCopyTrackLog c";
		String[] whereBodies = new String[] {
				"UPPER(c.tFilmCopy.copyNo) like UPPER(:copyNo)", 
				"UPPER(c.tFilm.filmName) like UPPER(:filmName)",
				"c.status = :status", 
				"(UPPER(c.fromTPublisher.publishername) like UPPER(:sendFrom) or UPPER(c.fromCinema.shortName) like UPPER(:sendFrom))",
				"(UPPER(c.toTPublisher.publishername) like UPPER(:sendTo) or UPPER(c.toCinema.shortName) like UPPER(:sendTo))",
				"c.tFilmCopy.tFilm.yearMonth  like (:yearMonth)",
				"c.sendDate >= :startSendDate ", 
				"c.sendDate <=:endSendDate",
				"c.receiveDate >= :stsrtReceiveDate",
				"c.receiveDate <= :endReceiveDate"
				};
		String joinClause = "";
		String orderClause = null;
		if (query.getSort() == null
				|| query.getSort().trim().length() == 0) {
			orderClause = "c.sendDate";//排序
		}
		PageResult<TFilmCopyTrackLog> result = getDao().queryQueryCriteria(fromClause,
				joinClause, orderClause, whereBodies, query);

		return result;
	}
	@Override
	public void deleteByFilmIdAndCopyId(Long filmId, Long copyId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("filmId", filmId);
		params.put("copyId", copyId);
		String hql = "delete from TFilmCopyTrackLog c where c.tFilm.id =:filmId and c.tFilmCopy.id = :copyId";
		
		dao.updateHql(hql, params);
	}

}
