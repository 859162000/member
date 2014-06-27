package com.wanda.ccs.jobhub.member.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.wanda.ccs.jobhub.member.SegmentCSVExporter;
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
	
	public Long[] updateLockCombineSegment(Long[] segmentId, String occupiedBy) {
		return segmentDao.lockCombineSegment(segmentId, occupiedBy);
	}
	
	public void updateUnLockCombineSegment(Long[] segmentId, String occupiedBy) {
		segmentDao.unlockCombineSegment(segmentId, occupiedBy);
	}
	
	/*public static void main(String[] args) {
		com.mchange.v2.c3p0.ComboPooledDataSource dataSource = null;
		try {
			dataSource = new com.mchange.v2.c3p0.ComboPooledDataSource();
			dataSource.setUser("ccs_mbr_prod");
			dataSource.setPassword("ccs_mbr_prod");
			dataSource.setJdbcUrl("jdbc:oracle:thin:@10.199.201.105:1521:ccsstag");
			
			SegmentServiceImpl service = new SegmentServiceImpl();
			SegmentDao dao = new SegmentDao();
			dao.dataSource = dataSource;
			dao.dataSourceOds = dataSource;
			service.segmentDao = dao;
			
			service.updateCalCount(1333L, 100000000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dataSource.close();
		}
	}*/
	
	@Transactional
	public Long updateCalCount(Long segmentId, int timeout) throws SegmentCalCountException {
		SegmentVo segmentVo = segmentDao.getSegment(segmentId);
		Long newCount = 0L;
		
		try {
			if(segmentVo.getCombineSegment() == false) {	//单一客群计算
				newCount = segmentCalCount(segmentVo, timeout);
			} else {										//复合客群计算
				newCount = combineSemgentCalCount(segmentVo, timeout);
			}
			
			Long controlNum = newCount / 100 * segmentVo.getControlCountRate();
			if(controlNum < 1) {
				controlNum = 0L;
			}
			segmentDao.updateControl(segmentVo.getSegmentId(), 0L, controlNum);
			
			//更新客群数量，并设置客群状态为计算完成
			segmentDao.updateCalCount(segmentId, newCount, controlNum, SegmentVo.STATUS_COMPLETE);
		} catch (Exception e) {
			segmentDao.updateSegmentStatus(segmentId, SegmentVo.STATUS_FAILED);
			throw new SegmentCalCountException(e);
		}
		
		return newCount;
	}
	
	/**
	 * 计算单个客群
	 * 
	 * @throws Exception 
	 * 
	 */
	private Long segmentCalCount(SegmentVo segmentVo, int timeout) throws Exception {
		Long newCount = 0L;		
		
		CriteriaQueryResult criteriaResult = segmentCriteria.getSegmentQuery(segmentVo.getCriteriaScheme(), segmentVo.getSortName(), segmentVo.getSortOrder());
		SegmentCriteriaDef segmentCriteriaDef = new SegmentCriteriaDef();
		Long countInDw = segmentDao.getCountInDw(criteriaResult, timeout);
		
		//客户行中更新客户实际数量, 如果数据仓库中的数量大于最大数量限制，则直接等于最大数量
		newCount = (countInDw >= segmentVo.getMaxCount()) ? segmentVo.getMaxCount() : countInDw;
		
		segmentDao.deleteSegmMember(segmentVo.getSegmentId());//删除客群落地会员
		segmentDao.saveSegmMember(criteriaResult, newCount, timeout, segmentVo.getSegmentId(), segmentCriteriaDef.getOrderByMap().get(segmentVo.getSortName()));
		
		//更新客群数量，并设置客群状态为计算完成
		//segmentDao.updateCalCount(segmentVo.getSegmentId(), newCount, SegmentVo.STATUS_COMPLETE);
		
		return newCount;
	}
	
	/**
	 * 计算复合客群
	 * 
	 * @param segmentVo
	 * @param timeout
	 * @throws Exception 
	 */
	private Long combineSemgentCalCount(SegmentVo segmentVo, int timeout) throws Exception {
		Long newCount = 0L;							//复合客群计算结果
		Long[] subSegmentIds = null;				//复合客群中所有子客群列表
		List<CombineSegmentSubVo> oldList = null;	//第一次加载的复合客群列表
		List<CombineSegmentSubVo> newList = null;	//此列表主要作用：当验证复合客群中子客群操作时如果有存在版本变化或计算数量变化情况时
		
		List<Integer> dValue = null;	//差值缓存
		List<String> batchSql = null;	//更新sql缓存
		List<String> countAlterSqlList = null;	//更新sql缓存
		
		try {
			oldList = segmentDao.getCombineSegmentById(segmentVo.getSegmentId());
			List<Long> combineSegmentId = new ArrayList<Long>();
			for(int i=0,len=oldList.size();i<len;i++) {
				combineSegmentId.add(oldList.get(i).getSubSegmentId());
			}
			subSegmentIds = combineSegmentId.toArray(new Long[] {});
			combineSegmentId = null;
			
			if(lockSegment(subSegmentIds)) {		//锁定成功
				newList = isComplete(oldList, segmentVo, timeout);		//复合客群中涉及的子客群全部计算完成并返回最新的子客群数据
				
				batchSql = new ArrayList<String>();
				CombineSegmentSubVo oldVo = null;
				CombineSegmentSubVo newVo = null;
				// 判断复合客群中所涉及的客群是否计算数量是否都已更新
				for(int i=0,len=oldList.size();i<len;i++) {
					oldVo = oldList.get(i);
					newVo = newList.get(i);
					if(!oldVo.getSegmentVersion().equals(newVo.getVersion())) { //子客群版本有变化
						// 更新最新子客群版本及其数量
						batchSql.add("update T_COMBINE_SEGMENT_SUB s set s.segment_version="+newVo.getVersion()+",s.cal_count="+newVo.getCalCount()+",s.cal_count_time=to_date('"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(newVo.getCalCountTime())+"','yyyy-MM-dd HH24:mi:ss') where s.combine_segment_sub_id="+newVo.getCombineSegmentSubId());
					} else if(oldVo.getSubCalCount() == null || oldVo.getSubCalCount() == -1) {	//子客群计算完成，需要更新复合客群中的子客群版本
						// 更新子客群数量
						batchSql.add("update T_COMBINE_SEGMENT_SUB s set s.cal_count="+newVo.getCalCount()+",s.cal_count_time=to_date('"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(newVo.getCalCountTime())+"','yyyy-MM-dd HH24:mi:ss') where s.combine_segment_sub_id="+newVo.getCombineSegmentSubId());
					}
					
					oldVo = null;newVo = null;
				}
				
				// 开始进行复合客群计算
//				Map<String, String> param = new HashMap<String, String>();
//				param.put("relation", "");
//				param.put("temptable", "");
				countAlterSqlList = new ArrayList<String>();		//计算复合关系sql缓存
				dValue = new ArrayList<Integer>();					//差值缓存
				StringBuilder countAlterSql = new StringBuilder();	//计算复合关系sql
				String countAlterResultSql = "";					//计算结果sql
				String suffixSql = "select count(member_id) from (";
				int firstSegmentCalCount = -1;
				for(int i=0,len=newList.size();i<len;i++) {
					newVo = newList.get(i);
					if(i == 0) {
						firstSegmentCalCount = newVo.getCalCount();	//存储第一个子客群的计算数量
						parseSql(countAlterSql, newVo);
						//parseSql(countAlterSql, newVo, i, param);
					} else {		//开始进行与前一个子客群的计算操作
						//parseSql(countAlterSql, newVo, i, param);
						parseSql(countAlterSql, newVo);
						countAlterSqlList.add(suffixSql + countAlterSql.toString() + ")");
						//countAlterSqlList.add(countAlterSql.toString());
					}
					//System.out.println(suffixSql + countAlterSql.toString());
					if(i == len-1) { //保存复合客群计算结果sql
						countAlterResultSql = countAlterSql.toString();
						//countAlterResultSql = "select distinct member_id from (" +countAlterSql.toString();
					}
					newVo = null;
				}
				
				// 将计算的差值保存到缓存中
				int[] countAlter = segmentDao.getCombineSegmentCountAlter(countAlterSqlList.toArray(new String[] {}));
				for(int i=0,len=countAlter.length;i<len;i++) {
					dValue.add(Math.abs(firstSegmentCalCount - countAlter[i]));
					firstSegmentCalCount = countAlter[i];
					if(i == len-1) {	//获取最后一个计算sql为最终计算结果
						newCount = Long.parseLong(countAlter[i]+"");
					}
				}
				//删除客群落地会员
				segmentDao.deleteSegmMember(segmentVo.getSegmentId());
				// 保存计算结果
				segmentDao.saveCombineSegmentMember(countAlterResultSql, timeout, segmentVo.getSegmentId());
				
				// 更新所有计算差值
				for(int i=0,len=dValue.size();i<len;i++) {
					newVo = newList.get(i+1);
					batchSql.add("update T_COMBINE_SEGMENT_SUB s set s.count_alter="+dValue.get(i)+" where s.combine_segment_sub_id="+newVo.getCombineSegmentSubId());
					newVo = null;
				}
				
				// 执行所有复合客群的sql操作
				segmentDao.updateCombineSegmentSql(batchSql.toArray(new String[] {}));
			}
		} catch(Exception e) {
			//e.printStackTrace();
			throw e;
		} finally {
			updateUnLockCombineSegment(subSegmentIds, SegmentVo.OCCUPIED_BY_CAL_COUNT);
		}
		
		return newCount;
	}
	
	public static void main(String[] args) {
		/*String suffixSql = "select count(distinct member_id) from (";
		
		StringBuilder countAlartSql = new StringBuilder();
		List<CombineSegmentSubVo> newList = new ArrayList<CombineSegmentSubVo>();
		CombineSegmentSubVo v1 = new CombineSegmentSubVo();
		v1.setSubSegmentId(969L);
		
		CombineSegmentSubVo v4 = new CombineSegmentSubVo();
		v4.setSubSegmentId(6074L);
		v4.setSetRelation("INTERSECT");
		
		CombineSegmentSubVo v2 = new CombineSegmentSubVo();
		v2.setSubSegmentId(969L);
		v2.setSetRelation("UNION");
		
		CombineSegmentSubVo v3 = new CombineSegmentSubVo();
		v3.setSubSegmentId(969L);
		v3.setSetRelation("MINUS");
		
		newList.add(v1);newList.add(v4);newList.add(v2);newList.add(v3);
		CombineSegmentSubVo newVo = null;
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("relation", "");
		param.put("temptable", "");
		for(int i=0,len=newList.size();i<len;i++) {
			newVo = newList.get(i);
			//if(i == 0) {	//存储第一个子客群的计算数量
				//countAlartSql.append("(select t").append(i).append(".member_id from T_SEGM_MEMBER t").append(i).append(" where t").append(i).append(".segment_id=").append(newVo.getSubSegmentId()).append(")");
				//parseSql(countAlartSql, newVo, i, param);
			parseSql(countAlartSql, newVo);
			//} else {		//开始进行与前一个子客群的计算操作
			//	parseSql(countAlartSql, newVo, i, param);
			//}
			System.out.println(countAlartSql.toString());
			newVo = null;
		}*/
		
		int firstSegmentCalCount = 4423;
		
		int[] countAlter = new int[] {4423, 9947};
		
		for(int i=0,len=countAlter.length;i<len;i++) {
			System.out.println(Math.abs(firstSegmentCalCount - countAlter[i]));
			firstSegmentCalCount = countAlter[i];
//			if(i == len-1) {	//获取最后一个计算sql为最终计算结果
//				newCount = Long.parseLong(countAlter[i]+"");
//			}
		}
	}
	
	private void parseSql(StringBuilder countAlartSql, CombineSegmentSubVo newVo) {
		if("UNION".equals(newVo.getSetRelation())) { //合集
			countAlartSql.append(" union select member_id from T_SEGM_MEMBER t where t.segment_id=").append(newVo.getSubSegmentId());
		} else if("INTERSECT".equals(newVo.getSetRelation())) {	//交集
			countAlartSql.append(" intersect select member_id from T_SEGM_MEMBER t where t.segment_id=").append(newVo.getSubSegmentId());
		} else if("MINUS".equals(newVo.getSetRelation())) { //差集
			countAlartSql.append(" minus select member_id from T_SEGM_MEMBER t where t.segment_id=").append(newVo.getSubSegmentId());
		} else {
			countAlartSql.append("select member_id from T_SEGM_MEMBER t where t.segment_id=").append(newVo.getSubSegmentId());
		}
	}
	
	/**
	 * 
	 * UNION	INTERSECT	MINUS
	 * 如果上一个是union all 那么下一个可直接union all 否则需要创建一张临时表在添加where条件
	 * 
	 * @param countAlartSql
	 * @param newVo
	 */
	/*private void parseSql(StringBuilder countAlartSql, CombineSegmentSubVo newVo, int index, Map<String, String> param) {
		//countAlartSql.deleteCharAt(countAlartSql.length() - 1);
		
		if(index == 0) {
			param.put("temptable", " t"+index);
			countAlartSql.append(" select t").append(index).append(".member_id from T_SEGM_MEMBER t").append(index).append(" where t").append(index).append(".segment_id=").append(newVo.getSubSegmentId());
		} else if(param.get("relation") != null && !"".equals(param.get("relation"))) {
			//countAlartSql.insert(0, "(");
			if("UNION".equals(param.get("relation")) && !"UNION".equals(newVo.getSetRelation())) {
				// 创建一张临时表
				countAlartSql.insert(0, " select member_id from (");
				param.put("temptable", " tt"+index);
				//tempTable = " tt"+index;
				countAlartSql.append(param.get("temptable"));
				
				if("INTERSECT".equals(newVo.getSetRelation())) {	//交集
					countAlartSql.append(" where exists (");
					countAlartSql.append("select 1 from T_SEGM_MEMBER t").append(index).append(" where t").append(index).append(".segment_id=").append(newVo.getSubSegmentId()).append(" and t").append(index).append(".member_id=").append(param.get("temptable")).append(".member_id");
					countAlartSql.append(")");
				} else if("MINUS".equals(newVo.getSetRelation())) { //差集
					countAlartSql.append(" where not exists (");
					countAlartSql.append("select 1 from T_SEGM_MEMBER t").append(index).append(" where t").append(index).append(".segment_id=").append(newVo.getSubSegmentId()).append(" and t").append(index).append(".member_id=").append(param.get("temptable")).append(".member_id");
					countAlartSql.append(")");
				}
			} else {	//不需要创建临时表
				countAlartSql.deleteCharAt(countAlartSql.length() - 1);	//将临时表的右括号清除
				
				if("UNION".equals(newVo.getSetRelation())) { //合集
					countAlartSql.append(" union all ");
					countAlartSql.append("select t").append(index).append(".member_id from T_SEGM_MEMBER t").append(index).append(" where t").append(index).append(".segment_id=").append(newVo.getSubSegmentId());
				} else if("INTERSECT".equals(newVo.getSetRelation())) {	//交集
					countAlartSql.append(param.get("relation")!=null?" and":" where").append(" exists (");
					countAlartSql.append("select 1 from T_SEGM_MEMBER t").append(index).append(" where t").append(index).append(".segment_id=").append(newVo.getSubSegmentId()).append(" and t").append(index).append(".member_id=").append(param.get("temptable")).append(".member_id");
					countAlartSql.append(")");
				} else if("MINUS".equals(newVo.getSetRelation())) { //差集
					countAlartSql.append(param.get("relation")!=null?" and":" where").append(" not exists (");
					countAlartSql.append("select 1 from T_SEGM_MEMBER t").append(index).append(" where t").append(index).append(".segment_id=").append(newVo.getSubSegmentId()).append(" and t").append(index).append(".member_id=").append(param.get("temptable")).append(".member_id");
					countAlartSql.append(")");
				}
			}
		} else {
			countAlartSql.deleteCharAt(countAlartSql.length() - 1);
			
			if("UNION".equals(newVo.getSetRelation())) { //合集
				countAlartSql.append(" union all ");
				countAlartSql.append("select t").append(index).append(".member_id from T_SEGM_MEMBER t").append(index).append(" where t").append(index).append(".segment_id=").append(newVo.getSubSegmentId());
			} else if("INTERSECT".equals(newVo.getSetRelation())) {	//交集
				countAlartSql.append(" and exists (");
				countAlartSql.append("select 1 from T_SEGM_MEMBER t").append(index).append(" where t").append(index).append(".segment_id=").append(newVo.getSubSegmentId()).append(" and t").append(index).append(".member_id=").append(param.get("temptable")).append(".member_id");
				countAlartSql.append(")");
			} else if("MINUS".equals(newVo.getSetRelation())) { //差集
				countAlartSql.append(" and not exists (");
				countAlartSql.append("select 1 from T_SEGM_MEMBER t").append(index).append(" where t").append(index).append(".segment_id=").append(newVo.getSubSegmentId()).append(" and t").append(index).append(".member_id=").append(param.get("temptable")).append(".member_id");
				countAlartSql.append(")");
			}
		}
		
		if(param.get("temptable") != null && !"".equals(param.get("temptable"))) {
			countAlartSql.append(")");
		}
		param.put("relation", newVo.getSetRelation());

	}*/
	
	/**
	 * 锁定复合客群涉及的所有子客群
	 * 
	 * @param combineSegment
	 * @return
	 * @throws Exception
	 */
	private boolean lockSegment(Long[] combineSegment) throws Exception {
		int retry = 0;	//重试3次
		
		Long[] unlockList = updateLockCombineSegment(combineSegment, SegmentVo.OCCUPIED_NONE);
		while(unlockList.length != 0) {	//有被占用子客群存在
			if(unlockList.length > 0) {	//锁定失败
				if(retry < 3) {
					Thread.sleep(1000 * 60 * 5); //间隔3分钟时间
					unlockList = updateLockCombineSegment(combineSegment, SegmentVo.OCCUPIED_NONE);
					retry++;
					System.out.println("lockSegment fail waitting 15min");
				} else {
					StringBuilder errorMsg = new StringBuilder("subSegment [");
					for(int i=0,len=unlockList.length;i<len;i++) {
						errorMsg.append(unlockList[i]).append(",");
					}
					errorMsg.deleteCharAt(errorMsg.length() - 1);
					errorMsg.append("] has been occupied! Try 4 times and still occupied!");
					throw new SegmentCalCountException(errorMsg.toString());
				}	
			}
		}
		
		return true;
	}
	
	/**
	 * 返回最新的复合客群列表（主要用于当复合客群中子客群没有更新计算数量和计算时间的情况）
	 * 
	 * @param subList
	 * @param segmentVo
	 * @param timeout
	 * @return
	 * @throws Exception
	 */
	private List<CombineSegmentSubVo> isComplete(List<CombineSegmentSubVo> subList, SegmentVo segmentVo, int timeout) throws Exception {
		//int retry = 0;
		//boolean isContinue = false;
		
		CombineSegmentSubVo subVo = null;
		//while(!isContinue) {	//循环判断子客群是否全部计算完成直到有错误退出
		for(int i=0,len=subList.size();i<len;i++) {	//判断所有子客群是否计算完成
			subVo = subList.get(i);
			if(SegmentVo.STATUS_FAILED.equals(subVo.getStatus())) {	//有计算失败的存在
				throw new SegmentCalCountException("SubSegment ID="+subVo.getSubSegmentId()+" Calculation Fail!");
			} else if(SegmentVo.STATUS_NONE.equals(subVo.getStatus()) || SegmentVo.STATUS_CALCULATING.equals(subVo.getStatus())) {	//有未计算存在
				// 执行子客群计算，注：复合客群计算时在外层已将所有涉及到的子客群锁定上，如果还存在正在计算的客群将重新进行计算
				try {
					System.out.println("开始进行子客群计算");
					//更新子客群为计算中状态
					segmentDao.updateSegmentStatus(subVo.getSubSegmentId(), SegmentVo.STATUS_CALCULATING);	
					updateCalCount(subVo.getSubSegmentId(), timeout);
					// 计算完成子客群计算成功，重新加载子客群状态
					subList = segmentDao.getCombineSegmentById(segmentVo.getSegmentId());
					//isContinue = true;
					// 复合客群子客群计算完成
					System.out.println("子客群计算完成");
				} catch (Exception e) {	//子客群计算失败
					throw new SegmentCalCountException("SubSegment ID="+subVo.getSubSegmentId()+" Calculation Fail!");
				}
			} else if(SegmentVo.STATUS_COMPLETE.equals(subVo.getStatus())) {	//计算完成
				//isContinue = true;
			} /*else if(SegmentVo.STATUS_CALCULATING.equals(subVo.getStatus())) { //计算中
				// 以获取到锁说明出现某些异常导致客群状态没改变，因此直接计算
				try {
					System.out.println("开始进行子客群计算");
					updateCalCount(subVo.getSubSegmentId(), timeout);
					// 计算完成子客群计算成功，重新加载子客群状态
					subList = segmentDao.getCombineSegmentById(segmentVo.getSegmentId());
					isContinue = true;
					// 复合客群子客群计算完成
					System.out.println("子客群计算完成");
				} catch (Exception e) {	//子客群计算失败
					throw new SegmentCalCountException("SubSegment ID="+subVo.getSubSegmentId()+" Calculation Fail!");
				}
			}*/
			subVo = null;
		}
			
			/*if(!isContinue) {		//子客群没有全部成功并且没有继续向下走的标志,否则如果向下走的标志为true，不在等待
				if(retry < 3) {
					Thread.sleep(1000 * 60 * 15);
					subList = segmentDao.getCombineSegmentById(segmentVo.getSegmentId());
					retry++;
				} else {
				throw new SegmentCalCountException("SegmentId="+segmentVo.getSegmentId()+" calculate timeout! Try 5 times and still calculate!");
				}
				subVo = null;
			}*/
		//}
		
		return subList;
	}
	
	@Transactional
	public Integer updateExport(Long segmentExportId, int timeout)  {
		SegmentExportVo export = segmentDao.getSegmentExport(segmentExportId);
		Long segmentId = export.getSegmentId();
		
		SegmentVo segmentVo = segmentDao.getSegment(segmentId);
		
		//SegmentExcelExporter exporter = segmentDao.export(segmentVo,segmentId, export, segmentVo.getMaxCount(), timeout);
		
		//segmentDao.insertExportFile(export, segmentVo.getName() + ".xlsx", exporter);
		
		SegmentCSVExporter exporter = segmentDao.exportCSV(segmentVo,segmentId, export, segmentVo.getMaxCount(), timeout);
		
		segmentDao.insertExportFile(export, segmentVo.getName() + ".csv", exporter);
		
		segmentDao.updateExport(segmentExportId, SegmentExportVo.STATUS_EXPORTED, exporter.exportedRowCount());
		
		return exporter.exportedRowCount();
	}
	
}
