package com.wanda.mms.control.stream;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.solar.etl.config.mapping.Field;
import com.solar.etl.config.mapping.FieldSet;
import com.solar.etl.db.ResultQuery;
import com.solar.etl.db.SqlHelp;
import com.solar.etl.spi.LineHandle;
import com.wanda.mms.control.stream.util.DateUtil;
import com.wanda.mms.control.stream.vo.MemberPoint;


/**
 *	会员积分计算 ：卖品计算
 * @author wangshuai
 * @date 2013-07-09	
 */


public class GoodPointHistoryLineHandle implements LineHandle{
	private Connection conn;
	public GoodPointHistoryLineHandle(Connection conn){
		this.conn=conn;
	}
	public GoodPointHistoryLineHandle(){
		conn=Basic.mbr;
	}
	@Override
	public int handle(FieldSet fieldset) {
		 
		int r=0;

		MemberPoint mb = new MemberPoint();
		String sqlfandmember = "select p.MEMBER_POINT_ID,EXG_POINT_BALANCE,EXG_EXPIRE_POINT_BALANCE,POINT_HISTORY_ID,LEVEL_POINT_TOTAL,ACTIVITY_POINT,p.ISDELETE,p.CREATE_BY,p.CREATE_DATE,p.UPDATE_BY,p.UPDATE_DATE,p.VERSION,LAST_EXCHANGE_DATE,p.MEMBER_ID from T_MEMBER_POINT p ,T_MEMBER m  where p.member_id=m.member_id and m.status='1' and m.isdelete='0' and p.member_id=?";

		String sqlupmember="UPDATE T_MEMBER_POINT SET EXG_POINT_BALANCE=decode(sign(EXG_POINT_BALANCE+?),-1,0,EXG_POINT_BALANCE+?),LEVEL_POINT_TOTAL=LEVEL_POINT_TOTAL+?,UPDATE_DATE=sysdate,IS_LEVEL='0' WHERE MEMBER_ID=?";
		
		String sqlupdd=" UPDATE T_GOODS_TRANS_ORDER SET IS_POINT=? ,POINT=? where ORDER_ID=? and TRANS_ORDER_ID=? and IS_POINT=2 and CINEMA_INNER_CODE=?";
		
		String gsql = "select GOODS_TRANS_ID from T_GOODS_TRANS_ORDER where ORDER_ID=? and TRANS_ORDER_ID=? and IS_POINT=2 and CINEMA_INNER_CODE=?";
		 
		String sqlupyp="UPDATE T_GOODS_TRANS_DETAIL  SET IS_POINT=? where ORDER_ID=? and CINEMA_INNER_CODE=?";
				
		Field mfield=fieldset.getFieldByName("MEMBER_ID");
	    Field crfield=fieldset.getFieldByName("TICKET_COUNT");
		Field lpfield=fieldset.getFieldByName("LEVEL_POINT");//1,2
		Field ofield=fieldset.getFieldByName("ORG_POINT_BALANCE");
		Field pbfield=fieldset.getFieldByName("POINT_BALANCE");
		Field oridfield=fieldset.getFieldByName("POINT_TRANS_CODE"); //2 
		Field pofield=fieldset.getFieldByName("UPDATE_DATE");//1,2
		 Field cfield=fieldset.getFieldByName("CINEMA_INNER_CODE");
		 Field ttfield=fieldset.getFieldByName("T_TRANS_ORDER_ID");
		 
		 
			Field stfield=fieldset.getFieldByName("SET_TIME");//1,2
			//  wangshua
			String IS_POINT ="2";
			String st = DateUtil.getDateStrymd(Timestamp.valueOf(stfield.destValue));
			String tm = " 23:59:59";
			stfield.destValue=st+tm;
			//System.out.println("基本识分计算SET_TIME："+stfield.destValue);
		//CINEMA_INNER_CODE  T_TRANS_ORDER_ID
      //  System.out.println(crfield.destValue+""+crfield.srcValue);
		ResultQuery rq= SqlHelp.query(conn, sqlfandmember,mfield.srcValue);
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
				long aa = mp.getExg_Point_Balance()+(Long.valueOf(lpfield.srcValue));
				pbfield.destValue=String.valueOf(aa);
				mb.setExg_Point_Balance(Long.valueOf(lpfield.destValue));
				mb.setLevel_Point_Total(Long.valueOf(lpfield.destValue)) ;
			 	
				String djjf = lpfield.destValue;
				
				String phseq="0";
				String mid = String.valueOf(mp.getMemberId());
				
				String[] names = {djjf,djjf,djjf,mid};
				
	
				SqlHelp.operate(conn, sqlupmember, names);
//
				String is ="1";
				String orid =oridfield.destValue;
				
				ResultQuery transrq= SqlHelp.query(conn, gsql,orid,crfield.srcValue,cfield.destValue);
				ResultSet transrqrs= transrq.getResultSet();
				 
				if(transrqrs!=null&&transrqrs.next()){
					ttfield.destValue  =	transrqrs.getString("GOODS_TRANS_ID");//订单ID
				}
				transrq.free();
				String point = pofield.srcValue;
				String[] ddnames = {is,point,orid,crfield.srcValue,cfield.destValue};
				
				SqlHelp.operate(conn, sqlupdd, ddnames);
 
				String isp="3";
 
				String[] ypnames = {isp,orid,cfield.destValue};

				SqlHelp.operate(conn, sqlupyp, ypnames);
				
	
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
