package com.wanda.mms.control.stream;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import com.solar.etl.config.mapping.Field;
import com.solar.etl.config.mapping.FieldSet;
import com.solar.etl.db.ResultQuery;
import com.solar.etl.db.SqlHelp;
import com.solar.etl.spi.LineHandle;
import com.wanda.mms.control.stream.util.DateUtil;
import com.wanda.mms.control.stream.vo.MemberPoint;

/**
 *	会员降级
 * @author wangshuai
 * @date 2013-08-20	
 */


public class DownLevelLineHandle implements LineHandle{
	private Connection conn;
	public DownLevelLineHandle(Connection conn){
		this.conn=conn;
	}
	public DownLevelLineHandle(){
		conn=Basic.mbr;
	}
	@Override
	public int handle(FieldSet fieldset) {
		 // 1.先查出有多少会员
		//2.根据每个会员的ID去积分历史里去查积分。
		//3.根据当前等级与会员积分来判断是否降级。
		//3.1 如果满足那么插入降级信息进入级别历史表。
		//3.2更新会员级别表。
		//
		//1. 之后积分清零
		//
		int r=0;
		Dimdef dd = new Dimdef();
 
		String sqllevel = "select MEMBER_ID,MEM_LEVEL,EXPIRE_DATE,ORG_LEVEL,SET_TIME,TARGET_LEVEL,LEVEL_POINT_OFFSET,TICKET_OFFSET,LEVEL_ID,ISDELETE,CREATE_BY,CREATE_DATE,UPDATE_BY,UPDATE_DATE,VERSION,MEMBER_LEVEL_HISTORY_ID from T_MEMBER_LEVEL t where MEMBER_ID=?";
		
		//查看会员积分历史
		String sqltemp =" select  MEMBER_ID,sum(LEVEL_POINT) LEVEL_POINT, sum(TICKET_COUNT) TICKET_COUNT "
		+"  from T_POINT_HISTORY t    where set_time >= to_date(?, 'yyyy-mm-dd')  and   set_time  < to_date(?, 'yyyy-mm-dd') + 1  and MEMBER_ID = ? and IS_HISTORY<>'1' group by MEMBER_ID	"  ;
		//得到会员级别SEQ
 		String sqllevelseq="select S_T_LEVEL_HISTORY.nextVal SEQ from dual";
 		//插入会员级别历史表
 		String sql = "insert into T_LEVEL_HISTORY(LEVEL_HISTORY_ID,MEM_LEVEL,EXPIRE_DATE,ORG_MEM_LEVEL,ORG_EXPIRE_DATE,SET_TIME,RESON_TYPE,REASON,CHG_TYPE,MEMBER_ID," +
		"	LEVEL_POINT,TICKET_COUNT,ISDELETE,CREATE_BY,CREATE_DATE,UPDATE_BY,UPDATE_DATE,VERSION)" +
		"	values(?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?,sysdate " +
				" ,?,?,?,?,?,?,?,?,sysdate,?,sysdate,?)";
 		
		 String upmemlevesql="UPDATE T_MEMBER_LEVEL SET MEM_LEVEL=?,EXPIRE_DATE=to_date(?,'yyyy-mm-dd hh24:mi:ss'),ORG_LEVEL=?,SET_TIME=sysdate,TARGET_LEVEL=?,LEVEL_POINT_OFFSET=?,TICKET_OFFSET=?,UPDATE_DATE=sysdate,MEMBER_LEVEL_HISTORY_ID=? WHERE MEMBER_ID=?";
	
		 
		 //更新会员级别表
 		
		String upmempoint ="update T_MEMBER_POINT t set t.is_level='1' where t.member_id=?";	
			
		Field mfield=fieldset.getFieldByName("MEMBER_ID");
		Field updateField= fieldset.getFieldByName("UPDATE_DATE");
		String year = updateField.destValue.substring(0, 4);
		String startdate = year+"-01-01";
		String enddate = year+"-12-31";
		Field apfield=fieldset.getFieldByName("ACTIVITY_POINT");//1,2

	 	ResultQuery rq= SqlHelp.query(conn, sqllevel,mfield.destValue);
		ResultSet rs= rq.getResultSet();
		
		try {
 			if(rs!=null&&rs.next()){
// 	
//				mb.setExg_Point_Balance(Long.valueOf(apfield.destValue));
//				mb.setActivity_Point(Long.valueOf(apfield.destValue)) ;
// 
// 
 				long seq=0;
 				long mid=0;
 				long level_point=0;
 				long ticket_count=0;
				ResultQuery historyrq= SqlHelp.query(conn, sqltemp,startdate,enddate,mfield.destValue);
				ResultSet historyrs= historyrq.getResultSet();
				if(historyrs!=null&&historyrs.next()){
					mid=historyrs.getLong("MEMBER_ID");
					level_point=historyrs.getLong("LEVEL_POINT");
					ticket_count=historyrs.getLong("TICKET_COUNT");
					
				}
				historyrq.free();
				
				ResultQuery seqrq= SqlHelp.query(conn, sqllevelseq);
				ResultSet seqrs= seqrq.getResultSet();
				
				if(seqrs!=null&&seqrs.next()){
					seq=seqrs.getLong("SEQ");
				}
				seqrq.free();
				int level =rs.getInt("MEM_LEVEL");
//				
//				while(temrs!=null&&temrs.next()){ }
				int ti = (Integer.valueOf(year)+1);
				String time =String.valueOf(ti)+"-12-31 23:59:59";
			String expdate = DateUtil.getDateStrss(rs.getTimestamp("EXPIRE_DATE"));
				//rs.getExpire_date()变更前级别有效期
				switch (level) {
				case 2: if(ticket_count<12&&level_point<500){
				
				  //String[] lh = {"SEQ","变更后级别","变更后级别有效期","变更前级别","变更前级别有效期",dd.RESON_TYPE_QT,"member_sys","up","MEMBER_ID","有效积分数","有效票数","0","member_sys","member_sys","1"};
					
					String[] lh = {String.valueOf(seq),String.valueOf(level-1),time,String.valueOf(level),expdate,dd.RESON_TYPE_QT,"member_sys","DOWN",mfield.destValue,String.valueOf(level_point),String.valueOf(ticket_count),"0","member_sys","member_sys","1"};
					//添加等级历史
					SqlHelp.operate(conn, sql, lh);	
					//OK
					String 	mllLevel_point_offset ="";
					String mllTicket_offset="";
					if((500-level_point)>0){
					  mllLevel_point_offset=String.valueOf(500-level_point);
					}else{
						mllLevel_point_offset="0";
					}
					if((12-ticket_count)>0){
						mllTicket_offset=String.valueOf(12-ticket_count);
					}else{
						mllTicket_offset="0";
					}
				  //String[] mll={生效的级别,生效级别的有效期,生效之前的级别,下一目标级别,还差xx积分升级至目标级别,还差xx影票升级至下一级,String.valueOf(seq),MEMBER_ID};
					//更新等级表
					String[] mll={String.valueOf(level-1),time,String.valueOf(level),String.valueOf(level),mllLevel_point_offset,mllTicket_offset,String.valueOf(seq),mfield.destValue};
					 
					SqlHelp.operate(conn, upmemlevesql, mll);	
					

					String [] mempoint={mfield.destValue};
					SqlHelp.operate(conn, upmempoint, mempoint);	
					
				}else{
				//	long seq = lhdao.seqLevelHistroy(conn);
						
					String [] mempoint={mfield.destValue};
					SqlHelp.operate(conn, upmempoint, mempoint);
					
				}
					
					break;
				case 3:if(ticket_count<24&&level_point<1000){
					

					
					  //String[] lh = {"SEQ","变更后级别","变更后级别有效期","变更前级别","变更前级别有效期",dd.RESON_TYPE_QT,"member_sys","up","MEMBER_ID","有效积分数","有效票数","0","member_sys","member_sys","1"};
						
					String[] lh = {String.valueOf(seq),String.valueOf(level-1),time,String.valueOf(level),expdate,dd.RESON_TYPE_QT,"member_sys","DOWN",mfield.destValue,String.valueOf(level_point),String.valueOf(ticket_count),"0","member_sys","member_sys","1"};
						//添加等级历史
						SqlHelp.operate(conn, sql, lh);	
						
						String 	mllLevel_point_offset ="";
						String mllTicket_offset="";
						if((1000-level_point)>0){
						  mllLevel_point_offset=String.valueOf(1000-level_point);
						}else{
							mllLevel_point_offset="0";
						}
						if((24-ticket_count)>0){
							mllTicket_offset=String.valueOf(24-ticket_count);
						}else{
							mllTicket_offset="0";
						}
					  //String[] mll={生效的级别,生效级别的有效期,生效之前的级别,下一目标级别,还差xx积分升级至目标级别,还差xx影票升级至下一级,String.valueOf(seq),MEMBER_ID};
						//更新等级表
						String[] mll={String.valueOf(level-1),time,String.valueOf(level),String.valueOf(level),mllLevel_point_offset,mllTicket_offset,String.valueOf(seq),mfield.destValue};
						 
						SqlHelp.operate(conn, upmemlevesql, mll);	
						

						String [] mempoint={mfield.destValue};
						SqlHelp.operate(conn, upmempoint, mempoint);	

			
			}else{

					String [] mempoint={mfield.destValue};
					SqlHelp.operate(conn, upmempoint, mempoint);
					
				}
//					   
					break;
				case 4:if(ticket_count<48&&level_point<3000){
					 //String[] lh = {"SEQ","变更后级别","变更后级别有效期","变更前级别","变更前级别有效期",dd.RESON_TYPE_QT,"member_sys","up","MEMBER_ID","有效积分数","有效票数","0","member_sys","member_sys","1"};
					
					String[] lh = {String.valueOf(seq),String.valueOf(level-1),time,String.valueOf(level),expdate,dd.RESON_TYPE_QT,"member_sys","DOWN",mfield.destValue,String.valueOf(level_point),String.valueOf(ticket_count),"0","member_sys","member_sys","1"};
					//添加等级历史
					SqlHelp.operate(conn, sql, lh);	
					
					String 	mllLevel_point_offset ="";
					String mllTicket_offset="";
					if((3000-level_point)>0){
						mllLevel_point_offset="0";
					}else{
						mllLevel_point_offset="0";
					}
					if((48-ticket_count)>0){
						mllTicket_offset="0";
					}else{
						mllTicket_offset="0";
					}
				  //String[] mll={生效的级别,生效级别的有效期,生效之前的级别,下一目标级别,还差xx积分升级至目标级别,还差xx影票升级至下一级,String.valueOf(seq),MEMBER_ID};
					//更新等级表
					String[] mll={String.valueOf(level-1),time,String.valueOf(level),String.valueOf(level),mllLevel_point_offset,mllTicket_offset,String.valueOf(seq),mfield.destValue};
					 
					SqlHelp.operate(conn, upmemlevesql, mll);	
					

					String [] mempoint={mfield.destValue};
					SqlHelp.operate(conn, upmempoint, mempoint);	


				}else{	 
					String [] mempoint={mfield.destValue};
					SqlHelp.operate(conn, upmempoint, mempoint);
				
				}
					
					break;

				default:
					
					
					break;
				}
 				System.out.println(mfield.destValue);
 				r=0;
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
