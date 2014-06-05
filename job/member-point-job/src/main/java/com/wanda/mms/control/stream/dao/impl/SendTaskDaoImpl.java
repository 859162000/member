package com.wanda.mms.control.stream.dao.impl;

 


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.wanda.mms.control.stream.dao.SendTaskDao;
import com.wanda.mms.control.stream.db.SENDDBConnection;
import com.wanda.mms.control.stream.util.DateUtil;
import com.wanda.mms.control.stream.vo.SendTask;
 

 

public class SendTaskDaoImpl implements SendTaskDao {

	static Logger logger = Logger.getLogger(SendTaskDaoImpl.class.getName());
	/**
	 * 插入任务对列
	 * @param stlist
	 * @return
	 */
	public String addSendTask(List<SendTask> stlist) {
		  Connection conn=null;// 数据数据库连接属性 conn，用于接收数据连接
		  PreparedStatement pst=null;//  数据库预先编译声明类型的属性 pst 用于操作数据库
		//  ResultSet rs=null;// 结果集类型的 属性 rs 用于接收返回的结果集
		
		// TODO Auto-generated method stub
		int[] aa = null;
		String getSendTime;
		 //序列名不对以后改过来
		String sql = "insert into t_im_sendtask (seqid,TARGETID,msgId,sendTime,status,retryTimes,TargetMobile,MsgType,priority)values(S_T_IM_SENDTASK.Nextval,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?,?,?,?)";// 
		SENDDBConnection db=SENDDBConnection.getInstance();	
		conn=db.getConnection();
		String flag ="";
		try {
			pst=conn.prepareStatement(sql);
			for (int i = 0; i < stlist.size(); i++) {
				SendTask st =new SendTask();
				st = stlist.get(i);
				pst.setLong(1, st.getTargetId());
				pst.setLong(2, st.getMsgId());				
			 
				//to_date('" + st.getSendTime() + "','yyyy-mm-dd hh24:mi:ss'))";
				// System.out.println("插入任务对列方法调用 : 接收人是"+st.getTargetId()+"  信息发送内容ID=" +st.getMsgId());
				pst.setString(3,st.getSendTime() );
				pst.setLong(4, st.getStatus());
				pst.setLong(5, st.getRetryTimes());
				pst.setString(6, st.getTargetMobile());
				pst.setLong(7, st.getMsgType());
				pst.setLong(8, st.getPriority());
				pst.addBatch();
				
			}
		    aa = pst.executeBatch(); 
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < aa.length; i++) {	
				sb.append(aa.toString()).append(",");
			}
			flag = sb.toString();
			
		// System.out.println("插入任务对列方法调用结束");
			conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
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
		
		 
//		}
		return flag;	//返回状态标识
		
	 
	}
	/**
	 * 插入任务对列
	 * @param stlist
	 * @param SendTime
	 * @param msgId
	 * @return String
	 */
	public String addSendTask(List<SendTask> stlist,String SendTime ,Long msgId) {
		  Connection conn=null;// 数据数据库连接属性 conn，用于接收数据连接
		  PreparedStatement pst=null;//  数据库预先编译声明类型的属性 pst 用于操作数据库
		  ResultSet rs=null;// 结果集类型的 属性 rs 用于接收返回的结果集

		// TODO Auto-generated method stub
		int[] aa = null;
		String getSendTime;
		 //序列名不对以后改过来
		String sql = "insert into t_im_sendtask (seqid,TARGETID,msgId,sendTime,status,retryTimes,TargetMobile,MsgType,priority)values(S_T_D_ACTOR.Nextval,?,"+ msgId +",to_date('" + SendTime + "','yyyy-mm-dd hh24:mi:ss'),?,?,?,?,?)";// 
		SENDDBConnection db=SENDDBConnection.getInstance();	
		conn=db.getConnection();
		String flag ="";
		try {
			pst=conn.prepareStatement(sql);
			for (int i = 0; i < stlist.size(); i++) {
				SendTask st =new SendTask();
				st = stlist.get(i);
				pst.setLong(1, st.getTargetId());
			//	pst.setLong(2, st.getMsgId());				
			 
				//to_date('" + SendTime + "','yyyy-mm-dd hh24:mi:ss'))";
			//	 System.out.println("addSendTask SQL 是  ="+sql );
				 
				pst.setLong(2, st.getStatus());
				pst.setLong(3, st.getRetryTimes());
				pst.setString(4, st.getTargetMobile());
				pst.setLong(5, st.getMsgType());
				pst.setLong(6, st.getPriority());
				pst.addBatch();
				
			}
		    aa = pst.executeBatch(); 
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < aa.length; i++) {	
				sb.append(aa.toString()).append(",");
			}
			flag = sb.toString();
		 //System.out.println("t_im_sendtask 写入任务对列");
			conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
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
		
		 
//		}
		return flag;	//返回状态标识
		
	 
	}
	
