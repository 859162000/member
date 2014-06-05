/**
 * 
 */
package com.wanda.ccs.jobhub.member.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.google.code.pathlet.jdbc.ExtJdbcTemplate;
import com.wanda.ccs.jobhub.member.SystemException;
import com.wanda.ccs.jobhub.member.vo.CombineSegmentSubVo;
import com.wanda.ccs.jobhub.member.vo.SegmentVo;
import com.wanda.ccs.jobhub.member.vo.TargetVo;

/**
 * @author YangJianbin
 *
 */
public class CreateContactHistoryDao {
	
	private final static int BATCH_FETCH_SIZE = 1000;
	
	@InstanceIn(path = "/dataSource")
	private DataSource dataSource;
	
	@InstanceIn(path = "/transactionManager")
	private DataSourceTransactionManager transactionManager;
	
	private ExtJdbcTemplate extJdbcTemplate = null;
	
	@InstanceIn(path = "SegmentDao")
	private SegmentDao segmentDao;
	
	public ExtJdbcTemplate getJdbcTemplate()  {
		if(this.extJdbcTemplate == null) {
			this.extJdbcTemplate = new ExtJdbcTemplate(dataSource);
		}
		return this.extJdbcTemplate;
	}
	
	/**
	 * 保存联络清单，首先判断目标类型:
	 * 	若为客群，则从落地客群中取出会员;
	 * 	若为文件导入，则从文件关联数据中取出会员
	 * @param targetVo
	 * 			冻结目标
	 * @param userId
	 * 			操作人员
	 * @param maxCount
	 * 			受众数量
	 * @param controlCount
	 * 			控制组数量
	 */
	public void saveContactHistoryFromTarget(final TargetVo targetVo, final String userId, final int maxCount, final int controlCount){
		if(targetVo == null)
			return;
		String sql = "";
		ArrayList<Object> argList = new ArrayList<Object>();
		if(targetVo.getTargetType().equals("10")){//目标类型为客群
			SegmentVo segmentVo = segmentDao.getSegment(targetVo.getSegmentId());
			StringBuffer segmentSql = new StringBuffer();
			if(segmentVo.getCombineSegment() == false ){
				segmentSql.append("SELECT  s.MEMBER_ID from T_SEGM_MEMBER s where s.SEGMENT_ID = ? ORDER BY dbms_random.VALUE()");
				argList.add(targetVo.getSegmentId());
			}else if(segmentVo.getCombineSegment() == true){
				List<CombineSegmentSubVo> list = segmentDao.getCombineSegment(targetVo.getSegmentId());
				if(list != null && !list.isEmpty()){
					CombineSegmentSubVo segmentSubVo = list.get(0);
					argList.add(segmentSubVo.getSubSegmentId());
					segmentSql.append(" (select MEMBER_ID from T_SEGM_MEMBER where segment_id = ?) ");
					for(int i = 1; i< list.size(); i++){
						CombineSegmentSubVo nextSegmentSubVo = list.get(i);
						segmentSql.insert(0, "(").append(nextSegmentSubVo.getSetRelation()).append(" (select MEMBER_ID from T_SEGM_MEMBER where segment_id = ?) ").append(")");
						argList.add(nextSegmentSubVo.getSubSegmentId());
					}
				}
				segmentSql.insert(0, "select distinct MEMBER_ID from ( ").append(" ) ORDER BY dbms_random.VALUE()");
			}
//			sql = "select s.MEMBER_ID from T_SEGM_MEMBER s where s.SEGMENT_ID = ?  ORDER BY dbms_random.VALUE()";
//			argList.add(targetVo.getSegmentId());
			sql = segmentSql.toString();
		}else if(targetVo.getTargetType().equals("20")){//目标类型为excel导入
			sql = "select contact.MEMBER_ID from T_CONTACT_HISTORY_TEMP contact where contact.FILE_ATTACH_ID = ?  ORDER BY dbms_random.VALUE()";
			argList.add(targetVo.getFileAttachId());
    	}
		//this.logHelper.info("saveContactHistory start");
		getJdbcTemplate().query(sql, argList.toArray(), new ResultSetExtractor<Object>() {
			public Object extractData(ResultSet resultset) throws SQLException,
					org.springframework.dao.DataAccessException {
				try {
					List<Long> memberIds = new ArrayList<Long>();
					int selectedRows = 0;
					int memberSize = maxCount+controlCount;
					resultset.setFetchSize(memberSize);
					while(resultset.next()) {
						selectedRows++;
						Long memberId = resultset.getLong(1);
						memberIds.add(memberId);
						if(selectedRows == maxCount){//当选择数量等于受众数量时，则把当前选择的保存为受众数量
							saveContactHistory(memberIds,targetVo.getActTargetId(),userId, "0");
							memberIds.clear();
							//logHelper.info("~~reach the MAX Count, selectedRows:" + selectedRows + ", isControlset: false");
						}else if(memberIds.size() >= BATCH_FETCH_SIZE){
							if(selectedRows <= maxCount){//当查询数量小于受众数量时，插入类型为受众数量
								saveContactHistory(memberIds,targetVo.getActTargetId(),userId, "0");
								//logHelper.info("~~reach the MAX Count, selectedRows:" + selectedRows + ", isControlset: false");
							}else{//反之为控制组数量
								saveContactHistory(memberIds,targetVo.getActTargetId(),userId, "1");
								//logHelper.info("~~reach the MAX Count, selectedRows:" + selectedRows + ", isControlset: true");
							}
							memberIds.clear();
						}
						if(selectedRows >= memberSize) {
							//logHelper.info("~~reach the MAX Count, selectedRows:" + selectedRows);
							break;
						}
					}
					if(memberIds.size() > 0){
						if(selectedRows <= maxCount){
							saveContactHistory(memberIds,targetVo.getActTargetId(),userId, "0");
							//logHelper.info("~~reach the MAX Count, selectedRows:" + selectedRows + ", isControlset: false");
						}else{
							saveContactHistory(memberIds,targetVo.getActTargetId(),userId, "1");
							//logHelper.info("~~reach the MAX Count, selectedRows:" + selectedRows + ", isControlset: true");
						}
						memberIds.clear();
					}
				} catch (Exception e) {
					throw new SQLException(e);
				}
				return null;
			}
		});
		//this.logHelper.info("saveContactHistory end");
	}
	
