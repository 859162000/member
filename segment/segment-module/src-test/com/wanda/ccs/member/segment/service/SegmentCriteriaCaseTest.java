package com.wanda.ccs.member.segment.service;

import static com.wanda.ccs.member.segment.SegmentConstants.Schemas.RPT2;

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
import com.wanda.ccs.member.segment.defimpl.SegmentCriteriaDef;
import com.wanda.ccs.sqlasm.CriteriaResult;
import com.wanda.ccs.sqlasm.expression.ExpressionCriterion;
import com.wanda.ccs.sqlasm.expression.JsonCriteriaHelper;

/**
 * 客群条件各场景测试
 * 对不同场景下产生的客群条件进行测试
 * @author Charlie Zhang
 *
 */
public class SegmentCriteriaCaseTest extends PathletBaseTest {

	private static Logger sqlLog = Logger.getLogger("sql");
	private static Logger resultLog = Logger.getLogger("result");
	
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
		testOneByOneCriterion();
		testWholeCriterion();
	}

	/**
	 * 对某一个客群，把其中存在的每一个条件作为单一条件产生查询条件到数据库中进行验证，并打印出每一个条件的执行结果。
	 * 
	 * @param segmentCode
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private void testOneByOneCriterion() throws Exception {
		ExtJdbcTemplate jdbcTemplate = getJdbcTemplateDw();
		
		String criteriaSchemeJson = getThisPackageText("SegmentCriteriaCaseTest.js");
		
		List<ExpressionCriterion> criteria = JsonCriteriaHelper.parse(criteriaSchemeJson);
		SegmentCriteriaDef def = new SegmentCriteriaDef();

		//得到会员总数
		Long count = jdbcTemplate.queryForLong("select count(member.MEMBER_KEY) from " + RPT2 + ".T_D_CON_MEMBER member where member.STATUS='1' and member.ISDELETE = 0");
		

		Long countSalesTransAll = jdbcTemplate.queryForLong("select count(distinct MEMBER_KEY) from " +RPT2+".T_F_CON_MEMBER_CINEMA");
		Long countSalesCon = jdbcTemplate.queryForLong("select count(distinct MEMBER_KEY) from " +RPT2+".T_F_CON_MEMBER_SALE");
		Long countSalesTrans = jdbcTemplate.queryForLong("select count(distinct MEMBER_KEY) from " +RPT2+".T_F_CON_MEMBER_TICKET");
		sqlLog.info("********** 开始进行客群测试  **********");
		resultLog.info("********** 开始进行客群测试 **********");
		resultLog.info("当前会员总数:" + count);
		resultLog.info("当前参与总交易的会员总数：" +countSalesTransAll);
		resultLog.info("当前参与票房交易的会员总数：" +countSalesTrans);
		resultLog.info("当前参与卖品交易的会员总数：" +countSalesCon);
		
		long EMPTY_SQL_LENGTH = 148;
		
		//单独针对一个条件算出会员总数并打印
		for(ExpressionCriterion cri : criteria) {
			List<ExpressionCriterion> singleCri = new ArrayList<ExpressionCriterion>();
			singleCri.add(cri);
			CriteriaResult criResult = def.getParser().parse(singleCri);
			
			String countSqlBegin = "select count(*) from (SELECT distinct member.MEMBER_KEY ";
			String countSqlNoDistictBegin = "select count(*) from (SELECT member.MEMBER_KEY ";
			
			StringBuilder sql= (new StringBuilder())
					.append(countSqlBegin)
					.append(criResult.getComposedText())
					.append(")");
			
			StringBuilder sqlNoDistinct = (new StringBuilder())
					.append(countSqlNoDistictBegin)
					.append(criResult.getComposedText())
					.append(")");
			
			
			StringBuilder parameterizeSql = new StringBuilder().append(countSqlBegin)
					.append(criResult.getParameterizeText()).append( ")");
			
			sqlLog.info("inputId=" + cri.getId() + ", label=" + cri.getLabel() + " Query SQL: \n" + parameterizeSql.toString());
			//sqlLog.info(criResult.getParameters());
			
			try {
				long startTime = System.currentTimeMillis();
				long criCount = jdbcTemplate.queryForLong(sql.toString(), criResult.getParameters().toArray());
				long endTime = System.currentTimeMillis();
				//用非distinct方式查询，看是否和之前查出的数量相同
				long criCountNoDistinct = jdbcTemplate.queryForLong(sqlNoDistinct.toString(), criResult.getParameters().toArray());
				
				if(parameterizeSql.length() <= EMPTY_SQL_LENGTH) {
					resultLog.error("inputId=" + cri.getId() + ", label=" + cri.getLabel() + ", 没有SQL任何拼入!!!!");
				}
				else {
					resultLog.info("inputId=" + cri.getId() + ", label=" + cri.getLabel() + 
							"\n 数量:" + criCount + ", 与总数相等:" + (criCount == count) + 
							", NoDistinct数量:" + criCountNoDistinct + ((criCount != criCountNoDistinct) ? "（有重复!!!!）" : "") + 
							", 耗时=" + (endTime - startTime) + "ms");
				}
			}
			catch(Exception e) {
				resultLog.error("inputId=" + cri.getId() + ", label=" + cri.getLabel() + "， 执行查询失败!!!!");
				e.printStackTrace();
			}
			
		}

	}
	
	
	private void testWholeCriterion()  throws Exception {
		ExtJdbcTemplate jdbcTemplate = getJdbcTemplateDw();
		String criteriaSchemeJson = getThisPackageText("SegmentCriteriaCaseTest.js");
		
		List<ExpressionCriterion> criteria = JsonCriteriaHelper.parse(criteriaSchemeJson);
		SegmentCriteriaDef def = new SegmentCriteriaDef();
		
		String countSqlBegin = "select count(*) from (SELECT distinct member.MEMBER_KEY ";
		String countSqlNoDistictBegin = "select count(*) from (SELECT member.MEMBER_KEY ";
		
		CriteriaResult criResult = def.getParser().parse(criteria);
		
		StringBuilder sql= (new StringBuilder())
				.append(countSqlBegin)
				.append(criResult.getComposedText())
				.append(")");
		
		StringBuilder sqlNoDistinct = (new StringBuilder())
				.append(countSqlNoDistictBegin)
				.append(criResult.getComposedText())
				.append(")");
		
		
		StringBuilder parameterizeSql = new StringBuilder().append(countSqlBegin)
				.append(criResult.getParameterizeText()).append( ")");
		
		sqlLog.info("全部条件查询， Query SQL: \n" + parameterizeSql.toString());
		
		try {
			long startTime = System.currentTimeMillis();
			long criCount = jdbcTemplate.queryForLong(sql.toString(), criResult.getParameters().toArray());
			long endTime = System.currentTimeMillis();
			//用非distinct方式查询，看是否和之前查出的数量相同
			long criCountNoDistinct = jdbcTemplate.queryForLong(sqlNoDistinct.toString(), criResult.getParameters().toArray());
			

			resultLog.info("全部条件查询，" + 
						"\n 数量:" + criCount + ", 与总数相等: 无   " + 
						", NoDistinct数量:" + criCountNoDistinct + ((criCount != criCountNoDistinct) ? "（有重复!!!!）" : "") + 
						", 耗时=" + (endTime - startTime) + "ms");
			
		}
		catch(Exception e) {
			resultLog.error("全部条件查询，执行查询失败!!!!");
			e.printStackTrace();
		}

	}
	

}
