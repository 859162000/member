package com.wanda.mms.control.stream;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.solar.etl.config.mapping.Field;
import com.solar.etl.config.mapping.FieldSet;
import com.solar.etl.db.ResultQuery;
import com.solar.etl.db.SqlHelp;
import com.solar.etl.log.LogUtils;
import com.solar.etl.spi.LineHandle;
import com.wanda.mms.control.stream.util.DateUtil;
import com.wanda.mms.control.stream.vo.MemberPoint;


/**
 *	会员积分计算 ：影票计算
 * @author wangshuai
 * @date 2013-07-09	
 */

public class PointHistoryLineHandle implements LineHandle{
	private Connection conn;
	public PointHistoryLineHandle(Connection conn){
		this.conn=conn;
	}
	public PointHistoryLineHandle(){
		conn=Basic.mbr;
//		try {
//			conn.setAutoCommit(false);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	@Override
	public int handle(FieldSet fieldset) {
		 
		int r=0;
		Dimdef dd = new Dimdef();
		//String sql ="select  MEMBER_ID from T_TICKET_TRANS_DETAIL t   where to_char(BIZ_DATE,'yyyymmdd') between ? and ?  group by MEMBER_ID ";
	//	String sql ="select  MEMBER_ID from T_TICKET_TRANS_DETAIL t   where IS_POINT!=? and IS_POINT!=? and IS_POINT!=?  group by MEMBER_ID ";
		String sqlFilm ="";
		 
	//	String sqlType ="select t_ticket_payment_id,payment_name,payment_hash from T_TICKET_PAYMENT_TYPE t where PAYMENT_HASH=?";//查询出支付方式
	//	String sqlup="";
		MemberPoint mb = new MemberPoint();
		String sqlfandmember = "select p.MEMBER_POINT_ID,EXG_POINT_BALANCE,EXG_EXPIRE_POINT_BALANCE,POINT_HISTORY_ID,LEVEL_POINT_TOTAL,ACTIVITY_POINT,p.ISDELETE,p.CREATE_BY,p.CREATE_DATE,p.UPDATE_BY,p.UPDATE_DATE,p.VERSION,LAST_EXCHANGE_DATE,p.MEMBER_ID from T_MEMBER_POINT p ,T_MEMBER m  where p.member_id=m.member_id and m.status='1' and m.isdelete='0' and p.member_id=?";
		
		String sqlupmember="UPDATE T_MEMBER_POINT SET EXG_POINT_BALANCE=decode(sign(EXG_POINT_BALANCE+?),-1,0,EXG_POINT_BALANCE+?),LEVEL_POINT_TOTAL=LEVEL_POINT_TOTAL+?,UPDATE_DATE=sysdate,IS_LEVEL='0' WHERE MEMBER_ID=?";
		
		String sqlupdd=" UPDATE T_TICKET_TRANS_ORDER SET IS_POINT=? ,POINT=? where IS_POINT=2 and CINEMA_INNER_CODE=? and TRANS_ORDER_ID=?";
		 
		String sqlorder="SELECT TRANS_ID FROM T_TICKET_TRANS_ORDER where CINEMA_INNER_CODE=? and TRANS_ORDER_ID=?";
		
		String sqlupyp="UPDATE T_TICKET_TRANS_DETAIL  SET IS_POINT=? where  TRANS_ORDER_ID=? AND CINEMA_INNER_CODE=?";
		
		
		
	 
		
		Field mfield=fieldset.getFieldByName("MEMBER_ID");
		Field lpfield=fieldset.getFieldByName("LEVEL_POINT");//1,2
		Field ofield=fieldset.getFieldByName("ORG_POINT_BALANCE");
		Field pbfield=fieldset.getFieldByName("POINT_BALANCE");
		Field ttfield=fieldset.getFieldByName("T_TRANS_ORDER_ID");
		 
		Field cncfield=fieldset.getFieldByName("CINEMA_INNER_CODE"); //2 
		Field toidfield=fieldset.getFieldByName("CREATE_DATE"); //2 
		Field pofield=fieldset.getFieldByName("UPDATE_DATE");//1,2
		Field stfield=fieldset.getFieldByName("SET_TIME");//1,2
		//  wangshua
		String IS_POINT ="2";
		String st = DateUtil.getDateStrymd(Timestamp.valueOf(stfield.destValue));
		String tm = " 23:59:59";
		stfield.destValue=st+tm;
	//	System.out.println("基本识分计算SET_TIME："+stfield.destValue);
		//ORG_POINT_BALANCE
		//POINT_BALANCE
		//POINT_HISTORY_ID
		//Field nfield=fieldset.getFieldByName("MEMBER_NUM");
		//Field filmFiled=fieldset.getFieldByName("FILM_NAME");  ?
		Field filmFiled=null;

//		System.out.println(mfield.destValue);
//		System.out.println("会员基础计算");
		ResultQuery rq= SqlHelp.query(conn, sqlfandmember,mfield.destValue);
		ResultSet rs= rq.getResultSet();
		int i =0;
		MemberPoint mp = new MemberPoint();
		try {
			if(rs!=null&&rs.next()){
			//	mfield.destValue=

				mp.setMember_point_id(rs.getLong("MEMBER_POINT_ID"));
				mp.setMemberId(rs.getLong("MEMBER_ID"));//会员ID
//可兑换积分余额#会员目前为止所有剩余的可兑换积分，t_point_history中产生积分历史数据时更新余额。在t_point_history中插入记录时，可兑换积分<>0时，同步更新此字段=此字段+可兑换积分。											
				mp.setExg_Point_Balance(rs.getLong("EXG_POINT_BALANCE"));
 
				mp.setExg_Expire_Point_Balance(rs.getLong("EXG_EXPIRE_POINT_BALANCE"));
				//最有一次引起余额计算对应的积分历史ID
				mp.setPoint_History_Id(rs.getLong("POINT_HISTORY_ID"));
				//入会至今累计的定级积分，每次定级积分增加时累加
				mp.setLevel_Point_Total(rs.getLong("LEVEL_POINT_TOTAL"));
				//入会至今非定积分积分的增加，非定级积分增加时累加
				mp.setActivity_Point(rs.getLong("ACTIVITY_POINT"));
				//产生积分兑换历史记录时,更新此值
				mp.setLast_Exchange_Date(rs.getString("LAST_EXCHANGE_DATE"));
				//逻辑删除标识,默认:0 未删除;1删除;其他:非法
				mp.setIsdelete(rs.getInt("ISDELETE"));
				//创建人员工ID(系统自动:"member_sys";管理界面:登录员工ID)
				mp.setCreate_By(rs.getString("CREATE_BY"));
				//创建时间#CREATE_DATE
				mp.setCreate_Date(DateUtil.getDateStrss(rs.getDate("CREATE_DATE")));
				//更新人员工ID(系统自动:"member_sys";管理界面:登录员工ID)
				mp.setUpdate_By(rs.getString("UPDATE_BY"));
				//更新时间#
				mp.setUpdate_Date(DateUtil.getDateStrss(rs.getDate("UPDATE_DATE")));
				//版本号
				mp.setVersion(rs.getInt("VERSION"));
				
				ofield.destValue=String.valueOf(mp.getExg_Point_Balance());
				long aa = mp.getExg_Point_Balance()+(Long.valueOf(lpfield.destValue));
				pbfield.destValue=String.valueOf(aa);
//				System.out.println("积分更新前"+ofield.destValue);
//				System.out.println("加入积分"+lpfield.destValue+"积分更新后"+pbfield.destValue);
				mb.setExg_Point_Balance(Long.valueOf(lpfield.destValue));
				mb.setLevel_Point_Total(Long.valueOf(lpfield.destValue)) ;
			 	
				String djjf = lpfield.destValue;
				
				String phseq="0";
				String mid = String.valueOf(mp.getMemberId());
				
				String[] names = {djjf,djjf,djjf,mid};
				
	
				SqlHelp.operate(conn, sqlupmember, names);
//				System.out.println("基础积分计算：更新会员积分表SQL  "+sqlupmember);
//				System.out.println("条件1:"+djjf+"条件2:"+ djjf+"条件3："+djjf+"条件4："+mid );
//
				String is ="1";
				 
				
				String point =pofield.srcValue;
				String[] ddnames = {is,point,cncfield.destValue,toidfield.srcValue};
				
				SqlHelp.operate(conn, sqlupdd, ddnames);
				
				
				ResultQuery transrq= SqlHelp.query(conn, sqlorder,cncfield.destValue,toidfield.srcValue);
				ResultSet transrqrs= transrq.getResultSet();
				if(transrqrs!=null&&transrqrs.next()){
					ttfield.destValue=transrqrs.getString("TRANS_ID");
				}
				String isp="3";
 
				String[] ypnames = {isp,toidfield.srcValue,cncfield.destValue};

				SqlHelp.operate(conn, sqlupyp, ypnames);
				transrq.free();
				r=1;
			}		
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{
			rq.free();
		}
		LogUtils.info("lpfield:"+lpfield.destValue);
		return r;
	}
	@Override
	public void commit() {
 //		try {
 //			conn.commit();
 //		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}
	@Override
	public void rollback() {
//		try {
//			conn.rollback();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
	}
}
