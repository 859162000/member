package com.wanda.ccs.schedule.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.model.TRoundTicketType;
import com.wanda.ccs.model.TSchedulePlanB;
import com.wanda.ccs.schedule.dao.ITRoundTicketTypeDao;
import com.wanda.ccs.schedule.service.TRoundTicketTypeService;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;

/**
 * 
 */
@Service
public class TRoundTicketTypeServiceImpl extends
		BaseCrudServiceImpl<TRoundTicketType> implements
		TRoundTicketTypeService {

	@Autowired
	private ITRoundTicketTypeDao dao = null;

	@Override
	public IBaseDao<TRoundTicketType> getDao() {
		return dao;
	}

	@Override
	public void deleteByTicketTypeAndPlanB(Long ticketTypeId, Long planBId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tid", ticketTypeId);
		params.put("pid", planBId);
		getDao().updateHql(
				"delete from TRoundTicketType c where c.tTicketType.id = :tid and c.tSchedulePlanB.id = :pid",
				params);
	}

	@Override
	public TRoundTicketType findByTicketTypeAndPlanB(Long ticketTypeId,
			Long planBId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tid", ticketTypeId);
		params.put("pid", planBId);
		List<TRoundTicketType> lst = getDao()
				.queryHql(
						"from TRoundTicketType c where c.tTicketType.id = :tid and c.tSchedulePlanB.id = :pid",
						params);
		if (lst == null || lst.size() == 0)
			return null;
		return lst.get(0);
	}

	@Override
	public boolean checkRoundTickettype(Long ticketTypeId) {
		if(ticketTypeId == null)
			return false;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tid", ticketTypeId);
		String hql = "select count(*)  from TRoundTicketType c where c.tTicketType.id = :tid";
		if( getDao().queryCountHql(hql, params) > 0)
			return true;
		return false;
	}
	@Override
	public List<TRoundTicketType> findAndOrderByMyCinemaSelfDefining(TSchedulePlanB planB){
		StringBuilder hql = new StringBuilder();
		hql.append("select r from TRoundTicketType r left join fetch r.tTicketType t left join t.tTicketTypeCinemas c");
		hql.append(" where r.tSchedulePlanB.id = :planBId ");
		hql.append(" and r.tSchedulePlanB.header.cinemaId = :cinemaId ");
		hql.append(" and c.cinemaId = :cinemaId ");
		hql.append(" order by c.orderInCinema asc");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("planBId", planB.getId());
		params.put("cinemaId",planB.getHeader().getCinemaId());
		
		setQueryCacheable(true);
		getDao().setRegion("findAndOrderByMyCinemaSelfDefining");
		return getDao().queryHql(hql.toString(), params);
	}
	@Override
	public void evictSecendCache(){
		dao.evictSecendCache();
	}
}
