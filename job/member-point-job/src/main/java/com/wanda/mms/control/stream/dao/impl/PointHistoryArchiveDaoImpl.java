package com.wanda.mms.control.stream.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.log4j.Logger;

import com.wanda.mms.control.stream.dao.MemberPointDao;
import com.wanda.mms.control.stream.dao.PointHistoryArchiveDao;
import com.wanda.mms.control.stream.util.DateUtil;
import com.wanda.mms.control.stream.vo.MemberPoint;
import com.wanda.mms.control.stream.vo.PointHistoryArchive;

public class PointHistoryArchiveDaoImpl implements PointHistoryArchiveDao {
	static Logger logger = Logger.getLogger(PointHistoryArchiveDaoImpl.class.getName());

//	private PreparedStatement pst=null;//  数据库预先编译声明类型的属性 pst 用于操作数据库
//	private PreparedStatement pstone=null;//  数据库预先编译声明类型的属性 pst 用于操作数据库
	
//	private ResultSet rs=null;// 结果集类型的 属性 rs 用于接收返回的结果集

	
	@Override
	public int addPointHistoryArchive(Connection conn,
			PointHistoryArchive pointhistoryarchive) {
		// TODO Auto-generated method stub
		int flag=0;
		int flagup =0;
		String str ="";
		PreparedStatement pst=null;
		Date da = new Date();
		String date = DateUtil.getDateStrss(da);//时间可能是 时分秒
		String yy = (String) date.subSequence(0, 4);
		int intyy = Integer.valueOf(yy);
		intyy = intyy +1;
		String yyyy = String.valueOf(intyy);
		String te = "-12-31 23:59:59";
		String time =yyyy+te; 
		//S_T_POINT_HISTORY_ARCHIVE
	//	String sql = "insert into t_im_module_log (seqid,dataymd,moduleid,starttime,endtime,status,DESCRIPTION)values(S_T_F_VOUCHER.nextVal,?,?,to_date('" + lm.getStartTime() + "','yyyy-mm-dd hh24:mi:ss'),to_date('" + lm.getEndTime() + "','yyyy-mm-dd hh24:mi:ss'),?,?)"; 
		String sql = "insert into T_POINT_HISTORY_ARCHIVE(POINT_HISTORY_ARCHIVE_ID,MEMBER_ID,SET_TIME,LEVEL_POINT,ACTIVITY_POINT,EXCHANGE_POINT,EXCHANGE_POINT_EXPIRE_TIME,POINT_TYPE,POINT_SYS," +
				"	POINT_TRANS_TYPE,POINT_TRANS_CODE,POINT_TRANS_CODE_WEB,ADJ_RESION,ORG_POINT_BALANCE,POINT_BALANCE,IS_SYNC_BALANCE,ISDELETE,CREATE_BY,CREATE_DATE,UPDATE_BY,UPDATE_DATE,VERSION,ARCHIEVE_BY,ARCHIVE_TIME,TICKET_COUNT,MEMBER_POINT_ID,ADJ_REASON_TYPE,ADJ_REASON,ORDER_ID,PRODUCT_NAME,IS_SUCCEED,CINEMA_INNER_CODE,POINT_HISTORY_ID)" +
				"	values(S_T_POINT_HISTORY_ARCHIVE.nextval,?,sysdate,?,?,?,to_date('" +pointhistoryarchive.getExchange_Point_Expire_Time()+ "','yyyy-mm-dd hh24:mi:ss')  " +
						" ,?,?,?,?,?,?,?,?,?,?,?,to_date('" +pointhistoryarchive.getCreate_Date()+ "','yyyy-mm-dd hh24:mi:ss'),?,to_date('" +pointhistoryarchive.getUpdate_Date()+ "','yyyy-mm-dd hh24:mi:ss'),?,?,sysdate,?,?,?,?,?,?,?,?,?)";
		//String sql="UPDATE T_IM_SENDTASK SET Status=?,sendtime=to_date('" + sendtime + "','yyyy-mm-dd hh24:mi:ss'),retrytimes=? WHERE seqid=?";
		
		//System.out.println("sql = "+sql);
		 
		//System.out.println(pointhistoryarchive.getPointHistoryid());
		MemberPointDao mpdao = new  MemberPointDaoImpl();
		
//		SENDDBConnection db=SENDDBConnection.getInstance();	
//		conn=db.getConnection();
	//	MemberPoint memp =mpdao.fandMemberPointByID(conn,pointhistroy.getMemberid());
		try {
		 
			
			pst=conn.prepareStatement(sql);
			
			pst.setLong(1,pointhistoryarchive.getMemberid());//会员ID
			pst.setLong(2,pointhistoryarchive.getLevel_Point());//定级积分
			
			
			//to_date('" + lm.getStartTime() + "','yyyy-mm-dd hh24:mi:ss'))";
		//	pst.setInt(3,pointhistoryarchive.getTicket_Count());//升降级判定票数
			pst.setLong(3,pointhistoryarchive.getActivity_Point()); //非定级积分
			pst.setLong(4, pointhistoryarchive.getExchange_Point());//可兑换积分
			pst.setString(5, pointhistoryarchive.getPoint_Type());//积分操作类型(1:购买;2:礼品;3:奖励;4调整;5:会员终止;6:积分兑换;其他值:其他）
			pst.setString(6, pointhistoryarchive.getPoint_Sys());//源系统(1:POS;2:网站;3:会员系统;Others 其他)
			pst.setString(7, pointhistoryarchive.getPoint_Trans_Type());//.单据类型(1:交易;2:积分兑换;3:特殊积分活动;4积分清零)
			pst.setString(8, pointhistoryarchive.getPoint_Trans_Code());//单号(交易单号(POS/网站)/积分兑换交易单号/特殊积分规则ID)
			pst.setString(9, pointhistoryarchive.getPoint_Trans_Code_Web());//如果point_trans_type是交易,point_sys是网站，则在此记录网站订单号关联的POS订单
			pst.setString(10, pointhistoryarchive.getAdj_Resion()); //人工调整时，记录输入调整原因。
			
			//查询得到更新前的积分
		//	pointhistroy.setOrg_Point_Balance(memp.getExg_Point_Balance());
			pst.setLong(11, pointhistoryarchive.getOrg_Point_Balance()	);//更新t_member_point.exg_[oint_balance前的值
			//与可兑换积分相加得到更新后的积分
		//	pointhistroy.setPoint_Balance(memp.getExg_Point_Balance()+pointhistroy.getExchange_Point());
			pst.setLong(12, pointhistoryarchive.getPoint_Balance());//更新t_member_point.exg_[oint_balance后的值
			
			pst.setLong(13,pointhistoryarchive.getIs_Sync_Balance());//0:未计算;1已计算是否已经计算并更新此会员对应的积分账户。
			pst.setInt(14, pointhistoryarchive.getIsdelete());
			pst.setString(15, pointhistoryarchive.getCreate_By());
			pst.setString(16,pointhistoryarchive.getUpdate_By());
			pst.setLong(17,pointhistoryarchive.getVersion());
		//	pst.setLong(19, pointhistoryarchive.getMember_point_id());
		//	pst.setLong(20, pointhistoryarchive.getAdj_reason());
		//	pst.setString(21, pointhistoryarchive.getAdj_reason_type());
			pst.setString(18, pointhistoryarchive.getArchieve_By());//系统自动归档为"member_sys";其他情况归档员工ID
			pst.setInt(19, pointhistoryarchive.getTicket_Count()); 
			pst.setLong(20, pointhistoryarchive.getMember_point_id());
			pst.setString(21, pointhistoryarchive.getAdj_reason_type());//维表.积分调整原因类型
			pst.setString(22, pointhistoryarchive.getAdj_reason());//积分调整的原因
			pst.setString(23, pointhistoryarchive.getOrder_id());//接口兑换积分记录pos订单号，礼品订单号
			pst.setString(24, pointhistoryarchive.getProduct_name());//接口传入,网站兑换礼品时，记录产品命名
			pst.setString(25, pointhistoryarchive.getIs_succeed());//交易订单是否成功 1代表成功，0代表订单失效，同时成一条回退记录
			pst.setString(26, pointhistoryarchive.getCinema_inner_code());//积分兑换发生在哪个影城，积分规则计算是哪个影城的交易送的积分
			pst.setLong(27, pointhistoryarchive.getPointHistoryid());//会员id
			
			flag = pst.executeUpdate();
			 	
			 
			
		 
			 
			 
			
			conn.commit();
			 
		
	
			
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
		
		return flag;  
	}

}
