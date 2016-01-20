package com.wanda.mrb.intf.member;

import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import org.w3c.dom.Element;
import com.wanda.mrb.intf.ConstDef;
import com.wanda.mrb.intf.SQLConstDef;
import com.wanda.mrb.intf.ServiceBase;
import com.wanda.mrb.intf.utils.FormatTools;
import com.wanda.mrb.intf.utils.ResultQuery;
import com.wanda.mrb.intf.utils.SendMsgUtil;
import com.wanda.mrb.intf.utils.SmsConfigFactory;
import com.wanda.mrb.intf.utils.SqlHelp;
/**
 * 积分奖励
 * @author chenxm
 *@date 2014-11-18
 */
public class Reward  extends ServiceBase{
	public Reward(){
		super();
		super.intfCode=ConstDef.CONST_INTFCODE_M_REWARD;
		this.timeOutFlag = true;
	}
	private String balance;
	private String operationType;
	private String orderNo;
	private String productName = "积分奖励";//网站礼品
	private String cinema;
	private String cinemaInnerCode = "";
	private String operator;
	private String expireDate="";
	private String memberStatus = "";
	private String mobile;
	private String activityName;//奖励积分原因
	@Override
	protected void bizPerform() throws Exception {
		Connection conn = getDBConnection();
		int mid=this.checkMember(conn);//验证会员是否存在
		
		//查询影城内码
		ResultQuery rsq=SqlHelp.query(conn, SQLConstDef.MEMBER_SELECT_INNERCODE_BY_CODE, cinema);
		ResultSet rs = null;
		if("99999999".equals(cinema)){
			cinemaInnerCode = "002";
		}else{
			rs=rsq.getResultSet();
			if(rs != null && rs.next()){
				cinemaInnerCode = rs.getString("INNER_CODE"); 
			}else{
				throwsBizException("M070004", "影城编码错误！");
			}
			rsq.free();
		}
		//获取会员可用积分
		double myBalance=0;
		rsq=SqlHelp.query(conn, SQLConstDef.QUERY_POINT_BALANCE, this.memberNo);
		rs=rsq.getResultSet();
		if(rs !=null && rs.next()){
			memberStatus = rs.getString("STATUS");
			if ("1".equals(memberStatus)) {
				myBalance=rs.getDouble("EXG_POINT_BALANCE");
				mobile = rs.getString("MOBILE");
			} else {
				throwsBizException("M070005", "该会员已禁用！");
			}
		}else{
			throwsBizException("M070002", "会员不存在");
		}
		if(Double.valueOf(balance)<0){
			throwsBizException("M070001", "积分奖励不能为负值");
		}
		rsq.free();

		//查询订单是否已经增加过积分
		rsq=SqlHelp.query(conn, SQLConstDef.CHECK_TRANS_ORDER, orderNo, cinemaInnerCode, "1", String.valueOf(mid));
		rs=rsq.getResultSet();
		if(rs.next()){
			throwsBizException("M070003", "该订单已经奖励积分！");
		}
		rsq.free();
		//积分历史记录
		SqlHelp.operate(conn, SQLConstDef.INSERT_POINT_HISTORY_REWARD,
				"3",//积分操作类型为：维表废弃.积分操作类型(1 购买 2 赠送 3 奖励 4 调整 5 过期 6 线下兑换（目前所有兑换均为6) 7 网站积分兑换(未用)  8 其他,）
				expireDate+" 23:59:59",
				"1",//版本号
				String.valueOf(mid),//会员编码
				operationType,//积分来源
				String.valueOf(myBalance),//ORG_POINT_BALANCE:更新t_member_point.exg_point_balance前的值
				"0",//LEVEL_POINT:会员通过交易获得的积分值,仅用作会员升降级判定，不可用于积分兑换，也不可清零
				"0",//ACTIVITY_POINT:会员通过特殊积分活动获得的积分值
				"0",//IS_SYNC_BALANCE
				String.valueOf(myBalance+Double.valueOf(balance)),//POINT_BALANCE:更新t_member_point.exg_point_balance后的值
				"0",//ISDELETE:逻辑删除标识,默认:0 未删除;1删除;其他:非法
				String.valueOf(Double.valueOf(balance)),//EXCHANGE_POINT:会员通过交易及特殊积分活动获得的积分，可以使用此积分进行积分兑换
				productName,//PRODUCT_NAME:写死，积分奖励
				orderNo,//ORDER_ID:接口兑换积分记录pos订单号，礼品订单号
				"3",//POINT_TRANS_TYPE:维表.单据类型(1:交易;2:积分兑换;3:特殊积分活动;4积分清零)
				"1",//IS_SUCCEED:交易订单是否成功 1代表成功，0代表订单失效，同时成一条回退记录
				operator,//CREATE_BY
				cinemaInnerCode,//CINEMA_INNER_CODE:影城内码
				orderNo,
				activityName);//订单号
		
		//记录此活动获取积分，在T_GOODS_TRANS_ORDER
		SqlHelp.operate(conn, SQLConstDef.INSERT_GOODS_TRANS_ORDER, 
						orderNo,
						balance,
						cinemaInnerCode,
						String.valueOf(mid),
						productName,
						FormatTools.bizDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date())),
						String.valueOf(Double.valueOf(balance))
						);
		
		//修改会员积分
		SqlHelp.operate(conn, SQLConstDef.UPDATE_POINT_BALANCE, 
				String.valueOf(Integer.parseInt(balance)),
				String.valueOf(Integer.parseInt(balance)),
				String.valueOf(mid));
		
		//获取短信平台代理地址和通道号
		String msgSvcIp = "";
		String msgChannelId = "";
		String msgRedOpen = "";
		
		Map<String,String> msgConfigMap = SmsConfigFactory.getSmsConfigInstance(conn);
