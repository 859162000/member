package com.wanda.mrb.intf.member;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.icebean.core.common.XMLElmEnum;
import com.wanda.mrb.intf.ConstDef;
import com.wanda.mrb.intf.GetEventCmn;
import com.wanda.mrb.intf.SQLConstDef;
import com.wanda.mrb.intf.ServiceBase;
import com.wanda.mrb.intf.member.vo.TFilmInfo;
import com.wanda.mrb.intf.member.vo.TGoodsInfo;
import com.wanda.mrb.intf.utils.FormatTools;
import com.wanda.mrb.intf.utils.ResultQuery;
import com.wanda.mrb.intf.utils.SendMsgUtil;
import com.wanda.mrb.intf.utils.SmsConfigFactory;
import com.wanda.mrb.intf.utils.SqlHelp;

public class RedeemOnline  extends ServiceBase{
	public RedeemOnline(){
		super();
		super.intfCode=ConstDef.CONST_INTFCODE_M_REDEEMONLINE;
		this.timeOutFlag = true;
	}

    protected final org.apache.log4j.Logger log = Logger.getLogger(this.getClass());
	private String balance;
	private String operationType;
	private String orderNo;
	private String productName;//网站礼品
	private String cinema;
	private String cinemaInnerCode = "";
	private String operator;
	private String memberStatus = "";
	private String orderType;
	private Vector<TFilmInfo> films = null;
	private Vector<TGoodsInfo> goods = null;
	private String mobile;
	GetEventCmn gec = new GetEventCmn();
	
	@Override
	protected void bizPerform() throws Exception {
		Connection conn = getDBConnection();
		int mid=this.checkMember(conn);//验证会员是否存在
		String goodName = "";
		StringBuffer showTime=new StringBuffer();
		//替换短信模板中的{goodname}内容
		StringBuffer filmNameBuff=new StringBuffer();
		
		//短信内容是否构造成功标志
		boolean messageFlag=true;
		
		//查询影城内码
		ResultQuery rsq=SqlHelp.query(conn, SQLConstDef.MEMBER_SELECT_INNERCODE_BY_CODE, cinema);
		ResultSet rs=rsq.getResultSet();
		if(rs != null && rs.next()){
			cinemaInnerCode = rs.getString("inner_code"); 
		}else{
			throwsBizException("M070004", "影城编码错误！");
		}
		rsq.free();
		
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
			throwsBizException("M070002", "会员不存在,或无可用积分！");
		}
		if(myBalance<Double.valueOf(balance)){
			throwsBizException("M070001", "积分不足！");
		}
		if(Double.valueOf(balance)<0){
			throwsBizException("M070001", "兑换积分不能为负值");
		}
		rsq.free();

		//查询订单是否扣款成功
		rsq=SqlHelp.query(conn, SQLConstDef.CHECK_TRANS_ORDER, orderNo, cinemaInnerCode, "1", String.valueOf(mid));
		rs=rsq.getResultSet();
		if(rs.next()){
			throwsBizException("M070003", "该订单已经消费成功！");
		}
		rsq.free();
		
