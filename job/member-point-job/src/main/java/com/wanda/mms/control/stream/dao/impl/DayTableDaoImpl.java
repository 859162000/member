package com.wanda.mms.control.stream.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.wanda.mms.control.stream.dao.DayTableDao;
import com.wanda.mms.control.stream.vo.DayTable;

public class DayTableDaoImpl implements DayTableDao {
	static Logger logger = Logger.getLogger(DayTableDaoImpl.class.getName());


	@Override
	public DayTable fandDayTableBynewMembers(Connection con, String sql,
			String date1,String date2,String type) {
		// TODO Auto-generated method stub
		
		logger.info("Entering fandDayTableBynewMembers()");
		 
		  PreparedStatement pst=null;//  数据库预先编译声明类型的属性 pst 用于操作数据库
		  ResultSet rs=null;// 结果集类型的 属性 rs 用于接收返回的结果集
		  DayTable dt = new DayTable();
		
//		SENDDBConnection db=SENDDBConnection.getInstance();	
//		conn=db.getConnection();
		
		try {
			pst=con.prepareStatement(sql);
			pst.setString(1,date1);//时间
			pst.setString(2,date2);//时间
			System.out.println("sql:"+sql);
			System.out.println("pst:"+date1+","+date2);
			rs=pst.executeQuery();
			while (rs.next()) {
				if(type.equals("1")){
				if(rs.getString("NEWMEMBERS")!=null){//	新增会员数：14151(4361552) 
					dt.setNewMembers(rs.getString("NEWMEMBERS"));//newMembers
					dt.setNewMembersCount(rs.getString("NEWMEMBERSCOUNT"));//String netMembersCount; 总数
				}else{
					dt.setNewMembers("0");//newMembers
					dt.setNewMembersCount("0");//String netMembersCount; 总数
			
				} 
				if(rs.getString("NETMEMBERS")!=null){ //				线上入会数：3010(665181)
					dt.setNetMembers(rs.getString("NETMEMBERS")); // String netMembers;
					dt.setNetMembersCount(rs.getString("NETMEMBERSCOUNT"));// String netMembersCount;
				}else{
					dt.setNetMembers("0"); // String netMembers;
					dt.setNetMembersCount("0");// String netMembersCount;
			
				}
				if(rs.getString("NOTNETMEMBERS")!=null){ //				线下入会数：11141(3607578) 
					dt.setNotNetMembers(rs.getString("NOTNETMEMBERS"));// String notNetMembers;
					dt.setNotNetMembersCount(rs.getString("NOTNETMEMBERSCOUNT"));// String notNetMembersCount;
				}else{
					dt.setNotNetMembers("0");// String notNetMembers;
					dt.setNotNetMembersCount("0");// String notNetMembersCount;
				
				}
			}
				if(type.equals("2")){
				if(rs.getString("NETTICKETSUM")!=null){//				线上票房消费金额：38546(92777844) 
					dt.setNetTicketSum(rs.getString("NETTICKETSUM"));//private String netTicketSum;
					dt.setNetTicketSumCount(rs.getString("NETTICKETSUMCOUNT"));//private String netTicketSumCount;
				}else{
					dt.setNetTicketSum("0");//private String netTicketSum;
					dt.setNetTicketSumCount("0");//private String netTicketSumCount;
				
				}
 
				if(rs.getString("NOTNETTICKETSUM")!=null){ 			 
//					线下票房消费金额：5503102(469954405) 
					dt.setNotNetTicketSum(rs.getString("NOTNETTICKETSUM"));// String notNetTicketSum;
//					
					dt.setNotNetTicketSumCount(rs.getString("NOTNETTICKETSUMCOUNT"));// String notNetTicketSumCount;
				}else{
					dt.setNotNetTicketSum("0");// String notNetTicketSum;
//					
					dt.setNotNetTicketSumCount("0");// String notNetTicketSumCount;
			
				}
			}
			if(type.equals("3")){
				if(rs.getString("NETGOODSSUM")!=null){ 			 
//					线上卖品消费金额：695(1271131) 
					dt.setNetGoodsSum(rs.getString("NETGOODSSUM"));//  String netGoodsSum;
					
					dt.setNetGoddsSumCount(rs.getString("NETGODDSSUMCOUNT"));//  String netGoddsSumCount;
				}else{
					dt.setNetGoodsSum("0");//  String netGoodsSum;
					
					dt.setNetGoddsSumCount("0");//  String netGoddsSumCount;
			
				}
				
				if(rs.getString("NOTNETGOODSSUM")!=null){ 			 
////				线下卖品消费金额：345682(15918924) 
					dt.setNotNetGoodsSum(rs.getString("NOTNETGOODSSUM"));// private String notNetGoodsSum;
//					
					dt.setNotNetGoddsSumCount(rs.getString("NOTNETGODDSSUMCOUNT"));// private String notNetGoddsSumCount;
				}else{
					dt.setNotNetGoodsSum("0");// private String notNetGoodsSum;
//					
					dt.setNotNetGoddsSumCount("0");// private String notNetGoddsSumCount;
				
				}
			}
			if(type.equals("4")){
				if(rs.getString("NETPOINT")!=null){ 			 
////				线上会员累计积分：43243(76232240) 
					dt.setNetPoint(rs.getString("NETPOINT"));// private String netPoint;
					
					dt.setNotNetPoint(rs.getString("NOTNETPOINT"));// private String notNetPoint;
				}else{
					dt.setNetPoint("0");// private String netPoint;
					
					dt.setNotNetPoint("0");// private String notNetPoint;
			
				}
			}
			if(type.equals("5")){
				if(rs.getString("NETPOINTCOUNT")!=null){ 			 
////				线下会员累计积分：4964341(678524872) 
					dt.setNetPointcount(rs.getString("NETPOINTCOUNT"));// private String netPointcount;
					dt.setNotNetPointcount(rs.getString("NOTNETPOINTCOUNT"));// private String notNetPointcount;
				}else{
					dt.setNetPointcount("0");// private String netPointcount;
					dt.setNotNetPointcount("0");// private String notNetPointcount;
			
				}

			}
			if(type.equals("6")){
			   	if(rs.getString("EXCHANGEPOINT")!=null){ 			 
////				线上会员兑换积分：96
					dt.setExchangePoint(rs.getString("EXCHANGEPOINT"));	// private String exchangePoint;
					//dt.get = rs.getString("");
			    	}else{
			    		dt.setExchangePoint("0");	// private String exchangePoint;				
			    	}
		    	}
			if(type.equals("7")){
				if(rs.getString("NETTICKETSUMMONTH")!=null){ 			 
////				线下会员累计积分：4964341(678524872) 
					dt.setNetTicketSumMonth(rs.getString("NETTICKETSUMMONTH"));// private String netPointcount;
					dt.setNotNetTicketSumMonth(rs.getString("NOTNETTICKETSUMMONTH"));// private String notNetPointcount;
				}else{
					dt.setNetTicketSumMonth("0");// private String netPointcount;
					dt.setNotNetTicketSumMonth("0");// private String notNetPointcount;
			
				}

			}
			if(type.equals("9")){
				if(rs.getString("NETTICKETSUMYEAR")!=null){ 			 
////				线下会员累计积分：4964341(678524872) 
					dt.setNetTicketSumYear(rs.getString("NETTICKETSUMYEAR"));// private String netPointcount;
					dt.setNotNetTicketSumYear(rs.getString("NOTNETTICKETSUMYEAR"));// private String notNetPointcount;
				}else{
					dt.setNetTicketSumYear("0");// private String netPointcount;
					dt.setNotNetTicketSumYear("0");// private String notNetPointcount;
			
				}

			}
			
			if(type.equals("11")){
			   	if(rs.getString("EXCHANGEPOINTNEW")!=null){ 			 
////				线上会员兑换积分：96
					dt.setExchangePointnew(rs.getString("EXCHANGEPOINTNEW"));	// private String exchangePoint;
					//dt.get = rs.getString("");
			    	}else{
			    		dt.setExchangePointnew("0");	// private String exchangePoint;				
			    	}
		    	}
			if(type.equals("12")){
			   	if(rs.getString("EXCHANGEPOINTBALANCE")!=null){ 			 
////				线上会员兑换积分：96
					dt.setExchangePointBalance(rs.getString("EXCHANGEPOINTBALANCE"));	// private String exchangePoint;
					//dt.get = rs.getString("");
			    	}else{
			    		dt.setExchangePointBalance("0");	// private String exchangePoint;				
			    	}
		    	}
			
			
			
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
		
		return dt;
	}


	@Override
	public List<DayTable> fandDayTableBynewMembersList(Connection con,
			String sql, String date1, String date2, String type) {
		// TODO Auto-generated method stub
		
		logger.info("Entering fandDayTableBynewMembers()");
		 
		  PreparedStatement pst=null;//  数据库预先编译声明类型的属性 pst 用于操作数据库
		  ResultSet rs=null;// 结果集类型的 属性 rs 用于接收返回的结果集
		 
		List<DayTable> dtlist = new ArrayList<DayTable>();
//		SENDDBConnection db=SENDDBConnection.getInstance();	
//		conn=db.getConnection();
		
		try {
			pst=con.prepareStatement(sql);
			pst.setString(1,date1);//时间
			pst.setString(2,date2);//时间
			System.out.println("sql:"+sql);
			System.out.println("pst:"+date1+","+date2);
			rs=pst.executeQuery();
			while (rs.next()) {
				if(type.equals("1")){
					if(rs.getString("NEWMEMBERS")!=null){//	新增会员数：14151(4361552) 
						 DayTable dt = new DayTable();
						 dt.setNewMembers(rs.getString("NEWMEMBERS"));//newMembers
						 dt.setNewMembersCount(rs.getString("NEWMEMBERSCOUNT"));//String netMembersCount; 总数
						 dt.setCinema_inner_code(rs.getString("INNER_CODE"));
					  //				线上入会数：3010(665181)
						 dt.setNetMembers(rs.getString("NETMEMBERS")); // String netMembers;
						 dt.setNetMembersCount(rs.getString("NETMEMBERSCOUNT"));// String netMembersCount;
					  //				线下入会数：11141(3607578) 
						 dt.setNotNetMembers(rs.getString("NOTNETMEMBERS"));// String notNetMembers;
						 dt.setNotNetMembersCount(rs.getString("NOTNETMEMBERSCOUNT"));// String notNetMembersCount;
						 dtlist.add(dt);
				} 
			}
				if(type.equals("2")){
					if(rs.getString("NETTICKETSUM")!=null){//				线上票房消费金额：38546(92777844) 
						 DayTable dt = new DayTable();
						dt.setNetTicketSum(rs.getString("NETTICKETSUM"));//private String netTicketSum;
						dt.setNetTicketSumCount(rs.getString("NETTICKETSUMCOUNT"));//private String netTicketSumCount;
//					线下票房消费金额：5503102(469954405) 
						dt.setNotNetTicketSum(rs.getString("NOTNETTICKETSUM"));// String notNetTicketSum;
						dt.setNotNetTicketSumCount(rs.getString("NOTNETTICKETSUMCOUNT"));// String notNetTicketSumCount;
						dt.setCinema_inner_code(rs.getString("INNER_CODE"));
						dtlist.add(dt);
					} 
			}
			if(type.equals("3")){
				if(rs.getString("NETGOODSSUM")!=null){ 			 
//					线上卖品消费金额：695(1271131) 
					 DayTable dt = new DayTable();
					dt.setNetGoodsSum(rs.getString("NETGOODSSUM"));//  String netGoodsSum;
					
					dt.setNetGoddsSumCount(rs.getString("NETGODDSSUMCOUNT"));//  String netGoddsSumCount;
			 
////				线下卖品消费金额：345682(15918924) 

					dt.setNotNetGoodsSum(rs.getString("NOTNETGOODSSUM"));// private String notNetGoodsSum;
//					
					dt.setNotNetGoddsSumCount(rs.getString("NOTNETGODDSSUMCOUNT"));// private String notNetGoddsSumCount;
					 dt.setCinema_inner_code(rs.getString("INNER_CODE"));
					 dtlist.add(dt);
				} 
			}
			if(type.equals("4")){
				if(rs.getString("NETPOINT")!=null){ 			 
////				线上会员累计积分：43243(76232240) 
					 DayTable dt = new DayTable();
					dt.setNetPoint(rs.getString("NETPOINT"));// private String netPoint;
					
					dt.setNotNetPoint(rs.getString("NOTNETPOINT"));// private String notNetPoint;
					 dt.setCinema_inner_code(rs.getString("INNER_CODE"));
					 dtlist.add(dt);
				} 
			}
			if(type.equals("5")){
				if(rs.getString("NETPOINTCOUNT")!=null){ 			 
////				线下会员累计积分：4964341(678524872) 
					 DayTable dt = new DayTable();
					dt.setNetPointcount(rs.getString("NETPOINTCOUNT"));// private String netPointcount;
					dt.setNotNetPointcount(rs.getString("NOTNETPOINTCOUNT"));// private String notNetPointcount;
					 dt.setCinema_inner_code(rs.getString("INNER_CODE"));
					 dtlist.add(dt);
				} 

			}
			if(type.equals("6")){
				if(rs.getString("EXCHANGEPOINT")!=null){ 			 
////				线上会员兑换积分：96
					 DayTable dt = new DayTable();
					dt.setExchangePoint(rs.getString("EXCHANGEPOINT"));	// private String exchangePoint;
					 dt.setCinema_inner_code(rs.getString("INNER_CODE"));
					//dt.get = rs.getString("");
					 dtlist.add(dt);
			    	} 
		    	}
			if(type.equals("7")){
				if(rs.getString("NETTICKETSUMMONTH")!=null){ 			 
////				线下会员累计积分：4964341(678524872) 
					 DayTable dt = new DayTable();
					dt.setNetTicketSumMonth(rs.getString("NETTICKETSUMMONTH"));// private String netPointcount;
					dt.setNotNetTicketSumMonth(rs.getString("NOTNETTICKETSUMMONTH"));// private String notNetPointcount;
					 dt.setCinema_inner_code(rs.getString("INNER_CODE"));
					 dtlist.add(dt);
				} 

			}
			if(type.equals("9")){
				if(rs.getString("NETTICKETSUMYEAR")!=null){ 			 
////				线下会员累计积分：4964341(678524872) 
					 DayTable dt = new DayTable();
					dt.setNetTicketSumYear(rs.getString("NETTICKETSUMYEAR"));// private String netPointcount;
					dt.setNotNetTicketSumYear(rs.getString("NOTNETTICKETSUMYEAR"));// private String notNetPointcount;
					 dt.setCinema_inner_code(rs.getString("INNER_CODE"));
					 dtlist.add(dt);
				} 

			}
			
			if(type.equals("11")){
				if(rs.getString("EXCHANGEPOINTNEW")!=null){ 			 
////				线上会员兑换积分：96
					 DayTable dt = new DayTable();
					dt.setExchangePointnew(rs.getString("EXCHANGEPOINTNEW"));	// private String exchangePoint;
					 dt.setCinema_inner_code(rs.getString("INNER_CODE"));
					//dt.get = rs.getString("");
					 dtlist.add(dt);
			    	} 
		    	}
			if(type.equals("12")){
				if(rs.getString("EXCHANGEPOINTBALANCE")!=null){ 			 
////				线上会员兑换积分：96
					 DayTable dt = new DayTable();
					dt.setExchangePointBalance(rs.getString("EXCHANGEPOINTBALANCE"));	// private String exchangePoint;
					 dt.setCinema_inner_code(rs.getString("INNER_CODE"));
					//dt.get = rs.getString("");
					 dtlist.add(dt);
			    	} 
		    	}
			
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
		
		return dtlist;
	}


	@Override
	public List<DayTable> fandDayInnerCodeList(Connection con) {
		// TODO Auto-generated method stub
		
		logger.info("Entering fandDayTableBynewMembers()");
		 
		  PreparedStatement pst=null;//  数据库预先编译声明类型的属性 pst 用于操作数据库
		  ResultSet rs=null;// 结果集类型的 属性 rs 用于接收返回的结果集
		 
		List<DayTable> dtlist = new ArrayList<DayTable>();
//		SENDDBConnection db=SENDDBConnection.getInstance();	
//		conn=db.getConnection();
		
		try {
			String sql = " Select INNER_CODE from    CCS_NRPT2.T_D_CON_CINEMA t";
			pst=con.prepareStatement(sql);
			System.out.println("sql:"+sql);
			rs=pst.executeQuery();
			while (rs.next()) {
				
					 //	新增会员数：14151(4361552) 
					DayTable dt = new DayTable();
					dt.setNewMembers("0");//newMembers
					dt.setNewMembersCount("0");//String netMembersCount; 总数
					  //				线上入会数：3010(665181)
					dt.setNetMembers("0"); // String netMembers;
					dt.setNetMembersCount("0");// String netMembersCount;
					  //				线下入会数：11141(3607578) 
					dt.setNotNetMembers("0");// String notNetMembers;
					dt.setNotNetMembersCount("0");// String notNetMembersCount;
			 			//	线上票房消费金额：38546(92777844) 
					dt.setNetTicketSum("0");//private String netTicketSum;
					dt.setNetTicketSumCount("0");//private String netTicketSumCount;
//					线下票房消费金额：5503102(469954405) 
					dt.setNotNetTicketSum("0");// String notNetTicketSum;
					dt.setNotNetTicketSumCount("0");// String notNetTicketSumCount; 
//					线上卖品消费金额：695(1271131) 
					dt.setNetGoodsSum("0");//  String netGoodsSum;
					dt.setNetGoddsSumCount("0");//  String netGoddsSumCount;
////				线下卖品消费金额：345682(15918924) 
					dt.setNotNetGoodsSum("0");// private String notNetGoodsSum;
					dt.setNotNetGoddsSumCount("0");// private String notNetGoddsSumCount;
////				线上会员累计积分：43243(76232240) 
					dt.setNetPoint("0");// private String netPoint;
					dt.setNotNetPoint("0");// private String notNetPoint; 
////				线下会员累计积分：4964341(678524872) 
					dt.setNetPointcount("0");// private String netPointcount;
					dt.setNotNetPointcount("0");// private String notNetPointcount; 
////				线上会员兑换积分：96
					dt.setExchangePoint("0");	// private String exchangePoint;
					//dt.get = rs.getString(""); 
////				线下会员累计积分：4964341(678524872) 
					dt.setNetTicketSumMonth("0");// private String netPointcount;
					dt.setNotNetTicketSumMonth("0");// private String notNetPointcount;		 
////				线下会员累计积分：4964341(678524872) 
					dt.setNetTicketSumYear("0");// private String netPointcount;
					dt.setNotNetTicketSumYear("0");// private String notNetPointcount;
////				线上会员兑换积分：96
					dt.setExchangePointnew("0");	// private String exchangePoint; 
					//dt.get = rs.getString("");
////				线上会员兑换积分：96
					dt.setExchangePointBalance("0");	// private String exchangePoint;
					dt.setCinema_inner_code(rs.getString("INNER_CODE"));
					//dt.get = rs.getString("");
					dtlist.add(dt);
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
		
		return dtlist;
	}

}
