package com.wanda.mms.control.stream.service;

import java.sql.Connection;
import java.util.Date;

import org.apache.log4j.Logger;

import com.wanda.mms.control.stream.Basic;
import com.wanda.mms.control.stream.dao.LevelHistoryDao;
import com.wanda.mms.control.stream.dao.MemberLevelDao;
import com.wanda.mms.control.stream.dao.MemberPointDao;
import com.wanda.mms.control.stream.dao.PointHistroyDao;
import com.wanda.mms.control.stream.dao.impl.LevelHistoryDaoImpl;
import com.wanda.mms.control.stream.dao.impl.MemberLevelDaoImpl;
import com.wanda.mms.control.stream.dao.impl.MemberPointDaoImpl;
import com.wanda.mms.control.stream.dao.impl.PointHistroyDaoImpl;
import com.wanda.mms.control.stream.util.DateUtil;
import com.wanda.mms.control.stream.vo.LevelHistory;
import com.wanda.mms.control.stream.vo.MemberLevel;
import com.wanda.mms.control.stream.vo.MemberPoint;
import com.wanda.mms.control.stream.vo.PointHistroy;

/**
 *	会员初始化
 * @author wangshuai
 * @date 2013-05-28	
 */
public class IntegralInitialization {
	static Logger logger = Logger.getLogger(IntegralInitialization.class.getName());

	MemberPointDao mpdao = new MemberPointDaoImpl();
	PointHistroyDao phdao = new PointHistroyDaoImpl();
	MemberLevelDao mbldao = new MemberLevelDaoImpl();
	LevelHistoryDao lhdao = new LevelHistoryDaoImpl();
	
	public int addMemberPointByID(Connection conn,long member_id,String userName,long pointbalance){
		logger.info("Entering addMemberPointByID(Connection ="+conn+" member_id="+member_id+" userName= "+userName+" pointbalance= "+pointbalance+")");
		int flag=0;
		Long memberPointSeq = mpdao.seqMemberPoint(conn);
		Date da = new Date();
		String date = DateUtil.getDateStrss(da);//时间可能是 时分秒
		String yy = (String) date.subSequence(0, 4);
		int intyy = Integer.valueOf(yy);
		intyy = intyy +1;
		String yyyy = String.valueOf(intyy);
		String te = "-12-31 23:59:59";
		String time =yyyy+te; 
		
		MemberPoint mb = new MemberPoint();
		  mb.setMember_point_id(memberPointSeq);//会员积分ID	;
		  mb.setMemberId(member_id);//会员ID
		  mb.setExg_Point_Balance(0);//可兑换积分余额
		  mb.setExg_Expire_Point_Balance(0);//当年即将过期可兑换积分#EXG_EXPIRE_POINT_BALANCE
		  mb.setPoint_History_Id(0); //最后发生积分历史ID#POINT_HISTORY_ID
		  mb.setLevel_Point_Total(0);//总累计定积分#LEVEL_POINT_TOTAL
		  mb.setActivity_Point(0) ;//总累计非定级积分#ACTIVITY_POINT
		  mb.setLast_Exchange_Date(" ");//最后兑换时间#LAST_EXCHANGE_DATE
		  mb.setIsdelete(0);	//逻辑删除标识
		  mb.setCreate_By(userName);//创建者
		  mb.setCreate_Date(date);
		  mb.setUpdate_By(userName); //更新者
		  mb.setUpdate_Date(date);
		  mb.setLast_Exchange_Date(date);
		  mb.setVersion(0);//版本号
		  flag = mpdao.addMemberPointByID(conn, mb);
		  
		  
		  if(pointbalance!=0){
			  String ss ="";
				PointHistroy ph = new PointHistroy();
				ph.setMemberid(member_id);//需要传进来 会员ID
				ph.setMember_point_id(memberPointSeq);//需要传进来 会员ID
				ph.setExchange_Point(pointbalance);//需要传进来  可兑换积分#EXCHANGE_POINT
				ph.setExg_expire_point_balance(0);//当年即将过期可兑换积分
				ph.setPoint_Sys("0");//需要传进来 维表.源系统(1:POS;2:网站;3:会员系统;Others 其他)
				ph.setPoint_Trans_Code("");//需要传进来 单号(交易单号(POS/网站)/积分兑换交易单号/特殊积分规则ID)
				ph.setPoint_Trans_Code_Web("");//需要传进来 如果point_trans_type是交易,point_sys是网站，则在此记录网站订单号关联的POS订单
				ph.setCreate_By(userName);//?创建人员工ID(系统自动:"member_sys";管理界面:登录员工ID)
				ph.setUpdate_By(userName);//更新人员工ID(系统自动:"member_sys";管理界面:登录员工ID)
				
				ph.setSetTime(date);//执行积分的时间
				ph.setLevel_Point(0);//定级积分#LEVEL_POINT
				ph.setTicket_Count(0);//升降级判定票数#TICKET_COUNT
				ph.setActivity_Point(pointbalance);//非定级积分#ACTIVITY_POINT
				ph.setExchange_Point_Expire_Time(time);
				ph.setPoint_Type("3");//维表.积分操作类型(1:购买;2:礼品;3:奖励;4调整;5:会员终止;6:积分兑换;其他值:其他）
				ph.setPoint_Trans_Type("3");//维表.单据类型(1:交易;2:积分兑换;3:特殊积分活动;4积分清零)
				ph.setAdj_reason_type("");//维表.积分调整原因类型
				ph.setAdj_reason("");
				ph.setIsdelete(0);
				ph.setVersion(1);
				
				ss  = phdao.addPointHistroy(conn, ph);
		  }
		String ssli =   LevelInitialization(conn,member_id,userName);
	//	  System.out.println(ssli);
		 // DOWNLEVEL
		 // INTEGRALRESET 
		  logger.info("Exiting addMemberPointByID()");
		return flag;
	}
	
