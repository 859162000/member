package com.wanda.member.activity.partitioner;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.util.CollectionUtils;

import com.wanda.member.activity.conts.RuleTypeConts;
import com.wanda.member.activity.model.ActivityPointRule;

public class GoodsRuleSqlPartitioner implements Partitioner {
	private static final Log logger = LogFactory.getLog(GoodsRuleSqlPartitioner.class);
	private ResourceAwareItemReaderItemStream<List<ActivityPointRule>> rulePartitionReader;
	
	@Override
	public Map<String, ExecutionContext> partition(int gridSize) {
		
		Map<String, ExecutionContext> results = new LinkedHashMap<String, ExecutionContext>();
		List<ActivityPointRule> ruleList = null;
		try {
			rulePartitionReader.open(new ExecutionContext());
			ruleList = rulePartitionReader.read();
			rulePartitionReader.close();
		} catch (UnexpectedInputException e) {
			logger.error("Unexpected Input For Goods Rule XML.", e);
		} catch (ParseException e) {
			logger.error("Parse Error For Goods Rule XML.", e);
		} catch (NonTransientResourceException e) {
			logger.error("Non Transient Resource For Goods Rule XML.", e);
		} catch (Exception e) {
			logger.error("Read Goods Rule XML Error.", e);
		}
		
		if(!CollectionUtils.isEmpty(ruleList)){
			for (int index = 0; index < ruleList.size(); index++) {
				ActivityPointRule rule = ruleList.get(index);
				ExecutionContext context = new ExecutionContext();
				context.put("sql_index", index);
				context.put("rule_suffix", rule.getExtPointRuleId() +"_"+ rule.getExtPointCriteriaId());
				context.put("point_addition_percent", rule.getAdditionPercent());
				context.put("point_addition_code", rule.getAdditionCode());
				context.put("ext_point_rule_id", rule.getExtPointRuleId());
				context.put("ext_point_criteria_id", rule.getExtPointCriteriaId());
				results.put("partition."+RuleTypeConts.GOODS_RULE+"."+index, context);
			}
		}
		
		return results;
	}
	public ResourceAwareItemReaderItemStream<List<ActivityPointRule>> getRulePartitionReader() {
		return rulePartitionReader;
	}
	public void setRulePartitionReader(
			ResourceAwareItemReaderItemStream<List<ActivityPointRule>> rulePartitionReader) {
		this.rulePartitionReader = rulePartitionReader;
	}
}
