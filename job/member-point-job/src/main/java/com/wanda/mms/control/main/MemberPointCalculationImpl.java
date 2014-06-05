package com.wanda.mms.control.main;

import java.sql.Connection;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.solar.etl.SolarEtlExecutor;
import com.solar.etl.config.EtlBean;
import com.solar.etl.config.EtlConfig;
import com.solar.etl.db.ConnctionFactory;
import com.wanda.mms.control.stream.Basic;
import com.wanda.mms.control.stream.dao.impl.TmpDaoImpl;

public class MemberPointCalculationImpl {

	public static Connection mbr = ConnctionFactory
			.getConnection("db.mbr_flag");
	public static Map<String, String> map = new HashMap<String, String>();

	public static void compute(String[] args) {

		AllMbrPoint all = new AllMbrPoint();
		if (args == null || args.length == 0) {

			// 在每天早上7点10到7点50分结束
			// while(startTiem.before(new Date())&&endTime.after(new Date())){
			// flag=true;

			Calendar cal = Calendar.getInstance();
			int x = -1;// or x=-3;
			cal.add(Calendar.DAY_OF_MONTH, x);
			String bzda = new java.text.SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
			// LogUtils.debug("Bizdate:"+bzda);
			String[] dz = bzda.split("-");
			String da = dz[0] + dz[1] + dz[2];
			long ymd = Long.valueOf(da);

			int flag1 = all.fandstatus(bzda);
			if (flag1 == 1) {
				// 为真开始跑数
				String bar = bzda;
				TmpDaoImpl tm = new TmpDaoImpl();
				List<String> strlist = tm.fandCinema_inner_code(Basic.mbr, bar);
				for (int i = 0; i < strlist.size(); i++) {
					String cinema = strlist.get(i);
					map.put("DATE", bar);
					map.put("CINEMA", cinema);
					tm.tmp(Basic.mbr, cinema);
					EtlBean prmapping = EtlConfig.getInstance().getEtlBean(
							"textpointrule");// 插入到临时表。 等待规则
					SolarEtlExecutor.runetlFixedThread(prmapping, new String[] {
							"-bizdate", bar, "-cinema", cinema }, 10);
				}
				all.pointcalculate(bar);
				all.send(bar);
				all.updatestatus(ymd);
			}
			all.repair();

		} else if (args != null && args.length == 2) {
			if (args[0].equals("ztbizdate") && args[1] != null) {

				String bar = args[1];

				String[] dz = bar.split("-");
				String da = dz[0] + dz[1] + dz[2];
				long ymd = Long.valueOf(da);

				int flag1 = all.fandstatus(bar);

				if (flag1 == 1) {
					// 为真开始跑数

					TmpDaoImpl tm = new TmpDaoImpl();
					List<String> strlist = tm.fandCinema_inner_code(Basic.mbr,
							bar);
					for (int i = 0; i < strlist.size(); i++) {
						String cinema = strlist.get(i);
						map.put("DATE", bar);
						map.put("CINEMA", cinema);
						tm.tmp(Basic.mbr, cinema);
						EtlBean prmapping = EtlConfig.getInstance().getEtlBean(
								"textpointrule");// 插入到临时表。 等待规则
						SolarEtlExecutor.runetl(prmapping, new String[] {
								"-bizdate", bar, "-cinema", cinema }, 1);

					}
					all.pointcalculate(bar);
					all.send(bar);
					all.updatestatus(ymd);

				}

			} else if (args[0].equals("ztrecaluc") && args[1] != null) {

				// String foo=args[0];
				String bar = args[1];

				String[] dz = bar.split("-");
				String da = dz[0] + dz[1] + dz[2];
				long ymd = Long.valueOf(da);

				int flag1 = all.fandstatus(bar);

				if (flag1 == 1) {
					// 为真开始跑数

					TmpDaoImpl tm = new TmpDaoImpl();
					List<String> strlist = tm.fandCinema_inner_code(Basic.mbr,
							bar);
					for (int i = 0; i < strlist.size(); i++) {
						String cinema = strlist.get(i);
						map.put("DATE", bar);
						map.put("CINEMA", cinema);
						tm.tmp(Basic.mbr, cinema);
						EtlBean prmapping = EtlConfig.getInstance().getEtlBean(
								"textpointrule");// 插入到临时表。 等待规则
						SolarEtlExecutor.runetl(prmapping, new String[] {
								"-bizdate", bar, "-cinema", cinema }, 1);

					}
					all.pointrepair(bar);// 全量重算
					all.send(bar);// 推数
					all.updatestatus(ymd);// 更新状态表

				}

			} else if (args[0].equals("bizdate") && args[1] != null) {// args[]
																		// is
																		// null
																		// 算前一个营业日积分及已经ｏｋ的影城积分
				// String foo=args[0];
				String bar = args[1];
				TmpDaoImpl tm = new TmpDaoImpl();
				List<String> strlist = tm.fandCinema_inner_code(Basic.mbr, bar);
				for (int i = 0; i < strlist.size(); i++) {
					String cinema = strlist.get(i);
					map.put("DATE", bar);
					map.put("CINEMA", cinema);
					tm.tmp(Basic.mbr, cinema);
					EtlBean prmapping = EtlConfig.getInstance().getEtlBean(
							"textpointrule");// 插入到临时表。 等待规则
					SolarEtlExecutor.runetl(prmapping, new String[] {
							"-bizdate", bar, "-cinema", cinema }, 1);

				}
				all.pointcalculate(bar); // 全量计算
			} else if (args[0].equals("recaluc") && args[1] != null) {// args[]
																		// is
																		// null
																		// 算前一个营业日积分及已经ｏｋ的影城积分
				// String foo=args[0];
				String bar = args[1];
				TmpDaoImpl tm = new TmpDaoImpl();
				List<String> strlist = tm.fandCinema_inner_code(Basic.mbr, bar);
				for (int i = 0; i < strlist.size(); i++) {
					String cinema = strlist.get(i);
					map.put("DATE", bar);
					map.put("CINEMA", cinema);
					tm.tmp(Basic.mbr, cinema);
					EtlBean prmapping = EtlConfig.getInstance().getEtlBean(
							"textpointrule");// 插入到临时表。 等待规则
					SolarEtlExecutor.runetlFixedThread(prmapping, new String[] {
							"-bizdate", bar, "-cinema", cinema }, 10);

				}

				all.pointrepair(bar); // 全量重算
			}
		} else if (args != null && args.length == 3) {
			if (args[0].equals("recaluc") && args[1] != null && args[2] != null) {// args[]
																					// is
																					// null
																					// 算前一个营业日积分及已经ｏｋ的影城积分
				// String foo=args[0];
				String bar = args[1];
				String cic = args[2];
				TmpDaoImpl tm = new TmpDaoImpl();

				String cinema = cic;
				map.put("DATE", bar);
				map.put("CINEMA", cinema);
				tm.tmp(Basic.mbr, cinema);
				EtlBean prmapping = EtlConfig.getInstance().getEtlBean(
						"textpointrule");// 插入到临时表。 等待规则
				SolarEtlExecutor.runetl(prmapping, new String[] { "-bizdate",
						bar, "-cinema", cinema }, 1);

				String[] aa = { "recaluc", bar, cic };
				Basic.main(aa);
			}
		}
		// recaluc

	}
}
