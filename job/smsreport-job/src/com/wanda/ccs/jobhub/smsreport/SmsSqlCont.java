package com.wanda.ccs.jobhub.smsreport;

/**
 * 短信SQL常量
 * 
 * @author zhurui
 * @date 2014年1月7日 下午4:18:30
 */
public class SmsSqlCont {
	
	//	******************************************************
	//						以下为影城短信统计sql
	//	******************************************************
	public static class CINEMA {
		
		//会员数的sql统计和以前保持一致
		private static final String MEMBER_SQL = "select b.inner_code, sum(a.recruit_member_count) as NEW_MEMBER, "+	//新增会员数
				"sum(case when a.channel_key not in('48','49','50') then a.recruit_member_count else 0 end) as NOT_NET_MEMBER, "+	//线下新增会员数
				"sum(case when a.channel_key in ('48','49','50') then a.recruit_member_count else 0 end) as NET_MEMBER, "+	//线上新增会员数
				"sum(case when a.biz_date_key = to_date(?, 'yyyy-mm-dd') then a.member_count else 0 end) as NEW_MEMBER_SUM, "+	//会员总数
				"sum(case when a.channel_key not in('48','49','50') and a.biz_date_key = to_date(?, 'yyyy-mm-dd') then a.member_count else 0 end) as NOT_NET_MEMBER_SUM, "+	//线下新增会员数
				"sum(case when a.channel_key in('48','49','50') and a.biz_date_key = to_date(?, 'yyyy-mm-dd') then a.member_count else 0 end) as NET_MEMBER_SUM "+	//线上新增会员数
				"from CCS_NRPT2.t_f_con_vip_recruit a,CCS_NRPT2.t_d_con_cinema b where a.cinema_key =b.cinema_key and a.biz_date_key = to_date(?, 'yyyy-mm-dd') and b.code <>-999 and b.area_id <>-999 "+
				"group by b.inner_code";
		
		//票房
		public static final String TICKET_SQL = "select b.inner_code,"+ 
				"sum(case when a.member_key>0 and a.show_biz_date_key = to_date(?,'yyyy-mm-dd') and a.channel_key = '47' then a.bk_admissions else 0 end)-sum(case when a.member_key>0 and a.show_biz_date_key = to_date(?,'yyyy-mm-dd') and a.channel_key = '47' then a.re_admissions else 0 end) as NOT_NET_TICKET_SUM,"+	//会员昨日线下票房
				"sum(case when a.member_key>0 and a.show_biz_date_key = to_date(?,'yyyy-mm-dd') and a.channel_key in('48','49','50') then a.bk_admissions else 0 end)-sum(case when a.member_key>0 and a.show_biz_date_key = to_date(?,'yyyy-mm-dd') and a.channel_key in('48','49','50') then a.re_admissions else 0 end) as NET_TICKET_SUM "+	//会员昨日线上票房
				"from CCS_NRPT2.t_f_con_ticket a,CCS_NRPT2.t_d_con_cinema b where a.cinema_key=b.cinema_key group by b.inner_code";

		//卖品
		public static final String GOODS_SQL = "select b.inner_code, sum(case when a.member_key>0 then a.bk_sale_amount else 0 end)-sum(case when a.member_key>0 then a.re_sale_amount else 0 end) as NOT_NET_GOODS_SUM from CCS_NRPT2.t_f_con_sale a,CCS_NRPT2.t_d_con_cinema b where a.cinema_key=b.cinema_key and a.book_biz_date_key=to_date(?,'yyyy-mm-dd') group by b.inner_code";	//线下会员卖品

		// 可兑换积分兑换
		public static final String EXCHANGE_POINT_SQL = "select b.inner_code, -sum(case when point_type_key =6 then new_credid else 0 end) as EXCHANGE_POINT from CCS_NRPT2.t_f_con_vip_credit a,CCS_NRPT2.t_d_con_cinema b where a.cinema_key = b.cinema_key and a.biz_date_key = to_date(?, 'yyyy-mm-dd') and a.cinema_key <> -999 group by b.inner_code";

