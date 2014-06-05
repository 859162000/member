package com.wanda.ccs.jobhub.member.job;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.wanda.ccs.jobhub.member.service.CampaignCalculateService;
import com.wanda.ccs.jobhub.member.utils.CalculateThreadPool;
import com.wanda.ccs.jobhub.member.vo.CampaignDetailVo;
import com.wanda.ccs.jobhub.member.vo.CampaignVo;
import com.wanda.ccs.member.segment.defimpl.CampaignCriteriaDef;

/**
 * 活动计算
 * 
 * @author ZR
 * @date 2014年4月18日 下午6:09:22
 */
public class CampaignCalculateJob implements Job {
	
	@InstanceIn(path = "CampaignCalculateService")
	private CampaignCalculateService campaignCalculateService;
	// 总处理个数
	public static final Integer MAX_AVAILABLE = 1;	
	
	public List<CampaignDetailVo> queryCampaignDetailById(Long campaignId) throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		String date = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
		String[] dates = date.split("-");
		int ymd = Integer.parseInt(dates[0]+""+dates[1]+""+dates[2]);
		
		List<CampaignDetailVo> newList = new ArrayList<CampaignDetailVo>();
		CampaignVo vo = campaignCalculateService.queryCampaignById(campaignId);
		if(vo == null) {
			throw new JobExecutionException("没有找到指定的营销活动");
		}
		
		campaignCalculateService.updateCampaignStatus(new Long[] {campaignId}, CampaignVo.STATUS_EXECUTION);
		vo.setCampaignStatus(CampaignVo.STATUS_EXECUTION);
		
		campaignCalculateService.deleteCampaignDetail(vo.getCampaignId());
		String[] startTime = new SimpleDateFormat("yyyy-MM-dd").format(vo.getStartDate()).split("-");
		String[] endTime = new SimpleDateFormat("yyyy-MM-dd").format(vo.getEndDate()).split("-");
		int start = Integer.parseInt(startTime[0]+""+startTime[1]+""+startTime[2]);
		int end = Integer.parseInt(endTime[0]+""+endTime[1]+""+endTime[2]);
		
		CampaignDetailVo cvo = null;
		for(int j=start; j<=end; j++) {
			if(j > ymd) {
				break;
			}
			cvo = new CampaignDetailVo(vo);
			cvo.setYmd(j);
			cvo.setCinemaScheme(vo.getCinemaScheme());
			cvo.setCriteriaScheme(vo.getCriteriaScheme());
			newList.add(cvo);
			cvo = null;
		}
		
		if(newList.size() > 0) {
			campaignCalculateService.batchCreateCalDetail(newList);
		}
		
