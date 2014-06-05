package com.wanda.mms.control.main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import bsh.ParseException;

import com.solar.etl.SolarEtlExecutor;
import com.solar.etl.config.EtlBean;
import com.solar.etl.config.EtlConfig;
import com.solar.etl.db.ConnctionFactory;
import com.solar.etl.log.LogUtils;
import com.wanda.mms.control.stream.Basic;
import com.wanda.mms.control.stream.dao.impl.TmpDaoImpl;

public class AllMbrPoint {
	static Logger logger = Logger.getLogger(AllMbrPoint.class.getName());
	static{
		EtlConfig.getInstance();
	}
	
	public static Connection mbr=ConnctionFactory.getConnection("db.mbr_flag");
	public static Map<String,String> map = new HashMap<String, String>();
	/**
	 * @param args
	 */
	public static void main(String[] args) throws ParseException {
		

		//select seqid from CCS_REPORT.t_sys_data_job where  seqid=(select max(seqid) from CCS_REPORT.t_sys_data_job where FLAG_POS_LOAD=? and ymd=?)
		//update CCS_REPORT.t_sys_data_job set FLAG_ETL_OLAP=?, TIME_ETL_OLAP=sysdate where seqid=(select max(seqid) from CCS_REPORT.t_sys_data_job where FLAG_POS_LOAD=? and ymd=?)
		//判断 状态是否OK 如为真执行 下边方法{ 推数 改变状态
		
		//} //如为假 不执行
		
		//扫描 状态2 表，如为真 看有多少家影城需要修复数据
		
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTime(new Date());
//		calendar.add(Calendar.DAY_OF_MONTH, -1);
//		String bizdate = new SimpleDateFormat("yyyyMMdd").format(calendar.getTime());
//		calendar.add(Calendar.DAY_OF_MONTH, 1);
//		String dateString = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
//		
//		Date startTiem = null;
//		Date endTime = null;
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		try {
//			startTiem = formatter.parse(dateString+" 07:10:00");
//			endTime = formatter.parse(dateString+" 07:50:00");
//		}catch (java.text.ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
	//	boolean flag=false;
		AllMbrPoint all = new 	AllMbrPoint();
	if(args==null||args.length==0){	
		 
		//在每天早上7点10到7点50分结束
	//	while(startTiem.before(new Date())&&endTime.after(new Date())){
		//	flag=true;
		 
			Calendar cal=Calendar.getInstance();
			int x=-1;//or x=-3;
			cal.add(Calendar.DAY_OF_MONTH,x);
			String bzda=new java.text.SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
		//	LogUtils.debug("Bizdate:"+bzda);
			String[] dz = bzda.split("-");
			String da = dz[0]+dz[1]+dz[2];
			long ymd = Long.valueOf(da);
		 
			int flag1 = all.fandstatus(bzda);
				if (flag1==1) {
					//为真开始跑数
					String bar=bzda;
					TmpDaoImpl tm = new TmpDaoImpl();
					List<String> strlist = tm.fandCinema_inner_code(Basic.mbr, bar);
					for (int i = 0; i < strlist.size(); i++) {
						String cinema =strlist.get(i);
						map.put("DATE",bar); 
						map.put("CINEMA",cinema); 
						tm.tmp(Basic.mbr,cinema);
						EtlBean prmapping=EtlConfig.getInstance().getEtlBean("textpointrule");//插入到临时表。 等待规则
				 		SolarEtlExecutor.runetlFixedThread(prmapping,new String[]{"-bizdate",bar,"-cinema",cinema},10);
					}
			 		all.pointcalculate(bar);
			 		all.send(bar);
			 		all.updatestatus(ymd);
				}
				 	all.repair();
			 
			
		} else if(args!=null&&args.length==2){
			if(args[0].equals("ztbizdate")&&args[1]!=null ){	

				String bar=args[1];
		
				String[] dz = bar.split("-");
				String da = dz[0]+dz[1]+dz[2];
				long ymd = Long.valueOf(da);
			 
				int flag1 = all.fandstatus(bar);
				
				 
				 	if (flag1==1) {
						//为真开始跑数

						TmpDaoImpl tm = new TmpDaoImpl();
						List<String> strlist = tm.fandCinema_inner_code(Basic.mbr, bar);
						for (int i = 0; i < strlist.size(); i++) {
							String cinema =strlist.get(i);
							map.put("DATE",bar); 
							map.put("CINEMA",cinema); 
							tm.tmp(Basic.mbr,cinema);
							EtlBean prmapping=EtlConfig.getInstance().getEtlBean("textpointrule");//插入到临时表。 等待规则
					 		SolarEtlExecutor.runetl(prmapping,new String[]{"-bizdate",bar,"-cinema",cinema},1);
						
						}
				 		all.pointcalculate(bar);
				 		all.send(bar);
				 	 	all.updatestatus(ymd);
					
				 	}
				
			}else if(args[0].equals("ztrecaluc")&&args[1]!=null ){	
				 
				//	String foo=args[0];
					String bar=args[1];
			
					String[] dz = bar.split("-");
					String da = dz[0]+dz[1]+dz[2];
					long ymd = Long.valueOf(da);
				 
					int flag1 = all.fandstatus(bar);
					
					 
						if (flag1==1) {
							//为真开始跑数

							TmpDaoImpl tm = new TmpDaoImpl();
							List<String> strlist = tm.fandCinema_inner_code(Basic.mbr, bar);
							for (int i = 0; i < strlist.size(); i++) {
								String cinema =strlist.get(i);
								map.put("DATE",bar); 
								map.put("CINEMA",cinema); 
								tm.tmp(Basic.mbr,cinema);
								EtlBean prmapping=EtlConfig.getInstance().getEtlBean("textpointrule");//插入到临时表。 等待规则
						 		SolarEtlExecutor.runetl(prmapping,new String[]{"-bizdate",bar,"-cinema",cinema},1);
							
							}
					 		all.pointrepair(bar);//全量重算
					 		all.send(bar);//推数
					 		all.updatestatus(ymd);//更新状态表
					 		
						}
						
						
					 
					
				}else  if(args[0].equals("bizdate")&&args[1]!=null ){//args[] is null 算前一个营业日积分及已经ｏｋ的影城积分
				//	String foo=args[0];
					String bar=args[1];
					TmpDaoImpl tm = new TmpDaoImpl();
					List<String> strlist = tm.fandCinema_inner_code(Basic.mbr, bar);
					for (int i = 0; i < strlist.size(); i++) {
						String cinema =strlist.get(i);
						map.put("DATE",bar); 
						map.put("CINEMA",cinema); 
						tm.tmp(Basic.mbr,cinema);
						EtlBean prmapping=EtlConfig.getInstance().getEtlBean("textpointrule");//插入到临时表。 等待规则
				 		SolarEtlExecutor.runetl(prmapping,new String[]{"-bizdate",bar,"-cinema",cinema},1);
					
					}
			 		all.pointcalculate(bar); //全量计算
					}else  if(args[0].equals("recaluc")&&args[1]!=null ){//args[] is null 算前一个营业日积分及已经ｏｋ的影城积分
				//	String foo=args[0];
					String bar=args[1];
					TmpDaoImpl tm = new TmpDaoImpl();
					List<String> strlist = tm.fandCinema_inner_code(Basic.mbr, bar);
					for (int i = 0; i < strlist.size(); i++) {
						String cinema =strlist.get(i);
						map.put("DATE",bar); 
						map.put("CINEMA",cinema); 
						tm.tmp(Basic.mbr,cinema);
						EtlBean prmapping=EtlConfig.getInstance().getEtlBean("textpointrule");//插入到临时表。 等待规则
						SolarEtlExecutor.runetlFixedThread(prmapping,new String[]{"-bizdate",bar,"-cinema",cinema},10);
					
					}
		 		 
					all.pointrepair(bar); //全量重算
				}	
		}else if(args!=null&&args.length==3){
			   if(args[0].equals("recaluc")&&args[1]!=null&&args[2]!=null){//args[] is null 算前一个营业日积分及已经ｏｋ的影城积分
				//String foo=args[0];
				String bar=args[1];
				String cic=args[2];
				TmpDaoImpl tm = new TmpDaoImpl();
			
				 
					String cinema =cic;
					map.put("DATE",bar); 
					map.put("CINEMA",cinema); 
					tm.tmp(Basic.mbr,cinema);
					EtlBean prmapping=EtlConfig.getInstance().getEtlBean("textpointrule");//插入到临时表。 等待规则
			 		SolarEtlExecutor.runetl(prmapping,new String[]{"-bizdate",bar,"-cinema",cinema},1);
				
			 
		 		 
			 		String [] aa={"recaluc",bar,cic};
					Basic.main(aa);
				}
		    }
		//recaluc
		}
	    //全量计算
		public void pointcalculate(String bar){
		   // int falg =0;
			EtlBean mapping=EtlConfig.getInstance().getEtlBean("gbcinema");//基础积分计算:影票计算 //改按影城计算。
			SolarEtlExecutor.runetlFixedThread(mapping,new String[]{"-bizdate",bar},10);//影城内码 OK
	 
		}
		//全量重算
		public void pointrepair(String bar){
		   // int falg =0;
			EtlBean mapping=EtlConfig.getInstance().getEtlBean("gbcinemarepair");// 
			SolarEtlExecutor.runetlFixedThread(mapping,new String[]{"-bizdate",bar},10);//影城内码 OK 10 为分线程
	 
		}
	//查询状态
	public int fandstatus(String bate){
		Connection connposFlag=null;
 
		PreparedStatement ps=null;
 
		 
		 int flag =0;
		 
			//args[] is null 算前一个营业日积分及已经ｏｋ的影城积分
			String bar=bate;
	
			String[] dz = bar.split("-");
			String da = dz[0]+dz[1]+dz[2];
			long ymd = Long.valueOf(da);
		
			ResultSet rs = null;
		//	ResultSet rs1 = null;
			try {
				String sql = "select j.ymd, j.flag_mbr,j.flag_mbr_points_repair,j.time_mbr, "
				     +"  j.time_mbr_repair, j.flag_mbr_points, j.time_mbr_points, j.seqid   "
				     +"  from CCS_REPORT.T_SYS_DATA_JOB j  where ymd=? and FLAG_MBR=1 and FLAG_ETL_SQL=1 and FLAG_MBR_POINTS=0";
				connposFlag=AllMbrPoint.mbr;
				ps = connposFlag.prepareStatement(sql);
				ps.setLong(1,ymd);
				LogUtils.debug("状态表sql:"+sql);
				LogUtils.debug("状态表参数:"+ymd);
				rs=ps.executeQuery();
				if (rs!=null&&rs.next()) {
 
					flag=1;
 
 
				}
			
			
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if(ps!=null){
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					logger.error(e);
					e.printStackTrace();
				}
			}
			if(rs!=null){

				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					logger.error(e);
					e.printStackTrace();
				}
				rs=null;	
			}
			
		} 	
 
