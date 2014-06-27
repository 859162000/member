package com.wanda.ccs.jobhub.member.service.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.wanda.ccs.jobhub.member.dao.CampaignDao;
import com.wanda.ccs.jobhub.member.job.CampaignCalculateJob;
import com.wanda.ccs.jobhub.member.job.CampaignCalculateJob.CalculateType;
import com.wanda.ccs.jobhub.member.service.CampaignCalculateService;
import com.wanda.ccs.jobhub.member.vo.CampaignDetailVo;
import com.wanda.ccs.jobhub.member.vo.CampaignVo;
import com.wanda.ccs.member.segment.defimpl.CampaignCriteriaDef;
import com.wanda.ccs.sqlasm.CriteriaResult;
import com.wanda.ccs.sqlasm.expression.ExpressionCriterion;
import com.wanda.ccs.sqlasm.expression.JsonCriteriaHelper;

@Transactional(rollbackFor = { java.lang.Exception.class })
public class CampaignCalculateServiceImpl implements CampaignCalculateService {
	// 票类影城
	public static final String TICKET_FLAG = "inputId\":\"watchTradeCinema\",\"groupId\":\"tsale\",\"label\":\"观影影城\",\"groupLabel\":\"票房交易\",\"operator\":\"in\",\"value";
	// 卖品影城
	public static final String CONSALE_FLAG = "inputId\":\"conSaleCinema\",\"groupId\":\"conSale\",\"label\":\"卖品交易影城\",\"groupLabel\":\"卖品交易\",\"operator\":\"in\",\"value";
	
	public static final String PREFIX = "select b.IS_CONTROL,count(b.MEMBER_KEY) NUM,sum(b.INCOME) INCOME from (select a.MEMBER_KEY,a.IS_CONTROL, sum(case when a.PAY_METHOD_KEY=232 then 0 else a.INCOME end) INCOME from (";
	
	public static final String SUFFIX = ") a where a.INCOME > 0 group by a.MEMBER_KEY, a.IS_CONTROL) b group by b.IS_CONTROL";
	
	public static final String SQL_CAL_PREFIX = " select distinct a.MEMBER_KEY,a.IS_CONTROL from ( ";
	
	public static final String SQL_CAL_SUFFIX = " ) a where a.INCOME > 0 ";
	
	@InstanceIn(path = "CampaignDao")
	private CampaignDao campaignDao;
	
	private String parseCalMemberSql(String sql) {
		StringBuilder sb = new StringBuilder();
		sb.append(SQL_CAL_PREFIX).append(sql).append(SQL_CAL_SUFFIX);
		
		return sb.toString();
	}
	
	private String parseCalMemberSumSql(String ticketSql, String conSaleSql) {
		StringBuilder sql = new StringBuilder();
		sql.append(SQL_CAL_PREFIX);
		sql.append(ticketSql);
		sql.append("\n union all \n");
		sql.append(conSaleSql);
		sql.append(SQL_CAL_SUFFIX);
		
		return sql.toString();
	}
	
	private String getRate(Integer one, Integer two) {
		double t = 0;
		if(two > 0) {
			t = new Double(one) / new Double(two);
		}
        DecimalFormat df = new DecimalFormat("#.####");
        return df.format(t);
    }
	
	private String parseSql(String sql) {
		StringBuilder sb = new StringBuilder();
		sb.append(PREFIX).append(sql).append(SUFFIX);
		
		return sb.toString();
	}
	
	private String parseSumSql(String ticketSql, String conSaleSql) {
		StringBuilder sql = new StringBuilder();
		sql.append(PREFIX);
		sql.append(ticketSql);
		sql.append("\n union all \n");
		sql.append(conSaleSql);
		sql.append(SUFFIX);
		
		return sql.toString();
	}
	
