package com.wanda.mms.control.stream.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.wanda.mms.control.stream.dao.T_member_tagDao;
import com.wanda.mms.control.stream.db.SENDDBConnection;
import com.wanda.mms.control.stream.util.DateUtil;
import com.wanda.mms.control.stream.vo.T_member_tag;

public class T_member_tagDaoImpl implements T_member_tagDao {
	static Logger logger = Logger.getLogger(T_member_tagDaoImpl.class.getName());

//	private Connection conn=null;// 数据数据库连接属性 conn，用于接收数据连接
//	private PreparedStatement pst=null;//  数据库预先编译声明类型的属性 pst 用于操作数据库
//	private ResultSet rs=null;// 结果集类型的 属性 rs 用于接收返回的结果集

	/**
	 * 插入更新会员标签
	 * @param tmlist
	 * @return
	 */
	@Override
	public String updateT_member_tag(List<T_member_tag> tmlist) {
		// TODO Auto-generated method stub
		int[] aa = null;
		Connection conn=null;
		PreparedStatement pst=null;
		String getSendTime;
		 //序列名不对以后改过来
		 Date da = new Date();
		 String date = DateUtil.getDateStrss(da);
		String sql = "UPDATE T_MEMBER_TAG SET FIRST_SHOW=?,PRICE_SENSE=?,FAMILY_STRUCT=?,EXCEPTION_TRANS=?,ACTIVITY=?,ISDELETE=?,UPDATE_BY=?,UPDATE_DATE=to_date('" + date + "','yyyy-mm-dd hh24:mi:ss'),VERSION=? WHERE MEMBER_ID=?";// 
		SENDDBConnection db=SENDDBConnection.getInstance();	
		conn=db.getConnection();
		String flag ="";
		try {
			pst=conn.prepareStatement(sql);
			for (int i = 0; i < tmlist.size(); i++) {
				T_member_tag tm =new T_member_tag();
				tm = tmlist.get(i);
				pst.setString(1, tm.getFirst_show());
				pst.setString(2, tm.getPrice_sense());				
			 
				pst.setString(3,tm.getFamily_struct() );
				pst.setString(4, tm.getException_trans());
				pst.setString(5, tm.getActivity());
				pst.setInt(6, tm.getIsdelete());
				pst.setString(7, tm.getUpdate_By());
				pst.setInt(8, tm.getVersion());
				pst.setLong(9, tm.getMember_id());
				pst.addBatch();
				
			}
		    aa = pst.executeBatch(); 
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < aa.length; i++) {	
				sb.append(aa.toString()).append(",");
			}
			flag = sb.toString();
		
			conn.commit();
			 
		
	
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
		}  
		
		 
//		}
		return flag;	//返回状态标识
		
	 
	}

}
