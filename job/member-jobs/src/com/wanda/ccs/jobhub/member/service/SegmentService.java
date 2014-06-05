package com.wanda.ccs.jobhub.member.service;

import com.wanda.ccs.jobhub.member.DataAccessException;
import com.wanda.ccs.jobhub.member.SegmentCalCountException;

public interface SegmentService {

	/**
	 * 锁定某客群，在客群的标志位 OCCUPIED 字段上设置一个标记。表示客群正在被使用，不能修改。
	 * @param segmentId
	 * @return
	 */
	boolean updateLockSegment(Long segmentId, String occupiedBy);

	void updateUnlockSegment(Long segmentId, String occupiedBy);

	/**
	 * 进行客群计算操作。
	 * 首先根据客群设置得到计算客群的数量的SQL；然后用此SQL到数据仓库中查询得到数量；最后把新计算出的数量和计算时间更新到 T_SEGMENT.CAL_COUNT 和T_SEGMENT.CAL_COUNT_TIME
	 * 
	 * @param segmentId
	 * @throws DataAccessException
	 */
	Long updateCalCount(Long segmentId, int timeout)
			throws SegmentCalCountException;

	Integer updateExport(Long segmentExportId, int timeout) ;

}