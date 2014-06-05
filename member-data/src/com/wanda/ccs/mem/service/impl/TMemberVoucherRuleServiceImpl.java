package com.wanda.ccs.mem.service.impl;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.mem.dao.ITMemberVoucherRuleDao;
import com.wanda.ccs.mem.service.TMemberVoucherRuleService;
import com.wanda.ccs.model.TMemVoucherRule;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;
import com.xcesys.extras.core.util.DateUtil;
import com.xcesys.extras.core.util.StringUtil;

/**
 * 券发放管理service实现类
 * @author yaoguoqing
 *
 */
@Service
public class TMemberVoucherRuleServiceImpl extends BaseCrudServiceImpl<TMemVoucherRule> implements TMemberVoucherRuleService{
	
	@Autowired
	private ITMemberVoucherRuleDao dao;
	
	public IBaseDao<TMemVoucherRule> getDao() {
		return dao;
	}

	public void createOrUpdate(TMemVoucherRule tMemVoucherRule) {
		if(tMemVoucherRule.getId() == null)
			tMemVoucherRule.setCode(this.getVoucherRuleCode());
		super.createOrUpdate(tMemVoucherRule);
	}
	
	public void delete(Long[] ids) {
		for(int i=0; i<ids.length; i++) {
			Long id = ids[i];
			TMemVoucherRule rule = this.getDao().findById(id);
			this.getDao().remove(rule);
		}
	}

	public PageResult<TMemVoucherRule> findByCriteria(
			QueryCriteria<String, Object> query) {
		Vector<String> paramQuery = new Vector<String>();
		String hql = " select tvr from TMemVoucherRule tvr left join tvr.tSegment ts left join tvr.tVoucherPool tvp ";
		paramQuery.add(" tvr.code like :code ");
		paramQuery.add(" tvr.name like :name ");
		paramQuery.add(" tvr.status = :status ");
		String[] param = paramQuery.toArray(new String[paramQuery.size()]);
		String join = "";
		String order = "";
		PageResult<TMemVoucherRule> result = this.getDao().queryQueryCriteria(hql, join, order, param, query);
		return result;
	}

	public TMemVoucherRule findById(Long id) {
		return super.findById(id);
	}

	public TMemVoucherRule update(TMemVoucherRule tMemVoucherRule) {
		return super.update(tMemVoucherRule);
	}
	
	private String getVoucherRuleCode() {
		StringBuffer code = new StringBuffer();
		code.append("VRR").append(new SimpleDateFormat("yyyyMMdd").format(DateUtil.getCurrentDate()));
		String sql = " select max(code) MAXCODE from t_mem_voucher_rule ";
		getDao().setCacheable(false);
		List<Map<String, ?>> list = this.getDao().queryNativeSQL(sql, null);
		String maxCode = "";
		if(list != null && !list.isEmpty()) {
			Map<String, ?> map = list.get(0);
			if(map.get("MAXCODE") != null) {
				maxCode = map.get("MAXCODE").toString();
			}
		}
		if(!StringUtil.isNullOrBlank(maxCode)) {
			code.append(String.format("%04d", Long.valueOf(maxCode.substring(11))+1));
		}else {
			code.append("0001");
		}
		return code.toString();
	}
}
