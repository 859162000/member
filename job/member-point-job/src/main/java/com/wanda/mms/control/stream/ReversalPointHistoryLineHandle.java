package com.wanda.mms.control.stream;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.solar.etl.config.mapping.Field;
import com.solar.etl.config.mapping.FieldSet;
import com.solar.etl.db.ResultQuery;
import com.solar.etl.db.SqlHelp;
import com.solar.etl.spi.LineHandle;
import com.wanda.mms.control.stream.util.DateUtil;
import com.wanda.mms.control.stream.vo.MemberPoint;

/**
 *	会员积分计算 ：积分冲正
 * @author wangshuai
 * @date 2013-08-01	
 */


public class ReversalPointHistoryLineHandle implements LineHandle{
	private Connection conn;
	public ReversalPointHistoryLineHandle(Connection conn){
		this.conn=conn;
	}
	public ReversalPointHistoryLineHandle(){
		conn=Basic.mbr;
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
		
		String sqlupmember="UPDATE T_MEMBER_POINT SET EXG_POINT_BALANCE=decode(sign(EXG_POINT_BALANCE+?),-1,0,EXG_POINT_BALANCE+?),LEVEL_POINT_TOTAL=LEVEL_POINT_TOTAL+?,ACTIVITY_POINT =ACTIVITY_POINT+?,UPDATE_DATE=sysdate WHERE MEMBER_ID=?";

		
		Field mfield=fieldset.getFieldByName("MEMBER_ID");
		Field lpfield=fieldset.getFieldByName("LEVEL_POINT");//定级
		Field apfield=fieldset.getFieldByName("ACTIVITY_POINT");//非定级
		Field epfield=fieldset.getFieldByName("EXCHANGE_POINT");//可兑换
		Field ofield=fieldset.getFieldByName("ORG_POINT_BALANCE");//更新前
		Field pbfield=fieldset.getFieldByName("POINT_BALANCE");//更新后
 
		lpfield.destValue="-"+lpfield.destValue;
		apfield.destValue="-"+apfield.destValue;
		epfield.destValue="-"+epfield.destValue;
		
		System.out.println("定级"+lpfield.destValue+"非定级"+apfield.destValue+"可兑换"+epfield.destValue);

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
				long aa = mp.getExg_Point_Balance()+(Long.valueOf(epfield.destValue));
				pbfield.destValue=String.valueOf(aa);
//				System.out.println("积分更新前"+ofield.destValue);
//				System.out.println("加入积分"+lpfield.destValue+"积分更新后"+pbfield.destValue);
				mb.setExg_Point_Balance(Long.valueOf(epfield.destValue));
				mb.setLevel_Point_Total(Long.valueOf(lpfield.destValue)) ;
			 	
				String djjf = lpfield.destValue;
				String djjf1 = apfield.destValue;
				String djjf2 = epfield.destValue;
				String mid = String.valueOf(mp.getMemberId());
				
				String[] names = {djjf2,djjf2,djjf,djjf1,mid};
				
	
				SqlHelp.operate(conn, sqlupmember, names);

				r=1;
			}		
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{
			rq.free();
		}
		return r;
	}
	@Override
	public void commit() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void rollback() {
		// TODO Auto-generated method stub
		
	}
}
