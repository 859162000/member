package com.wanda.member.validate;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.wanda.member.countermgt.data.TMemberRepair;
import com.wanda.member.countermgt.data.TMemberRepairMapper;
import com.wanda.member.upgrade.data.TPointHistory;
import com.wanda.member.upgrade.data.TPointHistoryExample;
import com.wanda.member.upgrade.data.TPointHistoryMapper;
import com.wanda.mms.dao.MyBatisDAO;
@Service
@Scope("prototype")
public class ValidatePointHis extends MyBatisDAO {
	
	final static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	public int validate(String memberid){
		try {
			TPointHistoryMapper mapper = sqlSession.getMapper(TPointHistoryMapper.class);
			TMemberRepairMapper repairedMapper = sqlSession.getMapper(TMemberRepairMapper.class);
			TPointHistoryExample example = new TPointHistoryExample();
			
			example.createCriteria().andMemberIdEqualTo(new BigDecimal(memberid))
			.andSetTimeGreaterThan(format.parse("2013-08-09")).andIsdeleteEqualTo(new BigDecimal(0)).andIsHistoryEqualTo("0");
		
			List<TPointHistory> list = mapper.selectByExample(example );
			Collections.sort(list);
			
			TMemberRepair record = checkList(list);
			if(record == null){
				return 1;
			}
			repairedMapper.insert(record);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		return 0;
	}
	private TMemberRepair checkList(List<TPointHistory> list) {
		TMemberRepair repair = null;
		TPointHistory privous = null;
		TPointHistory current = null;
		for(int i =0;i<list.size();i++){
			if(i==0){
				continue;
			}
			if(i!=0){
				privous = list.get(i-1);
			}
			current = list.get(i);
			if(!current.getOrgPointBalance().equals(privous.getPointBalance()) ){
				System.out.println(privous.getPointHistoryId()+"-"+privous.getMemberId()+":"+current.getPointHistoryId()+"-"+current.getMemberId()+"not equal:"+current.getOrgPointBalance()+":"+privous.getPointBalance());
				repair = new TMemberRepair();
				repair.setMemberId(privous.getMemberId());
				repair.setIsRepaired((short)-1);
				repair.setMemberNo(privous.getPointHistoryId()+"-"+current.getPointHistoryId());
			}
		}
		return repair;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
