package com.wanda.ccs.member.segment.defimpl;

import static com.wanda.ccs.member.segment.SegmentConstants.Schemas.RPT2;
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


public class ExtPointCriteriaDef {
	
	public final static String GROUP_ID_MEMBER = "memberLevel"; //会员基本组ID
	
	public final static String GROUP_ID_TICKET = "tsale";       //票房交易组ID
	
	public final static String GROUP_ID_CON_ITEM = "conSale";   //卖品交易组ID

	private CriteriaParser conSaleParser;
	
	private CriteriaParser ticketSaleParser;
	
	private CriteriaParser memberParser;

	public ExtPointCriteriaDef () {
		
		//公共composite parser定义
		FilmCompositeParser filmCom = new FilmCompositeParser();
		CinemaCompositeParser cinemaCom = new CinemaCompositeParser();
		ConItemCompositeParser conitemCom = new ConItemCompositeParser();
		
		//公共值映射定义
		Map<Object, Object> booleanMapper = new HashMap<Object, Object>();
		booleanMapper.put("0", "is null");
		booleanMapper.put("1", "is not null");	
		
		//公共条件
		Clause whereDef = newPlain().in("where").output("member.STATUS='1' and member.ISDELETE = 0");
		
		Clause member_cinema = newPlain().in("from").output(RPT2+".T_D_CON_CINEMA member_cinema")
				.depends(newPlain().in("where").output("member_cinema.CINEMA_KEY = member.CINEMA_KEY"));

		//定义会员基本信息条件
		//注册影城
		Clause member_registerCinema = newExpression().in("where").output("member.RECRUIT_CINEMA", DataType.STRING, cinemaCom);
		//性别
		Clause member_gender = newExpression().in("where").output("member.SEX", DataType.STRING);
		//现在的等级
		Clause member_nowMemberLevel = newExpression().in("where").output("member.MEMBER_LEVEL_CODE", DataType.STRING);
		//z招募日期
		Clause member_registerDate = newExpression().in("where").output("member.RECRUIT_TIME", DataType.DATE_TIME);
		//注册方式
		Clause member_registerType = newExpression().in("where").output("member.RECRUIT_TYPE", DataType.STRING);
		//生日
		Clause member_birthday = newExpression().in("where").output("member.BIRTHDAY", DataType.DATE);
		//出生月
		//Clause member_birthmonth = newExpression().in("where").output("member.BIRTH_MONTH", DataType.STRING, null, false));
		//管理影城
		Clause member_manageCinema = newExpression().in("where").output("member_cinema.INNER_CODE", DataType.STRING, cinemaCom)
				.depends(member_cinema);
		
		
		this.memberParser = newParser(QUERY_PARAGRAPHS)
				.add(newPlain().in("select").output("member.MEMBER_KEY"))
				.add(newPlain().in("from").output(RPT2+".T_D_CON_MEMBER member").depends(member_cinema))
				.add(whereDef)
		//添加会员基本信息的查询
		.add(notEmpty("registerCinema"), member_registerCinema)//注册影城
		.add(notEmpty("gender"), member_gender) //性别
		.add(notEmpty("nowMemberLevel"), member_nowMemberLevel) //当前级别
		.add(notEmpty("registerDate"), member_registerDate) //招募日期
		.add(notEmpty("registerType"), member_registerType) //注册类型
		.add(notEmpty("birthday"), member_birthday) //生日
		.add(notEmpty("manageCinema"), member_manageCinema); //管理影城
		
		
		//卖品交易
		Clause consale = newPlain().in("from").output(RPT2+".T_F_CON_MEMBER_SALE consale")
				.depends(newPlain().in("where").output("member.MEMBER_KEY=consale.MEMBER_KEY"));
		
		Clause consale_date = newPlain().in("where").output("consale_date.DATE_KEY=consale.BOOK_DATE_KEY")
				.depends(newPlain().in("from").output(RPT2+".T_D_CON_DATE consale_date"))
				.depends(consale);

		Clause consale_hour =newPlain().in("from").output(RPT2+".T_D_CON_HOUR consale_hour")
				.depends(newPlain().in("where").output("consale_hour.HOUR_KEY=consale.BOOK_HOUR_KEY"))
				.depends(consale);
		
		Clause consale_cinema = newPlain().in("from").output(RPT2+".T_D_CON_CINEMA consale_cinema")
				.depends(newPlain().in("where").output("consale_cinema.CINEMA_KEY = consale.CINEMA_KEY"))
				.depends(consale);
		
		Clause consale_cate = newPlain().in("from").output(RPT2+".T_D_CON_CS_CLASS consale_cate")
				.depends(newPlain().in("where").output("consale.SALE_CLASS_KEY = consale_cate.SALE_CLASS_KEY"))
				.depends(consale);
		
		Clause consale_item = newPlain().in("from").output(RPT2+".T_D_CON_CS_SALE_ITEM consale_item")
				.depends(newPlain().in("where").output("consale.SALE_ITEM_KEY = consale_item.SALE_ITEM_KEY"))
				.depends(consale);
		
		

		this.conSaleParser = newParser(QUERY_PARAGRAPHS)
			.add(newPlain().in("select").output("consale_cinema.INNER_CODE,member.MEMBER_KEY,sum(consale.BK_SALE_AMOUNT - consale.RE_SALE_AMOUNT) SALE_AMOUNT,nvl(consale.BK_CS_ORDER_CODE,consale.RE_CS_ORDER_CODE) CS_ORDER_CODE,consale_item.ITEM_CODE"))
			//.add(newPlain().in("select").output("consale_cinema.INNER_CODE,member.MEMBER_KEY,consale.BK_CS_ORDER_CODE,consale_item.ITEM_CODE"))
			.add(newPlain().in("from").output(RPT2+".T_D_CON_MEMBER member")
					.depends(consale_cinema)
					.depends(consale_item))
			.add(whereDef)

			//卖品交易日期
			//.add(notEmpty("conSaleDate", newExpression().in("where"").output("consale.BOOK_DATE_KEY", DataType.DATE)
			//		.depends(TableClause.consale))
			//节假日卖品交易
			.add(notEmpty("conSaleHoliday"), newValue().in("where").output("consale_date.HOLIDAY_ID  {0}", DataType.SQL, true).addValueMapper(0, booleanMapper)
		 		.depends(consale_date))
			//卖品交易时段
			.add(notEmpty("conSaleHourPeriod"), newExpression().in("where").output("consale_hour.TIME_DIVIDING_ID", DataType.STRING)
				.depends(consale_hour))
			//卖品交易小时
			.add(notEmpty("conSaleHour"), newExpression().in("where").output("consale.BOOK_HOUR_KEY", DataType.STRING)
					.depends(consale))
			//卖品交易影城
			.add(notEmpty("conSaleCinema"), newExpression().in("where").output("consale_cinema.INNER_CODE", DataType.STRING, cinemaCom)
					.depends(consale_cinema))
			//卖品品项金额
			.add(notEmpty("conSaleAmount"), newExpression().in("having").output("sum(consale.BK_SALE_AMOUNT) - sum(consale.RE_SALE_AMOUNT)", DataType.DOUBLE)
					.depends(consale))
			//卖品品类
			.add(notEmpty("conSaleCategory"), newExpression().in("where").output("consale_cate.THIRD_CLASS_ID", DataType.LONG)
					.depends(consale_cate))
			//卖品品项
			.add(notEmpty("conSaleItem"), newExpression().in("where").output("consale_item.item_code", DataType.STRING, conitemCom)
					.depends(consale_item))
			//生日观影，卖品条件
			.add(notEmpty("birthdayFilm"), newPlain().in("where").output("to_char(member.BIRTHDAY,'mm/dd')=to_char(consale.BOOK_DATE_KEY,'mm/dd')")
					.depends(consale))
			//添加会员基本信息的查询
			.add(notEmpty("registerCinema"), member_registerCinema)//注册影城
			.add(notEmpty("gender"), member_gender) //性别
			.add(notEmpty("nowMemberLevel"), member_nowMemberLevel) //当前级别
			.add(notEmpty("registerDate"), member_registerDate) //招募日期
			.add(notEmpty("registerType"), member_registerType) //注册类型
			.add(notEmpty("birthday"), member_birthday) //生日
			//.add(notEmpty("birthmonth"), member_birthmonth) //出生月
			.add(notEmpty("manageCinema"), member_manageCinema); //管理影城

		
		//票房交易
		Clause transSales = newPlain().in("from").output(RPT2+".T_F_CON_MEMBER_TICKET transSales")
				.depends(newPlain().in("where").output("member.MEMBER_KEY=transSales.MEMBER_KEY"));
		
		Clause transSales_date = newPlain().in("where").output("transSales_date.DATE_KEY=transSales.SHOW_DATE_KEY")
				.depends(newPlain().in("from").output(RPT2+".T_D_CON_DATE transSales_date")).depends(transSales);
		
		Clause transSales_hour = newPlain().in("from").output(RPT2+".T_D_CON_HOUR transSales_hour")
				.depends(newPlain().in("where").output("transSales_hour.HOUR_KEY=transSales.SHOW_HOUR_KEY"))
				.depends(transSales);
		
		Clause transSales_cinema = newPlain().in("from").output(RPT2+".T_D_CON_CINEMA transSales_cinema")
				.depends(newPlain().in("where").output("transSales_cinema.CINEMA_KEY = transSales.CINEMA_KEY"))
				.depends(transSales);
		
		Clause transSales_film = newPlain().in("from").output(RPT2+".T_D_CON_FILM transSales_film")
				.depends(newPlain().in("where").output("transSales_film.FILM_KEY = transSales.FILM_KEY"))
				.depends(transSales);
	

		this.ticketSaleParser = newParser(QUERY_PARAGRAPHS)
			.add(newPlain().in("select").output("transSales_cinema.INNER_CODE,member.MEMBER_KEY,sum(transSales.Bk_Admissions -  transSales.Re_Admissions) Admissions,nvl(transSales.BK_CT_ORDER_CODE,transSales.RE_CT_ORDER_CODE) CT_ORDER_CODE,nvl(transSales.BK_TICKET_NUMBER,transSales.re_TICKET_NUMBER) TICKET_NUMBER"))
			//.add(newPlain().in("select").output("transSales_cinema.INNER_CODE,member.MEMBER_KEY,transSales.BK_CT_ORDER_CODE,transSales.BK_TICKET_NUMBER"))
			.add(newPlain().in("from").output(RPT2+".T_D_CON_MEMBER member")
					.depends(transSales_cinema)
					.depends(transSales))
			.add(whereDef)
			//观影日期
			//.add(notEmpty("watchTradeDay"), newExpression().in("where", "transSales.SHOW_DATE_KEY", DataType.DATE)
			//		.depends(TableClause.transSales))
			//节假日观影
			.add(notEmpty("holidayWatchTrade"), newValue().in("where").output("transSales_date.HOLIDAY_ID  {0}", DataType.SQL, true).addValueMapper(0, booleanMapper)
					.depends(transSales_date))
			//观影交易时段
			.add(notEmpty("watchTradeTime"), newExpression().in("where").output("transSales_hour.TIME_DIVIDING_ID", DataType.STRING)
					.depends(transSales_hour))
			//观影交易小时
			.add(notEmpty("watchTradeHour"), newExpression().in("where").output("transSales.SHOW_HOUR_KEY", DataType.STRING)
					.depends(transSales))
			//观影交易影城
			.add(notEmpty("watchTradeCinema"), newExpression().in("where").output("transSales_cinema.INNER_CODE", DataType.STRING, cinemaCom)
					.depends(transSales_cinema))
			//观影影片
			.add(notEmpty("transFilm"), newExpression().in("where").output("transSales_film.FILM_CODE", DataType.STRING, filmCom)
					.depends(transSales_film))
			//生日当天观影
			.add(notEmpty("birthdayFilm"), newPlain().in("where").output("to_char(member.BIRTHDAY,'mm/dd')=to_char(transSales.SHOW_DATE_KEY,'mm/dd')")
					.depends(transSales))
			//添加会员基本信息的查询
			.add(notEmpty("registerCinema"), member_registerCinema)//注册影城
			.add(notEmpty("gender"), member_gender) //性别
			.add(notEmpty("nowMemberLevel"), member_nowMemberLevel) //当前级别
			.add(notEmpty("registerDate"), member_registerDate) //招募日期
			.add(notEmpty("registerType"), member_registerType) //注册类型
			.add(notEmpty("birthday"), member_birthday) //生日
			//.add(notEmpty("birthmonth"), member_birthmonth) //出生月
			.add(notEmpty("manageCinema"), member_manageCinema); //管理影城
	}
	
	
	public CriteriaParser getMemberParser() {
		return memberParser;
	}
	
	
	/**
	 * 得到卖品的交易信息解析器
	 * @return
	 */
	public CriteriaParser getConSaleParser() {
		return conSaleParser;
	}

	/**
	 * 得到卖品的交易信息解析器
	 * @return
	 */
	public CriteriaParser getTicketSaleParser() {
		return ticketSaleParser;
	}
	
}
