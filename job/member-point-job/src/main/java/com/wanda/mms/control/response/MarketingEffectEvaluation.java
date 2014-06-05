package com.wanda.mms.control.response;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.solar.etl.config.mapping.Field;
import com.solar.etl.config.mapping.FieldSet;
import com.solar.etl.spi.LineHandle;
import com.wanda.mms.dao.MyBatisDAO;
import com.wanda.mms.data.TActResult;
import com.wanda.mms.data.TCmnActivity;
import com.wanda.mms.data.TCmnActivityMapper;
@Service("marketingEffectEvaluation")
public class MarketingEffectEvaluation extends MyBatisDAO implements LineHandle {
	Log logger = LogFactory.getLog(MarketingEffectEvaluation.class);
	private static final String FINISHED = "20";
	private static final String PROCESSING = "10";
	public TActResult  handleResult = null;
	/*
	 * 营销效果评估 
	 *  
	 */
	@Override
	public int handle(FieldSet fieldSet) {
		MarketingEffectMapper mapper = sqlSession.getMapper(MarketingEffectMapper.class);
		TActResult result = getActResult(fieldSet,mapper);
		if(isProcessing(fieldSet.getFieldByName("CMN_ACTIVITY_ID"))){
			result.setStatus(PROCESSING);
		}else{
			result.setStatus(FINISHED);
//			result.setStatus(PROCESSING);
		}	
		result.setUpdateDate(new Date());
//		mapper.updateResult(result);
		handleResult = result;
		return 0;
	}

	/**
	 * 开始统计计算时把状态设置为‘统计中’，如当前时间大于活动波次结束时间把状态设置为‘统计结束’否则设置为‘待统计’。
	 * @param field 
	 * @return
	 */
	private boolean isProcessing(Field field) {	
		TCmnActivityMapper mapper = sqlSession.getMapper(TCmnActivityMapper.class);
		TCmnActivity re = mapper.selectByPrimaryKey(new BigDecimal(field.destValue));
		if(re == null){
			logger.error("error-0001:无法获取活动信息");
			return true;
		}
		Date dt = new Date();
		dt.setDate(dt.getDate()-2);
		if(re.getEndDtime().after(dt)){
			return true;
		}else{
			return false;
		}
	}


	public TActResult getActResult(FieldSet fieldSet, MarketingEffectMapper mapper) {
		Field mfield=fieldSet.getFieldByName("ACT_RESULT_ID");
		Field mFiledActivity = fieldSet.getFieldByName("CMN_ACTIVITY_ID");
		Field resSegmentFiled = fieldSet.getFieldByName("RES_SEGMENT_ID");
		String actResultId = mfield.destValue; 
		String cmnActivityid = mFiledActivity.destValue;
		BigDecimal actResult = new BigDecimal(actResultId);
		BigDecimal cmnActivity = new BigDecimal(cmnActivityid);
		BigDecimal resSegment = new BigDecimal(resSegmentFiled.destValue);
		
		TActResult result = new TActResult();
		result.setActResultId(actResult);
		
		result.setResCount( mapper.getRelResponseCount(actResult));//推荐响应人数  **
		
		result.setAlterResCount(mapper.getAlterResponseCount(actResult));//关联响应人数 **
		result.setControlCount(mapper.getControlcount(cmnActivity));// 控制组人数 
		result.setControlResCount(mapper.getControlResCount(cmnActivity,resSegment));//控制组响应人数
		result.setContactCount(mapper.getContractConunt(cmnActivity));//联络人数（发送人数）
		return result;
	}
	

	@Override
	public void commit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void rollback() {
		// TODO Auto-generated method stub

	}

}