//		msgSvcIp = msgConfigMap.get("MSG_MQ_IP");
		msgSvcIp = msgConfigMap.get("MSG_NEW_PROXY_IP");
		msgChannelId = msgConfigMap.get("MSG_CHANNEL_ID");
		msgRedOpen = msgConfigMap.get("MSG_RED_OPEN");
		
		if ("1".equals(msgRedOpen)) {
			try {
				SendMsgUtil.sendMsgCheckCode(conn, msgSvcIp, msgChannelId, mobile, cinemaInnerCode, "尊敬的会员，恭喜您成功获得"+balance+"分，您账户当前积分是"+String.valueOf((int)(myBalance+Double.valueOf(balance)))+"分。");
			} catch (Exception e) {
				SendMsgUtil.sendMsgCheckCode(conn, msgSvcIp, msgChannelId, mobile, "002", "尊敬的会员，恭喜您成功获得"+balance+"分，您账户当前积分是"+String.valueOf((int)(myBalance+Double.valueOf(balance)))+"分。");
			}
		}	
	}

	@Override
	protected void parseXMLParam(Element root) throws Exception {
		try {
			this.memberNo = getChildValueByName(root,"MEMBER_NO", 64);
			balance = getChildValueByName(root,"REWARD_POINT", 20);
			operationType = getChildValueByName(root,"OPERATION_TYPE", 1);//是否存在歧义？待确定，数据库设计1表示pos2表示网站，但历史数据正好相反
			orderNo = getChildValueByName(root,"ORDER_NO", 50);
			cinema = getChildValueByName(root,"CINEMA", 8);
			operator = getChildValueByName(root,"OPERATOR", 50);
			expireDate = getChildValueByName(root,"POINT_EXPIRE_TIME", 50);
			activityName = getChildValueByName(root,"ACTIVITY_NAME", 100);
			try {
				expireDate = FormatTools.formatDate(expireDate);
			} catch (Exception e) {
				throw e;
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	protected String composeXMLBody() {
		return null;
	}

}