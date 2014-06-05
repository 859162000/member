package com.wanda.member.activity.processor;

import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.batch.item.ItemProcessor;

import com.wanda.ccs.member.segment.service.CriteriaQueryService;
import com.wanda.ccs.member.segment.service.impl.CriteriaQueryServiceImpl;
import com.wanda.ccs.sqlasm.CriteriaResult;
import com.wanda.ccs.sqlasm.expression.ExpressionCriterion;
import com.wanda.ccs.sqlasm.expression.JsonCriteriaHelper;
import com.wanda.member.activity.conts.RuleTypeConts;
import com.wanda.member.activity.model.ActivityPointRule;
import com.wanda.member.activity.model.ActivityPointRuleSql;

public class RuleToSqlProcessor implements ItemProcessor<ActivityPointRule, ActivityPointRuleSql> {
	private String bizDate;
	private String cinemaInnerCode;
	private String memberId;
	private CriteriaQueryService cqService = new CriteriaQueryServiceImpl();
	
	@Override
	public ActivityPointRuleSql process(ActivityPointRule item)
			throws Exception {
		String ruleType = item.getRuleType();
		ActivityPointRuleSql ruleSql = new ActivityPointRuleSql();
		BeanUtils.copyProperties(ruleSql, item);
		String criteriaJson = item.getCriteriaScheme();
		List<ExpressionCriterion> criteriaList = JsonCriteriaHelper.parse(criteriaJson);
		//会员类规则
		if(RuleTypeConts.MEMBER_RULE.equals(ruleType)){
			
			//获取会员查询SQL
			CriteriaResult memberCr = cqService.getExtPointMemberQuery(criteriaList);
			if(null != memberCr){
				StringBuffer memberSql = new StringBuffer(memberCr.getComposedText());
				if(!StringUtils.isEmpty(this.getMemberId())){
					memberSql.append(" and member.MEMBER_KEY = ? ");
				}
				memberSql.append(" group by member.MEMBER_KEY");
				ruleSql.setSqlStr(memberSql.toString());

				List<Object> paramList = memberCr.getParameters();
				if(!StringUtils.isEmpty(this.getMemberId())){
					paramList.add(this.getMemberId());
				}
				ruleSql.setSqlParams(paramList);
			}
		}
		
		//影票类规则
		if(RuleTypeConts.TICKET_RULE.equals(ruleType)){
			//获取影票查询SQL
			CriteriaResult ticketCr = cqService.getExtPointTicketQuery(criteriaList);
			if(null != ticketCr){
				StringBuffer ticketSql = new StringBuffer(ticketCr.getComposedText());
				ticketSql.append(" and transSales.SHOW_BIZ_DATE_KEY = to_date(?, 'yyyy-mm-dd') ");
				if(!StringUtils.isEmpty(this.getMemberId())){
					ticketSql.append(" and member.MEMBER_KEY = ? ");
				}
				if(!StringUtils.isEmpty(this.getCinemaInnerCode())){
					ticketSql.append(" and transSales_cinema.INNER_CODE = ? ");
				}
				ticketSql.append(" group by ");
				ticketSql.append(" transSales_cinema.INNER_CODE, ");
				ticketSql.append(" NVL (transSales.BK_TICKET_NUMBER, transSales.re_TICKET_NUMBER), ");
				ticketSql.append(" NVL (transSales.BK_CT_ORDER_CODE, transSales.RE_CT_ORDER_CODE), ");
				ticketSql.append(" MEMBER.MEMBER_KEY, ");
				ticketSql.append(" transSales.SHOW_BIZ_DATE_KEY ");
				ruleSql.setSqlStr(ticketSql.toString());

				List<Object> paramList = ticketCr.getParameters();
				paramList.add(bizDate);
				if(!StringUtils.isEmpty(this.getMemberId())){
					paramList.add(this.getMemberId());
				}
				if(!StringUtils.isEmpty(this.getCinemaInnerCode())){
					paramList.add(this.getCinemaInnerCode());
				}
				ruleSql.setSqlParams(paramList);
			}
		}
		
		//卖品类规则
		if(RuleTypeConts.GOODS_RULE.equals(ruleType)){
			//获取卖品查询SQL
			CriteriaResult goodsCr = cqService.getExtPointConSaleQuery(criteriaList);
			if(null != goodsCr){
				StringBuffer goodsSql = new StringBuffer(goodsCr.getComposedText());
				goodsSql.append(" and consale.BOOK_BIZ_DATE_KEY = to_date(?, 'yyyy-mm-dd') ");
				if(!StringUtils.isEmpty(this.getMemberId())){
					goodsSql.append(" and member.MEMBER_KEY = ? ");
				}
				if(!StringUtils.isEmpty(this.getCinemaInnerCode())){
					goodsSql.append(" and consale_cinema.INNER_CODE = ? ");
				}
				goodsSql.append(" group by ");
				goodsSql.append(" consale_cinema.INNER_CODE, ");
				goodsSql.append(" member.MEMBER_KEY, ");
				goodsSql.append(" nvl(consale.BK_CS_ORDER_CODE,consale.RE_CS_ORDER_CODE), ");
				goodsSql.append(" consale_item.ITEM_CODE, ");
				goodsSql.append(" consale.BOOK_BIZ_DATE_KEY ");
				ruleSql.setSqlStr(goodsSql.toString());

				List<Object> paramList = goodsCr.getParameters();
				paramList.add(bizDate);
				if(!StringUtils.isEmpty(this.getMemberId())){
					paramList.add(this.getMemberId());
				}
				if(!StringUtils.isEmpty(this.getCinemaInnerCode())){
					paramList.add(this.getCinemaInnerCode());
				}
				ruleSql.setSqlParams(paramList);
			}
		}
		
		return ruleSql;
	}

	public String getBizDate() {
		return bizDate;
	}

	public void setBizDate(String bizDate) {
		this.bizDate = bizDate;
	}

	public String getCinemaInnerCode() {
		return cinemaInnerCode;
	}

	public void setCinemaInnerCode(String cinemaInnerCode) {
		this.cinemaInnerCode = cinemaInnerCode;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public CriteriaQueryService getCqService() {
		return cqService;
	}

	public void setCqService(CriteriaQueryService cqService) {
		this.cqService = cqService;
	}
}
