package com.wanda.ccs.film.service.impl;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.film.dao.ITFilmContractDao;
import com.wanda.ccs.film.dao.ITFilmDao;
import com.wanda.ccs.film.service.TFilmContractService;
import com.wanda.ccs.model.TFcAttach;
import com.wanda.ccs.model.TFilm;
import com.wanda.ccs.model.TFilmContract;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;
import com.xcesys.extras.core.util.StringUtil;

@Service
public class TFilmContractServiceImpl extends
		BaseCrudServiceImpl<TFilmContract> implements TFilmContractService {

	@Autowired
	private ITFilmContractDao dao;
	@Autowired
	private ITFilmDao filmDao;

	@Override
	public IBaseDao<TFilmContract> getDao() {
		return dao;
	}

	@Override
	public void createOrUpdate(TFilmContract o) {
		Long filmId = o.gettFilmId();
		Long publisherId = o.getPublisherId();
		if (filmId != null && filmId.longValue() > 0) {
			TFilm film = filmDao.getById(filmId);
			film.setAccountRatioDesc(o.getAccountRatioDesc());
			if (publisherId != null && !publisherId.equals(film.getPublisherId())) {
				film.setPublisherId(publisherId);
				filmDao.update(film);
			}
		}

		dao.createOrUpdate(o);
	}

	@Override
	public PageResult<TFilmContract> findByCriteria(
			QueryCriteria<String, Object> query) {
		String fromClause = " select c from TFilmContract c left join fetch c.tFilm f left join fetch c.tPublisher p";
		String[] whereBodies = new String[] { "c.deleted=false",
				"c.tFilm.filmName like :filmName",
				"upper(c.tFilm.pinCode) like upper(:pinCode)",
				"c.tFilm.accountType = :accountType",
				"c.tFilm.country = :country", "UPPER(p.publishername) like UPPER(:publishername)",
				"c.tFilm.filmAccountType = :filmAccountType",
				"f.premiereDate >= :startPremiereDate",
				"f.premiereDate <= :endPremiereDate"};
		String joinClause = "";
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = "c.contractNo desc";
		}
		PageResult<TFilmContract> result = getDao().queryQueryCriteria(
				fromClause, joinClause, orderClause, whereBodies, query);
		return result;
	}

	@Override
	public boolean checkExisted(String contractNo) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from TFilmContract c where c.deleted = false";
		if (!StringUtil.isNullOrBlank(contractNo)) {
			hql += " and c.contractNo=:contractNo";
			params.put("contractNo", contractNo);
		} else {
			return false;
		}
		int count = super.queryCountHql(hql, params, null);
		return count > 0;
	}
	
	@Override
	public void delete(Collection<TFilmContract> list) {
		super.delete(list);
		for (TFilmContract crt : list) {
			for (TFcAttach atth : crt.gettFcAttachs()) {
				String path = atth.getAttachFilePath();
				File file = new File(path);
				if(file.exists()){
					file.delete();
				}
			}
		}
	}
	
	@Override
	public void delete(TFilmContract o) {
		super.delete(o);
	}
	
	

}
