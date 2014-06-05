package com.wanda.ccs.member.segment.defimpl;

import static com.wanda.ccs.member.segment.SegmentConstants.Schemas.MBRODS;
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

public class FilmCompositeParser extends CompositeCriteriaParser {
	
	public <C extends Criterion> CriteriaResult parse(List<C> criteria) {
		if(this.parser == null) {
			this.parser = newParser(SELECT_PARAGRAPHS)
					.add(newPlain().in("select").output("F.FILM_CODE"))
					.add(newPlain().in("from").output(MBRODS+".T_FILM F"))
					
					.add(notEmpty("filmName"), newExpression().in("where").output("F.FILM_NAME", STRING))
					.add(notEmpty("showSet"), newExpression().in("where").output("F.SHOW_SET", STRING))
					.add(notEmpty("filmTypes"), newExpression().in("where").output("FT.TYPE", STRING)
							.depends(newPlain().in("from").output(MBRODS+".T_FT_TYPE FT"))
							.depends(newPlain().in("where").output("FT.FILM_ID=F.SEQID")))
					.add(notEmpty("filmCate"), newExpression().in("where").output("F.FILM_CATE", STRING))
					.add(notEmpty("country"), newExpression().in("where").output("F.COUNTRY", STRING))
					.add(notEmpty("director"), newExpression().in("where").output("F.DIRECTOR", STRING))
					.add(notEmpty("mainActors"), newExpression().in("where").output("F.MAIN_ACTORS", STRING));
			
			/*
			Clause[] defaultClause = {
					new SqlClause("select", "F.FILM_CODE", true),
					new SqlClause("from", MBRODS+".T_FILM F", true) //注意，由于报表库中的影片信息不全，这里使用总部应用数据库中的T_FILM表, 而不是报表库中的表
				};
			
			Map<String, Clause> clauseMap = new LinkedHashMap<String, Clause>();
			clauseMap.put("filmName", new ValueClause("where", "F.FILM_NAME", DataType.STRING, false));
			clauseMap.put("showSet", new ValueClause("where", "F.SHOW_SET", DataType.STRING, false));
			clauseMap.put("filmTypes", new ValueClause("where", "FT.TYPE", DataType.STRING, false)					
					.depends(new SqlClause("from", MBRODS+".T_FT_TYPE FT", true))
					.depends(new SqlClause("where", "FT.FILM_ID=F.SEQID", true)));
			clauseMap.put("filmCate", new ValueClause("where", "F.FILM_CATE", DataType.STRING, false));
			clauseMap.put("country", new ValueClause("where", "F.COUNTRY", DataType.STRING, false));
			clauseMap.put("director", new ValueClause("where", "F.DIRECTOR", DataType.STRING, false));
			clauseMap.put("mainActors", new ValueClause("where", "F.MAIN_ACTORS", DataType.STRING, false));

			this.parser = new DefaultCriteriaParser(clauseGroups, defaultClause, clauseMap, true);
			*/
		}
		return parser.parse(criteria);
	}

}
