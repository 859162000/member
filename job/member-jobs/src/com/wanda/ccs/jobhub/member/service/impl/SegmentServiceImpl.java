package com.wanda.ccs.jobhub.member.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.wanda.ccs.jobhub.member.SegmentCalCountException;
import com.wanda.ccs.jobhub.member.SegmentExcelExporter;
import com.wanda.ccs.jobhub.member.dao.SegmentDao;
import com.wanda.ccs.jobhub.member.service.SegmentService;
import com.wanda.ccs.jobhub.member.vo.CombineSegmentSubVo;
import com.wanda.ccs.jobhub.member.vo.SegmentExportVo;
import com.wanda.ccs.jobhub.member.vo.SegmentVo;
import com.wanda.ccs.member.segment.defimpl.SegmentCriteriaDef;
import com.wanda.ccs.member.segment.service.CriteriaQueryResult;
import com.wanda.ccs.member.segment.service.impl.CriteriaQueryServiceImpl;

public class SegmentServiceImpl implements SegmentService {
	
	@InstanceIn(path = "SegmentDao")
	private SegmentDao segmentDao;
	
	private CriteriaQueryServiceImpl segmentCriteria = new CriteriaQueryServiceImpl();
	

	public boolean updateLockSegment(Long segmentId, String occupiedBy) {
		return segmentDao.lockSegment(segmentId, occupiedBy); 
	}
	
	public void updateUnlockSegment(Long segmentId, String occupiedBy) {
		segmentDao.unlockSegment(segmentId, occupiedBy);
	}

	public Long updateCalCount(Long segmentId, int timeout)
			throws SegmentCalCountException {
		SegmentVo segmentVo = segmentDao.getSegment(segmentId);
		Long newCount = 0L;
		
		try {
			if(segmentVo.getCombineSegment() == false) {
				CriteriaQueryResult criteriaResult = segmentCriteria.getSegmentQuery(segmentVo.getCriteriaScheme(), segmentVo.getSortName(), segmentVo.getSortOrder());
				SegmentCriteriaDef segmentCriteriaDef = new SegmentCriteriaDef();
				Long countInDw = segmentDao.getCountInDw(criteriaResult, timeout);
				
				//客户行中更新客户实际数量, 如果数据仓库中的数量大于最大数量限制，则直接等于最大数量
				newCount = (countInDw >= segmentVo.getMaxCount()) ? segmentVo.getMaxCount() : countInDw;
				
				segmentDao.deleteSegmMember(segmentId);//删除客群落地会员
				segmentDao.saveSegmMember(criteriaResult, newCount, timeout, segmentId,segmentCriteriaDef.getOrderByMap().get(segmentVo.getSortName()));
				segmentDao.updateCalCount(segmentId, newCount, SegmentVo.STATUS_COMPLETE); //如果数据仓库中的数量小于最大数量，则等于数据仓库中的数量
	
				//获取包含该客群的符合客群集合
				List<CombineSegmentSubVo> list = segmentDao.getCombineSegmentBySubId(segmentId);
				//更新符合客群
				if(list != null && !list.isEmpty()){
					for(CombineSegmentSubVo segmentSubVo : list){
						updateCalCount(segmentSubVo.getSegmentId(), timeout);
					}
				}
			}
			else {
				List<CombineSegmentSubVo> list = segmentDao.getCombineSegment(segmentId);
				if(list != null && !list.isEmpty()){
					List<Object> args = new ArrayList<Object>();
					StringBuffer sql = new StringBuffer();
					CombineSegmentSubVo segmentSubVo = list.get(0);
					args.add(segmentSubVo.getSubSegmentId());
					sql.append(" (select MEMBER_ID from T_SEGM_MEMBER where segment_id = ?) ");
					newCount = segmentDao.getSegmentCount(sql.toString(), args);
					segmentDao.updateSubSegmentCount(segmentSubVo.getCombineSegmentSubId(), 0L, newCount);
					for(int i = 1; i< list.size(); i++){
						Long oldCount = segmentDao.getSegmentCount(sql.toString(), args);
						CombineSegmentSubVo nextSegmentSubVo = list.get(i);
						sql.insert(0, "(").append(nextSegmentSubVo.getSetRelation()).append(" (select MEMBER_ID from T_SEGM_MEMBER where segment_id = ?) ").append(")");
						args.add(nextSegmentSubVo.getSubSegmentId());
						newCount = segmentDao.getSegmentCount(sql.toString(), args);
						segmentDao.updateSubSegmentCount(nextSegmentSubVo.getCombineSegmentSubId(), newCount - oldCount, newCount);
					}
				}
			}
			
			//更新客群数量，并设置客群状态为计算完成
			segmentDao.updateCalCount(segmentId, newCount, SegmentVo.STATUS_COMPLETE);
		}
		catch (Exception e) {
			segmentDao.updateSegmentStatus(segmentId, SegmentVo.STATUS_FAILED);
			throw new SegmentCalCountException(e);
		}
		
		return newCount;
	}

	public Integer updateExport(Long segmentExportId, int timeout)  {
		SegmentExportVo export = segmentDao.getSegmentExport(segmentExportId);
		Long segmentId = export.getSegmentId();
		
		SegmentVo segmentVo = segmentDao.getSegment(segmentId);
		
		SegmentExcelExporter exporter = segmentDao.export(segmentVo,segmentId, export, segmentVo.getMaxCount(), timeout);
		
		segmentDao.insertExportFile(export, segmentVo.getName() + ".xlsx", exporter);
		
		segmentDao.updateExport(segmentExportId, SegmentExportVo.STATUS_EXPORTED, exporter.exportedRowCount());
		
		return exporter.exportedRowCount();
	}
	
}
