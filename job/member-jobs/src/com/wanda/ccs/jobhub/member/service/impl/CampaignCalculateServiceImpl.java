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