		// 可兑换积分增加
		public static final String EXCHANGE_POINT_NEW_SQL = "select b.inner_code, sum(a.new_credid) as EXCHANGE_POINT_NEW from CCS_NRPT2.t_f_con_vip_credit a,CCS_NRPT2.t_d_con_cinema b where a.cinema_key = b.cinema_key and a.biz_date_key = to_date(?, 'yyyy-mm-dd') group by b.inner_code";	//可兑换积分增加
		
		// 可兑换积分余额
		public static final String EXCHANGE_POINT_BALANCE_SQL = "select b.inner_code, sum(a.reserve_credit) as EXCHANGE_POINT_BALANCE from CCS_NRPT2.t_f_con_vip_credit_total a,CCS_NRPT2.t_d_con_cinema b where a.cinema_key=b.cinema_key and a.biz_date_key = to_date(?, 'yyyy-mm-dd') group by b.inner_code";	//可兑换积分余额	

		// 会员月票房累计，以后分线上，线下
		public static final String TICKET_MONTH_SQL = "select b.inner_code,"+
				"sum(case when a.member_key>0 and a.show_biz_date_key >= to_date(?,'yyyy-mm-dd') and a.channel_key = '47' then a.bk_admissions else 0 end)-sum(case when a.member_key>0 and a.show_biz_date_key >= to_date(?,'yyyy-mm-dd') and a.channel_key = '47' then a.re_admissions else 0 end) as NOT_NET_TICKET_MONTH_SUM, "+	//线下月会员票房
				"sum(case when a.member_key>0 and a.show_biz_date_key >= to_date(?,'yyyy-mm-dd') and a.channel_key in('48','49','50') then a.bk_admissions else 0 end)-sum(case when a.member_key>0 and a.show_biz_date_key >= to_date(?,'yyyy-mm-dd') and a.channel_key in('48','49','50') then a.re_admissions else 0 end) as NET_TICKET_MONTH_SUM "+	//线上月会员票房
				"from CCS_NRPT2.t_f_con_ticket a,CCS_NRPT2.t_d_con_cinema b where a.cinema_key=b.cinema_key group by b.inner_code";

		//会员年票房累计，以后分线上，线下
		public static final String TICKET_YEAR_SQL = "select b.inner_code,"+
				"sum(case when a.member_key>0 and a.show_biz_date_key >= to_date(?,'yyyy-mm-dd') and a.channel_key not in('48','49','50') then a.bk_admissions else 0 end)-sum(case when a.member_key>0 and a.show_biz_date_key >= to_date(?,'yyyy-mm-dd') and a.channel_key not in('48','49','50') then a.re_admissions else 0 end) as NOT_NET_TICKET_YEAR_SUM,"+	//会员本年线下票房
				"sum(case when a.member_key>0 and a.show_biz_date_key >= to_date(?,'yyyy-mm-dd') and a.channel_key in('48','49','50') then a.bk_admissions else 0 end)-sum(case when a.member_key>0 and a.show_biz_date_key >= to_date(?,'yyyy-mm-dd') and a.channel_key in('48','49','50') then a.re_admissions else 0 end) as NET_TICKET_YEAR_SUM "+ 	//会员本年线上票房
				"from CCS_NRPT2.t_f_con_ticket a,CCS_NRPT2.t_d_con_cinema b where a.cinema_key=b.cinema_key group by b.inner_code";
		
	}
	
	//	******************************************************
	//						以下为院线短信统计sql
	//	******************************************************
	
	private static class LINE {
		
		//会员数的sql统计和以前保持一致
		public static final String MEMBER_SQL = "select sum(a.recruit_member_count) as NEW_MEMBER, "+	//新增会员数
				"sum(case when a.channel_key not in('48','49','50') then a.recruit_member_count else 0 end) as NOT_NET_MEMBER, "+	//线下新增会员数
				"sum(case when a.channel_key in ('48','49','50') then a.recruit_member_count else 0 end) as NET_MEMBER, "+	//线上新增会员数
				"sum(case when a.biz_date_key = to_date(?, 'yyyy-mm-dd') then a.member_count else 0 end) as NEW_MEMBER_SUM, "+	//会员总数
				"sum(case when a.channel_key not in('48','49','50') and a.biz_date_key = to_date(?, 'yyyy-mm-dd') then a.member_count else 0 end) as NOT_NET_MEMBER_SUM, "+	//线下新增会员数
				"sum(case when a.channel_key in ('48','49','50') and a.biz_date_key = to_date(?, 'yyyy-mm-dd') then a.member_count else 0 end) as NET_MEMBER_SUM "+	//线上新增会员数
				"from CCS_NRPT2.t_f_con_vip_recruit a,CCS_NRPT2.t_d_con_cinema b where a.cinema_key =b.cinema_key and a.biz_date_key = to_date(?, 'yyyy-mm-dd') and b.code <>-999 and b.area_id <>-999";
			