		//创建记录  17
		SqlHelp.operate(conn, SQLConstDef.INSERT_POINT_HISTORY,
				"6",//积分操作类型为：6.积分兑换
				"1",//版本号
				String.valueOf(mid),//会员编码
				operationType,//积分来源
				String.valueOf(myBalance),//ORG_POINT_BALANCE:更新t_member_point.exg_point_balance前的值
				"0",//LEVEL_POINT:会员通过交易获得的积分值,仅用作会员升降级判定，不可用于积分兑换，也不可清零
				"0",//ACTIVITY_POINT:会员通过特殊积分活动获得的积分值
				"0",//IS_SYNC_BALANCE
				String.valueOf(myBalance-Double.valueOf(balance)),//POINT_BALANCE:更新t_member_point.exg_point_balance后的值
				"0",//ISDELETE:逻辑删除标识,默认:0 未删除;1删除;其他:非法
				String.valueOf(-Double.valueOf(balance)),//EXCHANGE_POINT:会员通过交易及特殊积分活动获得的积分，可以使用此积分进行积分兑换
				productName,//PRODUCT_NAME:接口传入,网站兑换礼品时，记录产品命名
				orderNo,//ORDER_ID:接口兑换积分记录pos订单号，礼品订单号
				"2",//POINT_TRANS_TYPE:维表.单据类型(1:交易;2:积分兑换;3:特殊积分活动;4积分清零)
				"1",//IS_SUCCEED:交易订单是否成功 1代表成功，0代表订单失效，同时成一条回退记录
				operator,//CREATE_BY
				cinemaInnerCode,
				orderNo);//CINEMA_INNER_CODE:影城内码
		if (!"".equals(orderType) && orderType != null && ("TA".indexOf(orderType) >= 0 )) {
			//创建购票记录
			for (int i = 0; i < films.size(); i++) {
				TFilmInfo fi = films.get(i);
                try {
                    // update by mlh 2016/11/17
                    String showtime = fi.showTime;
                    // 如果影片放映时间不为空
                    if (null != showtime && !showtime.equals("")) {
                        // 格式化放映日期字符串
                        showTime = bulidFilmName(showTime, showtime);
                    }
                    filmNameBuff =filmNameBuff.append(fi.hallCode).append(";时间:").append(showTime)
                                    .append(";影片:").append(fi.filmName).append(";")
                                    .append(fi.ticketNum).append("张;");

                } catch (Exception e) {
                    e.printStackTrace();
                    log.warn("设置短信内容中的filmname格式转换报错");
                    messageFlag = false;
                }
				//10个参数
				//ORDER_ID,TOTAL_AMOUNT,TICKET_NUM,CINEMA_INNER_CODE,MEMBER_ID,FILM_CODE,FILM_NAME,HALL_NUM,SHOW_TIME,BIZ_DATE
				SqlHelp.operate(conn, SQLConstDef.INSERT_TICKET_TRANS_ORDER, 
						orderNo,
						fi.amount,
						fi.ticketNum,
						cinemaInnerCode,
						String.valueOf(mid),
						fi.fileCode,
						fi.filmName,
						fi.hallCode,
						fi.showTime,
						FormatTools.bizDate(fi.showTime),
						fi.ticketPoint);
			}
		}
		
		if (("GA".indexOf(orderType) >= 0 )) {
			String goodTime = FormatTools.getNowTime();
			if (orderType.length()>=1) {//买了卖品
				//创建购卖品记录
				for (int i = 0; i < goods.size(); i++) {
					TGoodsInfo gi = goods.get(i);
					goodName = goodName + gi.saleUnitName + ";";
					//6个参数  ORDER_ID,TOTAL_AMOUNT,CINEMA_INNER_CODE,MEMBER_ID,GOOD_NAME,BIZ_DATE
					SqlHelp.operate(conn, SQLConstDef.INSERT_GOODS_TRANS_ORDER, 
							orderNo,
							gi.saleUnitAmount,
							cinemaInnerCode,
							String.valueOf(mid),
							gi.saleUnitName,
							FormatTools.bizDate(goodTime),
							gi.saleUnitPoint
							);
				}
			} else {//网站礼品
				SqlHelp.operate(conn, SQLConstDef.INSERT_GOODS_TRANS_ORDER, 
						orderNo,
						balance,
						cinemaInnerCode,
						String.valueOf(mid),
						productName,
						FormatTools.bizDate(goodTime),
						String.valueOf(-Double.valueOf(balance))
						);
			}
		}
		
