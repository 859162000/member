package com.wanda.ccs.member.segment.defimpl;

import static com.wanda.ccs.member.segment.SegmentConstants.Schemas.RPT2;
import static com.wanda.ccs.sqlasm.CriteriaParserBuilder.SELECT_PARAGRAPHS;
import static com.wanda.ccs.sqlasm.CriteriaParserBuilder.newParser;
import static com.wanda.ccs.sqlasm.CriteriaParserBuilder.notEmpty;
import static com.wanda.ccs.sqlasm.DataType.STRING;
import static com.wanda.ccs.sqlasm.expression.ExpressionClauseBuilder.newExpression;
import static com.wanda.ccs.sqlasm.expression.ExpressionClauseBuilder.newPlain;

import java.util.List;

import com.wanda.ccs.sqlasm.Clause;
import com.wanda.ccs.sqlasm.CriteriaResult;
import com.wanda.ccs.sqlasm.Criterion;
import com.wanda.ccs.sqlasm.DataType;
import com.wanda.ccs.sqlasm.expression.CompositeCriteriaParser;

public class ActivityCompositeParser extends CompositeCriteriaParser {
	
	public <C extends Criterion> CriteriaResult parse(List<C> criteria) {
		if(this.parser == null) {
			// 主表 活动波次
			Clause cmnActivity = newPlain().in("from").output(RPT2+".T_CMN_ACTIVITY activity");
			// 活动
			Clause campaignJoin = newPlain().in("from").output(RPT2+".T_CAMPAIGN campaign")
					.depends(newPlain().in("where").output("campaign.CAMPAIGN_ID=activity.CAMPAIGN_ID"))
					.depends(cmnActivity);
			// 波次阶段
			Clause cmnPhaseJoin = newPlain().in("from").output(RPT2+".T_CMN_PHASE phase")
					.depends(newPlain().in("where").output("activity.CMN_PHAISE_ID=phase.CMN_PHAISE_ID"))
					.depends(cmnActivity);
			// 波次目标
			Clause targetJoin = newPlain().in("from").output(RPT2+".T_ACT_TARGET target")
					.depends(newPlain().in("where").output("activity.ACT_TARGET_ID=target.ACT_TARGET_ID"))
					.depends(cmnActivity);
			// 客户群目标
			Clause segmentJoin = newPlain().in("from").output(RPT2+".T_SEGMENT segment")
					.depends(newPlain().in("where").output("target.SEGMENT_ID=segment.SEGMENT_ID"))
					.depends(targetJoin);
			
			this.parser = newParser(SELECT_PARAGRAPHS)
					.add(newPlain().in("select").output("activity.CMN_ACTIVITY_ID"))
					// 波次名称
					.add(notEmpty("cmnName"), newExpression().in("where").output("activity.NAME", DataType.STRING)
							.depends(cmnActivity))
					// 波次开始时间
					.add(notEmpty("cmnStartDate"), newExpression().in("where").output("activity.START_DTIME", DataType.DATE)
							.depends(cmnActivity))
					// 波次结束时间
					.add(notEmpty("cmnEndDate"), newExpression().in("where").output("activity.END_DTIME", DataType.DATE)
							.depends(cmnActivity))
					// 目标客群名称
					.add(notEmpty("sengentName"), newExpression().in("where").output("segment.NAME", DataType.STRING)
							.depends(segmentJoin))
					// 阶段名称
					.add(notEmpty("phaseName"), newExpression().in("where").output("phase.NAME", DataType.STRING)
							.depends(cmnPhaseJoin))
					// 活动名称
					.add(notEmpty("campaignName"), newExpression().in("where").output("campaign.NAME", DataType.STRING)
							.depends(campaignJoin));
		}
		return parser.parse(criteria);
	}

}