		//票房
		public static final String TICKET_SQL = "select "+ 
					"sum(case when a.member_key>0 and a.show_biz_date_key = to_date(?,'yyyy-mm-dd') and a.channel_key = '47' then a.bk_admissions else 0 end)-sum(case when a.member_key>0 and a.show_biz_date_key = to_date(?,'yyyy-mm-dd') and a.channel_key = '47' then a.re_admissions else 0 end) as NOT_NET_TICKET_SUM,"+	//会员昨日线下票房
					"sum(case when a.member_key>0 and a.show_biz_date_key = to_date(?,'yyyy-mm-dd') and a.channel_key in('48','49','50') then a.bk_admissions else 0 end)-sum(case when a.member_key>0 and a.show_biz_date_key = to_date(?,'yyyy-mm-dd') and a.channel_key in('48','49','50') then a.re_admissions else 0 end) as NET_TICKET_SUM "+	//会员昨日线上票房
					"from CCS_NRPT2.t_f_con_ticket a";
		
		//卖品
		public static final String GOODS_SQL = "select sum(case when a.member_key>0 then a.bk_sale_amount else 0 end)-sum(case when a.member_key>0 then a.re_sale_amount else 0 end) as NOT_NET_GOODS_SUM from CCS_NRPT2.t_f_con_sale a,CCS_NRPT2.t_d_con_cinema b where a.cinema_key=b.cinema_key and a.book_biz_date_key=to_date(?,'yyyy-mm-dd')";	//线下会员卖品
		
		// 可兑换积分兑换
		public static final String EXCHANGE_POINT_SQL = "select -sum(case when point_type_key =6 then new_credid else 0 end) as EXCHANGE_POINT from CCS_NRPT2.t_f_con_vip_credit a,CCS_NRPT2.t_d_con_cinema b where a.cinema_key = b.cinema_key and a.biz_date_key = to_date(?, 'yyyy-mm-dd') and a.cinema_key <> -999"; 
			
		// 可兑换积分增加
		public static final String EXCHANGE_POINT_NEW_SQL = "select sum(a.new_credid) as EXCHANGE_POINT_NEW from CCS_NRPT2.t_f_con_vip_credit a,CCS_NRPT2.t_d_con_cinema b where a.cinema_key = b.cinema_key and a.biz_date_key = to_date(?, 'yyyy-mm-dd') ";	//可兑换积分增加
		
		// 可兑换积分余额
		public static final String EXCHANGE_POINT_BALANCE_SQL = "select sum(a.reserve_credit) as EXCHANGE_POINT_BALANCE from CCS_NRPT2.t_f_con_vip_credit_total a,CCS_NRPT2.t_d_con_cinema b where a.cinema_key=b.cinema_key and a.biz_date_key = to_date(?, 'yyyy-mm-dd')";	//可兑换积分余额	
		
		// 会员月票房累计，以后分线上，线下
		public static final String TICKET_MONTH_SQL = "select "+
					"sum(case when a.member_key>0 and a.show_biz_date_key >= to_date(?,'yyyy-mm-dd') and a.channel_key = '47' then a.bk_admissions else 0 end)-sum(case when a.member_key>0 and a.show_biz_date_key >= to_date(?,'yyyy-mm-dd') and a.channel_key = '47' then a.re_admissions else 0 end) as NOT_NET_TICKET_MONTH_SUM, "+	//线下月会员票房
					"sum(case when a.member_key>0 and a.show_biz_date_key >= to_date(?,'yyyy-mm-dd') and a.channel_key in('48','49','50') then a.bk_admissions else 0 end)-sum(case when a.member_key>0 and a.show_biz_date_key >= to_date(?,'yyyy-mm-dd') and a.channel_key in('48','49','50') then a.re_admissions else 0 end) as NET_TICKET_MONTH_SUM "+	//线上月会员票房
					"from CCS_NRPT2.t_f_con_ticket a";
		
