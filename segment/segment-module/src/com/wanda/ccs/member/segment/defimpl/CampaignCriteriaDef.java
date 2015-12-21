package com.wanda.ccs.member.segment.defimpl;

//import static com.wanda.ccs.member.segment.SegmentConstants.Schemas.RPT2;
import static com.wanda.ccs.member.segment.SegmentConstants.Schemas.CCSDW;
import static com.wanda.ccs.member.segment.SegmentConstants.Schemas.MBRODS;
import static com.wanda.ccs.sqlasm.CriteriaParserBuilder.QUERY_PARAGRAPHS;
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

public class CampaignCriteriaDef {
	
	public final static String GROUP_ID_TICKET = "tsale"; //票房交易组ID
	
	public final static String GROUP_ID_CON_ITEM = "conSale"; //卖品交易组ID
	
	public static CriteriaParser TICKET_CRITERIA_PARSER = getTicketSaleParser();	//票类解析器
	
	public static CriteriaParser CONSALE_CRITERIA_PARSER = getConSaleParser(); //卖品解析器
	
	/**
	 * 得到卖品的交易信息解析器
	 * @return
	 * 
	 */
	private static CriteriaParser getTicketSaleParser() {
		CriteriaParser ticketParser = null;
			
		//公共值映射定义
		Map<Object, Object> booleanMapper = new HashMap<Object, Object>();
		booleanMapper.put("0", "is null");
		booleanMapper.put("1", "is not null");	
		
		//公共条件
		Clause whereDef = newPlain().in("where").output("transSales.SHOW_BIZ_DATE=to_date('#ymd','yyyy-mm-dd') and campaign_segment.CAMPAIGN_ID=#cid and member.STATUS='1' and member.ISDELETE = 0");
		
		//票房交易
		Clause memberTicketTable = newPlain().in("from").output(/**RPT2*/CCSDW+".V_DW_F_MEMBER_TICKET transSales")
				.depends(newPlain().in("where").output("member.MEMBER_KEY=transSales.MEMBER_ID"));
		
		Clause ticketDateTable = newPlain().in("where").output("transSales_date.DATE_KEY=transSales.SHOW_BIZ_DATE")
				.depends(newPlain().in("from").output(/**RPT2*/CCSDW+".T_DW_D_DATE transSales_date")).depends(memberTicketTable);
		
		Clause ticketHourTable = newPlain().in("from").output(/**RPT2*/CCSDW+".T_DW_D_HOUR transSales_hour")
				.depends(newPlain().in("where").output("transSales_hour.HOUR_KEY=transSales.HOUR_KEY"))
				.depends(memberTicketTable);
		
		Clause ticketCinemaTable = newPlain().in("from").output(/**RPT2*/CCSDW+".T_DW_D_CINEMA transSales_cinema")
				.depends(newPlain().in("where").output("transSales_cinema.CINEMA_KEY = transSales.CINEMA_KEY"))
				.depends(memberTicketTable);
		
		Clause ticketFilmTable = newPlain().in("from").output(/**RPT2*/CCSDW+".T_DW_D_FILM transSales_film")
				.depends(newPlain().in("where").output("transSales_film.FILM_KEY = transSales.FILM_KEY"))
				.depends(memberTicketTable);
		
		Clause campaignSegmentTable = newPlain().in("from").output(MBRODS+".T_CAMPAIGN_SEGMENT campaign_segment")
				.depends(newPlain().in("where").output("transSales.MEMBER_ID = campaign_segment.MEMBER_ID"))
				.depends(memberTicketTable);
		
		Clause groupBy = newPlain().in("groupby").output("member.MEMBER_KEY,campaign_segment.IS_CONTROL,transSales.PAY_METHOD_NAME,nvl(transSales.BK_ORDER_ID,transSales.RE_ORDER_ID) ,nvl(transSales.BK_TICKET_NUM,transSales.re_TICKET_NUM)");
		
		ticketParser = newParser(QUERY_PARAGRAPHS)
			.add(newPlain().in("select").output("member.MEMBER_KEY,campaign_segment.IS_CONTROL,transSales.PAY_METHOD_NAME,nvl(transSales.BK_ORDER_ID,transSales.RE_ORDER_ID) ORDER_CODE,nvl(transSales.BK_TICKET_NUM,transSales.re_TICKET_NUM) ITEM_CODE,sum(transSales.Bk_PAYMENT_MOUNT-transSales.Re_PAYMENT_MOUNT) INCOME"))
			.add(newPlain().in("from").output(/**RPT2*/CCSDW+".V_DW_F_MEMBER member")
					.depends(memberTicketTable)
					.depends(ticketCinemaTable)
					.depends(campaignSegmentTable))
			.add(whereDef)
			.add(groupBy)
			
			//节假日观影
			.add(notEmpty("holidayWatchTrade"), newValue().in("where").output("transSales_date.HOLIDAY_ID {0}", DataType.SQL, true).addValueMapper(0, booleanMapper)
					.depends(ticketDateTable))
			//观影交易时段
			.add(notEmpty("watchTradeTime"), newExpression().in("where").output("transSales_hour.TIME_DIVIDING_ID", DataType.STRING)
					.depends(ticketHourTable))
			//观影交易小时
			.add(notEmpty("watchTradeHour"), newExpression().in("where").output("transSales.HOUR_KEY", DataType.STRING)
					.depends(memberTicketTable))
			//观影交易影城
			.add(notEmpty("watchTradeCinema"), newExpression().in("where").output("transSales_cinema.INNER_CODE", DataType.STRING, new CinemaCompositeParser())
					.depends(ticketCinemaTable))
			//观影影片
			.add(notEmpty("transFilm"), newExpression().in("where").output("transSales_film.FILM_CODE", DataType.STRING, new FilmCompositeParser())
					.depends(ticketFilmTable))
			//生日当天观影
			.add(notEmpty("birthdayFilm"), newPlain().in("where").output("to_char(member.BIRTHDAY,'mm/dd')=to_char(transSales.SHOW_BIZ_DATE,'mm/dd')")
					.depends(memberTicketTable));
		
		return ticketParser;
	}
	
	
	/**
	 * 得到卖品的交易信息解析器
	 * @return
	 */
	private static CriteriaParser getConSaleParser() {
		CriteriaParser conSaleParser = null;
			
		//公共值映射定义
		Map<Object, Object> booleanMapper = new HashMap<Object, Object>();
		booleanMapper.put("0", "is null");
		booleanMapper.put("1", "is not null");	
		
		//公共条件
		Clause whereDef = newPlain().in("where").output("consale.BIZ_DATE=to_date('#ymd','yyyy-mm-dd') and campaign_segment.CAMPAIGN_ID=#cid and consale.GOODS_COUNT > 0 and member.STATUS='1' and member.ISDELETE = 0");
		
		//卖品交易
		Clause memberSaleTable = newPlain().in("from").output(/**RPT2*/CCSDW+".V_DW_F_MEMBER_SALE consale")
				.depends(newPlain().in("where").output("member.MEMBER_KEY=consale.MEMBER_ID"));
		
		Clause saleDateTable = newPlain().in("where").output("consale_date.DATE_KEY=consale.BIZ_DATE")
				.depends(newPlain().in("from").output(/**RPT2*/CCSDW+".T_DW_D_DATE consale_date"))
				.depends(memberSaleTable);

		Clause saleHourTable =newPlain().in("from").output(/**RPT2*/CCSDW+".T_DW_D_HOUR consale_hour")
				.depends(newPlain().in("where").output("consale_hour.HOUR_KEY=consale.HOUR_KEY"))
				.depends(memberSaleTable);
		
		Clause saleCinemaTable = newPlain().in("from").output(/**RPT2*/CCSDW+".T_DW_D_CINEMA consale_cinema")
				.depends(newPlain().in("where").output("consale_cinema.CINEMA_KEY = consale.CINEMA_KEY"))
				.depends(memberSaleTable);
		
		Clause saleClassTable = newPlain().in("from").output(/**RPT2*/CCSDW+".T_DW_D_SALE_CLASS consale_cate")
				.depends(newPlain().in("where").output("consale.ITEM_CLASS_CODE = consale_cate.ITEM_CLASS_ID"))
				.depends(memberSaleTable);
		
		Clause saleItemTable = newPlain().in("from").output(/**RPT2*/CCSDW+".V_DW_D_SALE_ITEM consale_item")
				.depends(newPlain().in("where").output("consale.ITEM_CODE = consale_item.ITEM_ID"))
				.depends(memberSaleTable);
		
		Clause campaignSegmentTable = newPlain().in("from").output(MBRODS+".T_CAMPAIGN_SEGMENT campaign_segment")
				.depends(newPlain().in("where").output("consale.MEMBER_ID = campaign_segment.MEMBER_ID"))
				.depends(memberSaleTable);
		
		Clause groupBy = newPlain().in("groupby").output("member.MEMBER_KEY,campaign_segment.IS_CONTROL,consale.PAYMENT_CODE,nvl(consale.BK_ORDER_ID,consale.RE_ORDER_ID),consale_item.ITEM_CODE");
		
		conSaleParser = newParser(QUERY_PARAGRAPHS)
				.add(newPlain().in("select").output("member.MEMBER_KEY,campaign_segment.IS_CONTROL,consale.PAYMENT_CODE,nvl(consale.BK_ORDER_ID,consale.RE_ORDER_ID) ORDER_CODE,consale_item.ITEM_CODE,sum(consale.GOODS_COUNT * (consale.BK_SALE_AMOUNT - consale.RE_SALE_AMOUNT)) INCOME"))
				.add(newPlain().in("from").output(/**RPT2*/CCSDW+".V_DW_F_MEMBER member")
						.depends(saleCinemaTable)
						.depends(saleItemTable)
						.depends(campaignSegmentTable))
				.add(whereDef)
				.add(groupBy)
				
				//节假日卖品交易
				.add(notEmpty("conSaleHoliday"), newValue().in("where").output("consale_date.HOLIDAY_ID {0}", DataType.SQL, true).addValueMapper(0, booleanMapper)
			 		.depends(saleDateTable))
				//卖品交易时段
				.add(notEmpty("conSaleHourPeriod"), newExpression().in("where").output("consale_hour.TIME_DIVIDING_ID", DataType.STRING)
					.depends(saleHourTable))
				//卖品交易小时
				.add(notEmpty("conSaleHour"), newExpression().in("where").output("consale.HOUR_KEY", DataType.STRING)
						.depends(memberSaleTable))
				//卖品交易影城
				.add(notEmpty("conSaleCinema"), newExpression().in("where").output("consale_cinema.INNER_CODE", DataType.STRING, new CinemaCompositeParser())
						.depends(saleCinemaTable))
				//卖品品项金额
				.add(notEmpty("conSaleAmount"), newExpression().in("having").output("sum(consale.BK_SALE_AMOUNT) - sum(consale.RE_SALE_AMOUNT)", DataType.DOUBLE)
						.depends(memberSaleTable))
				//卖品品类
				.add(notEmpty("conSaleCategory"), newExpression().in("where").output("consale_cate.ITEM_CLASS_CODE", DataType.LONG)
						.depends(saleClassTable))
				//卖品品项
				.add(notEmpty("conSaleItem"), newExpression().in("where").output("consale_item.item_code", DataType.STRING, new ConItemCompositeParser())
						.depends(saleItemTable));
		
		return conSaleParser;
	}
	
}
