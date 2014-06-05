package com.wanda.mms.control.response;

import java.math.BigDecimal;
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
import com.wanda.mms.control.response.handle.ActHandler;
import com.wanda.mms.control.response.handle.AlterActHandler;
import com.wanda.mms.control.response.handle.ResActHandler;
import com.wanda.mms.control.stream.util.DateUtil;
import com.wanda.mms.control.stream.vo.MemberPoint;
import com.wanda.mms.data.TCmnActivity;
import com.wanda.mms.data.TCmnActivityMapper;


/**
 *	会员营销统计 标记响应 
 * @author wangshuai
 * @date 2013-09-21	
 */


public class TactresultUpLineHandle implements LineHandle {
	static Logger logger = Logger.getLogger(AllMbrPoint.class.getName());
	
	private final static int RESPONSE_TYPE_MAIN = 1;//推荐响应
	private final static int RESPONSE_TYPE_ALTER = 2;//关联响应
	
	public final static String OCCUPIED_BY_ACT_RESULT = "2";//被活动波次响应计算占用

	
	private Connection conn;
	public TactresultUpLineHandle(Connection conn){
		this.conn=conn;
	}
	public TactresultUpLineHandle(){
		conn=MarketingStatistics.mbr;
	}
	@Override
	public int handle(FieldSet fieldset) {
		if(unBeginProcessing(fieldset)){
			return 0;
		}
		 //
		int r=0;
		//更新活动波次响应客群表中的 STATUS 活动波次执行结果状态  10执行中  20执行结束 30统计中
		int segmentoccupied=0;
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

		logger.info("selsql="+selsql);
		String upresult=" update t_act_result t set status ='30' where  t.act_result_id=?  " ;
		//更新波次目标状态为
		String ycupid="update t_act_result t set status ='40' where  t.act_result_id=?   ";
		
	//	String segmentsql = "select * from T_SEGMENT where  occupied='0' and segment_id=?";
		
	//	String upsegmentsql="update T_SEGMENT t set t.OCCUPIED='2' where t.SEGMENT_ID=? and t.OCCUPIED='0'";
		Field mfield=fieldset.getFieldByName("ACT_RESULT_ID");
		ResultQuery rq= SqlHelp.query(conn, selsql,mfield.destValue,mfield.destValue);
		try { 
				ResultSet rs= rq.getResultSet();
				boolean segmentLocked = true;
				 while(rs!=null&&rs.next()){
					 
						Long actTargetId = rs.getLong("act_target_id");//波次目标ID
					 	String restype = rs.getString("restype");//type=res(推荐响应),alter(关联响应)
					 	Long segmentId= rs.getLong("segment_id");//关联响应客群ID或 推荐响应客群ID
 
					ActHandler handler = getActHandler(restype);
						if(handler !=null){
							boolean temFlag = handler.updateHistoryResponse(segmentId, actTargetId);
							if(!temFlag){//任何一次 更新不到数据， 都视为不需要操作。
										 //所有的数据都能更新到，才更更新 act_result_t
								logger.info(" segmentLocked = false ,segmentId :actTargetId="+segmentId+":"+actTargetId);
								segmentLocked = false;
							}else{
								logger.info(" segmentLocked = true ,segmentId :actTargetId="+segmentId+":"+actTargetId);
							}
						}else{
							 logger.error("handler is null");	
						}
				} 
				 logger.debug("segmentLocked=="+segmentLocked);
				 if(segmentLocked){	
					 String[] upactresult={mfield.destValue};
					 SqlHelp.operate(conn, upresult, upactresult);
					 logger.info("更新目标波次状态， SQL：" + upresult  +"条件："+upactresult);	
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
 		r=0;
 		
		return r;
	}

	private boolean unBeginProcessing(FieldSet fieldset) {
		return false;
	}
	private ActHandler getActHandler(String restype) {
		 if(restype.equals("res")){
			 return new ResActHandler(conn);
		 }else if(restype.equals("alter")){
			 return new AlterActHandler(conn);
		 }else{
			 return null;
		 }
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
