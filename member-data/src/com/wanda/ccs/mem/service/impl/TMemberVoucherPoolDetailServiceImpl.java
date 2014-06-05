package com.wanda.ccs.mem.service.impl;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.mem.dao.ITMemberVoucherPoolDetailDao;
import com.wanda.ccs.mem.service.TMemberVoucherPoolDetailService;
import com.wanda.ccs.model.TVoucherPoolDetail;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;

@Service
public class TMemberVoucherPoolDetailServiceImpl extends BaseCrudServiceImpl<TVoucherPoolDetail> implements TMemberVoucherPoolDetailService {
	
	@Autowired
	private ITMemberVoucherPoolDetailDao dao;

	@Override
	public IBaseDao<TVoucherPoolDetail> getDao() {
		return dao;
	}
	
	
	public List<TVoucherPoolDetail> findTicketAll() {
		String sql = " select * from t_voucher_pool_detail tpd join  t_voucher_pool tvp on (tpd.voucher_pool_id = tvp.voucher_pool_id) join t_member t on (tpd.member_id = t.member_id)";
		List<TVoucherPoolDetail> list = this.getDao().queryNativeSQL(TVoucherPoolDetail.class, sql, null);
		return list;
	}
	
	@SuppressWarnings("all")
	public PageResult<TVoucherPoolDetail> findByCriteria(
			QueryCriteria<String, Object> query, Map<String, Object> map) {
		query.setPageSize(10);
		StringBuffer buffer = new StringBuffer();
		buffer.append("	select tpd.VOUCHER_POOL_DETAIL_ID as VOUCHER_POOL_DETAIL_ID, tpd.VOUCHER_POOL_ID as VOUCHER_POOL_ID, tm.MEMBER_ID as MEMBER_ID, ");
		buffer.append(" tm.NAME as NAME, tm.MOBILE as MOBILE, tv.EXPIRY_DATE as EXPIRY_DATE, ");
		buffer.append(" tv.SEQUENCE_IN_ORDER as SEQUENCE_IN_ORDER, vorder.ORDER_NUM as ORDER_NUM, type.TYPE_NAME as TYPE_NAME, ");
		buffer.append(" type.USE_TYPE as USE_TYPE ");
		buffer.append(" from t_voucher_pool_detail tpd ");
		buffer.append(" left join t_member tm on (tpd.MEMBER_ID = tm.MEMBER_ID) ");
		buffer.append(" left join t_voucher tv on (tpd.bar_code = tv.bar_code) ");
		buffer.append(" left join t_voucher_type type on (tv.VOUCHER_TYPE_ID = type.VOUCHER_TYPE_ID) ");
		buffer.append(" left join t_voucher_order vorder on (type.VOUCHER_TYPE_ID = vorder.VOUCHER_TYPE_ID) where VOUCHER_POOL_ID = " + query.get("voucherPoolId"));
		String sqlCount = " select count(*) from (" + buffer.toString() + ")";
		this.setQueryCacheable(false);
		PageResult result = this.getDao().queryNativeSQL(buffer.toString(), sqlCount, map, query.getStartIndex(), query.getPageSize());
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public PageResult<TVoucherPoolDetail> findByCriteria(
			QueryCriteria<String, Object> query) {
		query.setPageSize(10);
		StringBuffer buffer = new StringBuffer();
		buffer.append("	select tv.VOUCHER_NUMBER as VOUCHER_NUMBER ,tpd.VOUCHER_POOL_DETAIL_ID AS DETAILID ,type.TYPE_NAME AS NAME , poo.POOL_TYPE AS POOL_TYPE, tv.EXPIRY_DATE AS EXPIRY_DATE,tv.CREATE_DATE AS CREATE_DATE,type.USE_TYPE AS USE_TYPE");
		buffer.append("  from t_voucher_pool_detail tpd ");
		buffer.append(" left join t_voucher tv on (tpd.bar_code = tv.bar_code) left join t_voucher_type type on (tv.VOUCHER_TYPE_ID = type.VOUCHER_TYPE_ID) join T_VOUCHER_POOL poo on (poo.VOUCHER_POOL_ID = tpd.VOUCHER_POOL_ID) where tpd.member_id = " + query.get("memberId") + " and tpd.OPERRATE_TYPE = 2");
		String sqlCount = " select count(*) from (" + buffer.toString() + ")";
		this.setQueryCacheable(false);
		PageResult result = this.getDao().queryNativeSQL(buffer.toString(), sqlCount, null, query.getStartIndex(), query.getPageSize());
		return result;

	}
}
