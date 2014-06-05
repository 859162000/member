package com.wanda.mms.control.stream;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.wanda.mms.control.stream.dao.MemberLevelDao;
import com.wanda.mms.control.stream.dao.MemberPointDao;
import com.wanda.mms.control.stream.dao.PointHistroyDao;
import com.wanda.mms.control.stream.dao.T_DimdefDao;
import com.wanda.mms.control.stream.dao.T_MemberDao;
import com.wanda.mms.control.stream.dao.T_ticket_trans_orderDao;
import com.wanda.mms.control.stream.dao.impl.MemberLevelDaoImpl;
import com.wanda.mms.control.stream.dao.impl.MemberPointDaoImpl;
import com.wanda.mms.control.stream.dao.impl.PointHistroyDaoImpl;
import com.wanda.mms.control.stream.dao.impl.T_DimdefDaoImpl;
import com.wanda.mms.control.stream.dao.impl.T_MemberDaoImpl;
import com.wanda.mms.control.stream.dao.impl.T_ticket_trans_orderDaoImpl;
import com.wanda.mms.control.stream.util.DateUtil;
import com.wanda.mms.control.stream.util.Page;
import com.wanda.mms.control.stream.vo.MemberLevel;
import com.wanda.mms.control.stream.vo.MemberPoint;
import com.wanda.mms.control.stream.vo.PointHistroy;
import com.wanda.mms.control.stream.vo.T_Dimdef;
import com.wanda.mms.control.stream.vo.T_Member;
import com.wanda.mms.control.stream.vo.T_ticket_trans_order;

/**
 *	基本积分累计计算
 * @author wangshuai
 * @date 2013-05-28	
 */
public class BasicPoint {
	static Logger logger = Logger.getLogger(BasicPoint.class.getName());
	
	Dimdef dd = new Dimdef();
	public String basicpointcount(Connection conn){
		logger.info("Entering basicpointcount()");
		String ss = "";
		int point = 0;
		
		  
		T_ticket_trans_orderDao ttdao = new T_ticket_trans_orderDaoImpl();
		//查的时间是两天前的数据对吧。也就是说如查今天是2013-05-28 晚上九点跑 
		//那么我应该去查 2013-05-27 号得所有数据。
		//分页查某个表，得到这个会员今天的花的钱数，手机号，票数
		//在查询这个手机号的卖品 钱数 相加 得到AA T_segment
		//AA等于定级积分 等于可对换积分 加上有效期 票数  
		//PointHistroyDao pointDao = new PointHistroyDaoImpl(); 
		//MemberLevelDao memdao = new MemberLevelDaoImpl();
		//String sup ="";
		StringBuffer sbup = new StringBuffer();
		//得到总条数
		String is_point = dd.IS_POINT;//要从维表里取出来
		point=Integer.valueOf(is_point);
		 
		 
		Long totalNum = ttdao.countT_ticket_trans_order(conn,is_point);
		Page page = new Page();  
		long pageSize =40;
		long pageNo = 0;
		while (totalNum>0) {
			page.setPage(pageNo);
			page.setPageSize(pageSize);
		//	List<MemberLevel> mplist = ttdao..fandMemberLevelPage(conn,page);//有问题想想明天解决
			List<T_ticket_trans_order> ttlist = ttdao.fandT_ticket_trans_orderPage(conn, point, page);
			
			String ttss=pointcount(conn,ttlist);
			 
				 sbup.append(ttss).append("\n");	
			//}
			//pageNo = pageNo+1;
			totalNum = totalNum - pageSize; 	
		}
		ss = sbup.toString();
		logger.info("Exiting basicpointcount()");
		return ss;
	}
	
