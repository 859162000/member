package com.wanda.member.upgrade.service;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.solar.etl.config.mapping.FieldSet;
import com.solar.etl.spi.LineHandle;
import com.wanda.member.upgrade.data.TMemberLevel;
import com.wanda.member.upgrade.exception.UnSupportLevelException;
import com.wanda.mms.dao.MyBatisDAO;
@Service
@Scope("prototype")
public class MemberUpgradeHandler extends MyBatisDAO implements LineHandle  {
	Log logger = LogFactory.getLog(MemberUpgradeHandler.class);
	@Resource
	MemberLevelService memberLevelService;
	@Resource(name="memberUpgradeStrategyUp2Two")
	MemberUpgradeStrategy one2TwoUpgrade = null;
	@Resource(name="memberUpgradeStrategyUp2Three")
	MemberUpgradeStrategy two2ThreeUpgrade = null;
	@Resource(name="memberUpgradeStratreyUp2Four")
	MemberUpgradeStrategy two2FourUpgrade = null;
	@Override
	public int handle(FieldSet fieldSet) {
		logger.info("is handling member id = "+fieldSet.getFieldByName("MEMBER_ID").destValue);
		// 1 --------------- 查看会员级别		
		BigDecimal memberId = new BigDecimal(fieldSet.getFieldByName("MEMBER_ID").destValue);
		String year = fieldSet.getFieldByName("UPDATE_DATE").destValue.substring(0, 4);
		return handleEachMember(memberId, year);
	}

	public int handleEachMember(BigDecimal memberId, String year) {
		logger.info("begin handle member "+memberId+" year is"+year);
		TMemberLevel memberLevel = memberLevelService.getMemberLevel(memberId);
		if(memberLevel == null){
			logger.error("member level is null");
			return 0;
		}
		logger.info(" member level is "+memberLevel.getMemLevel());
	    // 2 ---------------查看积分历史
		// where set_time >= to_date(?, 'yyyy-mm-dd')  and   
		//set_time  < to_date(?, 'yyyy-mm-dd') + 1  and MEMBER_ID = ? 
		//and IS_HISTORY<>'1' group by MEMBER_ID	"  ;
		
		// 3 ---------------生成序列
		//		select S_T_LEVEL_HISTORY.nextVal SEQ from dual
		// 4 进行级别计算
		MemberUpgradeStrategy strategy = null;
		try {
			strategy = getMemberUpgradeStrategy(memberLevel.getMemLevel());
			
			if(strategy == null){
				return 0;
			}else{
				logger.info("strategy is " + strategy.getClass().getName());
				strategy.compute(year,memberLevel);
				return 0;
			}
		} catch (UnSupportLevelException e) {
			e.printStackTrace();
			return 0;
		}
	}

	private MemberUpgradeStrategy getMemberUpgradeStrategy(String memLevel) throws UnSupportLevelException {
		if("1".equals(memLevel)){
			return one2TwoUpgrade;
		}else if("2".equals(memLevel)){
			return two2ThreeUpgrade;
		}else if("3".equals(memLevel)){
			return two2FourUpgrade;
		}else if("4".equals(memLevel)){
			return null;
		}else{
			throw new UnSupportLevelException(memLevel);
		}
	}
	
	@Override
	public void commit() {
		
	}

	@Override
	public void rollback() {
		// TODO Auto-generated method stub
		
	}
}
