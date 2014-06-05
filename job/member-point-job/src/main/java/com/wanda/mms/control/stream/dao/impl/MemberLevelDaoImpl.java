package com.wanda.mms.control.stream.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.wanda.mms.control.stream.dao.MemberLevelDao;
import com.wanda.mms.control.stream.util.DateUtil;
import com.wanda.mms.control.stream.util.Page;
import com.wanda.mms.control.stream.vo.MemberLevel;
import com.wanda.mms.control.stream.vo.MemberPoint;

public class MemberLevelDaoImpl implements MemberLevelDao {
	static Logger logger = Logger.getLogger(MemberLevelDaoImpl.class.getName());

//	private PreparedStatement pstt=null;//  数据库预先编译声明类型的属性 pst 用于操作数据库
//	private PreparedStatement pst=null;// 
//	private ResultSet rs=null;// 结果集类型的 属性 rs 用于接收返回的结果集
	
	@Override
	public int addMemberLevelByID(Connection conn, MemberLevel ml) {
		// TODO Auto-generated method stub
		
		 
		  PreparedStatement pst=null;// 
		  

		// TODO Auto-generated method stub
		int flag=0;
	
		//序列名不对以后改过来  select S_T_F_VOUCHER.nextVal from dual
		//得到之后在插入 并把这个值回传。
		Date da = new Date();
		String date = DateUtil.getDateStrss(da);//时间可能是 时分秒
	//	String sql = "insert into t_im_module_log (seqid,dataymd,moduleid,starttime,endtime,status,DESCRIPTION)values(S_T_F_VOUCHER.nextVal,?,?,to_date('" + lm.getStartTime() + "','yyyy-mm-dd hh24:mi:ss'),to_date('" + lm.getEndTime() + "','yyyy-mm-dd hh24:mi:ss'),?,?)"; 
		String sql = "insert into T_MEMBER_LEVEL(MEMBER_ID,MEM_LEVEL,EXPIRE_DATE,ORG_LEVEL,SET_TIME,TARGET_LEVEL,LEVEL_POINT_OFFSET,TICKET_OFFSET,CHANGE_LEVEL_NO,LEVEL_ID" +
				"	,ISDELETE,CREATE_BY,CREATE_DATE,UPDATE_BY,UPDATE_DATE,VERSION,MEMBER_LEVEL_HISTORY_ID)" +
				"	values(?,?,to_date('" + ml.getExpire_date() + "','yyyy-mm-dd hh24:mi:ss'),?,sysdate,?,?,?,?,?,?,?,sysdate,?,sysdate,?,?)";
		try {
				
			pst=conn.prepareStatement(sql);
			conn.setAutoCommit(false);
			pst.setLong(1,ml.getMember_id());//会员ID
			pst.setString(2,ml.getMem_level());//当前级别#MEM_LEVEL
			pst.setString(3,ml.getOrg_level());//上一级别#ORG_LEVEL
			pst.setString(4,ml.getTarget_level()); //目标级别#TARGET_LEVEL
			pst.setLong(5, ml.getLevel_point_offset());//升级至目标级别差距(积分)#LEVEL_POINT_OFFSET
			pst.setLong(6,ml.getTicket_offset() );//升级至目标级别差距(影票)#TICKET_OFFSET
			pst.setString(7, ml.getChange_level_no());//级别变更批次号
			pst.setLong(8, ml.getLast_level_history_id());//最后级别历史流水#LAST_LEVEL_HISTORY_ID
			pst.setInt(9, ml.getIsdelete());	//逻辑删除标识
			pst.setString(10,ml.getCreate_By());//创建者
			pst.setString(11,ml.getUpdate_By()); //更新者
			pst.setInt(12,ml.getVersion());//版本号
			pst.setLong(13, ml.getMember_level_history_id());
			flag = pst.executeUpdate();
			
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
		 
		return flag;
	
	}

	@Override
	public MemberLevel fandMemberLevelByID(Connection conn, long memberId) {
		// TODO Auto-generated method stub
		MemberLevel ml = new MemberLevel();
		PreparedStatement pstt=null;//  数据库预先编译声明类型的属性 pst 用于操作数据库
		 
		ResultSet rs=null;// 结果集类型的 属性 rs 用于接收返回的结果集
		try {
			String sqll = "select MEMBER_ID,MEM_LEVEL,EXPIRE_DATE,ORG_LEVEL,SET_TIME,TARGET_LEVEL,LEVEL_POINT_OFFSET,TICKET_OFFSET,CHANGE_LEVEL_NO,LEVEL_ID,ISDELETE,CREATE_BY,CREATE_DATE,UPDATE_BY,UPDATE_DATE,VERSION,MEMBER_LEVEL_HISTORY_ID from T_MEMBER_LEVEL where MEMBER_ID=?";
			
				pstt=conn.prepareStatement(sqll);
				pstt.setLong(1, memberId);
				rs=pstt.executeQuery();
				while (rs.next()) {
					//会员ID#MEMBER_ID
					ml.setMember_id(rs.getLong("MEMBER_ID"));//会员ID
					//当前级别#MEM_LEVEL
					ml.setMem_level(rs.getString("MEM_LEVEL"));
					//当前级别有效期#EXPIRE_DATE
					ml.setExpire_date(DateUtil.getDateStrss(rs.getDate("EXPIRE_DATE")));
					//上一级别#ORG_LEVEL
					ml.setOrg_level(rs.getString("ORG_LEVEL"));
					//级别变更时间#SET_TIME
					if(rs.getDate("SET_TIME")!=null){
					ml.setSet_time(DateUtil.getDateStrss(rs.getDate("SET_TIME")));
					}else{
					  Date 	da = new Date();
					  ml.setSet_time(DateUtil.getDateStrss(da));
					}
					//目标级别#TARGET_LEVEL
					ml.setTarget_level(rs.getString("TARGET_LEVEL"));
					//升级至目标级别差距(积分)#LEVEL_POINT_OFFSET
					ml.setLevel_point_offset(rs.getLong("LEVEL_POINT_OFFSET"));
					//升级至目标级别差距(影票)#TICKET_OFFSET
					ml.setTicket_offset(rs.getLong("TICKET_OFFSET"));
					//级别变更批次号
					ml.setChange_level_no(rs.getString("CHANGE_LEVEL_NO"));
					//最后级别历史流水#LAST_LEVEL_HISTORY_ID
					ml.setLast_level_history_id(rs.getLong("LEVEL_ID"));
					//逻辑删除标识,默认:0 未删除;1删除;其他:非法
					ml.setIsdelete(rs.getInt("ISDELETE"));
					//创建人员工ID(系统自动:"member_sys";管理界面:登录员工ID)
					ml.setCreate_By(rs.getString("CREATE_BY"));
					//创建时间#CREATE_DATE
					ml.setCreate_Date(DateUtil.getDateStrss(rs.getDate("CREATE_DATE")));
					//更新人员工ID(系统自动:"member_sys";管理界面:登录员工ID)
					ml.setUpdate_By(rs.getString("UPDATE_BY"));
					//更新时间#
					ml.setUpdate_Date(DateUtil.getDateStrss(rs.getDate("UPDATE_DATE")));
					//版本号
					ml.setVersion(rs.getInt("VERSION"));
					
					ml.setMember_level_history_id(rs.getLong("MEMBER_LEVEL_HISTORY_ID"));
				}
				 
		
			} catch (SQLException e) {
				// TODO Auto-generated catch block
			 
					logger.error(e);
			 

				e.printStackTrace();
			}finally {
				if(pstt!=null){
					try {
						pstt.close();
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
			
		
		return ml;
	}

	@Override
	public int updateMemberLevelByID(Connection conn, MemberLevel ml) {
		// TODO Auto-generated method stub
		
		PreparedStatement pst=null;// 
  	 
		 int flag=0;
		 Date da = new Date();
		 String date = DateUtil.getDateStrss(da);
		 
		 String sql="UPDATE T_MEMBER_LEVEL SET MEM_LEVEL=?,EXPIRE_DATE=to_date('" + ml.getExpire_date() + "','yyyy-mm-dd hh24:mi:ss'),ORG_LEVEL=?,SET_TIME=to_date('" + ml.getSet_time() + "','yyyy-mm-dd hh24:mi:ss'),TARGET_LEVEL=?,LEVEL_POINT_OFFSET=?,TICKET_OFFSET=?,CHANGE_LEVEL_NO=?,MEMBER_LEVEL_HISTORY_ID=?,ISDELETE=?,UPDATE_BY=?,UPDATE_DATE=sysdate,VERSION=? WHERE MEMBER_ID=? and LEVEL_ID=?";
		 String sql2="UPDATE T_MEMBER_LEVEL SET MEM_LEVEL=?,EXPIRE_DATE=to_date('" + ml.getExpire_date() + "','yyyy-mm-dd hh24:mi:ss'),ORG_LEVEL=?,SET_TIME=to_date('" + ml.getSet_time() + "','yyyy-mm-dd hh24:mi:ss'),TARGET_LEVEL=?,LEVEL_POINT_OFFSET=?,TICKET_OFFSET=?,CHANGE_LEVEL_NO=?,ISDELETE=?,UPDATE_BY=?,UPDATE_DATE=sysdate,VERSION=? WHERE MEMBER_ID=? and LEVEL_ID=?";
			
		 //	SENDDBConnection db=SENDDBConnection.getInstance();	
		try {
			if(ml.getMember_level_history_id()==0){
				pst=conn.prepareStatement(sql2);
				 
 				pst.setString(1, ml.getMem_level());//会员身份目前生效的级别
			 
				pst.setString(2,ml.getOrg_level());//生效之前的级别
				pst.setString(3,ml.getTarget_level());//下一目标级别
				pst.setLong(4,ml.getLevel_point_offset());//还差xx积分升级至目标级别：会员升级计算后更新
				pst.setLong(5,ml.getTicket_offset());//还差xx影票升级至下一级会员升级计算后更新
				pst.setString(6,ml.getChange_level_no());
				//System.out.println(ml.getLast_level_history_id());
				pst.setInt(7, ml.getIsdelete());//逻辑删除标识,默认:0 未删除;1删除;其他:非法
				pst.setString(8, ml.getUpdate_By());//更新人员工ID(系统自动:"member_sys";管理界面:登录员工ID)
				pst.setInt(9, ml.getVersion());
				pst.setLong(10, ml.getMember_id());//会员ID
				pst.setLong(11, ml.getLast_level_history_id());//ID
				System.out.println("会员等级表："+sql2);
				System.out.println("会员ID="+ml.getMember_id()+"会员等级表主键id="+ml.getLast_level_history_id());
				System.out.println("会员历史ID="+ml.getMember_level_history_id());
				
				flag=pst.executeUpdate();
		
			}
			else{
				
				pst=conn.prepareStatement(sql);
				 
 				pst.setString(1, ml.getMem_level());//会员身份目前生效的级别
			 
				pst.setString(2,ml.getOrg_level());//生效之前的级别
				pst.setString(3,ml.getTarget_level());//下一目标级别
				pst.setLong(4,ml.getLevel_point_offset());//还差xx积分升级至目标级别：会员升级计算后更新
				pst.setLong(5,ml.getTicket_offset());//还差xx影票升级至下一级会员升级计算后更新
				pst.setString(6,ml.getChange_level_no());
				//System.out.println(ml.getLast_level_history_id());
				pst.setLong(7, ml.getMember_level_history_id()); //历史ID
				pst.setInt(8, ml.getIsdelete());//逻辑删除标识,默认:0 未删除;1删除;其他:非法
				pst.setString(9, ml.getUpdate_By());//更新人员工ID(系统自动:"member_sys";管理界面:登录员工ID)
				pst.setInt(10, ml.getVersion());
				pst.setLong(11, ml.getMember_id());//会员ID
				pst.setLong(12, ml.getLast_level_history_id());//ID
			//	System.out.println("会员等级表："+sql);
			//	System.out.println("会员ID="+ml.getMember_id()+"会员等级表主键id="+ml.getLast_level_history_id());
			//	System.out.println("会员历史ID="+ml.getMember_level_history_id());
				
				flag=pst.executeUpdate();
			}
			//conn=db.getConnection();
				
				 
			 
				 
				 
		
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
		 
		 
		
		
		return flag;
		
	}

	@Override
	public List<MemberLevel> fandMemberLevelPage(Connection conn, Page page) {
		// TODO Auto-generated method stub
		 List<MemberLevel> mllist = new ArrayList<MemberLevel>();
		 PreparedStatement pst=null;// 
		 ResultSet rs=null;// 结果集类型的 属性 rs 用于接收返回的结果集
		 Long seq = null  ;
			String sql = "SELECT * FROM (  "
					+ " SELECT A.*, ROWNUM RN  "
					+ " FROM (select  p.MEMBER_ID,MEM_LEVEL,EXPIRE_DATE,ORG_LEVEL,SET_TIME,TARGET_LEVEL,LEVEL_POINT_OFFSET,TICKET_OFFSET,CHANGE_LEVEL_NO,LEVEL_ID,p.ISDELETE,p.CREATE_BY,p.CREATE_DATE,p.UPDATE_BY,p.UPDATE_DATE,p.VERSION  from T_MEMBER_LEVEL p ,T_MEMBER m  where p.member_id=m.member_id and m.status='1' and m.isdelete='0'  "
					+"	  "
					+ "  　) A  "
					+ " WHERE ROWNUM <= ?) "
					+ " WHERE RN >= ? "; 
			//SENDDBConnection db=SENDDBConnection.getInstance();	
			//conn=db.getConnection();
		//	System.out.println(" ROWNUM ="+page.getPage() +"RN "+page.getPageSize()+"升降级= "+sql);
			try {
				pst=conn.prepareStatement(sql);
				//System.out.println("ROWNUM ="+page.getPageSize());
				//System.out.println("RN  "+page.getPage());
				pst.setLong(1,page.getPageSize());
				pst.setLong(2,page.getPage());
				rs=pst.executeQuery();
				while (rs.next()) {
					MemberLevel ml= new MemberLevel();

					//会员ID#MEMBER_ID
					ml.setMember_id(rs.getLong("MEMBER_ID"));//会员ID
					//当前级别#MEM_LEVEL
					ml.setMem_level(rs.getString("MEM_LEVEL"));
					//当前级别有效期#EXPIRE_DATE
					ml.setExpire_date(DateUtil.getDateStrss(rs.getDate("EXPIRE_DATE")));
					//上一级别#ORG_LEVEL
					ml.setOrg_level(rs.getString("ORG_LEVEL"));
					//级别变更时间#SET_TIME
					System.out.println(rs.getLong("MEMBER_ID"));
					if(rs.getTimestamp("SET_TIME")!=null){
					ml.setSet_time(DateUtil.getDateStrss(rs.getTimestamp("SET_TIME")));
					}else{
						  Date 	da = new Date();
						  ml.setSet_time(DateUtil.getDateStrss(da));
						}
					//目标级别#TARGET_LEVEL
					ml.setTarget_level(rs.getString("TARGET_LEVEL"));
					//升级至目标级别差距(积分)#LEVEL_POINT_OFFSET
					ml.setLevel_point_offset(rs.getLong("LEVEL_POINT_OFFSET"));
					//升级至目标级别差距(影票)#TICKET_OFFSET
					ml.setTicket_offset(rs.getLong("TICKET_OFFSET"));
					//级别变更批次号
					ml.setChange_level_no(rs.getString("CHANGE_LEVEL_NO"));
					//最后级别历史流水#LAST_LEVEL_HISTORY_ID
					ml.setLast_level_history_id(rs.getLong("LEVEL_ID"));
					//逻辑删除标识,默认:0 未删除;1删除;其他:非法
					ml.setIsdelete(rs.getInt("ISDELETE"));
					//创建人员工ID(系统自动:"member_sys";管理界面:登录员工ID)
					ml.setCreate_By(rs.getString("CREATE_BY"));
					//创建时间#CREATE_DATE
					ml.setCreate_Date(DateUtil.getDateStrss(rs.getDate("CREATE_DATE")));
					//更新人员工ID(系统自动:"member_sys";管理界面:登录员工ID)
					ml.setUpdate_By(rs.getString("UPDATE_BY"));
					//更新时间#
					ml.setUpdate_Date(DateUtil.getDateStrss(rs.getDate("UPDATE_DATE")));
					//版本号
					ml.setVersion(rs.getInt("VERSION"));
					
					mllist.add(ml);
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
			 
		
		return mllist;
		
	}

	@Override
	public long fandMemberLevelTotalNum(Connection conn) {
		// TODO Auto-generated method stub
		
		Long seq = null  ;
		String sql = "SELECT COUNT(*) totalnum  from T_MEMBER_LEVEL p  ,T_MEMBER m  where p.member_id=m.member_id and m.status='1' and m.isdelete='0'  ";
		//SENDDBConnection db=SENDDBConnection.getInstance();	
		//conn=db.getConnection();  
		 PreparedStatement pst=null;// 
		 ResultSet rs=null;// 结果集类型的 属性 rs 用于接收返回的结果集
		try {
			pst=conn.prepareStatement(sql);
			//  pst.setString(1,date);
			rs=pst.executeQuery();
			while (rs.next()) {
				 seq=(rs.getLong("totalnum"));
			}
			 
	
		} catch (SQLException e) {
		 
				logger.error(e);
			 

			// TODO Auto-generated catch block
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
	public long fandMemberLevelBySEQ(Connection conn) {
		// TODO Auto-generated method stub
		

		Long seq = null  ;
		String sql = "select S_T_MEMBER_LEVEL.nextVal seq from dual";
		//SENDDBConnection db=SENDDBConnection.getInstance();	
		//conn=db.getConnection();
		 PreparedStatement pst=null;// 
		 ResultSet rs=null;// 结果集类型的 属性 rs 用于接收返回的结果集
		try {
			pst=conn.prepareStatement(sql);
			rs=pst.executeQuery();
			while (rs.next()) {
				 seq=(rs.getLong("seq"));
			}
			 
		} catch (SQLException e) {
		 
				logger.error(e);
			 

			// TODO Auto-generated catch block
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
		return seq;
		 
	}

}
