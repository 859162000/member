package com.wanda.ccs.member.segment.service.impl;

import static com.wanda.ccs.sqlasm.CriteriaParserBuilder.SELECT_PARAGRAPHS;
import static com.wanda.ccs.sqlasm.CriteriaParserBuilder.equalsValue;
import static com.wanda.ccs.sqlasm.CriteriaParserBuilder.newParser;
import static com.wanda.ccs.sqlasm.CriteriaParserBuilder.notEmpty;
import static com.wanda.ccs.sqlasm.expression.ExpressionClauseBuilder.newExpression;
import static com.wanda.ccs.sqlasm.expression.ExpressionClauseBuilder.newPlain;
import static com.wanda.ccs.sqlasm.expression.ExpressionClauseBuilder.newValue;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.transaction.annotation.Transactional;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.google.code.pathlet.jdbc.EntityInsertDef;
import com.google.code.pathlet.jdbc.EntityRowMapper;
import com.google.code.pathlet.jdbc.EntityUpdateDef;
import com.google.code.pathlet.jdbc.ExtJdbcTemplate;
import com.google.code.pathlet.util.ValueUtils;
import com.google.code.pathlet.vo.QueryParamVo;
import com.google.code.pathlet.vo.QueryResultVo;
import com.wanda.ccs.jobhub.client.JobScheduleService;
import com.wanda.ccs.member.ap2in.UserProfile;
import com.wanda.ccs.member.segment.AuditJdbcTemplate;
import com.wanda.ccs.member.segment.service.CriteriaQueryResult;
import com.wanda.ccs.member.segment.service.SensitiveService;
import com.wanda.ccs.member.segment.vo.SensitiveWordVo;
import com.wanda.ccs.sqlasm.Clause;
import com.wanda.ccs.sqlasm.Condition;
import com.wanda.ccs.sqlasm.CriteriaParseException;
import com.wanda.ccs.sqlasm.CriteriaParser;
import com.wanda.ccs.sqlasm.CriteriaResult;
import com.wanda.ccs.sqlasm.DataType;
import com.wanda.ccs.sqlasm.expression.ArrayExpCriterion;
import com.wanda.ccs.sqlasm.expression.ExpressionCriterion;
import com.wanda.ccs.sqlasm.expression.JsonCriteriaHelper;
import com.wanda.ccs.sqlasm.expression.Operator;
import com.wanda.ccs.sqlasm.expression.SingleExpCriterion;

public class SensitiveServiceImpl implements SensitiveService {
private ExtJdbcTemplate queryTemplate = null;
	
	private AuditJdbcTemplate auditJdbcTemplate = null;
	
	@InstanceIn(path = "/dataSource")
	public void setDataSource(DataSource dataSource) {
		queryTemplate = new ExtJdbcTemplate(dataSource);
		
		auditJdbcTemplate = new AuditJdbcTemplate(dataSource, "T_MEMBER_SENSITIVE");
		auditJdbcTemplate.registerInsertEntity(new EntityInsertDef("insert", SensitiveWordVo.class, "T_MEMBER_SENSITIVE", null, new String[] {"remarks"}));
		auditJdbcTemplate.registerUpdateEntity(new EntityUpdateDef(
				"update", SensitiveWordVo.class, "T_MEMBER_SENSITIVE",
				new String[] { "wordId" }, new String[] { "wordId", 
						 "createBy", "createDate", "status", "issueRegion",
						"issueCinema","isdelete"}));
		auditJdbcTemplate.registerDelete("delete", "DELETE from T_MEMBER_SENSITIVE where word_id= ? ");
		auditJdbcTemplate.registerLogicDelete("logicDelete", "update T_MEMBER_SENSITIVE set ISDELETE='1' where word_id=?");
	}

	
	@InstanceIn(path = "/jobScheduleService")
	JobScheduleService scheduleService;
	
	private ExtJdbcTemplate getJdbcTemplate() {
		return this.queryTemplate;
	}
	
	private AuditJdbcTemplate getAuditTemplate() {
		return this.auditJdbcTemplate;
	}

	@Override
	public void insert(SensitiveWordVo sensitive) {
		Long wordId = getJdbcTemplate().queryForLong(
				"select S_T_MEMBER_SENSITIVE.NEXTVAL from DUAL");
		//String wordId = getSegmentCode();
		sensitive.setWordId(wordId);
		sensitive.setWordTitle(sensitive.getWordTitle());
		sensitive.setWordContent(sensitive.getWordContent());
		sensitive.setCreateDate(new Timestamp(System.currentTimeMillis()));
		sensitive.setUpdateDate(new Timestamp(System.currentTimeMillis()));
		sensitive.setIsdelete("0");
		sensitive.setStatus("0");
		getAuditTemplate().insertEntity("insert", sensitive);
	}
	
	private String getSegmentCode() {
		StringBuffer code = new StringBuffer();
		code.append("SWD").append(
				new SimpleDateFormat("yyyyMMdd").format(new Date(System
						.currentTimeMillis())));
		String sql = "select max(CODE) as MAXCODE from T_SEGMENT s where s.CODE like ?";

		List<Map<String, Object>> list = getJdbcTemplate().queryForList(sql,
				code.toString() + "%");

		String maxCode = "";
		if (list != null && !list.isEmpty()) {
			Map<String, Object> map = list.get(0);
			if (map.get("MAXCODE") != null)
				maxCode = map.get("MAXCODE").toString();
		}
		if (ValueUtils.notEmpty(maxCode)) {
			code.append(String.format("%04d",
					Long.valueOf(maxCode.substring(11)) + 1));
		} else {
			code.append("0001");
		}
		return code.toString();
	}

