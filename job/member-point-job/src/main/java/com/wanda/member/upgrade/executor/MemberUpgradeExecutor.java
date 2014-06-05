package com.wanda.member.upgrade.executor;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import com.solar.etl.SolarEtlExecutor;
import com.solar.etl.config.EtlBean;
import com.solar.etl.config.EtlConfig;
import com.wanda.member.upgrade.data.TMemberUpgradeTask;
import com.wanda.member.upgrade.data.TMemberUpgradeTaskExample;
import com.wanda.member.upgrade.data.TMemberUpgradeTaskMapper;
import com.wanda.member.upgrade.service.MemberUpgradeHandler;
import com.wanda.mms.dao.MyBatisDAO;
import com.wanda.mms.service.MbrApplicationContext;

public class MemberUpgradeExecutor {
	private static String TASK_ID = "100000";
	static Log loger = LogFactory.getLog(MemberUpgradeExecutor.class);
	static SimpleDateFormat fromat = new java.text.SimpleDateFormat("yyyy-MM-dd");
	static ApplicationContext context = null;
	static MyBatisDAO myBatisDAO = null;
	static {
		// EtlConfig.getInstance();
		context = MbrApplicationContext.getInstance();
		myBatisDAO = context.getBean("myBatisDAO",MyBatisDAO.class);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("============================" + args.length);
		if (args == null || args.length == 0) {
			Calendar cal = Calendar.getInstance();
			int x = -0;// or x=-3;
			cal.add(Calendar.DAY_OF_MONTH, x);
			String bzda = new java.text.SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
			EtlBean prmapping = EtlConfig.getInstance().getEtlBean("uplevel");// 插入到临时表。
																				// 等待规则
			SolarEtlExecutor.runetlFixedThread(prmapping, new String[] {"-bizdate", bzda }, 10);
		} else if (args.length == 1) {
			loger.info("args[0]==="+args[0]);
			if (args[0].equalsIgnoreCase("all")) {
				execSpecMonth();
			}else{
				loger.info("day:" + args[0] + " is proecssing ");
				System.out.println("day:" + args[0] + " is proecssing ");
				execSpecDay(args[0]);
			}
			
		} else if (args.length == 2) {
			loger.info("begin upgrade spec member "+args[1]);
			MemberUpgradeHandler handler = context.getBean(MemberUpgradeHandler.class);
			loger.info("get Handler "+handler);
			handler.handleEachMember(new BigDecimal(args[1]), args[0]);
		}

	}

	/**
	 * month yyyy-MM
	 * 
	 * @param month
	 */
	public static void execSpecMonth() {
		TMemberUpgradeTaskMapper mapper = myBatisDAO.sqlSession.getMapper(TMemberUpgradeTaskMapper.class);
		TMemberUpgradeTaskExample example = new TMemberUpgradeTaskExample();
		example.createCriteria().andStatusEqualTo(new BigDecimal(0)).andTaskIdEqualTo(TASK_ID);
		List<TMemberUpgradeTask> list = mapper.selectByExample(example );
		loger.info("task num=="+list.size());
		for(TMemberUpgradeTask task:list){
			loger.info("exec day:"+task.getTaskDay());
			task.setStatus(new BigDecimal(1));
			mapper.updateByPrimaryKey(task);
			execSpecDay(task.getTaskDay());
			task.setStatus(new BigDecimal(2));
			mapper.updateByPrimaryKey(task);
		}
	}

	private static void execSpecDay(String specDay) {
		try {
			fromat.parse(specDay);
		} catch (ParseException e) {
			loger.error(e.getMessage());
			return;
		}
		EtlBean prmapping = EtlConfig.getInstance().getEtlBean("uplevel");// 插入到临时表。
																			// 等待规则
		SolarEtlExecutor.runetlFixedThread(prmapping, new String[] {"-bizdate", specDay }, 10);
	}

}
