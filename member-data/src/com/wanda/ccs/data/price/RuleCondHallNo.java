package com.wanda.ccs.data.price;

import java.util.LinkedHashMap;
import java.util.Map;

import com.wanda.ccs.basemgt.service.THallService;
import com.xcesys.extras.util.SpringContextUtil;

public class RuleCondHallNo extends RuleCondList{
	private final static String NAMED_HALLNO = "号厅";
	Map<String, String> HALLMAP;
	
	public RuleCondHallNo(EnumRuleCondType dim) {
		super(dim);
	}

	public Map<String, String> getList() {
		if (HALLMAP == null) {
			HALLMAP = new LinkedHashMap<String, String>();

			/*Vector<Long> ids = new Vector<Long>();
			for (String id : set)
				ids.add(Long.parseLong(id));*/
			
			for (String hallNo: SpringContextUtil.getBean(
					THallService.class).findAllHallNo())
				HALLMAP.put(hallNo,hallNo + NAMED_HALLNO);
		}

		return HALLMAP;
	}

	@Override
	Object getEveluateParam(Map<String, Object> params) {
		return params.get(RuleParamTypes.HALL_NO);
	}
	public boolean isNotAvailable() {
		return HALLMAP == null || HALLMAP.size() == 0;
	}
}
