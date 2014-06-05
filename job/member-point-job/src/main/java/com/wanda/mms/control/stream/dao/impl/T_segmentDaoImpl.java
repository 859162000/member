package com.wanda.mms.control.stream.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.wanda.mms.control.stream.dao.T_segmentDao;
import com.wanda.mms.control.stream.util.DateUtil;
import com.wanda.mms.control.stream.vo.T_ext_point_rule;
import com.wanda.mms.control.stream.vo.T_segment;

public class T_segmentDaoImpl implements T_segmentDao {
	static Logger logger = Logger.getLogger(T_segmentDaoImpl.class.getName());

//	private PreparedStatement pst=null;//  数据库预先编译声明类型的属性 pst 用于操作数据库
	
//	private ResultSet rs=null;// 结果集类型的 属性 rs 用于接收返回的结果集

	@Override
	public T_segment fandT_segmentById(Connection conn,long segmentId) {
		// TODO Auto-generated method stub
		T_segment ts = new T_segment();
		PreparedStatement pst=null;
		ResultSet rs=null;
		 Long seq = null  ;
			String sql = "select * from T_SEGMENT t where EXT_POINT_CRITERIA_ID=?"; 
			//SENDDBConnection db=SENDDBConnection.getInstance();	
			//conn=db.getConnection();
		//	System.out.println(" ROWNUM ="+page.getPage() +"RN "+page.getPageSize()+"升降级= "+sql);
			try {
				pst=conn.prepareStatement(sql);
				pst.setLong(1,segmentId);
				rs=pst.executeQuery();
				while (rs.next()) {
					 
					ts.setSegment_id(rs.getLong("EXT_POINT_CRITERIA_ID"));//客群ID
					ts.setName(rs.getString("NAME"));//名称
					ts.setCode(rs.getString("CODE"));//编码
					ts.setType(rs.getString("TYPE"));//客群类型
					ts.setQuery_conditions(rs.getString("QUERY_CONDITIONS"));//查询条件配置
					ts.setConfig_version(rs.getString("CONFIG_VERSION"));//配置版本号
					ts.setSort_name(rs.getString("SORT_NAME"));//排序条件名称
					ts.setSort_order(rs.getString("SORT_ORDER"));//排序顺序
					ts.setCount(rs.getLong("COUNT"));//数量
					ts.setCount_time(DateUtil.getDateStrss(rs.getDate("COUNT_TIME")));//数量计算时间
					ts.setCreation_level(rs.getString("CREATION_LEVEL"));
					ts.setCreation_cinema_id(rs.getLong("CREATION_CINEMA_ID"));
					ts.setCreation_area_id(rs.getString("CREATION_AREA_ID"));
					ts.setAllow_modifier(rs.getString("ALLOW_MODIFIER"));
					ts.setCreate_by(rs.getString("CREATE_BY"));
					ts.setCreate_date(DateUtil.getDateStrss(rs.getDate("CREATE_DATE")));
					ts.setUpdate_by(rs.getString("UPDATE_BY"));
					ts.setUpdate_date(DateUtil.getDateStrss(rs.getDate("UPDATE_DATE")));
					ts.setVersion(rs.getInt("VERSION"));
					
					 
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
			 
		
		return ts;
		
	}

}
