package com.wanda.mms.control.stream.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.wanda.mms.control.stream.dao.MemberPointDao;
import com.wanda.mms.control.stream.dao.PointHistroyDao;
import com.wanda.mms.control.stream.util.DateUtil;
import com.wanda.mms.control.stream.util.Page;
import com.wanda.mms.control.stream.vo.MemberPoint;
import com.wanda.mms.control.stream.vo.PointHistroy;

public class PointHistroyDaoImpl implements PointHistroyDao {
	static Logger logger = Logger.getLogger(PointHistroyDaoImpl.class.getName());

	//private Connection conn=null;// 数据数据库连接属性 conn，用于接收数据连接
//	private PreparedStatement pst=null;//  数据库预先编译声明类型的属性 pst 用于操作数据库 	
//	private ResultSet rs=null;// 结果集类型的 属性 rs 用于接收返回的结果集
	
	@Override
	public String addListPointHistroy(Connection conn,
 			List<PointHistroy> pointhistroylist) {
 	// TODO Auto-generated method stub
//		int flag=0;
//		int flagup =0;
			String str ="";
//		String sflag ="";
//		List seqlist = new ArrayList();
//		long seq = seqPointHistroy(conn);
//		
//		//序列名不对以后改过来  select S_T_F_VOUCHER.nextVal from dual
//		//得到之后在插入 并把这个值回传。
//		Date da = new Date();
//		String date = DateUtil.getDateStrss(da);//时间可能是 时分秒
//		String yy = (String) date.subSequence(0, 4);
//		int intyy = Integer.valueOf(yy);
//		intyy = intyy +1;
//		String yyyy = String.valueOf(intyy);
//		String te = "-12-31 23:59:59";
//		String time =yyyy+te; 
//		String dt = yy+te;
//	//	String sql = "insert into t_im_module_log (seqid,dataymd,moduleid,starttime,endtime,status,DESCRIPTION)values(S_T_F_VOUCHER.nextVal,?,?,to_date('" + lm.getStartTime() + "','yyyy-mm-dd hh24:mi:ss'),to_date('" + lm.getEndTime() + "','yyyy-mm-dd hh24:mi:ss'),?,?)"; 
//		String sql = "insert into T_POINT_HISTORY(POINT_HISTORY_ID,MEMBER_ID,SET_TIME,LEVEL_POINT,TICKET_COUNT,ACTIVITY_POINT,EXCHANGE_POINT,EXCHANGE_POINT_EXPIRE_TIME,POINT_TYPE,POINT_SYS," +
//				"	POINT_TRANS_TYPE,POINT_TRANS_CODE,POINT_TRANS_CODE_POS,ADJ_RESION,ORG_POINT_BALANCE,POINT_BALANCE,IS_SYNC_BALANCE,ISDELETE,CREATE_BY,CREATE_DATE,UPDATE_BY,UPDATE_DATE,VERSION,MEMBER_POINT_ID,ADJ_REASON,ADJ_REASON_TYPE)" +
//				"	values("+seq+",?,to_date('" + pointhistroy.getSetTime() + "','yyyy-mm-dd hh24:mi:ss'),?,?,?,?,to_date('" + pointhistroy.getExchange_Point_Expire_Time() + "','yyyy-mm-dd hh24:mi:ss')  " +
//						" ,?,?,?,?,?,?,?,?,?,?,?,to_date('" + date + "','yyyy-mm-dd hh24:mi:ss'),?,to_date('" + date + "','yyyy-mm-dd hh24:mi:ss'),?,?,?,?)";
//		//String sql="UPDATE T_IM_SENDTASK SET Status=?,sendtime=to_date('" + sendtime + "','yyyy-mm-dd hh24:mi:ss'),retrytimes=? WHERE seqid=?";
//		
//		System.out.println("sql = "+sql);
//		 
//		
//		MemberPointDao mpdao = new  MemberPointDaoImpl();
//		
////		SENDDBConnection db=SENDDBConnection.getInstance();	
////		conn=db.getConnection();
//		//MemberPoint memp =mpdao.fandMemberPointByID(conn,pointhistroy.getMemberid());
//		try {
//			conn.setAutoCommit(false);
//			
//			pst=conn.prepareStatement(sql);
//			
//			pst.setLong(1,pointhistroy.getMemberid());//会员ID
//			pst.setLong(2,pointhistroy.getLevel_Point());//定级积分
//			
//			
//			//to_date('" + lm.getStartTime() + "','yyyy-mm-dd hh24:mi:ss'))";
//			pst.setInt(3,pointhistroy.getTicket_Count());//升降级判定票数
//			pst.setLong(4,pointhistroy.getActivity_Point()); //非定级积分
//			pst.setLong(5, pointhistroy.getExchange_Point());//可兑换积分
//			pst.setString(6, pointhistroy.getPoint_Type());//积分操作类型(1:购买;2:礼品;3:奖励;4调整;5:会员终止;6:积分兑换;其他值:其他）
//			pst.setString(7, pointhistroy.getPoint_Sys());//源系统(1:POS;2:网站;3:会员系统;Others 其他)
//			pst.setString(8, pointhistroy.getPoint_Trans_Type());//.单据类型(1:交易;2:积分兑换;3:特殊积分活动;4积分清零)
//			pst.setString(9, pointhistroy.getPoint_Trans_Code());//单号(交易单号(POS/网站)/积分兑换交易单号/特殊积分规则ID)
//			pst.setString(10, pointhistroy.getPoint_Trans_Code_Pos());//如果point_trans_type是交易,point_sys是网站，则在此记录网站订单号关联的POS订单
//			pst.setString(11, pointhistroy.getAdj_Resion()); //人工调整时，记录输入调整原因。
//			
//			//更新前的积分
//			pst.setLong(12, pointhistroy.getOrg_Point_Balance()	);//更新t_member_point.exg_[oint_balance前的值
//			//与可兑换积分相加得到更新后的积分
//			//pointhistroy.setPoint_Balance(pointhistroy.getOrg_Point_Balance()+pointhistroy.getExchange_Point());
//			pst.setLong(13, pointhistroy.getPoint_Balance());//更新t_member_point.exg_[oint_balance后的值
//			pst.setString(14,"0");//0:未计算;1已计算是否已经计算并更新此会员对应的积分账户。
//			pst.setInt(15, pointhistroy.getIsdelete());//
//			pst.setString(16, pointhistroy.getCreate_By());
//			pst.setString(17,pointhistroy.getUpdate_By());
//			pst.setLong(18,pointhistroy.getVersion());
//			pst.setLong(19, pointhistroy.getMember_point_id());
//			pst.setString(20, pointhistroy.getAdj_reason());
//			pst.setString(21, pointhistroy.getAdj_reason_type());
//			
//			
//			flag = pst.executeUpdate();
//			MemberPoint memp = new MemberPoint();
//			memp.setPoint_History_Id(seq);
//			memp.setExg_Point_Balance(pointhistroy.getPoint_Balance());
//			//long ljdj= memp.getLevel_Point_Total(); //总累计定积分
//		//	long ljfdj =memp.getActivity_Point();  //  总累计非定级积分
//			long kdhjf = pointhistroy.getExchange_Point(); //可兑换积分余额
//			
//		//	long gqjf = memp.getExg_Expire_Point_Balance()+pointhistroy.getExchange_Point();//当年即将过期可兑换积分
//			memp.setLevel_Point_Total(pointhistroy.getLevel_Point());
//			memp.setActivity_Point(pointhistroy.getActivity_Point());
//			memp.setExg_Point_Balance(pointhistroy.getExchange_Point()); 
//			
//			if(pointhistroy.getPoint_Type().equals("6")){//维表.积分操作类型(1:购买;2:礼品;3:奖励;4调整;5:会员终止;6:积分兑换;其他值:其他）
//				memp.setExg_Expire_Point_Balance(pointhistroy.getExg_expire_point_balance());
//				memp.setLast_Exchange_Date(date);
//			}else if(pointhistroy.getPoint_Trans_Type().equals("4")){
//				memp.setExg_Expire_Point_Balance(pointhistroy.getExg_expire_point_balance());//清零还有点不太明白
//			}else if(pointhistroy.getPoint_Type().equals("4")){
//				if(dt.equals(pointhistroy.getExchange_Point_Expire_Time())){
//					memp.setExg_Expire_Point_Balance(pointhistroy.getExg_expire_point_balance());
//				}
//			}else{
//				 
//			}
//			
//				memp.setMember_point_id(pointhistroy.getMemberid());
//				memp.setMemberId(pointhistroy.getMemberid());
//				flagup = mpdao.updateMemberPointByID(conn, memp);//更新会员积分账户
//			 
//				pointhistroy.setPointHistoryid(seq);
//				pointhistroy.setIs_Sync_Balance(1);
//				
//			    sflag =	updatePointHistroy(conn,pointhistroy);
//				
//			
//			conn.commit();
//			str = flagup+sflag+flag;
//			System.out.println(str);
//			
// 
//			 
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			try {
//				conn.rollback();
//			} catch (SQLException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			e.printStackTrace();
//		}
//		
		return str;  
	}

	private String pointHistroy(Connection conn, PointHistroy pointhistroy, boolean autoCommit) {
		// TODO Auto-generated method stub
		int flag=0;
		int flagup =0;
		String str ="";
		String sflag ="";
		long seq = seqPointHistroy(conn);
		
		//序列名不对以后改过来  select S_T_F_VOUCHER.nextVal from dual
		//得到之后在插入 并把这个值回传。
		PreparedStatement pst=null;// 
 
		String date = pointhistroy.getSetTime();//时间可能是 时分秒
		String yy = (String) date.subSequence(0, 4);
		int intyy = Integer.valueOf(yy);
		intyy = intyy +1;
		String yyyy = String.valueOf(intyy);
		String te = "-12-31 23:59:59";
		String time =yyyy+te; 
		String dt = yy+te;
		String sql ="";
		if(pointhistroy.getPoint_Type().equals("4")){
			 sql = "insert into T_POINT_HISTORY(POINT_HISTORY_ID,MEMBER_ID,SET_TIME,LEVEL_POINT,TICKET_COUNT,ACTIVITY_POINT,EXCHANGE_POINT,EXCHANGE_POINT_EXPIRE_TIME,POINT_TYPE,POINT_SYS," +
				"	POINT_TRANS_TYPE,POINT_TRANS_CODE,POINT_TRANS_CODE_WEB,ADJ_RESION,ORG_POINT_BALANCE,POINT_BALANCE,IS_SYNC_BALANCE,ISDELETE,CREATE_BY,CREATE_DATE,UPDATE_BY,UPDATE_DATE,VERSION,MEMBER_POINT_ID,ADJ_REASON,ADJ_REASON_TYPE,ORDER_ID,PRODUCT_NAME,IS_SUCCEED,CINEMA_INNER_CODE)" +
				"	values("+seq+",?,to_date('" + pointhistroy.getSetTime() + "','yyyy-mm-dd hh24:mi:ss'),?,?,?,?,to_date('" + time + "','yyyy-mm-dd hh24:mi:ss')  " +
						" ,?,?,?,?,?,?,?,?,?,?,?,sysdate,?,sysdate,?,?,?,?,?,?,?,?)";

		}else{
			 sql = "insert into T_POINT_HISTORY(POINT_HISTORY_ID,MEMBER_ID,SET_TIME,LEVEL_POINT,TICKET_COUNT,ACTIVITY_POINT,EXCHANGE_POINT,EXCHANGE_POINT_EXPIRE_TIME,POINT_TYPE,POINT_SYS," +
			"	POINT_TRANS_TYPE,POINT_TRANS_CODE,POINT_TRANS_CODE_WEB,ADJ_RESION,ORG_POINT_BALANCE,POINT_BALANCE,IS_SYNC_BALANCE,ISDELETE,CREATE_BY,CREATE_DATE,UPDATE_BY,UPDATE_DATE,VERSION,MEMBER_POINT_ID,ADJ_REASON,ADJ_REASON_TYPE,ORDER_ID,PRODUCT_NAME,IS_SUCCEED,CINEMA_INNER_CODE)" +
			"	values("+seq+",?,sysdate,?,?,?,?,to_date('" + pointhistroy.getExchange_Point_Expire_Time() + "','yyyy-mm-dd hh24:mi:ss')  " +
					" ,?,?,?,?,?,?,?,?,?,?,?,sysdate,?,sysdate,?,?,?,?,?,?,?,?)";

		}
		// EXCHANGE_POINT_EXPIRE_TIME
		//System.out.println("sql = "+sql);
		 
		MemberPointDao mpdao = new  MemberPointDaoImpl();
		
//				SENDDBConnection db=SENDDBConnection.getInstance();	
//				conn=db.getConnection();
		//MemberPoint memp =mpdao.fandMemberPointByID(conn,pointhistroy.getMemberid());
		try {
			if(autoCommit) {
				conn.setAutoCommit(false);
			}
			
			pst=conn.prepareStatement(sql);
			
			pst.setLong(1,pointhistroy.getMemberid());//会员ID
			pst.setLong(2,pointhistroy.getLevel_Point());//定级积分
			
			
			//to_date('" + lm.getStartTime() + "','yyyy-mm-dd hh24:mi:ss'))";
			pst.setInt(3,pointhistroy.getTicket_Count());//升降级判定票数
			pst.setLong(4,pointhistroy.getActivity_Point()); //非定级积分
			pst.setLong(5, pointhistroy.getExchange_Point());//可兑换积分
			pst.setString(6, pointhistroy.getPoint_Type());//积分操作类型(1:购买;2:礼品;3:奖励;4调整;5:会员终止;6:积分兑换;其他值:其他）
			pst.setString(7, pointhistroy.getPoint_Sys());//源系统(1:POS;2:网站;3:会员系统;Others 其他)
			pst.setString(8, pointhistroy.getPoint_Trans_Type());//.单据类型(1:交易;2:积分兑换;3:特殊积分活动;4积分清零)
			pst.setString(9, pointhistroy.getPoint_Trans_Code());//单号(交易单号(POS/网站)/积分兑换交易单号/特殊积分规则ID)
			pst.setString(10, pointhistroy.getPoint_Trans_Code_Web());//如果point_trans_type是交易,point_sys是网站，则在此记录网站订单号关联的POS订单
			pst.setString(11, pointhistroy.getAdj_Resion()); //人工调整时，记录输入调整原因。
			
			//更新前的积分
			pst.setLong(12, pointhistroy.getOrg_Point_Balance()	);//更新t_member_point.exg_[oint_balance前的值
			//与可兑换积分相加得到更新后的积分
			//pointhistroy.setPoint_Balance(pointhistroy.getOrg_Point_Balance()+pointhistroy.getExchange_Point());
			pst.setLong(13, pointhistroy.getPoint_Balance());//更新t_member_point.exg_[oint_balance后的值
			pst.setString(14,"0");//0:未计算;1已计算是否已经计算并更新此会员对应的积分账户。
			pst.setInt(15, pointhistroy.getIsdelete());//逻辑删除标识,默认:0 未删除;1删除;其他:非法
			pst.setString(16, pointhistroy.getCreate_By());//创建人员工ID(系统自动:"member_sys";管理界面:登录员工ID)
			pst.setString(17,pointhistroy.getUpdate_By());//更新人员工ID(系统自动:"member_sys";管理界面:登录员工ID)
			pst.setLong(18,pointhistroy.getVersion());//版本号#VERSION
			pst.setLong(19, pointhistroy.getMember_point_id());
			pst.setString(20, pointhistroy.getAdj_reason());//积分调整的原因
			pst.setString(21, pointhistroy.getAdj_reason_type());//积分调整原因类型
			pst.setString(22,pointhistroy.getOrder_id() );//接口兑换积分记录pos订单号，礼品订单号
			pst.setString(23,pointhistroy.getProduct_name() );//接口传入,网站兑换礼品时，记录产品命名
			pst.setString(24,pointhistroy.getIs_succeed());//交易订单是否成功 1代表成功，0代表订单失效，同时成一条回退记录
			pst.setString(25,pointhistroy.getCinema_inner_code() );//积分兑换发生在哪个影城，积分规则计算是哪个影城的交易送的积分
			flag = pst.executeUpdate();
			MemberPoint memp = new MemberPoint();
			memp.setPoint_History_Id(seq);
			//memp.setExg_Point_Balance(pointhistroy.getPoint_Balance());
			//long ljdj= memp.getLevel_Point_Total(); //总累计定积分
		//	long ljfdj =memp.getActivity_Point();  //  总累计非定级积分
			long kdhjf = pointhistroy.getExchange_Point(); //可兑换积分余额
			
		//	long gqjf = memp.getExg_Expire_Point_Balance()+pointhistroy.getExchange_Point();//当年即将过期可兑换积分
			memp.setLevel_Point_Total(pointhistroy.getLevel_Point());
			memp.setActivity_Point(pointhistroy.getActivity_Point());
			memp.setExg_Point_Balance(pointhistroy.getExchange_Point()); 
			
			if(pointhistroy.getPoint_Type().equals("6")){//维表.积分操作类型(1:购买;2:礼品;3:奖励;4调整;5:会员终止;6:积分兑换;其他值:其他）
				memp.setExg_Expire_Point_Balance(pointhistroy.getExchange_Point());
				memp.setLast_Exchange_Date(date);
			}else if(pointhistroy.getPoint_Trans_Type().equals("4")){//维表.单据类型(1:交易;2:积分兑换;3:特殊积分活动;4积分清零)
				memp.setExg_Expire_Point_Balance(pointhistroy.getExg_expire_point_balance());//清零还有点不太明白
			}else if(pointhistroy.getPoint_Type().equals("4")){//维表.积分操作类型(1:购买;2:礼品;3:奖励;4调整;5:会员终止;6:积分兑换;其他值:其他）
				if(dt.equals(pointhistroy.getExchange_Point_Expire_Time())){
					memp.setExg_Expire_Point_Balance(pointhistroy.getExchange_Point());
				}
			}else{
				 
			}
			
				memp.setMember_point_id(pointhistroy.getMemberid());
				memp.setMemberId(pointhistroy.getMemberid());
				flagup = mpdao.updateMemberPointByID(conn, memp);//更新会员积分账户
			 
				pointhistroy.setPointHistoryid(seq);
				pointhistroy.setIs_Sync_Balance(1);
				
			    sflag =	updatePointHistroy(conn,pointhistroy);
				
		    if(autoCommit) {
		    	conn.commit();
		    }
			
			str = flagup+sflag+flag;
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
	
	@Override
	public String addPointHistroyNotAutoCommit(Connection conn, PointHistroy pointhistroy) {
		return pointHistroy(conn, pointhistroy, false);
	}
	
	@Override
	public String addPointHistroy(Connection conn, PointHistroy pointhistroy) {
		return pointHistroy(conn, pointhistroy, true); 
	}

	@Override
	public String updatePointHistroy(Connection conn, PointHistroy pointhistroy) {
		// TODO Auto-generated method stub
		PreparedStatement pst=null;// 
		ResultSet rs=null;

		int flag=0;
		 Date da = new Date();
		 String date = DateUtil.getDateStrss(da);
		String sql="UPDATE T_POINT_HISTORY SET IS_SYNC_BALANCE=?,UPDATE_BY=?,UPDATE_DATE=sysdate,VERSION=? WHERE POINT_HISTORY_ID=?";
		//	SENDDBConnection db=SENDDBConnection.getInstance();	
		try {
			//conn=db.getConnection();
			pst=conn.prepareStatement(sql);
			 
				//System.out.println("pointhistroy.getIsdelete()="+pointhistroy.getIsdelete());
				pst.setLong(1,pointhistroy.getIs_Sync_Balance());
				pst.setString(2,pointhistroy.getUpdate_By());
				pst.setInt(3, pointhistroy.getVersion());
				pst.setLong(4, pointhistroy.getPointHistoryid());
				
				flag=pst.executeUpdate();
				 
			 
			 
		
		} catch (SQLException e) {
		 
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
		 
		 
		
		String fl = String.valueOf(flag) ;
		return fl;
		
		
		 
	}

	@Override
	public Long seqPointHistroy(Connection conn) {
		// TODO Auto-generated method stub
		
		PreparedStatement pst=null;// 
		ResultSet rs=null;

		Long seq = null  ;
		String sql = "select S_T_POINT_HISTORY.nextVal seq from dual";
		//SENDDBConnection db=SENDDBConnection.getInstance();	
		//conn=db.getConnection();
		
		try {
			pst=conn.prepareStatement(sql);
			rs=pst.executeQuery();
			while (rs.next()) {
				 seq=(rs.getLong("seq"));
			}
			 
	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
		 
				logger.error(e);
			 

			e.printStackTrace();
		}	finally {
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
	public List<PointHistroy> fandPointHistroyByLevel(Connection conn,String date,
			long memberId) {
		// TODO Auto-generated method stub
		//会员级别当前有效期前12月（1月1日00:00:00  到 升级计算时间）
		PreparedStatement pst=null;// 
		ResultSet rs=null;

		String yy = (String) date.subSequence(0, 4);
		int intyy = Integer.valueOf(yy);
		String yyyy = String.valueOf(intyy);
		String te = "0101";
		String teda="1231";
		String time =yyyy+te; 
		String dada =yyyy+teda;
		List<PointHistroy> phlist = new ArrayList<PointHistroy>();//用于接受会员积分统计历史
		String sql ="select  MEMBER_ID,sum(LEVEL_POINT) LEVEL_POINT, sum(TICKET_COUNT) TICKET_COUNT "
		+"  from T_POINT_HISTORY t    where set_time >= to_date(?, 'yyyy-mm-dd')  and   set_time  < to_date(?, 'yyyy-mm-dd') + 1  and MEMBER_ID = ? and IS_HISTORY<>'1' group by MEMBER_ID	"  ;
		    
		    try {
			pst=conn.prepareStatement(sql);
			pst.setString(1,time);
			pst.setString(2,dada);
			pst.setLong(3,memberId);
			rs=pst.executeQuery();
			logger.info(" select * T_POINT_HISTORY between "+ time+" and "+dada+" and MEMBER_ID= "+memberId);
//			System.out.println(sql+"  "+time+" "+dada+"  "+memberId);
			while (rs.next()) {
				PointHistroy ph= new PointHistroy();
//				mp.setExg_Expire_Point_Balance(rs.getLong("IntegralReset"));
//				mp.setMemberId(rs.getLong("MEMBER_ID"));
		//		ph.setPointHistoryid(rs.getLong("POINT_HISTORY_ID"));
				ph.setMemberid(rs.getLong("MEMBER_ID"));
			//	ph.setSetTime( DateUtil.getDateStrss(rs.getDate("SET_TIME")));
				ph.setLevel_Point(rs.getLong("LEVEL_POINT"));
				ph.setTicket_Count(rs.getInt("TICKET_COUNT"));
//				ph.setActivity_Point(rs.getLong("ACTIVITY_POINT"));
//				ph.setExchange_Point(rs.getLong("EXCHANGE_POINT"));
//				ph.setExchange_Point_Expire_Time(DateUtil.getDateStrss(rs.getDate("EXCHANGE_POINT_EXPIRE_TIME")));
//				ph.setPoint_Sys(rs.getString("POINT_SYS"));
//				ph.setPoint_Trans_Type(rs.getString("POINT_TRANS_TYPE"));
//				ph.setPoint_Trans_Code(rs.getString("POINT_TRANS_CODE"));
//				ph.setPoint_Trans_Code_Web(rs.getString("POINT_TRANS_CODE_WEB"));
//				ph.setAdj_reason_type(rs.getString("ADJ_REASON_TYPE"));
//				ph.setAdj_reason(rs.getString("ADJ_REASON"));
//				ph.setOrg_Point_Balance(rs.getLong("ORG_POINT_BALANCE"));
//				ph.setPoint_Balance(rs.getLong("POINT_BALANCE"));
//				ph.setIs_Sync_Balance(rs.getLong("IS_SYNC_BALANCE"));
//				ph.setIsdelete(rs.getInt("ISDELETE"));
//				ph.setCreate_By(rs.getString("CREATE_BY"));
//				ph.setCreate_Date(DateUtil.getDateStrss(rs.getDate("CREATE_DATE")));
//				ph.setUpdate_By(rs.getString("UPDATE_BY"));
//				ph.setUpdate_Date(DateUtil.getDateStrss(rs.getDate("UPDATE_DATE")));
//				ph.setVersion(rs.getInt("VERSION"));
				phlist.add(ph);
			}
			 
	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error(e);
			e.printStackTrace();
		} 	finally {
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
		return phlist;
	}

	@Override
	public long fandPointHistroyResetTotalNum(Connection conn,String date) {
		// TODO Auto-generated method stub
		
		Long seq = null  ;
		PreparedStatement pst=null;// 
		ResultSet rs=null;

		
		String sql = "SELECT COUNT( *) totalnum　 from (select     MEMBER_ID  from T_POINT_HISTORY  where 1=1 and to_char(EXCHANGE_POINT_EXPIRE_TIME,'yyyy') = ?  group by MEMBER_ID) A";
		//SENDDBConnection db=SENDDBConnection.getInstance();	
		//conn=db.getConnection();  
		
		try {
			pst=conn.prepareStatement(sql);
			String yyyy = date.substring(0, 4);

			pst.setString(1,yyyy);
			rs=pst.executeQuery();
			while (rs.next()) {
				 seq=(rs.getLong("totalnum"));
			}
			 
	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error(e);
			e.printStackTrace();
		} 	finally {
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
	public List<MemberPoint> fandPointHistroyResetPage(Connection conn, String date,
			Page page) {
		// TODO Auto-generated method stub
		PreparedStatement pst=null;// 
		ResultSet rs=null;

		 List<MemberPoint> list = new ArrayList<MemberPoint>();
			String yy = (String) date.subSequence(0, 4);
			int intyy = Integer.valueOf(yy);
			intyy = intyy +1;
			String yyyy = String.valueOf(intyy);
			String te = "0101";
			String time =yyyy+te; 
			String mmdd = "1231";
			String ymd = yyyy+mmdd;

			
		 Long seq = null  ;
			String sql = "SELECT * FROM (  "
					+ " SELECT A.*, ROWNUM RN  "
					+ " FROM (select sum(activity_Point) activity_Point, sum(level_Point) level_Point_Total,sum(EXCHANGE_POINT) IntegralReset , MEMBER_ID  from T_POINT_HISTORY  where "
					+"	to_char(EXCHANGE_POINT_EXPIRE_TIME,'yyyymmdd') between "+time+"and "+ymd+"  group by "
					+ " MEMBER_ID　) A  "
					+ " WHERE ROWNUM <= ?) "
					+ " WHERE RN >= ? "; 
			//SENDDBConnection db=SENDDBConnection.getInstance();	
			//conn=db.getConnection();
		//	System.out.println("yyyymmdd = "+date+" ROWNUM ="+page.getPage() +"RN "+page.getPageSize()+"积分清零= "+sql);
			try {
				pst=conn.prepareStatement(sql);
				//pst.setString(1,date);
				pst.setLong(1,page.getPageSize());
				pst.setLong(2,page.getPage());
				rs=pst.executeQuery();
				while (rs.next()) {
					MemberPoint mp = new MemberPoint();
					mp.setActivity_Point(rs.getLong("activity_Point"));
					mp.setLevel_Point_Total(rs.getLong("level_Point_Total"));
					mp.setExg_Expire_Point_Balance(rs.getLong("IntegralReset"));
					mp.setMemberId(rs.getLong("MEMBER_ID"));
					
					list.add(mp);
				}
				 
		
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.error(e);
				e.printStackTrace();
			} 	finally {
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
			 
		
		return list;
		
	}

	@Override
	public List<PointHistroy> fandPointHistroyResetArchive(Connection conn,
			String date, long memberId) {
		// TODO Auto-generated method stub
		 List<PointHistroy> list = new ArrayList<PointHistroy>();
		 PreparedStatement pst=null;// 
		 ResultSet rs=null;

		 Long seq = null  ;
			String sql = " "
					+ " select POINT_HISTORY_ID,MEMBER_ID,SET_TIME,LEVEL_POINT,TICKET_COUNT,ACTIVITY_POINT," +
							"EXCHANGE_POINT,EXCHANGE_POINT_EXPIRE_TIME,POINT_TYPE,POINT_SYS,POINT_TRANS_TYPE," +
							"POINT_TRANS_CODE,POINT_TRANS_CODE_POS,ADJ_REASON_TYPE,ADJ_REASON,ORG_POINT_BALANCE," +
							"POINT_BALANCE,IS_SYNC_BALANCE,ISDELETE,CREATE_BY,CREATE_DATE,UPDATE_BY,UPDATE_DATE,VERSION,  " +
							"MEMBER_POINT_ID,ADJ_REASON_TYPE,ADJ_REASON,ORDER_ID,PRODUCT_NAME,IS_SUCCEED,CINEMA_INNER_CODE " +
							"from T_POINT_HISTORY  where "
					+"	1=1 and to_char(EXCHANGE_POINT_EXPIRE_TIME,'yyyymmdd') = ? and MEMBER_ID=?  " ;
					 
			 
				 
			//SENDDBConnection db=SENDDBConnection.getInstance();	
			//conn=db.getConnection();
		 	try {
				pst=conn.prepareStatement(sql);
				pst.setString(1,date);
				pst.setLong(2,memberId);
			
				rs=pst.executeQuery();
				while (rs.next()) {
					PointHistroy ph= new PointHistroy();
//					mp.setExg_Expire_Point_Balance(rs.getLong("IntegralReset"));
//					mp.setMemberId(rs.getLong("MEMBER_ID"));
					ph.setPointHistoryid(rs.getLong("POINT_HISTORY_ID"));
					ph.setMemberid(rs.getLong("MEMBER_ID"));
					ph.setSetTime( DateUtil.getDateStrss(rs.getDate("SET_TIME")));
					ph.setLevel_Point(rs.getLong("LEVEL_POINT"));
					ph.setTicket_Count(rs.getInt("TICKET_COUNT"));
					ph.setActivity_Point(rs.getLong("ACTIVITY_POINT"));
					ph.setExchange_Point(rs.getLong("EXCHANGE_POINT"));
					ph.setExchange_Point_Expire_Time(DateUtil.getDateStrss(rs.getDate("EXCHANGE_POINT_EXPIRE_TIME")));
					ph.setPoint_Sys(rs.getString("POINT_SYS"));
					ph.setPoint_Trans_Type(rs.getString("POINT_TRANS_TYPE"));
					ph.setPoint_Trans_Code(rs.getString("POINT_TRANS_CODE"));
					ph.setPoint_Trans_Code_Web(rs.getString("POINT_TRANS_CODE_WEB"));
					ph.setAdj_reason_type(rs.getString("ADJ_REASON_TYPE"));
					ph.setAdj_reason(rs.getString("ADJ_REASON"));
					ph.setOrg_Point_Balance(rs.getLong("ORG_POINT_BALANCE"));
					ph.setPoint_Balance(rs.getLong("POINT_BALANCE"));
					ph.setIs_Sync_Balance(rs.getLong("IS_SYNC_BALANCE"));
					ph.setIsdelete(rs.getInt("ISDELETE"));
					ph.setCreate_By(rs.getString("CREATE_BY"));
					ph.setCreate_Date(DateUtil.getDateStrss(rs.getDate("CREATE_DATE")));
					ph.setUpdate_By(rs.getString("UPDATE_BY"));
					ph.setUpdate_Date(DateUtil.getDateStrss(rs.getDate("UPDATE_DATE")));
					ph.setVersion(rs.getInt("VERSION"));
					ph.setMember_point_id(rs.getLong("member_point_id"));
					ph.setAdj_reason_type(rs.getString("ADJ_REASON_TYPE"));
					ph.setAdj_reason(rs.getString("ADJ_REASON"));
					ph.setOrder_id(rs.getString("ORDER_ID"));
					ph.setProduct_name(rs.getString("PRODUCT_NAME"));
					ph.setIs_succeed(rs.getString("IS_SUCCEED"));
					ph.setCinema_inner_code(rs.getString("CINEMA_INNER_CODE"));
					list.add(ph);
				}
				 
		 
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.error(e);
				e.printStackTrace();
			}	finally {
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
			 
		
		return list;
		
	}

	@Override
	public int delPointHistroyReset(Connection conn, long pointHistoryId) {
		// TODO Auto-generated method stub
		PreparedStatement pst=null;// 
	 

		int flag=0;
		String sql="DELETE  FROM T_POINT_HISTORY  WHERE POINT_HISTORY_ID=?";
		//	SENDDBConnection db=SENDDBConnection.getInstance();	
		try {
			//conn=db.getConnection();
			pst=conn.prepareStatement(sql);
			 
			//	System.out.println("pointHistoryId ="+pointHistoryId);

				pst.setLong(1, pointHistoryId);
				
				flag=pst.executeUpdate();
				 
			 
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

 

	@Override
	public List<PointHistroy> fandPointHistroyPoint_Trans_Type(Connection conn,
			String pointTransType, Page page) {
		// TODO Auto-generated method stub
		 List<PointHistroy> list = new ArrayList<PointHistroy>();
			//String yy = (String) date.subSequence(0, 4);
		//	int intyy = Integer.valueOf(yy);
		//	intyy = intyy +1;
		//	String yyyy = String.valueOf(intyy);
		//	String te = "0101";
		//	String time =yyyy+te; 
		//	String mmdd = "1231";
		//	String ymd = yyyy+mmdd;

		PreparedStatement pst=null;// 
		ResultSet rs=null;
	
		 Long seq = null  ;
			String sql = "SELECT * FROM (  "
					+ " SELECT A.*, ROWNUM RN  "
					+ " FROM (select sum(EXCHANGE_POINT) IntegralReset , MEMBER_ID  from T_POINT_HISTORY  where "
					+"	to_char(EXCHANGE_POINT_EXPIRE_TIME,'yyyymmdd')   group by "
					+ " MEMBER_ID　) A  "
					+ " WHERE ROWNUM <= ?) "
					+ " WHERE RN >= ? "; 
			//SENDDBConnection db=SENDDBConnection.getInstance();	
			//conn=db.getConnection();
		//	System.out.println("yyyymmdd =   ROWNUM ="+page.getPage() +"RN "+page.getPageSize()+"积分==清零= "+sql);
			try {
				pst=conn.prepareStatement(sql);
				//pst.setString(1,date);
				pst.setLong(1,page.getPageSize());
				pst.setLong(2,page.getPage());
				rs=pst.executeQuery();
				while (rs.next()) {
					PointHistroy mp = new PointHistroy();
					//mp.setExg_Expire_Point_Balance(rs.getLong("IntegralReset"));
					//mp.setMemberId(rs.getLong("MEMBER_ID"));
				//--------------	
					list.add(mp);
				}
 
		
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.error(e);
				e.printStackTrace();
			} 	finally {
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
			 
		
		return list;
		
	}

	@Override
	public List<PointHistroy> fandPointHistroyByPoint_Trans_Type(
			Connection conn, String Point_Trans_Type, long memberId) {
		// TODO Auto-generated method stub
		//会员级别当前有效期前12月（1月1日00:00:00  到 升级计算时间）
		PreparedStatement pst=null;// 
		ResultSet rs=null;

		//String yy = (String) date.subSequence(0, 4);
		//int intyy = Integer.valueOf(yy);
		//String yyyy = String.valueOf(intyy);
		String te = "0101";
		//String time =yyyy+te; 
		List<PointHistroy> phlist = new ArrayList<PointHistroy>();//用于接受会员积分统计历史
		String sql = "select  * from T_POINT_HISTORY where Point_Trans_Type=? and MEMBER_ID=? ";
		try {
			pst=conn.prepareStatement(sql);
			//pst.setString(1,time);
			pst.setString(1,Point_Trans_Type);
			pst.setLong(2,memberId);
			rs=pst.executeQuery();
		//	System.out.println(sql+"  "+Point_Trans_Type+" "+memberId+"   "+memberId);
			while (rs.next()) {
				PointHistroy ph= new PointHistroy();
//				mp.setExg_Expire_Point_Balance(rs.getLong("IntegralReset"));
//				mp.setMemberId(rs.getLong("MEMBER_ID"));
				ph.setPointHistoryid(rs.getLong("POINT_HISTORY_ID"));
				ph.setMemberid(rs.getLong("MEMBER_ID"));
				ph.setSetTime( DateUtil.getDateStrss(rs.getDate("SET_TIME")));
				ph.setLevel_Point(rs.getLong("LEVEL_POINT"));
				ph.setTicket_Count(rs.getInt("TICKET_COUNT"));
				ph.setActivity_Point(rs.getLong("ACTIVITY_POINT"));
				ph.setExchange_Point(rs.getLong("EXCHANGE_POINT"));
				ph.setExchange_Point_Expire_Time(DateUtil.getDateStrss(rs.getDate("EXCHANGE_POINT_EXPIRE_TIME")));
				ph.setPoint_Sys(rs.getString("POINT_SYS"));
				ph.setPoint_Trans_Type(rs.getString("POINT_TRANS_TYPE"));
				ph.setPoint_Trans_Code(rs.getString("POINT_TRANS_CODE"));
				ph.setPoint_Trans_Code_Web(rs.getString("POINT_TRANS_CODE_WEB"));
				ph.setAdj_reason_type(rs.getString("ADJ_REASON_TYPE"));
				ph.setAdj_reason(rs.getString("ADJ_REASON"));
				ph.setOrg_Point_Balance(rs.getLong("ORG_POINT_BALANCE"));
				ph.setPoint_Balance(rs.getLong("POINT_BALANCE"));
				ph.setIs_Sync_Balance(rs.getLong("IS_SYNC_BALANCE"));
				ph.setIsdelete(rs.getInt("ISDELETE"));
				ph.setCreate_By(rs.getString("CREATE_BY"));
				ph.setCreate_Date(DateUtil.getDateStrss(rs.getDate("CREATE_DATE")));
				ph.setUpdate_By(rs.getString("UPDATE_BY"));
				ph.setUpdate_Date(DateUtil.getDateStrss(rs.getDate("UPDATE_DATE")));
				ph.setVersion(rs.getInt("VERSION"));
				phlist.add(ph);
			}
 
	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
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
		return phlist;
	}

	@Override
	public long fandPointHistroyResetTotal(Connection conn, String date) {
		// TODO Auto-generated method stub
		PreparedStatement pst=null;// 
		ResultSet rs=null;

		Long seq = null  ;
		String yy = (String) date.subSequence(0, 4);
		int intyy = Integer.valueOf(yy);
		String yyyy = String.valueOf(intyy);
		String te = "0101";
		String time =yyyy+te; 
		String mmdd = "1231";
		String ymd = yyyy+mmdd;
		
		String sql = "SELECT COUNT(*) totalnum　 from (select     MEMBER_ID  from T_POINT_HISTORY  where 1=1 and to_char(EXCHANGE_POINT_EXPIRE_TIME,'yyyymmdd') between "+time+" and "+ymd+"  group by MEMBER_ID) A";
		//SENDDBConnection db=SENDDBConnection.getInstance();	
		//conn=db.getConnection();  
		
		try {
			pst=conn.prepareStatement(sql);
			rs=pst.executeQuery();
			while (rs.next()) {
				 seq=(rs.getLong("totalnum"));
			}

	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error(e);
			e.printStackTrace();
		}	finally {
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
	public List<PointHistroy> fandPointHistroyByExchange_point_expire_time(
			Connection conn, String date, long memberId) {
		// TODO Auto-generated method stub
		//会员级别当前有效期前12月（1月1日00:00:00  到 升级计算时间）
		PreparedStatement pst=null;// 
		ResultSet rs=null;

		String yy = (String) date.subSequence(0, 4);
		int intyy = Integer.valueOf(yy);
		String yyyy = String.valueOf(intyy);
		String te = "0101";
		String teda="1231";
		String time =yyyy+te; 
		String dada =yyyy+teda;
		List<PointHistroy> phlist = new ArrayList<PointHistroy>();//用于接受会员积分统计历史
		String sql = "select  * from T_POINT_HISTORY where to_char(EXCHANGE_POINT_EXPIRE_TIME,'yyyymmdd')  between  ? and ? and MEMBER_ID=? ";
		try {
			pst=conn.prepareStatement(sql);
			pst.setString(1,time);
			pst.setString(2,dada);
			pst.setLong(3,memberId);
			rs=pst.executeQuery();
		//	System.out.println(sql+"  "+time+" "+date+"  "+memberId);
			while (rs.next()) {
				PointHistroy ph= new PointHistroy();
//				mp.setExg_Expire_Point_Balance(rs.getLong("IntegralReset"));
//				mp.setMemberId(rs.getLong("MEMBER_ID"));
				ph.setPointHistoryid(rs.getLong("POINT_HISTORY_ID"));
				ph.setMemberid(rs.getLong("MEMBER_ID"));
				ph.setSetTime( DateUtil.getDateStrss(rs.getDate("SET_TIME")));
				ph.setLevel_Point(rs.getLong("LEVEL_POINT"));
				ph.setTicket_Count(rs.getInt("TICKET_COUNT"));
				ph.setActivity_Point(rs.getLong("ACTIVITY_POINT"));
				ph.setExchange_Point(rs.getLong("EXCHANGE_POINT"));
				ph.setExchange_Point_Expire_Time(DateUtil.getDateStrss(rs.getDate("EXCHANGE_POINT_EXPIRE_TIME")));
				ph.setPoint_Sys(rs.getString("POINT_SYS"));
				ph.setPoint_Trans_Type(rs.getString("POINT_TRANS_TYPE"));
				ph.setPoint_Trans_Code(rs.getString("POINT_TRANS_CODE"));
				ph.setPoint_Trans_Code_Web(rs.getString("POINT_TRANS_CODE_WEB"));
				ph.setAdj_reason_type(rs.getString("ADJ_REASON_TYPE"));
				ph.setAdj_reason(rs.getString("ADJ_REASON"));
				ph.setOrg_Point_Balance(rs.getLong("ORG_POINT_BALANCE"));
				ph.setPoint_Balance(rs.getLong("POINT_BALANCE"));
				ph.setIs_Sync_Balance(rs.getLong("IS_SYNC_BALANCE"));
				ph.setIsdelete(rs.getInt("ISDELETE"));
				ph.setCreate_By(rs.getString("CREATE_BY"));
				ph.setCreate_Date(DateUtil.getDateStrss(rs.getDate("CREATE_DATE")));
				ph.setUpdate_By(rs.getString("UPDATE_BY"));
				ph.setUpdate_Date(DateUtil.getDateStrss(rs.getDate("UPDATE_DATE")));
				ph.setVersion(rs.getInt("VERSION"));
				ph.setMember_point_id(rs.getLong("MEMBER_POINT_ID"));
				ph.setAdj_reason_type(rs.getString("ADJ_REASON_TYPE"));
				ph.setAdj_reason(rs.getString("ADJ_REASON"));
				ph.setOrder_id(rs.getString("ORDER_ID"));
				ph.setProduct_name(rs.getString("PRODUCT_NAME"));
				ph.setIs_succeed(rs.getString("IS_SUCCEED"));
				ph.setCinema_inner_code(rs.getString("CINEMA_INNER_CODE"));
				phlist.add(ph);
			}
	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
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
		return phlist;
	}

	@Override
	public long fandPointHistroyByPoint_Trans_Type(Connection conn,
			String Point_Trans_Type) {
		// TODO Auto-generated method stub
		
		Long seq = null  ;
		PreparedStatement pst=null;// 
		ResultSet rs=null;

		String sql = "SELECT COUNT(*) totalnum　 from T_POINT_HISTORY  where Point_Trans_Type=?";
		//SENDDBConnection db=SENDDBConnection.getInstance();	
		//conn=db.getConnection();  
		
		try {
			pst=conn.prepareStatement(sql);
			pst.setString(1,Point_Trans_Type);
			rs=pst.executeQuery();
			while (rs.next()) {
				 seq=(rs.getLong("totalnum"));
			}

	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
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
	public List<PointHistroy> fandPointHistroyByPage(Connection conn,
			String pointTransType, Page page) {
		// TODO Auto-generated method stub
		 List<PointHistroy> list = new ArrayList<PointHistroy>();
		PreparedStatement pst=null;// 
		ResultSet rs=null;
	
			String sql = "SELECT * FROM (  "
					+ " SELECT A.*, ROWNUM RN  "
					+ " FROM (select *  from T_POINT_HISTORY  where T_POINT_HISTORY.Point_Trans_Type=?) A  "
					+ " WHERE ROWNUM <= ?) "
					+ " WHERE RN >= ? "; 
			//SENDDBConnection db=SENDDBConnection.getInstance();	
			//conn=db.getConnection();
			//System.out.println(" "+sql);
			try {
				pst=conn.prepareStatement(sql);
				//pst.setString(1,date);
				pst.setString(1, pointTransType);
				pst.setLong(2,page.getPageSize());
				pst.setLong(3,page.getPage());
				rs=pst.executeQuery();
				while (rs.next()) {
					PointHistroy ph= new PointHistroy();
//					mp.setExg_Expire_Point_Balance(rs.getLong("IntegralReset"));
//					mp.setMemberId(rs.getLong("MEMBER_ID"));
					ph.setPointHistoryid(rs.getLong("POINT_HISTORY_ID"));
					ph.setMemberid(rs.getLong("MEMBER_ID"));
					ph.setSetTime( DateUtil.getDateStrss(rs.getDate("SET_TIME")));
					ph.setLevel_Point(rs.getLong("LEVEL_POINT"));
					ph.setTicket_Count(rs.getInt("TICKET_COUNT"));
					ph.setActivity_Point(rs.getLong("ACTIVITY_POINT"));
					ph.setExchange_Point(rs.getLong("EXCHANGE_POINT"));
					ph.setExchange_Point_Expire_Time(DateUtil.getDateStrss(rs.getDate("EXCHANGE_POINT_EXPIRE_TIME")));
					ph.setPoint_Sys(rs.getString("POINT_SYS"));
					ph.setPoint_Trans_Type(rs.getString("POINT_TRANS_TYPE"));
					ph.setPoint_Trans_Code(rs.getString("POINT_TRANS_CODE"));
					ph.setPoint_Trans_Code_Web(rs.getString("POINT_TRANS_CODE_WEB"));
					ph.setAdj_reason_type(rs.getString("ADJ_REASON_TYPE"));
					ph.setAdj_reason(rs.getString("ADJ_REASON"));
					ph.setOrg_Point_Balance(rs.getLong("ORG_POINT_BALANCE"));
					ph.setPoint_Balance(rs.getLong("POINT_BALANCE"));
					ph.setIs_Sync_Balance(rs.getLong("IS_SYNC_BALANCE"));
					ph.setIsdelete(rs.getInt("ISDELETE"));
					ph.setCreate_By(rs.getString("CREATE_BY"));
					ph.setCreate_Date(DateUtil.getDateStrss(rs.getDate("CREATE_DATE")));
					ph.setUpdate_By(rs.getString("UPDATE_BY"));
					ph.setUpdate_Date(DateUtil.getDateStrss(rs.getDate("UPDATE_DATE")));
					ph.setVersion(rs.getInt("VERSION"));
					ph.setMember_point_id(rs.getLong("MEMBER_POINT_ID"));
					ph.setAdj_reason_type(rs.getString("ADJ_REASON_TYPE"));
					ph.setAdj_reason(rs.getString("ADJ_REASON"));
					ph.setOrder_id(rs.getString("ORDER_ID"));
					ph.setProduct_name(rs.getString("PRODUCT_NAME"));
					ph.setIs_succeed(rs.getString("IS_SUCCEED"));
					ph.setCinema_inner_code(rs.getString("CINEMA_INNER_CODE"));
					list.add(ph);
				}

		
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.error(e);
				e.printStackTrace();
			}	finally {
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
			 
		
		return list;
		
	}

	@Override
	public List<PointHistroy> fandPointHistroyByLevel(Connection conn,
			long memberId) {
		// TODO Auto-generated method stub
		//会员级别当前有效期前12月（1月1日00:00:00  到 升级计算时间）
		PreparedStatement pst=null;// 
		ResultSet rs=null;

	//	String yy = (String) date.subSequence(0, 4);
//		int intyy = Integer.valueOf(yy);
//		String yyyy = String.valueOf(intyy);
//		String te = "0101";
//		String teda="1231";
//		String time =yyyy+te; 
//		String dada =yyyy+teda;
		List<PointHistroy> phlist = new ArrayList<PointHistroy>();//用于接受会员积分统计历史
		String sql ="select  MEMBER_ID,sum(LEVEL_POINT) LEVEL_POINT, sum(TICKET_COUNT) TICKET_COUNT "
		+"  from T_POINT_HISTORY t    where  MEMBER_ID = ? and IS_HISTORY<>'1' group by MEMBER_ID	"  ;
		    
		    try {
			pst=conn.prepareStatement(sql);
			pst.setLong(1,memberId);
			rs=pst.executeQuery();
			logger.info(" select * T_POINT_HISTORY  MEMBER_ID= "+memberId);
//			System.out.println(sql+"  "+time+" "+dada+"  "+memberId);
			while (rs.next()) {
				PointHistroy ph= new PointHistroy();
//				mp.setExg_Expire_Point_Balance(rs.getLong("IntegralReset"));
//				mp.setMemberId(rs.getLong("MEMBER_ID"));
		//		ph.setPointHistoryid(rs.getLong("POINT_HISTORY_ID"));
				ph.setMemberid(rs.getLong("MEMBER_ID"));
			//	ph.setSetTime( DateUtil.getDateStrss(rs.getDate("SET_TIME")));
				ph.setLevel_Point(rs.getLong("LEVEL_POINT"));
				ph.setTicket_Count(rs.getInt("TICKET_COUNT"));
//				ph.setActivity_Point(rs.getLong("ACTIVITY_POINT"));
//				ph.setExchange_Point(rs.getLong("EXCHANGE_POINT"));
//				ph.setExchange_Point_Expire_Time(DateUtil.getDateStrss(rs.getDate("EXCHANGE_POINT_EXPIRE_TIME")));
//				ph.setPoint_Sys(rs.getString("POINT_SYS"));
//				ph.setPoint_Trans_Type(rs.getString("POINT_TRANS_TYPE"));
//				ph.setPoint_Trans_Code(rs.getString("POINT_TRANS_CODE"));
//				ph.setPoint_Trans_Code_Web(rs.getString("POINT_TRANS_CODE_WEB"));
//				ph.setAdj_reason_type(rs.getString("ADJ_REASON_TYPE"));
//				ph.setAdj_reason(rs.getString("ADJ_REASON"));
//				ph.setOrg_Point_Balance(rs.getLong("ORG_POINT_BALANCE"));
//				ph.setPoint_Balance(rs.getLong("POINT_BALANCE"));
//				ph.setIs_Sync_Balance(rs.getLong("IS_SYNC_BALANCE"));
//				ph.setIsdelete(rs.getInt("ISDELETE"));
//				ph.setCreate_By(rs.getString("CREATE_BY"));
//				ph.setCreate_Date(DateUtil.getDateStrss(rs.getDate("CREATE_DATE")));
//				ph.setUpdate_By(rs.getString("UPDATE_BY"));
//				ph.setUpdate_Date(DateUtil.getDateStrss(rs.getDate("UPDATE_DATE")));
//				ph.setVersion(rs.getInt("VERSION"));
				phlist.add(ph);
			}
			 
	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error(e);
			e.printStackTrace();
		} 	finally {
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
		return phlist;
	}

}
