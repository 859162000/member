package com.wanda.ccs.schedule.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.model.PlanTransData;
import com.wanda.ccs.model.TSchedulePlanB;
import com.wanda.ccs.schedule.dao.ITSchedulePlanBDao;
import com.wanda.ccs.schedule.dao.ITTaskOutRealTimeDao;
import com.wanda.ccs.schedule.service.TSchedulePlanBService;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.dao.util.QLUtil;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;

@Service
public class TSchedulePlanBServiceImpl extends
		BaseCrudServiceImpl<TSchedulePlanB> implements TSchedulePlanBService {

	@Autowired
	private ITSchedulePlanBDao dao = null;
	@Autowired
	private ITTaskOutRealTimeDao taskOutRealTimeDao;
	
	@Override
	public IBaseDao<TSchedulePlanB> getDao() {
		return dao;
	}

	@Override
	public List<TSchedulePlanB> findByPlanHId(Long planHId,
			String... orderClauses) {
		String from = " from TSchedulePlanB c left join fetch c.header h where h.id =:planHId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("planHId", planHId);
		super.setQueryCacheable(true);
		// List<TSchedulePlanB> planBs = getDao().queryHql(from, params);
		// TSchedulePlanH planH = null;
		// if(planB != null && planB.size()>=0){
		// for(TSchedulePlanB b : planB){
		// planH = b.getHeader();
		// }
		// }
		String orderClause = StringUtils.join(orderClauses, ',');
		if (orderClause == null) {
			orderClause = "c.hallName, c.startTime";
		}

		String hql = QLUtil.generateQL(from, null, orderClause, null, null,
				null, params);
		return getDao().queryHql(hql, params);
	}
	@Override
	public TSchedulePlanB update(TSchedulePlanB o) {
		if(o.getPublished()!=null && o.getPublished()){
			taskOutRealTimeDao.create(o.getHeader().gettCinema().getCode());
		}
		return super.update(o);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Boolean getBroadcastById(Long id){
		Boolean broadcast = false;
		String from = "select c.broadcast from TSchedulePlanB c where c.id = "+id;
		List list  = getDao().queryHql(from, null);
		
		Object obj = list.get(0);
		if(obj != null && obj instanceof Boolean){
			broadcast = (Boolean)obj;
		}
		
		return broadcast;
	}
	
	public PlanTransData getTransData(Long id){
		PlanTransData t = new PlanTransData();
		String from = "select new com.wanda.ccs.model.PlanTransData(c.broadcast,c.tranSts,c.tranFn,c.tranMsg,c.tranTime) from TSchedulePlanB c where c.id = "+id;
		List<?> list  = getDao().queryHql(from, null);
		if(!list.isEmpty()){
			t = (PlanTransData) list.get(0);
		}
		return t;
	}
}
