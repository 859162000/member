package com.wanda.ccs.jobhub.member.service;

public interface MemberNextLevelService {
	
	/**
	 * 清除会员升级统计临时数据
	 */
	public void clearMemberNextLevel();
	
	/**
	 * 统计会员下一级别升级所需条件
	 * 
	 * @param yyyy
	 */
	public void calculateMemberNextLevel(String yyyy);
	
	/**
	 * 根据统计数据更新t_member_levle表中的字段
	 */
	public void updateMemberNextLevel();
	
	/**
	 * 是否存在临时数据
	 */
	public boolean existTempDate();
	
}