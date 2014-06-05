package com.wanda.mms.control.stream.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.wanda.mms.control.stream.dao.DayTableDao;
import com.wanda.mms.control.stream.dao.impl.DayTableDaoImpl;
import com.wanda.mms.control.stream.db.STAGDBConnection;
import com.wanda.mms.control.stream.vo.CinemaString;
import com.wanda.mms.control.stream.vo.DayTable;

public class DayTableService {
	public static void main(String[] args) {
		DayTableService dd= new DayTableService();
		
		Calendar cal=Calendar.getInstance();
		int x=-28;//or x=-3;
		cal.add(Calendar.DAY_OF_MONTH,x);
		String bzda=new java.text.SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
		System.out.println("Bizdate:"+bzda);
		STAGDBConnection db=STAGDBConnection.getInstance();	
		 
		DayTable dt =	dd.fandByDay(db.getConnection(),bzda);
		String ss = dd.dayTabletoString(dt, bzda);
		//System.out.println( "线上新增会员数 :"+ dt.getNetMembersCount());
	}
	
	public String dayTabletoString(DayTable dd,String date){
		
		String[] sp =date.split("-");
		for (int i = 0; i < sp.length; i++) {
			System.out.println(sp[i]);
		}
		String ss =sp[1]+"月"+sp[2]+"日";
		
		String text="";
		StringBuffer sb = new StringBuffer();
		sb.append("各位领导，以下为万达会员系统").append(ss).append("数据统计，请查收。").append("\n");
		sb.append("新增会员数：").append(dd.getNewMembers()).append("("+dd.getNewMembersCount()+")").append("\n");//ok
		sb.append("线上入会数：").append(dd.getNetMembers()).append("("+dd.getNetMembersCount()+")").append("\n");//ok
		sb.append("线下入会数：").append(dd.getNotNetMembers()).append("("+dd.getNotNetMembersCount()+")").append("\n");//ok
		sb.append("线上票房消费金额：").append(dd.getNetTicketSum()).append("(本月累计"+dd.getNetTicketSumMonth()+"，本年累计"+dd.getNetTicketSumYear()+")").append("\n");//ok
		sb.append("线下票房消费金额：").append(dd.getNotNetTicketSum()).append("(本月累计"+dd.getNotNetTicketSumMonth()+"，本年累计"+dd.getNotNetTicketSumYear()+")").append("\n");//ok
	//	sb.append("线上卖品消费金额：").append(dd.getNetGoodsSum()).append("("+dd.getNetGoddsSumCount()+")").append("\n");//ok
		sb.append("卖品消费金额：").append(dd.getNotNetGoodsSum()).append("\n");//ok
	//	sb.append("线上会员累计积分：").append(dd.getNetPoint()).append("("+dd.getNetPointcount()+")").append("\n");
	//	sb.append("线下会员累计积分：").append(dd.getNotNetPoint()).append("("+dd.getNotNetPointcount()+")").append("\n");
		sb.append("可兑换积分增加：").append(dd.getExchangePointnew()).append("\n");//ok
		sb.append("可兑换积分兑换：").append(dd.getExchangePoint()).append("\n");//ok
		sb.append("可兑换积分余额：").append(dd.getExchangePointBalance()).append("\n");//ok
		 System.out.println(sb.toString());
		 text=sb.toString();
//		dd.getNewMembers( );//新增会员数
//		dd.getNewMembersCount( );//总会员数
//		dd.getNetMembers();//线上新增会员数
//		dd.getNetMembersCount( );//线上新增会员总数
//		dd.getNotNetMembers( );  // 线下新增会员数
//		dd.getNotNetMembersCount( ); //   线下新增会员总数
//		dd.getNetTicketSum( );//线上会员票房    
//		dd.getNetTicketSumCount( );//线上会员总票房    
//		dd.getNotNetTicketSum( );  // 线下会员票房
//		dd.getNotNetTicketSumCount( );  // 线下会员总票房
//		dd.getNetGoodsSum( ); //线上会员卖品   
//		dd.getNetGoddsSumCount( ); //线上会员总卖品   
//		dd.getNotNetGoodsSum( ); //线下会员卖品 
//		dd.getNotNetGoddsSumCount(  ); //线下会员总卖品 
//		dd.getNetPoint(  );//      线上新增积分 
//		dd.getNotNetPoint( );  //线下新增积分
//		dd.getNetPointcount( );//     线上总积分    
//		dd.getNotNetPointcount( ); //线下总积分
//		//exchangePointsql
//		dd.getExchangePoint();//      兑换积分
		
		
//		 各位领导，以下为万达会员系统4月21日数据统计，请查收。 
//		 新增会员数：14151(4361552) --括号中会员总量
//		 线上入会数：3010(665181) --括号中线上会员总量
//		 线下入会数：11141(3607578) --括号中线下会员总量
//		 线上票房消费金额：38546（本月累计**，本年累计**）
//		 线下票房消费金额：5503102（本月累计**，本年累计**）
//		 卖品消费金额：695 
//		 可兑换积分增加：43243
//		 可兑换积分兑换：9666
//		 可兑换积分余额：78524872 
		
		return text;
	}
	
