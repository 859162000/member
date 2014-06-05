package com.wanda.mms.control.stream.dao.impl;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.wanda.mms.control.stream.dao.LogModuleDao;
import com.wanda.mms.control.stream.db.SENDDBConnection;
import com.wanda.mms.control.stream.vo.LogModule;



public class LogModuleDaoImpl implements LogModuleDao {

	private Connection conn = null;// 数据数据库连接属性 conn，用于接收数据连接
	private PreparedStatement pst = null;// 数据库预先编译声明类型的属性 pst 用于操作数据库

	// 添加日志信息
	public void addLogModuleDao(LogModule lm) {

		// 序列名不对以后改过来 select S_T_F_VOUCHER.nextVal from dual
		// 得到之后在插入 并把这个值回传。

		String sql = "insert into t_im_module_log (seqid,dataymd,moduleid,starttime,endtime,status,DESCRIPTION)values(S_T_IM_MODULE_LOG.nextVal,?,?,to_date('"
				+ lm.getStartTime()
				+ "','yyyy-mm-dd hh24:mi:ss'),to_date('"
				+ lm.getEndTime() + "','yyyy-mm-dd hh24:mi:ss'),?,?)";

		SENDDBConnection db = SENDDBConnection.getInstance();
		conn = db.getConnection();

		try {
			pst = conn.prepareStatement(sql);
			pst.setLong(1, lm.getDataYMD());
			pst.setInt(2, lm.getModuleID());

			pst.setInt(3, lm.getStatus());
			pst.setString(4, lm.getDescription());
			System.out.println("日志"+sql+"条件1: "+lm.getDataYMD()+"   条件2: "+lm.getModuleID()+"条件3:"+lm.getStatus()+"条件4:"+lm.getDescription());
			pst.executeUpdate();
			// System.out.println("添加日志信息  数据日期 YYYYMMDD"+lm.getDataYMD()+"模块ID . 1-数据采集 2-指标计算 3-发送任务生成 4-发送器"+lm.getDescription()+"开始时间"+lm.getStartTime()+"结束时间"+lm.getEndTime()+"执行结果 0-成功，其他-失败"+lm.getStatus()+"结果描述"+lm.getDescription());

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
