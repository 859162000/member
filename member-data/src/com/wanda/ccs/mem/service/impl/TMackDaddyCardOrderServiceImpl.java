package com.wanda.ccs.mem.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.wanda.ccs.auth.CcsUserProfile;
import com.wanda.ccs.jobhub.client.JobScheduleService;
import com.wanda.ccs.mem.dao.ITMackDaddyCardOrderDao;
import com.wanda.ccs.mem.service.TMackDaddyCardOrderLogService;
import com.wanda.ccs.mem.service.TMackDaddyCardOrderService;
import com.wanda.ccs.model.IMemberDimType;
import com.wanda.ccs.model.TMackDaddyCardOrder;
import com.wanda.ccs.model.TMackDaddyCardOrderLog;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;
import com.xcesys.extras.core.util.DateUtil;

@Service
public class TMackDaddyCardOrderServiceImpl extends
		BaseCrudServiceImpl<TMackDaddyCardOrder> implements
		TMackDaddyCardOrderService {

	@Autowired
	private ITMackDaddyCardOrderDao dao = null;
	
	@Autowired
	private TMackDaddyCardOrderLogService logSvc;
	
	@Autowired
	private JobScheduleService jobScheduleService;

	@Override
	public IBaseDao<TMackDaddyCardOrder> getDao() {
		return dao;
	}

	@Override
	public PageResult<TMackDaddyCardOrder> findByCriteria(
			QueryCriteria<String, Object> query) {
		String fromClause = "from TMackDaddyCardOrder c";
		String[] whereBodies = new String[] {
				"c.status = :status",
				"c.tCinema.area = :region",
				"c.cinemaId = :cinemaId",
				"c.submitBy like :submitBy"
		};
		String joinClause = "";
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = "c.id desc";
		}

		PageResult<TMackDaddyCardOrder> result = getDao().queryQueryCriteria(
				fromClause, joinClause, orderClause, whereBodies, query);
		return result;

	}
	
	@Override
	public void createOrUpdateRequest(CcsUserProfile user, TMackDaddyCardOrder order, String action, String comments){
		if(order == null)
			return;
		String status = match(action, new String[] {
				IMemberDimType.DIMTYPE_CARD_ORDER_STATUS_A,
				IMemberDimType.DIMTYPE_CARD_ORDER_STATUS_F,
				IMemberDimType.DIMTYPE_CARD_ORDER_STATUS_P,
				IMemberDimType.DIMTYPE_CARD_ORDER_STATUS_X});
		TMackDaddyCardOrderLog log = new TMackDaddyCardOrderLog();
		log.setActionBy(user.getName());
		log.setActionById(user.getId());
		log.setComments(comments);
		log.setFromStatus(order.getStatus());
		if(status.equals(IMemberDimType.DIMTYPE_CARD_ORDER_STATUS_A)){
			log.setActionName("提交申请");
		}else if(status.equals(IMemberDimType.DIMTYPE_CARD_ORDER_STATUS_F)){
			log.setActionName("审核拒绝");
		}else if(status.equals(IMemberDimType.DIMTYPE_CARD_ORDER_STATUS_P)){
			log.setActionName("审核通过");
		}else if(status.equals(IMemberDimType.DIMTYPE_CARD_ORDER_STATUS_X)){
			log.setActionName("取消审核");
		}
		order.setStatus(status);
		if(order.getId() == null){
			order.setSubmitBy(user.getName());
			order.setSubmitById(user.getId());
			order.setSubmitTime(DateUtil.getCurrentDate());
		}
		if(status.equals(IMemberDimType.DIMTYPE_CARD_ORDER_STATUS_E) || status.equals(IMemberDimType.DIMTYPE_CARD_ORDER_STATUS_P)){
			log.setActionBy(user.getName());
			log.setActionById(user.getId());
			log.setActionTime(DateUtil.getCurrentDate());
		}
		if(status.equals(IMemberDimType.DIMTYPE_CARD_ORDER_STATUS_P)){
			//当制卡申请单审批通过时,生成起始卡号并往任务表中插入数据,在etl中进行批处理
			String sql = "select max(end_no) as maxno from T_MACK_DADDY_CARD_ORDER c where c.cinema_id = :cinemaId and c.status = :status";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("cinemaId", order.gettCinema().getId());
			params.put("status", IMemberDimType.DIMTYPE_CARD_ORDER_STATUS_P);
			setQueryCacheable(false);
			List<Map<String, ?>> list = getDao().queryNativeSQL(sql, params);
			StringBuffer startNo = new StringBuffer();
			StringBuffer endNo = new StringBuffer();
			startNo.append("6").append(order.gettCinema().getInnerCode()).append("0000");
			endNo.append("6").append(order.gettCinema().getInnerCode()).append("0000");
			if(list != null && !list.isEmpty() && list.get(0) != null && list.get(0).get("MAXNO") != null){
				String maxCode = list.get(0).get("MAXNO").toString();
				startNo.append(String.format("%08d", Long.valueOf(maxCode.substring(maxCode.length()-8))+1));
				endNo.append(String.format("%08d", Long.valueOf(maxCode.substring(maxCode.length()-8))+order.getNumberOfCards()));
			}else{
				startNo.append(String.format("%08d", 1L));
				endNo.append(String.format("%08d", Long.valueOf(order.getNumberOfCards())));
			}
			order.setStartNo(startNo.toString());
			order.setEndNo(endNo.toString());
			
			jobScheduleService.scheduleJob("CreateCardJob", "member-jobs");
		}
		getDao().createOrUpdate(order);
		log.setToStatus(order.getStatus());
		log.setActionTime(DateUtil.getCurrentDate());
		log.settMackDaddyCardOrder(order);
		log.setMackDaddyCardOrderId(order.getId());
		logSvc.createOrUpdate(log);
	}
	
	public static String match(String str, String[] values) {
		if (str == null)
			return null;
		if (values == null)
			return str;
		for (String s : values)
			if (s.equals(str))
				return str;
		return null;
	}
	
}
