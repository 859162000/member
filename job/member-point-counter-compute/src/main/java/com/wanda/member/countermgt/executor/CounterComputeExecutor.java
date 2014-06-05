package com.wanda.member.countermgt.executor;

import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import com.solar.etl.SolarEtlExecutor;
import com.solar.etl.config.EtlBean;
import com.solar.etl.config.EtlConfig;
import com.wanda.member.countermgt.service.CounterComputeFacade;
import com.wanda.mms.dao.MyBatisDAO;
import com.wanda.mms.service.MbrApplicationContext;

public class CounterComputeExecutor {
	private static final String TASK_ID = "20000";
	static Log log = LogFactory.getLog(CounterComputeExecutor.class);
	static ApplicationContext context = null;
	static MyBatisDAO myBatisDAO = null;
	static {
		// EtlConfig.getInstance();
		context = MbrApplicationContext.getInstance();
		myBatisDAO = context.getBean("myBatisDAO", MyBatisDAO.class);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args == null || args.length == 0) {
			Calendar cal = Calendar.getInstance();
			int x = -0;// or x=-3;
			cal.add(Calendar.DAY_OF_MONTH, x);
			String bzda = new java.text.SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
			compute(bzda);
		} else if (args.length == 1) {
			if (args[0].equalsIgnoreCase("set")) {
				counterComputeByMember();
			}else if(args[0].equalsIgnoreCase("rule1")){
				log.info("exec:"+args[0]);
				counterComputeByMemberRule1();
			}else if(args[0].equalsIgnoreCase("rulenull")){
				log.info("exec:"+args[0]);
				counterComputeByMemberNullRule();
			} else {
				String memberId = args[0];
				CounterComputeFacade facade = context.getBean("counterComputeFacade", CounterComputeFacade.class);
				facade.computeByMember(memberId);
			}
		}
	}
	
	public static void counterComputeByMemberSet(){
		log.info("counterComputeByMemberSet start");
		CounterComputeFacade facade = context.getBean("counterComputeFacade", CounterComputeFacade.class);
		facade.computeCounterbyMemberSet();
		log.info("counterComputeByMemberSet end");
	}
	
	public static void counterComputeByMember(){
		EtlBean prmapping = EtlConfig.getInstance().getEtlBean("countercompute");
		SolarEtlExecutor.runetlFixedThread(prmapping,null, 10);
	}
	public static void counterComputeByMemberRule1(){
		EtlBean prmapping1 = EtlConfig.getInstance().getEtlBean("countercomputerule1");
		SolarEtlExecutor.runetlFixedThread(prmapping1,null, 16);
	}
	public static void counterComputeByMemberNullRule(){
		EtlBean prmapping = EtlConfig.getInstance().getEtlBean("countercomputenullrule");
		SolarEtlExecutor.runetlFixedThread(prmapping,null, 16);	
	}

	private static void compute(String bzda) {
		EtlBean prmapping = EtlConfig.getInstance().getEtlBean("countercompute");
		SolarEtlExecutor.runetlFixedThread(prmapping, new String[] {"-bizdate", bzda }, 20);
	}

}
