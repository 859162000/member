package com.wanda.mms.control.response.handle;

import java.sql.Connection;

import org.apache.log4j.Logger;

import com.solar.etl.db.SqlHelp;

public abstract class AbstractActHandler implements ActHandler {
	static Logger logger = Logger.getLogger(AbstractActHandler.class.getName());
	private String responsColumn;
	private Connection conn;
	public AbstractActHandler(String responsColumn,Connection conn) {
		this.responsColumn = responsColumn;
		this.conn = conn;
	}

	@Override
	public boolean updateHistoryResponse(Long segmentId, Long actTargetId) {
		logger.debug("segmentId=="+segmentId);
		logger.debug("actTargetId=="+actTargetId);
		String updateRsponse = getUpdateSql(responsColumn);

		boolean segmentLocked = false;
		// 3. 执行更新相应响应状态
		try {
			segmentLocked = lockSegment(segmentId, "2");
			if(segmentLocked){
				SqlHelp.operate(conn,updateRsponse,new String[] { Long.toString(segmentId),	Long.toString(actTargetId) });
			}
		} finally {
			if (segmentLocked) {
				unlockSegment(segmentId, "0");
			}
		}
		logger.debug("updateHistoryResponse segmentLocked=="+segmentLocked);
		logger.info("营销效果统    计活动联络历史清单    ， SQL：" + updateRsponse + "条件："	+ Long.toString(segmentId) + "  :   "+ Long.toString(actTargetId));
		return segmentLocked;
	}

	private boolean lockSegment(Long segmentId, String occupiedBy) {
		logger.debug("occupiedBy=="+occupiedBy);
		boolean rows = false;
		String updateSegment = "update T_SEGMENT t set t.OCCUPIED=? where t.SEGMENT_ID=? and t.OCCUPIED='0'";
		int rowsflag = SqlHelp.operate(conn, updateSegment, new String[] {
				occupiedBy, Long.toString(segmentId) });
		if (rowsflag == 1) {
			rows = true;
		}
		return rows;
	}

	/**
	 * 解锁表客户群对应客群ID列
	 * 
	 * @param segmentId
	 * @param occupiedBy
	 * @return
	 */
	private boolean unlockSegment(Long segmentId, String occupiedBy) {
		boolean rows = false;
		String updateSegment = "update T_SEGMENT t set t.OCCUPIED=? where t.SEGMENT_ID=? and t.OCCUPIED='2'";
		int rowsflag = SqlHelp.operate(conn, updateSegment, new String[] {
				occupiedBy, Long.toString(segmentId) });
		if (rowsflag == 1) {
			rows = true;
		}
		return rows;
	}

	private String getUpdateSql(String responsColumn) {
		// 2. 拼接相应响应状态SQL字段
		String updateRsponse = "UPDATE t_contact_history upd_his set "
				+ responsColumn
				+ "='1' where  exists ("
				+ "select his.contact_history_id "
				+ "from t_contact_history his, t_segm_member seg "
				+ "where his.member_id=seg.member_id and seg.segment_id=? and his.act_target_id=? and upd_his.contact_history_id =his.contact_history_id  )  and HAS_SEND='1' ";
		return updateRsponse;
	}

}