		//会员年票房累计，以后分线上，线下
		public static final String TICKET_YEAR_SQL = "select "+
					"sum(case when a.member_key>0 and a.show_biz_date_key >= to_date(?,'yyyy-mm-dd') and a.channel_key not in('48','49','50') then a.bk_admissions else 0 end)-sum(case when a.member_key>0 and a.show_biz_date_key >= to_date(?,'yyyy-mm-dd') and a.channel_key not in('48','49','50') then a.re_admissions else 0 end) as NOT_NET_TICKET_YEAR_SUM,"+	//会员本年线下票房
					"sum(case when a.member_key>0 and a.show_biz_date_key >= to_date(?,'yyyy-mm-dd') and a.channel_key in('48','49','50') then a.bk_admissions else 0 end)-sum(case when a.member_key>0 and a.show_biz_date_key >= to_date(?,'yyyy-mm-dd') and a.channel_key in('48','49','50') then a.re_admissions else 0 end) as NET_TICKET_YEAR_SUM "+ 	//会员本年线上票房
					"from CCS_NRPT2.t_f_con_ticket a";
		
	}
	//	******************************************************
	//						以下为应用操作sql
	//	******************************************************
	
	// 查询当前状态是否发送
	public static final String QUERY_JOB_STATUS = "select t.FLAG_SMS, t.FLAG_CINEMA_SMS from CCS_REPORT.T_SYS_DATA_JOB t where ymd=? and FLAG_ETL_SQL_POINTS=1";
	
	// 查询当前状态是否发送
	public static final String CHECK_JOB_STATUS = "select count(*) from CCS_REPORT.T_SYS_DATA_JOB t where ymd=? and FLAG_ETL_SQL_POINTS=1 {1}";
	
	public static final String UPDATE_JOB_STATUS = "update CCS_REPORT.T_SYS_DATA_JOB t set t.{1} where t.ymd=? ";
	
	// 查询发送用户
	public static final String QUERY_SEND_USER = "select t.seqid,t.name,t.duty,t.mobile,t.rtx,t.issystemuser,t.cinema_inner_code from CCS_MBR_PROD.T_IM_TARGET t where 1=1 ";
	
	//查询发送数量
	public static final String QUERY_WAIT_SEND_SMS_SQL_COUNT = "select count(*) from T_IM_SENDTASK t, T_IM_MSGTOSEND m where t.msgid=m.seqid and m.dataymd=? and m.cinema_inner_code {1}";
	
	// 查询院线发送列表
	public static final String QUERY_WAIT_SEND_SMS_LINE_SQL = 
			"select s.seqid,s.targetid,s.msgid,t.name,t.mobile,t.cinema_inner_code,null as cinema_inner_name,m.dataymd,m.NEW_MEMBER,m.NOT_NET_MEMBER,m.NET_MEMBER,m.NEW_MEMBER_SUM,m.NOT_NET_MEMBER_SUM,m.NET_MEMBER_SUM,m.NOT_NET_TICKET_SUM,m.NET_TICKET_SUM,m.NOT_NET_GOODS_SUM,m.EXCHANGE_POINT,m.EXCHANGE_POINT_NEW,m.EXCHANGE_POINT_BALANCE,m.NOT_NET_TICKET_MONTH_SUM,m.NET_TICKET_MONTH_SUM,m.NOT_NET_TICKET_YEAR_SUM,m.NET_TICKET_YEAR_SUM " + 
			"from T_IM_SENDTASK s, T_IM_TARGET t, T_IM_MSGTOSEND m " +
			"where s.targetid=t.seqid and s.msgid=m.seqid and m.dataymd=? and s.msgtype=1 and t.cinema_inner_code is null {1} ";
	