	/**
	 * 跟据会员交易记录添加积分流水
	 * @param conn
	 * @param ttlist
	 * @return
	 */
	public String pointcount(Connection conn,List<T_ticket_trans_order> ttlist){
		logger.info("Entering pointcount()");
		String ss = "";
		Date da = new Date();
		String date = DateUtil.getDateStrss(da);//时间可能是 时分秒
		String yy = (String) date.subSequence(0, 4);
		int intyy = Integer.valueOf(yy);
		intyy = intyy +1;
		String yyyy = String.valueOf(intyy);
		String te = "-12-31 23:59:59";
		String time =yyyy+te;
		
		
		//要去维表中查询出 一条定好的基本积分计算规则。根据规则参与积分的计算
		//T_ext_point_rule
		
		//定级积分，T_ticket_trans_order ，非定级积分，可兑换积分，可兑换积分有效期，
		//升降机判定票数，积分操作类型，是1 维表.积分操作类型(1:购买;2:礼品;3:奖励;4调整;5:会员终止;6:积分兑换;其他值:其他）
		//积分操作源系统，维表.源系统(1:POS;2:网站;3:会员系统;Others 其他)
		//积分操作源单类型，是1   维表.单据类型(1:交易;2:积分兑换;3:特殊积分活动;4积分清零)
		//积分操作源唯一标识，单号(交易单号(POS/网站)/积分兑换交易单号/特殊积分规则ID)
		//积分操作源唯一标识WEBPOS POS订单号??  如果point_trans_type是交易,point_sys是网站，则在此记录网站订单号关联的POS订单
		PointHistroyDao pointDao = new PointHistroyDaoImpl();//会员积分历史DAO
		MemberPointDao memDao = new MemberPointDaoImpl();//会员积分DAO
		T_MemberDao t_memberdao=new T_MemberDaoImpl();//会员表DAO
	 
		T_ticket_trans_orderDao ttdao = new T_ticket_trans_orderDaoImpl();		
		T_DimdefDao tddao = new T_DimdefDaoImpl();
		T_Dimdef td = tddao.fandT_DimdefByID(conn, Long.valueOf(dd.POINT), dd.POINT_QJGZ);
		double de = Double.valueOf(td.getCode());
		for (int i = 0; i < ttlist.size(); i++) {
			T_ticket_trans_order tt = ttlist.get(i);//会员影票交易数据
			// memDao.fandMemberPointByID(conn, memberId)
		//	T_Member tm = t_memberdao.fandT_MemberByMOBILE(conn,  tt.getMember_num());//会员手机号
			T_Member tm = t_memberdao.fandT_MemberByID(conn,tt.getMemberId());//根据会员ID查询会员基本信息
			if(tm.getMemberId()==0){//如果查询失败
				tm = t_memberdao.fandT_MemberByMOBILE(conn,  tt.getMember_num());//根据会员手机号查询会员基本信息
			}
			
			MemberPoint mp =	memDao.fandMemberPointByID(conn, tm.getMemberId());
			PointHistroy ph = new PointHistroy();
			ph.setMemberid(tm.getMemberId());//需要传进来 会员ID
			ph.setMember_point_id(tm.getMemberId());//需要传进来 会员ID
			
			//还要加上卖品
			//在这加上只算4张最高价的影票
			double qjgz=tt.getPoint_amount()*de;
			String ss1 = String.valueOf(qjgz);
			BigDecimal d5 = new BigDecimal( ss1  );
			String s6 =d5.setScale(0,BigDecimal.ROUND_DOWN).toString();
			//System.out.println(s6);
			long a = Long.valueOf(s6);
			ph.setExchange_Point(a);//还要加上卖品  需要传进来  可兑换积分#EXCHANGE_POINT
			
			ph.setOrg_Point_Balance(mp.getExg_Point_Balance());//从会员积分表中得到当前可对换积分余额放到变化前可兑换积分余额字段中
			ph.setPoint_Balance(mp.getExg_Point_Balance()+a);//
			ph.setExg_expire_point_balance(0);//当年即将过期可兑换积分
			//是不是会员系统
			ph.setPoint_Sys(dd.POINT_SYS_HYXT);//需要传进来 维表.源系统(1:POS;2:网站;3:会员系统;Others 其他)
			
			//这两个字段有什么区别
			ph.setPoint_Trans_Code(tt.getOrder_id());//需要传进来 单号(交易单号(POS/网站)/积分兑换交易单号/特殊积分规则ID)
			//ph.setPoint_Trans_Code_Web(tt.getOrder_id());//需要传进来 如果point_trans_type是交易,point_sys是网站，则在此记录网站订单号关联的POS订单
			
			
			ph.setCreate_By("member_sys");//?创建人员工ID(系统自动:"member_sys";管理界面:登录员工ID)
			ph.setUpdate_By("member_sys");//更新人员工ID(系统自动:"member_sys";管理界面:登录员工ID)
			ph.setSetTime(date);//执行积分的时间
			
			
			ph.setLevel_Point(a);//定级积分#LEVEL_POINT
			ph.setTicket_Count(tt.getTicket_num());//升降级判定票数#TICKET_COUNT
			ph.setActivity_Point(0);//非定级积分#ACTIVITY_POINT
			ph.setExchange_Point_Expire_Time(time);//可兑换积分有效期
			ph.setCinema_inner_code(tt.getCinema_inner_code());//积分兑换发生在哪个影城，积分规则计算是哪个影城的交易送的积分
			 
			
			//要从维表中查出来
			//t_dimdefdao 
			ph.setPoint_Type(dd.POINT_TYPE_GM);//维表.积分操作类型(1:购买;2:礼品;3:奖励;4调整;5:会员终止;6:积分兑换;其他值:其他）
			ph.setPoint_Trans_Type(dd.POINT_TRANS_TYPE_JY);//维表.单据类型(1:交易;2:积分兑换;3:特殊积分活动;4积分清零)
			ph.setAdj_reason_type("");//维表.积分调整原因类型
			ph.setAdj_reason("基本积分计算:影票计算");//积分调整的原因
			ph.setIsdelete(0);
			ph.setVersion(1);
			if(tm.getMemberId()!=0){//如果会员ID不会空添加会员积分历史记录
			ss  = pointDao.addPointHistroy(conn, ph);
			}
			if(ss.equals("111")){
				
				tt.setIs_point("1");//要从维表中查出来
				tt.setPoint(a);
				ttdao.updateT_ticket_trans_order(conn, tt);
				
			}
		}
		logger.info("Exiting pointcount()");
		return ss;
	}
	

}
