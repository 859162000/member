package com.wanda.mms.control.main;

public class TestTxt {
	public static void main(String[] args) {
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
//			+"--------------------------------------------------------票房------------------------------------   "        //
//			+"                                                                                                   "        //
//			+"------------------------------------------------                                                   "        //
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
//			+"----------------------------------------------------------卖品----------------------------------   "        //
//			+"                                                                                                   "        //
//			+"-----------------------------------------------------------------------                            "        //
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
//			+"--------------------------------------------------------新增积分--------------------------------   "        //
//			+"                                                                                                   "        //
//			+"----------------------------------------------------------------------------------                 "        //
		String pointsql = "select  sum(case when c.CHANNEL_CODE in('05','06','07') and b.POINT_TYPE_CODE <> 6 then NEW_CREDID else 0 end ) NETPOINT    " //      线上新增积分 
			+", sum(case when c.CHANNEL_CODE not in('05','06','07') and b.POINT_TYPE_CODE <> 6 then NEW_CREDID else 0 end ) NOTNETPOINT        "        //线下新增积分
			+"FROM CCS_NRPT2.T_F_CON_VIP_CREDIT a  inner join  CCS_NRPT2.T_D_CON_CHANNEL c on a.CHANNEL_KEY = c.CHANNEL_KEY         "  //      
			+"inner join CCS_NRPT2.T_D_CON_POINT_TYPE b on a.POINT_TYPE_KEY = b.POINT_TYPE_KEY    inner join CCS_NRPT2.T_D_CON_CINEMA d  on a.CINEMA_KEY = d.CINEMA_KEY                           "        //
			+"where (d.INNER_CODE not in('311','333','304','370','802','381','849','811','336','340','371','808')  ) and BIZ_DATE_KEY  < to_date(?, 'yyyy-mm-dd') + 1 and BIZ_DATE_KEY >= to_date(?, 'yyyy-mm-dd')                                           "   ;     //'2013-07-02' 
//			+"---------------------------------------------------------累计积分-------------------------------   "        //
//			+"                                                                                                   "        //
//			+"----------------------------------------------------------------------------                       "        //
		String countsql = "select  sum(case when c.CHANNEL_CODE in('05','06','07') then TOTAL_CREDIT else 0 end ) NETPOINTCOUNT  "//     线上总积分    
			+", sum(case when c.CHANNEL_CODE in('05','06','07') then 0 else TOTAL_CREDIT end ) NOTNETPOINTCOUNT        "        //线下总积分
			+"FROM CCS_NRPT2.T_F_CON_VIP_CREDIT_TOTAL a                                                           "  //      
			+"inner join  CCS_NRPT2.T_D_CON_CHANNEL c on a.CHANNEL_KEY = c.CHANNEL_KEY        inner join CCS_NRPT2.T_D_CON_CINEMA b  on a.CINEMA_KEY = b.CINEMA_KEY                      "        //
			+"where   (b.INNER_CODE not in('311','333','304','370','802','381','849','811','336','340','371','808')  ) and BIZ_DATE_KEY  < to_date(?, 'yyyy-mm-dd') + 1 and BIZ_DATE_KEY >= to_date(?, 'yyyy-mm-dd')                                       ";        //'2013-07-02'
//			+"-----------------------------------------------------------可兑换积分兑换：9666-----------------------------   "        //
//			+"                                                                                                   "        //
//			+"-------------------------------------------------------------------------                          "        //
		String exchangePointsql="select   sum(CONNERT_CREDIT) EXCHANGEPOINT   FROM CCS_NRPT2.T_F_CON_VIP_CONVERT  a    inner join  CCS_NRPT2.T_D_CON_CHANNEL c on a.CHANNEL_KEY = c.CHANNEL_KEY  inner join CCS_NRPT2.T_D_CON_CINEMA b  on a.CINEMA_KEY = b.CINEMA_KEY                           "  //      兑换积分
			+"where (b.INNER_CODE not in('311','333','304','370','802','381','849','811','336','340','371','808')  ) and  BIZ_DATE_KEY  < to_date(?, 'yyyy-mm-dd') + 1 and BIZ_DATE_KEY >= to_date(?, 'yyyy-mm-dd')                                           "        ;//
		
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
		
		System.out.println("-----------票房OK------------------");
		System.out.println(ticketsql);
		System.out.println();
		System.out.println("-------------卖品OK----------------");
		System.out.println();
		System.out.println(goodssql);
		System.out.println("-----------新增会员数OK  ------------------");
		System.out.println();
		System.out.println(numsql);
		System.out.println("--------------新增积分OK---------------");
		System.out.println();
		System.out.println(pointsql);
		System.out.println("-------------累计积分OK----------------");
		System.out.println();
		System.out.println(countsql);
		System.out.println("---------------可兑换积分兑换OK--------------");
		System.out.println();
		System.out.println(exchangePointsql);
		System.out.println("-----------可兑换积分增加 OK ------------------");
		System.out.println();
		System.out.println(exchangePointnewsql);
		System.out.println("--------------可兑换积分余额OK---------------");
		System.out.println();
		System.out.println(exchangePointBalancesql);
		System.out.println("-------------会员月票房累计，以后分线上，线下OK----------------");
		System.out.println();
		System.out.println(ticketMonthsql);
		System.out.println("---------------会员年票房累计，以后分线上，线下OK--------------");
		System.out.println();
		System.out.println(ticketYearsql);
		
	}

}
