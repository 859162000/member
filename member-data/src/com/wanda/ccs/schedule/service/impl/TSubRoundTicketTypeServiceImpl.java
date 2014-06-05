package com.wanda.ccs.schedule.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.model.TRoundFilm;
import com.wanda.ccs.model.TSubRoundTicketType;
import com.wanda.ccs.schedule.dao.ITSubRoundTicketTypeDao;
import com.wanda.ccs.schedule.service.TSubRoundTicketTypeService;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;

@Service
public class TSubRoundTicketTypeServiceImpl extends
		BaseCrudServiceImpl<TSubRoundTicketType> implements
		TSubRoundTicketTypeService {

	@Autowired
	private ITSubRoundTicketTypeDao dao = null;

	@Override
	public IBaseDao<TSubRoundTicketType> getDao() {
		return dao;
	}

	@Override
	public List<TSubRoundTicketType> findAndOrderByMyCinemaSelfDefining(TRoundFilm rf){
		StringBuilder hql = new StringBuilder();
		hql.append("select r from TSubRoundTicketType r left join fetch r.tTicketType t left join t.tTicketTypeCinemas c");
		hql.append(" where r.tRoundFilm.id = :rfId ");
		hql.append(" and r.tRoundFilm.schedulePlanB.header.cinemaId = :cinemaId ");
		hql.append(" and c.cinemaId = :cinemaId ");
		hql.append(" order by c.orderInCinema asc");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("rfId", rf.getId());
		params.put("cinemaId",rf.getSchedulePlanB().getHeader().getCinemaId());
		
		setQueryCacheable(true);
		getDao().setRegion("findAndOrderByMyCinemaSelfDefining");
		return getDao().queryHql(hql.toString(), params);
	}
	
	@Override
	public int[] getTicketTypeCountAndPrice(long planBId , long ticketTypeId){
		int[] cp = new int[2];
		StringBuilder hql = new StringBuilder();
		hql.append("select count(r.id),sum(r.adjustPrice.price) from TSubRoundTicketType r")
			.append(" where r.tTicketType.id = :ticketTypeId and r.tRoundFilm.schedulePlanB.id = :planBId");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("planBId", planBId);
		params.put("ticketTypeId",ticketTypeId);
		
		List<?> list = getDao().queryHql(hql.toString(), params);
		if(list != null && !list.isEmpty()){
			Object[] obj = (Object[])list.get(0);
			cp[0] = NumberUtils.toInt("" + obj[0],0);
			cp[1] = NumberUtils.toInt("" + obj[1],0);
		}
		return cp;
	}
}
