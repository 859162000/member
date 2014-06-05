package com.wanda.ccs.film.dao.impl;


import org.springframework.stereotype.Repository;

import com.wanda.ccs.film.dao.ITFilmCopyDao;
import com.wanda.ccs.model.TFilmCopy;
import com.xcesys.extras.core.dao.impl.BaseDaoImpl;

@Repository
public class TFilmCopyDaoImpl extends BaseDaoImpl<TFilmCopy> implements
		ITFilmCopyDao {

}

