package com.wanda.ccs.mem.service.impl;

import java.util.Map;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.mem.dao.ITMemberVoucherPoolDao;
import com.wanda.ccs.mem.service.TMemberVoucherPoolService;
import com.wanda.ccs.model.TVoucherPool;
import com.wanda.ccs.model.TVoucherPoolDetail;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;

/**
 * 券库管理实现类
 * @author yaoguoqing
 *
 */
@Service
public class TMemberVoucherPoolServiceImpl extends BaseCrudServiceImpl<TVoucherPool> implements TMemberVoucherPoolService{
	
	@Autowired
	private ITMemberVoucherPoolDao dao;
	
	public IBaseDao<TVoucherPool> getDao() {
		return dao;
	}

	@Override
	public void createOrUpdate(TVoucherPool tVoucherPool) {
		super.createOrUpdate(tVoucherPool);
	}

	@Override
	public void delete(Long[] ids) {
		for(int i=0; i<ids.length; i++) {
			Long id = ids[i];
			TVoucherPool pool = this.getDao().findById(id);
			this.getDao().remove(pool);
		}
	}

	@Override
	public TVoucherPool findById(Long id) {
		return this.getDao().findById(id);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public PageResult<TVoucherPool> findByCriteria(
			QueryCriteria<String, Object> query) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" select t.voucher_pool_id as voucher_pool_id , t.name as name, t.send_lock as send_lock, ");
		buffer.append(" (select count(d.voucher_pool_detail_id) from T_VOUCHER_POOL_DETAIL d where d.voucher_pool_id = t.voucher_pool_id and d.member_id is not null) as surplus_num, ");
		buffer.append(" (select count(d.voucher_pool_detail_id) from T_VOUCHER_POOL_DETAIL d where d.voucher_pool_id = t.voucher_pool_id and d.member_id is null) as grant_num ");
		buffer.append(" from t_voucher_pool t where 1 = 1");
		if(query.get("id") != null && !"".equals(query.get("id"))) {
			buffer.append(" and t.voucher_pool_id like '%" + query.get("id") + "%'");
		}
		if(query.get("name") != null && !"".equals(query.get("name"))) {
			buffer.append(" and t.name like '%" + query.get("name") + "%'");
		}
		String sqlCount = " select count(*) from (" + buffer.toString() + ")";
		this.setQueryCacheable(false);
		PageResult result = this.getDao().queryNativeSQL(buffer.toString(), sqlCount, null, query.getStartIndex(), query.getPageSize());
		return result;
	}
}
