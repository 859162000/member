package com.wanda.ccs.mbr.tag.service;
/**
 * 
 * @author lining
 *
 */
public interface TagBuildService  {
	/**
	 * 价格敏感标签数据操作
	 * @throws Exception
	 */
	public void buildPriceSensitive() throws Exception;
	/**
	 * 家庭构成数据操作
	 * @throws Exception
	 */
	public void buildFamilyComposition() throws Exception;
	/**
	 * 异常消费数据操作
	 * @throws Exception
	 */
	public void buildAbnormalConsumption() throws Exception;
	/**
	 * 会员活跃度数据操作
	 * @throws Exception
	 */
	public void buildMembersAcitveRate() throws Exception;
	/**
	 * 电子渠道偏好（'20'线下，'10'线上）数据操作
	 * @throws Exception
	 */
	public void buildEchannelPreference() throws Exception;
	/**
	 * 6影片偏好数据操作
	 * @throws Exception
	 */
	public void buildFilmPreferences() throws Exception;
}
