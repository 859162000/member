package com.wanda.mms.control.stream.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import org.apache.log4j.Logger;

import com.wanda.mms.control.stream.dao.T_event_upgradeDao;
import com.wanda.mms.control.stream.util.DateUtil;
import com.wanda.mms.control.stream.vo.T_event_upgrade;

public class T_event_upgradeDaoImpl implements T_event_upgradeDao {
	static Logger logger = Logger.getLogger(T_event_upgradeDaoImpl.class.getName());

	@Override
	public int addTeventupgrade(Connection conn,T_event_upgrade teu) {
		// TODO Auto-generated method stub
		int flag=0;
		PreparedStatement pst=null;// 	
		//序列名不对以后改过来  select S_T_F_VOUCHER.nextVal from dual
		//得到之后在插入 并把这个值回传。
		Date da = new Date();
		String date = DateUtil.getDateStrss(da);//时间可能是 时分秒
	//	String sql = "insert into t_im_module_log (seqid,dataymd,moduleid,starttime,endtime,status,DESCRIPTION)values(S_T_F_VOUCHER.nextVal,?,?,to_date('" + lm.getStartTime() + "','yyyy-mm-dd hh24:mi:ss'),to_date('" + lm.getEndTime() + "','yyyy-mm-dd hh24:mi:ss'),?,?)"; 
		String sql = "insert into T_EVENT_UPGRADE(SEQ_ID,MEMBER_ID,MEMBER_LEVEL,STATUS,CREATE_TIME )" +
				 "	values(S_T_EVENT_UPGRADE.nextVal,?,?,?,sysdate)";
		try {
				
			pst=conn.prepareStatement(sql);
			//System.out.println(sql);
 
			pst.setLong(1,teu.getMember_id());//会员ID
			pst.setString(2,teu.getMember_level()); //当前级别，就是升级后的级别
			pst.setString(3,teu.getStatus()); //0：未发送，1：已发送
		//	pst.setString(4,teu.getCreate_time()); //任务创建时间
 
			flag = pst.executeUpdate(); 
		//	System.out.println("添加日志信息  数据日期 YYYYMMDD"+lm.getDataYMD()+"模块ID . 1-数据采集 2-指标计算 3-发送任务生成 4-发送器"+lm.getDescription()+"开始时间"+lm.getStartTime()+"结束时间"+lm.getEndTime()+"执行结果 0-成功，其他-失败"+lm.getStatus()+"结果描述"+lm.getDescription());
 
	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
		 
				logger.error(e);
		 

			e.printStackTrace();
		}  finally {
			if(pst!=null){
				try {
					pst.close();
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
