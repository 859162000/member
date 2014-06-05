package com.wanda.mms.control.stream;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.log4j.Logger;

import com.solar.etl.config.mapping.Field;
import com.solar.etl.config.mapping.FieldSet;
import com.solar.etl.db.ResultQuery;
import com.solar.etl.db.SqlHelp;
import com.solar.etl.spi.LineHandle;
import com.wanda.mms.control.stream.util.DateUtil;
import com.wanda.mms.control.stream.vo.MemberPoint;


/**
 *	会员积分计算： 特殊积分插入积分历史  
 * @author wangshuai
 * @date 2013-07-09	
 */



public class ActivityPointHistoryLineHandle implements LineHandle{
	static Logger logger = Logger.getLogger(ActivityPointHistoryLineHandle.class.getName());
	private Connection conn;
	public ActivityPointHistoryLineHandle(Connection conn){
		this.conn=conn;
	}
	public ActivityPointHistoryLineHandle(){
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
		
		//查临时表
		String sqltemp ="select TMP_EXT_TRANS_DETAIL_ID,TRANS_DETAIL_TYPE,TRANS_DETAIL_CODE,EXT_POINT_RULE_ID,ORDER_ID,CINEMA_INNER_CODE,BK_CT_ORDER_CODE,ACTIVITY_POINT from TMP_EXT_TRANS_DETAIL where TRANS_DETAIL_CODE=?" +
		"and BK_CT_ORDER_CODE=?  and TRANS_DETAIL_TYPE='ticket' ";
		
	//	String sqltemp ="select TMP_EXT_TRANS_DETAIL_ID,TRANS_DETAIL_TYPE,TRANS_DETAIL_CODE,EXT_POINT_RULE_ID,ORDER_ID,CINEMA_INNER_CODE,BK_CT_ORDER_CODE,ACTIVITY_POINT from TMP_EXT_TRANS_DETAIL where TRANS_DETAIL_CODE=?" +
	//	"   and TRANS_DETAIL_TYPE='ticket' and ACTIVITY_POINT<>0";
		//插入积分历史 
		String sqlupmember="UPDATE T_MEMBER_POINT SET EXG_POINT_BALANCE=decode(sign(EXG_POINT_BALANCE+?),-1,0,EXG_POINT_BALANCE+?),ACTIVITY_POINT=ACTIVITY_POINT+?,UPDATE_DATE=sysdate,IS_LEVEL='0' WHERE MEMBER_ID=?";
		
		
		//String sqlupyp="UPDATE T_TICKET_TRANS_DETAIL  SET IS_POINT=? where TRANS_ORDER_ID=?";
		
		String ttsql ="SELECT TRANS_ID FROM T_TICKET_TRANS_ORDER where CINEMA_INNER_CODE=? and TRANS_ORDER_ID=?";
		
		String sqlpoint = "insert into T_POINT_HISTORY(POINT_HISTORY_ID,MEMBER_ID,SET_TIME,LEVEL_POINT,TICKET_COUNT,ACTIVITY_POINT,EXCHANGE_POINT,EXCHANGE_POINT_EXPIRE_TIME,POINT_TYPE,POINT_SYS," +
		"	POINT_TRANS_TYPE,POINT_TRANS_CODE,ADJ_RESION,ORG_POINT_BALANCE,POINT_BALANCE,IS_SYNC_BALANCE,ISDELETE,CREATE_BY,CREATE_DATE,UPDATE_BY,UPDATE_DATE,VERSION,CINEMA_INNER_CODE,POINT_EXT_RULE_ID,TRANS_TYPE,T_TRANS_ORDER_ID,T_TRANS_DETAIL_ID)" +
		"	values(S_T_POINT_HISTORY.nextVal,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),0,0,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss')  " +
				" ,?,?,?,?,?,?,?,?,?,?,sysdate,?,sysdate,?,?,?,?,?,?)";
		 //22-22
	//	String[] hnames1 = {"会员ID","非定级积分","可对换积分","3","3","3","特殊积分规则ID","特殊积分计算:影票计算 订单ID加票号","更新前的值","更新后的值","1","0","member_sys","member_sys","1","影城内码"};
	 //System.out.println();
		
		Field mfield=fieldset.getFieldByName("MEMBER_ID");
		//Field lpfield=fieldset.getFieldByName("LEVEL_POINT");//1,2
		Field apfield=fieldset.getFieldByName("ACTIVITY_POINT");//1,2
		//ACTIVITY_POINT
		 
		Field oidfield=fieldset.getFieldByName("ORDER_ID");
		Field bzfield=fieldset.getFieldByName("BIZ_DATE");
		Field oridfield=fieldset.getFieldByName("CREATE_BY"); //2 
		Field tfield=fieldset.getFieldByName("TICKET_NO");
		Field toidfield=fieldset.getFieldByName("TRANS_ORDER_ID");
		Field cicfield=fieldset.getFieldByName("CINEMA_INNER_CODE");
		Field ispfield=fieldset.getFieldByName("IS_POINT");
		Field cncfield=fieldset.getFieldByName("CINEMA_INNER_CODE"); //2 
		Field ttdidfield=fieldset.getFieldByName("TICKET_TRANS_DETAIL_ID"); //2 
		//IS_POINT   
		String IS_POINT ="2";
		
		
		 
		
		String st = DateUtil.getDateStrymd(Timestamp.valueOf(bzfield.destValue));
		String tm = " 23:59:59";
		String stm=st+tm;
		//System.out.println("特殊识分计算SET_TIME："+stm);
	
		//ORG_POINT_BALANCE
		//POINT_BALANCE
		//POINT_HISTORY_ID
		//Field nfield=fieldset.getFieldByName("MEMBER_NUM");
		//Field filmFiled=fieldset.getFieldByName("FILM_NAME");  ?
		Field filmFiled=null;

		//System.out.println(mfield.destValue);
		//System.out.println(sqlFilm);
		ResultQuery rq= SqlHelp.query(conn, sqlfandmember,mfield.destValue);
		ResultSet rs= rq.getResultSet();
		int i =0;
		MemberPoint mp = new MemberPoint();
		try {
			if(rs!=null&&rs.next()){
				
				//TICKET_NO   
				//Field nfield=fieldset.getFieldByName("MEMBER_NUM");
				//Field filmFiled=fieldset.getFieldByName("FILM_NAME");  ?
				 
				


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
				
				 
 
				
				mb.setExg_Point_Balance(Long.valueOf(apfield.destValue));
				mb.setActivity_Point(Long.valueOf(apfield.destValue)) ;
			 	
				String djjf = apfield.destValue;
				
				String phseq="0";
				String mid = String.valueOf(mp.getMemberId());
				
			//	ResultQuery temrq= SqlHelp.query(conn, sqltemp,tfield.destValue);
				ResultQuery temrq= SqlHelp.query(conn, sqltemp,tfield.destValue,toidfield.destValue);
				ResultSet temrs= temrq.getResultSet();
				
				while(temrs!=null&&temrs.next()){
					ResultQuery xrq= SqlHelp.query(conn, sqlfandmember,mfield.destValue);
					ResultSet xrs= xrq.getResultSet();
					long epb = 0;
					if(xrs!=null&&xrs.next()){
						epb=xrs.getLong("EXG_POINT_BALANCE");
					}
					xrq.free();
			   	//查询这张票有多少条规则
				String apoint=	temrs.getString("ACTIVITY_POINT");//积分
				if(apoint==null){
					apoint="0";
				}	 
				String extid =	temrs.getString("EXT_POINT_RULE_ID");//规则ID
					
				 //调用插入方法
//				String sqlpoint = "insert into T_POINT_HISTORY(POINT_HISTORY_ID,MEMBER_ID,SET_TIME,LEVEL_POINT,TICKET_COUNT,ACTIVITY_POINT,EXCHANGE_POINT,EXCHANGE_POINT_EXPIRE_TIME,POINT_TYPE,POINT_SYS," +
//				"	POINT_TRANS_TYPE,POINT_TRANS_CODE,ORG_POINT_BALANCE,POINT_BALANCE,IS_SYNC_BALANCE,ISDELETE,CREATE_BY,CREATE_DATE,UPDATE_BY,UPDATE_DATE,VERSION,CINEMA_INNER_CODE)" +
//				"	values(S_T_POINT_HISTORY.nextVal,?,sysdate,0,0,?,?,to_date(,'yyyy-mm-dd hh24:mi:ss')  " +
//						" ,?,?,?,?,?,?,?,?,?,sysdate,?,sysdate,?,?)";
				Date date = new Date();
				String da = DateUtil.getDateStrymd(date).substring(0, 4);
				
				String te="-12-31 23:59:59";
				String datea =String.valueOf(Long.valueOf(da)+1)+te;
			//	System.out.println(datea);
				
				ResultQuery transrq= SqlHelp.query(conn, ttsql,cncfield.destValue,toidfield.srcValue);
				ResultSet transrqrs= transrq.getResultSet();
				 String  trid="";//订单ID
				if(transrqrs!=null&&transrqrs.next()){
					trid  =	transrqrs.getString("TRANS_ID");
				}
				
				String trans_type = "ticket";
				String[] hnames1 = {"会员ID","非定级积分","可对换积分",datea,"3","3","3","特殊积分规则ID","特殊积分计算:影票计算 订单ID加票号","更新前的值","更新后的值","1","0","member_sys","member_sys","1","影城内码"};
				 
				String[] hnames = {mid,stm,apoint,apoint,datea,"3","3","3",oidfield.destValue,"特殊积分计算:影票计算 订单ID:"+toidfield.destValue+" 票号: "+tfield.destValue,String.valueOf(epb),String.valueOf((epb+Long.valueOf(apoint))),"1","0","member_sys","member_sys","1",cicfield.destValue,extid,trans_type,trid,ttdidfield.destValue};
				
				if(Long.valueOf(apoint)!=0){
				SqlHelp.operate(conn, sqlpoint, hnames);	
				}
				String[] names = {apoint,apoint,apoint,mid};
				
				
				SqlHelp.operate(conn, sqlupmember, names);
 
				transrq.free();
				}
				
				
//
			 
				
				String isp="4";
 
//				String[] ypnames = {isp,oridfield.srcValue};
//
//				SqlHelp.operate(conn, sqlupyp, ypnames);
				ispfield.destValue=isp;
				
				temrq.free();
				r=1;
			}		
		}catch (SQLException e) {
			logger.error(e);
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
