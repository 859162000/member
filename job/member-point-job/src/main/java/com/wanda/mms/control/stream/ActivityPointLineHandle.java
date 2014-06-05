package com.wanda.mms.control.stream;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.solar.etl.config.mapping.Field;
import com.solar.etl.config.mapping.FieldSet;
import com.solar.etl.db.ResultQuery;
import com.solar.etl.db.SqlHelp;
import com.solar.etl.log.LogUtils;
import com.solar.etl.spi.LineHandle;

/**
 *	会员积分计算： 特殊积分计算  
 * @author wangshuai
 * @date 2013-07-09	
 */



public class ActivityPointLineHandle implements LineHandle{
	static Logger logger = Logger.getLogger(ActivityPointLineHandle.class.getName());
	private Connection conn;
	public ActivityPointLineHandle(Connection conn){
		this.conn=conn;
	}
	public ActivityPointLineHandle(){
		conn=Basic.mbr;
	}
	@Override
	public int handle(FieldSet fieldset) {
		 // 两个状态字段来判断 第 一 是否为 sale 销售 第二个状态是为 影票与卖品
		int r=0;
		Dimdef dd = new Dimdef();
		//SegmentCriteriaService sc = new SegmentCriteriaService();
		 
		//String sql ="select  MEMBER_ID from T_TICKET_TRANS_DETAIL t   where to_char(BIZ_DATE,'yyyymmdd') between ? and ?  group by MEMBER_ID ";
	//	String sql ="select  MEMBER_ID from T_TICKET_TRANS_DETAIL t   where IS_POINT!=? and IS_POINT!=? and IS_POINT!=?  group by MEMBER_ID ";
//		String sqlFilm ="select   TICKET_TRANS_DETAIL_ID,TICKET_TYPE_NAME,TICKET_TYPE_CODE,AMOUNT,PAYMENT_HASH,TICKET_NO,TRANS_ORDER_ID "
// +" ,ORDER_ID,CINEMA_INNER_CODE,BIZ_DATE,POINT,COLUMN1,MEMBER_ID,HIS_MEMBER_NO ,LEVEL_POINT,ACTIVITY_POINT,IS_POINT,IS_TICKET "
// +" from T_TICKET_TRANS_DETAIL t  where TICKET_TRANS_DETAIL_ID =? ";
		String sqlFilm ="select TMP_EXT_TRANS_DETAIL_ID,TRANS_DETAIL_TYPE,TRANS_DETAIL_CODE,EXT_POINT_RULE_ID,ORDER_ID,CINEMA_INNER_CODE,BK_CT_ORDER_CODE from TMP_EXT_TRANS_DETAIL where TRANS_DETAIL_CODE=?" +
				"and BK_CT_ORDER_CODE=?  and TRANS_DETAIL_TYPE='ticket' ";
		
		
	//	String sqlFilm ="select TMP_EXT_TRANS_DETAIL_ID,TRANS_DETAIL_TYPE,TRANS_DETAIL_CODE,EXT_POINT_RULE_ID,ORDER_ID,CINEMA_INNER_CODE,BK_CT_ORDER_CODE from TMP_EXT_TRANS_DETAIL where TRANS_DETAIL_CODE=?" +
	//	" and TRANS_DETAIL_TYPE='ticket' ";	
		
		
		
		
	//	String sqlType ="select t_ticket_payment_id,payment_name,payment_hash from T_TICKET_PAYMENT_TYPE t where PAYMENT_HASH=?";//查询出支付方式
	//	String sqlup="";
		String sqll = "select EXT_POINT_RULE_ID,EXT_POINT_CRITERIA_ID,CODE,NAME,START_DTIME,END_DTIME,ADDITION_PERCENT,ADDITION_CODE,STATUS from T_EXT_POINT_RULE where EXT_POINT_RULE_ID=?";
		String sqlts ="select SEGMENT_ID,NAME,CODE,SEGMENT_TYPE,CRITERIA_SCHEME,SORT_NAME,SORT_ORDER,MAX_COUNT,CAL_COUNT,UPDATE_BY,UPDATE_DATE from T_SEGMENT t where SEGMENT_ID=?";
		
		String upsql="UPDATE TMP_EXT_TRANS_DETAIL SET ACTIVITY_POINT=? where  TMP_EXT_TRANS_DETAIL_ID=?";
		

//		Field mfield=fieldset.getFieldByName("TICKET_TRANS_DETAIL_ID");
		Field amountfield=fieldset.getFieldByName("AMOUNT");
		Field lpfield=fieldset.getFieldByName("LEVEL_POINT");
		Field apfield=fieldset.getFieldByName("ACTIVITY_POINT");//特殊积分
		Field ispfield=fieldset.getFieldByName("IS_POINT");
		Field pfield=fieldset.getFieldByName("POINT");//积分
		
		Field efield=fieldset.getFieldByName("EXT_POINT_RULE_ID");
		Field tfield=fieldset.getFieldByName("TICKET_NO");
		Field toidfield=fieldset.getFieldByName("TRANS_ORDER_ID");
		//TICKET_NO   
		//Field nfield=fieldset.getFieldByName("MEMBER_NUM");
		//Field filmFiled=fieldset.getFieldByName("FILM_NAME");  ?
		 
		String extid="";

		ResultQuery rq= SqlHelp.query(conn, sqlFilm,tfield.destValue,toidfield.destValue);
	//	ResultQuery rq= SqlHelp.query(conn, sqlFilm,tfield.destValue);
		ResultSet rs= rq.getResultSet();
		int i =0;
		
//		System.out.println(sqlFilm);
//		System.out.println("条件1："+tfield.destValue+"条件2："+toidfield.destValue);

	
		
		
		try {
			
			while(rs!=null&&rs.next()){//轮训 TMP_EXT_TRANS_DETAIL 数据
				
			//	mfield.destValue=
				
				if(efield.destValue==null){
					efield.destValue=rs.getString("EXT_POINT_RULE_ID");
					extid=rs.getString("EXT_POINT_RULE_ID");
				}else{
					StringBuffer eid =new StringBuffer();
					eid.append(efield.destValue).append(",");
					efield.destValue=eid.toString()+rs.getString("EXT_POINT_RULE_ID");
					extid=rs.getString("EXT_POINT_RULE_ID");
				}
				LogUtils.info("rule Sql:"+sqll);
				LogUtils.info("extid:"+extid);
				ResultQuery erq= SqlHelp.query(conn, sqll,extid);
				ResultSet ers= erq.getResultSet();
				if(ers!=null&&ers.next()){
				String percent = ers.getString("ADDITION_PERCENT");
				String code = ers.getString("ADDITION_CODE");
					if(percent!=null){
						double amo = Double.valueOf(amountfield.destValue);
						double de = Double.valueOf(percent)/100;
						double qjgz=amo*de;
						String ss1 = String.valueOf(qjgz);
						BigDecimal d5 = new BigDecimal( ss1  );
						String s6 =d5.setScale(0,BigDecimal.ROUND_DOWN).toString();
						String seq=rs.getString("TMP_EXT_TRANS_DETAIL_ID");
						//更新S6 upsql
						SqlHelp.operate(conn, upsql, s6,seq);
						if(apfield.destValue==null)
						{
							apfield.destValue="0";
						}
						apfield.destValue=String.valueOf(Long.valueOf(apfield.destValue)+Long.valueOf(s6));
							
						int a7=	Integer.valueOf(s6)+Integer.valueOf(lpfield.destValue);
							String s7=String.valueOf(a7);
							pfield.destValue=(s7);
					}else{
						if(apfield.destValue==null){
							apfield.destValue="0";
						}
						String seq=rs.getString("TMP_EXT_TRANS_DETAIL_ID");
						//更新S6 upsql
						LogUtils.info("upsql:"+upsql);
						LogUtils.info("seq:"+seq);
						SqlHelp.operate(conn, upsql, code,seq);
						apfield.destValue=String.valueOf(Long.valueOf(apfield.destValue)+Long.valueOf(code));
						int a7=	Integer.valueOf(apfield.destValue)+Integer.valueOf(lpfield.destValue);
						String s7=String.valueOf(a7);
						pfield.destValue=(s7);
				}
			}
				ispfield.destValue="2";
				erq.free();
				r=1;
				
				}
			 
				
		       
		
			 	
			
		}catch (SQLException e) {
			logger.error(e);
			e.printStackTrace();
		}finally{
			ispfield.destValue="2";
			rq.free();
		}
		if(r!=1){
		apfield.destValue="0";
		int a7=	Integer.valueOf("0")+Integer.valueOf(lpfield.destValue);
		String s7=String.valueOf(a7);
		pfield.destValue=(s7);
		ispfield.destValue="2";
		r=1;
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
