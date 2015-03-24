package com.wanda.ccs.member.segment.defimpl;

import static com.wanda.ccs.member.segment.SegmentConstants.Schemas.MBRODS;
import static com.wanda.ccs.member.segment.SegmentConstants.Schemas.RPT2;
import static com.wanda.ccs.sqlasm.CriteriaParserBuilder.QUERY_PARAGRAPHS_SEGMENT;
import static com.wanda.ccs.sqlasm.CriteriaParserBuilder.newParser;
import static com.wanda.ccs.sqlasm.CriteriaParserBuilder.notEmpty;
import static com.wanda.ccs.sqlasm.expression.ExpressionClauseBuilder.newExpression;
import static com.wanda.ccs.sqlasm.expression.ExpressionClauseBuilder.newPlain;
import static com.wanda.ccs.sqlasm.expression.ExpressionClauseBuilder.newValue;

import java.util.HashMap;
import java.util.Map;

import com.wanda.ccs.sqlasm.Clause;
import com.wanda.ccs.sqlasm.CriteriaParser;
import com.wanda.ccs.sqlasm.DataType;

public class SegmentCriteriaDef {
	
	private Map<String, String> orderByMap;
	
	private CriteriaParser parser;
	
	public SegmentCriteriaDef() {

		FilmCompositeParser filmCom = new FilmCompositeParser();
		ActivityCompositeParser activityCom = new ActivityCompositeParser();
		CinemaCompositeParser cinemaCom = new CinemaCompositeParser();
		ConItemCompositeParser conitemCom = new ConItemCompositeParser();
		
		// 假日映射
//		Map<Object, Object> holidayMapper = new HashMap<Object, Object>();
//		holidayMapper.put("1", "'chunjie'");		// 春节
//		holidayMapper.put("2", "'wuyi'");
//		holidayMapper.put("3", "'shiyi'");
//		holidayMapper.put("4", "'duanwu'");
//		holidayMapper.put("5", "'yuandan'");
//		holidayMapper.put("6", "'zhongqiu'");
//		holidayMapper.put("7", "'qingming'");
		
		//首映场观影 (对应T_DIMDEF表中typeid=106)
		Map<Object, Object> filePlanTypeMapper = new HashMap<Object, Object>();
		filePlanTypeMapper.put("1", " =3 ");		//是（首映场观影）
		filePlanTypeMapper.put("2", " <> 3 ");		//否（除首映场观影之外的所有）
		
		// 级别变更类型映射
//		Map<Object, Object> levelChangeMapper = new HashMap<Object, Object>();
//		levelChangeMapper.put("1", "'UP'"); 			//升级
//		levelChangeMapper.put("2", "'DOWN'"); 			//降级
		
		// 波次响应类型映射
		Map<Object, Object> responseMapper = new HashMap<Object, Object>();
		responseMapper.put("1", "history.HAS_RESPONSE=1");
		responseMapper.put("2", "history.HAS_RESPONSE2=1");
		
		Map<Object, Object> booleanMapper = new HashMap<Object, Object>();
		booleanMapper.put("0", "is null");
		booleanMapper.put("1", "is not null");	
		
		Map<Object, Object> booleanTransAllMapper = new HashMap<Object, Object>();
		booleanTransAllMapper.put("0", " <> -999 ");//否
		booleanTransAllMapper.put("1", " = -999");	//是
		
		Map<Object, Object> booleanChannelMapper = new HashMap<Object, Object>();
		booleanChannelMapper.put("0", " not in ('05','06','07') ");//否
		booleanChannelMapper.put("1", " in ('05','06','07') ");//是
		
		
		Clause consale = newPlain().in("from").output(RPT2+".T_F_CON_MEMBER_SALE consale")
				.depends(newPlain().in("where").output("member.MEMBER_KEY=consale.MEMBER_KEY"));
		
		Clause consaleNot = newPlain().in("notexistsConFrom").output(RPT2+".T_F_CON_MEMBER_SALE consaleNot")
		.depends(newPlain().in("notexistsConWhere").output("member.MEMBER_KEY=consaleNot.MEMBER_KEY"));

		//Clause consale_date = new SqlClause("from", DW+".T_D_CON_DATE consale_date")
		//		.depends(new SqlClause("where").output("consale_date.DATE_KEY=consale.BOOK_DATE_KEY"))
		//		.depends(consale);
		
		Clause consale_date = newPlain().in("where").output("consale_date.DATE_KEY=consale.BOOK_BIZ_DATE_KEY")
				.depends(newPlain().in("from").output(RPT2+".T_D_CON_DATE consale_date"))
				.depends(consale);
		//未发生卖品交易
		Clause consale_dateNot = newPlain().in("notexistsConWhere").output("consale_dateNot.DATE_KEY=consaleNot.BOOK_BIZ_DATE_KEY")
				.depends(newPlain().in("notexistsConFrom").output(RPT2+".T_D_CON_DATE consale_dateNot"))
				.depends(consaleNot);
		
		Clause consale_hour = newPlain().in("from").output(RPT2+".T_D_CON_HOUR consale_hour")
				.depends(newPlain().in("where").output("consale_hour.HOUR_KEY=consale.BOOK_HOUR_KEY"))
				.depends(consale);
		//未发生卖品交易
		Clause consale_hourNot = newPlain().in("notexistsConFrom").output(RPT2+".T_D_CON_HOUR consale_hourNot")
				.depends(newPlain().in("notexistsConWhere").output("consale_hourNot.HOUR_KEY=consaleNot.BOOK_HOUR_KEY"))
				.depends(consaleNot);
		
		Clause consale_cinema = newPlain().in("from").output(RPT2+".T_D_CON_CINEMA consale_cinema")
				.depends(newPlain().in("where").output("consale_cinema.CINEMA_KEY = consale.CINEMA_KEY"))
				.depends(consale);
		//未发生卖品交易
		Clause consale_cinemaNot = newPlain().in("notexistsConFrom").output(RPT2+".T_D_CON_CINEMA consale_cinemaNot")
				.depends(newPlain().in("notexistsConWhere").output("consale_cinemaNot.CINEMA_KEY = consaleNot.CINEMA_KEY"))
				.depends(consaleNot);
		
		Clause member_cinema = newPlain().in("from").output(RPT2+".T_D_CON_CINEMA member_cinema")
				.depends(newPlain().in("where").output("member_cinema.CINEMA_KEY = member.CINEMA_KEY"));
		
		Clause consale_cate = newPlain().in("from").output(RPT2+".T_D_CON_CS_CLASS consale_cate")
				.depends(newPlain().in("where").output("consale.SALE_CLASS_KEY = consale_cate.SALE_CLASS_KEY"))
				.depends(consale);
		//未发生卖品交易
		Clause consale_cateNot = newPlain().in("notexistsConFrom").output(RPT2+".T_D_CON_CS_CLASS consale_cateNot")
				.depends(newPlain().in("notexistsConWhere").output("consaleNot.SALE_CLASS_KEY = consale_cateNot.SALE_CLASS_KEY"))
				.depends(consaleNot);
		
		Clause consale_item = newPlain().in("from").output(RPT2+".T_D_CON_CS_SALE_ITEM consale_item")
				.depends(newPlain().in("where").output("consale.SALE_ITEM_KEY = consale_item.SALE_ITEM_KEY"))
				.depends(consale);
		//未发生卖品交易
		Clause consale_itemNot = newPlain().in("notexistsConFrom").output(RPT2+".T_D_CON_CS_SALE_ITEM consale_itemNot")
				.depends(newPlain().in("notexistsConWhere").output("consaleNot.SALE_ITEM_KEY = consale_itemNot.SALE_ITEM_KEY"))
				.depends(consaleNot);
		//卖品支付方式
		Clause consale_paymethod = newPlain().in("from").output(RPT2+".T_D_CON_PAY_METHOD consale_paymethod")
				.depends(newPlain().in("where").output("consale_paymethod.PAY_METHOD_KEY = consale.PAY_METHOD_KEY"))
				.depends(consale);
		//卖品支付方式未发生
		Clause consale_paymethodNot = newPlain().in("notexistsConFrom").output(RPT2+".T_D_CON_PAY_METHOD consale_paymethodNot")
				.depends(newPlain().in("notexistsConWhere").output("consale_paymethodNot.PAY_METHOD_KEY = consaleNot.PAY_METHOD_KEY"))
				.depends(consaleNot);
		
		//总交易
		Clause transSalesAll = newPlain().in("from").output(RPT2+".T_F_CON_MEMBER_CINEMA transSalesAll")
				.depends(newPlain().in("where").output("member.MEMBER_KEY=transSalesAll.MEMBER_KEY"));
		
		Clause transSalesAllNot = newPlain().in("notexistsTransAllFrom").output(RPT2+".T_F_CON_MEMBER_CINEMA transSalesAllNot")
		.depends(newPlain().in("notexistsTransAllWhere").output("member.MEMBER_KEY=transSalesAllNot.MEMBER_KEY"));
		
		//Clause transSalesAll_date = newPlain().in("from").output(DW+".T_D_CON_DATE transSalesAll_date")
		//		.depends(newPlain().in("where").output("transSalesAll_date.DATE_KEY=transSalesAll.DATE_KEY"))
		//		.depends(transSalesAll);
		
		Clause transSalesAll_date = newPlain().in("where").output("transSalesAll_date.DATE_KEY=transSalesAll.DATE_KEY")
				.depends(newPlain().in("from").output(RPT2+".T_D_CON_DATE transSalesAll_date")).depends(transSalesAll);
		//未交易
		Clause transSalesAll_dateNot = newPlain().in("notexistsTransAllWhere").output("transSalesAll_dateNot.DATE_KEY=transSalesAllNot.DATE_KEY")
		.depends(newPlain().in("notexistsTransAllFrom").output(RPT2+".T_D_CON_DATE transSalesAll_dateNot")).depends(transSalesAllNot);
		
		Clause transSalesAll_hour = newPlain().in("from").output(RPT2+".T_D_CON_HOUR transSalesAll_hour")
				.depends(newPlain().in("where").output("transSalesAll_hour.HOUR_KEY=transSalesAll.HOUR_KEY"))
				.depends(transSalesAll);
		//未交易
		Clause transSalesAll_hourNot = newPlain().in("notexistsTransAllFrom").output(RPT2+".T_D_CON_HOUR transSalesAll_hourNot")
		.depends(newPlain().in("notexistsTransAllWhere").output("transSalesAll_hourNot.HOUR_KEY=transSalesAllNot.HOUR_KEY")).depends(transSalesAllNot);
		
		Clause transSalesAll_cinema = newPlain().in("from").output(RPT2+".T_D_CON_CINEMA transSalesAll_cinema")
				.depends(newPlain().in("where").output("transSalesAll_cinema.CINEMA_KEY = transSalesAll.CINEMA_KEY"))
				.depends(transSalesAll);
		//未交易
		Clause transSalesAll_cinemaNot = newPlain().in("notexistsTransAllFrom").output(RPT2+".T_D_CON_CINEMA transSalesAll_cinemaNot")
		.depends(newPlain().in("notexistsTransAllWhere").output("transSalesAll_cinemaNot.CINEMA_KEY = transSalesAllNot.CINEMA_KEY")).depends(transSalesAllNot);
		
		Clause transSalesAll_paymethod = newPlain().in("from").output(RPT2+".T_D_CON_PAY_METHOD transSalesAll_paymethod")
		.depends(newPlain().in("where").output("transSalesAll_paymethod.PAY_METHOD_KEY = transSalesAll.PAY_METHOD_KEY"))
		.depends(transSalesAll);
		//未交易
		Clause transSalesAll_paymethodNot = newPlain().in("notexistsTransAllFrom").output(RPT2+".T_D_CON_PAY_METHOD transSalesAll_paymethodNot")
		.depends(newPlain().in("notexistsTransAllWhere").output("transSalesAll_paymethodNot.PAY_METHOD_KEY = transSalesAllNot.PAY_METHOD_KEY")).depends(transSalesAllNot);
		
		//票房交易
		Clause transSales = newPlain().in("from").output(RPT2+".T_F_CON_MEMBER_TICKET transSales")
				.depends(newPlain().in("where").output("member.MEMBER_KEY=transSales.MEMBER_KEY"));
		
		Clause transSalesNot = newPlain().in("notexistsTransFrom").output(RPT2+".T_F_CON_MEMBER_TICKET transSalesNot")
		.depends(newPlain().in("notexistsTransWhere").output("member.MEMBER_KEY=transSalesNot.MEMBER_KEY"));
		
		Clause transSales_date = newPlain().in("where").output("transSales_date.DATE_KEY=transSales.SHOW_BIZ_DATE_KEY")
				.depends(newPlain().in("from").output(RPT2+".T_D_CON_DATE transSales_date")).depends(transSales);
		//未交易
		Clause transSales_dateNot = newPlain().in("notexistsTransWhere").output("transSales_dateNot.DATE_KEY=transSalesNot.SHOW_DATE_KEY")
		.depends(newPlain().in("notexistsTransFrom").output(RPT2+".T_D_CON_DATE transSales_dateNot")).depends(transSalesNot);
		
		Clause transSales_tickettype = newPlain().in("from").output(RPT2+".T_D_CON_TICKET_TYPE transSales_tickettype")
				.depends(newPlain().in("where").output("transSales_tickettype.TICKET_TYPE_KEY=transSales.TICKET_TYPE_KEY"))
				.depends(transSales);
		//未交易
		Clause transSales_tickettypeNot = newPlain().in("notexistsTransFrom").output(RPT2+".T_D_CON_TICKET_TYPE transSales_tickettypeNot")
		.depends(newPlain().in("notexistsTransWhere").output("transSales_tickettypeNot.TICKET_TYPE_KEY=transSalesNot.TICKET_TYPE_KEY"))
		.depends(transSalesNot);
		
		Clause transSales_hour = newPlain().in("from").output(RPT2+".T_D_CON_HOUR transSales_hour")
				.depends(newPlain().in("where").output("transSales_hour.HOUR_KEY=transSales.SHOW_HOUR_KEY"))
				.depends(transSales);
		//未交易
		Clause transSales_hourNot = newPlain().in("notexistsTransFrom").output(RPT2+".T_D_CON_HOUR transSales_hourNot")
				.depends(newPlain().in("notexistsTransWhere").output("transSales_hourNot.HOUR_KEY=transSalesNot.SHOW_HOUR_KEY"))
				.depends(transSalesNot);
		
		Clause transSales_cinema = newPlain().in("from").output(RPT2+".T_D_CON_CINEMA transSales_cinema")
				.depends(newPlain().in("where").output("transSales_cinema.CINEMA_KEY = transSales.CINEMA_KEY"))
				.depends(transSales);
		//未发生
		Clause transSales_cinemaNot = newPlain().in("notexistsTransFrom").output(RPT2+".T_D_CON_CINEMA transSales_cinemaNot")
				.depends(newPlain().in("notexistsTransWhere").output("transSales_cinemaNot.CINEMA_KEY = transSalesNot.CINEMA_KEY"))
				.depends(transSalesNot);
		
		Clause transSales_film = newPlain().in("from").output(RPT2+".T_D_CON_FILM transSales_film")
				.depends(newPlain().in("where").output("transSales_film.FILM_KEY = transSales.FILM_KEY"))
				.depends(transSales);
		
		//观影类型（首映场）
		Clause schedule_plan = newPlain().in("from").output(MBRODS+".T_SCHEDULE_PLAN_B schedule_plan")
				.depends(newPlain().in("where").output("schedule_plan.FILM_ID = transSales_film.SEQID"))
				.depends(transSales_film);
		
		//未发生
		Clause transSales_filmNot = newPlain().in("notexistsTransFrom").output(RPT2+".T_D_CON_FILM transSales_filmNot")
				.depends(newPlain().in("notexistsTransWhere").output("transSales_filmNot.FILM_KEY = transSalesNot.FILM_KEY"))
				.depends(transSalesNot);
		
		Clause transSales_paymethod = newPlain().in("from").output(RPT2+".T_D_CON_PAY_METHOD transSales_paymethod")
				.depends(newPlain().in("where").output("transSales_paymethod.PAY_METHOD_KEY = transSales.PAY_METHOD_KEY"))
				.depends(transSales);
		
		//未发生
		Clause transSales_paymethodNot = newPlain().in("notexistsTransFrom").output(RPT2+".T_D_CON_PAY_METHOD transSales_paymethodNot")
				.depends(newPlain().in("notexistsTransWhere").output("transSales_paymethodNot.PAY_METHOD_KEY = transSalesNot.PAY_METHOD_KEY"))
				.depends(transSalesNot);
		
		Clause transSales_channel = newPlain().in("from").output(RPT2+".T_D_CON_CHANNEL transSales_channel")
		.depends(newPlain().in("where").output("transSales_channel.CHANNEL_KEY = transSales.CHANNEL_KEY"))
		.depends(transSales);
		//未发生
		Clause transSales_channelNot = newPlain().in("notexistsTransFrom").output(RPT2+".T_D_CON_CHANNEL transSales_channelNot")
		.depends(newPlain().in("notexistsTransWhere").output("transSales_channelNot.CHANNEL_KEY = transSalesNot.CHANNEL_KEY"))
		.depends(transSalesNot);
		
		
		Clause transSalesAll_channel = newPlain().in("from").output(RPT2+".T_D_CON_CHANNEL transSalesAll_channel")
		.depends(newPlain().in("where").output("transSalesAll_channel.CHANNEL_KEY = transSalesAll.CHANNEL_KEY"))
		.depends(transSalesAll);
		
		Clause mbrTagResult = newPlain().in("from").output(MBRODS+".T_MBR_TAG_RESULT mbr_tag_result")
				.depends(newPlain().in("where").output("mbr_tag_result.MEMBER_ID = member.MEMBER_KEY"));
		
		Clause acx_behavior_segment = newPlain().in("from").output(MBRODS+".ACX_BEHAVIOR_SEGMENT acx_behavior_segment")
				.depends(newPlain().in("where").output("acx_behavior_segment.MEMBER_KEY = member.MEMBER_KEY"));
		
		//Clause points = newPlain().in("from").output(RPT2+".T_D_CON_FILM transSales_film")
		//		.depends(newPlain().in("where").output("transSales_film.FILM_KEY = transSales.FILM_KEY"));
		
		//积分连会员库
		//Clause member_member = newPlain().in("from").output(MBRODS+".T_MEMBER member_member")
		//.depends(newPlain().in("where").output("member_member.MEMBER_NO=member.MEMBER_NO"));
		
		//积分连会员库积分历史; 开发测试使用 关联会员维表中的member_key
		//Clause member_point = newPlain().in("from").output(MBRODS+".T_MEMBER_POINT member_point")
		//		.depends(newPlain().in("where").output("member_point.member_id=member.member_id"));

		//Clause point_history = newPlain().in("from").output(MBRODS+".T_POINT_HISTORY point_history")
		//		.depends(newPlain().in("where").output("point_history.member_id=member.member_id"));
		
		//生产环境使用 生产环境中 会员维表没有member_id此字段
		Clause member_point = newPlain().in("from").output(MBRODS+".T_MEMBER_POINT member_point")
				.depends(newPlain().in("where").output("member_point.member_id=member.member_key"));
		
		Clause point_history = newPlain().in("from").output(MBRODS+".T_POINT_HISTORY point_history")
				.depends(newPlain().in("where").output("point_history.member_id=member.member_key"));
		
		//会员级别
		Clause member_level = newPlain().in("from").output(MBRODS+".T_MEMBER_LEVEL member_level")
				.depends(newPlain().in("where").output("member_level.member_id=member.member_key"));
		
		//会员级别历史记录
		Clause member_level_history = newPlain().in("from").output(MBRODS+".T_LEVEL_HISTORY member_level_history")
				.depends(newPlain().in("where").output("member_level_history.level_history_id=member_level.member_level_history_id")
				.depends(member_level));
		
		// ==================== 活动标签 ======================
		// 波次目标
		Clause cmn_target = newPlain().in("from").output(MBRODS+".T_ACT_TARGET target")
				.depends(newPlain().in("where").output("target.ACT_TARGET_ID=history.ACT_TARGET_ID"));
		// 活动联络历史
		Clause contact_history = newPlain().in("from").output(MBRODS+".T_CONTACT_HISTORY history")
				.depends(newPlain().in("where").output("history.MEMBER_ID=member.MEMBER_KEY"));
		
		// 波次活动
		Clause cmn_activity = newPlain().in("from").output(MBRODS+".T_CMN_ACTIVITY activity")
				.depends(newPlain().in("where").output("target.ACT_TARGET_ID=activity.ACT_TARGET_ID"))
				.depends(cmn_target)
				.depends(contact_history);
		// ==================== 活动标签 ======================
		
		
		// ==================== 会员统计属性 ======================
		
		// 会员属性统计表
		Clause memeberTag = newPlain().in("from").output(MBRODS+".T_MEMBER_TAG tag")
				.depends(newPlain().in("where").output("tag.MEMBER_ID=member.MEMBER_KEY"));
		
		// ==================== 会员统计属性 ======================
		
		// ==================== 假日信息表 ======================
		
		// 假日信息表
		Clause holidayDate = newPlain().in("from").output(RPT2+".T_D_CON_DATE holiday")
				.depends(newPlain().in("where").output("holiday.DATE_KEY=transSalesAll.DATE_KEY")
				.depends(transSalesAll));
		
		// ==================== 假日信息表 ======================
		
		Clause group_by = newPlain().in("groupby").output("member.MEMBER_KEY");
		
		
		this.parser = newParser(QUERY_PARAGRAPHS_SEGMENT)
		
		.add(newPlain().in("where").output("member.STATUS='1' and member.ISDELETE = 0"))
		.add(newPlain().in("from").output(RPT2+".T_D_CON_MEMBER member"))
		
		////////////卖品交易条件////////////
		//卖品交易日期
		.add(notEmpty("conSaleDate"), newExpression().in("where").output("consale.BOOK_DATE_KEY", DataType.DATE)
				.depends(consale))
		//卖品交易日期未发生
		.add(notEmpty("conSaleDateNot"), newExpression().in("notexistsConWhere").output("consaleNot.BOOK_DATE_KEY", DataType.DATE)
				.depends(consaleNot))
				
		//节假日卖品交易
//		.add(notEmpty("conSaleHoliday"), newValue().in("where").output("consale_date.HOLIDAY_ID  {0}", DataType.SQL, true).addValueMapper(0, booleanMapper)
//				.depends(consale_date))
			
		//节日卖品交易
		.add(notEmpty("holidayConSale"), newValue().in("where").output("( consale_date.HOLIDAY_ID in ({*}) or consale_date.HOLIDAY_W_ID in ({*}) )", DataType.STRING, false)//.addValueMapper(0, holidayMapper)
				.depends(consale_date))
				
		//节假日卖品交易未发生
//		.add(notEmpty("conSaleHolidayNot"), newValue().in("notexistsConWhere").output("consale_dateNot.HOLIDAY_ID  {0} ", DataType.SQL, true).addValueMapper(0, booleanMapper)
//				.depends(consale_dateNot))
				
		//节日卖品交易-未发生
		.add(notEmpty("holidayConSaleNot"), newValue().in("notexistsConWhere").output("(consale_dateNot.HOLIDAY_ID in ({*}) or consale_dateNot.HOLIDAY_W_ID in ({*}))", DataType.STRING, false)//.addValueMapper(0, holidayMapper)
				.depends(consale_dateNot))
				
		//卖品交易时段
		.add(notEmpty("conSaleHourPeriod"), newExpression().in("where").output("consale_hour.TIME_DIVIDING_ID", DataType.STRING)
				.depends(consale_hour))
		//卖品交易时段未发生
		.add(notEmpty("conSaleHourPeriodNot"), newExpression().in("notexistsConWhere").output("consale_hourNot.TIME_DIVIDING_ID", DataType.STRING)
				.depends(consale_hourNot))
		//卖品交易小时
		.add(notEmpty("conSaleHour"), newExpression().in("where").output("consale.BOOK_HOUR_KEY", DataType.STRING)
				.depends(consale))
		//卖品交易小时未发生
		.add(notEmpty("conSaleHourNot"), newExpression().in("notexistsConWhere").output("consaleNot.BOOK_HOUR_KEY", DataType.STRING)
				.depends(consaleNot))
		//卖品交易影城
		.add(notEmpty("conSaleCinema"), newExpression().in("where").output("consale_cinema.INNER_CODE", DataType.STRING, cinemaCom)
				.depends(consale_cinema))
		//卖品交易影城未发生		
		.add(notEmpty("conSaleCinemaNot"), newExpression().in("notexistsConWhere").output("consale_cinemaNot.INNER_CODE", DataType.STRING, cinemaCom)
				.depends(consale_cinemaNot))
		//卖品消费金额
		.add(notEmpty("conSaleAmount"), newExpression().in("having").output("sum(consale.BK_SALE_AMOUNT) - sum(consale.RE_SALE_AMOUNT)", DataType.DOUBLE)
				.depends(consale).depends(group_by))
		//卖品消费次数
		.add(notEmpty("conSaleConsumeTime"), newExpression().in("having").output("count(distinct consale.Bk_CS_ORDER_CODE)", DataType.INTEGER)
				.depends(consale).depends(group_by))
		//卖品品类
		.add(notEmpty("conSaleCategory"), newExpression().in("where").output("consale_cate.THIRD_CLASS_ID", DataType.LONG)
				.depends(consale_cate))
				
		//卖品品类未发生
		.add(notEmpty("conSaleCategoryNot"), newExpression().in("notexistsConWhere").output("consale_cateNot.THIRD_CLASS_ID", DataType.LONG)
				.depends(consale_cateNot))
		//卖品品项
		.add(notEmpty("conSaleItem"), newExpression().in("where").output("consale_item.item_code", DataType.STRING, conitemCom)
				.depends(consale_itemNot))
		//卖品品项未发生
		.add(notEmpty("conSaleItemNot"), newExpression().in("notexistsConWhere").output("consale_itemNot.item_code", DataType.STRING, conitemCom)
				.depends(consale_item))
		//卖品交易星期
		.add(notEmpty("conSaleWeeks"), newExpression().in("where").output("consale_date.FEW_WEEK_ID", DataType.STRING, conitemCom)
				.depends(consale_date))
		//卖品交易星期未发生		
		.add(notEmpty("conSaleWeeksNot"), newExpression().in("notexistsConWhere").output("consale_dateNot.FEW_WEEK_ID", DataType.STRING, conitemCom)
				.depends(consale_dateNot))
		//卖品支付方式
		.add(notEmpty("conSalePayMethod"), newExpression().in("where").output("consale_paymethod.PAY_METHOD_CODE", DataType.STRING)
				.depends(consale_paymethod))
		//卖品支付方式未发生
		.add(notEmpty("conSalePayMethodNot"), newExpression().in("notexistsConWhere").output("consale_paymethodNot.PAY_METHOD_CODE", DataType.STRING)
				.depends(consale_paymethodNot))

		////////////添加会员基本条件////////////
		//招募渠道
		.add(notEmpty("registerChannel"), newExpression().in("where").output("member.RECRUITMENT_CHANNEL_CODE", DataType.STRING))
		//注册影城
		.add(notEmpty("registerCinema"), newExpression().in("where").output("member.RECRUIT_CINEMA", DataType.STRING, cinemaCom))
		//性别
		.add(notEmpty("gender"), newExpression().in("where").output("member.SEX", DataType.STRING))
		//级别
		.add(notEmpty("nowMemberLevel"), newExpression().in("where").output("member.MEMBER_LEVEL_CODE", DataType.STRING))
		//入会时间
		.add(notEmpty("registerDate"), newExpression().in("where").output("member.RECRUIT_TIME", DataType.DATE_TIME))
		//是否已婚
		.add(notEmpty("marry"), newExpression().in("where").output("member.MARR_STATUS_CODE", DataType.STRING))
		//教育
		.add(notEmpty("education"), newExpression().in("where").output("member.EDUCATION", DataType.STRING))
		//工作
		.add(notEmpty("memberJob"), newExpression().in("where").output("member.OCCUPATION", DataType.INTEGER))
		//收入
		.add(notEmpty("familyIncome"), newExpression().in("where").output("member.INCOME", DataType.INTEGER))
		//孩子个数
		.add(notEmpty("childrenNum"), newExpression().in("where").output("member.CHILDERN_NUMBER", DataType.STRING))
		//手机报
		.add(notEmpty("mobileNews"), newExpression().in("where").output("member.NEWSPAPER_FLAG", DataType.STRING))
		//会员注册方式
		.add(notEmpty("recruitType"), newExpression().in("where").output("member.RECRUIT_TYPE", DataType.STRING))
		//会员细分
		.add(notEmpty("memberDNA"), newExpression().in("where").output("acx_behavior_segment.BEHAVIOR_SEGMENT_VALUE", DataType.STRING).depends(acx_behavior_segment))
		
		//是否希望联络
		.add(notEmpty("noDisturb"), newExpression().in("where").output("member.ISCONTACTABLE", DataType.STRING))
		//管理影城
		.add(notEmpty("manageCinema"), newExpression().in("where").output("member_cinema.INNER_CODE", DataType.STRING, cinemaCom)
				.depends(member_cinema))
		//生日
		.add(notEmpty("birthday"), newExpression().in("where").output("member.BIRTHDAY", DataType.DATE))
		
		//最近一次观影时间
		.add(notEmpty("lastTicketShowTime"), newExpression().in("where").output("mbr_tag_result.LAST_TICKET_SHOW_TIME", DataType.DATE_TIME)
				.depends(mbrTagResult))
		
		//最近一次卖品交易时间
		.add(notEmpty("lastConRransTime"), newExpression().in("where").output("mbr_tag_result.LAST_CON_TRANS_TIME", DataType.DATE_TIME)
				.depends(mbrTagResult))
		
		////////////会员积分条件////////////
		//最近一次积分累计时间
//		.add(notEmpty("lastPointBalance", 
//				new ValueClause("where").output("member_point.SET_TIME", DataType.DATE_TIME, null, true).depends(member_point))
		//最近一次积分累计时间
		.add(notEmpty("lastPointBalance"),
				newExpression().in("where").output("member_point.UPDATE_DATE", DataType.DATE_TIME)
				.depends(member_point))
		//最近一次兑换时间
		.add(notEmpty("lastChangeTime"), newExpression().in("where").output("member_point.LAST_EXCHANGE_DATE", DataType.DATE_TIME)
				.depends(member_point))
		//可兑换积分余额
		.add(notEmpty("exchangePoint"), newExpression().in("where").output("member_point.EXG_POINT_BALANCE", DataType.LONG)
				.depends(member_point))
		//累计积分日期
		.add(notEmpty("getPointTime"), newExpression().in("where").output("point_history.SET_TIME", DataType.DATE).depends(point_history))
		//累计可兑换积分总数
		.add(notEmpty("exchangePointCount"), newExpression().in("having").output("sum(point_history.EXCHANGE_POINT)", DataType.INTEGER)
				.depends(point_history).depends(group_by))
		//累计定级积分总数
		.add(notEmpty("levelPointCount"), newExpression().in("having").output("sum(point_history.LEVEL_POINT)", DataType.INTEGER)
				.depends(point_history).depends(group_by))
		//累计非定级积分总数
		.add(notEmpty("activityPointCount"), newExpression().in("having").output("sum(point_history.ACTIVITY_POINT)", DataType.INTEGER)
				.depends(point_history).depends(group_by))
	
		////////////总交易条件////////////
		//交易日期
		.add(notEmpty("transDay"), newExpression().in("where").output("transSalesAll.DATE_KEY", DataType.DATE)
				.depends(transSalesAll))

		//交易节日
		.add(notEmpty("transHoliday"), newValue().in("where").output("(holiday.HOLIDAY_ID in ({*})  or holiday.HOLIDAY_W_ID in ({*}))", DataType.STRING, false)//.addValueMapper(0, holidayMapper)
		 		.depends(holidayDate))
				
		//交易日期未交易
		.add(notEmpty("transDayNot"), newExpression().in("notexistsTransAllWhere").output("transSalesAllNot.DATE_KEY", DataType.DATE).depends(transSalesAllNot))
		//节假日交易
		.add(notEmpty("holidayTrans"), newValue().in("where").output("transSalesAll_date.HOLIDAY_ID  {0}", DataType.SQL, true).addValueMapper(0, booleanMapper)
		 		.depends(transSalesAll_date))
		 //节假日交易未交易
//		.add(notEmpty("holidayTransNot"), newValue().in("notexistsTransAllWhere").output("transSalesAll_dateNot.HOLIDAY_ID  {0}", DataType.SQL, true).addValueMapper(0, booleanMapper)
//		 		.depends(transSalesAll_dateNot))
		 	
		 //节假日交易-未交易
		 .add(notEmpty("transHolidayNot"), newValue().in("notexistsTransAllWhere").output("( transSalesAll_dateNot.HOLIDAY_ID in ({*}) or transSalesAll_dateNot.HOLIDAY_W_ID in ({*}) )", DataType.STRING, false)//.addValueMapper(0, holidayMapper)
		 		.depends(transSalesAll_dateNot))
		 
		 //总交易的交易星期
		 .add(notEmpty("transWeeks"), newExpression().in("where").output("transSalesAll_date.FEW_WEEK_ID", DataType.STRING)
		 		.depends(transSalesAll_date))
		 		
		  //总交易的交易星期未交易
		 .add(notEmpty("transWeeksNot"), newExpression().in("notexistsTransAllWhere").output("transSalesAll_dateNot.FEW_WEEK_ID", DataType.STRING)
		 		.depends(transSalesAll_dateNot))
		//交易时段
		.add(notEmpty("transTime"), newExpression().in("where").output("transSalesAll_hour.TIME_DIVIDING_ID", DataType.STRING)
				.depends(transSalesAll_hour))
		//交易时段未交易
		.add(notEmpty("transTimeNot"), newExpression().in("notexistsTransAllWhere").output("transSalesAll_hourNot.TIME_DIVIDING_ID", DataType.STRING)
				.depends(transSalesAll_hourNot))
		//交易小时
		.add(notEmpty("transHour"), newExpression().in("where").output("transSalesAll.HOUR_KEY", DataType.STRING)
				.depends(transSalesAll))
				
		//交易小时未交易
		.add(notEmpty("transHourNot"), newExpression().in("notexistsTransAllWhere").output("transSalesAllNot.HOUR_KEY", DataType.STRING).depends(transSalesAllNot))
		//曾经持卡消费
		.add(notEmpty("transCards"), newValue().in("where").output("transSalesAll.CARD_KEY  {0}", DataType.SQL, true).addValueMapper(0, booleanTransAllMapper)
						.depends(transSalesAll))
		//电子渠道交易
		.add(notEmpty("transElectronic"), newValue().in("where").output("transSalesAll_channel.CHANNEL_CODE  {0}", DataType.SQL, true).addValueMapper(0, booleanChannelMapper)
						.depends(transSalesAll_channel))
		//交易影城
		.add(notEmpty("transCinema"), newExpression().in("where").output("transSalesAll_cinema.INNER_CODE", DataType.STRING, cinemaCom)
				.depends(transSalesAll_cinema))
				
		//交易影城未交易
		.add(notEmpty("transCinemaNot"), newExpression().in("notexistsTransAllWhere").output("transSalesAll_cinemaNot.INNER_CODE", DataType.STRING, cinemaCom)
				.depends(transSalesAll_cinemaNot))
		//观影支付方式
		.add(notEmpty("transPayMethod"), newExpression().in("where").output("transSalesAll_paymethod.PAY_METHOD_CODE", DataType.STRING)
				.depends(transSalesAll_paymethod))
				
		//观影支付方式未交易
		.add(notEmpty("transPayMethodNot"), newExpression().in("notexistsTransAllWhere").output("transSalesAll_paymethodNot.PAY_METHOD_CODE", DataType.STRING)
				.depends(transSalesAll_paymethodNot))
		//总交易消费金额
		.add(notEmpty("transMoney"), newExpression().in("having").output("sum(transSalesAll.BK_TOTAL_INCOME)", DataType.DOUBLE)
				.depends(transSalesAll).depends(group_by))
		//transNumber
		.add(notEmpty("transNumber"), newExpression().in("having").output("count(distinct transSalesAll.Bk_ORDER_CODE)", DataType.INTEGER)
				.depends(transSalesAll).depends(group_by))
				
				
		//票房交易
		//观影日期
		.add(notEmpty("watchTradeDay"), newExpression().in("where").output("transSales.SHOW_BIZ_DATE_KEY", DataType.DATE)
				.depends(transSales))
		//观影日期未交易
		.add(notEmpty("watchTradeDayNot"), newExpression().in("notexistsTransWhere").output("transSalesNot.SHOW_BIZ_DATE_KEY", DataType.DATE)
				.depends(transSalesNot))
		//是否节假日观影
//		 .add(notEmpty("holidayWatchTrade"), newValue().in("where").output("transSales_date.HOLIDAY_ID {0}", DataType.SQL, true).addValueMapper(0, booleanMapper)
//			 .depends(transSales_date))
		
		// 节假日观影
		.add(notEmpty("watchTradeHoliday"), newValue().in("where").output("( transSales_date.HOLIDAY_ID in ({*}) or transSales_date.HOLIDAY_W_ID in ({*}) )", DataType.STRING, false)//.addValueMapper(0, holidayMapper)
			 .depends(transSales_date))
				
		//是否节假日观影未交易
//		 .add(notEmpty("holidayWatchTradeNot"), newValue().in("notexistsTransWhere").output("transSales_dateNot.HOLIDAY_ID {0}", DataType.SQL, true).addValueMapper(0, booleanMapper)
//			 .depends(transSales_dateNot))
			 
		//节假日观影-未交易
		.add(notEmpty("watchTradeNotHoliday"), newValue().in("notexistsTransWhere").output("( transSales_dateNot.HOLIDAY_ID in ({*}) or transSales_dateNot.HOLIDAY_W_ID in ({*}) )", DataType.STRING, false)//.addValueMapper(0, holidayMapper)
			 .depends(transSales_dateNot))
			 
		//票房交易的交易星期
		 .add(notEmpty("watchTradeWeeks"), newExpression().in("where").output("transSales_date.FEW_WEEK_ID", DataType.STRING)
		 		.depends(transSales_date))
		 	
		 //票房交易的交易星期未发生
		 .add(notEmpty("watchTradeWeeksNot"), newExpression().in("notexistsTransWhere").output("transSales_dateNot.FEW_WEEK_ID", DataType.STRING)
		 		.depends(transSales_dateNot))
		 //观影交易时段
		.add(notEmpty("watchTradeTime"), newExpression().in("where").output("transSales_hour.TIME_DIVIDING_ID", DataType.STRING)
				.depends(transSales_hour))
		//观影交易时段未发生
		.add(notEmpty("watchTradeTimeNot"), newExpression().in("notexistsTransWhere").output("transSales_hourNot.TIME_DIVIDING_ID", DataType.STRING)
				.depends(transSales_hourNot))
		//观影交易小时
		.add(notEmpty("watchTradeHour"), newExpression().in("where").output("transSales.SHOW_HOUR_KEY", DataType.STRING)
				.depends(transSales))
				
		//观影交易小时未发生
		.add(notEmpty("watchTradeHourNot"), newExpression().in("notexistsTransWhere").output("transSalesNot.SHOW_HOUR_KEY", DataType.STRING)
				.depends(transSalesNot))
		//观影支付方式
		.add(notEmpty("watchPayMethod"), newExpression().in("where").output("transSales_paymethod.PAY_METHOD_CODE", DataType.STRING)
				.depends(transSales_paymethod))
		//观影支付方式未发生
		.add(notEmpty("watchPayMethodNot"), newExpression().in("notexistsTransWhere").output("transSales_paymethodNot.PAY_METHOD_CODE", DataType.STRING)
				.depends(transSales_paymethodNot))
		//观影交易影城
		.add(notEmpty("watchTradeCinema"), newExpression().in("where").output("transSales_cinema.INNER_CODE", DataType.STRING, cinemaCom)
				.depends(transSales_cinema))
		//观影交易影城未发生
		.add(notEmpty("watchTradeCinemaNot"), newExpression().in("notexistsTransWhere").output("transSales_cinemaNot.INNER_CODE", DataType.STRING, cinemaCom)
				.depends(transSales_cinemaNot))
		//观影消费金额
		.add(notEmpty("purchaseMoney"), newExpression().in("having").output("sum(transSales.BK_ADMISSIONS)-sum(transSales.RE_ADMISSIONS)", DataType.INTEGER)
				.depends(transSales).depends(group_by))
		//观影消费次数
		.add(notEmpty("purchaseNumber"), newExpression().in("having").output("count(distinct transSales.BK_CT_ORDER_CODE)", DataType.INTEGER)
				.depends(transSales).depends(group_by))
		//观影购票数
		.add(notEmpty("ticketNumber"), newExpression().in("having").output("count(distinct transSales.BK_TICKET_NUMBER)-count(distinct transSales.RE_TICKET_NUMBER)", DataType.INTEGER)
				.depends(transSales).depends(group_by))
		//观影影片
		.add(notEmpty("transFilm"), newExpression().in("where").output("transSales_film.FILM_CODE", DataType.STRING, filmCom)
				.depends(transSales_film))
				
		//首映场观影
		.add(notEmpty("filmPlanType"), newValue().in("where").output("schedule_plan.TYPE {0}", DataType.SQL, true).addValueMapper(0, filePlanTypeMapper)
				.depends(schedule_plan))
				
		//观影影片未发生
		.add(notEmpty("transFilmNot"), newExpression().in("notexistsTransWhere").output("transSales_filmNot.FILM_CODE", DataType.STRING, filmCom)
				.depends(transSales_filmNot))
		//观影票类
		.add(notEmpty("ticketType"), newExpression().in("where").output("transSales_tickettype.TYPE_GROUP_ID", DataType.STRING)
				.depends(transSales_tickettype))
		//观影票类未交易
		.add(notEmpty("ticketTypeNot"), newExpression().in("notexistsTransWhere").output("transSales_tickettypeNot.TYPE_GROUP_ID", DataType.STRING)
				.depends(transSales_tickettypeNot))
		//观影交易渠道		
		.add(notEmpty("watchTradeChannel"), newExpression().in("where").output("transSales_channel.CHANNEL_CODE", DataType.STRING)
				.depends(transSales_channel))
				
		//观影交易渠道未交易
		.add(notEmpty("watchTradeChannelNot"), newExpression().in("notexistsTransWhere").output("transSales_channelNot.CHANNEL_CODE", DataType.STRING)
				.depends(transSales_channelNot))
		
		// ================= 会员级别 ================= 
		//将升级目标级别
		.add(notEmpty("upTargetLevel"),newExpression().in("where").output("member_level.TARGET_LEVEL", DataType.STRING).depends(member_level))
		//距离目标级别积分差
		.add(notEmpty("targetLevelGap"),newExpression().in("where").output("member_level.LEVEL_POINT_OFFSET", DataType.INTEGER).depends(member_level))
		//距离目标级别票数差
		.add(notEmpty("levelTickCount"),newExpression().in("where").output("member_level.TICKET_OFFSET", DataType.INTEGER).depends(member_level))
		// 当前级别变更时间
		.add(notEmpty("nowChangeLevelTime"),newExpression().in("where").output("member_level_history.SET_TIME", DataType.DATE_TIME).depends(member_level_history))
		// 当前级别变更类型
		//.add(notEmpty("changeLevelType"),newValue().in("where").output("member_level_history.CHG_TYPE {*}", DataType.SQL, true).addValueMapper(0, levelChangeMapper).depends(member_level_history))
		.add(notEmpty("changeLevelType"),newExpression().in("where").output("member_level_history.CHG_TYPE", DataType.STRING).depends(member_level_history))
		// 变更级别
		.add(notEmpty("memberLevelChange"),newExpression().in("where").output("member_level_history.MEM_LEVEL", DataType.STRING).depends(member_level_history))
		
		// ================= 会员级别 ================= 
		
		// ================== 活动 ==================
		
		// 波次活动
		.add(notEmpty("cmnActivity"),newExpression().in("where").output("activity.CMN_ACTIVITY_ID", DataType.STRING, activityCom).depends(cmn_activity))
		// 活动响应		对应维表 1为推荐响应  2为关联响应
		.add(notEmpty("cmnResponse"), newValue().in("where").output("({*|or})", DataType.SQL, true).addValueMapper(0, responseMapper).depends(contact_history))
		// 响应次数
		.add(notEmpty("responseCount"), newExpression().in("having").output("count(member.MEMBER_KEY)", DataType.INTEGER)
				.depends(contact_history).depends(group_by))
		
		// ================== 活动 ==================
		
		
		// ================== 会员统计属性 ==================
		
		// 价格敏感
		.add(notEmpty("priceConscious"),newExpression().in("where").output("tag.PRICE_CONSCIOUS", DataType.INTEGER).depends(memeberTag))
		// 家庭构成
		.add(notEmpty("familyNumber"),newValue().in("where").output("( tag.FAMILY_NUMBER1 in ({*}) or tag.FAMILY_NUMBER2 in ({*}) )", DataType.INTEGER, false).depends(memeberTag))
		// 异常消费
		.add(notEmpty("tradeAbnormal"),newExpression().in("where").output("tag.TRADE_ABNORMAL", DataType.STRING).depends(memeberTag))
		// 会员活跃度
		.add(notEmpty("activityLevel"),newExpression().in("where").output("tag.ACTIVITY_LEVEL", DataType.STRING).depends(memeberTag))
		// 电子渠道偏好
		.add(notEmpty("onlineTradeType"),newExpression().in("where").output("tag.ONLINE_TRADE_TYPE", DataType.STRING).depends(memeberTag))
		// 影片偏好
		.add(notEmpty("favFilmType"),newValue().in("where").output("( tag.FAV_FILM_TYPE1 in ({*}) or tag.FAV_FILM_TYPE2 in ({*}) or tag.FAV_FILM_TYPE3 in ({*}) )", DataType.SQL, true).depends(memeberTag));
		
		// ================== 会员统计属性 ==================
		
		
		
		//TODO 这块儿需要整理
		this.orderByMap = new HashMap<String, String>();
		orderByMap.put("40", "member.MEMBER_LEVEL_CODE"); 			//会员级别
		orderByMap.put("10", "member_point.EXG_POINT_BALANCE");		//可用积分余额
		
		/* Order By 部分需要重新整理
		criteriaClauseMap.put("orderby", points);
		parser.add(valueEquals("orderby", "10"), newPlain("transSalesAll.BK_ORDER_CODE").depends(transSalesAll));
		parser.add(valueEquals("orderby", "20"), newPlain("transSalesAll.BK_TOTAL_INCOME").depends(transSalesAll));
		parser.add(valueEquals("orderby", "40"), newPlain("member.MEMBER_LEVEL_CODE"));
		
		this.orderByMap = new HashMap<String, String>();
		orderByMap.put("10", "transSalesAll.BK_ORDER_CODE"); //消费次数
		orderByMap.put("20", "transSalesAll.BK_TOTAL_INCOME"); //总消费金额
		orderByMap.put("30", ""); //可兑换积分余额
		orderByMap.put("40", "member.MEMBER_LEVEL_CODE"); //会员级别
		orderByMap.put("50", ""); //活跃度
		*/
	}
	
	
	public CriteriaParser getParser() {
		return this.parser;
	}
	
	public Map<String, String> getOrderByMap() {
		return this.orderByMap;
	}



}
