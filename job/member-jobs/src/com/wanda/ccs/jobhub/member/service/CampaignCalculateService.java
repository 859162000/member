package com.wanda.ccs.jobhub.member.service;

import java.util.List;

import com.wanda.ccs.jobhub.member.job.CampaignCalculateJob.CalculateType;
import com.wanda.ccs.jobhub.member.vo.CampaignDetailVo;
import com.wanda.ccs.jobhub.member.vo.CampaignVo;

public interface CampaignCalculateService {
	
	// 同步会员落地
	public void sycnMemberSegment(Long campaignId)  throws Exception;
	
	// 清除客群同步数据
	public void deleteMemberSegment(Long campaignId);
	
	// 客群数据是否存在
	public boolean existMemberSegment(Long campaignId);
	
	// 统计票类收入（推荐+对比组）
	public void calTicketIncome(CampaignDetailVo vo);
	
	// 统计卖品收入（推荐+对比组）
	public void calConSaleIncome(CampaignDetailVo vo);
	
	// 统计卖品总收入
	public void calSumIncome(CampaignDetailVo vo);
	
	// 无推荐规则收入统计
	public void calNonCriteriaIncome(CampaignDetailVo vo);
	
	// 汇总
	public CampaignDetailVo queryCalDetailTotal(Long campaignId, CalculateType calType);
	
	// 获取当天要统计的活动
	public List<CampaignVo> queryCampaign(String date, String[] status);
	
	// 更新活动状态
	public void updateCampaignStatus(Long[] campaignId, String status);
	
	// 查询活动计算状态
	public List<CampaignDetailVo> queryCalDetailList(Integer ymd, Long campaignId, String type, String[] status);
	
	// 更新计算状态
	public void updateCalDetailStatus(Long campaignId, int ymd, String status);
	
	// 生成计算状态
	public void batchCreateCalDetail(List<CampaignDetailVo> list) throws Exception;
	
	// 保存活动统计结果同时更新计算状态为成功
	public void updateCalDetail(CampaignDetailVo vo);
	
	// 存在统计总量
	public boolean existCalDetailTotal(Long campaignId);
	
	// 查询之前的活动
	public List<CampaignVo> queryCampaignByBefore(String date); 
	
	// 查询活动对应的明细记录
	public List<CampaignDetailVo> queryCampaignDetailById(Long campaignId);
	
	public CampaignVo queryCampaignById(Long campaignId);
	
	public void clearSyncCalMemeber(Long campaignId);
	
	public void deleteCampaignDetail(Long campaignId);
	
}