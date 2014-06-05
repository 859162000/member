package com.wanda.mms.control.stream.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.wanda.mms.control.stream.dao.MemberPointDao;
import com.wanda.mms.control.stream.dao.StreamDao;
import com.wanda.mms.control.stream.db.SENDDBConnection;
import com.wanda.mms.control.stream.util.DateUtil;
import com.wanda.mms.control.stream.vo.MemberPoint;
import com.wanda.mms.control.stream.vo.Stream;

public class StreamDaoImpl implements StreamDao {
	static Logger logger = Logger.getLogger(StreamDaoImpl.class.getName());

//	private Connection conn=null;// 数据数据库连接属性 conn，用于接收数据连接
//	private PreparedStatement pst=null;//  数据库预先编译声明类型的属性 pst 用于操作数据库
//	private PreparedStatement pstone=null;//  数据库预先编译声明类型的属性 pst 用于操作数据库
//	
//	private ResultSet rs=null;// 结果集类型的 属性 rs 用于接收返回的结果集
	

	@Override
	public String addListStreamDao(Connection conn,List<Stream> streamlist) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String addStreamDao(Connection conn,Stream stream) {
		// TODO Auto-generated method stub
		int flag=0;
		int flagup =0;
		String str ="";
		PreparedStatement pst=null;//  数据库预先编译声明类型的属性 pst 用于操作数据库
 	    PreparedStatement pstone=null;//  数据库预先编译声明类型的属性 pst 用于操作数据库
 
		
		
		//序列名不对以后改过来  select S_T_F_VOUCHER.nextVal from dual
		//得到之后在插入 并把这个值回传。
		Date da = new Date();
		String date = DateUtil.getDateStrss(da);//时间可能是 时分秒
	//	String sql = "insert into t_im_module_log (seqid,dataymd,moduleid,starttime,endtime,status,DESCRIPTION)values(S_T_F_VOUCHER.nextVal,?,?,to_date('" + lm.getStartTime() + "','yyyy-mm-dd hh24:mi:ss'),to_date('" + lm.getEndTime() + "','yyyy-mm-dd hh24:mi:ss'),?,?)"; 
		String sql = "insert into T_POINT_HISTORY(POINT_HISTORY_ID,MEMBER_ID,SET_TIME,LEVEL_POINT,TICKET_COUNT,ACTIVITY_POINT,EXCHANGE_POINT,EXCHANGE_POINT_EXPIRE_TIME,POINT_TYPE,POINT_SYS," +
				"	POINT_TRANS_TYPE,POINT_TRANS_CODE,POINT_TRANS_CODE_WEB,ADJ_RESION,ORG_POINT_BALANCE,POINT_BALANCE,IS_SYNC_BALANCE,ISDELETE,CREATE_BY,CREATE_DATE,UPDATE_BY,UPDATE_DATE,VERSION)" +
				"	values(S_T_F_VOUCHER.nextVal,?,sysdate,?,?,?,?,to_date('" + stream.getExchange_Point_Expire_Time() + "','yyyy-mm-dd hh24:mi:ss')  " +
						" ,?,?,?,?,?,?,?,?,?,?,?,sysdate,?,sysdate,?)";
		//String sql="UPDATE T_IM_SENDTASK SET Status=?,sendtime=to_date('" + sendtime + "','yyyy-mm-dd hh24:mi:ss'),retrytimes=? WHERE seqid=?";
		
		String upsql="update ";
		
		MemberPointDao mpdao = new  MemberPointDaoImpl();
		
		SENDDBConnection db=SENDDBConnection.getInstance();	
		conn=db.getConnection();
		MemberPoint memp =mpdao.fandMemberPointByID(conn,stream.getMemberid());
		try {
			conn.setAutoCommit(false);
			
			pst=conn.prepareStatement(sql);
			pstone=conn.prepareStatement(upsql);
			pst.setLong(1,stream.getMemberid());//会员ID
			pst.setLong(2,stream.getLevel_Point());//定级积分
			
			
			//to_date('" + lm.getStartTime() + "','yyyy-mm-dd hh24:mi:ss'))";
			pst.setInt(3,stream.getTicket_Count());//升降级判定票数
			pst.setLong(4,stream.getActivity_Point()); //非定级积分
			pst.setLong(5, stream.getExchange_Point());//可兑换积分
			pst.setString(6, stream.getPoint_Type());//积分操作类型(1:购买;2:礼品;3:奖励;4调整;5:会员终止;6:积分兑换;其他值:其他）
			pst.setString(7, stream.getPoint_Sys());//源系统(1:POS;2:网站;3:会员系统;Others 其他)
			pst.setString(8, stream.getPoint_Trans_Type());//.单据类型(1:交易;2:积分兑换;3:特殊积分活动;4积分清零)
			pst.setString(9, stream.getPoint_Trans_Code());//单号(交易单号(POS/网站)/积分兑换交易单号/特殊积分规则ID)
			pst.setString(10, stream.getPoint_Trans_Code_Web());//如果point_trans_type是交易,point_sys是网站，则在此记录网站订单号关联的POS订单
			pst.setString(11, stream.getAdj_Resion()); //人工调整时，记录输入调整原因。
			
			pst.setLong(12, stream.getOrg_Point_Balance()	);//更新t_member_point.exg_[oint_balance前的值
			pst.setLong(13, stream.getPoint_Balance());//更新t_member_point.exg_[oint_balance后的值
			pst.setString(14,"0");//0:未计算;1已计算是否已经计算并更新此会员对应的积分账户。
			pst.setInt(15, stream.getIsdelete());
			
			pst.setString(16, stream.getCreate_By());
			
			pst.setLong(17,stream.getVersion());
			flag = pst.executeUpdate();
			 
			pstone.setInt(1, 1);
			
			flagup = pstone.executeUpdate();
			
			conn.commit();
			if(pst!=null){
				pst.close();
			}
			if(pstone!=null){

				pstone.close();
				 	
			}
	
		//	System.out.println("添加日志信息  数据日期 YYYYMMDD"+lm.getDataYMD()+"模块ID . 1-数据采集 2-指标计算 3-发送任务生成 4-发送器"+lm.getDescription()+"开始时间"+lm.getStartTime()+"结束时间"+lm.getEndTime()+"执行结果 0-成功，其他-失败"+lm.getStatus()+"结果描述"+lm.getDescription());
			 
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
			if(pstone!=null){

				try {
					pstone.close();
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
