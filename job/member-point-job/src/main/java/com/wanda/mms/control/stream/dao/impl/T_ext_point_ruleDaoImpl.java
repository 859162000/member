package com.wanda.mms.control.stream.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.wanda.mms.control.stream.dao.T_ext_point_ruleDao;
import com.wanda.mms.control.stream.util.DateUtil;
import com.wanda.mms.control.stream.vo.T_ext_point_rule;
import com.wanda.mms.control.stream.vo.T_ticket_trans_order;

public class T_ext_point_ruleDaoImpl implements T_ext_point_ruleDao {
	static Logger logger = Logger.getLogger(T_ext_point_ruleDaoImpl.class.getName());

//	private PreparedStatement pst=null;//  数据库预先编译声明类型的属性 pst 用于操作数据库
	
//	private ResultSet rs=null;// 结果集类型的 属性 rs 用于接收返回的结果集
	
	@Override
	public List<T_ext_point_rule> fandT_ext_point_ruleByDate(Connection conn,String statusa,String statusb,
			String endDtime) {
		// TODO Auto-generated method stub
		 List<T_ext_point_rule> trlist = new ArrayList<T_ext_point_rule>();
		 PreparedStatement pst=null;
		 ResultSet rs=null;
		 Long seq = null  ;
			String sql = "select * from T_EXT_POINT_RULE t where    to_char(END_DTIME,'yyyy-mm-dd hh:mi:ss') = ? STATUS=? or STATUS=?"; 
			//SENDDBConnection db=SENDDBConnection.getInstance();	
			//conn=db.getConnection();
		//	System.out.println(" ROWNUM ="+page.getPage() +"RN "+page.getPageSize()+"升降级= "+sql);
			try {
				pst=conn.prepareStatement(sql);
				pst.setString(1,endDtime);
				pst.setString(2,statusa);
				pst.setString(3,statusb);
				rs=pst.executeQuery();
				while (rs.next()) {
					T_ext_point_rule tr= new T_ext_point_rule();
					//会员交易表ID
					tr.setExt_point_rule_id(rs.getLong("EXT_POINT_RULE_ID"));
					//POS 订单ID
					tr.setSegment_id(rs.getLong("EXT_POINT_CRITERIA_ID"));
					//影票 金额
					tr.setCode(rs.getString("CODE"));
					// 影票 数量
					tr.setName(rs.getString("NAME"));
					//会员手机号
					tr.setStatus(rs.getString("STATUS"));
					//是否计算 -- 状态
					tr.setStart_dtime(DateUtil.getDateStrss(rs.getDate("START_DTIME")));
					tr.setEnd_dtime(DateUtil.getDateStrss(rs.getDate("END_DTIME")));
					tr.setAddition_percent(rs.getDouble("ADDITION_PERCENT"));
					tr.setAddition_code(rs.getDouble("ADDITION_CODE"));
					tr.setCreate_by(rs.getString("CREATE_BY"));
					tr.setCreate_date(DateUtil.getDateStrss(rs.getDate("CREATE_DATE")));
					tr.setUpdate_by(rs.getString("UPDATE_BY"));
					tr.setUpdate_date(DateUtil.getDateStrss(rs.getDate("UPDATE_DATE")));
					tr.setVersion(rs.getInt("VERSION"));
					
					trlist.add(tr);
				}
	
		
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
			 
		
		return trlist;
		
	}

}
