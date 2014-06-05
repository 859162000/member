package com.wanda.mms.control.stream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.solar.etl.config.mapping.Field;
import com.solar.etl.config.mapping.FieldSet;
import com.solar.etl.db.ResultQuery;
import com.solar.etl.db.SqlHelp;
import com.solar.etl.log.LogUtils;
import com.solar.etl.spi.LineHandle;
import com.wanda.ccs.member.segment.service.CriteriaQueryService;
import com.wanda.ccs.member.segment.service.impl.CriteriaQueryServiceImpl;
import com.wanda.ccs.sqlasm.CriteriaResult;
import com.wanda.ccs.sqlasm.expression.ExpressionCriterion;
import com.wanda.ccs.sqlasm.expression.JsonCriteriaHelper;

/**
 *	会员积分计算 ：特殊积分临时表写入
 * @author wangshuai
 * @date 2013-07-19	
 */


public class TextPointRulestatus implements LineHandle{
	private Connection conn;
	private Connection conndw;
	Log log = LogFactory.getLog(TextPointRulestatus.class);
	public TextPointRulestatus(Connection conn){
		this.conn=conn;
	}
	public TextPointRulestatus(){
		conn=Basic.mbr;
		conndw=Basic.mbrdw;
	}
	@Override
	public int handle(FieldSet fieldset) {
		 
		int r=0;
		
		//String sql ="select  MEMBER_ID from T_TICKET_TRANS_DETAIL t   where to_char(BIZ_DATE,'yyyymmdd') between ? and ?  group by MEMBER_ID ";
	//	String sql ="select  MEMBER_ID from T_TICKET_TRANS_DETAIL t   where IS_POINT!=? and IS_POINT!=? and IS_POINT!=?  group by MEMBER_ID ";
		String sqlFilm ="select EXT_POINT_CRITERIA_ID,NAME,CODE,CRITERIA_SCHEME,CONFIG_VERSION from t_ext_point_criteria where EXT_POINT_CRITERIA_ID=?";
		 
	//	String sqlType ="select t_ticket_payment_id,payment_name,payment_hash from T_TICKET_PAYMENT_TYPE t where PAYMENT_HASH=?";//查询出支付方式
	//	String sqlup="";
		
		//1 查询t_ext_point_criteria 表的 ID 得到JSON 串
		//CriteriaQueryService service = new CriteriaQueryServiceImpl(); 
		//service.getSegmentQuery(。。。）;
		//CreateContactHistoryDAO
		//得到SQL 去另一个库中把内容查出来
		//之后把内容添加进去 	要查两个方法 一个是卖品，一个是影票的，那个不为NULL 就执行那个
		//之后改变RULE表的状态
		//modfiy by lining  start 
//		String ticketsql="    select   TICKET_TRANS_DETAIL_ID,TICKET_TYPE_NAME,TICKET_TYPE_CODE,AMOUNT,PAYMENT_HASH,TICKET_NO,TRANS_ORDER_ID,ORDER_ID,CINEMA_INNER_CODE,BIZ_DATE  "
//  +",POINT,MEMBER_ID,HIS_MEMBER_NO ,LEVEL_POINT,ACTIVITY_POINT,IS_POINT,IS_TICKET,EXT_POINT_RULE_ID from T_TICKET_TRANS_DETAIL t  where TRANS_ORDER_ID=? and  CINEMA_INNER_CODE=?  ";
		String ticketsql="    select   TICKET_TRANS_DETAIL_ID,TICKET_TYPE_NAME,TICKET_TYPE_CODE,AMOUNT,PAYMENT_HASH,TICKET_NO,TRANS_ORDER_ID,ORDER_ID,CINEMA_INNER_CODE,BIZ_DATE  "
				  +",POINT,MEMBER_ID,HIS_MEMBER_NO ,LEVEL_POINT,ACTIVITY_POINT,IS_POINT,IS_TICKET,EXT_POINT_RULE_ID from T_TICKET_TRANS_DETAIL t  where TRANS_ORDER_ID=? and TICKET_NO=? AND  CINEMA_INNER_CODE=?  ";
		//modify by lining end  由order查询改为 ticket查询			
		
		String  consql=" 	select   T_GOODS_TRANS_DETAIL_ID,GOODS_NAME,AMONT,TRANS_TYPE,TRANS_TIME,GODDS_SUD,GOODS_COUNT,POS_ORDER_ID,ORDER_ID,BIZ_DATE,POINT,PAYMENT_HASH "
			+"  ,LEVEL_POINT ,ACTIVITY_POINT,IS_POINT,IS_GOODS,EXT_POINT_RULE_ID,MEMBER_ID,GOODS_ID  from T_GOODS_TRANS_DETAIL t  where TRANS_ORDER_ID=? and  CINEMA_INNER_CODE=?";
		
		String insertSqltmp="INSERT INTO TMP_EXT_TRANS_DETAIL(TMP_EXT_TRANS_DETAIL_ID,TRANS_DETAIL_TYPE,TRANS_DETAIL_CODE,EXT_POINT_RULE_ID,CINEMA_INNER_CODE,MEMBER_ID,BK_CT_ORDER_CODE,ORDER_ID)VALUES(S_TMP_EXT_TRANS_DETAIL.nextVal,?,?,?,?,?,?,?)";
		
	 
		Field idfield=fieldset.getFieldByName("EXT_POINT_RULE_ID");
		Field eidfield=fieldset.getFieldByName("EXT_POINT_CRITERIA_ID");
		Field sfield=fieldset.getFieldByName("STATUS");
		Field cinemaFiled = fieldset.getFieldByName("CINEMA");
		Field bizDateFiled = fieldset.getFieldByName("BIZ_DATE");
		//  wangshua
		String status ="30";
//		Connection con = DBConnection(); 
	 //	System.out.println("查询有效的特殊积分规则对应的积分条件:"+sqlFilm);
	 //	System.out.println("对应条件："+eidfield.destValue);
		ResultQuery rq= SqlHelp.query(conn, sqlFilm,eidfield.destValue);
		log.info("查询有效的特殊积分规则对应的积分条件:"+sqlFilm);
		ResultSet rs= rq.getResultSet();
		try {
			if(rs!=null&&rs.next()){
				//delete by lining start 2013-11-23
//					String datemap =AllMbrPoint.map.get("DATE");
//					String cinemamap =AllMbrPoint.map.get("CINEMA");
				
				String datemap =bizDateFiled.destValue;
				String cinemamap =cinemaFiled.destValue;
					//delete by lining end 2013-11-23
				//	System.out.println("datemap:"+datemap);
				//	System.out.println("cinemamap:"+cinemamap);
				//	mfield.destValue=
					String code = rs.getString("CODE");
					String json = rs.getString("CRITERIA_SCHEME");
				 
					

			
			CriteriaQueryService service = new CriteriaQueryServiceImpl();
			List<ExpressionCriterion> SaleCriteria = JsonCriteriaHelper.parse(json);
			CriteriaResult cqrcon = service.getExtPointConSaleQuery(SaleCriteria);
			
			if(cqrcon!=null){
			
			String sqlcon = cqrcon.getComposedText();
			sqlcon =sqlcon+"   and consale_cinema.INNER_CODE = '"+cinemamap+"'  and   consale.BOOK_BIZ_DATE_KEY=to_date(' "+datemap+"', 'yyyy-mm-dd')  group by consale_cinema.INNER_CODE,member.MEMBER_KEY,consale.BK_CS_ORDER_CODE,consale_item.ITEM_CODE, consale.BOOK_BIZ_DATE_KEY ";
			List<Object> conparlist = cqrcon.getParameters();
		//	List<DataType> conparlisttype =	cqrcon.getParameterTypes();
			StringBuffer consb = new StringBuffer();
			
//			String[] con =sz.split(","); 
		 	LogUtils.debug("卖品SQL:"+sqlcon);
 		 	LogUtils.debug("卖品条件:"+conparlist.toString());	
 			//added by fby
 			
 			ResultQuery dwconrq= SqlHelp.query(conndw, sqlcon,conparlist);
 			ResultSet dwconrs= dwconrq.getResultSet();

 			while(dwconrs!=null&&dwconrs.next()){
 				
 				//添加 卖品
 				String bk =dwconrs.getString("BK_CS_ORDER_CODE");
 				String incon =dwconrs.getString("INNER_CODE");
 				String mid =dwconrs.getString("MEMBER_KEY");
 				String bkcode=dwconrs.getString("ITEM_CODE");
 				
 				
				ResultQuery ticketrq= SqlHelp.query(conn, consql, bk,incon);
				log.info("根据订单获取票房的SQL:"+consql);
 	 			ResultSet ticketrs= ticketrq.getResultSet();
 	 			String order_id="";
 	 			while(ticketrs!=null&&ticketrs.next()){
 	 				order_id=ticketrs.getString("ORDER_ID");
 	 				String[] ypnames = {"con",bkcode,idfield.destValue,incon,mid,bk,order_id};
 	 				log.info("instert SQL:"+insertSqltmp);
 	  				SqlHelp.operate(conn, insertSqltmp, ypnames);
 	 			}
 				//
 		//		String[] ypnames1 = {"S_TMP_EXT_TRANS_DETAIL.nextVal","ticket:票 con:品项","票号或品项ID","规则ID","影城内码","会员ID","BK_CT_ORDER_CODE"};
  				ticketrq.free();
 				
 			}
 			dwconrq.free();
 		
			}
			 
//			System.out.println(sql );
//			 System.out.println(sb.toString() );
//			 //如果查出的数据不为NULL那么添加
//			 String ss = sb.toString();
//			 System.out.println(ss);
//			 String[] yp = ss.split(",");
		//	System.out.println(json);
			List<ExpressionCriterion> ticketCriteria = JsonCriteriaHelper.parse(json);
			CriteriaResult cqrticket = service.getExtPointTicketQuery(ticketCriteria);

 			if(cqrticket!=null){
			String sqlaa = cqrticket.getComposedText();
			sqlaa =sqlaa+"   and transSales_cinema.INNER_CODE= '"+cinemamap+"'  and  transSales.SHOW_BIZ_DATE_KEY=to_date(' "+datemap+"', 'yyyy-mm-dd')  group by transSales_cinema.INNER_CODE,member.MEMBER_KEY,transSales.BK_CT_ORDER_CODE,transSales.BK_TICKET_NUMBER,transSales.SHOW_BIZ_DATE_KEY ";
			List<Object> parlist = cqrticket.getParameters();
		//	List<DataType> parlisttype =	cqrcon.getParameterTypes();
			LogUtils.debug("影票SQL:"+sqlaa);
			LogUtils.debug("影票条件:"+parlist.toString());
			StringBuffer sb = new StringBuffer();
		 
 			ResultQuery dwrq= SqlHelp.query(conndw,sqlaa,parlist);
 			ResultSet dwrs= dwrq.getResultSet();
 			Set<String[]> insertSet = new HashSet<String[]>();
 			while(dwrs!=null&&dwrs.next()){
// 				ResultSetMetaData rsData = dwrq.getResultSet().getMetaData();
// 					for (int i = 0; i < rsData.getColumnCount(); i++) {
// 						System.out.println(rsData.getColumnName(i+1));// 
// 					}
// 					//添加影票
 				String bk =dwrs.getString("BK_CT_ORDER_CODE");
 				String incon =dwrs.getString("INNER_CODE");
 				String mid =dwrs.getString("MEMBER_KEY");
 				String bkcode=dwrs.getString("BK_TICKET_NUMBER");
 				
 				
 				ResultQuery ticketrq= SqlHelp.query(conn, ticketsql, bk,bkcode,incon);
 				log.info("ticketsql:"+ticketsql);
 	 			ResultSet ticketrs= ticketrq.getResultSet();
 	 			if("830_17906958827175742".equals(bk)){
 	 				System.out.println("830_17906958827175742");
 	 			}
 	 			while(ticketrs!=null&&ticketrs.next()){
 	 				String order_id=ticketrs.getString("ORDER_ID");
 	 				String ticketNo = ticketrs.getString("TICKET_NO");
 	 				order_id=ticketrs.getString("ORDER_ID");
 	 				String[] ypnames1 = {"S_TMP_EXT_TRANS_DETAIL.nextVal","ticket:票 con:品项","票号或品项ID","规则ID","影城内码","会员ID","BK_CT_ORDER_CODE","订单号"};
 	 				//String insertSqltmp="INSERT INTO TMP_EXT_TRANS_DETAIL(TMP_EXT_TRANS_DETAIL_ID,TRANS_DETAIL_TYPE,TRANS_DETAIL_CODE,EXT_POINT_RULE_ID,CINEMA_INNER_CODE,MEMBER_ID,BK_CT_ORDER_CODE,ORDER_ID)VALUES(S_TMP_EXT_TRANS_DETAIL.nextVal,?,?,?,?,?,?,?)";
// 	 				String[] ypnames = {"ticket",bkcode,idfield.destValue,incon,mid,bk,order_id};
 	 				
 	 				String[] ypnames = {"ticket",ticketNo,idfield.destValue,incon,mid,bk,order_id};
 	 				insertSet.add(ypnames); 	  				
 	 			}
 	 			LogUtils.info(insertSet.size());
 	 			for(String[] insertParm :insertSet){
 	 				SqlHelp.operate(conn, insertSqltmp, insertParm);
 	 			}
 	 			insertSet.clear();
 				//
 				
  				ticketrq.free();
 			}
 			
 			dwrq.free();
 
//				if(sfield.destValue.equals("20")){
//					sfield.destValue=status;
//				}
				
				r=1;
			}	
		}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{
			rq.free();
		}
		return 0;
	}
	public Connection  DBConnection(){
 		Connection connect = null;
		try {
 			
 			String driver = "oracle.jdbc.driver.OracleDriver";
 			String  url = "jdbc:oracle:thin:@10.199.201.39:1521:orapub";
 			String name  = "ccs_mbr_stag";
 			String psw  = "ccs_mbr_stag";
 	
			Class.forName(driver); //加载驱动类包 new一个Driver 向我们的DriverManager注册
//			new com.mysql.jdbc.Driver();
			System.out.println(url+"  url  "+name+"   "+psw);
		//	logger.info(url+"  url  "+name+"   "+psw);
			connect=DriverManager.getConnection(url, name, psw);	//通过DriverManager的getConnection方法向它传递JDBC连接
			//的URL并给这个连接用户名和密码连接指定的数据库并返回连接对象
			if (connect!=null) {
			//	logger.info("数据库连接成功");
				System.out.println("数据库连接成功");
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connect;
 	}
	
	private void streamFree(Reader jsonReader, BufferedReader bufferedReader) {
		try {
			if (jsonReader != null)
				jsonReader.close();
			if (bufferedReader != null )
				bufferedReader.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (jsonReader != null)
					jsonReader.close();
				if (bufferedReader != null )
					bufferedReader.close();
			} catch (IOException ioefinally) {
				ioefinally.printStackTrace();
			}
		}
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
