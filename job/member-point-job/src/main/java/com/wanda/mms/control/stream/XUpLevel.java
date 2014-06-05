package com.wanda.mms.control.stream;

import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.wanda.mms.control.stream.dao.LevelHistoryDao;
import com.wanda.mms.control.stream.dao.MemberLevelDao;
import com.wanda.mms.control.stream.dao.MemberPointDao;
import com.wanda.mms.control.stream.dao.PointHistroyDao;
import com.wanda.mms.control.stream.dao.T_event_upgradeDao;
import com.wanda.mms.control.stream.dao.impl.LevelHistoryDaoImpl;
import com.wanda.mms.control.stream.dao.impl.MemberLevelDaoImpl;
import com.wanda.mms.control.stream.dao.impl.MemberPointDaoImpl;
import com.wanda.mms.control.stream.dao.impl.PointHistroyDaoImpl;
import com.wanda.mms.control.stream.dao.impl.T_event_upgradeDaoImpl;
import com.wanda.mms.control.stream.db.SENDDBConnection;
import com.wanda.mms.control.stream.util.DateUtil;
import com.wanda.mms.control.stream.util.Page;
import com.wanda.mms.control.stream.vo.LevelHistory;
import com.wanda.mms.control.stream.vo.MemberLevel;
import com.wanda.mms.control.stream.vo.MemberPoint;
import com.wanda.mms.control.stream.vo.PointHistroy;
import com.wanda.mms.control.stream.vo.T_event_upgrade;
/**
 *	会员升级 调度
 * @author wangshuai
 * @date 2013-05-28	
 */
