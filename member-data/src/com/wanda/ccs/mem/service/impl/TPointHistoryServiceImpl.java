package com.wanda.ccs.mem.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Vector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wanda.ccs.mem.dao.ITPointHistoryDao;
import com.wanda.ccs.mem.service.TMemberPointService;
import com.wanda.ccs.mem.service.TPointHistoryService;
import com.wanda.ccs.model.TMemberPoint;
import com.wanda.ccs.model.TPointHistory;
import com.wanda.mms.control.stream.PointAdjust;
import com.wanda.mms.control.stream.vo.PointHistroy;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;

@Service
public class TPointHistoryServiceImpl extends
		BaseCrudServiceImpl<TPointHistory> implements TPointHistoryService {

	@Autowired
	private ITPointHistoryDao dao;

	@Autowired
	private TMemberPointService tMemberPointService;

	@Override
	public IBaseDao<TPointHistory> getDao() {
		return dao;
	}

	@Override
	public PageResult<TPointHistory> findByCriteria(
			QueryCriteria<String, Object> query) {
		query.setPageSize(10);
		if (query.get("pagePoint") != null) {
			query.setPage(Integer.parseInt(String.valueOf(query.get("pagePoint"))));
		}
		Vector<String> queryParam = new Vector<String>();
		String fromClause = " from TPointHistory c ";
		queryParam.add("c.memberId = :memberId ");
		queryParam.add("c.isDeleted = 0");
		String[] whereBodies = queryParam
				.toArray(new String[queryParam.size()]);
		String joinClause = "";
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = " c.setTime desc, c.updatedDate desc";
		}
		setQueryCacheable(false);
		PageResult<TPointHistory> result = getDao().queryQueryCriteria(
				fromClause, joinClause, orderClause, whereBodies, query);
		return result;
	}

	/**
	 * select ca.inner_name as INNER_NAME,god.biz_date as BIZ_DATE,god.order_id
	 * as ORDER_ID,god.goods_name as GOODS_NAME,god.amont as AMONT,
	 * god.goods_count as GOODS_COUNT, god.level_point as LEVLE_POINT,
	 * god.trans_type as TRANS_TYPE,god.activity_point as ACTIVITY_POINT,
	 * god.point as POINT, god.trans_time as TRANS_TIME,ter.name as
	 * NAME,ter.status as STATUS,ter.start_dtime as START_DTIME,ter.end_dtime as
	 * END_DTIME from t_goods_trans_order goo inner join t_member tm on
	 * goo.member_id =tm.member_id inner join t_cinema ca on
	 * goo.cinema_inner_code =ca.inner_code left join t_goods_trans_detail god
	 * on god.trans_order_id=goo.trans_order_id and
	 * god.cinema_inner_code=goo.cinema_inner_code left join t_point_history pt
	 * on goo.order_id=pt.point_trans_code and
	 * goo.cinema_inner_code=pt.cinema_inner_code left join t_con_item ci on
	 * ci.item_code=god.goods_id left join t_ext_point_rule ter on
	 * ter.ext_point_rule_id=pt.POINT_EXT_RULE_ID where goo.order_id=94220 and
	 * god.cinema_inner_code=993
	 * 
	 * 
	 * 
	 * select ca.inner_name as INNER_NAME,god.biz_date as BIZ_DATE,god.order_id
	 * as ORDER_ID,god.goods_name as NAME,god.amont as AMONT, god.goods_count as
	 * GOODS_COUNT,god.level_point as LEVEL_POINT,god.trans_type as
	 * TRANS_TYPE,god.activity_point as ACTIVITY_POINT, god.point as POINT,
	 * god.trans_time as TRANS_TIME, god.ext_point_rule_id as EXT_POINT_RULE_ID
	 * 
	 * from t_goods_trans_order goo inner join t_member tm on goo.member_id
	 * =tm.member_id inner join t_cinema ca on goo.cinema_inner_code
	 * =ca.inner_code left join t_goods_trans_detail god on
	 * god.trans_order_id=goo.trans_order_id and
	 * god.cinema_inner_code=goo.cinema_inner_code where goo.order_id=94204 and
	 * god.cinema_inner_code=993
	 */

	@SuppressWarnings("all")
	public PageResult<TPointHistory> findByCriteriaForCon(
			QueryCriteria<String, Object> query, Map<String, Object> map) {
		query.setPageSize(10);
		StringBuffer buffer = new StringBuffer();
		buffer.append("  select ca.inner_name as INNER_NAME,god.biz_date as BIZ_DATE,god.order_id as ORDER_ID,god.goods_name  as GOODS_NAME,god.amont as AMONT, ");
		buffer.append("  god.goods_count as GOODS_COUNT,god.level_point as LEVEL_POINT,god.trans_type as TRANS_TYPE,god.activity_point as ACTIVITY_POINT, ");
		buffer.append(" god.point as POINT, goo.trans_time  as TRANS_TIME, god.ext_point_rule_id as EXT_POINT_RULE_ID ");
		buffer.append("  from t_goods_trans_order goo inner join t_member tm on goo.member_id =tm.member_id inner join t_cinema ca on goo.cinema_inner_code =ca.inner_code ");
		buffer.append(" left join   t_goods_trans_detail god on god.trans_order_id=goo.trans_order_id and god.cinema_inner_code=goo.cinema_inner_code ");
		// buffer.append(" left join t_con_item ci on ci.item_code=god.goods_id left join t_ext_point_rule ter on ter.ext_point_rule_id=pt.POINT_EXT_RULE_ID ");
		buffer.append(" where  goo.order_id= " + query.get("ORDER_ID")
				+ " and god.cinema_inner_code = "
				+ query.get("CINEMA_INNERCODE"));
		String sqlCount = " select count(*) from (" + buffer.toString() + ")";

		this.setQueryCacheable(false);
		PageResult result = this.getDao().queryNativeSQL(buffer.toString(),
				sqlCount, map, query.getStartIndex(), query.getPageSize());
		return result;
	}

	/**
	 * select pt.point_history_id,ca.seqid,pt.point_sys,tod.order_id,tod.
	 * cinema_inner_code,tm.member_id,tm.member_no,tm.mobile, ca.inner_name as
	 * INNER_NAME, tod.order_id as ORDER_ID,too.film_name as
	 * FILM_NAME,too.hall_num as HALL_NUM,tod.ticket_type_name as
	 * TICKET_TYPE_NAME, tod.ticket_no as TICKET_NO,tod.amount as
	 * AMOUNT,tod.refund_flag as REFUND_FLAG,tod.level_point as
	 * LEVEL_POINT,tod.activity_point as ACTIVITY_POINT, tod.point as
	 * POINT,too.trans_time as TRANS_TIME,too.biz_date as BIZ_DATE,
	 * too.show_time as SHOW_TIME, ter.name as NAME,ter.status as STATUS,
	 * ter.start_dtime as START_DTIME,ter.end_dtime as END_DTIME from
	 * t_ticket_trans_order too inner join t_member tm on too.member_id
	 * =tm.member_id inner join t_cinema ca on too.cinema_inner_code=
	 * ca.inner_code left join t_ticket_trans_detail tod on too.trans_order_id =
	 * tod.trans_order_id and too.cinema_inner_code=tod.cinema_inner_code left
	 * join t_point_history pt on too.order_id = pt.point_trans_code and
	 * too.order_id = pt.point_trans_code left join t_ext_point_rule ter on
	 * ter.ext_point_rule_id=pt.POINT_EXT_RULE_ID where too.order_id='94205' and
	 * too.cinema_inner_code='993'
	 * 
	 * 
	 * select ca.inner_name as INNER_CODE, tod.order_id as
	 * ORDER_ID,too.film_name as FILM_NAME, too.hall_num as HALL_NUM,
	 * tod.ticket_type_name as TICKET_TYPE_NAME, tod.ticket_no as TICKET_NO,
	 * tod.amount as AMOUNT, tod.refund_flag as REFUND_FLAG,tod.level_point as
	 * LEVEL_POINT,tod.activity_point as ACTIVITY_POINT, tod.point as
	 * POINT,too.trans_time as TRANS_TIME,too.biz_date as BIZ_DATE,too.show_time
	 * as SHOW_TIME,tod.ext_point_rule_id as EXT_POINT_RULE_ID from
	 * t_ticket_trans_order too inner join t_member tm on too.member_id
	 * =tm.member_id inner join t_cinema ca on too.cinema_inner_code=
	 * ca.inner_code left join t_ticket_trans_detail tod on too.trans_order_id =
	 * tod.trans_order_id and too.cinema_inner_code=tod.cinema_inner_code where
	 * too.order_id='94214' and too.cinema_inner_code='993'
	 */
	@SuppressWarnings("all")
	public PageResult<TPointHistory> findByCriteriaForTicket(
			QueryCriteria<String, Object> query, Map<String, Object> map) {
		query.setPageSize(10);
		StringBuffer buffer = new StringBuffer();
		buffer.append(" select ca.inner_name as INNER_NAME, too.order_id as ORDER_ID,too.film_name as FILM_NAME, too.hall_num  as HALL_NUM, tod.ticket_type_name as TICKET_TYPE_NAME, ");
		buffer.append(" tod.ticket_no as TICKET_NO, tod.amount  as AMOUNT, tod.refund_flag as REFUND_FLAG,tod.level_point as LEVEL_POINT,tod.activity_point as ACTIVITY_POINT, ");
		buffer.append("  tod.point as POINT,too.trans_time  as  TRANS_TIME,too.biz_date  as BIZ_DATE,too.show_time as SHOW_TIME,tod.ext_point_rule_id as EXT_POINT_RULE_ID ");
		// buffer.append(" ter.start_dtime as START_DTIME,ter.end_dtime as END_DTIME");
		buffer.append(" from t_ticket_trans_order too inner join t_member tm on too.member_id =tm.member_id inner join t_cinema ca on too.cinema_inner_code= ca.inner_code ");
		buffer.append(" left  join t_ticket_trans_detail tod on too.trans_order_id = tod.trans_order_id and too.cinema_inner_code=tod.cinema_inner_code  ");
		// buffer.append(" left join t_point_history pt on too.order_id = pt.point_trans_code  and too.order_id = pt.point_trans_code left join t_ext_point_rule ter on ter.ext_point_rule_id=pt.POINT_EXT_RULE_ID");
		buffer.append(" where  too.order_id= " + query.get("ORDER_ID")
				+ " and too.cinema_inner_code = "
				+ query.get("CINEMA_INNERCODE"));
		String sqlCount = " select count(*) from (" + buffer.toString() + ")";

		this.setQueryCacheable(false);
		PageResult result = this.getDao().queryNativeSQL(buffer.toString(),
				sqlCount, map, query.getStartIndex(), query.getPageSize());
		return result;
	}

	@Override
	public void createOrUpdate(TPointHistory pHistory) {
		PointHistroy ph = new PointHistroy();

		ph.setMemberid(pHistory.getMemberId());

		ph.setAdj_reason(pHistory.getAdjResion());
		ph.setAdj_Resion(pHistory.getAdjResion());
		ph.setAdj_reason_type(String.valueOf(pHistory.getAdjReasonType()));

		if (pHistory.getLevelPoint() != null
				&& !"".equals(pHistory.getLevelPoint())) {

			String ss1 = String.valueOf(pHistory.getLevelPoint());
			BigDecimal d5 = new BigDecimal(ss1);
			String s6 = d5.setScale(0, BigDecimal.ROUND_DOWN).toString();
			long a = Long.valueOf(s6);
			ph.setLevel_Point(a);
		} else {
			ph.setLevel_Point(0);
		}
		if (pHistory.getActivityPoint() != null
				&& !"".equals(pHistory.getActivityPoint())) {
			String ss1 = String.valueOf(pHistory.getActivityPoint());
			BigDecimal d5 = new BigDecimal(ss1);
			String s6 = d5.setScale(0, BigDecimal.ROUND_DOWN).toString();
			long a = Long.valueOf(s6);
			ph.setActivity_Point(a);
		} else {
			ph.setActivity_Point(0);
		}
		ph.setExchange_Point(ph.getLevel_Point() + ph.getActivity_Point());

		ph.setCreate_By(pHistory.getCreatedBy());
		ph.setPoint_Type("4");
		ph.setTicket_Count(0);
		ph.setPoint_Sys("3");
		ph.setPoint_Trans_Type("1");
		Date date = null;
		if (pHistory.getSetTime() != null && !"".equals(pHistory.getSetTime())) {
			date = pHistory.getSetTime();
		} else {
			date = new Date();
		}

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String yyyy = String.valueOf(format.format(date));
		String te = new SimpleDateFormat(" HH:mm:ss").format(new Date());
		String settime = yyyy + te;
		// ph.setExchange_Point_Expire_Time(time);

		// ph.setSetTime(format.format(date));
		ph.setSetTime(settime);
		ph.setCinema_inner_code(pHistory.getCinemaInnerCode());
		PointAdjust pAdjust = new PointAdjust();
		pAdjust.adjust(super.getUniversalDao().getConnection(), ph);
	}

	@Override
	public boolean checkPointBalance(Long activityPoint, Long levelPoint,
			Long memberId) {
		if (memberId != null) {
			/*
			 * todu
			 */
			TMemberPoint tMemberPoint = tMemberPointService
					.getTMemberPointByMemId(memberId);
			if (((activityPoint + levelPoint) + tMemberPoint
					.getExgPointBalance()) < 0) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

}
