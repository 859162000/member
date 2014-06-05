package com.wanda.ccs.member.segment.service.impl;

import static com.wanda.ccs.sqlasm.CriteriaParserBuilder.*;
import static com.wanda.ccs.sqlasm.expression.ExpressionClauseBuilder.*;

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
import com.wanda.ccs.member.ap2in.UserProfile;
import com.wanda.ccs.member.segment.AuditJdbcTemplate;
import com.wanda.ccs.member.segment.service.ExtPointCriteriaService;
import com.wanda.ccs.member.segment.vo.ExtPointCriteriaVo;
import com.wanda.ccs.sqlasm.Clause;
import com.wanda.ccs.sqlasm.Condition;
import com.wanda.ccs.sqlasm.CriteriaParser;
import com.wanda.ccs.sqlasm.CriteriaResult;
import com.wanda.ccs.sqlasm.DataType;
import com.wanda.ccs.sqlasm.expression.ArrayExpCriterion;
import com.wanda.ccs.sqlasm.expression.ExpressionCriterion;
import com.wanda.ccs.sqlasm.expression.Operator;
import com.wanda.ccs.sqlasm.expression.SingleExpCriterion;
import com.wanda.ccs.sqlasm.expression.ValueClause;
import com.wanda.ccs.sqlasm.impl.DefaultCriteriaParser;


public class ExtPointCriteriaServiceImpl implements ExtPointCriteriaService {

	private ExtJdbcTemplate queryTemplate = null;
	
	private AuditJdbcTemplate auditJdbcTemplate = null;
	
	@InstanceIn(path="/dataSource")  
	private DataSource dataSource;
	
	private ExtJdbcTemplate getQueryTemplate()  {
		if(queryTemplate == null) {
			queryTemplate = new ExtJdbcTemplate(dataSource);
		}
		
		return this.queryTemplate;
	}
	
	private AuditJdbcTemplate getAuditTemplate() {
		if(auditJdbcTemplate == null) {
			
			auditJdbcTemplate = new AuditJdbcTemplate(dataSource, "T_EXT_POINT_CRITERIA");
			auditJdbcTemplate.registerInsertEntity(new EntityInsertDef("insert", ExtPointCriteriaVo.class, "T_EXT_POINT_CRITERIA"));
			auditJdbcTemplate.registerUpdateEntity(new EntityUpdateDef("update", ExtPointCriteriaVo.class, "T_EXT_POINT_CRITERIA", 
					new String[] {"extPointCriteriaId"}, new String[] {"extPointCriteriaId", "code", "createBy", "createDate", "ownerLevel", "ownerRegion", "ownerCinema"}));
			auditJdbcTemplate.registerDelete("delete", "DELETE from T_EXT_POINT_CRITERIA where EXT_POINT_CRITERIA_ID= ? ");
			auditJdbcTemplate.registerLogicDelete("logicDelete", "update T_EXT_POINT_CRITERIA set ISDELETE='1' where EXT_POINT_CRITERIA_ID=?");
		}
		
		return this.auditJdbcTemplate;
	}
	

	public QueryResultVo<Map<String, Object>> queryList(QueryParamVo queryParam, List<ExpressionCriterion> criteria, UserProfile userinfo) {

		criteria.add(new SingleExpCriterion("userLevel", userinfo.getLevel().name()));
		criteria.add(new ArrayExpCriterion("userInfo",
				new String[] {userinfo.getId(), Long.toString(userinfo.getCinemaId()), userinfo.getRegionCode()}));

		criteria.add(new ArrayExpCriterion("orderby", null, null, null, 
				new String[]{queryParam.getSortName(), queryParam.getSortOrder()}));
		
		Clause mainTable = newPlain().in("from").output("T_EXT_POINT_CRITERIA s");

		Map<Condition, Clause> clauseMap = new LinkedHashMap<Condition, Clause>();
		clauseMap.put(notEmpty("code"), newExpression().in("where").output("s.CODE", DataType.STRING, Operator.EQUAL));
		clauseMap.put(notEmpty("name"), newExpression().in("where").output("s.NAME", DataType.STRING, Operator.LIKE));
		clauseMap.put(notEmpty("createBy"), newExpression().in("where").output("s.CREATE_BY", DataType.STRING, Operator.EQUAL));
		clauseMap.put(notEmpty("updateTimeFrom"), newExpression().in("where", false).output("s.UPDATE_DATE", DataType.DATE_TIME, Operator.GREAT_THAN_EQUAL));
		clauseMap.put(notEmpty("updateTimeTo"), newExpression().in("where", false).output("s.UPDATE_DATE", DataType.DATE_TIME, Operator.LESS_THAN_EQUAL));
		
		clauseMap.put(equalsValue("userLevel", "REGION"), newValue().from("userInfo").in("where")
				.output("(s.OWNER_LEVEL='REGION' or s.OWNER_LEVEL='CINEMA') and s.OWNER_REGION={2}", DataType.STRING, false));
		clauseMap.put(equalsValue("userLevel", "CINEMA"), newValue().from("userInfo").in("where")
				.output("s.OWNER_LEVEL='CINEMA' and s.OWNER_CINEMA={1}", DataType.LONG, false));
		clauseMap.put(notEmpty("orderby"), newValue().in("orderby").output("s.{0} {1}", DataType.SQL, true));
		
		
		CriteriaParser countParser = newParser(SELECT_PARAGRAPHS)
				.add(newPlain().in("select").output("count(s.EXT_POINT_CRITERIA_ID)"))
				.add(newPlain().in("where").output("s.ISDELETE='0'"))
				.add(mainTable)
				.add(clauseMap);
		
		CriteriaResult countResult = countParser.parse(criteria);

		CriteriaParser listParser = newParser(SELECT_PARAGRAPHS)
			.add(newPlain().in("select").output("s.EXT_POINT_CRITERIA_ID,s.CODE,s.NAME,s.CREATE_BY,s.UPDATE_BY,s.UPDATE_DATE"))
			.add(newPlain().in("where").output("s.ISDELETE='0'"))
			.add(mainTable)
			.add(clauseMap);

		CriteriaResult listResult = listParser.parse(criteria);
		

		
		logSql(countResult.getComposedText(), listResult.getComposedText(), listResult.getParameters());
		
		Long rowCount = getQueryTemplate().queryForLong(countResult.getComposedText(), countResult.getParameters().toArray());
		List<Map<String, Object>> listQueryResult = getQueryTemplate()
				.query(queryParam.getStartIndex(), queryParam.getFetchSize(), listResult.getComposedText(), listResult.getParameters().toArray(), new ColumnMapRowMapper());

		return new QueryResultVo<Map<String, Object>>(rowCount, listQueryResult);
	}
	
