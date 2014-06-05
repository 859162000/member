package com.wanda.mms.control.stream.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.wanda.mms.control.stream.dao.MemberPointDao;
import com.wanda.mms.control.stream.util.DateUtil;
import com.wanda.mms.control.stream.util.Page;
import com.wanda.mms.control.stream.vo.MemberPoint;

public class MemberPointDaoImpl implements MemberPointDao {
	static Logger logger = Logger.getLogger(MemberPointDaoImpl.class.getName());

//	private PreparedStatement pstt=null;//  数据库预先编译声明类型的属性 pst 用于操作数据库
//	private PreparedStatement pst=null;// 	
//	private ResultSet rs=null;// 结果集类型的 属性 rs 用于接收返回的结果集
	

	@Override
	public MemberPoint fandMemberPointByID(Connection conn, long memberId) {
		// TODO Auto-generated method stub
		MemberPoint mp = new MemberPoint();
		PreparedStatement pstt=null;
		ResultSet rs=null;
		try {    
		String sqll = "select MEMBER_POINT_ID,EXG_POINT_BALANCE,EXG_EXPIRE_POINT_BALANCE,POINT_HISTORY_ID,LEVEL_POINT_TOTAL,ACTIVITY_POINT,ISDELETE,CREATE_BY,CREATE_DATE,UPDATE_BY,UPDATE_DATE,VERSION,LAST_EXCHANGE_DATE,MEMBER_ID from T_MEMBER_POINT where MEMBER_ID=?";
		
			pstt=conn.prepareStatement(sqll);
			pstt.setLong(1, memberId);
			rs=pstt.executeQuery();
			while (rs.next()) {
				mp.setMember_point_id(rs.getLong("MEMBER_POINT_ID"));
				mp.setMemberId(rs.getLong("MEMBER_ID"));//会员ID
//可兑换积分余额#会员目前为止所有剩余的可兑换积分，t_point_history中产生积分历史数据时更新余额。在t_point_history中插入记录时，可兑换积分<>0时，同步更新此字段=此字段+可兑换积分。											
				mp.setExg_Point_Balance(rs.getLong("EXG_POINT_BALANCE"));
 /**
 * 会员当年即将过期积分：会员在今年内（积分清零日前,当年12月31日）的未使用可兑换积分。
 *	场景1：
 *在t_point_history中产生的积分变化数据的有效期在当年12月31日前，更新此字段
 *在t_point_history中插入的记录可兑换积分<>0时，且可兑换积分过期日<=当年12月31日时，更新此字段=此字段+发生可兑换积分。
 *场景2：
 *在t_point_history中插入清零记录时候，更新此字段为下一年度积分余额。
 *在t_point_history中插入的记录为积分清零记录时，更新此字段=下一年度积分余额。
 */
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
				
			}
 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
		 
				logger.error(e);
			 

			e.printStackTrace();
		} finally {
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
		
		
		
		return mp;
	}
	
	@Override
	public int upMemberPointBylevel(Connection conn,long MemberId) {
		// TODO Auto-generated method stub
		//memp.setLevel_Point_Total(pointhistroy.getLevel_Point());
		//memp.setActivity_Point(pointhistroy.getActivity_Point());
		//memp.setExg_Point_Balance(pointhistroy.getExchange_Point()); 
		int flag=0;
		PreparedStatement pst=null;// 	
		 Date da = new Date();
		 String date = DateUtil.getDateStrss(da);
	//	String sql="UPDATE T_MEMBER_POINT SET EXG_POINT_BALANCE=EXG_POINT_BALANCE+"+mb.getExg_Point_Balance()+",EXG_EXPIRE_POINT_BALANCE=EXG_EXPIRE_POINT_BALANCE+"+mb.getExg_Expire_Point_Balance()+",POINT_HISTORY_ID=?,LEVEL_POINT_TOTAL=LEVEL_POINT_TOTAL+"+mb.getLevel_Point_Total()+",ACTIVITY_POINT=ACTIVITY_POINT+"+mb.getActivity_Point()+",LAST_EXCHANGE_DATE=?,ISDELETE=?,UPDATE_BY=?,UPDATE_DATE=sysdate,VERSION=? WHERE MEMBER_POINT_ID=?";
		String sql="UPDATE T_MEMBER_POINT SET UPDATE_DATE=sysdate,IS_LEVEL='1' WHERE MEMBER_ID=?";
		//// decode(sign(EXG_POINT_BALANCE+"+mb.getExg_Point_Balance()+"),-1,0,EXG_POINT_BALANCE+"+mb.getExg_Point_Balance()+") 
		 //
		//	SENDDBConnection db=SENDDBConnection.getInstance();	
		try {
			//conn=db.getConnection();
			pst=conn.prepareStatement(sql);
			pst.setLong(1, MemberId);
		    flag=pst.executeUpdate();
		
		} catch (SQLException e) {
		 
				logger.error(e);
		 

			e.printStackTrace();
		}   finally {
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
	public int updateMemberPointByID(Connection conn, MemberPoint mb) {
		// TODO Auto-generated method stub
		//memp.setLevel_Point_Total(pointhistroy.getLevel_Point());
		//memp.setActivity_Point(pointhistroy.getActivity_Point());
		//memp.setExg_Point_Balance(pointhistroy.getExchange_Point()); 
		int flag=0;
		PreparedStatement pst=null;// 	
		 Date da = new Date();
		 String date = DateUtil.getDateStrss(da);
	//	String sql="UPDATE T_MEMBER_POINT SET EXG_POINT_BALANCE=EXG_POINT_BALANCE+"+mb.getExg_Point_Balance()+",EXG_EXPIRE_POINT_BALANCE=EXG_EXPIRE_POINT_BALANCE+"+mb.getExg_Expire_Point_Balance()+",POINT_HISTORY_ID=?,LEVEL_POINT_TOTAL=LEVEL_POINT_TOTAL+"+mb.getLevel_Point_Total()+",ACTIVITY_POINT=ACTIVITY_POINT+"+mb.getActivity_Point()+",LAST_EXCHANGE_DATE=?,ISDELETE=?,UPDATE_BY=?,UPDATE_DATE=sysdate,VERSION=? WHERE MEMBER_POINT_ID=?";
		String sql="UPDATE T_MEMBER_POINT SET EXG_POINT_BALANCE=decode(sign(EXG_POINT_BALANCE+"+mb.getExg_Point_Balance()+"),-1,0,EXG_POINT_BALANCE+"+mb.getExg_Point_Balance()+"),EXG_EXPIRE_POINT_BALANCE=EXG_EXPIRE_POINT_BALANCE+"+mb.getExg_Expire_Point_Balance()+",LEVEL_POINT_TOTAL=LEVEL_POINT_TOTAL+"+mb.getLevel_Point_Total()+",ACTIVITY_POINT=ACTIVITY_POINT+"+mb.getActivity_Point()+",LAST_EXCHANGE_DATE=?,ISDELETE=?,UPDATE_BY=?,UPDATE_DATE=sysdate,VERSION=?,IS_LEVEL='0' WHERE MEMBER_ID=?";
		//// decode(sign(EXG_POINT_BALANCE+"+mb.getExg_Point_Balance()+"),-1,0,EXG_POINT_BALANCE+"+mb.getExg_Point_Balance()+") 
		 //
		//	SENDDBConnection db=SENDDBConnection.getInstance();	
		try {
			//conn=db.getConnection();
			pst=conn.prepareStatement(sql);
			// System.out.println(sql);
//会员目前为止所有剩余的可兑换积分，t_point_history中产生积分历史数据时更新余额。	在t_point_history中插入记录时，可兑换积分<>0时，同步更新此字段=此字段+可兑换积分。
				//pst.setLong(1, mb.getExg_Point_Balance());
			 
				//pst.setLong(1,mb.getExg_Expire_Point_Balance());
			//	pst.setLong(1,mb.getPoint_History_Id());
				//pst.setLong(4,mb.getLevel_Point_Total());
				//pst.setLong(5,mb.getActivity_Point());
				pst.setString(1,mb.getLast_Exchange_Date());
				pst.setInt(2, mb.getIsdelete());
				pst.setString(3, mb.getUpdate_By());
				pst.setInt(4, mb.getVersion());
				pst.setLong(5, mb.getMemberId());
				
				flag=pst.executeUpdate();
				 
				 
	 
				
		
		} catch (SQLException e) {
		 
				logger.error(e);
		 

			e.printStackTrace();
		}   finally {
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
	public int addMemberPointByID(Connection conn, MemberPoint mb) {
		// TODO Auto-generated method stub
		int flag=0;
		PreparedStatement pst=null;// 	
		//序列名不对以后改过来  select S_T_F_VOUCHER.nextVal from dual
		//得到之后在插入 并把这个值回传。
		Date da = new Date();
		String date = DateUtil.getDateStrss(da);//时间可能是 时分秒
	//	String sql = "insert into t_im_module_log (seqid,dataymd,moduleid,starttime,endtime,status,DESCRIPTION)values(S_T_F_VOUCHER.nextVal,?,?,to_date('" + lm.getStartTime() + "','yyyy-mm-dd hh24:mi:ss'),to_date('" + lm.getEndTime() + "','yyyy-mm-dd hh24:mi:ss'),?,?)"; 
		String sql = "insert into T_MEMBER_POINT(MEMBER_POINT_ID,EXG_POINT_BALANCE,EXG_EXPIRE_POINT_BALANCE,POINT_HISTORY_ID,LEVEL_POINT_TOTAL,ACTIVITY_POINT," +
				"	ISDELETE,CREATE_BY,CREATE_DATE,UPDATE_BY,UPDATE_DATE,VERSION,LAST_EXCHANGE_DATE,MEMBER_ID)" +
				"	values(?,?,?,?,?,?,?,?,to_date('" + mb.getCreate_Date() + "','yyyy-mm-dd hh24:mi:ss'),?,to_date('" + mb.getUpdate_Date()+ "','yyyy-mm-dd hh24:mi:ss'),?,to_date('" + mb.getLast_Exchange_Date()+ "','yyyy-mm-dd hh24:mi:ss'),?)";
		try {
				
			pst=conn.prepareStatement(sql);
			//System.out.println(sql);
			pst.setLong(1,mb.getMember_point_id());//会员积分ID
			pst.setLong(2,mb.getExg_Point_Balance());//可兑换积分余额
			pst.setLong(3,mb.getExg_Expire_Point_Balance());//当年即将过期可兑换积分#EXG_EXPIRE_POINT_BALANCE
			pst.setLong(4,mb.getPoint_History_Id()); //最后发生积分历史ID#POINT_HISTORY_ID
			pst.setLong(5, mb.getLevel_Point_Total());//总累计定积分#LEVEL_POINT_TOTAL
			pst.setLong(6,mb.getActivity_Point() );//总累计非定级积分#ACTIVITY_POINT
			pst.setInt(7, mb.getIsdelete());	//逻辑删除标识
			pst.setString(8,mb.getCreate_By());//创建者
			pst.setString(9,mb.getUpdate_By()); //更新者
			pst.setInt(10,mb.getVersion());//版本号
		//	pst.setString(11, mb.getLast_Exchange_Date());//最后兑换时间#LAST_EXCHANGE_DATE
			pst.setLong(11, mb.getMemberId());//会员ID
			flag = pst.executeUpdate(); 
		//	System.out.println("添加日志信息  数据日期 YYYYMMDD"+lm.getDataYMD()+"模块ID . 1-数据采集 2-指标计算 3-发送任务生成 4-发送器"+lm.getDescription()+"开始时间"+lm.getStartTime()+"结束时间"+lm.getEndTime()+"执行结果 0-成功，其他-失败"+lm.getStatus()+"结果描述"+lm.getDescription());
 
	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
		 
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
	
	public long fandMemberPointTotalNum(Connection conn) {
		// TODO Auto-generated method stub
		
		Long seq = null  ;
		String sql = "SELECT COUNT(*) totalnum  from T_MEMBER_POINT  ";
		//SENDDBConnection db=SENDDBConnection.getInstance();	
		//conn=db.getConnection();  
		PreparedStatement pst=null;// 
		ResultSet rs=null;
		try {
			pst=conn.prepareStatement(sql);
			//  pst.setString(1,date);
			rs=pst.executeQuery();
			while (rs.next()) {
				 seq=(rs.getLong("totalnum"));
			}
 
	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			 
				logger.error(e);
			 

			e.printStackTrace();
		} 	finally {
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
	
	public List<MemberPoint> fandMemberPointPage(Connection conn,Page page) {
		// TODO Auto-generated method stub
		 List<MemberPoint> list = new ArrayList<MemberPoint>();
	     PreparedStatement pst=null;// 
		 ResultSet rs=null;
		 Long seq = null  ;
			String sql = "SELECT * FROM (  "
					+ " SELECT A.*, ROWNUM RN  "
					+ " FROM (select  MEMBER_ID  from T_MEMBER_POINT  "
					+"	  "
					+ "  　) A  "
					+ " WHERE ROWNUM <= ?) "
					+ " WHERE RN >= ? "; 
			//SENDDBConnection db=SENDDBConnection.getInstance();	
			//conn=db.getConnection();
			logger.info(" ROWNUM ="+page.getPage() +"RN "+page.getPageSize()+"升级= "+sql);
			//System.out.println(" ROWNUM ="+page.getPage() +"RN "+page.getPageSize()+"升级= "+sql);
			try {
				pst=conn.prepareStatement(sql);

				pst.setLong(1,page.getPageSize());
				pst.setLong(2,page.getPage());
				rs=pst.executeQuery();
				while (rs.next()) {
					MemberPoint mp = new MemberPoint();
					mp.setExg_Expire_Point_Balance(rs.getLong("IntegralReset"));
					mp.setMemberId(rs.getLong("MEMBER_ID"));
					
					list.add(mp);
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
		
		return list;
		
	}


	@Override
	public List<MemberPoint> fandMemberPointDayPage(Connection conn, Page page,
			String date) {
		// TODO Auto-generated method stub
		 List<MemberPoint> list = new ArrayList<MemberPoint>();
	     PreparedStatement pst=null;// 
		 ResultSet rs=null;
		 Long seq = null  ;
			String sql = "SELECT * FROM (  "
					+ " SELECT A.*, ROWNUM RN  "
					+ " FROM (select  p.MEMBER_ID  from T_MEMBER_POINT p,T_MEMBER m where p.member_id=m.member_id and m.status='1' and m.isdelete='0' and  to_char(p.update_date, 'yyyy-mm-dd') between ? and ?  "
					+"	  "
					+ "  　) A  "
					+ " WHERE ROWNUM <= ?) "
					+ " WHERE RN >= ? "; 
			//SENDDBConnection db=SENDDBConnection.getInstance();	
			//conn=db.getConnection();
			logger.info(" ROWNUM ="+page.getPage() +"RN "+page.getPageSize()+"升级= "+sql);
			//System.out.println(" ROWNUM ="+page.getPage() +"RN "+page.getPageSize()+"升级= "+sql);
			try {
				pst=conn.prepareStatement(sql);
				  pst.setString(1,date);
				  pst.setString(2,date);
				pst.setLong(3,page.getPageSize());
				pst.setLong(4,page.getPage());
				rs=pst.executeQuery();
				while (rs.next()) {
					MemberPoint mp = new MemberPoint();
				//	mp.setExg_Expire_Point_Balance(rs.getLong("IntegralReset"));
					mp.setMemberId(rs.getLong("MEMBER_ID"));
					
					list.add(mp);
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
		
		return list;
		
	}
	
	@Override
	public List<MemberPoint> fandMemberPointDayPage(Connection conn, Page page) {
		// TODO Auto-generated method stub
		 List<MemberPoint> list = new ArrayList<MemberPoint>();
	     PreparedStatement pst=null;// 
		 ResultSet rs=null;
		 Long seq = null  ;
			String sql = "SELECT * FROM (  "
					+ " SELECT A.*, ROWNUM RN  "
					+ " FROM (select  p.MEMBER_ID  from T_MEMBER_POINT p,T_MEMBER m where p.member_id=m.member_id and m.status='1' and m.isdelete='0' and is_level<>'1'  "
					+"	  "
					+ "  　) A  "
					+ " WHERE ROWNUM <= ?) "
					+ " WHERE RN >= ? "; 
			//SENDDBConnection db=SENDDBConnection.getInstance();	
			//conn=db.getConnection();
			logger.info(" ROWNUM ="+page.getPage() +"RN "+page.getPageSize()+"升级= "+sql);
			//System.out.println(" ROWNUM ="+page.getPage() +"RN "+page.getPageSize()+"升级= "+sql);
			try {
				pst=conn.prepareStatement(sql);
				pst.setLong(1,page.getPageSize());
				pst.setLong(2,page.getPage());
				rs=pst.executeQuery();
				while (rs.next()) {
					MemberPoint mp = new MemberPoint();
				//	mp.setExg_Expire_Point_Balance(rs.getLong("IntegralReset"));
					mp.setMemberId(rs.getLong("MEMBER_ID"));
					
					list.add(mp);
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
		
		return list;
		
	}


	@Override
	public long fandMemberPointTotalNumDay(Connection conn, String date) {
		// TODO Auto-generated method stub
		
		Long seq = null  ;
		String sql = "SELECT COUNT(*) totalnum from T_MEMBER_POINT p ,T_MEMBER m  where p.member_id=m.member_id and m.status='1' and m.isdelete='0' and   to_char(p.update_date, 'yyyy-mm-dd') between ? and ? ";
		//SENDDBConnection db=SENDDBConnection.getInstance();	
		//conn=db.getConnection();  
		 PreparedStatement pst=null;// 
		 ResultSet rs=null;// 结果集类型的 属性 rs 用于接收返回的结果集
		try {
			pst=conn.prepareStatement(sql);
			  pst.setString(1,date);
			  pst.setString(2,date);
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
	public long fandMemberPointTotalNumDay(Connection conn) {
		// TODO Auto-generated method stub
		
		Long seq = null  ;
		String sql = "SELECT COUNT(*) totalnum from T_MEMBER_POINT p ,T_MEMBER m  where p.member_id=m.member_id and m.status='1' and m.isdelete='0' and    p.is_level<>'1' ";
		//SENDDBConnection db=SENDDBConnection.getInstance();	
		//conn=db.getConnection();  
		 PreparedStatement pst=null;// 
		 ResultSet rs=null;// 结果集类型的 属性 rs 用于接收返回的结果集
		try {
			pst=conn.prepareStatement(sql);
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
	public Long seqMemberPoint(Connection conn) {
		PreparedStatement pst=null;// 
		ResultSet rs=null;
		Long seq = null  ;
		String sql = "select S_T_MEMBER_POINT.nextVal seq from dual";
		try {
			pst=conn.prepareStatement(sql);
			rs=pst.executeQuery();
			while (rs.next()) {
				 seq=(rs.getLong("seq"));
			}
		} catch (SQLException e) {
				logger.error(e);
			e.printStackTrace();
		}	finally {
			if(pst!=null){
				try {
					pst.close();
				} catch (SQLException e) {
					logger.error(e);
					e.printStackTrace();
				}
			}
			if(rs!=null){

				try {
					rs.close();
				} catch (SQLException e) {
					logger.error(e);
					e.printStackTrace();
				}
				rs=null;	
			}
		} 		
		return seq;
		 
	}

}