		return newList;
	}
	
	public List<CampaignDetailVo> queryBeforeCampaignDetail(String date) throws Exception {
		List<CampaignDetailVo> oldList = new ArrayList<CampaignDetailVo>();
		// 获取
		List<CampaignVo> campaignList = campaignCalculateService.queryCampaignByBefore(date);
		if(campaignList != null && campaignList.size() > 0) {
			CampaignVo vo = null;
			List<CampaignDetailVo> list = null;
			for(int i=0,len=campaignList.size();i<len;i++) {
				vo = campaignList.get(i);
				list = queryCampaignDetailById(vo.getCampaignId());
				if(list != null && list.size() > 0) {
					oldList.addAll(list);
					list = null;
				}
			}
		}
		
		return oldList;
	}
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		List<CampaignDetailVo> detailList = new ArrayList<CampaignDetailVo>();
		Long startTime = System.currentTimeMillis();
		
		JobDataMap jobDataMap = context.getTrigger().getJobDataMap();
		// 按活动重新跑
		String id = jobDataMap.get("id") == null ? null : jobDataMap.get("id").toString();
		if(id != null && !"".equals(id)) {
			try {
				List<CampaignDetailVo> list = queryCampaignDetailById(Long.parseLong(id));
				if(list != null && list.size() > 0) {
					detailList.addAll(list);
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new JobExecutionException(e);
			}
		} else {
			// 按日期重新跑
			String date = jobDataMap.get("date") == null ? null : jobDataMap.get("date").toString();
			if(date == null || "".equals(date)) {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DAY_OF_MONTH, -1);
				date = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
			}
			System.out.println("==============="+date+"=====================");
			String[] dates = date.split("-");
			int ymd = Integer.parseInt(dates[0]+""+dates[1]+""+dates[2]);
			if(jobDataMap.get("date") != null) { // 需要重新跑某天的数据
				// 查询是否已生成明细数据
				if(jobDataMap.get("status") != null && "fail".equals(jobDataMap.get("status"))) { // 只重新统计失败的
					detailList = campaignCalculateService.queryCalDetailList(ymd, null, "0", new String[] { CampaignDetailVo.STATUS_CREATE, CampaignDetailVo.STATUS_FAIL });
				} else { // 全部重新统计
					detailList = campaignCalculateService.queryCalDetailList(ymd, null, "0", null);
				}
			}
			
			if(detailList == null || detailList.isEmpty()) { // 重新统计
				// 获取的当天要统计的任务
				List<CampaignVo> campaignList = campaignCalculateService.queryCampaign(date, new String[] { CampaignVo.STATUS_COMMIT, CampaignVo.STATUS_EXECUTION });
	//			if(campaignList == null || campaignList.isEmpty()) {
	//				throw new JobExecutionException(date+" 没有要统计的活动！");
	//			}
				if(campaignList != null && campaignList.size() > 0) {
					// 生成日志文件
					CampaignDetailVo vo = null;
					CampaignVo cvo = null;
					List<Long> campaignIds = new ArrayList<Long>();
					for(int i=0,len=campaignList.size();i<len;i++) {
						cvo = campaignList.get(i);
						if(CampaignVo.STATUS_COMMIT.equals(cvo.getCampaignStatus())) {
							campaignIds.add(cvo.getCampaignId());
						}
						vo = new CampaignDetailVo(cvo);
						vo.setYmd(ymd); //设置统计日期
						detailList.add(vo);
						vo = null;
						cvo = null;
					}
					
					try {
						if(campaignIds != null && !campaignIds.isEmpty()) {
							campaignCalculateService.updateCampaignStatus(campaignIds.toArray(new Long[] {}), CampaignVo.STATUS_EXECUTION);
						}
						campaignCalculateService.batchCreateCalDetail(detailList);
					} catch (Exception e) {
						e.printStackTrace();
						throw new JobExecutionException(e);
					}
				}
			}
			
			try {
				List<CampaignDetailVo> beforeList =  queryBeforeCampaignDetail(date);
				if(beforeList != null && beforeList.size() > 0) {
					detailList.addAll(beforeList);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
				throw new JobExecutionException(e1);
			}
		
		}
		//Map<Long, CalTask> calTaskMap = new HashMap<Long, CalTask>();
		// 同步资源
		Semaphore available = new Semaphore(MAX_AVAILABLE, true);
		// 开始循环统计
		CalculateThreadPool.getInstance().init();
		try {
			for(int i=0,len=detailList.size();i<len;i++) {
				System.out.println(detailList.get(i).getYmd()+"======================");
				available.acquire();
				// 使用信号灯去控制
				//calTaskMap.put(detailList.get(i).getCampaignId(), new CalTask(detailList.get(i), available));
				//Thread thread = new Thread(calTaskMap.get(detailList.get(i).getCampaignId()));
				//Thread thread = new Thread(new CalTask(detailList.get(i), available));
				//thread.start();
				new CalTask(detailList.get(i), available).run();
			}
			// 等待所有资源释放后清理线程池资源
			available.acquire(MAX_AVAILABLE);
			System.out.println("统计结束，开始释放线程池资源");
			context.setResult("统计正常结束，"+getTime(startTime, System.currentTimeMillis()));
		} catch (InterruptedException e) {
			e.printStackTrace();
			context.setResult("统计失败！！！！！！！");
		} finally {
			CalculateThreadPool.getInstance().destory();
		}
	}
	
	private String getTime(Long startTime, Long endTime) {
		long between = (endTime - startTime) / 1000;//除以1000是为了转换成秒
		between = 1 * 60 + 1 * 3600 + 40;
		long hour = between % (24 * 3600) /3600;
		long minute = between % 3600 / 60;
		long second = between % 60;
		
		return "用时"+hour+"小时"+minute+"分"+second+"秒";
	}
	
	private String getRate(Integer one, Integer two) {
		double t = 0;
		if(two > 0) {
			t = Double.parseDouble(one.toString()) / Double.parseDouble(two.toString());
		}
        DecimalFormat df = new DecimalFormat("#.####");
        return df.format(t);
    }
	
	/**
	 * 统计任务
	 * 
	 * @author ZR
	 *
	 */
	public class CalTask implements Runnable {
		private CampaignDetailVo vo = null;
		private Semaphore available = null;
		private List<Future<CalLog>> subTask = new ArrayList<Future<CalLog>>();
		
		public CalTask(CampaignDetailVo vo, Semaphore available) {
			this.vo = vo;
			this.available = available;
		}
		
		public void run() {
			try {
				// 是否为第一次统计数据
				System.out.println("start-------------------->"+vo.getCampaignStatus()+"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
				if(CampaignVo.STATUS_COMMIT.equals(vo.getCampaignStatus())) { //说明是第一次统计
					//campaignCalculateService.sycnMemberSegment(vo.getCampaignId());
					vo.setCampaignStatus(CampaignVo.STATUS_EXECUTION); //更改当前活动状态为执行
				} else if(CampaignVo.STATUS_EXECUTION.equals(vo.getCampaignStatus())) {
					
				} else if(CampaignVo.STATUS_COMPLETE.equals(vo.getCampaignStatus())) { // 可能是需要重新统计的情况
					//campaignCalculateService.sycnMemberSegment(vo.getCampaignId());
				}
				if(campaignCalculateService.existCalDetailTotal(vo.getCampaignId()) == false) {
					campaignCalculateService.sycnMemberSegment(vo.getCampaignId());
				}
				
				// 开始统计
				if(vo.getCriteriaScheme() == null || "".equals(vo.getCriteriaScheme())) { //按会员统计收入
					// 统计总数
					Future<CalLog> future = CalculateThreadPool.getInstance().submit(new SubTask(vo, CalculateType.NON_CRITERIA_CALCULATE));
					subTask.add(future);
					
					CalLog log = future.get();
				} else {
					// 统计推荐+对比组
					Future<CalLog> one = CalculateThreadPool.getInstance().submit(new SubTask(vo, CalculateType.CALCULATE));
					subTask.add(one);
					// 统计总数
					Future<CalLog> two = CalculateThreadPool.getInstance().submit(new SubTask(vo, CalculateType.SUM_CALCULATE));
					subTask.add(two);
					
					CalLog Olog = one.get();
					CalLog Tlog =two.get();
				}
				// 保存统计结果
				campaignCalculateService.updateCalDetail(vo);
				
				// 汇总统计 获取统计记录
				CampaignDetailVo totalVo = null;
				if(vo.getCriteriaScheme() == null || "".equals(vo.getCriteriaScheme())) {
					totalVo = campaignCalculateService.queryCalDetailTotal(vo.getCampaignId(), CalculateType.NON_CRITERIA_CALCULATE);
				} else {
					totalVo = campaignCalculateService.queryCalDetailTotal(vo.getCampaignId(), CalculateType.SUM_CALCULATE);
				}
				totalVo.setRecommendRate(Double.valueOf(getRate(totalVo.getRecommendNum(), vo.getRecommendCount())));
				totalVo.setControlRate(Double.valueOf(getRate(totalVo.getControlNum(), vo.getControlCount())));
				totalVo.setSumRate(Double.valueOf(getRate(totalVo.getSumNum(), vo.getRecommendCount())));
				totalVo.setYmd(0);
				totalVo.setType("1");
				if(!campaignCalculateService.existCalDetailTotal(vo.getCampaignId())) { 
					// 生成统计明细
					List<CampaignDetailVo> t = new ArrayList<CampaignDetailVo>();
					t.add(new CampaignDetailVo(vo.getCampaignId(), 0, "1"));
					campaignCalculateService.batchCreateCalDetail(t);
				}
				// 更新汇总数据
				campaignCalculateService.updateCalDetail(totalVo);
				
				// 活动结束更新活动状态
				if(CampaignVo.STATUS_EXECUTION.equals(vo.getCampaignStatus()) && Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(vo.getEndDate())) == vo.getYmd()) {
					campaignCalculateService.updateCampaignStatus(new Long[] { vo.getCampaignId() }, CampaignVo.STATUS_COMPLETE);
					vo.setCampaignStatus(CampaignVo.STATUS_COMPLETE);
					System.out.println("update 40");
				}
				
				if(CampaignVo.STATUS_COMPLETE.equals(vo.getCampaignStatus())) { //如果是执行完成删除落地客群
					System.out.println("delete segment");
					campaignCalculateService.deleteMemberSegment(vo.getCampaignId());
					System.out.println("delete cal member");
					campaignCalculateService.clearSyncCalMemeber(vo.getCampaignId());
				}
			} catch (Exception e) {
				e.printStackTrace();
				campaignCalculateService.updateCalDetailStatus(vo.getCampaignId(), vo.getYmd(), CampaignDetailVo.STATUS_FAIL);
				Thread.currentThread().interrupt();
			} finally {
				// 停止子任务
				for(int i=0,len=subTask.size();i<len;i++) {
					subTask.get(i).cancel(true);
				}
				
				// 释放同步资源
				this.available.release();
			}
		}
	}
	
	/**
	 * 统计子任务
	 * 
	 * @author ZR
	 * @date 2014年4月16日 下午4:29:20
	 */
	public class SubTask implements Callable<CalLog> {
		private CampaignDetailVo vo = null;
		private CalculateType OperaType = null;
		
		public SubTask(CampaignDetailVo vo, CalculateType OperaType) {
			this.vo = vo;
			this.OperaType = OperaType;
		}

		@Override
		public CalLog call() throws Exception {
			CalLog calLog = new CalLog(vo.getCampaignId());
			calLog.setStartTime(System.currentTimeMillis());
			// 统计
			cal();
			calLog.setEndTime(System.currentTimeMillis());
			
			return calLog;
		}
		
		private void cal() {
			if(OperaType == CalculateType.NON_CRITERIA_CALCULATE) {
				campaignCalculateService.calNonCriteriaIncome(vo);
			} else if(OperaType == CalculateType.SUM_CALCULATE) {
				campaignCalculateService.calSumIncome(vo);
			} else if(OperaType == CalculateType.CALCULATE) {
				if(vo.getCriteriaScheme().contains(CampaignCriteriaDef.GROUP_ID_TICKET)) { //按票统计收入
					campaignCalculateService.calTicketIncome(vo);
				} else if(vo.getCriteriaScheme().contains(CampaignCriteriaDef.GROUP_ID_CON_ITEM)) { //按卖品统计收入
					campaignCalculateService.calConSaleIncome(vo);
				}
			}
		}
	}
	
	/**
	 * 计算日志
	 * 
	 * @author ZR
	 *
	 */
	public class CalLog implements Serializable {
		
		private static final long serialVersionUID = 5487073063718312890L;
		/* 活动ID */
		private Long campaignId;
		/* 开始执行时间 */
		private Long startTime;
		/* 结束执行时间 */
		private Long endTime;
		
		public CalLog(Long campaignId) {
			this.campaignId = campaignId;
		}
		
		public Long getCampaignId() {
			return campaignId;
		}
		public void setCampaignId(Long campaignId) {
			this.campaignId = campaignId;
		}
		public Long getStartTime() {
			return startTime;
		}
		public void setStartTime(Long startTime) {
			this.startTime = startTime;
		}
		public Long getEndTime() {
			return endTime;
		}
		public void setEndTime(Long endTime) {
			this.endTime = endTime;
		}
		
	}
	
	/**
	 * 计算类型
	 * 
	 * @author ZR
	 *
	 */
	public static enum CalculateType {
		SUM_CALCULATE("统计总值", 2), CALCULATE("普通票或卖品统计", 1), NON_CRITERIA_CALCULATE("无推荐规则响应统计", 3);
		
		private String name;
		private int type;
		private CalculateType(String name, int type) {
			this.name = name;
			this.type = type;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getType() {
			return type;
		}
		public void setType(int type) {
			this.type = type;
		}
		
	}
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		// 统计推荐+对比组
		/*Future<CalLog> one = CalculateThreadPool.getInstance().submitTest(new Callable<CalLog>() {
			
			@Override
			public CalLog call() throws Exception {
				System.out.println("1->start");
				Thread.sleep(8000);
				System.out.println("1->end");
				
				return null;
			}
			
		});
		
		// 统计总数
		Future<CalLog> two = CalculateThreadPool.getInstance().submitTest(new Callable<CalLog>() {

			@Override
			public CalLog call() throws Exception {
				System.out.println("2->start");
				Thread.sleep(1000);
				System.out.println("2->end");
				return null;
			}
			
		});
		
		System.out.println("wait");
		one.get();
		System.out.println("1");
		two.get();
		System.out.println("2");
		
		CalculateThreadPool.getInstance().destory();*/
	}
	
}
