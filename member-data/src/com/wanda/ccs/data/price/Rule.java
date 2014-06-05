package com.wanda.ccs.data.price;

import java.util.ArrayList;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wanda.ccs.model.TMarketingCampaign;
import com.wanda.ccs.price.service.TMarketingCampaignService;
import com.xcesys.extras.util.SpringContextUtil;

public class Rule {
	protected final Log log = LogFactory.getLog(getClass());

	static final String DELIMITER_REGEXP = "!\\$!";
	static final String DELIMITER = "!$!";
	public static final String REVERSE_PREFIX = "~";
	EnumRuleType ruleType;
	/** 目前仅用户限制规则。reverse为false表示“不可用于”规则，为true表示“仅可用于”规则 */
	boolean reverse;
	/** 规则中的条件部份 */
	ArrayList<RuleCond> conditions;
	/** 规则中的价格部份 */
	RulePrice price;
	private Long marketingCampaignId;

	public Long getMarketingCampaignId() {
		return marketingCampaignId;
	}

	public void setMarketingCampaignId(Long marketingCampaignId) {
		this.marketingCampaignId = marketingCampaignId;
	}

	public Rule(EnumRuleType ruleType, String priceType) {
		this.ruleType = ruleType;
		conditions = new ArrayList<RuleCond>();
		if (ruleType.isHasPrice())
			price = new RulePrice(priceType);
	}

	public void setReverse(boolean reverse) {
		this.reverse = reverse;
	}

	public boolean getReverse() {
		return reverse;
	}

	public RuleCond getCondition(String type) throws Exception {
		for (RuleCond cond : conditions)
			if (cond.getType().name().equals(type)) {
				return cond;
			}
		EnumRuleCondType condType = EnumRuleCondType.fromName(type);
		if (condType == null)
			return null;
		RuleCond cond = condType.newImpl();
		conditions.add(cond);
		return cond;
	}

	public RuleCond getConditionForEdit(String type) throws Exception {
		EnumRuleCondType condType = EnumRuleCondType.fromName(type);
		if (condType == null)
			return null;
		RuleCond editCond = condType.newImpl();

		for (RuleCond cond : conditions)
			if (cond.getType().name().equals(type)) {
				editCond.parseRule(cond.genRule());
				break;
			}
		return editCond;
	}

	public void removeCondition(RuleCond cond) {
		for (RuleCond c : conditions)
			if (c.getType() == cond.getType()) {
				conditions.remove(c);
				return;
			}
	}

	public void addCondition(RuleCond cond) {
		for (RuleCond c : conditions)
			if (c.getType() == cond.getType()) {
				c.parseRule(cond.genRule());
				return;
			}

		conditions.add(cond);
	}

	public EnumRuleType getRuleType() {
		return ruleType;
	}

	public ArrayList<RuleCond> getConditions() {
		return conditions;
	}

	public RuleCond getCondition(EnumRuleCondType type) {
		for (RuleCond cond : conditions)
			if (cond.getType() == type)
				return cond;
		return null;
	}

	public RulePrice getPrice() {
		return price;
	}

	/**
	 * 根据表达式创建规则对象
	 * 
	 * @param exp
	 */
	public void parseCondRule(String exp) throws Exception {
		if (exp == null)
			return;

		if (exp.startsWith(REVERSE_PREFIX)) {
			reverse = true;
			exp = exp.substring(1);
		}

		for (String rule : exp.split(DELIMITER_REGEXP, 0)) {
			if (rule == null || rule.equals(""))
				continue;

			String type = RuleCond.getType(rule);
			if (type == null) {
				log.warn("遇到不支持的规则\"" + rule + "\"");
				continue;
			}

			RuleCond cond = getCondition(type);
			if (cond == null) {
				log.warn("遇到不支持的规则\"" + rule + "\"");
				continue;
			}

			if (!cond.parseRule(rule))
				log.warn("无法解析规则\"" + rule + "\"");
		}
	}

	/**
	 * 生成规则
	 * 
	 * @return
	 */
	public String getCondRule() {
		StringBuffer sb = new StringBuffer();

		if (reverse)
			sb.append(REVERSE_PREFIX);

		boolean bFirst = true;
		for (RuleCond cond : conditions) {
			if (!cond.hasValue())
				continue;

			if (bFirst)
				bFirst = false;
			else
				sb.append(DELIMITER);
			sb.append(cond.genRule());
		}
		return sb.toString();
	}

	/**
	 * 生成规则文字说明
	 * 
	 * @return
	 */
	public String getDesc() {
		StringBuffer sb = new StringBuffer();

		boolean bFirst = true;
		for (RuleCond cond : conditions) {
			if (!cond.hasValue())
				continue;

			if (bFirst)
				bFirst = false;
			else
				sb.append(" 并且 ");
			sb.append(cond.genDesc());
		}
		if (price != null)
			sb.append(price.getDesc());
		if (marketingCampaignId != null) {
			TMarketingCampaign campaign = SpringContextUtil.getBean(
					TMarketingCampaignService.class).getById(
					marketingCampaignId);
			sb.append("[营销活动:");
			sb.append(campaign.getCampaignName());
			sb.append("]");
		}
		return sb.toString();
	}

	/**
	 * 生成用于编辑的文字规则说明
	 * 
	 * @return
	 */
	public String getEditCondDesc(String clz, Long id) {
		StringBuffer sb = new StringBuffer();

		boolean bFirst = true;
		for (RuleCond cond : conditions) {
			if (!cond.hasValue())
				continue;

			if (bFirst)
				bFirst = false;
			else
				sb.append(" 并且 ");
			sb.append("<a href=\"#\" class=\"").append(clz).append("\" idx=\"")
					.append(id).append("\" type=\"")
					.append(cond.getType().name()).append("\">")
					.append(cond.genDesc()).append("</a>");
		}
		return sb.toString();
	}

	public boolean isEmpty() {
		for (RuleCond cond : conditions) {
			if (cond.hasValue())
				return false;
		}
		return true;
	}

	public boolean matches(Map<String, Object> params, EnumRuleCondType[] skip) {
		for (RuleCond cond : conditions) {
			boolean bSkip = false;
			if (skip != null) {
				for (EnumRuleCondType t : skip)
					if (cond.getType() == t) {
						bSkip = true;
						break;
					}
			}
			if (bSkip)
				continue;

			if (!cond.matches(params))
				return false;
		}

		return true;
	}

	/**
	 * 若没有明确定义指定的条件类型，或者指定的条件类型不符，都返回false
	 * 
	 * @param type
	 * @param params
	 * @return
	 */
	public boolean matches(EnumRuleCondType type, Map<String, Object> params) {
		for (RuleCond cond : conditions)
			if (cond.dim == type)
				return cond.matches(params);
		return false;
	}

	public void setRegionScope(Vector<String> regions) {
		for (RuleCond cond : conditions)
			if (cond instanceof RuleCondScope)
				((RuleCondScope) cond).setRegionScope(regions);
	}

	public void setCinemaScope(Vector<Long> cinemas) {
		for (RuleCond cond : conditions)
			if (cond instanceof RuleCondScope)
				((RuleCondScope) cond).setCinemaScope(cinemas);
	}
}
