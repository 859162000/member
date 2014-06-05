package com.wanda.ccs.mem.service;

import com.wanda.ccs.model.TExtPointRule;
import com.xcesys.extras.core.service.ICrudService;

public interface TExtPointRuleService extends ICrudService<TExtPointRule> {
	/**
	 * 检验积分特殊规则名称
	 * @param name
	 * @param ruleId
	 * @return
	 */
	public boolean checkRuleName(String name, Long ruleId);
}
