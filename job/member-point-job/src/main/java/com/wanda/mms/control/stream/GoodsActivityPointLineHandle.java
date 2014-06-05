package com.wanda.mms.control.stream;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.solar.etl.config.mapping.Field;
import com.solar.etl.config.mapping.FieldSet;
import com.solar.etl.db.ResultQuery;
import com.solar.etl.db.SqlHelp;
import com.solar.etl.spi.LineHandle;

/**
 *	会员积分计算： 特殊积分计算 卖品计算 
 * @author wangshuai
 * @date 2013-07-09	
 */



public class GoodsActivityPointLineHandle implements LineHandle{
	private Connection conn;
	public GoodsActivityPointLineHandle(Connection conn){
		this.conn=conn;
	}
	public GoodsActivityPointLineHandle(){
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
		String sqlFilm ="select TMP_EXT_TRANS_DETAIL_ID,TRANS_DETAIL_TYPE,TRANS_DETAIL_CODE,EXT_POINT_RULE_ID from TMP_EXT_TRANS_DETAIL where TRANS_DETAIL_CODE=? and TRANS_DETAIL_TYPE='con' and BK_CT_ORDER_CODE=? and CINEMA_INNER_CODE=?";
	//	String sqlType ="select t_ticket_payment_id,payment_name,payment_hash from T_TICKET_PAYMENT_TYPE t where PAYMENT_HASH=?";//查询出支付方式
	//	String sqlup="";
		String sqll = "select EXT_POINT_RULE_ID,EXT_POINT_CRITERIA_ID,CODE,NAME,START_DTIME,END_DTIME,ADDITION_PERCENT,ADDITION_CODE,STATUS from T_EXT_POINT_RULE where EXT_POINT_RULE_ID=?";
		String sqlts ="select SEGMENT_ID,NAME,CODE,SEGMENT_TYPE,CRITERIA_SCHEME,SORT_NAME,SORT_ORDER,MAX_COUNT,CAL_COUNT,UPDATE_BY,UPDATE_DATE from T_SEGMENT t where SEGMENT_ID=?";
		
		String upsql="UPDATE TMP_EXT_TRANS_DETAIL SET ACTIVITY_POINT=? where  TMP_EXT_TRANS_DETAIL_ID=?";
		 
		Field mfield=fieldset.getFieldByName("T_GOODS_TRANS_DETAIL_ID");
		Field amountfield=fieldset.getFieldByName("AMONT");
		Field lpfield=fieldset.getFieldByName("LEVEL_POINT");
		Field apfield=fieldset.getFieldByName("ACTIVITY_POINT");
		Field ispfield=fieldset.getFieldByName("IS_POINT");
		Field pfield=fieldset.getFieldByName("POINT");
		Field tfield=fieldset.getFieldByName("GOODS_ID");
		Field efield=fieldset.getFieldByName("EXT_POINT_RULE_ID");
		Field cicfield=fieldset.getFieldByName("CINEMA_INNER_CODE");
		Field toidfield=fieldset.getFieldByName("TRANS_ORDER_ID");
		Field cofield = fieldset.getFieldByName("GOODS_COUNT");
		//TICKET_NO GOODS_COUNT
		//Field nfield=fieldset.getFieldByName("MEMBER_NUM");
		//Field filmFiled=fieldset.getFieldByName("FILM_NAME");  ?
		Field filmFiled=null;
		String extid = "";

		ResultQuery rq= SqlHelp.query(conn, sqlFilm,tfield.destValue,toidfield.destValue,cicfield.destValue);
		ResultSet rs= rq.getResultSet();
		int i =0;
		
		
		
		try { 
				while(rs!=null&&rs.next()){
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
					ResultQuery erq= SqlHelp.query(conn, sqll,extid);
					ResultSet ers= erq.getResultSet();
					if(ers!=null&&ers.next()){
					String percent = ers.getString("ADDITION_PERCENT");
					String code = ers.getString("ADDITION_CODE");
						if(percent!=null){
							if(lpfield.destValue==null){
								lpfield.destValue="0";
							}
						double amo = Double.valueOf(lpfield.destValue);
						 
						double de = Double.valueOf(percent)/100;
						double qjgz=amo*de;
						String ss1 = String.valueOf(qjgz);
						BigDecimal d5 = new BigDecimal( ss1  );
						String s6 =d5.setScale(0,BigDecimal.ROUND_DOWN).toString();
						String seq=rs.getString("TMP_EXT_TRANS_DETAIL_ID");
						//更新S6 upsql
						SqlHelp.operate(conn, upsql, s6,seq);
						if(apfield.destValue==null){
							apfield.destValue="0";
						}
						
							apfield.destValue=String.valueOf(Long.valueOf(apfield.destValue)+Long.valueOf(s6));
						int a7=	Integer.valueOf(s6)+Integer.valueOf(lpfield.destValue);
							String s7=String.valueOf(a7);
							pfield.destValue=(s7);
						}else{
							String seq=rs.getString("TMP_EXT_TRANS_DETAIL_ID");
							//更新S6 upsql
						String con  = 	cofield.destValue;
						String count =String.valueOf( Long.valueOf(con)*Long.valueOf(code));
							SqlHelp.operate(conn, upsql,count,seq);
							if(apfield.destValue==null){
								apfield.destValue="0";
							}
							apfield.destValue=String.valueOf(Long.valueOf(apfield.destValue)+Long.valueOf(count));
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
			e.printStackTrace();
		}finally{
			rq.free();
		}
		if(r!=1){//如果特殊积分无进入
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