	// 查询影城发送列表
	public static final String QUERY_WAIT_SEND_SMS_CINEMA_SQL = 
			"select s.seqid,s.targetid,s.msgid,t.name,t.mobile,t.cinema_inner_code,c.inner_name as cinema_inner_name,m.dataymd,m.NEW_MEMBER,m.NOT_NET_MEMBER,m.NET_MEMBER,m.NEW_MEMBER_SUM,m.NOT_NET_MEMBER_SUM,m.NET_MEMBER_SUM,m.NOT_NET_TICKET_SUM,m.NET_TICKET_SUM,m.NOT_NET_GOODS_SUM,m.EXCHANGE_POINT,m.EXCHANGE_POINT_NEW,m.EXCHANGE_POINT_BALANCE,m.NOT_NET_TICKET_MONTH_SUM,m.NET_TICKET_MONTH_SUM,m.NOT_NET_TICKET_YEAR_SUM,m.NET_TICKET_YEAR_SUM "+ 
			"from T_IM_SENDTASK s, T_IM_TARGET t, T_IM_MSGTOSEND m, T_CINEMA c "+
			"where s.targetid=t.seqid and s.msgid=m.seqid and t.cinema_inner_code=c.inner_code and m.dataymd=? and s.msgtype=1 {1} "+
			"order by c.inner_code";
	
	// 查询影城内码（不包含未知）
	public static final String QUERY_CINEMA_INNER_CODE = "select c.inner_code,c.inner_name from t_cinema c where inner_code<>000";
	
	// 保存短信统计数据
	public static final String INSERT_SMS = "insert into T_IM_MSGTOSEND(seqid,dataymd,create_date,cinema_inner_code,new_member,not_net_member,net_member,new_member_sum,not_net_member_sum,net_member_sum,not_net_ticket_sum,net_ticket_sum,not_net_goods_sum,exchange_point,exchange_point_new,exchange_point_balance,not_net_ticket_month_sum,net_ticket_month_sum,not_net_ticket_year_sum,net_ticket_year_sum) values(S_T_IM_MSGTOSEND.Nextval,?,sysdate,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	// 保存短信统计数据
	public static final String INSERT_SMS_SEQID = "insert into T_IM_MSGTOSEND(seqid,dataymd,create_date,cinema_inner_code,new_member,not_net_member,net_member,new_member_sum,not_net_member_sum,net_member_sum,not_net_ticket_sum,net_ticket_sum,not_net_goods_sum,exchange_point,exchange_point_new,exchange_point_balance,not_net_ticket_month_sum,net_ticket_month_sum,not_net_ticket_year_sum,net_ticket_year_sum) values(?,?,sysdate,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	// 更新短信统计数据
	public static final String UPDATE_SMS = "update T_IM_MSGTOSEND t set t.new_member=?,t.not_net_member=?,t.net_member=?,t.new_member_sum=?,t.not_net_member_sum=?,t.net_member_sum=?,t.not_net_ticket_sum=?,t.net_ticket_sum=?,t.not_net_goods_sum=?,t.exchange_point=?,t.exchange_point_new=?,t.exchange_point_balance=?,t.not_net_ticket_month_sum=?,t.net_ticket_month_sum=?,t.not_net_ticket_year_sum=?,t.net_ticket_year_sum=?,create_date=? where t.seqid=?";
	
	// 查询短信数据列表
	public static final String QUERY_SMS = "select * from T_IM_MSGTOSEND t where t.dataymd=? and t.cinema_inner_code {1}";
	
	// 查询会员短信数量
	public static final String QUERY_SMS_COUNT = "select count(*) from T_IM_MSGTOSEND t where t.dataymd=? and t.cinema_inner_code {1}";
	
	// 查询短信校验状态
	public static final String QUERY_SMS_VALID_FAIL_COUNT = "select count(*) from T_IM_MSGTOSEND t where t.dataymd=? and t.valid=2 and cienma_inner_code {1}";
	
	// 保存发送状态
	public static final String INSERT_TARGET_SMS_RELATION = "insert into T_IM_SENDTASK (seqid,TARGETID,msgId,status,TargetMobile,MsgType,priority)values(S_T_IM_SENDTASK.Nextval,?,?,?,?,?,?)";
	
	// 更新发送状态
	public static final String UPDATE_TARGET_SMS_RELATION = "update T_IM_SENDTASK t set t.status=?, t.sendtime=sysdate where t.seqid=?";
	
	// 更新短信校验状态
	public static final String UPDATE_SMS_VALID_STATUS = "update T_IM_MSGTOSEND t set t.valid=?, t.fail_desc=? where t.seqid=?";
	
	// 获取消息序列
	public static final String SEQ_SMS_ID = "select S_T_IM_MSGTOSEND.nextVal seq from dual";
	
}