	/**
	 * 查询有多少符合要求的手机号
	 * @param sendtypeid
	 * @param msgId
	 * @param SendTime
	 * @return List<SendTask>
	 */
	public List<SendTask> fandTargetandaddTaskSendTask(Long sendtypeid,Long msgId,String SendTime) {
		  Connection conn=null;// 数据数据库连接属性 conn，用于接收数据连接
		  PreparedStatement pst=null;//  数据库预先编译声明类型的属性 pst 用于操作数据库
		  ResultSet rs=null;// 结果集类型的 属性 rs 用于接收返回的结果集

		// TODO Auto-generated method stub
		List<SendTask> stlist = new ArrayList<SendTask>();
		
		String sql = " select gt.targetid, t.mobile, t.rtx, gs.msgtype, gs.needrtx, gs.priority  "
			+"   from t_im_target t, t_im_group_targets gt, t_im_group_sendtypes gs  "
			+"  where gs.sendtypeid= ? and gs.groupid=gt.groupid and t.seqid=gt.targetid  "
			+"  union select ts.targetid, t.mobile, t.rtx,ts.msgtype,  ts.needrtx, ts.priority "
			+"    from t_im_target t, t_im_target_sendtypes ts where ts.sendtypeid=? and "
			+"  ts.targetid=t.seqid  ";
		
		SENDDBConnection db=SENDDBConnection.getInstance();	
		conn=db.getConnection();
		
		try {
			pst=conn.prepareStatement(sql);
			pst.setLong(1, sendtypeid);
			pst.setLong(2, sendtypeid);
			rs=pst.executeQuery();
			while (rs.next()) {
				
				SendTask sendTask =new SendTask();
				sendTask.setTargetId(rs.getLong("targetid"));
				sendTask.setTargetMobile(rs.getString("mobile"));//接收者手机号
				sendTask.setTargetRTX(rs.getString("rtx"));
				sendTask.setMsgType(rs.getInt("msgtype"));
				sendTask.setNeedRTX(rs.getInt("needrtx"));
				sendTask.setPriority(rs.getInt("priority"));
				
				sendTask.setMsgId(msgId);
				//查时得一下			
				sendTask.setSendTime(SendTime);//信息发送时间 new 一个Date到时分秒
				sendTask.setStatus(0);//发送状态. 0-带发送,1-发送成功,2-发送中,3-失败重试
				sendTask.setRetryTimes(0);//重试次数
				stlist.add(sendTask);
			}
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(pst!=null){
				try {
					pst.close();
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
		
	
		
		
		return stlist;
	}

	/**
	 * 批量更新 发送消息任务功能。
	 * @param stlist
	 * @param sendtime
	 * @return  String
	 */
	public String updateListSendTask(List<SendTask> stlist,String sendtime) {
		// TODO Auto-generated method stub
		  Connection conn=null;// 数据数据库连接属性 conn，用于接收数据连接
		  PreparedStatement pst=null;//  数据库预先编译声明类型的属性 pst 用于操作数据库
		  ResultSet rs=null;// 结果集类型的 属性 rs 用于接收返回的结果集

		
		int flag=0;
		StringBuffer sf = new StringBuffer();
		String sql="UPDATE T_IM_SENDTASK SET Status=?,sendtime=to_date('" + sendtime + "','yyyy-mm-dd hh24:mi:ss'),retrytimes=? WHERE seqid=?";
		SENDDBConnection db=SENDDBConnection.getInstance();	
		try {
			conn=db.getConnection();
			pst=conn.prepareStatement(sql);
			for (int i = 0; i < stlist.size(); i++) {
				SendTask st = stlist.get(i);
				pst.setInt(1, st.getStatus());
				 //信息发送时间
				//to_date('" + sendtime + "','yyyy-mm-dd hh24:mi:ss'))";
				
				pst.setInt(2,st.getRetryTimes());
				pst.setLong(3, st.getSendTaskId());
				flag=pst.executeUpdate();
				sf.append(String.valueOf(flag)).append(",");
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
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
		String s =sf.toString();
		return s;
	}

	/**
	 * 更新 发送消息任务功能。
	 * @param sendtask
	 * @param sendtime
	 * @return  int
	 */
	public int updateListSendTask(SendTask sendtask,String datetime) {
		// TODO Auto-generated method stub
		  Connection conn=null;// 数据数据库连接属性 conn，用于接收数据连接
		  PreparedStatement pst=null;//  数据库预先编译声明类型的属性 pst 用于操作数据库
		  ResultSet rs=null;// 结果集类型的 属性 rs 用于接收返回的结果集

		int flag=0;
		 
		String sql="UPDATE T_IM_SENDTASK SET Status=?,sendtime=to_date('" + datetime + "','yyyy-mm-dd hh24:mi:ss'),retrytimes=? WHERE seqid=?";
		SENDDBConnection db=SENDDBConnection.getInstance();	
		try {
			conn=db.getConnection();
			pst=conn.prepareStatement(sql);
			 
				SendTask st = sendtask;
				pst.setInt(1, st.getStatus());
				 //信息发送时间
				//to_date('" + sendtime + "','yyyy-mm-dd hh24:mi:ss'))";
				
				pst.setInt(2,st.getRetryTimes());
				pst.setLong(3, st.getSendTaskId());
				flag=pst.executeUpdate();
				 
			 
		
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
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
	/**
	 * 得到同一信息发送内容得任务
	 * @param msgid
	 * @return  List<SendTask>
	 */
	public List<SendTask> fandMsgCode(Long  msgid) {
		  Connection conn=null;// 数据数据库连接属性 conn，用于接收数据连接
		  PreparedStatement pst=null;//  数据库预先编译声明类型的属性 pst 用于操作数据库
		  ResultSet rs=null;// 结果集类型的 属性 rs 用于接收返回的结果集

		// TODO Auto-generated method stub
		List<SendTask> stlist = new ArrayList<SendTask>();
		
		String sql = "select SEQID,TARGETID,MSGID,SENDTIME,STATUS,RETRYTIMES,TARGETMOBILE,MSGTYPE,PRIORITY from T_IM_SENDTASK t where msgid=?   ";
		SENDDBConnection db=SENDDBConnection.getInstance();	
		conn=db.getConnection();
		
		try {
			pst=conn.prepareStatement(sql);
			pst.setLong(1, msgid);
		
			rs=pst.executeQuery();
			while (rs.next()) {
				
				SendTask sendTask =new SendTask();
				sendTask.setSendTaskId(rs.getLong("SEQID"));
				sendTask.setTargetId(rs.getLong("targetid"));
				sendTask.setMsgId(rs.getLong("msgId"));
				sendTask.setSendTime(rs.getString("sendtime"));//信息发送时间 new 一个Date到时分秒
				sendTask.setStatus(rs.getInt("Status"));//发送状态. 0-带发送,1-发送成功,2-发送中,3-失败重试
				sendTask.setRetryTimes(rs.getInt("RetryTimes"));//重试次数
				sendTask.setTargetMobile(rs.getString("TARGETMOBILE"));//接收者手机号
				sendTask.setMsgType(rs.getInt("msgtype"));
				sendTask.setPriority(rs.getInt("priority"));
				
				
				
 				
//				sendTask.setTargetRTX(rs.getString("rtx"));
//				sendTask.setNeedRTX(rs.getInt("needrtx"));
				
				//查时得一下			

//				sendTask.setStatus(rs.getInt("Status"));//发送状态. 0-带发送,1-发送成功,2-发送中,3-失败重试
//				sendTask.setRetryTimes(rs.getInt("RetryTimes"));//重试次数
				stlist.add(sendTask);
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if(pst!=null){
				try {
					pst.close();
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
			
		return stlist;
		}
	
	
	/**
	 * 查询有多少符合要求消息发送者
	 *  
	 * @param msgId
	 * @param retrytimes
	 * @return List<SendTask>
	 */
	public List<SendTask> fandAllTaskSend(Long msgId,int retrytimes) {
		  Connection conn=null;// 数据数据库连接属性 conn，用于接收数据连接
		  PreparedStatement pst=null;//  数据库预先编译声明类型的属性 pst 用于操作数据库
		  ResultSet rs=null;// 结果集类型的 属性 rs 用于接收返回的结果集

		// TODO Auto-generated method stub
		List<SendTask> stlist = new ArrayList<SendTask>();
		
		String sql = "  select SEQID,TARGETID,MSGID,SENDTIME,STATUS,RETRYTIMES,TARGETMOBILE,MSGTYPE,PRIORITY" +
				" from T_IM_SENDTASK t where MSGID=? and  (status=0 or status=3) and retrytimes<?   ";
		
		SENDDBConnection db=SENDDBConnection.getInstance();	
		conn=db.getConnection();
		
		try {
			pst=conn.prepareStatement(sql);
			//System.out.println("msgId"+msgId);
			pst.setLong(1, msgId);
			pst.setLong(2, retrytimes);
			rs=pst.executeQuery();
			while (rs.next()) {
				
				SendTask sendTask =new SendTask();
				sendTask.setSendTaskId(rs.getLong("SEQID"));
				sendTask.setTargetId(rs.getLong("targetid"));
				sendTask.setMsgId(rs.getLong("MSGID"));
				String sst = DateUtil.getDateStrss( rs.getDate("SENDTIME"));
				sendTask.setSendTime( sst);//信息发送时间 new 一个Date到时分秒
				sendTask.setStatus(rs.getInt("STATUS"));//发送状态. 0-带发送,1-发送成功,2-发送中,3-失败重试
				sendTask.setRetryTimes(rs.getInt("RETRYTIMES"));//重试次数
				sendTask.setTargetMobile(rs.getString("TARGETMOBILE"));//接收者手机号
				sendTask.setMsgType(rs.getInt("msgtype"));
				sendTask.setPriority(rs.getInt("priority"));
//				sendTask.setTargetRTX(rs.getString("rtx"));
//
//				sendTask.setNeedRTX(rs.getInt("needrtx"));
				//查时得一下		
				stlist.add(sendTask);
			}
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(pst!=null){
				try {
					pst.close();
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
		
	
		
		
		return stlist;
	}
	
	/**
	 * 按优先级 查询有多少符合要求消息发送者
	 *  
	 * @param msgId
	 * @param priority
	 * @return List<SendTask>
	 */
	public List<SendTask> fandTaskSendByPriority(Long msgId, int priority) {
		  Connection conn=null;// 数据数据库连接属性 conn，用于接收数据连接
		  PreparedStatement pst=null;//  数据库预先编译声明类型的属性 pst 用于操作数据库
		  ResultSet rs=null;// 结果集类型的 属性 rs 用于接收返回的结果集

		// TODO Auto-generated method stub
		
	List<SendTask> stlist = new ArrayList<SendTask>();
		
		String sql = "  select SEQID,TARGETID,MSGID,SENDTIME,STATUS,RETRYTIMES,TARGETMOBILE,MSGTYPE,PRIORITY" +
				" from T_IM_SENDTASK t where MSGID=? and  (status=0 or status=3) and PRIORITY=?   ";
		
		SENDDBConnection db=SENDDBConnection.getInstance();	
		conn=db.getConnection();
		
		try {
			pst=conn.prepareStatement(sql);
			//System.out.println("msgId"+msgId);
			pst.setLong(1, msgId);
			pst.setLong(2, priority);
			rs=pst.executeQuery();
			while (rs.next()) {
				
				SendTask sendTask =new SendTask();
				sendTask.setSendTaskId(rs.getLong("SEQID"));
				sendTask.setTargetId(rs.getLong("targetid"));
				sendTask.setMsgId(rs.getLong("MSGID"));
				String sst = DateUtil.getDateStrss( rs.getDate("SENDTIME"));
				sendTask.setSendTime( sst);//信息发送时间 new 一个Date到时分秒
				sendTask.setStatus(rs.getInt("STATUS"));//发送状态. 0-带发送,1-发送成功,2-发送中,3-失败重试
				sendTask.setRetryTimes(rs.getInt("RETRYTIMES"));//重试次数
				sendTask.setTargetMobile(rs.getString("TARGETMOBILE"));//接收者手机号
				sendTask.setMsgType(rs.getInt("msgtype"));
				sendTask.setPriority(rs.getInt("priority"));
//				sendTask.setTargetRTX(rs.getString("rtx"));
//
//				sendTask.setNeedRTX(rs.getInt("needrtx"));
				//查时得一下		
				stlist.add(sendTask);
			}
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(pst!=null){
				try {
					pst.close();
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
		
	
		
		
		return stlist;
	 
	}
	public String addSendTaskRtx(List<SendTask> stlist, String sendTime,Long msgId) {
		  Connection conn=null;// 数据数据库连接属性 conn，用于接收数据连接
		  PreparedStatement pst=null;//  数据库预先编译声明类型的属性 pst 用于操作数据库
		  ResultSet rs=null;// 结果集类型的 属性 rs 用于接收返回的结果集

		// TODO Auto-generated method stub
		
		
		int[] aa = null;
		String getSendTime;
		 //序列名不对以后改过来
		String sql = "insert into t_im_sendtask_rtx (seqid,TARGETID,sendTime,status,retryTimes,TargetRtx,msgId,priority)values(S_T_D_ACTOR.Nextval,?,to_date('" + sendTime + "','yyyy-mm-dd hh24:mi:ss'),?,?,?,"+ msgId +",?)";// 
		SENDDBConnection db=SENDDBConnection.getInstance();	
		conn=db.getConnection();
		String flag ="";
		try {
			pst=conn.prepareStatement(sql);
			for (int i = 0; i < stlist.size(); i++) {
				SendTask st =new SendTask();
				st = stlist.get(i);
				pst.setLong(1, st.getTargetId());
			//	pst.setLong(2, st.getMsgId());				
			 
				//to_date('" + SendTime + "','yyyy-mm-dd hh24:mi:ss'))";
			//	 System.out.println("addSendTask SQL 是  ="+sql );
				pst.setLong(2, st.getStatus());
				pst.setLong(3, st.getRetryTimes());
				pst.setString(4, st.getTargetRTX());
				pst.setLong(5, st.getPriority());
				pst.addBatch();
				
			}
		    aa = pst.executeBatch(); 
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < aa.length; i++) {	
				sb.append(aa.toString()).append(",");
			}
			flag = sb.toString();
		 //System.out.println("t_im_sendtask 写入任务对列");
			conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
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
		
		 
//		}
		return flag;	//返回状态标识
	}
	public List<SendTask> fandAllTaskSendRtx(Long msgId, int retrytimes) {
		  Connection conn=null;// 数据数据库连接属性 conn，用于接收数据连接
		  PreparedStatement pst=null;//  数据库预先编译声明类型的属性 pst 用于操作数据库
		  ResultSet rs=null;// 结果集类型的 属性 rs 用于接收返回的结果集

		// TODO Auto-generated method stub
		
		List<SendTask> stlist = new ArrayList<SendTask>();
		
		String sql = "  select SEQID,TARGETID,SENDTIME,STATUS,RETRYTIMES,TARGETRTX,MSGID,PRIORITY " +
				" from T_IM_SENDTASK_RTX t where MSGID=? and  (status=0 or status=3) and retrytimes<?   ";
		
		SENDDBConnection db=SENDDBConnection.getInstance();	
		conn=db.getConnection();
		
		try {
			pst=conn.prepareStatement(sql);
			//System.out.println("msgId"+msgId);
			pst.setLong(1, msgId);
			pst.setLong(2, retrytimes);
			rs=pst.executeQuery();
			while (rs.next()) {
				
				SendTask sendTask =new SendTask();
				sendTask.setSendTaskId(rs.getLong("SEQID"));
				sendTask.setTargetId(rs.getLong("targetid"));
				sendTask.setMsgId(rs.getLong("MSGID"));
				String sst = DateUtil.getDateStrss( rs.getDate("SENDTIME"));
				sendTask.setSendTime( sst);//信息发送时间 new 一个Date到时分秒
				sendTask.setStatus(rs.getInt("STATUS"));//发送状态. 0-带发送,1-发送成功,2-发送中,3-失败重试
				sendTask.setRetryTimes(rs.getInt("RETRYTIMES"));//重试次数
				sendTask.setTargetRTX(rs.getString("TARGETRTX"));//接收者手机号
				sendTask.setPriority(rs.getInt("priority"));
//				sendTask.setTargetRTX(rs.getString("rtx"));
//
//				sendTask.setNeedRTX(rs.getInt("needrtx"));
				//查时得一下		
				stlist.add(sendTask);
			}
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(pst!=null){
				try {
					pst.close();
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
		
	
		
		
		return stlist;
	}
	public String updateListSendTaskRtx(List<SendTask> stlist, String datetime) {
		  Connection conn=null;// 数据数据库连接属性 conn，用于接收数据连接
		  PreparedStatement pst=null;//  数据库预先编译声明类型的属性 pst 用于操作数据库
		//  ResultSet rs=null;// 结果集类型的 属性 rs 用于接收返回的结果集

		// TODO Auto-generated method stub
 
		
		
		int flag=0;
		StringBuffer sf = new StringBuffer();
		String sql="UPDATE T_IM_SENDTASK_RTX SET Status=?,sendtime=to_date('" + datetime + "','yyyy-mm-dd hh24:mi:ss'),retrytimes=? WHERE seqid=?";
		SENDDBConnection db=SENDDBConnection.getInstance();	
		try {
			conn=db.getConnection();
			pst=conn.prepareStatement(sql);
			for (int i = 0; i < stlist.size(); i++) {
				SendTask st = stlist.get(i);
				pst.setInt(1, st.getStatus());
				 //信息发送时间
				//to_date('" + sendtime + "','yyyy-mm-dd hh24:mi:ss'))";
				
				pst.setInt(2,st.getRetryTimes());
				pst.setLong(3, st.getSendTaskId());
				flag=pst.executeUpdate();
				sf.append(String.valueOf(flag)).append(",");
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
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
		String s =sf.toString();
		return s;
		
	}

}
