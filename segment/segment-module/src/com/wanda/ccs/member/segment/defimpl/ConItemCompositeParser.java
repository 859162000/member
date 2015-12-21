package com.wanda.ccs.member.segment.defimpl;

//import static com.wanda.ccs.member.segment.SegmentConstants.Schemas.RPT2;
import static com.wanda.ccs.member.segment.SegmentConstants.Schemas.CCSDW;
import static com.wanda.ccs.sqlasm.CriteriaParserBuilder.SELECT_PARAGRAPHS;
import static com.wanda.ccs.sqlasm.CriteriaParserBuilder.newParser;
import static com.wanda.ccs.sqlasm.CriteriaParserBuilder.notEmpty;
import static com.wanda.ccs.sqlasm.DataType.STRING;
import static com.wanda.ccs.sqlasm.DataType.LONG;
import static com.wanda.ccs.sqlasm.expression.ExpressionClauseBuilder.newExpression;
import static com.wanda.ccs.sqlasm.expression.ExpressionClauseBuilder.newPlain;

import java.util.List;

import com.wanda.ccs.sqlasm.CriteriaResult;
import com.wanda.ccs.sqlasm.Criterion;
import com.wanda.ccs.sqlasm.expression.CompositeCriteriaParser;

public class ConItemCompositeParser extends CompositeCriteriaParser {
	
	public <C extends Criterion> CriteriaResult parse(List<C> criteria) {
		if(this.parser == null) {
			this.parser = newParser(SELECT_PARAGRAPHS)
					.add(newPlain().output("I.ITEM_CODE").in("select"))
//					.add(newPlain().output(RPT2+".T_D_CON_CS_SALE_ITEM I").in("from"))
					.add(newPlain().output(CCSDW+".V_DW_D_SALE_ITEM I").in("from"))
					
					.add(notEmpty("itemCode"), newExpression().output("I.ITEM_CODE", STRING).in("where"))
					.add(notEmpty("itemName"), newExpression().output("I.ITEM_NAME", STRING).in("where"))
					.add(notEmpty("conCategoryId"), newExpression().output("C.ITEM_CLASS_ID", LONG).in("where")
//							.depends(newPlain().output(RPT2+".T_D_CON_CS_CLASS C").in("from"))
							.depends(newPlain().output(CCSDW+".T_DW_D_SALE_CLASS C").in("from"))
							.depends(newPlain().output("C.ITEM_CLASS_ID=I.ITEM_CLASS_ID").in("where")))
					.add(notEmpty("unit"), newExpression().output("I.UNIT_CODE", STRING).in("where"));

//			Clause[] defaultClause = {
//				new SqlClause("select", "I.ITEM_CODE", true),
//				new SqlClause("from", RPT2+".T_D_CON_CS_SALE_ITEM I", true)
//			};
//
//			Map<String, Clause> clauseMap = new LinkedHashMap<String, Clause>();
//			clauseMap.put("itemCode", new ValueClause("where", "I.ITEM_CODE", DataType.STRING, false));
//			clauseMap.put("itemName", new ValueClause("where", "I.ITEM_NAME", DataType.STRING, false));
//			clauseMap.put("conCategoryId", new ValueClause("where", "C.THIRD_CLASS_ID", DataType.LONG, false)
//					.depends(new SqlClause("from", RPT2+".T_D_CON_CS_CLASS C", true))
//					.depends(new SqlClause("where", "C.SALE_CLASS_KEY=I.SALE_CLASS_KEY", true)));
//			clauseMap.put("unit", new ValueClause("where", "I.UNIT_CODE", DataType.STRING, false));
//
//			this.parser = new DefaultCriteriaParser(clauseGroups, defaultClause, clauseMap, true);
		}
		return parser.parse(criteria);
	}
	
}