	public List<CinemaString> dayTabletoCinemaString(List<DayTable> ldt,String date){
		
		String[] sp =date.split("-");
		for (int i = 0; i < sp.length; i++) {
			System.out.println(sp[i]);
		}
		String ss =sp[1]+"月"+sp[2]+"日";
		
		String text="";
		List<CinemaString> cslist = new ArrayList<CinemaString>();
		for (int i = 0; i < ldt.size(); i++) {
			StringBuffer sb = new StringBuffer();
			DayTable dd =ldt.get(i);
			CinemaString cs = new CinemaString();
			sb.append("各位领导，以下为万达会员系统").append(ss).append("数据统计，请查收。").append("\n");
			sb.append("新增会员数：").append(dd.getNewMembers()).append("("+dd.getNewMembersCount()+")").append("\n");//ok
			sb.append("线上入会数：").append(dd.getNetMembers()).append("("+dd.getNetMembersCount()+")").append("\n");//ok
			sb.append("线下入会数：").append(dd.getNotNetMembers()).append("("+dd.getNotNetMembersCount()+")").append("\n");//ok
			sb.append("线上票房消费金额：").append(dd.getNetTicketSum()).append("(本月累计"+dd.getNetTicketSumMonth()+"，本年累计"+dd.getNetTicketSumYear()+")").append("\n");//ok
			sb.append("线下票房消费金额：").append(dd.getNotNetTicketSum()).append("(本月累计"+dd.getNotNetTicketSumMonth()+"，本年累计"+dd.getNotNetTicketSumYear()+")").append("\n");//ok
		//	sb.append("线上卖品消费金额：").append(dd.getNetGoodsSum()).append("("+dd.getNetGoddsSumCount()+")").append("\n");//ok
			sb.append("卖品消费金额：").append(dd.getNotNetGoodsSum()).append("\n");//ok
		//	sb.append("线上会员累计积分：").append(dd.getNetPoint()).append("("+dd.getNetPointcount()+")").append("\n");
		//	sb.append("线下会员累计积分：").append(dd.getNotNetPoint()).append("("+dd.getNotNetPointcount()+")").append("\n");
			sb.append("可兑换积分增加：").append(dd.getExchangePointnew()).append("\n");//ok
			sb.append("可兑换积分兑换：").append(dd.getExchangePoint()).append("\n");//ok
			sb.append("可兑换积分余额：").append(dd.getExchangePointBalance()).append("\n");//ok
			 System.out.println(sb.toString());
			 cs.setContent(sb.toString());
			 cs.setCinema_inner_code(dd.getCinema_inner_code());
			 cslist.add(cs);
			 
//			
			
		}
		 
//		dd.getNewMembers( );//新增会员数
//		dd.getNewMembersCount( );//总会员数
//		dd.getNetMembers();//线上新增会员数
//		dd.getNetMembersCount( );//线上新增会员总数
//		dd.getNotNetMembers( );  // 线下新增会员数
//		dd.getNotNetMembersCount( ); //   线下新增会员总数
//		dd.getNetTicketSum( );//线上会员票房    
//		dd.getNetTicketSumCount( );//线上会员总票房    
//		dd.getNotNetTicketSum( );  // 线下会员票房
//		dd.getNotNetTicketSumCount( );  // 线下会员总票房
//		dd.getNetGoodsSum( ); //线上会员卖品   
//		dd.getNetGoddsSumCount( ); //线上会员总卖品   
//		dd.getNotNetGoodsSum( ); //线下会员卖品 
//		dd.getNotNetGoddsSumCount(  ); //线下会员总卖品 
//		dd.getNetPoint(  );//      线上新增积分 
//		dd.getNotNetPoint( );  //线下新增积分
//		dd.getNetPointcount( );//     线上总积分    
//		dd.getNotNetPointcount( ); //线下总积分
//		//exchangePointsql
//		dd.getExchangePoint();//      兑换积分
		
		
//		 各位领导，以下为万达会员系统4月21日数据统计，请查收。 
//		 新增会员数：14151(4361552) --括号中会员总量
//		 线上入会数：3010(665181) --括号中线上会员总量
//		 线下入会数：11141(3607578) --括号中线下会员总量
//		 线上票房消费金额：38546（本月累计**，本年累计**）
//		 线下票房消费金额：5503102（本月累计**，本年累计**）
//		 卖品消费金额：695 
//		 可兑换积分增加：43243
//		 可兑换积分兑换：9666
//		 可兑换积分余额：78524872 
		
		return cslist;
	}

