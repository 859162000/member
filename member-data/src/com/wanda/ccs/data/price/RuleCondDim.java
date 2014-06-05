package com.wanda.ccs.data.price;

import java.util.Map;

import com.wanda.ccs.service.SpringCommonService;

public class RuleCondDim extends RuleCondList {
	public RuleCondDim(EnumRuleCondType dim) {
		super(dim);
	}

	public Map<String, String> getList() {
		return SpringCommonService.getDimDefByTypeId(dim.dimType);
	}
}