		//积分
		SqlHelp.operate(conn, SQLConstDef.UPDATE_POINT_BALANCE, 
				String.valueOf(-Integer.parseInt(balance)),
				String.valueOf(-Integer.parseInt(balance)),
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
		
//		发送短信  ${时间}{渠道}{积分数}{订单号}
		String nowTime = FormatTools.getNowTime();
		//update by mlh 2016/11/17
        
        String message="";
        
        try {
            // 当操作类型为空时（兑换网站礼品）
            if ("".equals(orderType) || orderType == null) {

                message =gec.getRedeemEvenContent(conn)
                                .getRedeemContent()
                                .replace("${transtime}", nowTime)
                                .replace("${渠道}", "POS")
                                .replace("${订单号}", orderNo)
                                .replace("${goodname}", productName)
                                .replace("${point}", balance)
                                .replace("${balance}",String.valueOf((int) (myBalance - Double.valueOf(balance))))     
                                .replaceAll(" ", "");

            } else {

                message =gec.getRedeemEvenContent(conn)
                            .getRedeemContent()
                            .replace("${transtime}", nowTime)
                            .replace("${渠道}", "POS")
                            .replace("${订单号}", orderNo)
                            .replace("${goodname}", filmNameBuff.toString() + goodName)
                            .replace("${point}", balance)
                            .replace("${balance}",String.valueOf((int) (myBalance - Double.valueOf(balance))))      
                            .replaceAll(" ", "");

            }
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("构造短信内容报错");
            messageFlag = false;
        }
        
        // 如果构造短信内容没有报错
        if (messageFlag) {
            if (super.cinemaCode != null && !"99999999".equals(super.cinemaCode)) {
                // POS
                String cinemaInnerCode = "";
                // update by mlh 无用代码
                /*
                 * try { PreparedStatement ps =
                 * conn.prepareStatement(SQLConstDef.MEMBER_SELECT_INNERCODE_BY_CODE);
                 * ps.setString(1, super.cinemaCode); rs = ps.executeQuery(); while (rs.next()) {
                 * cinemaInnerCode = rs.getString("inner_code"); } } catch (Exception e) {
                 * conn.rollback(); throw e; }
                 */
                
                // update by mlh
                if ("1".equals(msgRedOpen)) {
                    try {
                        SendMsgUtil.sendMsgCheckCodeByEncode(conn, msgSvcIp, msgChannelId, mobile,
                                cinemaInnerCode, message);
                    } catch (Exception e) {
                        SendMsgUtil.sendMsgCheckCodeByEncode(conn, msgSvcIp, msgChannelId, mobile,
                                "002", message);
                    }

                }
            } else {
                if ("1".equals(msgRedOpen)) {
                    SendMsgUtil.sendMsgCheckCodeByEncode(conn, msgSvcIp, msgChannelId, mobile,
                            "002", message);
                }
            }
        } else {
            log.warn("构造短信内容失败，不进行短信发送。");
        }
			
	}
	
    /**
     * 格式化影片放映日期
     * 
     * @param showTime
     * @param FilmShowTime
     * @return 返回中文格式的日期字符
     */
    protected StringBuffer bulidFilmName(StringBuffer showTime, String filmShowTime) {

        log.info("showTime:" + showTime + "  filmShowTime:" + filmShowTime);
        String[] showTimes = null;
        showTimes = filmShowTime.split(",");
        for (String filmtime : showTimes) {
            filmtime = FormatTools.formatTimeForMessage(filmtime);
            showTime = showTime.append(filmtime).append(",");
        }
        if (showTime.length() > 0) {
            showTime.deleteCharAt(showTime.length() - 1);
        }
        log.info("构造后的showTime:" + showTime);
        return showTime;
    }