		return  flag ;
	}
	//修复
	public int repair(){
		 int flag =0;
		Connection connposFlag=null;
		PreparedStatement ps2=null;
		PreparedStatement ps3=null;
		ResultSet rs1 = null;
		try {
		String xfsql =  "select r.ymd,r.flag_pos_repair,r.flag_olap_repair,r.flag_sql_repair,r.flag_cube_olap_repair,r.flag_cube_sql_repair, r.cinema_code, "
			+"      r.flag_mbre_repair,r.time_mbre_repair  from CCS_REPORT.T_SYS_DATA_JOB_REPAIR r  "
			+"  where  r.flag_pos_repair=1   and r.flag_sql_repair=1   and r.flag_mbre_repair=0";
			
			
			connposFlag=AllMbrPoint.mbr;
		ps2 = connposFlag.prepareStatement(xfsql);
		 
		 LogUtils.debug("修复数据状态查询SQL:"+xfsql);
		  
		rs1=ps2.executeQuery();
		if(rs1!=null){
		while(rs1!=null&& rs1.next()) {
			String foo="recaluc";
			String bar= rs1.getString("CINEMA_CODE");
			String baz=rs1.getString("YMD");
			String [] ar ={foo,bar,baz};
			Basic.main(ar);
			EtlBean xbmapping=EtlConfig.getInstance().getEtlBean("xbtsuphistory");// 
			SolarEtlExecutor.runetl(xbmapping,new String[]{"-bizdate",baz,"-cinema",bar},1);  //时间是查出来的。
			ps3 = connposFlag.prepareStatement("update CCS_REPORT.T_SYS_DATA_JOB_REPAIR set FLAG_MBRE_REPAIR=?, time_mbre_repair=sysdate where YMD=? and  FLAG_POS_REPAIR=1   and FLAG_SQL_REPAIR=1   and FLAG_MBRE_REPAIR=0 and cinema_code=? ");
			ps3.setLong(1,1);
			ps3.setString(2,baz);
			ps3.setString(3,bar);
		 
			ps3.executeUpdate();
		}
		}
		 //把所有影票表中is_ticket 是0的定级与非定级，总积分初始化为0。
		
	}catch (SQLException e) {
		e.printStackTrace();
	}finally {
		if(ps3!=null){
			try {
				ps3.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.error(e);
				e.printStackTrace();
			}
		}
		if(rs1!=null){

			try {
				rs1.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.error(e);
				e.printStackTrace();
			}
			rs1=null;	
		}
		
	} 	
	return    flag ;
	}
	//更新状态
	public int updatestatus(Long ymd){
		Connection connposFlag=null;
		PreparedStatement ps1=null;
		 int flag =0;
			try {
				connposFlag	=AllMbrPoint.mbr;
			ps1 = connposFlag.prepareStatement("update CCS_REPORT.T_SYS_DATA_JOB set  FLAG_MBR_POINTS=?,time_mbr_points=sysdate where ymd=? and  FLAG_MBR=1 and FLAG_ETL_SQL=1 and FLAG_MBR_POINTS=0");
			ps1.setLong(1,1);
			ps1.setLong(2,ymd);
			ps1.executeUpdate();
			flag=1;
			}catch (SQLException e) {
				e.printStackTrace();
			}finally{

				if(ps1!=null){
					try {
						ps1.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						logger.error(e);
						e.printStackTrace();
					}
				}
			
			}
			return flag;
		
		
	}
	//推送
	public int send(String bar){
		 int flag =0;
		EtlBean uphmapping=EtlConfig.getInstance().getEtlBean("tsuphistory");// 
		SolarEtlExecutor.runetlFixedThread(uphmapping,new String[]{"-bizdate",bar},10);  
		
		 return flag;

	}

//	}

}
