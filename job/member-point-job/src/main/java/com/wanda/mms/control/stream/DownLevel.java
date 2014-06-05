package com.wanda.mms.control.stream;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.wanda.mms.control.stream.dao.LevelHistoryDao;
import com.wanda.mms.control.stream.dao.MemberLevelDao;
import com.wanda.mms.control.stream.dao.PointHistroyDao;
import com.wanda.mms.control.stream.dao.impl.LevelHistoryDaoImpl;
import com.wanda.mms.control.stream.dao.impl.MemberLevelDaoImpl;
import com.wanda.mms.control.stream.dao.impl.PointHistroyDaoImpl;
import com.wanda.mms.control.stream.db.SENDDBConnection;
import com.wanda.mms.control.stream.util.DateUtil;
import com.wanda.mms.control.stream.util.Page;
import com.wanda.mms.control.stream.vo.LevelHistory;
import com.wanda.mms.control.stream.vo.MemberLevel;
import com.wanda.mms.control.stream.vo.PointHistroy;
/**
 *	降级调度
 * @author wangshuai
 * @date 2013-05-24	
 */
public class DownLevel {
	static Logger logger = Logger.getLogger(DownLevel.class.getName());

	Dimdef dd = new Dimdef();
	public String downMemberLevel(Connection conn,MemberLevel ml ,List<PointHistroy> phlist,String date){
		String ss ="";
		PointHistroy phml = new PointHistroy();
		int ticket_count =0;
		long level_point =0;
		for (int i = 0; i < phlist.size(); i++) {
			PointHistroy ph = phlist.get(i);
			if(ml.getMember_id()==ph.getMemberid()){
				ticket_count+=ph.getTicket_Count();//得到要累计的升降级判定票数
				level_point +=ph.getLevel_Point();//定级积分
				   
			}
		
		}
		//Date da = new Date();
	//	String date =// DateUtil.getDateStrss(da);//时间可能是 时分秒
		String yy = (String) date.subSequence(0, 4);
		int intyy = Integer.valueOf(yy);
		int intyyss = Integer.valueOf(yy);
		intyy = intyy +1;
		String yyyy = String.valueOf(intyy);
		String te = "-12-31 23:59:59";
		String time =yyyy+te; 
		String intyyte = "-01-01 00:00:00";
		String yyssmm = intyy+intyyte; 
		Date ysmda = DateUtil.getStringForDate(yyssmm);
		LevelHistoryDao lhdao = new LevelHistoryDaoImpl();
		MemberLevelDao mldao = new  MemberLevelDaoImpl();
		MemberLevel mll = mldao.fandMemberLevelByID(conn, ml.getMember_id());
		LevelHistory lh = new LevelHistory();
		int level = Integer.valueOf(ml.getMem_level());
		long seq = lhdao.seqLevelHistroy(conn);
		//System.out.println(seq);
		//if(mll.getExpire_date())
		
		//da.after(when)
		//da.before();
		if(ysmda.before(DateUtil.getStringForDate(mll.getExpire_date()))){
			//logger.info(ysmda+"  "+DateUtil.getStringForDate(mll.getExpire_date()));
			//System.out.println(ysmda+"  "+DateUtil.getStringForDate(mll.getExpire_date()));
		}else{
	//	if(yyssmm.equals(mll.getExpire_date())){
	//	date1.before(date2)
		switch (level) {
		case 2: if(ticket_count<12&&level_point<500){
			//long seq = lhdao.seqLevelHistroy(conn);
			lh.setLevel_history_id(seq);
			lh.setMem_level(String.valueOf(level-1));//变更后级别=维表.会员级别
			lh.setOrg_mem_level(String.valueOf(level));//变更前级别=维表.会员级别
			//lh.setChg_type(ml.getMem_level());
			lh.setExpire_date(time);
			lh.setOrg_expire_date(ml.getExpire_date());//变更前级别有效期
			lh.setReson_type(dd.RESON_TYPE_QT);//维表.级别变更原因类型(1:老会员回馈,2:会员投诉:3储值卡会员转化,4:其他)
			//lh.setReson_type("member_sys");//级别调整：人工输入；升/降级计算:"member_sys"
			lh.setREASON(dd.MEMBER_SYS);
			lh.setChg_type(dd.DOWN);//级别变更类型(UP:升级,DOWN:降级)
			lh.setMember_id(ml.getMember_id());//会员ID#MEMBER_ID
			lh.setLevel_point(level_point);//判定用户可升级或降级的有效积分数,人工调整时,不记录。用于镜像变更前后值
			lh.setTicket_count(ticket_count);//判定用户可升级或降级的有效积分数,人工调整时,不记录。用于镜像变更前后值
			lh.setIsdelete(0);//逻辑删除标识,默认:0 未删除;1删除;其他:非法
			
			lh.setCreate_By(dd.MEMBER_SYS);//创建人员工ID(系统自动:"member_sys";管理界面:登录员工ID)
			
			lh.setUpdate_By(dd.MEMBER_SYS);//更新人员工ID(系统自动:"member_sys";管理界面:登录员工ID)
			
			lh.setVersion(1);
			mll.setMem_level(lh.getMem_level());
			mll.setExpire_date(time);
			mll.setOrg_level(String.valueOf(level));
			mll.setSet_time(date);
			mll.setTarget_level(String.valueOf(level));
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
			
			mll.setMember_level_history_id(seq);
			lhdao.addLevelHistory(conn, lh,mll);
			//ml.setMem_level(String.valueOf(level+1)); //等级加一
		}else{
		//	long seq = lhdao.seqLevelHistroy(conn);
 
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
			mldao.updateMemberLevelByID(conn, mll);
			//ml.setLast_level_history_id(seq);
			//lhdao.addLevelHistory(conn, lh,ml);
			
		}
			
			break;
		case 3:if(ticket_count<24&&level_point<1000){
		//	long seq = lhdao.seqLevelHistroy(conn);
			lh.setLevel_history_id(seq);
			lh.setMem_level(String.valueOf(level-1));//变更后级别=维表.会员级别
			lh.setOrg_mem_level(String.valueOf(level));//变更前级别=维表.会员级别
			lh.setOrg_expire_date(ml.getExpire_date());//变更前级别有效期
			lh.setReson_type(dd.RESON_TYPE_QT);//维表.级别变更原因类型(1:老会员回馈,2:会员投诉:3储值卡会员转化,4:其他)
			//lh.setReson_type("member_sys");//级别调整：人工输入；升/降级计算:"member_sys"
			lh.setREASON("member_sys");
			lh.setChg_type("DOWN");//级别变更类型(UP:升级,DOWN:降级)
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
			mll.setTarget_level(String.valueOf(level));
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
			 
			mll.setMember_level_history_id(seq);
			lhdao.addLevelHistory(conn, lh,mll);
		}else{		
			mll.setMem_level(String.valueOf(level));
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
		case 4:if(ticket_count<48&&level_point<3000){
		//	long seq = lhdao.seqLevelHistroy(conn);
			lh.setLevel_history_id(seq);
			lh.setMem_level(String.valueOf(level-1));//变更后级别=维表.会员级别
			lh.setOrg_mem_level(String.valueOf(level));//变更前级别=维表.会员级别
			lh.setOrg_expire_date(ml.getExpire_date());//变更前级别有效期
			lh.setReson_type(dd.RESON_TYPE_QT);//维表.级别变更原因类型(1:老会员回馈,2:会员投诉:3储值卡会员转化,4:其他)
			//lh.setReson_type("member_sys");//级别调整：人工输入；升/降级计算:"member_sys"
			lh.setREASON("member_sys");
			lh.setChg_type("DOWN");//级别变更类型(UP:升级,DOWN:降级)
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
			mll.setTarget_level(String.valueOf(level));
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
		}else{			mll.setMem_level(String.valueOf(level));
		//mll.setExpire_date(time);
	//	mll.setOrg_level(String.valueOf(level-1));
	//	mll.setSet_time(date);
	//	mll.setTarget_level(String.valueOf(level+1));
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
		mldao.updateMemberLevelByID(conn, mll);}
			
			break;

		default:
			
			
			break;
		}
	}
		
		return ss;
	}
	
	public String  downlevels(String date){
		String ss="" ;
		
		String yy = (String) date.subSequence(0, 4);
		int intyy = Integer.valueOf(yy);
		String yyyy = String.valueOf(intyy);
		String te = "-12-31 23:59:59";
		String time =yyyy+te; 
	//	System.out.println(time+"downlevels"+date);

		SENDDBConnection db=SENDDBConnection.getInstance();	
	    Connection conn = null;
		conn=db.getConnection();
		 
		PointHistroyDao pointDao = new PointHistroyDaoImpl();;
		MemberLevelDao memdao = new MemberLevelDaoImpl();
		String sup ="";
		StringBuffer sbup = new StringBuffer();
		//得到总条数
		Long totalNum = memdao.fandMemberLevelTotalNum(conn);
		Page page = new Page();  
		long pageSize =40;
		long pageNo = 0;
		while (totalNum>0) {
			if(pageNo==0){
				page.setPage(pageNo*pageSize);
			}else{
				page.setPage((pageNo*pageSize)+1);
			}
			page.setPageSize(pageSize +page.getPageSize());
			List<MemberLevel> mplist = memdao.fandMemberLevelPage(conn,page);//查询会员级别
			for (int i = 0; i < mplist.size(); i++) {
				MemberLevel mp = mplist.get(i);
				//MemberPoint memp =	memdao.fandMemberPointByID(conn, mp.getMemberId());
				//根据会员ID 与时间年份的1月1日到12月31日
				List<PointHistroy> phlist = pointDao.fandPointHistroyByLevel(conn, date, mp.getMember_id());
				
				String strDown = downMemberLevel(conn,mp,phlist,date);//降级方法
				 sbup.append(strDown).append("\n");
				 
			}
			
			pageNo = pageNo+1;
			totalNum = totalNum - pageSize; 
			
		}
		ss = sbup.toString();
		// System.out.println("downlevels"+ss);
		return ss;
		
	}
	
	public static void main(String[] args) {
		DownLevel down = new DownLevel();//创建降级调度
		Date da = new Date();//创建时间
		String datetime = DateUtil.getDateStrss(da);//时间可能是 时分秒
		String date =DateUtil.getDateStr(da);
		String yy = (String) date.subSequence(0, 4);
		String mm = date.substring(4, 6);
		int intyy = Integer.valueOf(yy);
		 intyy = intyy-1;
		String yyyy = String.valueOf(intyy);
		String te = "-12-31 23:59:59";
		String time =yyyy+te; 
		//System.out.println(time+"downlevels"+date);
		if(mm.equals("01")){//调度时间如果过了有可能会把明年的用户全都降一级，那可就悲剧了。所以加个判断
			down.downlevels(time);//根据去年的时间执行降级方法
		}else{
			down.downlevels(datetime);//查询今年的时间执行降级方法
		}
	}

}
