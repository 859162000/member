package com.wanda.member.countermgt.service.rule;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.wanda.member.countermgt.data.TCinemaTicket;
import com.wanda.member.countermgt.service.Condition;
import com.wanda.member.countermgt.service.CounterComputeRule;
import com.wanda.member.countermgt.service.RuleCondition;
import com.wanda.member.exception.NullCinemaException;
import com.wanda.member.exception.NullMemberIdException;
import com.wanda.member.upgrade.data.TMember;

@Service("rule1")
@Scope("prototype")
public class Rule1 extends AbstractCounterRule {
	@Resource(name = "rule2")
	protected CounterComputeRule rule2 = null;
	private List<TCinemaTicket> list = null;
	@Resource(name = "nullCounterRule")
	protected CounterComputeRule nullCounterRule = null;
	@Resource(name = "setCounterRule")
	protected CounterComputeRule setCounterRule = null;

	@Override
	protected void handleNull(TMember memeber) {
		currentRule = nullCounterRule;
	}

	@Override
	protected void handleUnique(TMember memeber) throws Exception {
//		List<TCinemaTicket> list = counterComputeServcie.getTicketCountCatalogByClima(memeber.getMemberKey());
		if (list != null && !list.isEmpty()) {
			if(list.get(0).getCinemaKey() == null)
				throw new NullCinemaException();
			memeber.setRegistCinemaId(new BigDecimal(list.get(0).getCinemaKey()));
		}else{
			loger.error("memeber:"+memeber.getMemberNo()+" have not TicketInfo");
			throw new Exception();
		}
		currentRule = setCounterRule;
	}

	@Override
	protected void handleMutiple(TMember memeber) {
		currentRule = rule2;
	}

	@Override
	protected Condition getCondition(TMember memeber) throws Exception {
		loger.debug(memeber.getMemberKey());
		list = counterComputeServcie.getTicketCountCatalogByClima(memeber.getMemberKey());
		loger.debug(list);
		return genCondition(list);
	}

	@Override
	public String getRuleId() {
	
		return "1";
	}
	
}