	/**
	 * 保存联络清单
	 * @param memberIdList 
	 * 			会员id集合
	 * @param actRargetId
	 * 			目标id
	 * @param userId
	 * 			操作的用户
	 * @param isControlset
	 * 			是否控制组人员
	 * @throws Exception
	 */
	public void saveContactHistory(final List<Long> memberIdList,
			final Long actRargetId, final String userId, final String isControlset) throws Exception{
		if(memberIdList == null || memberIdList.isEmpty())
			return;
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setName("SomeTxName");
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			String sql = " insert into T_CONTACT_HISTORY(CONTACT_HISTORY_ID, ACT_TARGET_ID, MEMBER_ID, HAS_RESPONSE, HAS_RESPONSE2, HAS_SEND, IS_CONTROLSET, create_by, create_date, version) values (S_T_CONTACT_HISTORY.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
			getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
				public int getBatchSize() {
					return memberIdList.size();
				}

				public void setValues(PreparedStatement pst, int i)
						throws SQLException {
					pst.setLong(1, actRargetId);
					pst.setLong(2, memberIdList.get(i));
					pst.setString(3, "0");
					pst.setString(4, "0");
					pst.setString(5, "0");
					pst.setString(6, isControlset);
					pst.setString(7, userId);
					pst.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
					pst.setString(9, "0");
				}
			});
		}catch(Exception ex) {
			transactionManager.rollback(status);
			throw ex;
		}
		transactionManager.commit(status);
	}
	
	/**
	 * 删除联络清单
	 * @param actTargetId
	 * @throws Exception
	 */
	public void delContactHistory(String actTargetId) {
		//this.logHelper.info("delete contactHistory dao start");
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setName("SomeTxName");
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			String delSql = "delete from T_CONTACT_HISTORY contact where contact.ACT_TARGET_ID = ?";
			ArrayList<Object> delArgList = new ArrayList<Object>();
			delArgList.add(actTargetId);
			getJdbcTemplate().update(delSql, delArgList.toArray());
		}catch(Exception ex) {
			transactionManager.rollback(status);
			throw new SystemException(ex);
		}
		transactionManager.commit(status);
		//this.logHelper.info("delete contactHistory success: targetId =" + actTargetId);
	}
	
}