	private void setValue(Map<String, Double> result, CampaignDetailVo vo, CampaignCalculateJob.CalculateType OperatType) {
		//没有推荐规则统计类型为3，
		if(OperatType == CampaignCalculateJob.CalculateType.CALCULATE || OperatType == CampaignCalculateJob.CalculateType.NON_CRITERIA_CALCULATE) { //统计
			// 推荐人数
			vo.setRecommendNum(result.get("RECOMMEND_NUM").intValue());
			// 对比组响应人数
			vo.setControlNum(result.get("CONTROL_NUM").intValue());
			// 推荐收入
			vo.setRecommendIncome(result.get("RECOMMEND_INCOME"));
			// 对比组响应收入
			vo.setControlIncome(result.get("CONTROL_INCOME"));
			// 计算推荐响应率
			vo.setRecommendRate(Double.valueOf(getRate(vo.getRecommendNum(), vo.getRecommendCount())));
			// 计算对比组响应率
			vo.setControlRate(Double.valueOf(getRate(vo.getControlNum(), vo.getControlCount())));
		} 
		
		if(OperatType == CampaignCalculateJob.CalculateType.SUM_CALCULATE || OperatType == CampaignCalculateJob.CalculateType.NON_CRITERIA_CALCULATE) { //总值
			// 总人数
			vo.setSumNum(result.get("RECOMMEND_NUM").intValue());
			// 总收入
			vo.setSumIncome(result.get("RECOMMEND_INCOME"));
			// 总响应率
			vo.setSumRate(Double.valueOf(getRate(vo.getSumNum(), vo.getRecommendCount())));
		}
	}
	
	/**
	 * 统计票收入
	 * 
	 * @param campaign
	 * @param vo
	 */
	@Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
	public void calTicketIncome(CampaignDetailVo vo) {
		//根据规则获取规则sql
		String criteriaScheme = vo.getCriteriaScheme();
		String cinemaScheme = vo.getCinemaScheme();
		cinemaScheme = cinemaScheme.replace("compositeCinema", TICKET_FLAG);
		criteriaScheme = criteriaScheme.substring(0,1)+cinemaScheme+','+criteriaScheme.substring(1,criteriaScheme.length());
		List<ExpressionCriterion> criteria = JsonCriteriaHelper.parse(criteriaScheme);
		CriteriaResult criteriaResult = CampaignCriteriaDef.TICKET_CRITERIA_PARSER.parse(criteria);
		
		// 原始sql
		String sourceSql = criteriaResult.getComposedText();
		// 添加交易日期和活动ID
		sourceSql = sourceSql.replace("#ymd", vo.getDateFormat());
		sourceSql = sourceSql.replace("#cid", vo.getCampaignId()+"");
		
		// 统计
		Map<String, Double> result = campaignDao.calIncome(parseSql(sourceSql), criteriaResult.getParameters().toArray());
		if(result != null && result.size() > 0) {
			setValue(result, vo, CampaignCalculateJob.CalculateType.CALCULATE);
		}
		
		// 统计计算会员落地表
		syncCalMember(parseCalMemberSql(sourceSql), criteriaResult.getParameters().toArray(), vo.getCampaignId(), vo.getYmd(), CalculateType.CALCULATE);
	}
	
	/**
	 * 统计卖品收入
	 * 
	 * @param campaign
	 * @param vo
	 */
	@Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
	public void calConSaleIncome(CampaignDetailVo vo) {
		//根据规则获取规则sql
		String criteriaScheme = vo.getCriteriaScheme();
		String cinemaScheme = vo.getCinemaScheme();
		cinemaScheme = cinemaScheme.replace("compositeCinema", CONSALE_FLAG);
		criteriaScheme = criteriaScheme.substring(0,1)+cinemaScheme+','+criteriaScheme.substring(1,criteriaScheme.length());
		List<ExpressionCriterion> criteria = JsonCriteriaHelper.parse(criteriaScheme);
		CriteriaResult criteriaResult = CampaignCriteriaDef.CONSALE_CRITERIA_PARSER.parse(criteria);
		
		// 原始sql
		String sourceSql = criteriaResult.getComposedText();
		// 添加交易日期和活动ID
		sourceSql = sourceSql.replace("#ymd", vo.getDateFormat());
		sourceSql = sourceSql.replace("#cid", vo.getCampaignId()+"");
		
		// 统计
		Map<String, Double> result = campaignDao.calIncome(parseSql(sourceSql), criteriaResult.getParameters().toArray());
		if(result != null && result.size() > 0) {
			setValue(result, vo, CampaignCalculateJob.CalculateType.CALCULATE);
		}
		
		// 统计计算会员落地表
		syncCalMember(parseCalMemberSql(sourceSql), criteriaResult.getParameters().toArray(), vo.getCampaignId(), vo.getYmd(), CalculateType.CALCULATE);
	}
	
