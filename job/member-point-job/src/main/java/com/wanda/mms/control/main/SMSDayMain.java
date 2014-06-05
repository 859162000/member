package com.wanda.mms.control.main;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import org.apache.log4j.Logger;
import com.solar.etl.config.EtlConfig;
import com.solar.etl.db.ConnctionFactory;
import com.solar.etl.log.LogUtils;
import com.talkweb.wanda.det.sms.MbrSMSReport;
import com.wanda.mms.control.stream.db.STAGDBConnection;
import com.wanda.mms.control.stream.service.MbrSMSDay;

public class SMSDayMain {
	static Logger logger = Logger.getLogger(SMSDayMain.class.getName());
 
	static{
		EtlConfig.getInstance();
	}
	public static Connection mbr=ConnctionFactory.getConnection("db.mbr_flag");
	
	public static void main(String[] args) {
		
		if(args==null||args.length==0){	
		Calendar cal=Calendar.getInstance();
		int x=-1;//or x=-3;
		cal.add(Calendar.DAY_OF_MONTH,x);
		String bzda=new java.text.SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
		System.out.println("Bizdate:"+bzda);
		SMSDayMain sdm = new SMSDayMain();
		int flag1 = sdm.fandstatus(bzda);
			if (flag1==1) {
			//Cinema
			LogUtils.debug("状态表状态成功短信开始生成");
			STAGDBConnection db=STAGDBConnection.getInstance();	
			
			MbrSMSDay mbr = new MbrSMSDay();
			
			mbr.daySaveContent(db.getConnection(), bzda);//院线生成短信任务
			
			MbrSMSReport report = new MbrSMSReport();
			report.mbrDayReport(bzda);		//院线发送短信任务
			//改变状态表中的状态短信发送完毕
			String[] dz = bzda.split("-");
			String da = dz[0]+dz[1]+dz[2];
			long ymd = Long.valueOf(da);
			sdm.updatestatus(ymd);
			System.out.println("Bizdate:"+bzda+" SMS OK");
			}else{
				int smsflag= sdm.fandstatussmsflag(bzda);
				if(smsflag==1){
					LogUtils.debug("今天院线短信以发送");
				}else{
					LogUtils.debug("状态表状态暂无请检查状态是否满足条件");
				}
				
			}
		}else if(args!=null&&args.length==2){
			if(args[0].equals("bizdate")&&args[1]!=null ){
			//	String foo=args[0];
				String bizdate=args[1];
				
				System.out.println("Bizdate:"+bizdate);
				SMSDayMain sdm = new SMSDayMain();
				String[] dz = bizdate.split("-");
				String da = dz[0]+dz[1]+dz[2];
				long ymd = Long.valueOf(da);
			 	int flag1 = sdm.fandstatus(bizdate);
				 	if (flag1==1) {
					STAGDBConnection db=STAGDBConnection.getInstance();	
					
					MbrSMSDay mbr = new MbrSMSDay();
					
					mbr.daySaveContent(db.getConnection(), bizdate);
					
					MbrSMSReport report = new MbrSMSReport();
					report.mbrDayReport(bizdate);
					

					
					sdm.updatestatus(ymd);
				 }else{
						int smsflag= sdm.fandstatussmsflag(bizdate);
						if(smsflag==1){
							LogUtils.debug("今天院线短信以发送");
						}else{
							LogUtils.debug("状态表状态暂无请检查状态是否满足条件");
						}
						
					}
					//改变状态表中的状态短信发送完毕
			}
				
		}
		
		//日志与状态。
	}
	
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
			try {//FLAG_SMS
				String sql = "select j.ymd, j.flag_mbr,j.flag_mbr_points_repair,j.time_mbr, "
				     +"  j.time_mbr_repair, j.flag_mbr_points, j.time_mbr_points, j.seqid   "
				     +"  from CCS_REPORT.T_SYS_DATA_JOB j  where ymd=? and FLAG_ETL_SQL_POINTS=1   and FLAG_SMS=0 ";
				connposFlag=CinemaSMSDayMain.mbr;
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
	
	public int fandstatussmsflag(String bate){
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
			try {//FLAG_SMS
				String sql = "select j.ymd, j.flag_mbr,j.flag_mbr_points_repair,j.time_mbr, "
				     +"  j.FLAG_SMS, j.flag_mbr_points, j.time_mbr_points, j.seqid   "
				     +"  from CCS_REPORT.T_SYS_DATA_JOB j  where ymd=? and FLAG_SMS=1    ";
				connposFlag=CinemaSMSDayMain.mbr;
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
	
	//更新状态
	public int updatestatus(Long ymd){
		Connection connposFlag=null;
		PreparedStatement ps1=null;
		 int flag =0;
			try {
				connposFlag	=AllMbrPoint.mbr;
			ps1 = connposFlag.prepareStatement("update CCS_REPORT.T_SYS_DATA_JOB set  FLAG_SMS=?,TIME_MBR_BASE=sysdate where ymd=? and FLAG_ETL_SQL_POINTS=1 ");
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

}
