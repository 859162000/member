package com.wanda.ccs.mem.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import com.wanda.ccs.mem.service.TSegmentService;
import com.wanda.ccs.model.TSegment;
import org.springframework.beans.factory.annotation.Autowired;

import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;
import com.xcesys.extras.core.util.StringUtil;
import com.wanda.ccs.mem.dao.ITSegmentDao;

@Service
public class TSegmentServiceImpl extends BaseCrudServiceImpl<TSegment>
		implements TSegmentService {

	@Autowired
	private ITSegmentDao dao = null;

	@Override
	public IBaseDao<TSegment> getDao() {
		return dao;
	}

	@Override
	public PageResult<TSegment> findByCriteria(
			QueryCriteria<String, Object> query) {
		String fromClause = "from TSegment c";
		String[] whereBodies = new String[] {
				"c.name like :name",
				"c.code like :code",
				query.get("region") == null ? "c.ownerRegion is null" : "c.ownerRegion = :region",
				query.get("cinemaId") == null ? "c.ownerCinema is null" : "c.ownerCinema = :cinemaId"
		};
		String joinClause = "";
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = "c.id desc";
		}

		PageResult<TSegment> result = getDao().queryQueryCriteria(fromClause,
				joinClause, orderClause, whereBodies, query);
		return result;

	}
	
	@Override
	public Long getCalCountById(Long segmentId) {
		if(segmentId == null)
			return null;
		String sql = "select CAL_COUNT from T_SEGMENT s where s.SEGMENT_ID = :id";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", segmentId);
		
		getDao().setCacheable(false);
		
		List<Map<String,?>> list = getDao().queryNativeSQL(sql, params);
		if (list != null
				&& !list.isEmpty()
				&& list.get(0) != null
				&& !list.get(0).isEmpty()
				&& list.get(0).get("CAL_COUNT") != null
				&& !StringUtil.isNullOrBlank(list.get(0).get("CAL_COUNT")
						.toString())) {
			return Long.parseLong(list.get(0).get("CAL_COUNT").toString());
		}
		return null;
	}
}
