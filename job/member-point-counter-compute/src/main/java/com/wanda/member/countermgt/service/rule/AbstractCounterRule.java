package com.wanda.member.countermgt.service.rule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.wanda.member.countermgt.data.RuleChain;
import com.wanda.member.countermgt.data.TCinemaTicket;
import com.wanda.member.countermgt.data.TDConMember;
import com.wanda.member.countermgt.data.TDConMemberExample;
import com.wanda.member.countermgt.data.TDConMemberMapper;
import com.wanda.member.countermgt.exception.UnRuleException;
import com.wanda.member.countermgt.service.Condition;
import com.wanda.member.countermgt.service.CounterComputeRule;
import com.wanda.member.countermgt.service.CounterComputeServcie;
import com.wanda.member.countermgt.service.RuleCondition;
import com.wanda.member.exception.NullMemberIdException;
import com.wanda.member.exception.NullMemberInfoException;
import com.wanda.member.upgrade.data.TMember;
import com.wanda.mms.dao.MyBatisDAO;
@Service
@Scope("prototype")
public abstract class AbstractCounterRule extends MyBatisDAO implements CounterComputeRule {
	protected Log loger = LogFactory.getLog(AbstractCounterRule.class);
	@Resource
	protected CounterComputeServcie counterComputeServcie = null;
	
	protected CounterComputeRule currentRule = null;
	
	protected abstract void handleMutiple(TMember memeber) ;

	protected abstract void handleNull(TMember memeber) throws Exception ;

	protected abstract void handleUnique(TMember member) throws Exception ;
	@Override
	public void compute(TMember memeber,RuleChain chain) throws Exception {
		chain.add(this);
		if(memeber.getMemberNo() == null){
			throw new  NullMemberIdException();
		}
		//获取 member_key
	
		List<TDConMember> memberList = counterComputeServcie.getDConMembers(memeber.getMemberNo());
				
		if(memberList == null||memberList.isEmpty()){
			loger.error("member no:"+memeber.getMemberNo()+" have not member info");
			throw new NullMemberInfoException(); 
		}
		for(TDConMember dconmember:memberList){
			memeber.setMemberKey(dconmember.getMemberKey());
			Condition condition = getCondition(memeber);
			if(condition.isUnique()){
				handleUnique(memeber);
				break;
			}else if(condition.isNull()) {
				handleNull(memeber);
			}else {
				handleMutiple(memeber);
				break;
			}
		}
		if(currentRule == null){
			throw new UnRuleException(this.getClass().getName());
		}
		currentRule.compute(memeber,chain);
	}

	
	protected abstract Condition getCondition(TMember memeber) throws Exception ;
	protected final RuleCondition genCondition(TCinemaTicket re) {
		RuleCondition condition = new RuleCondition();
		if(re == null){
			condition.setNull(true);
		}else{
			condition.setUnique(true);
		}
		return condition;
	}
	protected final RuleCondition genCondition(List<TCinemaTicket> list) {
		RuleCondition condition = new RuleCondition();
		if(list == null ||list.isEmpty()){
			condition.setNull(true);
		}else if(list.size()==1){
			condition.setUnique(true);
		}else{
			int count = getMaxCount(list);
			if(count>1){
				condition.setMultiple(true);
			}else{
				condition.setUnique(true);
			}			
		}
		return condition;
	}
	public static  int getMaxCount(List<TCinemaTicket> list) {
		if(list == null ||list.isEmpty()){
			return 0;
		}
		if(list.size()==1){
			return 1;
		}
		Map<String,Integer> map = new HashMap<String,Integer>();
		for(TCinemaTicket ticket:list){
			if(map.containsKey(ticket.getCinemaKey())){
				Integer count = map.get(ticket.getCinemaKey()).intValue()+ticket.getTicketNum();
				map.put(ticket.getCinemaKey(), count);
			}else{
				map.put(ticket.getCinemaKey(), ticket.getTicketNum());
			}		
		}
		return getMaxCinma(map);
	
	}

	private static int getMaxCinma(Map<String, Integer> map) {
		if(map == null||map.isEmpty()){
			return 0;
		}
		Map<Integer,Integer> revMap = new HashMap<Integer,Integer>();
		Set<String> set = map.keySet();
		int max = 0;
		for(String cinma:set){
			if(map.get(cinma).intValue()>max){
				max = map.get(cinma).intValue();
			}
			if(revMap.containsKey(map.get(cinma))){
				int cnt = revMap.get(map.get(cinma)).intValue()+1;
				revMap.put(map.get(cinma), cnt);
			}else{
				revMap.put(map.get(cinma), 1);
			}
		}
		return revMap.get(max).intValue();
	}

	public CounterComputeRule getCurrentRule() {
		return currentRule;
	}
}
