package com.wanda.mms.control.stream.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.wanda.mms.control.stream.UpLevel;
import com.wanda.mms.control.stream.dao.LevelHistoryDao;
import com.wanda.mms.control.stream.dao.MemberLevelDao;
import com.wanda.mms.control.stream.util.DateUtil;
import com.wanda.mms.control.stream.vo.LevelHistory;
import com.wanda.mms.control.stream.vo.MemberLevel;

public class LevelHistoryDaoImpl implements LevelHistoryDao {
	static Logger logger = Logger.getLogger(LevelHistoryDaoImpl.class.getName());

	//private Connection conn=null;// 数据数据库连接属性 conn，用于接收数据连接
	
	@Override
	public String addLevelHistory(Connection conn, LevelHistory levelhistory,MemberLevel mblerel) {
		// TODO Auto-generated method stub
		  PreparedStatement pst=null;//  数据库预先编译声明类型的属性 pst 用于操作数据库
		int flag=0;
		int flagup =0;
		String str ="";
		String sflag ="";
		//long seq = seqLevelHistroy(conn);
		
		//序列名不对以后改过来  select S_T_F_VOUCHER.nextVal from dual
		//得到之后在插入 并把这个值回传。
		Date da = new Date();
		String date = DateUtil.getDateStrss(da);//时间可能是 时分秒
		String yy = (String) date.subSequence(0, 4);
		int intyy = Integer.valueOf(yy);
		intyy = intyy +1;
		String yyyy = String.valueOf(intyy);
		String te = "-12-31 23:59:59";
		String time =yyyy+te; 
		//	levelhistory.setExpire_date(time);
	 	String sql = "insert into T_LEVEL_HISTORY(LEVEL_HISTORY_ID,MEM_LEVEL,EXPIRE_DATE,ORG_MEM_LEVEL,ORG_EXPIRE_DATE,SET_TIME,RESON_TYPE,REASON,CHG_TYPE,MEMBER_ID," +
				"	LEVEL_POINT,TICKET_COUNT,ISDELETE,CREATE_BY,CREATE_DATE,UPDATE_BY,UPDATE_DATE,VERSION)" +
				"	values("+levelhistory.getLevel_history_id()+",?,to_date('" + time + "','yyyy-mm-dd hh24:mi:ss'),?,?,sysdate " +
						" ,?,?,?,?,?,?,?,?,sysdate,?,sysdate,?)";
		//String sql="UPDATE T_IM_SENDTASK SET Status=?,sendtime=to_date('" + sendtime + "','yyyy-mm-dd hh24:mi:ss'),retrytimes=? WHERE seqid=?";
		
		//System.out.println("sql = "+sql);
		
		//T_MEMBER_LEVEL
		
		MemberLevelDao mldao = new  MemberLevelDaoImpl();
		
//		SENDDBConnection db=SENDDBConnection.getInstance();	
//		conn=db.getConnection();
		//MemberLevel ml = mldao.fandMemberLevelByID(conn, levelhistory.getMember_id());
		try {
			conn.setAutoCommit(false);
			
			pst=conn.prepareStatement(sql);
			
			pst.setString(1,levelhistory.getMem_level());//变更后级别#MEM_LEVEL
			//levelhistory.setChg_type(ml.getMem_level());
			pst.setString(2,levelhistory.getOrg_mem_level());//变更前级别#ORG_MEM_LEVEL
			
			//to_date('" + lm.getStartTime() + "','yyyy-mm-dd hh24:mi:ss'))";
			
			pst.setString(3,levelhistory.getOrg_expire_date());//变更前级别有效期#ORG_EXPIRE_DATE
			pst.setString(4,levelhistory.getReson_type()); //变更原因类型#RESON_TYPE
			pst.setString(5, levelhistory.getREASON());//人工输入；升/降级计算:"member_sys"
			pst.setString(6, levelhistory.getChg_type());//别变更类型(UP:升级,DOWN:降级)
			pst.setLong(7, levelhistory.getMember_id());//会员ID
			pst.setLong(8, levelhistory.getLevel_point());//判定用户可升级或降级的有效积分数,人工调整时,不记录。用于镜像变更前后值
			pst.setLong(9, levelhistory.getTicket_count());//判定用户可升级或降级的有效积分数,人工调整时,不记录。用于镜像变更前后值
			pst.setInt(10, levelhistory.getIsdelete());
			pst.setString(11, levelhistory.getCreate_By());
			pst.setString(12,levelhistory.getUpdate_By());
			pst.setLong(13,levelhistory.getVersion());
			flag = pst.executeUpdate();
			//更新会员级别
			//String Mem_level= ml.getMem_level();  
			
		//	ml.setSet_time(date);//级别变更即生效时间
			
		 
			 if(mblerel.getMember_level_history_id()==0){
				 mblerel.setMember_level_history_id(levelhistory.getLevel_history_id());
			 }else{
				 
			 }
			flagup = mldao.updateMemberLevelByID(conn, mblerel);//更新会员级别
		 
				
				
			 
			
			conn.commit();
			str = flag+","+flagup;
			//System.out.println(str);
			  
		} catch (SQLException e) {
			
			// TODO Auto-generated catch block
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			 
				logger.error(e);
			 
			e.printStackTrace();
		} finally {
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
		
		return str;  
	}

	@Override
	public String addListLevelHistory(Connection conn,
			List<LevelHistory> levelhistorylist) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long seqLevelHistroy(Connection conn) {
		// TODO Auto-generated method stub
		
		logger.info("Entering seqLevelHistroy()");
		Long seq = null  ;
		  PreparedStatement pst=null;//  数据库预先编译声明类型的属性 pst 用于操作数据库
		  ResultSet rs=null;// 结果集类型的 属性 rs 用于接收返回的结果集
		
		String sql = "select S_T_LEVEL_HISTORY.nextVal seq from dual";
		//SENDDBConnection db=SENDDBConnection.getInstance();	
		//conn=db.getConnection();
		
		try {
			pst=conn.prepareStatement(sql);
			rs=pst.executeQuery();
			while (rs.next()) {
				 seq=(rs.getLong("seq"));
			}
			 
			
		} catch (SQLException e) {
			 
				logger.error(e);
			 
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
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
		return seq;
	}

	@Override
	public String updateLevelHistory(Connection conn, LevelHistory levelhistory) {
		// TODO Auto-generated method stub
		
		return null;
	}

	@Override
	public String addLevelHistoryByInitialization(Connection conn,
			LevelHistory levelhistory) {
		// TODO Auto-generated method stub
		  PreparedStatement pst=null;//  数据库预先编译声明类型的属性 pst 用于操作数据库
		int flag=0;
		int flagup =0;
		String str ="";
		String sflag ="";

		Date da = new Date();
		String date = DateUtil.getDateStrss(da);//时间可能是 时分秒
		String yy = (String) date.subSequence(0, 4);
		int intyy = Integer.valueOf(yy);
		intyy = intyy +1;
		String yyyy = String.valueOf(intyy);
		String te = "-12-31 23:59:59";
		String time =yyyy+te; 
		//	levelhistory.setExpire_date(time);
	 	String sql = "insert into T_LEVEL_HISTORY(LEVEL_HISTORY_ID,MEM_LEVEL,EXPIRE_DATE,ORG_MEM_LEVEL,ORG_EXPIRE_DATE,SET_TIME,RESON_TYPE,REASON,CHG_TYPE,MEMBER_ID," +
				"	LEVEL_POINT,TICKET_COUNT,ISDELETE,CREATE_BY,CREATE_DATE,UPDATE_BY,UPDATE_DATE,VERSION)" +
				"	values("+levelhistory.getLevel_history_id()+",?,to_date('" + levelhistory.getExpire_date() + "','yyyy-mm-dd hh24:mi:ss'),?,?,sysdate  " +
						" ,?,?,?,?,?,?,?,?,sysdate,?,sysdate,?)";
		//String sql="UPDATE T_IM_SENDTASK SET Status=?,sendtime=to_date('" + sendtime + "','yyyy-mm-dd hh24:mi:ss'),retrytimes=? WHERE seqid=?";
		
		//System.out.println("sql = "+sql);

		try {
			pst=conn.prepareStatement(sql);
			conn.setAutoCommit(false);
			pst.setString(1,levelhistory.getMem_level());//变更后级别#MEM_LEVEL
			pst.setString(2,levelhistory.getOrg_mem_level());//变更前级别#ORG_MEM_LEVEL			
			pst.setString(3,levelhistory.getOrg_expire_date());//变更前级别有效期#ORG_EXPIRE_DATE
			pst.setString(4,levelhistory.getReson_type()); //变更原因类型#RESON_TYPE
			pst.setString(5, levelhistory.getREASON());//人工输入；升/降级计算:"member_sys"
			pst.setString(6, levelhistory.getChg_type());//别变更类型(UP:升级,DOWN:降级)
			pst.setLong(7, levelhistory.getMember_id());//会员ID
			pst.setLong(8, levelhistory.getLevel_point());//判定用户可升级或降级的有效积分数,人工调整时,不记录。用于镜像变更前后值
			pst.setLong(9, levelhistory.getTicket_count());//判定用户可升级或降级的有效积分数,人工调整时,不记录。用于镜像变更前后值
			pst.setInt(10, levelhistory.getIsdelete());
			pst.setString(11, levelhistory.getCreate_By());
			pst.setString(12,levelhistory.getUpdate_By());
			pst.setLong(13,levelhistory.getVersion());
			flag = pst.executeUpdate();
		 
			str = flag+" ";
			//System.out.println(str);
			 
			 
	
			 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		 
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
		
		return str;  
	}

}
