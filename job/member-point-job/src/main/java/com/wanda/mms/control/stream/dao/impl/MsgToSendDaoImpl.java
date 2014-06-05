package com.wanda.mms.control.stream.dao.impl;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.wanda.mms.control.stream.dao.MsgToSendDao;
import com.wanda.mms.control.stream.db.SENDDBConnection;
import com.wanda.mms.control.stream.util.DateUtil;
import com.wanda.mms.control.stream.vo.MsgToSend;

 
public class MsgToSendDaoImpl implements MsgToSendDao {

	static Logger logger = Logger.getLogger(MsgToSendDaoImpl.class.getName());
	/***
	 * 添加信息发送内容
	 * 
	 * @param mts
	 */
	public void addMsgToSend(MsgToSend mts) {
		  Connection conn = null;// 数据数据库连接属性 conn，用于接收数据连接
		  PreparedStatement pst = null;// 数据库预先编译声明类型的属性 pst 用于操作数据库
		  ResultSet rs = null;// 结果集类型的 属性 rs 用于接收返回的结果集


		// 序列名不对以后改过来 select S_T_F_VOUCHER.nextVal from dual
		// 得到之后在插入 并把这个值回传。
		String sql = "insert into t_im_msgtosend (seqId,sendTypeId,DataYMD,Content,generateTime,cinema_inner_code,msmflag)values(?,?,?,?,sysdate,?,?)";
		SENDDBConnection db = SENDDBConnection.getInstance();
		conn = db.getConnection();

		try {
			pst = conn.prepareStatement(sql);
			// mts
			pst.setLong(1, mts.getMsgId());
			pst.setInt(2, mts.getSendTypeId());
			pst.setLong(3, mts.getDataYMD());
			pst.setString(4, mts.getContent());
			pst.setString(5, mts.getCinema_inner_code());
			pst.setString(6, mts.getMsmflag());
			pst.executeUpdate();
		} catch (SQLException e) {
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
	}

	/***
	 * 得到信息发送内容的SEQ
	 * 
	 * @return Long
	 */
	public Long findMsgSEQ() {
		  Connection conn = null;// 数据数据库连接属性 conn，用于接收数据连接
		  PreparedStatement pst = null;// 数据库预先编译声明类型的属性 pst 用于操作数据库
		  ResultSet rs = null;// 结果集类型的 属性 rs 用于接收返回的结果集
		Long seq = null;
		String sql = "select S_T_IM_MSGTOSEND.nextVal seq from dual";
		SENDDBConnection db = SENDDBConnection.getInstance();
		conn = db.getConnection();

		try {
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				seq = (rs.getLong("seq"));
			}
		} catch (SQLException e) {
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

	/***
	 * 跟据时间与信息类型 查询出当天同一类型有多少条
	 * 
	 * @param date
	 * @param SendTypeId
	 */
	public List<MsgToSend> findMsgToSendByDate(Long date) {
		  Connection conn = null;// 数据数据库连接属性 conn，用于接收数据连接
		  PreparedStatement pst = null;// 数据库预先编译声明类型的属性 pst 用于操作数据库
		  ResultSet rs = null;// 结果集类型的 属性 rs 用于接收返回的结果集
		List<MsgToSend> mtslist = new ArrayList<MsgToSend>();
	//	String sql = "select  seqid,SendTypeID,DataYMD,Content,GenerateTime from T_IM_MSGTOSEND where SendTypeID=? and DataYMD=?";
		
		String sql = "        SELECT A.*, ROWNUM RN   "
   +"        FROM ( select t.seqid,t.sendtypeid,t.dataymd,t.generatetime,t.content from T_IM_MSGTOSEND t where t.dataymd=? order by t.seqid desc 　) A   "
  +"   WHERE ROWNUM <=1  ";
		SENDDBConnection db = SENDDBConnection.getInstance();
		conn = db.getConnection();

		try {
			pst = conn.prepareStatement(sql);

		 
			pst.setLong(1, date);

			rs = pst.executeQuery();
			while (rs.next()) {
				MsgToSend mts = new MsgToSend();
				mts.setMsgId(rs.getLong("seqid"));
				mts.setSendTypeId(rs.getInt("SendTypeID"));
				mts.setDataYMD(rs.getLong("DataYMD"));
				mts.setContent(rs.getString("Content"));

				String gt = DateUtil.getDateStrss(rs.getDate("GenerateTime"));
				mts.setGenerateTime(gt);

				mtslist.add(mts);
			}
		} catch (SQLException e) {
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
		return mtslist;
	}

	/***
	 * 跟据时间与信息类型 查询出当天同一类型时间最晚的一条
	 * 
	 * @param date
	 * @param SendTypeId
	 */
	public MsgToSend findMsgByDate(Long date, Long SendTypeId) {
		  Connection conn = null;// 数据数据库连接属性 conn，用于接收数据连接
		  PreparedStatement pst = null;// 数据库预先编译声明类型的属性 pst 用于操作数据库
		  ResultSet rs = null;// 结果集类型的 属性 rs 用于接收返回的结果集
		// select t.*, rownum from (select
		// seqid,SendTypeID,DataYMD,Content,GenerateTime from T_IM_MSGTOSEND
		// where SendTypeID=1 and DataYMD=20121206 order by GenerateTime desc )t
		// where rownum=1
		MsgToSend mts = new MsgToSend();
		String sql = "select t.*, rownum from (select  seqid,SendTypeID,DataYMD,Content,GenerateTime from T_IM_MSGTOSEND where SendTypeID=? and DataYMD=? order by  GenerateTime desc )t where rownum=1";
		SENDDBConnection db = SENDDBConnection.getInstance();
		conn = db.getConnection();

		try {
			pst = conn.prepareStatement(sql);

			pst.setLong(1, SendTypeId);
			pst.setLong(2, date);

			rs = pst.executeQuery();
			while (rs.next()) {

				mts.setMsgId(rs.getLong("seqid"));
				mts.setSendTypeId(rs.getInt("SendTypeID"));
				mts.setDataYMD(rs.getLong("DataYMD"));
				mts.setContent(rs.getString("Content"));

				String gt = DateUtil.getDateStrss(rs.getDate("GenerateTime"));
				mts.setGenerateTime(gt);
			}
		} catch (SQLException e) {
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
		return mts;
	}

	@Override
	public List<MsgToSend> findMsgToSendByType(Long date, String type) {
		  Connection conn = null;// 数据数据库连接属性 conn，用于接收数据连接
		  PreparedStatement pst = null;// 数据库预先编译声明类型的属性 pst 用于操作数据库
		  ResultSet rs = null;// 结果集类型的 属性 rs 用于接收返回的结果集
		List<MsgToSend> mtslist = new ArrayList<MsgToSend>();
	//	String sql = "select  seqid,SendTypeID,DataYMD,Content,GenerateTime from T_IM_MSGTOSEND where SendTypeID=? and DataYMD=?";
		
		String sql = " select t.seqid,t.sendtypeid,t.dataymd,t.generatetime,t.content, t.cinema_inner_code,t.msmflag from T_IM_MSGTOSEND t where t.dataymd=? and t.msmflag=?order by t.seqid desc 　   ";
		SENDDBConnection db = SENDDBConnection.getInstance();
		conn = db.getConnection();

		try {
			pst = conn.prepareStatement(sql);

		 
			pst.setLong(1,date);
			pst.setString(2,type);
			rs = pst.executeQuery();
			while (rs.next()) {
				MsgToSend mts = new MsgToSend();
				mts.setMsgId(rs.getLong("seqid"));
				mts.setSendTypeId(rs.getInt("SendTypeID"));
				mts.setDataYMD(rs.getLong("DataYMD"));
				mts.setContent(rs.getString("Content"));
				String gt = DateUtil.getDateStrss(rs.getDate("GenerateTime"));
				mts.setCinema_inner_code(rs.getString("cinema_inner_code"));
				mts.setMsmflag(rs.getString("msmflag"));
				mts.setGenerateTime(gt);

				mtslist.add(mts);
			}
		} catch (SQLException e) {
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
		return mtslist;
	}

	@Override
	public void upMsgToSend(MsgToSend mts) {
		  Connection conn = null;// 数据数据库连接属性 conn，用于接收数据连接
		  PreparedStatement pst = null;// 数据库预先编译声明类型的属性 pst 用于操作数据库
		  ResultSet rs = null;// 结果集类型的 属性 rs 用于接收返回的结果集


		// 序列名不对以后改过来 select S_T_F_VOUCHER.nextVal from dual
		// 得到之后在插入 并把这个值回传。
		String sql = "update t_im_msgtosend set msmflag=? where seqid=?";
		SENDDBConnection db = SENDDBConnection.getInstance();
		conn = db.getConnection();

		try {
			pst = conn.prepareStatement(sql);
			// mts
			System.out.println(sql);
			System.out.println(mts.getMsmflag()+"  : "+mts.getMsgId());
			pst.setString(1, mts.getMsmflag());
			pst.setLong(2, mts.getMsgId());
			
			pst.executeUpdate();
		} catch (SQLException e) {
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
	}
}
