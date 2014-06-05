package com.wanda.ccs.film.dao.impl;

import org.springframework.stereotype.Repository;

import com.wanda.ccs.film.dao.ITFilmSlotDao;
import com.wanda.ccs.model.TFilmSlot;
import com.xcesys.extras.core.dao.impl.BaseDaoImpl;

@Repository
public class TFilmSlotDaoImpl extends BaseDaoImpl<TFilmSlot> implements
		ITFilmSlotDao {

}
