package com.wanda.ccs.member.segment.service;

import static com.wanda.ccs.member.segment.defimpl.ExtPointCriteriaDef.GROUP_ID_CON_ITEM;
import static com.wanda.ccs.member.segment.defimpl.ExtPointCriteriaDef.GROUP_ID_TICKET;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Test;

import com.google.code.pathlet.jdbc.ExtJdbcTemplate;
import com.google.code.pathlet.util.ClassPathResource;
import com.wanda.ccs.member.segment.PathletBaseTest;
import com.wanda.ccs.member.segment.defimpl.ExtPointCriteriaDef;
import com.wanda.ccs.member.segment.vo.ExtPointCriteriaVo;
import com.wanda.ccs.sqlasm.CriteriaResult;
import com.wanda.ccs.sqlasm.expression.ExpressionCriterion;
import com.wanda.ccs.sqlasm.expression.JsonCriteriaHelper;

/**
 * 客群条件各场景测试
 * 对不同场景下产生的客群条件进行测试
 * @author clzhang
 *
 */
public class ExtPointCriteriaCaseTest extends PathletBaseTest {

	private static Logger sqlLog = Logger.getLogger("sql2");
	private static Logger resultLog = Logger.getLogger("result2");
	
	static {
		File logProerties;
		try {
			logProerties = (new ClassPathResource("/test_log4j.properties")).getFile();
			PropertyConfigurator.configure(logProerties.toURI().toURL());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test() throws Exception {
//		testOneByOneCriterion("EPC201308290001", "票房交易");
		testWholeCriterion("EPC201308290001", "票房交易");
//		testOneByOneCriterion("EPC201307260001", "卖品交易");
//		testWholeCriterion("EPC201307260001", "卖品交易");
	}

	/**
	 * 对某一个特殊积分条件，把其中存在的每一个条件作为单一条件产生查询条件到数据库中进行验证，并打印出每一个条件的执行结果。
	 * 
	 * @param extPointCriteriaCode
	 * @param parserType
	 * @throws Exception
	 */
	private void testOneByOneCriterion(String  extPointCriteriaCode, String parserType) throws Exception {
		ExtJdbcTemplate jdbcTemplate = getJdbcTemplateDw();
		ExtPointCriteriaService service = (ExtPointCriteriaService) getContainer()
				.getInstance("/segment/ExtPointCriteriaService");
		
		ExtPointCriteriaVo vo = service.getByCode(extPointCriteriaCode);
		
		List<ExpressionCriterion> criteria = JsonCriteriaHelper.parse(vo.getCriteriaScheme());
		ExtPointCriteriaDef def = new ExtPointCriteriaDef();

		//计算总数的前缀
		String countSqlBegin = "select count(*) from (";
		String countSqlEnd = null;
		
		CriteriaResult noneCriterionResult = null;
		if("票房交易".equals(parserType)) {
			noneCriterionResult = def.getTicketSaleParser().parse(new ArrayList<ExpressionCriterion>());
			countSqlEnd = " group by transSales_cinema.INNER_CODE,member.MEMBER_KEY,transSales.BK_CT_ORDER_CODE,transSales.BK_TICKET_NUMBER)";
		}
		else if("卖品交易".equals(parserType)) {
			noneCriterionResult = def.getConSaleParser().parse(new ArrayList<ExpressionCriterion>());
			countSqlEnd = " group by consale_cinema.INNER_CODE,member.MEMBER_KEY,consale.BK_CS_ORDER_CODE,consale_item.ITEM_CODE)";
		}
		String totalSql = noneCriterionResult.getParameterizeText();
		long EMPTY_SQL_LENGTH = totalSql.length();
		String totalCountSql = countSqlBegin + totalSql + countSqlEnd;
		long totalCount = jdbcTemplate.queryForLong(totalCountSql);
		
		sqlLog.info("********** 开始进行特殊积分条件测试（ " + parserType + "），code=" + vo.getCode() + ", name=" + vo.getName() + " **********");
		resultLog.info("********** 开始进行特殊积分条件测试（ " + parserType + "），code=" + vo.getCode() + ", name=" + vo.getName() + " **********");
		resultLog.info("当前记录总数:" + totalCount);
		sqlLog.info("当前记录总数， SQL：" + totalCountSql);

		System.out.println("EMPTY_SQL_LENGTH=" + EMPTY_SQL_LENGTH);
		//单独针对一个条件算出记录总数并打印
		for(ExpressionCriterion cri : criteria) {
			List<ExpressionCriterion> singleCri = new ArrayList<ExpressionCriterion>();

			CriteriaResult criResult = null; 
			if("票房交易".equals(parserType)) {
				//跳过卖品条件
				if(GROUP_ID_CON_ITEM.equals(cri.getGroupId())) {
					continue;
				}
				singleCri.add(cri);
				criResult = def.getTicketSaleParser().parse(singleCri);
			}
			else if("卖品交易".equals(parserType)) {
				//跳过卖品交易
				if(GROUP_ID_TICKET.equals(cri.getGroupId())) {
					continue;
				}
				singleCri.add(cri);
				criResult = def.getConSaleParser().parse(singleCri);
			}

			StringBuilder sql= (new StringBuilder())
					.append(countSqlBegin)
					.append(criResult.getComposedText())
					.append(countSqlEnd);
			
			StringBuilder parameterizeSql = new StringBuilder().append(countSqlBegin)
					.append(criResult.getParameterizeText()).append(countSqlEnd);
			
			sqlLog.info("inputId=" + cri.getId() + ", label=" + cri.getLabel() + " Query SQL: \n" + parameterizeSql.toString());
			//sqlLog.info(criResult.getParameters());
			
			try {
				long startTime = System.currentTimeMillis();
				long criCount = jdbcTemplate.queryForLong(sql.toString(), criResult.getParameters().toArray());
				long endTime = System.currentTimeMillis();
				
				if(parameterizeSql.length() <= EMPTY_SQL_LENGTH) {
					resultLog.error("inputId=" + cri.getId() + ", label=" + cri.getLabel() + ", 没有SQL任何拼入!!!!");
				}
				else {
					resultLog.info("inputId=" + cri.getId() + ", label=" + cri.getLabel() + 
							"\n 数量:" + criCount + ", 与总数相等:" + (criCount == totalCount) + 
							", 耗时=" + (endTime - startTime) + "ms");
				}
			}
			catch(Exception e) {
				resultLog.error("inputId=" + cri.getId() + ", label=" + cri.getLabel() + "， 执行查询失败!!!!");
				e.printStackTrace();
			}
			
		}

	}
	
	
	private void testWholeCriterion(String  extPointCriteriaCode, String parserType)  throws Exception {
		ExtJdbcTemplate jdbcTemplate = getJdbcTemplateDw();
		ExtPointCriteriaService service = (ExtPointCriteriaService) getContainer()
				.getInstance("/segment/ExtPointCriteriaService");
		
		ExtPointCriteriaVo vo = service.getByCode(extPointCriteriaCode);
		
		List<ExpressionCriterion> criteria = JsonCriteriaHelper.parse(vo.getCriteriaScheme());
		ExtPointCriteriaDef def = new ExtPointCriteriaDef();

		//计算总数的前缀
		String countSqlBegin = "select count(*) from (";
		String countSqlEnd = null;
		
		CriteriaResult criterionResult = null;
		if("票房交易".equals(parserType)) {
			criterionResult = def.getTicketSaleParser().parse(criteria);
			countSqlEnd = " group by transSales_cinema.INNER_CODE,member.MEMBER_KEY,transSales.BK_CT_ORDER_CODE,transSales.BK_TICKET_NUMBER)";
		}
		else if("卖品交易".equals(parserType)) {
			criterionResult = def.getConSaleParser().parse(criteria);
			countSqlEnd = " group by consale_cinema.INNER_CODE,member.MEMBER_KEY,consale.BK_CS_ORDER_CODE,consale_item.ITEM_CODE)";
		}
		String totalCountSql = countSqlBegin + criterionResult.getParameterizeText() + countSqlEnd;
		
		long startTime = System.currentTimeMillis();
		long totalCount = jdbcTemplate.queryForLong(totalCountSql);
		long endTime = System.currentTimeMillis();
		
		resultLog.info("全部条件查询，记录总数:" + totalCount + ", 耗时=" + (endTime - startTime) + "ms");
		sqlLog.info("全部条件查询， SQL：" + totalCountSql);
	}
	

}