	public String LevelInitialization(Connection conn,long member_id,String userName){
		logger.info("Entering LevelInitialization(Connection ="+conn+" member_id="+member_id+" userName= "+userName+" )");
		MemberLevel mlll =	mbldao.fandMemberLevelByID(conn, member_id);
		String ss = "";
		if(mlll.getMember_id()==0){
		LevelHistory lh = new LevelHistory();
		MemberLevel ml = new MemberLevel(); 
		Date da = new Date();
		String date = DateUtil.getDateStrss(da);//时间可能是 时分秒
		String yy = (String) date.subSequence(0, 4);
		int intyy = Integer.valueOf(yy);
		intyy = intyy +1;
		String yyyy = String.valueOf(intyy);
		String te = "-12-31 23:59:59";
		String time =yyyy+te; 
		long seq = lhdao.seqLevelHistroy(conn);
		lh.setLevel_history_id(seq);
		lh.setMem_level("1");
		lh.setExpire_date(time);
		lh.setOrg_mem_level("0");
		lh.setOrg_expire_date(date);
		lh.setSet_time(date);
		lh.setReson_type("4");//维表.级别变更原因类型(1:老会员回馈,2:会员投诉:3储值卡会员转化,4:其他)
		//lh.setReson_type("会员注册初始化");
		lh.setREASON("会员注册初始化");
		lh.setChg_type("up");
		lh.setMember_id(member_id);
		lh.setLevel_point(0);
		lh.setTicket_count(0);
		lh.setIsdelete(0);
		lh.setCreate_By(userName);
		lh.setCreate_Date(date);
		lh.setUpdate_By(userName);
		lh.setUpdate_Date(date);
		lh.setVersion(1);
		String jj = lhdao.addLevelHistoryByInitialization(conn, lh);
		ml.setMember_id(member_id);
		ml.setMem_level("1");
		ml.setExpire_date(time);
		ml.setOrg_level("0");
		ml.setSet_time(date);
		ml.setTarget_level("2");
		ml.setLevel_point_offset(500);
		ml.setTicket_offset(12);
		long seqml = mbldao.fandMemberLevelBySEQ(conn);
		ml.setLast_level_history_id(seqml);
		ml.setIsdelete(0);
		ml.setCreate_By(userName);
		ml.setCreate_Date(date);
		ml.setUpdate_By(userName);
		ml.setUpdate_Date(date);
		ml.setVersion(1);
		ml.setMember_level_history_id(seq);
		int sl = mbldao.addMemberLevelByID(conn, ml);
		ss = jj+sl;
		} 
		  logger.info("Exiting LevelInitialization()");
		return ss;
	}

}
