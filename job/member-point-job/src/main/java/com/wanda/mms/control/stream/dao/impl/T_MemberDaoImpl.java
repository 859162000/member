package com.wanda.mms.control.stream.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.wanda.mms.control.stream.dao.T_MemberDao;
import com.wanda.mms.control.stream.vo.T_Dimdef;
import com.wanda.mms.control.stream.vo.T_Member;

public class T_MemberDaoImpl implements T_MemberDao {
	static Logger logger = Logger.getLogger(T_MemberDaoImpl.class.getName());

//	private PreparedStatement pst=null;//  数据库预先编译声明类型的属性 pst 用于操作数据库
	
//	private ResultSet rs=null;// 结果集类型的 属性 rs 用于接收返回的结果集

	@Override
	public T_Member fandT_MemberByMOBILE(Connection conn, String MOBILE) {
		// TODO Auto-generated method stub
		T_Member tm = new T_Member();
		PreparedStatement pst=null;
		ResultSet rs=null;
		try {
			String sqll = "select * from T_MEMBER where MOBILE=? where STATUS=1 and ISDELETE=0 ";
			
				pst=conn.prepareStatement(sqll);
				pst.setString(1, MOBILE);
	
				rs=pst.executeQuery();
				while (rs.next()) {
					//会员ID#MEMBER_ID
					tm.setMemberId(rs.getLong("MEMBER_ID"));
					
				}
 
		
			} catch (SQLException e) {
				// TODO Auto-generated catch block
			 
					logger.error(e);
				 
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
				}
				
			} 	
			
		
		return tm;
	}

	@Override
	public T_Member fandT_MemberByID(Connection conn, Long memberId) {
		// TODO Auto-generated method stub
		T_Member tm = new T_Member();
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		//STATUS 会员状态：1:有效;0:无效
		//ISDELETE 逻辑删除标识,默认:0 未删除;1删除;其他:非法
		try {
			String sqll = "select * from T_MEMBER where MEMBER_ID=? and STATUS=1 and ISDELETE=0";
			
				pst=conn.prepareStatement(sqll);
				pst.setLong(1, memberId);
				rs=pst.executeQuery();
				while (rs.next()) {
					//会员ID#MEMBER_ID
					tm.setMemberId(rs.getLong("MEMBER_ID"));//会员ID
					tm.setMobile(rs.getString("mobile"));//会员手机号
					tm.setStatus(rs.getLong("status"));//是否可用
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
				}
				
			}	
			
		
		return tm;
	}

}
