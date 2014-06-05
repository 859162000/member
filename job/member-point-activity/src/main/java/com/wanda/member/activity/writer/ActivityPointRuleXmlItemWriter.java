package com.wanda.member.activity.writer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.batch.item.ItemWriter;
import org.springframework.util.CollectionUtils;

import com.wanda.ccs.member.segment.defimpl.ExtPointCriteriaDef;
import com.wanda.ccs.member.segment.service.CriteriaQueryService;
import com.wanda.ccs.member.segment.service.impl.CriteriaQueryServiceImpl;
import com.wanda.ccs.sqlasm.CriteriaResult;
import com.wanda.ccs.sqlasm.expression.ExpressionCriterion;
import com.wanda.ccs.sqlasm.expression.JsonCriteriaHelper;
import com.wanda.member.activity.conts.RuleTypeConts;
import com.wanda.member.activity.model.ActivityPointRule;

public class ActivityPointRuleXmlItemWriter implements ItemWriter<ActivityPointRule> {
	
	private ItemWriter<ActivityPointRule> memberRuleWriter;
	private ItemWriter<ActivityPointRule> ticketRuleWriter;
	private ItemWriter<ActivityPointRule> goodsRuleWriter;
	
	@Override
	public void write(List<? extends ActivityPointRule> items) throws Exception {
		
		List<ActivityPointRule> memberRuleList = new ArrayList<ActivityPointRule>();
		List<ActivityPointRule> ticketRuleList = new ArrayList<ActivityPointRule>();
		List<ActivityPointRule> goodsRuleList = new ArrayList<ActivityPointRule>();
		
		for (ActivityPointRule activityPointRule : items) {
			
			String criteriaJson = activityPointRule.getCriteriaScheme();
			
			List<ExpressionCriterion> criteriaList = JsonCriteriaHelper.parse(criteriaJson);
			Set<String> groupIds = this.getGroupIdSet(criteriaList);
			
			if(this.isMemberRule(groupIds)){
				// TODO 获取会员查询SQL
				ActivityPointRule memberRule = new ActivityPointRule();
				BeanUtils.copyProperties(memberRule, activityPointRule);
				memberRule.setRuleType(RuleTypeConts.MEMBER_RULE);
				memberRuleList.add(memberRule);
			}else{
				CriteriaQueryService service = new CriteriaQueryServiceImpl();
				//获取影票SQL
				CriteriaResult ticketCr = service.getExtPointTicketQuery(criteriaList);
				if(null != ticketCr){
					ActivityPointRule ticketRule = new ActivityPointRule();
					BeanUtils.copyProperties(ticketRule, activityPointRule);
					ticketRule.setRuleType(RuleTypeConts.TICKET_RULE);
					ticketRuleList.add(ticketRule);
				}
				
				//获取卖品SQL
				CriteriaResult goodsCr = service.getExtPointConSaleQuery(criteriaList);
				if(null != goodsCr){
					ActivityPointRule goodsRule = new ActivityPointRule();
					BeanUtils.copyProperties(goodsRule, activityPointRule);
					goodsRule.setRuleType(RuleTypeConts.GOODS_RULE);
					goodsRuleList.add(goodsRule);
				}
			}
		}
		
		if(!CollectionUtils.isEmpty(memberRuleList)){
			memberRuleWriter.write(memberRuleList);
		}
		if(!CollectionUtils.isEmpty(ticketRuleList)){
			ticketRuleWriter.write(ticketRuleList);
		}
		if(!CollectionUtils.isEmpty(goodsRuleList)){
			goodsRuleWriter.write(goodsRuleList);
		}
	}
	
	private Set<String> getGroupIdSet(List<ExpressionCriterion> criteria) {
		HashSet<String> groupIds = new HashSet<String>();
		
		for(ExpressionCriterion cri : criteria) {
			String groupId = cri.getGroupId();
			if(groupIds.contains(groupId) == false) {
				groupIds.add(groupId);
			}
		}
		
		return groupIds;
	}
	
	private boolean isMemberRule(Set<String> groupIds){
		boolean isMemberRule = false;
		//只有会员基本条件
		if(groupIds.contains(ExtPointCriteriaDef.GROUP_ID_MEMBER) 
				&& (!groupIds.contains(ExtPointCriteriaDef.GROUP_ID_CON_ITEM) 
						&& !groupIds.contains(ExtPointCriteriaDef.GROUP_ID_TICKET))
						){
			isMemberRule = true;
			
		}
		return isMemberRule;
	}

	public ItemWriter<ActivityPointRule> getMemberRuleWriter() {
		return memberRuleWriter;
	}

	public void setMemberRuleWriter(ItemWriter<ActivityPointRule> memberRuleWriter) {
		this.memberRuleWriter = memberRuleWriter;
	}

	public ItemWriter<ActivityPointRule> getTicketRuleWriter() {
		return ticketRuleWriter;
	}

	public void setTicketRuleWriter(ItemWriter<ActivityPointRule> ticketRuleWriter) {
		this.ticketRuleWriter = ticketRuleWriter;
	}

	public ItemWriter<ActivityPointRule> getGoodsRuleWriter() {
		return goodsRuleWriter;
	}

	public void setGoodsRuleWriter(ItemWriter<ActivityPointRule> goodsRuleWriter) {
		this.goodsRuleWriter = goodsRuleWriter;
	}

}