	@Override
	protected void parseXMLParam(Element root) throws Exception {
		try {
			memberNo = getChildValueByName(root,"MEMBER_NO", 64);
			balance = getChildValueByName(root,"BALANCE", 20);
			operationType = getChildValueByName(root,"OPERATION_TYPE", 1);
			orderNo = getChildValueByName(root,"ORDER_NO", 50);
			cinema = getChildValueByName(root,"CINEMA", 8);
			operator = getChildValueByName(root,"OPERATOR", 50);
			productName = getChildValueByName(root,"PRODUCT_NAME", 50);
			orderType = getChildValueByName(root,"ORDER_TYPE", 1);
			
			if (!"".equals(orderType) && orderType != null && ("TA".indexOf(orderType) >= 0 )) {
				//影片信息，粒度到场次
				TFilmInfo film = null;
				XMLElmEnum enumFilms = null;
				Element elmFilms = null;
				films = new Vector<TFilmInfo>();
				for (enumFilms = new XMLElmEnum(root, ConstDef.CONST_BIZPARAM_FILM_INFO); (enumFilms!=null && enumFilms.hasMoreElements()); ) {
					elmFilms = (Element) enumFilms.nextElement();
					film = new TFilmInfo();
					film.performId = getChildValueByName(elmFilms, "PERFORM_ID", 20);//
					film.fileCode = getChildValueByName(elmFilms, "FILM_CODE", 20);//
					film.filmName = getChildValueByName(elmFilms, "FILM_NAME", 50);//
					film.showTime = getChildValueByName(elmFilms, "SHOW_TIME", 20);//
					film.hallCode = getChildValueByName(elmFilms, "HALL", 500).trim();//
					film.ticketNum = getChildValueByName(elmFilms, "TICKET_NUM", 20);//
					film.amount = getChildValueByName(elmFilms, "AMOUNT", 20);//总金额
					film.ticketPoint = getChildValueByName(elmFilms, "TICKET_POINT", 20);//兑换积分数
					film.subAmount = getChildValueByName(elmFilms, "SUB_AMOUNT", 20);//补差金额
					productName = productName + film.filmName + ";"; 
					
					if(film.performId==null || film.performId.trim().equals("")){
						validateParamNotEmpty(film.fileCode,"影片代码");
						validateParamNotEmpty(film.filmName,"影片名称");
						validateParamNotEmpty(film.showTime,"影片上映时间");
					}
					
					films.add(film);
					
					if (("TA".indexOf(orderType) >= 0 )&&(films.size() == 0 )){
						throwsBizException("M070006", "缺少与订单类型匹配的购票信息");
					}
				}
			}
			
			if (!"".equals(orderType) && orderType != null && ("GA".indexOf(orderType) >= 0 )) {
				//卖品信息，粒度到售卖键
				TGoodsInfo good = null;
				XMLElmEnum enumGoods = null;
				Element elmGoods = null;
				goods = new Vector<TGoodsInfo>();
				for (enumGoods = new XMLElmEnum(root, ConstDef.CONST_BIZPARAM_GOODSINFO); (enumGoods!=null && enumGoods.hasMoreElements()); ) {
					elmGoods = (Element) enumGoods.nextElement();
					good = new TGoodsInfo();
					good.saleUnitName = getChildValueByName(elmGoods, "SALE_UNIT_NAME", 50);//售卖键名称
					good.saleUnitNum = getChildValueByName(elmGoods, "SALE_UNIT_NUM", 20);//售卖键数量
					good.saleUnitAmount = getChildValueByName(elmGoods, "SALE_UNIT_AMOUNT", 20);//售卖键金额
					good.saleUnitPoint = getChildValueByName(elmGoods, "SALE_UNIT_POINT", 20);//售卖键兑换积分数
					good.goodSubAmount = getChildValueByName(elmGoods, "GOODS_SUB_AMOUNT", 20);//售卖键积分加现金补差金额
					productName = productName + good.saleUnitName + ";";
					goods.add(good);
				}
				if (("GA".indexOf(orderType) >= 0 )&&(goods.size() == 0 )){
					throwsBizException("M070007", "缺少与订单类型匹配的卖品信息");
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	protected String composeXMLBody() {
		return null;
	}
	
	public static void main(String args[]){
	    String str="2016-11-18 11:20:00";
	    str=FormatTools.formatTimeForMessage(str);
	    StringBuffer S= new StringBuffer();
	    System.out.println(S.toString());
	    
	}

}