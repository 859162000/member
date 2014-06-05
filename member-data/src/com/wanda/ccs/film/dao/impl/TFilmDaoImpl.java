package com.wanda.ccs.film.dao.impl;

import org.springframework.stereotype.Repository;

import com.wanda.ccs.film.dao.ITFilmDao;
import com.wanda.ccs.model.TFilm;
import com.xcesys.extras.core.dao.impl.BaseDaoImpl;

@Repository
public class TFilmDaoImpl extends BaseDaoImpl<TFilm> implements ITFilmDao {

}
