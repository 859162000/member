package com.wanda.member.activity;

import java.util.List;

import com.wanda.ccs.member.segment.service.CriteriaQueryService;
import com.wanda.ccs.member.segment.service.impl.CriteriaQueryServiceImpl;
import com.wanda.ccs.sqlasm.CriteriaResult;
import com.wanda.ccs.sqlasm.expression.ExpressionCriterion;
import com.wanda.ccs.sqlasm.expression.JsonCriteriaHelper;


public class Test {

	public static void main(String[] args) {
		String criteriaJson = "[{\"inputId\":\"watchTradeCinema\",\"groupId\":\"tsale\",\"label\":\"观影影城\",\"groupLabel\":\"票房交易\",\"operator\":\"in\",\"value\":{\"selTarget\":true,\"criteria\":[{\"inputId\":\"innerName\",\"label\":\"影城内部名称\",\"operator\":\"eq\",\"value\":\"\",\"valueLabel\":\"\"},{\"inputId\":\"area\",\"label\":\"区域\",\"operator\":\"in\",\"value\":[],\"valueLabel\":\"\"},{\"inputId\":\"cityLevel\",\"label\":\"城市级别\",\"operator\":\"in\",\"value\":[],\"valueLabel\":\"\"},{\"inputId\":\"cityName\",\"label\":\"城市名称\",\"operator\":\"eq\",\"value\":\"\",\"valueLabel\":\"\"}],\"selections\":{\"value\":[\"304\",\"311\",\"333\",\"849\"],\"valueLabel\":[\"北京CBD万达广场店\",\"北京天通苑龙德广场店\",\"北京石景山万达广场店\",\"北京通州万达广场\"]}}}]";
		
		List<ExpressionCriterion> criteriaList = JsonCriteriaHelper.parse(criteriaJson);
		CriteriaQueryService service = new CriteriaQueryServiceImpl();
		//获取影票SQL
		CriteriaResult ticketCr = service.getExtPointTicketQuery(criteriaList);
		System.out.println(ticketCr.getComposedText());
	}

}
