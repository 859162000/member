package com.wanda.mms.control.stream.service;

import java.sql.Connection;
import java.util.Date;

import com.wanda.mms.control.stream.dao.PointHistroyDao;
import com.wanda.mms.control.stream.dao.impl.PointHistroyDaoImpl;
import com.wanda.mms.control.stream.util.DateUtil;
import com.wanda.mms.control.stream.vo.PointHistroy;

/**
 *	积分扣减
 * @author wangshuai
 * @date 2013-05-28	
 */
public class PointsDeduction {
	PointHistroyDao phdao = new PointHistroyDaoImpl();
	
	/***
	 * 积分扣减
	 * @param conn  连接
	 * @param memberid  会员ID
	 * @param exchange_point  可兑换积分
	 * @param org_Point_Balance 变化前可兑换积分余额
	 * @param point_sys  (1:POS;2:网站;3:会员系统;Others 其他)
	 * @param point_trans_code 交易单号(POS/网站)/积分兑换交易单号/特殊积分规则ID)
	 * @param point_trans_code_web 如果是网站，就传网站订单号  如不是可为空
	 * @param create_by  创建人员ID
	 * @param productName 商品名称（新加字段）
	 * @return
	 */
	public String deductionPoints(Connection conn,long memberid,long exchange_point ,long org_Point_Balance,String point_sys,String point_trans_code,String point_trans_code_web,String create_by,String productName ) 
	{
		String ss ="";
		Date da = new Date();
		String date = DateUtil.getDateStrss(da);//时间可能是 时分秒
		String yy = (String) date.subSequence(0, 4);
		int intyy = Integer.valueOf(yy);
		intyy = intyy +1;
		String yyyy = String.valueOf(intyy);
		String te = "-12-31 23:59:59";
		String time =yyyy+te; 
		PointHistroy ph = new PointHistroy();
		ph.setMemberid(memberid);//需要传进来 会员ID
		ph.setMember_point_id(memberid);//需要传进来 会员ID
		ph.setExchange_Point(exchange_point);//需要传进来  可兑换积分#EXCHANGE_POINT
		ph.setExg_expire_point_balance(exchange_point);//当年即将过期可兑换积分
		ph.setPoint_Sys(point_sys);//需要传进来 维表.源系统(1:POS;2:网站;3:会员系统;Others 其他)
		ph.setPoint_Trans_Code(point_trans_code);//需要传进来 单号(交易单号(POS/网站)/积分兑换交易单号/特殊积分规则ID)
		ph.setPoint_Trans_Code_Web(point_trans_code_web);//需要传进来 如果point_trans_type是交易,point_sys是网站，则在此记录网站订单号关联的POS订单
		ph.setCreate_By(create_by);//?创建人员ID(系统自动:"member_sys";管理界面:登录员工ID)
		ph.setUpdate_By(create_by);//更新人员ID(系统自动:"member_sys";管理界面:登录员工ID)
		ph.setExg_expire_point_balance(exchange_point);
		ph.setOrg_Point_Balance(org_Point_Balance);
		ph.setPoint_Balance(org_Point_Balance+exchange_point);
		ph.setSetTime(date);//执行积分的时间
		ph.setLevel_Point(0);//定级积分#LEVEL_POINT
		ph.setTicket_Count(0);//升降级判定票数#TICKET_COUNT
		ph.setActivity_Point(0);//非定级积分#ACTIVITY_POINT
		ph.setExchange_Point_Expire_Time(time);
		ph.setPoint_Type("6");//维表.积分操作类型(1:购买;2:礼品;3:奖励;4调整;5:会员终止;6:积分兑换;其他值:其他）
		ph.setPoint_Trans_Type("2");//维表.单据类型(1:交易;2:积分兑换;3:特殊积分活动;4积分清零)
		ph.setAdj_reason_type("");//维表.积分调整原因类型
		ph.setAdj_reason("");
		ph.setIsdelete(0);
		ph.setVersion(1);
		ph.setProduct_name(productName);
		ss  = phdao.addPointHistroy(conn, ph);
		return ss;
	}
	 
}
