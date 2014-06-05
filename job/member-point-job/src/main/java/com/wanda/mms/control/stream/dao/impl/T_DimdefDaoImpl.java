package com.wanda.mms.control.stream.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.wanda.mms.control.stream.dao.T_DimdefDao;
import com.wanda.mms.control.stream.util.DateUtil;
import com.wanda.mms.control.stream.vo.MemberLevel;
import com.wanda.mms.control.stream.vo.T_Dimdef;

public class T_DimdefDaoImpl implements T_DimdefDao {
	static Logger logger = Logger.getLogger(T_DimdefDaoImpl.class.getName());

//	private PreparedStatement pst=null;//  数据库预先编译声明类型的属性 pst 用于操作数据库
	
//	private ResultSet rs=null;// 结果集类型的 属性 rs 用于接收返回的结果集
	
	@Override
	public T_Dimdef fandT_DimdefByID(Connection conn, long T_Dimdefid,
			String name) {
		// TODO Auto-generated method stub
		PreparedStatement pst=null;
		ResultSet rs=null;
		T_Dimdef td = new T_Dimdef();
		try {
			String sqll = "select * from T_DIMDEF where typeid=? and name=?";
			
				pst=conn.prepareStatement(sqll);
				pst.setLong(1, T_Dimdefid);
				pst.setString(2, name);
				rs=pst.executeQuery();
				while (rs.next()) {
					//会员ID#MEMBER_ID
				//	ml.setMember_id(rs.getLong("MEMBER_ID"));//会员ID
					//维表代码
					td.setCode(rs.getString("code"));
					//维表名称
					td.setName(rs.getString("name"));
					//维表ID
					td.setSeqid(rs.getLong("seqid"));
					//维表类型id
					td.setTypeid(rs.getLong("typeid"));
					//逻辑删除标识,默认:0 未删除;1删除;其他:非法
					td.setIsdelete(rs.getInt("ISDELETE"));
					//版本号
					td.setVersion(rs.getInt("VERSION"));
					
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
			
		
		return td;
	}

}
