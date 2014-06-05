package com.wanda.mms.control.response;

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
import com.wanda.mms.control.main.AllMbrPoint;
import com.wanda.mms.control.stream.util.DateUtil;
import com.wanda.mms.control.stream.vo.MemberPoint;


/**
 *	会员营销统计 调度
 * @author wangshuai
 * @date 2013-09-11	
 */


public class TactresultLineHandle implements LineHandle{
	static Logger logger = Logger.getLogger(AllMbrPoint.class.getName());
	private Connection conn;
	public TactresultLineHandle(Connection conn){
		this.conn=conn;
	}
	public TactresultLineHandle(){
		conn=MarketingStatistics.mbr;
	}
	@Override
	public int handle(FieldSet fieldset) {
		 //
		int r=0;
		//更新活动波次响应客群表中的 STATUS 活动波次执行结果状态  10执行中  20执行结束 30统计中
		String upresultFilm ="  update t_act_result  set status =? where  act_result_id=?  ";
		 
		//	String sqlType ="select t_ticket_payment_id,payment_name,payment_hash from T_TICKET_PAYMENT_TYPE t where PAYMENT_HASH=?";//查询出支付方式
		//清理临时表客群结果
		String deltempresponsesql="delete from tmp_act_response where act_result_id=?  ";
		//获取被统计的客群ID,获取客群ID：同时取得响应设置ID，波次ID，响应类型，用于后续更新，波次编码用于输出日志，以便后续问题跟踪。	 
		String selsql = "select cmnactivtycode,act_result_id,cmn_activity_id,act_target_id,segment_id,restype from (    "+
					"select tca.code cmnactivtycode,tar.act_result_id,tca.cmn_activity_id,tat.act_target_id,tar.res_segment_id as segment_id,'res' as restype     "+
					"from t_act_result tar,t_act_target tat,t_cmn_activity tca				"+
					"where tar.cmn_activity_id=tca.cmn_activity_id and tca.act_target_id=tat.act_target_id and  tar.act_result_id=?  		"+
					"union																															"+
					"select tca.code cmnactivtycode ,tar.act_result_id,tca.cmn_activity_id,tat.act_target_id,tar.alter_segment_id as segment_id,'alter' as restype     "+
					"from t_act_result tar,t_act_target tat,t_cmn_activity tca			"+
					"where tar.cmn_activity_id=tca.cmn_activity_id and tca.act_target_id=tat.act_target_id and   tar.act_result_id=?    "+
					" ) a order by cmnactivtycode,act_result_id,a.cmn_activity_id,a.act_target_id,a.restype,a.segment_id ";
		//获取客群条件表达式
	 //	String tsegentsql="select * from t_segment where segment_id = ? ";
	 	//客群对应会员
	 	String tsegmmembersql="select SEGM_MEMBER_ID,SEGMENT_ID,MEMBER_ID from T_SEGM_MEMBER where SEGMENT_ID=?";
	 
	 	String insertTmpResponsesql="insert into tmp_act_response(ACT_RESULT_ID,CMN_ACTIVITY_ID,ACT_TARGET_ID,RESTYPE,RESPONSE_MEMBER_ID ) values (?,?,?,?,?)";
	 	
	 //	String tmpresponsesql = "select ACT_RESULT_ID,CMN_ACTIVITY_ID,ACT_TARGET_ID,RESTYPE,RESPONSE_MEMBER_ID from tmp_act_response  where act_target_id=?";
	 	//更新  推荐响应 type=res																
	 	String uprescontacthistorysql = "update t_contact_history t1 set has_response=1  where t1.act_target_id=? and t1.member_id=? ";//
		//更新  关联响应 type=alter
	 	String upaltercontactHistorysql = "update t_contact_history t1 set has_response2=1  where t1.act_target_id=? and t1.member_id=? ";
	 	
	 	
	 	String has1sql="  select count(distinct member_id) as countmemberid  from t_contact_history tch, t_act_target tat, t_cmn_activity tca where tch.has_response = 1   and tch.act_target_id = ?   and  tca.act_target_id = ?  and tca.cmn_activity_id = ? having count(distinct member_id) <> 0  ";
	 	
	 	String uphas1="update t_act_result t set res_count= ?  where t.cmn_activity_id=?";
	 	
		String has2sql="select count(distinct member_id) as countmemberid from t_contact_history tch,t_act_target tat,t_cmn_activity tca  where  tch.has_response2=1 and  tch.act_target_id=? and tca.act_target_id=? and  tca.cmn_activity_id=?";
		
		String uphas2=" update t_act_result t set alter_res_count= ? where t.cmn_activity_id=? ";
		
		String controlsql ="select count( distinct member_id) as countmemberid from t_contact_history tch,t_act_target tat,t_cmn_activity tca  where  tch.has_response=1  and tch.is_controlset  = 1 and  tch.act_target_id=? and tca.act_target_id=? and  tca.cmn_activity_id=? ";
	 	
		String upcontrolsql =" update t_act_result t set control_res_count= ? where t.cmn_activity_id=? ";
		
		String upresult=" update t_act_result t set status ='20' where  t.act_result_id=?  " ;
		
		String ycupid="update t_act_result t set status ='40' where  t.act_result_id=?   ";
		
		
		
	 	Field mfield=fieldset.getFieldByName("ACT_RESULT_ID");
		//String st = DateUtil.getDateStrymd(Timestamp.valueOf(mfield.destValue));
	 	String status="30";
		String[] hnames = {status,mfield.destValue};
		
		SqlHelp.operate(conn, upresultFilm, hnames);//更新响应设置状态(统计中)
		
		//清理客群结果临时表
		SqlHelp.operate(conn, deltempresponsesql, mfield.destValue);//更新响应设置状态(统计中)
		
		//System.out.println(selsql);
		//System.out.println(mfield.destValue);
 
		ResultQuery rq= SqlHelp.query(conn, selsql,mfield.destValue,mfield.destValue);
		ResultSet rs= rq.getResultSet();
		int i =0;
		MemberPoint mp = new MemberPoint();
		//获取被统计的客群ID
		try {
			while(rs!=null&&rs.next()){
				
			//	String cmnactivtycode =rs.getString("cmnactivtycode");//活动波次编码
				String actresultid =rs.getString("act_result_id");//活动波次执行结果ID
				String cmn_activity_id= rs.getString("cmn_activity_id");//活动波次ID
				String acttargetid = rs.getString("act_target_id");//波次目标ID
				String restype = rs.getString("restype");//type=res(推荐响应),alter(关联响应)
				String segmentid= rs.getString("segment_id");//关联响应客群ID或 推荐响应客群ID
				 
				
			//	System.out.println(segmentid);
				if(restype.equals("res")){//推荐响应 type=res
					
					ResultQuery serq= SqlHelp.query(conn, tsegmmembersql,segmentid);
					ResultSet sers= serq.getResultSet();
					while(sers!=null&&sers.next()){
						//调用客群接口返回Member_id 清单    调用结果从DW查询客群结果	 根据客群ID获得满足条件的member_id清单
						//String[] tmpactresponse = {act_result_id,act_target_id,segment_id,restype,response_member};
						String[] tmpactresponse = {actresultid,acttargetid,segmentid,restype,sers.getString("MEMBER_ID")};
					  //String[] hn = {活动波次ID,波次目标ID,type=res(推荐响应),alter(关联响应),会员ID};
						SqlHelp.operate(conn, insertTmpResponsesql, tmpactresponse);//保存结果至临时表
						System.out.println("1");
						//uprescontacthistorysql
						String[] uprescontacthistory={acttargetid,sers.getString("MEMBER_ID")};
						SqlHelp.operate(conn, uprescontacthistorysql, uprescontacthistory);//保存结果至临时表
						System.out.println(uprescontacthistorysql);
						System.out.println("ps1:"+acttargetid+"   ps2: "+sers.getString("MEMBER_ID"));
						
					}
					serq.free();
					
					
				}
				if(restype.equals("alter")){ //关联响应 type=alter
					ResultQuery serq= SqlHelp.query(conn, tsegmmembersql,segmentid);
					ResultSet sers= serq.getResultSet();
					while(sers!=null&&sers.next()){
						//调用客群接口返回Member_id 清单    调用结果从DW查询客群结果	 根据客群ID获得满足条件的member_id清单
						String[] tmpactresponse = {actresultid,acttargetid,segmentid,restype,sers.getString("MEMBER_ID")};
						SqlHelp.operate(conn, insertTmpResponsesql, tmpactresponse);//更新响应设置状态(统计中)
						//保存结果至临时表
						//
						System.out.println("2");
						String[] uprescontacthistory={acttargetid,sers.getString("MEMBER_ID")};
						SqlHelp.operate(conn, upaltercontactHistorysql, uprescontacthistory);//保存结果至临时表
						System.out.println(uprescontacthistorysql);
						System.out.println("ps1:"+acttargetid+"   ps2: "+sers.getString("MEMBER_ID"));
					}
					serq.free();
				}
					
				
				//更新响应统计信息	推荐响应统计				根据波次ID进行统计并更新，统计关联响应（has_response=1）的记录
				System.out.println(has1sql);
				System.out.println(acttargetid+"    "+acttargetid+"     "+cmn_activity_id);
				ResultQuery has1rq= SqlHelp.query(conn, has1sql,acttargetid,acttargetid,cmn_activity_id);
				ResultSet has1rs= has1rq.getResultSet();
				if(has1rs!=null&&has1rs.next()){
					 
					//System.out.println("3333333333333333333333333333");
					String[] uphas1count={has1rs.getString("countmemberid"),cmn_activity_id};
					SqlHelp.operate(conn, uphas1, uphas1count);//保存结果至活动波次执行结果表
					
				}
				has1rq.free();
				
				
				//更新响应统计信息  关联响应统计统计	根据波次ID进行统计并更新，统计关联响应（has_response2=1）的记录
				ResultQuery has2rq= SqlHelp.query(conn, has2sql,acttargetid,acttargetid,cmn_activity_id);
				ResultSet has2rs= has2rq.getResultSet();
				if(has2rs!=null&&has2rs.next()){
					
					String[] uphas2count={has2rs.getString("countmemberid"),cmn_activity_id};
					SqlHelp.operate(conn, uphas2, uphas2count);//保存结果至活动波次执行结果表
					
				}
				has2rq.free();
				
				
				//控制组响应数量     根据波次ID进行统计并更新    按以下条件识别控制组的响应数量    1.	目标会员是控制组:is_controlset =1     2.	目标会员满足推荐响应的条件:has_response=1
				ResultQuery controlrq= SqlHelp.query(conn, controlsql,acttargetid,acttargetid,cmn_activity_id);
				ResultSet controlrs= controlrq.getResultSet();
				if(controlrs!=null&&controlrs.next()){
					
					String[] upcontrol={controlrs.getString("countmemberid"),cmn_activity_id};
					SqlHelp.operate(conn, upcontrolsql, upcontrol);//保存结果至活动波次执行结果表
					
				}
				controlrq.free();
				 
				//更新响应设置状态（结束）
				String[] upactresult={actresultid};
				SqlHelp.operate(conn, upresult, upactresult);
			
				
				r=0;
			}		
		}catch (SQLException e) {
			//异常处理  1.	日志输出，2.	更新响应设置状态（异常）
			
			String[] upactresult={mfield.destValue};
			SqlHelp.operate(conn, ycupid, upactresult);
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