public class XUpLevel {
	static Logger logger = Logger.getLogger(XUpLevel.class.getName());
//	if(!log.isDebugEnabled()){
//		log.error(e);
//	}
	Dimdef dd = new Dimdef();
	/***
	 * 会员升级 
	 * 
	 * @return
	 */
	public String upMemberLevel(Connection conn,MemberPoint mp ,List<PointHistroy> phlist){
		logger.info("Entering upMemberLevel()");
		logger.info("会员升级upMemberLevel方法 Connection="+conn+"会员级别ID="+mp.getMemberId()+"会员积分历史条数="+phlist.size());
		String ss ="";
		PointHistroy phml = new PointHistroy();
		int ticket_count =0;
		long level_point =0;
		//
		for (int i = 0; i < phlist.size(); i++) {
			PointHistroy ph = phlist.get(i);
			if(mp.getMemberId()==ph.getMemberid()){
				ticket_count+=ph.getTicket_Count();//得到要累计的升降级判定票数
				level_point +=ph.getLevel_Point();//定级积分
				   
			}
		
		}
		Date da = new Date();
		String date = DateUtil.getDateStrss(da);//时间可能是 时分秒
		String yy = (String) date.subSequence(0, 4);
		int intyy = Integer.valueOf(yy);
		intyy = intyy +1;
		String yyyy = String.valueOf(intyy);
		String te = "-12-31 23:59:59";
		String time =yyyy+te; 
	//	System.out.println("date:"+date);
	//	System.out.println("time:"+time);
		 
		
		// MemberLevelDaoImpl implements MemberLevelDao
		//MemberLevel fandMemberLevelByID
		LevelHistoryDao lhdao = new LevelHistoryDaoImpl();
		MemberLevelDao mldao = new  MemberLevelDaoImpl();
		MemberPointDao mpdao = new MemberPointDaoImpl();
		MemberLevel ml = mldao.fandMemberLevelByID(conn, mp.getMemberId());
		MemberLevel mll = mldao.fandMemberLevelByID(conn, ml.getMember_id());
		LevelHistory lh = new LevelHistory();
		int level = Integer.valueOf(ml.getMem_level());
		long seq = lhdao.seqLevelHistroy(conn);
		T_event_upgradeDao teudao = new T_event_upgradeDaoImpl();
		T_event_upgrade teu = new T_event_upgrade();
		//System.out.println(seq);
    	//	System.out.println(date + "与    "+mll.getExpire_date() +"比效结果是 "+da.after(DateUtil.getStringForDate(mll.getExpire_date())));
		switch (level) {
		case 1: if(ticket_count>=12||level_point>=500){
			//long seq = lhdao.seqLevelHistroy(conn);
			lh.setLevel_history_id(seq);
			lh.setMem_level(String.valueOf(level+1));//变更后级别=维表.会员级别
			lh.setOrg_mem_level(String.valueOf(level));//变更前级别=维表.会员级别
			//lh.setChg_type(ml.getMem_level());
			lh.setExpire_date(time);
			lh.setOrg_expire_date(ml.getExpire_date());//变更前级别有效期
			lh.setReson_type(dd.RESON_TYPE_QT);//维表.级别变更原因类型(1:老会员回馈,2:会员投诉:3储值卡会员转化,4:其他)
			//lh.setReson_type("member_sys");//级别调整：人工输入；升/降级计算:"member_sys"
			lh.setREASON("member_sys");
			lh.setChg_type("UP");//级别变更类型(UP:升级,DOWN:降级)
			lh.setMember_id(ml.getMember_id());//会员ID#MEMBER_ID
			lh.setLevel_point(level_point);//判定用户可升级或降级的有效积分数,人工调整时,不记录。用于镜像变更前后值
			lh.setTicket_count(ticket_count);//判定用户可升级或降级的有效积分数,人工调整时,不记录。用于镜像变更前后值
			lh.setIsdelete(0);//逻辑删除标识,默认:0 未删除;1删除;其他:非法
			
			lh.setCreate_By("member_sys");//创建人员工ID(系统自动:"member_sys";管理界面:登录员工ID)
			
			lh.setUpdate_By("member_sys");//更新人员工ID(系统自动:"member_sys";管理界面:登录员工ID)
			
			lh.setVersion(1);
			mll.setMem_level(lh.getMem_level());
			mll.setExpire_date(time);
			mll.setOrg_level(String.valueOf(level));
			mll.setSet_time(date);
			mll.setTarget_level(String.valueOf(level+2));
			if((1000-level_point)>0){
				mll.setLevel_point_offset(1000-level_point);
			}else{
				mll.setLevel_point_offset(0);
			}
			if((24-ticket_count)>0){
				mll.setTicket_offset(24-ticket_count);
			}else{
				mll.setTicket_offset(0);
			}
		//	T_event_upgradeDaoImpl implements T_event_upgradeDao 

			mll.setMember_level_history_id(seq);
			lhdao.addLevelHistory(conn, lh,mll);
			
			teu.setMember_id(mll.getMember_id());
			teu.setMember_level(mll.getMem_level());
			teu.setStatus("0");
			teudao.addTeventupgrade(conn, teu);
			//teu.setCreate_time(createTime);
			//
			//ml.setMem_level(String.valueOf(level+1)); //等级加一
		}else{
		//	long seq = lhdao.seqLevelHistroy(conn);
 
			mll.setMem_level(String.valueOf(level));
			mll.setExpire_date(time);
			mll.setOrg_level(String.valueOf(level-1));
			mll.setSet_time(date);
			mll.setTarget_level(String.valueOf(level+1));
			if((500-level_point)>0){
				mll.setLevel_point_offset(500-level_point);
			}else{
				mll.setLevel_point_offset(0);
			}
			if((12-ticket_count)>0){
				mll.setTicket_offset(12-ticket_count);
			}else{
				mll.setTicket_offset(0);
			}
			mldao.updateMemberLevelByID(conn, mll);
			//ml.setLast_level_history_id(seq);
			//lhdao.addLevelHistory(conn, lh,ml);
			
		}
			
			break;
		case 2:if(ticket_count>=24||level_point>=1000){
		//	long seq = lhdao.seqLevelHistroy(conn);
			lh.setLevel_history_id(seq);
			lh.setMem_level(String.valueOf(level+1));//变更后级别=维表.会员级别
			lh.setOrg_mem_level(String.valueOf(level));//变更前级别=维表.会员级别
			lh.setOrg_expire_date(ml.getExpire_date());//变更前级别有效期
			lh.setReson_type(dd.RESON_TYPE_QT);//维表.级别变更原因类型(1:老会员回馈,2:会员投诉:3储值卡会员转化,4:其他)
			//lh.setReson_type("member_sys");//级别调整：人工输入；升/降级计算:"member_sys"
			lh.setREASON("member_sys");
			lh.setChg_type("UP");//级别变更类型(UP:升级,DOWN:降级)
			lh.setMember_id(ml.getMember_id());//会员ID#MEMBER_ID
			lh.setLevel_point(level_point);//判定用户可升级或降级的有效积分数,人工调整时,不记录。用于镜像变更前后值
			lh.setTicket_count(ticket_count);//判定用户可升级或降级的有效积分数,人工调整时,不记录。用于镜像变更前后值
			lh.setIsdelete(0);//逻辑删除标识,默认:0 未删除;1删除;其他:非法

 
			lh.setCreate_By("member_sys");//创建人员工ID(系统自动:"member_sys";管理界面:登录员工ID)
			
			lh.setUpdate_By("member_sys");//更新人员工ID(系统自动:"member_sys";管理界面:登录员工ID)
			lh.setVersion(1);
			mll.setMem_level(lh.getMem_level());
			mll.setExpire_date(time);
			mll.setOrg_level(String.valueOf(level));
			mll.setSet_time(date);
			mll.setTarget_level(String.valueOf(level+2));
			if((3000-level_point)>0){
				mll.setLevel_point_offset(3000-level_point);
			}else{
				mll.setLevel_point_offset(0);
			}		
			if((48-ticket_count)>0){
				mll.setTicket_offset(48-ticket_count);
			}else{
				mll.setTicket_offset(0);
			}
			 
			mll.setMember_level_history_id(seq);
			lhdao.addLevelHistory(conn, lh,mll);
			teu.setMember_id(mll.getMember_id());
			teu.setMember_level(mll.getMem_level());
			teu.setStatus("0");
			teudao.addTeventupgrade(conn, teu);
		}else{		
			mll.setMem_level(String.valueOf(level));
			mll.setExpire_date(time);
			mll.setOrg_level(String.valueOf(level-1));
			mll.setSet_time(date);
			mll.setTarget_level(String.valueOf(level+1));
		if((1000-level_point)>0){
			mll.setLevel_point_offset(1000-level_point);
		}else{
			mll.setLevel_point_offset(0);
		}
		if((24-ticket_count)>0){
			mll.setTicket_offset(24-ticket_count);
		}else{
			mll.setTicket_offset(0);
		}
		mldao.updateMemberLevelByID(conn, mll);}
			   
			break;
		case 3:if(ticket_count>=48||level_point>=3000){
		//	long seq = lhdao.seqLevelHistroy(conn);
			lh.setLevel_history_id(seq);
			lh.setMem_level(String.valueOf(level+1));//变更后级别=维表.会员级别
			lh.setOrg_mem_level(String.valueOf(level));//变更前级别=维表.会员级别
			lh.setOrg_expire_date(ml.getExpire_date());//变更前级别有效期
			lh.setReson_type(dd.RESON_TYPE_QT);//维表.级别变更原因类型(1:老会员回馈,2:会员投诉:3储值卡会员转化,4:其他)
			//lh.setReson_type("member_sys");//级别调整：人工输入；升/降级计算:"member_sys"
			lh.setREASON("member_sys");
			lh.setChg_type("UP");//级别变更类型(UP:升级,DOWN:降级)
			lh.setMember_id(ml.getMember_id());//会员ID#MEMBER_ID
			lh.setLevel_point(level_point);//判定用户可升级或降级的有效积分数,人工调整时,不记录。用于镜像变更前后值
			lh.setTicket_count(ticket_count);//判定用户可升级或降级的有效积分数,人工调整时,不记录。用于镜像变更前后值
			lh.setIsdelete(0);//逻辑删除标识,默认:0 未删除;1删除;其他:非法
			lh.setCreate_By("member_sys");//创建人员工ID(系统自动:"member_sys";管理界面:登录员工ID)
			lh.setUpdate_By("member_sys");//更新人员工ID(系统自动:"member_sys";管理界面:登录员工ID)
			lh.setVersion(1);
			mll.setMem_level(lh.getMem_level());
			mll.setExpire_date(time);
			mll.setOrg_level(String.valueOf(level));
			mll.setSet_time(date);
			mll.setTarget_level(String.valueOf(level+1));
			if((6000-level_point)>0){
				mll.setLevel_point_offset(0);
			}else{
				mll.setLevel_point_offset(0);
			}		
			if((96-ticket_count)>0){
				mll.setTicket_offset(0);
			}else{
				mll.setTicket_offset(0);
			}
			mll.setMember_level_history_id(seq);
			lhdao.addLevelHistory(conn, lh,mll);
			teu.setMember_id(mll.getMember_id());
			teu.setMember_level(mll.getMem_level());
			teu.setStatus("0");
			teudao.addTeventupgrade(conn, teu);
		}else{			mll.setMem_level(String.valueOf(level));
		mll.setExpire_date(time);
		mll.setOrg_level(String.valueOf(level-1));
		mll.setSet_time(date);
		mll.setTarget_level(String.valueOf(level+1));
		if((3000-level_point)>0){
			mll.setLevel_point_offset(3000-level_point);
		}else{
			mll.setLevel_point_offset(0);
		}
		if((48-ticket_count)>0){
			mll.setTicket_offset(48-ticket_count);
		}else{
			mll.setTicket_offset(0);
		}
		mldao.updateMemberLevelByID(conn, mll);}
			
			break;

		default:
			
			
			break;
		}
		logger.info("Exiting upMemberLevel()");
		mpdao.upMemberPointBylevel(conn, mp.getMemberId());
		return ss;
	}
	
