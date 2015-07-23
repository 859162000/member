/**  
* @Title: MessageSendConf.java
* @Package com.wanda.ccs.member.segment
* @Description: TODO(用一句话描述该文件做什么)
* @author 许雷
* @date 2015年6月23日 下午2:33:22
* @version V1.0  
*/
package com.wanda.ccs.member.segment;

/**
 * @ClassName: MessageSendConf
 * @Description: 客群短信的SQL 以及配置 
 * @author 许雷
 * @date 2015年6月23日 下午2:33:22
 *
 */
public interface MessageSendConf {
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ SQL string  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //

		/**
		* @Fields NO_SEND_CAL_SQL :计算客群不希望联系人数的SQL
		*/
		final String NO_SEND_CAL_SQL = "select COUNT(1)\n"
				+ "from T_SEGM_MEMBER SEME\n"
				+ "LEFT JOIN T_MEMBER ME ON SEME.MEMBER_ID = ME.MEMBER_ID \n"
				+ "WHERE ME.ISCONTACTABLE = 0 and SEME.SEGMENT_ID=?";

		/**
		* @Fields SELECT_MSG_SVC_INFO
		*/
		final String SELECT_MSG_SVC_INFO = "select mc.parameter_value from t_member_config mc where mc.parameter_name = ?";

		/**
		 * @Fields UPDATE_APPROVE_STATUS : 修改SEGM_MESSAGE审批状态的SQL
		 */
		final String UPDATE_SEND_STATUS = "update SEGM_MESSAGE set SEND_STATUS=? where SEGM_MESSAGE_ID=?";
		/**
		* @Fields NEXT_SEQ_ID_SQL : 获取SEGM_MESSAGE主键的SQL
		*/
		final String NEXT_SEQ_ID_SQL = "select S_SEGM_MESSAGE.NEXTVAL from DUAL";

		/**
		* @Fields NEXT_APPROVE_ID_SQL : 获取MESSAGE_APPROVE主键的SQL
		*/
		final String NEXT_APPROVE_ID_SQL = "select S_MESSAGE_APPROVE.NEXTVAL from DUAL";
		/**
		* @Fields NEXT_MESSAGE_SEND_LOG : 获取MESSAGE_SEND_LOG主键的SQL
		*/
		final String NEXT_MESSAGE_SEND_LOG = "select S_MESSAGE_SEND_LOG.NEXTVAL from DUAL";

		/**
		* @Fields SAVE_MESSAGE : 修改SEGM_MESSAGE信息的SQL
		*/
		final String SAVE_TIJIAO_MESSAGE = "update SEGM_MESSAGE set APPROVE_STATUS=?,CONTENT=?,SEND_TIME=?,UPDATE_BY=?,UPDATE_DATE=?,APPROVER=? where SEGM_MESSAGE_ID=?";
		/**
		* @Fields SAVE_MESSAGE : 修改SEGM_MESSAGE信息的SQL
		*/
		final String SAVE_BAOCUN_MESSAGE = "update SEGM_MESSAGE set APPROVE_STATUS=?,CONTENT=?,SEND_TIME=?,UPDATE_BY=?,UPDATE_DATE=? where SEGM_MESSAGE_ID=?";

