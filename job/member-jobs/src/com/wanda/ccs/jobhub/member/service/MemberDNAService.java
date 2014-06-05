package com.wanda.ccs.jobhub.member.service;

public interface MemberDNAService {
	
	/**
	 * 清除所有DNA临时数据
	 */
	public void cleanAllDNA() throws Exception ;
	
	/**
	 * 计算黄牛会员
	 */
	public void calculateMemberByHuangNiu() throws Exception ;
	
	/**
	 * 时间段内会员观影信息
	 * 
	 * @param date
	 */
	public void calculateMemberTicket(String date) throws Exception ;
	
	/**
	 * 计算会员行为基本信息
	 * 
	 * @param date
	 */
	public void calculateMemberBehaviorBase(String date) throws Exception ;
	
	/**
	 * 计算会员行为index
	 * 
	 * @param date
	 */
	public void calculateMemberBehaviorIndex() throws Exception ;
	
	/**
	 * 计算会员行为标准
	 * 
	 * @param date
	 */
	public void calculateMemberBehaviorStand() throws Exception ;
	
	/**
	 * 
	 * 
	 * @param date
	 */
	public void updateMemberBehaviorStand() throws Exception ;
	
	/**
	 * 
	 * 
	 * @param date
	 */
	public void calculateMemberBehaviorDistance() throws Exception ;
	
	/**
	 * 计算会员行为
	 * 
	 * @param date
	 */
	public void calculateMemberBehaviorSegment(String date) throws Exception ;
	
}