	public String  uplevels(){
		String ss="" ;
//		Date da = new Date();
//		String date = DateUtil.getDateStrymd(da);//时间可能是 时分秒
		//System.out.println("uplevelsfy"+date);

		SENDDBConnection db=SENDDBConnection.getInstance();	
	    Connection conn = null;
		conn=db.getConnection();
		 
		PointHistroyDao pointDao = new PointHistroyDaoImpl();;
	    MemberPointDao memdao = new MemberPointDaoImpl();
		 
		String sup ="";
		StringBuffer sbup = new StringBuffer();
		//得到总条数
		Long totalNum = memdao.fandMemberPointTotalNumDay(conn);
		 
		
		//改为统计会员表，变更时间为今天的所有会员。
		Page page = new Page();  
		long pageSize =50;
		long pageNo = 0;
		
		while (totalNum>0) {
			if(pageNo==0){
				page.setPage(pageNo*pageSize);
			}else{
				page.setPage((pageNo*pageSize)+1);
			}
			page.setPageSize(pageSize+page.getPageSize());
			List<MemberPoint> mplist = memdao.fandMemberPointDayPage(conn, page);
			//分页查今天积分有变更的会员积分表中的会员。
			for (int i = 0; i < mplist.size(); i++) {
				MemberPoint mp = mplist.get(i);
				//MemberPoint memp =	memdao.fandMemberPointByID(conn, mp.getMemberId());
				List<PointHistroy> phlist = pointDao.fandPointHistroyByLevel(conn, mp.getMemberId());
				//if(phlist.size()!=0){
				String strup = upMemberLevel(conn,mp,phlist);
				 sbup.append(strup).append("\n");
				//}
				
				 //0 5
				 //6  10
				 //11 15
				 //16 
				 
			}
		//	System.out.println("pageNo"+pageNo);
			pageNo = pageNo+1;
			totalNum = totalNum - pageSize; 
			
		}
		ss = sbup.toString();
		 
		return ss;
		
	}
	 
	public static void main(String[] args) {
		
		Calendar cal=Calendar.getInstance();
		int x=-1;//or x=-3;
		cal.add(Calendar.DAY_OF_MONTH,x);
		String bzda=new java.text.SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
	//	System.out.println("Bizdate:"+bzda);
		
		XUpLevel upl = new XUpLevel();
		upl.uplevels();

	}

	  

}