	public List<DayTable> fandByDayCinema(Connection con ,String date ,List<DayTable> ldt){

		 
		DayTableDao ddao = new DayTableDaoImpl();
		//fandDayInnerCodeList
		//NEWMEMBERS,NEWMEMBERSCOUNT,NETMEMBERS,NETMEMBERSCOUNT,NOTNETMEMBERS,NOTNETMEMBERSCOUNT
		//String 
//		+---------------------------------------------------------会员数---------------------------------    "        
//		+                                                                                                    "        
//		+"-----------------------------------------                                                          "        
		String numsql =	 "  SELECT d.INNER_CODE, sum(RECRUIT_MEMBER_COUNT) NEWMEMBERS    "    // 新增会员数   
			+",sum(MEMBER_COUNT) NEWMEMBERSCOUNT      "        //总会员数 
			+",sum(case when c.CHANNEL_CODE in('05','06','07') then RECRUIT_MEMBER_COUNT else 0 end ) NETMEMBERS "     //   线上新增会员数           
			+",sum(case when c.CHANNEL_CODE in('05','06','07') then MEMBER_COUNT else 0 end ) NETMEMBERSCOUNT     "       // 线上新增会员数 
			+",sum(case when c.CHANNEL_CODE in('05','06','07') then 0 else RECRUIT_MEMBER_COUNT end ) NOTNETMEMBERS   "       // 线下新增会员数
			+",sum(case when c.CHANNEL_CODE in('05','06','07') then 0 else MEMBER_COUNT end ) NOTNETMEMBERSCOUNT     "     //   线下新增会员数
			+"FROM CCS_NRPT2.T_F_CON_VIP_RECRUIT a                                                                "   //     
			+"inner join  CCS_NRPT2.T_D_CON_MBR_STATUS b on a.STATUS_KEY = b.STATUS_KEY     inner join CCS_NRPT2.T_D_CON_CHANNEL c on a.CHANNEL_KEY = c.CHANNEL_KEY      inner join CCS_NRPT2.T_D_CON_CINEMA d on a.CINEMA_KEY = d.CINEMA_KEY                               "     //   
			+"inner join CCS_NRPT2.T_D_CON_CHANNEL c on a.CHANNEL_KEY = c.CHANNEL_KEY      inner join CCS_NRPT2.T_D_CON_CINEMA b  on a.CINEMA_KEY = b.CINEMA_KEY                         "       // 
			+"where BIZ_DATE_KEY  < to_date(?, 'yyyy-mm-dd') + 1 and BIZ_DATE_KEY >= to_date(?, 'yyyy-mm-dd')                                           "        // '2013-07-02'
			+"and b.STATUS_CODE = '1'         group by d.INNER_CODE                                                                          ";        //
//		+"--------------------------------------------------------票房------------------------------------   "        //
//		+"                                                                                                   "        //
//		+"------------------------------------------------                                                   "        //
		String ticketsql = "select b.INNER_CODE, sum(case when c.CHANNEL_CODE in('05','06','07') and MEMBER_KEY <> -999 then BK_ADMISSIONS   "//        
			+"                                                                                                   "        //
			+"else 0 end ) - sum(case when c.CHANNEL_CODE in('05','06','07')  and MEMBER_KEY <> -999 then        "        //
			+"                                                                                                   "        //
			+"RE_ADMISSIONS else 0 end) NETTICKETSUM                                                          "        // 线上会员票房    
			+",sum(case when c.CHANNEL_CODE in('05','06','07') then BK_ADMISSIONS else 0 end ) - sum(case when   "        //
			+"                                                                                                   "       // 
			+"c.CHANNEL_CODE in('05','06','07') then RE_ADMISSIONS else 0 end) NETTICKETSUMCOUNT                         "        //线上会员总票房
			+",sum(case when c.CHANNEL_CODE not in('05','06','07') and MEMBER_KEY <> -999 then BK_ADMISSIONS else 0 end ) - sum(case when c.CHANNEL_CODE not in('05','06','07')  and MEMBER_KEY <> -999 then RE_ADMISSIONS else 0 end) NOTNETTICKETSUM               "        // 线下会员票房   
			+",sum(case when c.CHANNEL_CODE  not in('05','06','07') then BK_ADMISSIONS else 0 end ) - sum(case when c.CHANNEL_CODE not in('05','06','07') then RE_ADMISSIONS else 0 end) NOTNETTICKETSUMCOUNT                         "        // 线下会员总票房
			+"FROM CCS_NRPT2.T_F_CON_TICKET a                                                                     "        //
			+"inner join  CCS_NRPT2.T_D_CON_CHANNEL c on a.CHANNEL_KEY = c.CHANNEL_KEY    inner join CCS_NRPT2.T_D_CON_CINEMA b  on a.CINEMA_KEY = b.CINEMA_KEY                         "        //
			+"where  SHOW_DATE_KEY  < to_date(?, 'yyyy-mm-dd') + 1 and SHOW_DATE_KEY >= to_date(?, 'yyyy-mm-dd')         group by b.INNER_CODE                                 " ;       //'2013-07-02'
//		+"----------------------------------------------------------卖品----------------------------------   "        //
//		+"                                                                                                   "        //
//		+"-----------------------------------------------------------------------                            "        //
		String goodssql="select b.INNER_CODE, sum(case when c.CHANNEL_CODE in('05','06','07') and MEMBER_KEY <> -999 then BK_SALE_AMOUNT  "  //      
			+"                                                                                                   "        //
			+"else 0 end ) - sum(case when c.CHANNEL_CODE in('05','06','07')  and MEMBER_KEY <> -999 then        "        //
			+"                                                                                                   "        //
			+"RE_SALE_AMOUNT else 0 end) NETGOODSSUM                                                            "        // 线上会员卖品   
			+",sum(case when c.CHANNEL_CODE in('05','06','07') then BK_SALE_AMOUNT else 0 end ) - sum(case when  "        //
			+"                                                                                                   "        //
			+"c.CHANNEL_CODE in('05','06','07') then RE_SALE_AMOUNT else 0 end) NETGODDSSUMCOUNT                         "        //线上会员总卖品   
			+",sum(case when c.CHANNEL_CODE in('05','06','07') and MEMBER_KEY <> -999 then 0 else                "        //
			+"                                                                                                   "        //
			+"BK_SALE_AMOUNT end ) - sum(case when c.CHANNEL_CODE in('05','06','07')  and MEMBER_KEY <> -999     "        //
			+"                                                                                                   "        //
			+"then 0 else RE_SALE_AMOUNT end) NOTNETGOODSSUM                                                       "        // 线下会员卖品   
			+",sum(case when c.CHANNEL_CODE in('05','06','07') then 0 else BK_SALE_AMOUNT end ) - sum(case when  "        //
			+"                                                                                                   "      //  
			+"c.CHANNEL_CODE in('05','06','07') then 0 else RE_SALE_AMOUNT end) NOTNETGODDSSUMCOUNT                         "        //线下会员总卖品 
			+"FROM CCS_NRPT2.T_F_CON_SALE a                                                                       "        //
			+"inner join  CCS_NRPT2.T_D_CON_CHANNEL c on a.CHANNEL_KEY = c.CHANNEL_KEY    inner join CCS_NRPT2.T_D_CON_CINEMA b  on a.CINEMA_KEY = b.CINEMA_KEY                           "        //
			+"where  BOOK_BIZ_DATE_KEY  < to_date(?, 'yyyy-mm-dd') + 1 and BOOK_BIZ_DATE_KEY >= to_date(?, 'yyyy-mm-dd')           group by b.INNER_CODE                                    ";        //'2013-07-02'
//		+"--------------------------------------------------------新增积分--------------------------------   "        //
//		+"                                                                                                   "        //
//		+"----------------------------------------------------------------------------------                 "        //
		String pointsql = "select d.INNER_CODE, sum(case when c.CHANNEL_CODE in('05','06','07') and b.POINT_TYPE_CODE <> 6 then NEW_CREDID else 0 end ) NETPOINT    " //      线上新增积分 
			+", sum(case when c.CHANNEL_CODE not in('05','06','07') and b.POINT_TYPE_CODE <> 6 then NEW_CREDID else 0 end ) NOTNETPOINT        "        //线下新增积分
			+"FROM CCS_NRPT2.T_F_CON_VIP_CREDIT a  inner join  CCS_NRPT2.T_D_CON_CHANNEL c on a.CHANNEL_KEY = c.CHANNEL_KEY         "  //      
			+"inner join CCS_NRPT2.T_D_CON_POINT_TYPE b on a.POINT_TYPE_KEY = b.POINT_TYPE_KEY    inner join CCS_NRPT2.T_D_CON_CINEMA d  on a.CINEMA_KEY = d.CINEMA_KEY                           "        //
			+"where  BIZ_DATE_KEY  < to_date(?, 'yyyy-mm-dd') + 1 and BIZ_DATE_KEY >= to_date(?, 'yyyy-mm-dd')               group by d.INNER_CODE                               "   ;     //'2013-07-02' 
//		+"---------------------------------------------------------累计积分-------------------------------   "        //
//		+"                                                                                                   "        //
//		+"----------------------------------------------------------------------------                       "        //
		String countsql = "select  b.INNER_CODE, sum(case when c.CHANNEL_CODE in('05','06','07') then TOTAL_CREDIT else 0 end ) NETPOINTCOUNT  "//     线上总积分    
			+", sum(case when c.CHANNEL_CODE in('05','06','07') then 0 else TOTAL_CREDIT end ) NOTNETPOINTCOUNT        "        //线下总积分
			+"FROM CCS_NRPT2.T_F_CON_VIP_CREDIT_TOTAL a                                                           "  //      
			+"inner join  CCS_NRPT2.T_D_CON_CHANNEL c on a.CHANNEL_KEY = c.CHANNEL_KEY        inner join CCS_NRPT2.T_D_CON_CINEMA b  on a.CINEMA_KEY = b.CINEMA_KEY                      "        //
			+"where  BIZ_DATE_KEY  < to_date(?, 'yyyy-mm-dd') + 1 and BIZ_DATE_KEY >= to_date(?, 'yyyy-mm-dd')          group by b.INNER_CODE                                  ";        //'2013-07-02'
//		+"-----------------------------------------------------------可兑换积分兑换：9666-----------------------------   "        //
//		+"                                                                                                   "        //
//		+"-------------------------------------------------------------------------                          "        //
		String exchangePointsql="select  b.INNER_CODE, sum(CONNERT_CREDIT) EXCHANGEPOINT   FROM CCS_NRPT2.T_F_CON_VIP_CONVERT  a    inner join  CCS_NRPT2.T_D_CON_CHANNEL c on a.CHANNEL_KEY = c.CHANNEL_KEY  inner join CCS_NRPT2.T_D_CON_CINEMA b  on a.CINEMA_KEY = b.CINEMA_KEY                           "  //      兑换积分
			+"where  BIZ_DATE_KEY  < to_date(?, 'yyyy-mm-dd') + 1 and BIZ_DATE_KEY >= to_date(?, 'yyyy-mm-dd')    group by b.INNER_CODE                                          "        ;//
			//NEWMEMBERS,NEWMEMBERSCOUNT,NETMEMBERS,NETMEMBERSCOUNT,NOTNETMEMBERS,NOTNETMEMBERSCOUNT
	
		//在这加一个月的查询，年的查询
		
		//可兑换积分增加：43243
		String exchangePointnewsql="SELECT b.INNER_CODE,  sum(a.NEW_CREDID) EXCHANGEPOINTNEW FROM CCS_NRPT2.T_F_CON_VIP_CREDIT a  inner join CCS_NRPT2.T_D_CON_CINEMA b  on a.CINEMA_KEY = b.CINEMA_KEY  where a.POINT_TYPE_KEY <> 6 and a.BIZ_DATE_KEY  < to_date(?, 'yyyy-mm-dd') + 1 and a.BIZ_DATE_KEY >=  to_date(?, 'yyyy-mm-dd')group by b.INNER_CODE      ";
		
		//可兑换积分余额：78524872
		String exchangePointBalancesql="SELECT  b.INNER_CODE, sum(RESERVE_CREDIT) EXCHANGEPOINTBALANCE FROM CCS_NRPT2.T_F_CON_VIP_CREDIT_TOTAL a  inner join CCS_NRPT2.T_D_CON_CINEMA b  on a.CINEMA_KEY = b.CINEMA_KEY    where BIZ_DATE_KEY  < to_date(?, 'yyyy-mm-dd') + 1 and BIZ_DATE_KEY >= to_date(?, 'yyyy-mm-dd')    group by b.INNER_CODE   ";
		
		//会员月票房累计，以后分线上，线下
		String ticketMonthsql = "select  b.INNER_CODE,sum(case when c.CHANNEL_CODE in('05','06','07') and MEMBER_KEY <> -999 then BK_ADMISSIONS   "//        
			+"                                                                                                   "        //
			+"else 0 end ) - sum(case when c.CHANNEL_CODE in('05','06','07')  and MEMBER_KEY <> -999 then        "        //
			+"                                                                                                   "        //
			+"RE_ADMISSIONS else 0 end) NETTICKETSUMMONTH                                                          "        // 线上月会员票房    
			+",sum(case when c.CHANNEL_CODE not in('05','06','07') and MEMBER_KEY <> -999 then BK_ADMISSIONS else 0 end ) - sum(case when c.CHANNEL_CODE not in('05','06','07')  and MEMBER_KEY <> -999 then RE_ADMISSIONS else 0 end) NOTNETTICKETSUMMONTH               "        // 线下月会员票房   
			+"FROM CCS_NRPT2.T_F_CON_TICKET a                                                                     "        //
			+"inner join  CCS_NRPT2.T_D_CON_CHANNEL c on a.CHANNEL_KEY = c.CHANNEL_KEY    inner join CCS_NRPT2.T_D_CON_CINEMA b  on a.CINEMA_KEY = b.CINEMA_KEY                               "        //
			+"where  SHOW_DATE_KEY  < to_date(?, 'yyyy-mm-dd')  and SHOW_DATE_KEY >= to_date(?, 'yyyy-mm-dd')            group by b.INNER_CODE                                  " ;       //'2013-07-02'

		//会员年票房累计，以后分线上，线下
		String ticketYearsql = "select  b.INNER_CODE,sum(case when c.CHANNEL_CODE in('05','06','07') and MEMBER_KEY <> -999 then BK_ADMISSIONS   "//        
			+"                                                                                                   "        //
			+"else 0 end ) - sum(case when c.CHANNEL_CODE in('05','06','07')  and MEMBER_KEY <> -999 then        "        //
			+"                                                                                                   "        //
			+"RE_ADMISSIONS else 0 end) NETTICKETSUMYEAR                                                          "        // 线上年会员票房    
			+",sum(case when c.CHANNEL_CODE not in('05','06','07') and MEMBER_KEY <> -999 then BK_ADMISSIONS else 0 end ) - sum(case when c.CHANNEL_CODE not in('05','06','07')  and MEMBER_KEY <> -999 then RE_ADMISSIONS else 0 end) NOTNETTICKETSUMYEAR               "        // 线下会员票房   
			+"FROM CCS_NRPT2.T_F_CON_TICKET a                                                                     "        //
			+"inner join  CCS_NRPT2.T_D_CON_CHANNEL c on a.CHANNEL_KEY = c.CHANNEL_KEY     inner join CCS_NRPT2.T_D_CON_CINEMA b  on a.CINEMA_KEY = b.CINEMA_KEY                             "        //
			+"where  SHOW_DATE_KEY  < to_date(?, 'yyyy-mm-dd') + 1 and SHOW_DATE_KEY >= to_date(?, 'yyyy-mm-dd')      group by b.INNER_CODE                                        " ; 
			//先把传进来的list接到 在把查出来的数据按影城内码来添加回去
		String type1 ="1";
		
		List<DayTable> ddb1 =ddao.fandDayTableBynewMembersList(con, numsql, date,date,type1);
		for (int i = 0; i < ldt.size(); i++) {
			DayTable ddc = ldt.get(i);
			for (int j = 0; j < ddb1.size(); j++) {
				DayTable dd1= ddb1.get(j);
				if(dd1.getCinema_inner_code().equals(ddc.getCinema_inner_code())){
				ddc.setNewMembers(dd1.getNewMembers());//新增会员数
				ddc.setNewMembersCount(dd1.getNewMembersCount());//总会员数
				ddc.setNetMembers(dd1.getNetMembers());//线上新增会员数
				ddc.setNetMembersCount(dd1.getNetMembersCount());//线上新增会员总数
				ddc.setNotNetMembers(dd1.getNotNetMembers());  // 线下新增会员数
				ddc.setNotNetMembersCount(dd1.getNotNetMembersCount()); //   线下新增会员总数
				ldt.set(i, ddc);
				}
			}
		}
	 	
		String type2 ="2";
		List<DayTable> ddb2 =ddao.fandDayTableBynewMembersList(con, ticketsql, date,date,type2);
		for (int i = 0; i < ldt.size(); i++) {
			DayTable ddc = ldt.get(i);
			for (int j = 0; j < ddb2.size(); j++) {
				DayTable dd2= ddb2.get(j);
				if(dd2.getCinema_inner_code().equals(ddc.getCinema_inner_code())){
				ddc.setNetTicketSum(bigdecimalString(dd2.getNetTicketSum()));//线上会员票房    
				ddc.setNetTicketSumCount(bigdecimalString(dd2.getNetTicketSumCount()));//线上会员总票房    
				ddc.setNotNetTicketSum(bigdecimalString(dd2.getNotNetTicketSum()));  // 线下会员票房
				ddc.setNotNetTicketSumCount(bigdecimalString(dd2.getNotNetTicketSumCount()));  // 线下会员总票房
				ldt.set(i, ddc);
					}
			}
		}
		
		String type3 ="3";
		
		List<DayTable> ddb3 =ddao.fandDayTableBynewMembersList(con, goodssql, date,date,type3);
		for (int i = 0; i < ldt.size(); i++) {
			DayTable ddc = ldt.get(i);
			for (int j = 0; j < ddb3.size(); j++) {
				DayTable dd3= ddb3.get(j);
				if(dd3.getCinema_inner_code().equals(ddc.getCinema_inner_code())){
				ddc.setNetGoodsSum(bigdecimalString(dd3.getNetGoodsSum())); //线上会员卖品   
				ddc.setNetGoddsSumCount(bigdecimalString(dd3.getNetGoddsSumCount())); //线上会员总卖品   
				ddc.setNotNetGoodsSum(bigdecimalString(dd3.getNotNetGoodsSum())); //线下会员卖品 
				ddc.setNotNetGoddsSumCount(bigdecimalString(dd3.getNotNetGoddsSumCount())); //线下会员总卖品 
				ldt.set(i, ddc);
			}
	}
}
	
		String type4 ="4";
		List<DayTable> ddb4 =ddao.fandDayTableBynewMembersList(con, pointsql, date,date,type4);
		for (int i = 0; i < ldt.size(); i++) {
			DayTable ddc = ldt.get(i);
			for (int j = 0; j < ddb4.size(); j++) {
				DayTable dd4= ddb4.get(j);
				if(dd4.getCinema_inner_code().equals(ddc.getCinema_inner_code())){
					ddc.setNetPoint(dd4.getNetPoint());//      线上新增积分 
					ddc.setNotNetPoint(dd4.getNotNetPoint());  //线下新增积分
					ldt.set(i, ddc);
			}
	}
}
		
		String type5 ="5";
		List<DayTable> ddb5 =ddao.fandDayTableBynewMembersList(con, countsql, date,date,type5);
		for (int i = 0; i < ldt.size(); i++) {
			DayTable ddc = ldt.get(i);
			for (int j = 0; j < ddb5.size(); j++) {
				DayTable dd5= ddb5.get(j);
				if(dd5.getCinema_inner_code().equals(ddc.getCinema_inner_code())){
		
					ddc.setNetPointcount(dd5.getNetPointcount());//     线上总积分    
					ddc.setNotNetPointcount(dd5.getNotNetPointcount()); //线下总积分
					ldt.set(i, ddc);
				}
		}
	}
		String type6 ="6";
		List<DayTable> ddb6 =ddao.fandDayTableBynewMembersList(con, exchangePointsql,date,date,type6);
		for (int i = 0; i < ldt.size(); i++) {
			DayTable ddc = ldt.get(i);
			for (int j = 0; j < ddb6.size(); j++) {
				DayTable dd6= ddb6.get(j);
				if(dd6.getCinema_inner_code().equals(ddc.getCinema_inner_code())){
	
					ddc.setExchangePoint(dd6.getExchangePoint());//      兑换积分
					ldt.set(i, ddc);
				}
		}
	}
		
		String enddate = enddateString(date);
		String te = "01";
		String ddj =date.substring(0, 8);
		String ddte = ddj+te;
		String type7 ="7";//会员月票房累计，以后分线上，线下
		List<DayTable> ddb7 =ddao.fandDayTableBynewMembersList(con, ticketMonthsql,enddate,ddte,type7);
		for (int i = 0; i < ldt.size(); i++) {
			DayTable ddc = ldt.get(i);
			for (int j = 0; j < ddb7.size(); j++) {
				DayTable dd7= ddb7.get(j);
				if(dd7.getCinema_inner_code().equals(ddc.getCinema_inner_code())){
	
				ddc.setNetTicketSumMonth(bigdecimalString(dd7.getNetTicketSumMonth()));//     
				ddc.setNotNetTicketSumMonth(bigdecimalString(dd7.getNotNetTicketSumMonth()));//  
				ldt.set(i, ddc);
				}
		}
	}
	//	String type8 ="8";//会员月卖品累计，以后分线上，线下
		
		String type9 ="9";//会员年票房累计，以后分线上，线下
		String yearenddate = date.substring(0, 4)+"-12-31";
		String yearstartdate =date.substring(0, 4)+"-01-01";
		 
		List<DayTable> ddb9 =ddao.fandDayTableBynewMembersList(con, ticketYearsql,yearenddate,yearstartdate,type9);
		for (int i = 0; i < ldt.size(); i++) {
			DayTable ddc = ldt.get(i);
			for (int j = 0; j < ddb9.size(); j++) {
				DayTable dd9= ddb9.get(j);
				if(dd9.getCinema_inner_code().equals(ddc.getCinema_inner_code())){
	
				ddc.setNetTicketSumYear(bigdecimalString(dd9.getNetTicketSumYear()));//     
				ddc.setNotNetTicketSumYear(bigdecimalString(dd9.getNotNetTicketSumYear()));//  
				ldt.set(i, ddc);
				}
		}
	}
	//	String type10 ="10";//会员年卖品累计，以后分线上，线下
		
		String type11 ="11";
		//可兑换积分增加：43243
		List<DayTable> ddb11 =ddao.fandDayTableBynewMembersList(con, exchangePointnewsql,date,date,type11);
		for (int i = 0; i < ldt.size(); i++) {
			DayTable ddc = ldt.get(i);
			for (int j = 0; j < ddb11.size(); j++) {
				DayTable dd11= ddb11.get(j);
				if(dd11.getCinema_inner_code().equals(ddc.getCinema_inner_code())){
					ddc.setExchangePointnew(dd11.getExchangePointnew());//     //可兑换积分增加：43243
					ldt.set(i, ddc);
				}
		}
	}
		String type12 ="12";
		//可兑换积分余额：78524872
		List<DayTable> ddb12 =ddao.fandDayTableBynewMembersList(con, exchangePointBalancesql,date,date,type12);
		for (int i = 0; i < ldt.size(); i++) {
			DayTable ddc = ldt.get(i);
			for (int j = 0; j < ddb12.size(); j++) {
				DayTable dd12= ddb12.get(j);
				if(dd12.getCinema_inner_code().equals(ddc.getCinema_inner_code())){
					ddc.setExchangePointBalance(dd12.getExchangePointBalance());//      兑换积分
					ldt.set(i, ddc);
				}
		}
	}
		System.out.println(numsql);
		System.out.println("新增会员数，总会员数，线上新增会员数，线上新增会员总数，线下新增会员数，线下新增会员总数");
		System.out.println(ticketsql);
		System.out.println("线上会员票房，线上总票房，线下会员票房，线下总票房");
		System.out.println(goodssql);
		System.out.println("线上会员卖品，线上总卖品，线下会员卖品，线下总卖品");
		System.out.println(pointsql);
		System.out.println("线上新增积分,线下新增积分");
		System.out.println(countsql);
		System.out.println("线上总积分,线下总积分");
		System.out.println(exchangePointsql);
		System.out.println("兑换积分");
		
		return  ldt;
	}
	
	
	
