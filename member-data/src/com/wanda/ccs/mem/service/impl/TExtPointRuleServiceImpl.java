package com.wanda.ccs.mem.service.impl;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.mem.dao.ITExtPointRuleDao;
import com.wanda.ccs.mem.service.TExtPointRuleService;
import com.wanda.ccs.model.TExtPointRule;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;
import com.xcesys.extras.core.util.DateUtil;
import com.xcesys.extras.core.util.StringUtil;

@Service
public class TExtPointRuleServiceImpl extends
		BaseCrudServiceImpl<TExtPointRule> implements TExtPointRuleService {

	@Autowired
	private ITExtPointRuleDao dao = null;

	@Override
	public IBaseDao<TExtPointRule> getDao() {
		return dao;
	}

	@Override
	public PageResult<TExtPointRule> findByCriteria(
			QueryCriteria<String, Object> query) {
		String fromClause = "from TExtPointRule c";
		String[] whereBodies = new String[] {
				"c.code like :code",
				"c.name like :name",
				"c.startDtime >= to_date(:sStartDate,'yyyy-mm-dd')",
				"c.startDtime < to_date(:eStartDate,'yyyy-mm-dd')+1",
				"c.endDtime >= to_date(:sEndDate,'yyyy-mm-dd')",
				"c.endDtime < to_date(:eEndDate,'yyyy-mm-dd') + 1",
				"c.status = :status",
				"c.status in (:inStatus)"
		};
		String joinClause = "";
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = "c.id desc";
		}

		PageResult<TExtPointRule> result = getDao().queryQueryCriteria(
				fromClause, joinClause, orderClause, whereBodies, query);
		return result;

	}
	
	@Override
	public void createOrUpdate(TExtPointRule tExtPointRule) {
		if(tExtPointRule.getId() == null)
			tExtPointRule.setCode(this.getExtPointCode());
		super.createOrUpdate(tExtPointRule);
	}
	
	private String getExtPointCode() {
		StringBuffer code = new StringBuffer();
		code.append("PTR").append(new SimpleDateFormat("yyyyMMdd").format(DateUtil.getCurrentDate()));
		String sql = " select max(code) MAXCODE from t_ext_point_rule ";
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

	@Override
	public boolean checkRuleName(String name, Long ruleId) {
		if(StringUtil.isNullOrBlank(name))
			return false;
		StringBuffer hql = new StringBuffer("select c from TExtPointRule c where c.name = :name ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", name);
		if(ruleId != null){
			hql.append(" and c.id != :id");
			params.put("id", ruleId);
		}
		return super.queryCountHql(hql.toString(), params, null) > 0;
	}
}