	@Override
		public QueryResultVo<Map<String, Object>> queryList (
				QueryParamVo queryParam, List<ExpressionCriterion> criteria, UserProfile userinfo) {
		criteria.add(new SingleExpCriterion("userLevel", userinfo.getLevel().name()));
		criteria.add(new ArrayExpCriterion("userInfo",
				new String[] {userinfo.getId(), Long.toString(userinfo.getCinemaId()), userinfo.getRegionCode()}));

		criteria.add(new ArrayExpCriterion("orderby", null, null, null, 
				new String[]{queryParam.getSortName(), queryParam.getSortOrder()}));
		
		Clause segmentTable = newPlain().in("from").output("T_MEMBER_SENSITIVE s");
		
		Map<Condition, Clause> clauseMap = new LinkedHashMap<Condition, Clause>();
		
		clauseMap.put(notEmpty("wordId"), newExpression().in("where").output("s.word_id", DataType.STRING, Operator.EQUAL));
		clauseMap.put(notEmpty("wordTitle"), newExpression().in("where").output("s.word_title", DataType.STRING, Operator.LIKE));
		clauseMap.put(notEmpty("createBy"), newExpression().in("where").output("s.CREATE_BY", DataType.STRING, Operator.EQUAL));
		
		clauseMap.put(equalsValue("userLevel", "REGION"), newValue().from("userInfo").in("where")
				.output("(s.issue_region='REGION' or s.issue_region='CINEMA') ", DataType.STRING, false));
		clauseMap.put(notEmpty("orderby"), newValue().in("orderby").output("s.{0} {1}", DataType.SQL, true));

		CriteriaParser countParser = newParser(SELECT_PARAGRAPHS)
			.add(newPlain().in("select").output("count(s.word_id)"))
			.add(newPlain().in("where").output("s.ISDELETE='0'"))
			.add(segmentTable)
			.add(clauseMap);
		
		CriteriaResult countResult = countParser.parse(criteria);
		
		CriteriaParser listParser = newParser(SELECT_PARAGRAPHS)
				.add(newPlain().in("select").output("s.word_id, s.word_title, s.word_content,s.STATUS, s.CREATE_BY, s.UPDATE_BY, s.UPDATE_DATE"))
				.add(newPlain().in("where").output("s.ISDELETE='0'"))
				.add(segmentTable)
				.add(clauseMap);

		CriteriaResult listResult = listParser.parse(criteria);

		logSql(countResult.getComposedText(), listResult.getComposedText(), listResult.getParameters());

		Long rowCount = getJdbcTemplate().queryForLong(
				countResult.getComposedText(),
				countResult.getParameters().toArray());
		
		List<Map<String, Object>> listQueryResult = getJdbcTemplate().query(
				queryParam.getStartIndex(), queryParam.getFetchSize(),
				listResult.getComposedText(), listResult.getParameters().toArray(),
				new ColumnMapRowMapper());

		return new QueryResultVo<Map<String, Object>>(rowCount, listQueryResult);
	}
	private void logSql(String countSql, String listSql,
			List<Object> paramemters) {
		 System.out.println("CountSQL:" + countSql);
		 System.out.println("ListSQL:" + listSql);
		 System.out.println("ParametersCount:" + paramemters.size());
		 System.out.println(paramemters);
	}

	public SensitiveWordVo get(Long wordId) {
		SensitiveWordVo segment = (SensitiveWordVo) getJdbcTemplate().queryForObject(
				"select * from T_MEMBER_SENSITIVE where word_id=?",
				new Object[] { wordId },
				new EntityRowMapper<SensitiveWordVo>(SensitiveWordVo.class));
		return segment;
	}
	@Transactional
	public void update(SensitiveWordVo sensitive) {
		sensitive.setUpdateDate(new Timestamp(System.currentTimeMillis()));
		getAuditTemplate().updateEntity("update", sensitive);
		//getJdbcTemplate().update("update T_MEMBER_SENSITIVE t set t.control_count=?, t.control_count_rate=? where t.segment_id=?", new Object[] {segment.getControlCount(), segment.getControlCountRate(), segment.getSegmentId()});
	}
	
	@Transactional
	public void logicDelete(long  wordId) {
			getAuditTemplate().logicDeleteEntity("logicDelete", wordId);
	}
	
	public CriteriaQueryResult getSegmentQuery(long wordId)
			throws CriteriaParseException {
		List<ExpressionCriterion> criteria = JsonCriteriaHelper.parse(String.valueOf(wordId));
		CriteriaParser countParser = newParser(SELECT_PARAGRAPHS);
		
		CriteriaResult result = countParser.parse(criteria);
		return (CriteriaQueryResult) criteria;
	}
	public boolean hasSameName(String segmentName, Long selfSegmentId) {
		Long sameNameCount;

		if (selfSegmentId != null) {
			sameNameCount = getJdbcTemplate()
					.queryForLong(
							"select count(WORD_ID) from T_MEMBER_SENSITIVE where WORD_TITLE=? and WORD_ID<>? and ISDELETE='0'",
							segmentName, selfSegmentId);
		} else {
			sameNameCount = getJdbcTemplate().queryForLong(
					"select count(WORD_ID) from T_MEMBER_SENSITIVE where WORD_TITLE=? and ISDELETE='0'",
					segmentName);
		}

		if (sameNameCount != null && sameNameCount > 0) {
			return true;
		} else {
			return false;
		}
	}

}
