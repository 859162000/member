package com.wanda.ccs.mem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.mem.dao.ITMemFavFilmTypeDao;
import com.wanda.ccs.mem.service.TMemberFavFilmTypeService;
import com.wanda.ccs.model.TMemFavFilmtype;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;

@Service
public class TMemFavFilmTypeServiceImpl extends BaseCrudServiceImpl<TMemFavFilmtype>
		implements TMemberFavFilmTypeService {

	@Autowired
	private ITMemFavFilmTypeDao dao;

	@Override
	public IBaseDao<TMemFavFilmtype> getDao() {
		return dao;
	}
}