	private void logSql(String countSql, String listSql, List<Object> paramemters) {
		System.out.println("CountSQL:" + countSql);
		System.out.println("ListSQL:" + listSql);
		System.out.println("ParametersCount:" + paramemters.size());
		System.out.println(paramemters);
	}

	public ExtPointCriteriaVo get(Long extPointCriteriaId) {
		ExtPointCriteriaVo epc = (ExtPointCriteriaVo)getQueryTemplate().queryForObject(
				"select * from T_EXT_POINT_CRITERIA where EXT_POINT_CRITERIA_ID=?",
				new Object[]{extPointCriteriaId}, new EntityRowMapper<ExtPointCriteriaVo>(ExtPointCriteriaVo.class));
		return epc;
	}
	
	public ExtPointCriteriaVo getByCode(String code) {
		ExtPointCriteriaVo epc = (ExtPointCriteriaVo)getQueryTemplate().queryForObject(
				"select * from T_EXT_POINT_CRITERIA where CODE=?",
				new Object[]{code}, new EntityRowMapper<ExtPointCriteriaVo>(ExtPointCriteriaVo.class));
		return epc;
	}
	
	@Transactional
	public void insert(ExtPointCriteriaVo epc) {
		//Get total records count
		Long extPointCriteriaId = getQueryTemplate().queryForLong("select S_T_EXT_POINT_CRITERIA.NEXTVAL from DUAL");
		String code = getNewCode();
		//System.out.println("New Code: " + code);
		epc.setExtPointCriteriaId(extPointCriteriaId);
		epc.setCode(code);
		getAuditTemplate().insertEntity("insert", epc);
	}

	@Transactional
	public void update(ExtPointCriteriaVo epc) {
		getAuditTemplate().updateEntity("update", epc);
	}
	
	public boolean hasSameName(String name, Long selfCriteriaId) {
		Long sameNameCount;
		
		if(selfCriteriaId != null) {
			sameNameCount = getQueryTemplate().queryForLong("select count(t.EXT_POINT_CRITERIA_ID) from T_EXT_POINT_CRITERIA t where t.NAME=? and t.EXT_POINT_CRITERIA_ID<>? and t.ISDELETE='0'", name, selfCriteriaId);
		}
		else {
			sameNameCount = getQueryTemplate().queryForLong("select count(t.EXT_POINT_CRITERIA_ID) from T_EXT_POINT_CRITERIA t where t.NAME=? and t.ISDELETE='0'", name);
		}
		
		if(sameNameCount != null && sameNameCount > 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean hasReference(Long criteriaId) {
		Long refCount = getQueryTemplate().queryForLong("select count(t.EXT_POINT_RULE_ID) from T_EXT_POINT_RULE t where t.EXT_POINT_CRITERIA_ID=?", 
				criteriaId);
		if(refCount != null && refCount > 0) {
			return true;
		}
		else {
			return false;
		}
	}

	@Transactional
	public void delete(String[] extPointCriteriaIds) {
		for(String idString : extPointCriteriaIds) {
			Long extPoirntCriteriaId = new Long(idString.trim());
			getAuditTemplate().deleteEntity("delete", extPoirntCriteriaId);
		}
	}

	@Transactional
	public void logicDelete(String[] extPointCriteriaIds) {
		for(String idString : extPointCriteriaIds) {
			Long extPoirntCriteriaId = new Long(idString.trim());
			getAuditTemplate().logicDeleteEntity("logicDelete", extPoirntCriteriaId);
		}
	}
	

	private String getNewCode() {
		StringBuffer code = new StringBuffer();
		code.append("EPC").append(new SimpleDateFormat("yyyyMMdd").format(new Date(System.currentTimeMillis())));
		String sql = "select max(CODE) as MAXCODE from T_EXT_POINT_CRITERIA s where s.CODE like ?";
		
		List<Map<String, Object>> list = getQueryTemplate().queryForList(sql, code.toString()+"%");
		
		String maxCode = "";
		if(list != null && !list.isEmpty()){
			Map<String, Object> map = list.get(0);
			if(map.get("MAXCODE") != null)
				maxCode = map.get("MAXCODE").toString();
		}
		if(ValueUtils.notEmpty(maxCode)){
			code.append(String.format("%04d", Long.valueOf(maxCode.substring(11))+1)); 
		}else{
			code.append("0001");
		}
		return code.toString();
	}
	
}
