package com.wanda.ccs.film.dao.impl;


import org.springframework.stereotype.Repository;

import com.wanda.ccs.film.dao.ITFilmContractDao;
import com.wanda.ccs.model.TFilmContract;
import com.xcesys.extras.core.dao.impl.BaseDaoImpl;

@Repository
public class TFilmContractDaoImpl extends BaseDaoImpl<TFilmContract> implements
		ITFilmContractDao {

}