		/**
		* @Fields UPDATE_APPROVE_STATUS : 修改SEGM_MESSAGE审批状态的SQL
		*/
		final String UPDATE_APPROVE_STATUS = "update SEGM_MESSAGE set APPROVE_STATUS=?,APPROVER=? where SEGM_MESSAGE_ID=?";

//		/**
//		* @Fields SQL : 获取影城级别审批人账号的SQL
//		*/
//		final String CINEMA_SQL = "with regions as (select orgid,orgcode from ehr_wd_org@read_DL where parentunitid = 68468088 	and status = 1) "
//				+ "select u.username rtx "
//				+ "from ehr_wd_org@read_DL     o, "
//				+ "regions                          r, "
//				+ "ehr_wd_user@read_DL        u, "
//				+ "ehr_wd_user_pos_rel@read_DL prel, "
//				+ "ehr_wd_pos@read_DL         p, "
//				+ "t_cinema@read_DL            c "
//				+ "where o.parentunitid = r.orgid  "
//				+ "and o.status = 1  "
//				+ "and c.orgcode = o.orgcode "
//				+ "and u.employeeStatus = 2 "
//				+ "and u.orgid = o.orgid "
//				+ "and u.employeeid = prel.employeeid "
//				+ "and prel.jobid = p.jobid "
//				+ "and p.jobname = '影城经理' and c.seqid=? " 
//				+ "and rownum = 1";
//		/**
//		* @Fields SQL : 获取区域级别审批人账号的SQL
//		*/
//		final String REGION_SQL = "with regions as (select orgid,orgcode from ehr_wd_org@read_DL where parentunitid = 68468088 	and status = 1) "
//				+ "select u.username rtx "
//				+ "from ehr_wd_org@read_DL     o, "
//				+ "regions                          r, "
//				+ "ehr_wd_user@read_DL        u, "
//				+ "ehr_wd_user_pos_rel@read_DL prel, "
//				+ "ehr_wd_pos@read_DL         p, "
//				+ "t_cinema@read_DL            c "
//				+ "where o.parentunitid = r.orgid  "
//				+ "and o.status = 1  "
//				+ "and c.orgcode = o.orgcode "
//				+ "and u.employeeStatus = 2 "
//				+ "and u.orgid = o.orgid "
//				+ "and u.employeeid = prel.employeeid "
//				+ "and prel.jobid = p.jobid "
//				+ "	and p.jobname = '区域总经理' and c.area=? " 
//				+ "and rownum = 1";
//		/**
//		* @Fields GROUP_SQL : 获取院线级审批人账号
//		*/
//		String GROUP_SQL = "select  u.username " +
//				"from  " +
//				"ehr_wd_user@read_DL        u,  " +
//				"ehr_wd_user_pos_rel@read_DL prel  " +
//				"where  u.employeeStatus = 2  " +
//				"and u.employeeid = prel.employeeid  " +
//				"and prel.jobid =  '1255477183'  " +
//				"and rownum = 1"; 
		/**
		* @Fields CREATE_TABLE_SQL : 创建手机号临时表
		*/
		final String CREATE_TABLE_SQL =  "create table ${tableName} as   (select ME.MOBILE,SEME.SEGMENT_ID  " +
					"from T_SEGM_MEMBER SEME  " + 
					"LEFT JOIN T_MEMBER ME ON SEME.MEMBER_ID = ME.MEMBER_ID  " +    
					"WHERE ME.ISCONTACTABLE <> 0 and SEME.SEGMENT_ID=${SEGMENT_ID})";
		/**
		* @Fields CREATE_TABLE_SQL : 创建手机号临时表
		*/
		final String DROP_TABLE_SQL =  "drop table ${tableName}";
		/**
		* @Fields CREATE_TABLE_SQL : 创建手机号临时表
		*/
		final String EXITS_TABLE_SQL =  "select count(*) from  all_tables where table_name = '${tableName}' ";
		
		/**
		* @Fields MOBILE_GET_SQL : 获取客群手机号的SQL
		*/
		final String MOBILE_GET_SQL = "select MOBILE from ${tableName}";
		final String VERSION = "1.0"; // 客群短信版本号

		final String APPROVEVOVERSION = "1.0";// 审批版本号
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ 审批状态汇总   ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * @Fields APPROVE_PIZHUN :提交状态
		 */
		final String TIJIAO = "1";
		/**
		 * @Fields APPROVE_PIZHUN :保存状态
		 */
		final String BAOCUN = "0";
		/**
		 * @Fields APPROVE_PIZHUN :审批批准状态
		 */
		final String APPROVE_PIZHUN = "1";

		/**
		 * @Fields APPROVE_TUIHUI : 审批退回状态
		 */
		final String APPROVE_TUIHUI = "0";

		/**
		 * @Fields APPROVE_CHEXIAO : 审批撤销状态
		 */
		final String APPROVE_CHEXIAO = "9";

		/**
		 * @Fields WAIT_C_APPROVE : 待影城经理审批
		 */
		final String WAIT_C_APPROVE = "1000";

		/**
		 * @Fields WAIT_R_APPROVE : 待区域经理审批
		 */
		final String WAIT_R_APPROVE = "2000";

		/**
		 * @Fields WAIT_G_APPROVE : 待院线会员经理审批
		 */
		final String WAIT_G_APPROVE = "3000";

		/**
		 * @Fields BACK_C_APPROVE : 由影城经理退回修改
		 */
		final String BACK_C_APPROVE = "1999";

		/**
		 * @Fields BACK_R_APPROVE : 区域经理退回修改
		 */
		final String BACK_R_APPROVE = "2999";

		/**
		 * @Fields BACK_G_APPROVE : 院线经理退回修改
		 */
		final String BACK_G_APPROVE = "3999";

		/**
		 * @Fields NO_PASS_APPROVE : 不通过状态
		 */
		final String NO_PASS_APPROVE = "9999";

		/**
		 * @Fields PASS_APPROVE : 通过状态
		 */
		final String PASS_APPROVE = "9000";

}
