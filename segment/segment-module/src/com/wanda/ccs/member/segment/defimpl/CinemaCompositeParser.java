package com.wanda.ccs.member.segment.defimpl;

import static com.wanda.ccs.member.segment.SegmentConstants.Schemas.RPT2;
import static com.wanda.ccs.member.segment.SegmentConstants.Schemas.CCSDW;
import static com.wanda.ccs.sqlasm.CriteriaParserBuilder.SELECT_PARAGRAPHS;
import static com.wanda.ccs.sqlasm.CriteriaParserBuilder.newParser;
import static com.wanda.ccs.sqlasm.CriteriaParserBuilder.notEmpty;
import static com.wanda.ccs.sqlasm.DataType.STRING;
import static com.wanda.ccs.sqlasm.expression.ExpressionClauseBuilder.newExpression;
import static com.wanda.ccs.sqlasm.expression.ExpressionClauseBuilder.newPlain;

import java.util.List;

import com.wanda.ccs.sqlasm.CriteriaResult;
import com.wanda.ccs.sqlasm.Criterion;
import com.wanda.ccs.sqlasm.expression.CompositeCriteriaParser;


public class CinemaCompositeParser extends CompositeCriteriaParser {
	
	public <C extends Criterion> CriteriaResult parse(List<C> criteria) {
		if(this.parser == null) {
			this.parser = newParser(SELECT_PARAGRAPHS)
				.add(newPlain().output("C.INNER_CODE").in("select"))
//				.add(newPlain().output(RPT2+".T_D_CON_CINEMA C").in("from"))
				.add(newPlain().output(CCSDW+".T_DW_D_CINEMA C").in("from"))//切换数据用户T_D_CON_CINEMA--》T_DW_D_CINEMA
				.add(notEmpty("innerName"), newExpression().output("C.INNER_NAME", STRING).in("where"))
				.add(notEmpty("area"), newExpression().output("C.AREA_ID", STRING).in("where"))
				.add(notEmpty("cityLevel"), newExpression().output("C.CITY_LEVEL_CODE", STRING).in("where"))//CITY_LEVEL_id-->CITY_LEVEL_CODE
				.add(notEmpty("cityName"), newExpression().output("C.CITY_NAME", STRING).in("where"));
		}
		return parser.parse(criteria);
	}

}