	/**
	 * 统计总收入（注：此方法也可用于统计不包含推荐规则的活动）
	 * 
	 * @param campaign
	 * @param vo
	 */
	private void calIncome(CampaignDetailVo vo, CampaignCalculateJob.CalculateType operaType) {
		String cinemaScheme = vo.getCinemaScheme();
		
		// 票
		String ticketCinemaScheme = cinemaScheme.replace("compositeCinema", TICKET_FLAG);
		String ticketCriteriaScheme = "["+ticketCinemaScheme+"]";
		List<ExpressionCriterion> ticketCriteria = JsonCriteriaHelper.parse(ticketCriteriaScheme);
		CriteriaResult ticketCriteriaResult = CampaignCriteriaDef.TICKET_CRITERIA_PARSER.parse(ticketCriteria);
		
		//String ticketSql = parseSql(ticketCriteriaResult.getComposedText());
		String ticketSql = ticketCriteriaResult.getComposedText();
		//交易日期和活动ID
		ticketSql = ticketSql.replace("#ymd", vo.getDateFormat());
		ticketSql = ticketSql.replace("#cid", vo.getCampaignId()+"");
		
		// 卖品
		String conSaleCinemaScheme = cinemaScheme.replace("compositeCinema", CONSALE_FLAG);
		String conSaleCriteriaScheme = "["+conSaleCinemaScheme+"]";
		List<ExpressionCriterion> conSaleCriteria = JsonCriteriaHelper.parse(conSaleCriteriaScheme);
		CriteriaResult conSaleCriteriaResult = CampaignCriteriaDef.CONSALE_CRITERIA_PARSER.parse(conSaleCriteria);
		
		//String conSaleSql = parseSql(conSaleCriteriaResult.getComposedText());
		String conSaleSql = conSaleCriteriaResult.getComposedText();
		//交易日期和活动ID
		conSaleSql = conSaleSql.replace("#ymd", vo.getDateFormat());
		conSaleSql = conSaleSql.replace("#cid", vo.getCampaignId()+"");
		
		// union all 合并sql
		String sql = parseSumSql(ticketSql, conSaleSql);
		// 合并参数
		List<Object> param = new ArrayList<Object>();
		param.addAll(ticketCriteriaResult.getParameters());
		param.addAll(conSaleCriteriaResult.getParameters());
		
		Map<String, Double> result = campaignDao.calIncome(sql.toString(), param.toArray());
		if(result != null && result.size() > 0) {
			setValue(result, vo, operaType);
		}
		
		// 统计计算会员落地表
		syncCalMember(parseCalMemberSumSql(ticketSql, conSaleSql), param.toArray(), vo.getCampaignId(), vo.getYmd(), operaType);
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	private void syncCalMember(String sql, Object[] param, Long campaignId, Integer ymd, CalculateType operaType) {
		campaignDao.delSyncCalMember(campaignId, ymd, String.valueOf(operaType.getType()));
		campaignDao.syncCalMember(sql, param, campaignId, ymd, String.valueOf(operaType.getType()));
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
	public void calSumIncome(CampaignDetailVo vo) {
		calIncome(vo, CampaignCalculateJob.CalculateType.SUM_CALCULATE);
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
	public void calNonCriteriaIncome(CampaignDetailVo vo) {
		calIncome(vo, CampaignCalculateJob.CalculateType.NON_CRITERIA_CALCULATE);
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
	public List<CampaignVo> queryCampaign(String date, String[] status) {
		return campaignDao.queryCampaign(date, status);
	}

	@Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
	public CampaignDetailVo queryCalDetailTotal(Long campaignId, CalculateType calType) {
		CampaignDetailVo vo = campaignDao.queryCalTotal(campaignId);
		
		if(calType == CalculateType.NON_CRITERIA_CALCULATE) {
			Map<String, Integer> map = campaignDao.querySyncCalMemberCount(campaignId, String.valueOf(calType.getType()));
				vo.setRecommendNum(map.get("RECOMMEND_NUM"));
				vo.setControlNum(map.get("CONTROL_NUM"));
				vo.setSumNum(map.get("RECOMMEND_NUM"));
		} else {
			Map<String, Integer> map = campaignDao.querySyncCalMemberCount(campaignId, String.valueOf(CalculateType.CALCULATE.getType()));
			vo.setRecommendNum(map.get("RECOMMEND_NUM"));
			vo.setControlNum(map.get("CONTROL_NUM"));
			map = campaignDao.querySyncCalMemberCount(campaignId, String.valueOf(CalculateType.SUM_CALCULATE.getType()));
			vo.setSumNum(map.get("RECOMMEND_NUM"));
		}
		
		return vo;
	}

	@Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
	public List<CampaignDetailVo> queryCalDetailList(Integer ymd,
			Long campaignId, String type, String[] status) {
		 return campaignDao.queryCampaignDetailCal(ymd, campaignId, type, status);
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
	public boolean existCalDetailTotal(Long campaignId) {
		if(campaignDao.existCalTotal(campaignId) > 0) {
			return true;
		}
		
		return false;
	}

	public void batchCreateCalDetail(List<CampaignDetailVo> list)
			throws Exception {
		int index = campaignDao.batchCreateCalStatus(list);
		if(list.size() != index) {
			throw new Exception("batchCreateCalStatus fail!");
		}
	}

	public void updateCalDetail(CampaignDetailVo vo) {
		campaignDao.updateCalResult(vo);
	}
	
	public void updateCalDetailStatus(Long campaignId, int ymd, String status) {
		campaignDao.updateCalStatus(campaignId, ymd, status);
	}
	
	public void sycnMemberSegment(Long campaignId) throws Exception {
		campaignDao.sycnMemberSegment(campaignId);
	}
	
	public void deleteMemberSegment(Long campaignId) {
		campaignDao.deleteMemberSegment(campaignId);
	}

	@Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
	public boolean existMemberSegment(Long campaignId) {
		int index = campaignDao.memberSegmentCount(campaignId);
		if(index > 0) {
			return true;
		}
		return false;
	}
	
	public void updateCampaignStatus(Long[] campaignId, String status) {
		campaignDao.updateCampaignStatus(campaignId, status);
	}
	
	public static void main(String[] args) {
		
		/*String criteriaScheme = "[{\"inputId\":\"transFilm\",\"groupId\":\"tsale\",\"label\":\"观影影片\",\"groupLabel\":\"票房交易\",\"operator\":\"in\",\"value\":{\"selTarget\":true,\"criteria\":[{\"inputId\":\"filmName\",\"label\":\"影片名称\",\"operator\":\"eq\",\"value\":\"\",\"valueLabel\":\"\"},{\"inputId\":\"showSet\",\"label\":\"放映制式\",\"operator\":\"in\",\"value\":[],\"valueLabel\":\"\"},{\"inputId\":\"filmTypes\",\"label\":\"影片类型\",\"operator\":\"in\",\"value\":[],\"valueLabel\":\"\"},{\"inputId\":\"filmCate\",\"label\":\"类别\",\"operator\":\"in\",\"value\":[],\"valueLabel\":\"\"},{\"inputId\":\"country\",\"label\":\"国家\",\"operator\":\"in\",\"value\":[],\"valueLabel\":\"\"},{\"inputId\":\"director\",\"label\":\"导演\",\"operator\":\"like\",\"value\":\"\",\"valueLabel\":\"\"},{\"inputId\":\"mainActors\",\"label\":\"演员\",\"operator\":\"like\",\"value\":\"\",\"valueLabel\":\"\"}],\"selections\":{\"value\":[\"001107582012\"],\"valueLabel\":[\"101次求婚\"]}}}]";
		String cinemaScheme = "{\"compositeCinema\":{\"selTarget\":true,\"criteria\":[{\"inputId\":\"innerName\",\"label\":\"影城内部名称\",\"operator\":\"eq\",\"value\":\"\",\"valueLabel\":\"\"},{\"inputId\":\"area\",\"label\":\"区域\",\"operator\":\"in\",\"value\":[],\"valueLabel\":\"\"},{\"inputId\":\"cityLevel\",\"label\":\"城市级别\",\"operator\":\"in\",\"value\":[],\"valueLabel\":\"\"},{\"inputId\":\"cityName\",\"label\":\"城市名称\",\"operator\":\"eq\",\"value\":\"\",\"valueLabel\":\"\"}],\"selections\":{\"value\":[\"317\",\"848\"],\"valueLabel\":[\"蚌埠文化广场店\",\"蚌埠万达广场店\"]}}}";
		
		//String cinemaScheme = "{\"compositeCinema\":{\"selTarget\":false,\"criteria\":[{\"inputId\":\"innerName\",\"label\":\"影城内部名称\",\"operator\":\"eq\",\"value\":\"\",\"valueLabel\":\"\"},{\"inputId\":\"area\",\"label\":\"区域\",\"operator\":\"in\",\"value\":[\"01\"],\"valueLabel\":\"北京\"},{\"inputId\":\"cityLevel\",\"label\":\"城市级别\",\"operator\":\"in\",\"value\":[],\"valueLabel\":\"\"},{\"inputId\":\"cityName\",\"label\":\"城市名称\",\"operator\":\"eq\",\"value\":\"\",\"valueLabel\":\"\"}],\"selections\":{\"value\":[],\"valueLabel\":[]}}}";
		
		String cinemaTicket = "inputId\":\"watchTradeCinema\",\"groupId\":\"tsale\",\"label\":\"观影影城\",\"groupLabel\":\"票房交易\",\"operator\":\"in\",\"value";
		cinemaScheme = cinemaScheme.replace("compositeCinema", cinemaTicket);
		criteriaScheme = criteriaScheme.substring(0,1)+cinemaScheme+','+criteriaScheme.substring(1,criteriaScheme.length());
		
		//String s = "[{\"inputId\":\"watchTradeCinema\",\"groupId\":\"tsale\",\"label\":\"观影影城\",\"groupLabel\":\"票房交易\",\"operator\":\"in\",\"value\":{\"selTarget\":true,\"criteria\":[{\"inputId\":\"innerName\",\"label\":\"影城内部名称\",\"operator\":\"eq\",\"value\":\"\",\"valueLabel\":\"\"},{\"inputId\":\"area\",\"label\":\"区域\",\"operator\":\"in\",\"value\":[],\"valueLabel\":\"\"},{\"inputId\":\"cityLevel\",\"label\":\"城市级别\",\"operator\":\"in\",\"value\":[],\"valueLabel\":\"\"},{\"inputId\":\"cityName\",\"label\":\"城市名称\",\"operator\":\"eq\",\"value\":\"\",\"valueLabel\":\"\"}],\"selections\":{\"value\":[\"317\",\"848\"],\"valueLabel\":[\"蚌埠文化广场店\",\"蚌埠万达广场店\"]}}},{\"inputId\":\"transFilm\",\"groupId\":\"tsale\",\"label\":\"观影影片\",\"groupLabel\":\"票房交易\",\"operator\":\"in\",\"value\":{\"selTarget\":true,\"criteria\":[{\"inputId\":\"filmName\",\"label\":\"影片名称\",\"operator\":\"eq\",\"value\":\"\",\"valueLabel\":\"\"},{\"inputId\":\"showSet\",\"label\":\"放映制式\",\"operator\":\"in\",\"value\":[],\"valueLabel\":\"\"},{\"inputId\":\"filmTypes\",\"label\":\"影片类型\",\"operator\":\"in\",\"value\":[],\"valueLabel\":\"\"},{\"inputId\":\"filmCate\",\"label\":\"类别\",\"operator\":\"in\",\"value\":[],\"valueLabel\":\"\"},{\"inputId\":\"country\",\"label\":\"国家\",\"operator\":\"in\",\"value\":[],\"valueLabel\":\"\"},{\"inputId\":\"director\",\"label\":\"导演\",\"operator\":\"like\",\"value\":\"\",\"valueLabel\":\"\"},{\"inputId\":\"mainActors\",\"label\":\"演员\",\"operator\":\"like\",\"value\":\"\",\"valueLabel\":\"\"}],\"selections\":{\"value\":[\"051300012013\",\"051100012013\"],\"valueLabel\":[\"007：大破天幕杀机（数字IMAX）\",\"007：大破天幕杀机（数字）\"]}}}]";
		
		// 去除推荐响应规则的表达式
		//cinemaScheme = cinemaScheme.replace("compositeCinema", cinemaTicket);
		//criteriaScheme = "["+cinemaScheme+"]";
		
		List<ExpressionCriterion> criteria = JsonCriteriaHelper.parse(criteriaScheme);
		CriteriaResult criteriaResult = CampaignCriteriaDef.TICKET_CRITERIA_PARSER.parse(criteria);
		
		String sql = criteriaResult.getComposedText();
		sql = sql.replace("#ymd", "2013-08-25");
		sql = sql.replace("#cid", "26");
		StringBuilder sb = new StringBuilder();
		sb.append(PREFIX).append(sql).append(SUFFIX);
		
		System.out.println();
		
		//criteriaScheme = "[{\"inputId\":\"conSaleItem\",\"groupId\":\"conSale\",\"label\":\"卖品品项\",\"groupLabel\":\"卖品交易\",\"operator\":\"in\",\"value\":{\"selTarget\":true,\"criteria\":[{\"inputId\":\"itemCode\",\"label\":\"编码\",\"operator\":\"eq\",\"value\":\"\",\"valueLabel\":\"\"},{\"inputId\":\"itemName\",\"label\":\"名称\",\"operator\":\"like\",\"value\":\"\",\"valueLabel\":\"\"},{\"inputId\":\"conCategoryId\",\"label\":\"品类\",\"operator\":\"in\",\"value\":\"\",\"valueLabel\":\"\"},{\"inputId\":\"unit\",\"label\":\"计量单位\",\"operator\":\"in\",\"value\":[],\"valueLabel\":\"\"}],\"selections\":{\"value\":[\"C000000000003\"],\"valueLabel\":[\"16oz小可\"]}}}]";
		criteriaScheme = "[{\"inputId\":\"conSaleCategory\",\"groupId\":\"conSale\",\"label\":\"卖品品类\",\"groupLabel\":\"卖品交易\",\"operator\":\"in\",\"value\":[\"50\"]}]";
		String conSaleFlag = "inputId\":\"conSaleCinema\",\"groupId\":\"conSale\",\"label\":\"卖品交易影城\",\"groupLabel\":\"卖品交易\",\"operator\":\"in\",\"value";
		
		cinemaScheme = "{\"compositeCinema\":{\"selTarget\":true,\"criteria\":[{\"inputId\":\"innerName\",\"label\":\"影城内部名称\",\"operator\":\"eq\",\"value\":\"\",\"valueLabel\":\"\"},{\"inputId\":\"area\",\"label\":\"区域\",\"operator\":\"in\",\"value\":[],\"valueLabel\":\"\"},{\"inputId\":\"cityLevel\",\"label\":\"城市级别\",\"operator\":\"in\",\"value\":[],\"valueLabel\":\"\"},{\"inputId\":\"cityName\",\"label\":\"城市名称\",\"operator\":\"eq\",\"value\":\"\",\"valueLabel\":\"\"}],\"selections\":{\"value\":[\"317\",\"848\"],\"valueLabel\":[\"蚌埠文化广场店\",\"蚌埠万达广场店\"]}}}";
		cinemaScheme = cinemaScheme.replace("compositeCinema", conSaleFlag);
		criteriaScheme = criteriaScheme.substring(0,1)+cinemaScheme+','+criteriaScheme.substring(1,criteriaScheme.length());
		
		criteria = JsonCriteriaHelper.parse(criteriaScheme);
		CriteriaResult result = CampaignCriteriaDef.CONSALE_CRITERIA_PARSER.parse(criteria);
		
		sql = result.getComposedText();
		sql = sql.replace("#ymd", "2013-08-25");
		sql = sql.replace("#cid", "26");
		sb = new StringBuilder();
		sb.append(PREFIX).append(sql).append(SUFFIX);
		
		System.out.println();*/
		
		
		String criteriaScheme = "[{\"inputId\":\"transFilm\",\"groupId\":\"tsale\",\"label\":\"\u89c2\u5f71\u5f71\u7247\",\"groupLabel\":\"\u7968\u623f\u4ea4\u6613\",\"operator\":\"in\",\"value\":{\"selTarget\":true,\"criteria\":[{\"inputId\":\"filmName\",\"label\":\"\u5f71\u7247\u540d\u79f0\",\"operator\":\"like\",\"value\":\"\u54e5\u65af\u62c9\",\"valueLabel\":\"\u54e5\u65af\u62c9\"},{\"inputId\":\"showSet\",\"label\":\"\u653e\u6620\u5236\u5f0f\",\"operator\":\"in\",\"value\":[],\"valueLabel\":\"\"},{\"inputId\":\"filmTypes\",\"label\":\"\u5f71\u7247\u7c7b\u578b\",\"operator\":\"in\",\"value\":[],\"valueLabel\":\"\"},{\"inputId\":\"filmCate\",\"label\":\"\u7c7b\u522b\",\"operator\":\"in\",\"value\":[],\"valueLabel\":\"\"},{\"inputId\":\"country\",\"label\":\"\u56fd\u5bb6\",\"operator\":\"in\",\"value\":[],\"valueLabel\":\"\"},{\"inputId\":\"director\",\"label\":\"\u5bfc\u6f14\",\"operator\":\"like\",\"value\":\"\",\"valueLabel\":\"\"},{\"inputId\":\"mainActors\",\"label\":\"\u6f14\u5458\",\"operator\":\"like\",\"value\":\"\",\"valueLabel\":\"\"}],\"selections\":{\"value\":[\"051200492014\",\"051400492014\"],\"valueLabel\":[\"\u54e5\u65af\u62c9\uff08\u6570\u5b573D\uff09\",\"\u54e5\u65af\u62c9\uff08\u6570\u5b57IMAX3D\uff09\"]}}}]";
		String cinemaScheme = "{\"compositeCinema\":{\"selTarget\":true,\"criteria\":[{\"inputId\":\"innerName\",\"label\":\"\u5f71\u57ce\u5185\u90e8\u540d\u79f0\",\"operator\":\"eq\",\"value\":\"\",\"valueLabel\":\"\"},{\"inputId\":\"area\",\"label\":\"\u533a\u57df\",\"operator\":\"in\",\"value\":[],\"valueLabel\":\"\"},{\"inputId\":\"cityLevel\",\"label\":\"\u57ce\u5e02\u7ea7\u522b\",\"operator\":\"in\",\"value\":[],\"valueLabel\":\"\"},{\"inputId\":\"cityName\",\"label\":\"\u57ce\u5e02\u540d\u79f0\",\"operator\":\"eq\",\"value\":\"\",\"valueLabel\":\"\"}],\"selections\":{\"value\":[\"848\",\"317\",\"354\",\"811\",\"304\",\"333\",\"311\",\"849\",\"370\",\"890\",\"804\",\"815\",\"337\",\"353\",\"867\",\"314\",\"309\",\"325\",\"380\",\"312\",\"825\",\"885\",\"850\",\"306\",\"305\",\"348\",\"834\",\"318\",\"307\",\"831\",\"898\",\"842\",\"320\",\"851\",\"383\",\"338\",\"839\",\"852\",\"853\",\"840\",\"806\",\"854\",\"888\",\"335\",\"883\",\"882\",\"384\",\"365\",\"861\",\"363\",\"855\",\"876\",\"856\",\"862\",\"332\",\"302\",\"301\",\"329\",\"896\",\"897\",\"870\",\"366\",\"810\",\"863\",\"313\",\"355\",\"310\",\"889\",\"368\",\"847\",\"875\",\"809\",\"357\",\"877\",\"906\",\"821\",\"881\",\"900\",\"828\",\"316\",\"812\",\"902\",\"903\",\"868\",\"887\",\"886\",\"894\",\"369\",\"381\",\"905\",\"347\",\"901\",\"899\",\"817\",\"816\",\"322\",\"864\",\"819\",\"345\",\"865\",\"331\",\"893\",\"908\",\"909\",\"362\",\"327\",\"813\",\"833\",\"343\",\"328\",\"823\",\"367\",\"808\",\"371\",\"878\",\"336\",\"340\",\"356\",\"827\",\"866\",\"820\",\"342\",\"351\",\"378\",\"832\",\"803\",\"339\",\"801\",\"838\",\"802\",\"359\",\"319\",\"350\",\"872\",\"895\",\"352\",\"892\",\"349\",\"835\",\"846\",\"891\",\"373\",\"879\",\"871\",\"372\",\"321\",\"880\",\"375\",\"361\",\"324\",\"858\",\"315\",\"346\",\"341\",\"376\",\"845\",\"358\",\"907\",\"873\",\"904\",\"360\",\"843\",\"859\",\"326\",\"377\",\"884\",\"857\",\"826\",\"374\",\"824\",\"379\",\"030\",\"869\",\"874\"],\"valueLabel\":[\"\"]}}}";
		cinemaScheme = cinemaScheme.replace("compositeCinema", TICKET_FLAG);
		criteriaScheme = criteriaScheme.substring(0,1)+cinemaScheme+','+criteriaScheme.substring(1,criteriaScheme.length());
		List<ExpressionCriterion> criteria = JsonCriteriaHelper.parse(criteriaScheme);
		CriteriaResult criteriaResult = CampaignCriteriaDef.TICKET_CRITERIA_PARSER.parse(criteria);
		String sql = criteriaResult.getComposedText();
		sql = sql.replace("#ymd", "2013-08-25");
		sql = sql.replace("#cid", "66");
		StringBuilder sb = new StringBuilder();
		sb.append(PREFIX).append(sql).append(SUFFIX);
		System.out.println(sb.toString());
		System.out.println();
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
	public List<CampaignVo> queryCampaignByBefore(String date) {
		return campaignDao.queryIncompleteCampaignByBefore(date);
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
	public List<CampaignDetailVo> queryCampaignDetailById(Long campaignId) {
		List<CampaignDetailVo> newList = null;
		
		Map<Integer, CampaignDetailVo> map = campaignDao.queryCampaignDetailByTime(campaignId);
		if(map != null && map.size() > 0) {
			newList = new ArrayList<CampaignDetailVo>();
			CampaignVo cvo = new CampaignVo();
			Integer startDate = 0;
			Integer endDate = 0;
			
			for(Map.Entry<Integer, CampaignDetailVo> entry : map.entrySet()) {
				CampaignDetailVo vo = entry.getValue();
				String[] dates = new SimpleDateFormat("yyyy-MM-dd").format(vo.getStartDate()).split("-");
				startDate = Integer.parseInt(dates[0]+""+dates[1]+""+dates[2]);
				dates = new SimpleDateFormat("yyyy-MM-dd").format(vo.getEndDate()).split("-");
				endDate = Integer.parseInt(dates[0]+""+dates[1]+""+dates[2]);
				
				cvo.setCampaignId(campaignId);
				cvo.setStartDate(vo.getStartDate());
				cvo.setEndDate(vo.getEndDate());
				cvo.setCalCount(vo.getCalCount());
				cvo.setControlCount(vo.getControlCount());
				cvo.setRecommendCount(vo.getRecommendCount());
				cvo.setCinemaScheme(vo.getCinemaScheme());
				cvo.setCriteriaScheme(vo.getCriteriaScheme());
				cvo.setCampaignStatus(vo.getCampaignStatus());
				vo = null;
				
				for(int i=startDate; i<=endDate; i++) {
					vo = map.get(i);
					if(vo != null) { //已经存在明细数据
						newList.add(vo);
					} else { //尚未创建明细数据
						vo = new CampaignDetailVo(cvo);
						vo.setYmd(i);
						vo.setCinemaScheme(cvo.getCinemaScheme());
						vo.setCriteriaScheme(cvo.getCriteriaScheme());
						
						newList.add(vo);
					}
					vo = null;
				}
				
				break;
			}
		}
		
		return newList;
	}

	@Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
	public CampaignVo queryCampaignById(Long campaignId) {
		return campaignDao.queryCampaignById(campaignId);
	}

	public void clearSyncCalMemeber(Long campaignId) {
		campaignDao.cleanSyncCalMember(campaignId);
	}

	public void deleteCampaignDetail(Long campaignId) {
		campaignDao.deleteCampaignDetail(campaignId);
	}

}