	public DayTable fandByDay(Connection con ,String date){
		DayTable dd = new DayTable();
		DayTableDao ddao = new DayTableDaoImpl();
		//NEWMEMBERS,NEWMEMBERSCOUNT,NETMEMBERS,NETMEMBERSCOUNT,NOTNETMEMBERS,NOTNETMEMBERSCOUNT
		//String 
//		+---------------------------------------------------------会员数---------------------------------    "        
//		+                                                                                                    "        
//		+"-----------------------------------------                                                          "        
		String numsql =	 "  SELECT  sum(RECRUIT_MEMBER_COUNT) NEWMEMBERS    "    // 新增会员数   
			+",sum(MEMBER_COUNT) NEWMEMBERSCOUNT      "        //总会员数 
			+",sum(case when c.CHANNEL_CODE in('05','06','07') then RECRUIT_MEMBER_COUNT else 0 end ) NETMEMBERS "     //   线上新增会员数           
			+",sum(case when c.CHANNEL_CODE in('05','06','07') then MEMBER_COUNT else 0 end ) NETMEMBERSCOUNT     "       // 线上新增会员数 
			+",sum(case when c.CHANNEL_CODE in('05','06','07') then 0 else RECRUIT_MEMBER_COUNT end ) NOTNETMEMBERS   "       // 线下新增会员数
			+",sum(case when c.CHANNEL_CODE in('05','06','07') then 0 else MEMBER_COUNT end ) NOTNETMEMBERSCOUNT     "     //   线下新增会员数
			+"FROM CCS_NRPT2.T_F_CON_VIP_RECRUIT a                                                                "   //     
			+"inner join  CCS_NRPT2.T_D_CON_MBR_STATUS b on a.STATUS_KEY = b.STATUS_KEY     inner join CCS_NRPT2.T_D_CON_CHANNEL c on a.CHANNEL_KEY = c.CHANNEL_KEY      inner join CCS_NRPT2.T_D_CON_CINEMA d on a.CINEMA_KEY = d.CINEMA_KEY                               "     //   
			+"inner join CCS_NRPT2.T_D_CON_CHANNEL c on a.CHANNEL_KEY = c.CHANNEL_KEY      inner join CCS_NRPT2.T_D_CON_CINEMA b  on a.CINEMA_KEY = b.CINEMA_KEY                         "       // 
			+"where (d.INNER_CODE not in('311','333','304','370','802','381','849','811','336','340','371','808')  )  and BIZ_DATE_KEY  < to_date(?, 'yyyy-mm-dd') + 1 and BIZ_DATE_KEY >= to_date(?, 'yyyy-mm-dd')                                           "        // '2013-07-02'
			+"and b.STATUS_CODE = '1'                                                                                 ";        //
//		+"--------------------------------------------------------票房------------------------------------   "        //
//		+"                                                                                                   "        //
//		+"------------------------------------------------                                                   "        //
		String ticketsql = "select   sum(case when c.CHANNEL_CODE in('05','06','07') and MEMBER_KEY <> -999 then BK_ADMISSIONS   "//        
			+"                                                                                                   "        //
			+"else 0 end ) - sum(case when c.CHANNEL_CODE in('05','06','07')  and MEMBER_KEY <> -999 then        "        //
			+"                                                                                                   "        //
			+"RE_ADMISSIONS else 0 end) NETTICKETSUM                                                          "        // 线上会员票房    
			+",sum(case when c.CHANNEL_CODE in('05','06','07') then BK_ADMISSIONS else 0 end ) - sum(case when   "        //
			+"                                                                                                   "       // 
			+"c.CHANNEL_CODE in('05','06','07') then RE_ADMISSIONS else 0 end) NETTICKETSUMCOUNT                         "        //线上会员总票房
			+",sum(case when c.CHANNEL_CODE not in('05','06','07') and MEMBER_KEY <> -999 then BK_ADMISSIONS else 0 end ) - sum(case when c.CHANNEL_CODE not in('05','06','07')  and MEMBER_KEY <> -999 then RE_ADMISSIONS else 0 end) NOTNETTICKETSUM               "        // 线下会员票房   
			+",sum(case when c.CHANNEL_CODE  not in('05','06','07') then BK_ADMISSIONS else 0 end ) - sum(case when c.CHANNEL_CODE not in('05','06','07') then RE_ADMISSIONS else 0 end) NOTNETTICKETSUMCOUNT                         "        // 线下会员总票房
			+"FROM CCS_NRPT2.T_F_CON_TICKET a                                                                     "        //
			+"inner join  CCS_NRPT2.T_D_CON_CHANNEL c on a.CHANNEL_KEY = c.CHANNEL_KEY    inner join CCS_NRPT2.T_D_CON_CINEMA b  on a.CINEMA_KEY = b.CINEMA_KEY                         "        //
			+"where (b.INNER_CODE not in('311','333','304','370','802','381','849','811','336','340','371','808')  )  and   SHOW_DATE_KEY  < to_date(?, 'yyyy-mm-dd') + 1 and SHOW_DATE_KEY >= to_date(?, 'yyyy-mm-dd')                                         " ;       //'2013-07-02'
//		+"----------------------------------------------------------卖品----------------------------------   "        //
//		+"                                                                                                   "        //
//		+"-----------------------------------------------------------------------                            "        //
		String goodssql="select  sum(case when c.CHANNEL_CODE in('05','06','07') and MEMBER_KEY <> -999 then BK_SALE_AMOUNT  "  //      
			+"                                                                                                   "        //
			+"else 0 end ) - sum(case when c.CHANNEL_CODE in('05','06','07')  and MEMBER_KEY <> -999 then        "        //
			+"                                                                                                   "        //
			+"RE_SALE_AMOUNT else 0 end) NETGOODSSUM                                                            "        // 线上会员卖品   
			+",sum(case when c.CHANNEL_CODE in('05','06','07') then BK_SALE_AMOUNT else 0 end ) - sum(case when  "        //
			+"                                                                                                   "        //
			+"c.CHANNEL_CODE in('05','06','07') then RE_SALE_AMOUNT else 0 end) NETGODDSSUMCOUNT                         "        //线上会员总卖品   
			+",sum(case when c.CHANNEL_CODE in('05','06','07') and MEMBER_KEY <> -999 then 0 else                "        //
			+"                                                                                                   "        //
			+"BK_SALE_AMOUNT end ) - sum(case when c.CHANNEL_CODE in('05','06','07')  and MEMBER_KEY <> -999     "        //
			+"                                                                                                   "        //
			+"then 0 else RE_SALE_AMOUNT end) NOTNETGOODSSUM                                                       "        // 线下会员卖品   
			+",sum(case when c.CHANNEL_CODE in('05','06','07') then 0 else BK_SALE_AMOUNT end ) - sum(case when  "        //
			+"                                                                                                   "      //  
			+"c.CHANNEL_CODE in('05','06','07') then 0 else RE_SALE_AMOUNT end) NOTNETGODDSSUMCOUNT                         "        //线下会员总卖品 
			+"FROM CCS_NRPT2.T_F_CON_SALE a                                                                       "        //
			+"inner join  CCS_NRPT2.T_D_CON_CHANNEL c on a.CHANNEL_KEY = c.CHANNEL_KEY    inner join CCS_NRPT2.T_D_CON_CINEMA b  on a.CINEMA_KEY = b.CINEMA_KEY                           "        //
			+"where (b.INNER_CODE not in('311','333','304','370','802','381','849','811','336','340','371','808')  )  and BOOK_BIZ_DATE_KEY  < to_date(?, 'yyyy-mm-dd') + 1 and BOOK_BIZ_DATE_KEY >= to_date(?, 'yyyy-mm-dd')                                          ";        //'2013-07-02'
//		+"--------------------------------------------------------新增积分--------------------------------   "        //
//		+"                                                                                                   "        //
//		+"----------------------------------------------------------------------------------                 "        //
		String pointsql = "select  sum(case when c.CHANNEL_CODE in('05','06','07') and b.POINT_TYPE_CODE <> 6 then NEW_CREDID else 0 end ) NETPOINT    " //      线上新增积分 
			+", sum(case when c.CHANNEL_CODE not in('05','06','07') and b.POINT_TYPE_CODE <> 6 then NEW_CREDID else 0 end ) NOTNETPOINT        "        //线下新增积分
			+"FROM CCS_NRPT2.T_F_CON_VIP_CREDIT a  inner join  CCS_NRPT2.T_D_CON_CHANNEL c on a.CHANNEL_KEY = c.CHANNEL_KEY         "  //      
			+"inner join CCS_NRPT2.T_D_CON_POINT_TYPE b on a.POINT_TYPE_KEY = b.POINT_TYPE_KEY    inner join CCS_NRPT2.T_D_CON_CINEMA d  on a.CINEMA_KEY = d.CINEMA_KEY                           "        //
			+"where (d.INNER_CODE not in('311','333','304','370','802','381','849','811','336','340','371','808')  ) and BIZ_DATE_KEY  < to_date(?, 'yyyy-mm-dd') + 1 and BIZ_DATE_KEY >= to_date(?, 'yyyy-mm-dd')                                           "   ;     //'2013-07-02' 
//		+"---------------------------------------------------------累计积分-------------------------------   "        //
//		+"                                                                                                   "        //
//		+"----------------------------------------------------------------------------                       "        //
		String countsql = "select  sum(case when c.CHANNEL_CODE in('05','06','07') then TOTAL_CREDIT else 0 end ) NETPOINTCOUNT  "//     线上总积分    
			+", sum(case when c.CHANNEL_CODE in('05','06','07') then 0 else TOTAL_CREDIT end ) NOTNETPOINTCOUNT        "        //线下总积分
			+"FROM CCS_NRPT2.T_F_CON_VIP_CREDIT_TOTAL a                                                           "  //      
			+"inner join  CCS_NRPT2.T_D_CON_CHANNEL c on a.CHANNEL_KEY = c.CHANNEL_KEY        inner join CCS_NRPT2.T_D_CON_CINEMA b  on a.CINEMA_KEY = b.CINEMA_KEY                      "        //
			+"where   (b.INNER_CODE not in('311','333','304','370','802','381','849','811','336','340','371','808')  ) and BIZ_DATE_KEY  < to_date(?, 'yyyy-mm-dd') + 1 and BIZ_DATE_KEY >= to_date(?, 'yyyy-mm-dd')                                       ";        //'2013-07-02'
//		+"-----------------------------------------------------------可兑换积分兑换：9666-----------------------------   "        //
//		+"                                                                                                   "        //
//		+"-------------------------------------------------------------------------                          "        //
		String exchangePointsql="select   sum(CONNERT_CREDIT) EXCHANGEPOINT   FROM CCS_NRPT2.T_F_CON_VIP_CONVERT  a    inner join  CCS_NRPT2.T_D_CON_CHANNEL c on a.CHANNEL_KEY = c.CHANNEL_KEY  inner join CCS_NRPT2.T_D_CON_CINEMA b  on a.CINEMA_KEY = b.CINEMA_KEY                           "  //      兑换积分
			+"where (b.INNER_CODE not in('311','333','304','370','802','381','849','811','336','340','371','808')  ) and  BIZ_DATE_KEY  < to_date(?, 'yyyy-mm-dd') + 1 and BIZ_DATE_KEY >= to_date(?, 'yyyy-mm-dd')                                           "        ;//
		String type1 ="1";
		DayTable dd1 =ddao.fandDayTableBynewMembers(con, numsql, date,date,type1);
		//NEWMEMBERS,NEWMEMBERSCOUNT,NETMEMBERS,NETMEMBERSCOUNT,NOTNETMEMBERS,NOTNETMEMBERSCOUNT
	
		//在这加一个月的查询，年的查询
		
		//可兑换积分增加：43243
		String exchangePointnewsql="SELECT   sum(a.NEW_CREDID) EXCHANGEPOINTNEW FROM CCS_NRPT2.T_F_CON_VIP_CREDIT a  inner join CCS_NRPT2.T_D_CON_CINEMA b  on a.CINEMA_KEY = b.CINEMA_KEY  where (b.INNER_CODE not in('311','333','304','370','802','381','849','811','336','340','371','808')  ) and  a.POINT_TYPE_KEY <> 6 and a.BIZ_DATE_KEY  < to_date(?, 'yyyy-mm-dd') + 1 and a.BIZ_DATE_KEY >=  to_date(?, 'yyyy-mm-dd')      ";
		
		//可兑换积分余额：78524872
		String exchangePointBalancesql="SELECT    sum(RESERVE_CREDIT) EXCHANGEPOINTBALANCE FROM CCS_NRPT2.T_F_CON_VIP_CREDIT_TOTAL a  inner join CCS_NRPT2.T_D_CON_CINEMA b  on a.CINEMA_KEY = b.CINEMA_KEY    where  (b.INNER_CODE not in('311','333','304','370','802','381','849','811','336','340','371','808')  ) and  BIZ_DATE_KEY  < to_date(?, 'yyyy-mm-dd') + 1 and BIZ_DATE_KEY >= to_date(?, 'yyyy-mm-dd')        ";
	
		//会员月票房累计，以后分线上，线下
		String ticketMonthsql = "select  sum(case when c.CHANNEL_CODE in('05','06','07') and MEMBER_KEY <> -999 then BK_ADMISSIONS   "//        
			+"                                                                                                   "        //
			+"else 0 end ) - sum(case when c.CHANNEL_CODE in('05','06','07')  and MEMBER_KEY <> -999 then        "        //
			+"                                                                                                   "        //
			+"RE_ADMISSIONS else 0 end) NETTICKETSUMMONTH                                                          "        // 线上月会员票房    
			+",sum(case when c.CHANNEL_CODE not in('05','06','07') and MEMBER_KEY <> -999 then BK_ADMISSIONS else 0 end ) - sum(case when c.CHANNEL_CODE not in('05','06','07')  and MEMBER_KEY <> -999 then RE_ADMISSIONS else 0 end) NOTNETTICKETSUMMONTH               "        // 线下月会员票房   
			+"FROM CCS_NRPT2.T_F_CON_TICKET a                                                                     "        //
			+"inner join  CCS_NRPT2.T_D_CON_CHANNEL c on a.CHANNEL_KEY = c.CHANNEL_KEY    inner join CCS_NRPT2.T_D_CON_CINEMA b  on a.CINEMA_KEY = b.CINEMA_KEY                               "        //
			+"where  (b.INNER_CODE not in('311','333','304','370','802','381','849','811','336','340','371','808')  ) and   SHOW_DATE_KEY  < to_date(?, 'yyyy-mm-dd')  and SHOW_DATE_KEY >= to_date(?, 'yyyy-mm-dd')                                   " ;       //'2013-07-02'


		//会员年票房累计，以后分线上，线下
		String ticketYearsql = "select   sum(case when c.CHANNEL_CODE in('05','06','07') and MEMBER_KEY <> -999 then BK_ADMISSIONS   "//        
			+"                                                                                                   "        //
			+"else 0 end ) - sum(case when c.CHANNEL_CODE in('05','06','07')  and MEMBER_KEY <> -999 then        "        //
			+"                                                                                                   "        //
			+"RE_ADMISSIONS else 0 end) NETTICKETSUMYEAR                                                          "        // 线上年会员票房    
			+",sum(case when c.CHANNEL_CODE not in('05','06','07') and MEMBER_KEY <> -999 then BK_ADMISSIONS else 0 end ) - sum(case when c.CHANNEL_CODE not in('05','06','07')  and MEMBER_KEY <> -999 then RE_ADMISSIONS else 0 end) NOTNETTICKETSUMYEAR               "        // 线下会员票房   
			+"FROM CCS_NRPT2.T_F_CON_TICKET a                                                                     "        //
			+"inner join  CCS_NRPT2.T_D_CON_CHANNEL c on a.CHANNEL_KEY = c.CHANNEL_KEY     inner join CCS_NRPT2.T_D_CON_CINEMA b  on a.CINEMA_KEY = b.CINEMA_KEY                             "        //
			+"where  (b.INNER_CODE not in('311','333','304','370','802','381','849','811','336','340','371','808')  ) and    SHOW_DATE_KEY  < to_date(?, 'yyyy-mm-dd') + 1 and SHOW_DATE_KEY >= to_date(?, 'yyyy-mm-dd')                                            " ; 
		
		
		dd.setNewMembers(dd1.getNewMembers());//新增会员数
		dd.setNewMembersCount(dd1.getNewMembersCount());//总会员数
		dd.setNetMembers(dd1.getNetMembers());//线上新增会员数
		dd.setNetMembersCount(dd1.getNetMembersCount());//线上新增会员总数
		dd.setNotNetMembers(dd1.getNotNetMembers());  // 线下新增会员数
		dd.setNotNetMembersCount(dd1.getNotNetMembersCount()); //   线下新增会员总数
		
		String type2 ="2";
		DayTable dd2 =ddao.fandDayTableBynewMembers(con, ticketsql, date,date,type2);
		
		dd.setNetTicketSum(bigdecimalString(dd2.getNetTicketSum()));//线上会员票房    
		dd.setNetTicketSumCount(bigdecimalString(dd2.getNetTicketSumCount()));//线上会员总票房    
		dd.setNotNetTicketSum(bigdecimalString(dd2.getNotNetTicketSum()));  // 线下会员票房
		dd.setNotNetTicketSumCount(bigdecimalString(dd2.getNotNetTicketSumCount()));  // 线下会员总票房
		
		
		String type3 ="3";
		DayTable dd3 =ddao.fandDayTableBynewMembers(con, goodssql, date,date,type3);
		dd.setNetGoodsSum(bigdecimalString(dd3.getNetGoodsSum())); //线上会员卖品   
		dd.setNetGoddsSumCount(bigdecimalString(dd3.getNetGoddsSumCount())); //线上会员总卖品   
		dd.setNotNetGoodsSum(bigdecimalString(dd3.getNotNetGoodsSum())); //线下会员卖品 
		dd.setNotNetGoddsSumCount(bigdecimalString(dd3.getNotNetGoddsSumCount())); //线下会员总卖品 
		String type4 ="4";
		DayTable dd4 =ddao.fandDayTableBynewMembers(con, pointsql, date,date,type4);
		dd.setNetPoint(dd4.getNetPoint());//      线上新增积分 
		dd.setNotNetPoint(dd4.getNotNetPoint());  //线下新增积分
		
		String type5 ="5";
		DayTable dd5 =ddao.fandDayTableBynewMembers(con, countsql, date,date,type5);
		dd.setNetPointcount(dd5.getNetPointcount());//     线上总积分    
		dd.setNotNetPointcount(dd5.getNotNetPointcount()); //线下总积分
		String type6 ="6";
		DayTable dd6 =ddao.fandDayTableBynewMembers(con, exchangePointsql,date,date,type6);
		
		dd.setExchangePoint(dd6.getExchangePoint());//      兑换积分
		
		String enddate = enddateString(date);
		String te = "01";
		String ddj =date.substring(0, 8);
		String ddte = ddj+te;
		String type7 ="7";//会员月票房累计，以后分线上，线下
		DayTable dd7 =ddao.fandDayTableBynewMembers(con, ticketMonthsql,enddate,ddte,type7);
		
		dd.setNetTicketSumMonth(bigdecimalString(dd7.getNetTicketSumMonth()));//     
		dd.setNotNetTicketSumMonth(bigdecimalString(dd7.getNotNetTicketSumMonth()));//  
		
	//	String type8 ="8";//会员月卖品累计，以后分线上，线下
		
		String type9 ="9";//会员年票房累计，以后分线上，线下
		String yearenddate = date.substring(0, 4)+"-12-31";
		String yearstartdate =date.substring(0, 4)+"-01-01";
		 
		DayTable dd9 =ddao.fandDayTableBynewMembers(con, ticketYearsql,yearenddate,yearstartdate,type9);
		
		dd.setNetTicketSumYear(bigdecimalString(dd9.getNetTicketSumYear()));//     
		dd.setNotNetTicketSumYear(bigdecimalString(dd9.getNotNetTicketSumYear()));//  
		
	//	String type10 ="10";//会员年卖品累计，以后分线上，线下
		
		String type11 ="11";
		//可兑换积分增加：43243
		DayTable dd11 =ddao.fandDayTableBynewMembers(con, exchangePointnewsql,date,date,type11);
		
		dd.setExchangePointnew(dd11.getExchangePointnew());//     //可兑换积分增加：43243
		String type12 ="12";
		//可兑换积分余额：78524872
		DayTable dd12 =ddao.fandDayTableBynewMembers(con, exchangePointBalancesql,date,date,type12);
		
		dd.setExchangePointBalance(dd12.getExchangePointBalance());//      兑换积分
		
		System.out.println(numsql);
		System.out.println("新增会员数，总会员数，线上新增会员数，线上新增会员总数，线下新增会员数，线下新增会员总数");
		System.out.println(ticketsql);
		System.out.println("线上会员票房，线上总票房，线下会员票房，线下总票房");
		System.out.println(goodssql);
		System.out.println("线上会员卖品，线上总卖品，线下会员卖品，线下总卖品");
		System.out.println(pointsql);
		System.out.println("线上新增积分,线下新增积分");
		System.out.println(countsql);
		System.out.println("线上总积分,线下总积分");
		System.out.println(exchangePointsql);
		System.out.println("兑换积分");
		
		
		
		
		
		
		
		return dd;
	}
	
	public String bigdecimalString (String ss1){
		
		String ss = String.valueOf(ss1);
		BigDecimal d5 = new BigDecimal( ss  );
		String s6 =d5.setScale(0,BigDecimal.ROUND_DOWN).toString();
		return s6;
	}
	
	public String enddateString(String da){
		String te = "01";
		String dj =String.valueOf( Integer.valueOf(da.substring(5, 7))+1);
		if(Integer.valueOf(dj)<10){
			dj="0"+dj;
		}
		System.out.println(dj);
		String dd= da.substring(0, 5)+dj+"-";
		System.out.println(dd+te);
		String ddte = dd+te;
//		Calendar cal=Calendar.getInstance();
//		
//		int x=-1;//or x=-3;
//		//
//		Date date = DateUtil.getStringForDate(ddte);
//		cal.setTime(date);
//		cal.add(Calendar.DAY_OF_MONTH,x);
//		String bzda=new java.text.SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
		System.out.println(ddte);
		return ddte;
		
	}
 
	
	

}
