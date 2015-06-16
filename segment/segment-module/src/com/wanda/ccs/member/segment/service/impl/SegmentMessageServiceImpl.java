/**  
 * @Title: SegmentMessageS.java
 * @Package com.wanda.ccs.member.segment.service.impl
 * @Description: 客群短信发送服务类
 * @author 许雷
 * @date 2015年5月21日 上午11:12:37
 * @version V1.0  
 */
package com.wanda.ccs.member.segment.service.impl;

import static com.wanda.ccs.sqlasm.CriteriaParserBuilder.SELECT_PARAGRAPHS;
import static com.wanda.ccs.sqlasm.CriteriaParserBuilder.newParser;
import static com.wanda.ccs.sqlasm.CriteriaParserBuilder.notEmpty;
import static com.wanda.ccs.sqlasm.expression.ExpressionClauseBuilder.newExpression;
import static com.wanda.ccs.sqlasm.expression.ExpressionClauseBuilder.newPlain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import javax.sql.DataSource;

//import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.RowMapper;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.google.code.pathlet.jdbc.EntityInsertDef;
import com.google.code.pathlet.jdbc.EntityRowMapper;
import com.google.code.pathlet.jdbc.ExtJdbcTemplate;
//import com.google.code.pathlet.util.ClassPathResource;
//import com.google.code.pathlet.util.ClassUtils;
import com.google.code.pathlet.vo.QueryParamVo;
import com.google.code.pathlet.vo.QueryResultVo;
import com.wanda.ccs.member.ap2in.UserLevel;
import com.wanda.ccs.member.ap2in.UserProfile;
import com.wanda.ccs.member.segment.MessageSendJob;
import com.wanda.ccs.member.segment.service.SegmentMessageService;
import com.wanda.ccs.member.segment.vo.MessageApproveVo;
import com.wanda.ccs.member.segment.vo.SegmentMessageVo;
import com.wanda.ccs.member.segment.web.SegmentMessageAction;
import com.wanda.ccs.sqlasm.Clause;
import com.wanda.ccs.sqlasm.Condition;
import com.wanda.ccs.sqlasm.CriteriaParser;
import com.wanda.ccs.sqlasm.CriteriaResult;
import com.wanda.ccs.sqlasm.DataType;
import com.wanda.ccs.sqlasm.expression.ArrayExpCriterion;
import com.wanda.ccs.sqlasm.expression.ExpressionCriterion;
//import com.wanda.ccs.sqlasm.expression.JsonCriteriaHelper;
import com.wanda.ccs.sqlasm.expression.Operator;

/**
 * @ClassName: SegmentMessageServiceImpl
 * @Description: 客群短信发送服务类
 * @author 许雷
 * @date 2015年5月21日 上午11:12:37
 *
 */
public class SegmentMessageServiceImpl implements SegmentMessageService {

	private static Log logger = LogFactory.getLog(SegmentMessageAction.class);

	private static Timer timer = new Timer(); // 定时发送短信定时任务对象

	private final String VERSION = "1.0"; // 客群短信版本号

	private final String APPROVEVOVERSION = "1.0";// 审批版本号

	private ExtJdbcTemplate queryTemplate = null;

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ 审批状态汇总   ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * @Fields APPROVE_PIZHUN :提交状态
	 */
	private final String TIJIAO = "1";
	/**
	 * @Fields APPROVE_PIZHUN :保存状态
	 */
	private final String BAOCUN = "0";
	/**
	 * @Fields APPROVE_PIZHUN :审批批准状态
	 */
	private final String APPROVE_PIZHUN = "1";

	/**
	 * @Fields APPROVE_TUIHUI : 审批退回状态
	 */
	private final String APPROVE_TUIHUI = "0";

	/**
	 * @Fields APPROVE_CHEXIAO : 审批撤销状态
	 */
	private final String APPROVE_CHEXIAO = "9";

	/**
	 * @Fields WAIT_C_APPROVE : 待影城经理审批
	 */
	private final String WAIT_C_APPROVE = "1000";

	/**
	 * @Fields WAIT_R_APPROVE : 待区域经理审批
	 */
	private final String WAIT_R_APPROVE = "2000";

	/**
	 * @Fields WAIT_G_APPROVE : 待院线会员经理审批
	 */
	private final String WAIT_G_APPROVE = "3000";

	/**
	 * @Fields BACK_C_APPROVE : 由影城经理退回修改
	 */
	private final String BACK_C_APPROVE = "1999";

	/**
	 * @Fields BACK_R_APPROVE : 区域经理退回修改
	 */
	private final String BACK_R_APPROVE = "2999";

	/**
	 * @Fields BACK_G_APPROVE : 院线经理退回修改
	 */
	private final String BACK_G_APPROVE = "3999";

	/**
	 * @Fields NO_PASS_APPROVE : 不通过状态
	 */
	private final String NO_PASS_APPROVE = "9999";

	/**
	 * @Fields PASS_APPROVE : 通过状态
	 */
	private final String PASS_APPROVE = "9000";

	String status = null;

	private Timestamp time = null;

	Long seqId = null;

	@InstanceIn(path = "/dataSource")
	private DataSource dataSource;

	private ExtJdbcTemplate getJdbcTemplate() {
		if (queryTemplate == null) {
			queryTemplate = new ExtJdbcTemplate(dataSource);
			queryTemplate.registerInsertEntity(new EntityInsertDef("insert",
					SegmentMessageVo.class, "SEGM_MESSAGE"));
			queryTemplate
					.registerInsertEntity(new EntityInsertDef("insertApprove",
							MessageApproveVo.class, "MESSAGE_APPROVE"));
		}

		return this.queryTemplate;
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ SQL string  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //

	/**
	* @Fields NO_SEND_CAL_SQL :计算客群不希望联系人数的SQL
	*/
	private final String NO_SEND_CAL_SQL = "select COUNT(1)\n"
			+ "from T_SEGM_MEMBER SEME\n"
			+ "LEFT JOIN T_MEMBER ME ON SEME.MEMBER_ID = ME.MEMBER_ID \n"
			+ "WHERE ME.ISCONTACTABLE = 0 and SEME.SEGMENT_ID=?";


	/**
	* @Fields NEXT_SEQ_ID_SQL : 获取SEGM_MESSAGE主键的SQL
	*/
	private final String NEXT_SEQ_ID_SQL = "select S_SEGM_MESSAGE.NEXTVAL from DUAL";

	/**
	* @Fields NEXT_APPROVE_ID_SQL : 获取MESSAGE_APPROVE主键的SQL
	*/
	private final String NEXT_APPROVE_ID_SQL = "select S_MESSAGE_APPROVE.NEXTVAL from DUAL";
	/**
	* @Fields SAVE_MESSAGE : 修改SEGM_MESSAGE信息的SQL
	*/
	private final String SAVE_TIJIAO_MESSAGE = "update SEGM_MESSAGE set APPROVE_STATUS=?,CONTENT=?,SEND_TIME=?,UPDATE_BY=?,UPDATE_DATE=?,APPROVER=? where SEGM_MESSAGE_ID=?";
	/**
	* @Fields SAVE_MESSAGE : 修改SEGM_MESSAGE信息的SQL
	*/
	private final String SAVE_BAOCUN_MESSAGE = "update SEGM_MESSAGE set APPROVE_STATUS=?,CONTENT=?,SEND_TIME=?,UPDATE_BY=?,UPDATE_DATE=? where SEGM_MESSAGE_ID=?";

	/**
	* @Fields UPDATE_APPROVE_STATUS : 修改SEGM_MESSAGE审批状态的SQL
	*/
	private final String UPDATE_APPROVE_STATUS = "update SEGM_MESSAGE set APPROVE_STATUS=?,APPROVER=? where SEGM_MESSAGE_ID=?";

	/**
	* @Fields SQL : 获取影城级别审批人账号的SQL
	*/
	private final String CINEMA_SQL = "with regions as (select orgid,orgcode from ehr_wd_org@read_DL where parentunitid = 68468088 	and status = 1) "
			+ "select u.username rtx "
			+ "from ehr_wd_org@read_DL     o, "
			+ "regions                          r, "
			+ "ehr_wd_user@read_DL        u, "
			+ "ehr_wd_user_pos_rel@read_DL prel, "
			+ "ehr_wd_pos@read_DL         p, "
			+ "t_cinema@read_DL            c "
			+ "where o.parentunitid = r.orgid  "
			+ "and o.status = 1  "
			+ "and c.orgcode = o.orgcode "
			+ "and u.employeeStatus = 2 "
			+ "and u.orgid = o.orgid "
			+ "and u.employeeid = prel.employeeid "
			+ "and prel.jobid = p.jobid "
			+ "and p.jobname = '影城经理' and c.seqid=? " 
			+ "and rownum = 1";
	/**
	* @Fields SQL : 获取区域级别审批人账号的SQL
	*/
	private final String REGION_SQL = "with regions as (select orgid,orgcode from ehr_wd_org@read_DL where parentunitid = 68468088 	and status = 1) "
			+ "select u.username rtx "
			+ "from ehr_wd_org@read_DL     o, "
			+ "regions                          r, "
			+ "ehr_wd_user@read_DL        u, "
			+ "ehr_wd_user_pos_rel@read_DL prel, "
			+ "ehr_wd_pos@read_DL         p, "
			+ "t_cinema@read_DL            c "
			+ "where o.parentunitid = r.orgid  "
			+ "and o.status = 1  "
			+ "and c.orgcode = o.orgcode "
			+ "and u.employeeStatus = 2 "
			+ "and u.orgid = o.orgid "
			+ "and u.employeeid = prel.employeeid "
			+ "and prel.jobid = p.jobid "
			+ "	and p.jobname = '区域总经理' and c.area=? " 
			+ "and rownum = 1";
	/**
	* @Fields GROUP_SQL : 获取院线级审批人账号
	*/
	private String GROUP_SQL = "select  u.username " +
			"from  " +
			"ehr_wd_user@read_DL        u,  " +
			"ehr_wd_user_pos_rel@read_DL prel  " +
			"where  u.employeeStatus = 2  " +
			"and u.employeeid = prel.employeeid  " +
			"and prel.jobid =  '1255477183'  " +
			"and rownum = 1"; 
	/**
	* @Fields CREATE_TABLE_SQL : 创建手机号临时表
	*/
	private final String CREATE_TABLE_SQL =  "create table ${tableName} as   (select ME.MOBILE,SEME.SEGMENT_ID  " +
				"from T_SEGM_MEMBER SEME  " + 
				"LEFT JOIN T_MEMBER ME ON SEME.MEMBER_ID = ME.MEMBER_ID  " +    
				"WHERE ME.ISCONTACTABLE <> 0 and SEME.SEGMENT_ID=${SEGMENT_ID})";
	/**
	* @Fields CREATE_TABLE_SQL : 创建手机号临时表
	*/
	private final String DROP_TABLE_SQL =  "drop table ${tableName}";
	/**
	* @Fields CREATE_TABLE_SQL : 创建手机号临时表
	*/
	private final String EXITS_TABLE_SQL =  "select count(*) from  all_tables where table_name = '${tableName}' ";
	
	/**
	* @Fields MOBILE_GET_SQL : 获取客群手机号的SQL
	*/
	private final String MOBILE_GET_SQL = "select MOBILE from ${tableName}";
	/*
	 * (非 Javadoc) <p>Title: insert</p> <p>Description: </p>
	 * 
	 * @return
	 * 
	 * @see com.wanda.ccs.member.segment.service.SegmentMessageService#insert()
	 */

	@Override
	public String insert(SegmentMessageVo entity) {
		seqId = getJdbcTemplate().queryForLong(NEXT_SEQ_ID_SQL);
		Long noSendCal = getJdbcTemplate().queryForLong(NO_SEND_CAL_SQL,
				entity.getSegmentId());
		entity.setSegmMessageId(seqId);
		entity.setNoSendCal(noSendCal);
		entity.setVersion(VERSION);
		entity.setSend_status(-1L);//默认发送状态为未发送
		entity.setCreateDate(new Timestamp(new Date().getTime()));
		getJdbcTemplate().insertEntity("insert", entity);
		String CREATE_TABLE = CREATE_TABLE_SQL.replace("${tableName}", "T_MOIBLE_" + entity.getSegmentId());
		CREATE_TABLE = CREATE_TABLE.replace("${SEGMENT_ID}", entity.getSegmentId());
		String DROP_TABLE = DROP_TABLE_SQL.replace("${tableName}", "T_MOIBLE_" + entity.getSegmentId());
		String EXITS_TABLE = EXITS_TABLE_SQL.replace("${tableName}", "T_MOIBLE_" + entity.getSegmentId());
		int exits = getJdbcTemplate().queryForInt(EXITS_TABLE);
		if(exits!=0){
			getJdbcTemplate().execute(DROP_TABLE);
		}
		getJdbcTemplate().execute(CREATE_TABLE);
		logger.info("A SegmentMessageInfo has been Create SegmentMessageId="
				+ seqId);
		return "DONE";
	}

	/*
	 * (非 Javadoc) <p>Title: insert</p> <p>Description: </p>
	 * 
	 * @return
	 * 
	 * @see com.wanda.ccs.member.segment.service.SegmentMessageService#insert()
	 */

	@Override
	public List<String> getMoible(String segmId) {
		String MOBILE_GET = MOBILE_GET_SQL.replace("${tableName}", "T_MOIBLE_" + segmId);
		List<String> mobileList = getJdbcTemplate().query(MOBILE_GET, new RowMapper<String>() {
					@Override
					public String mapRow(ResultSet arg0, int arg1)
							throws SQLException {
						return arg0.getString("MOBILE");
					}
				});
		return mobileList;
	}

	/*
	 * (非 Javadoc) <p>Title: insertApprove</p> <p>Description: </p>
	 * 
	 * @param entity
	 * 
	 * @param userProfile
	 * 
	 * @return
	 * 
	 * @see
	 * com.wanda.ccs.member.segment.service.SegmentMessageService#insertApprove
	 * (com.wanda.ccs.member.segment.vo.MessageApproveVo,
	 * com.wanda.ccs.member.ap2in.UserProfile)
	 */

	@Override
	public String insertApprove(MessageApproveVo entity, UserProfile userProfile) {
		seqId = getJdbcTemplate().queryForLong(NEXT_APPROVE_ID_SQL);
		entity.setApproveId(seqId);
		entity.setVersion(APPROVEVOVERSION);
		entity.setCreateDate(new Timestamp(new Date().getTime()));
		entity.setApprover(userProfile.getId());
		getJdbcTemplate().insertEntity("insertApprove", entity);
		logger.info("A MessageApproveInfo has been Create MessageApproveId="
				+ seqId);
		return "DONE";
	}

	/*
	 * (非 Javadoc) <p>Title: saveMessage</p> <p>Description: </p>
	 * 
	 * @param entity
	 * 
	 * @param userProfile
	 * 
	 * @return
	 * 
	 * @see
	 * com.wanda.ccs.member.segment.service.SegmentMessageService#saveMessage
	 * (com.wanda.ccs.member.segment.vo.SegmentMessageVo,
	 * com.wanda.ccs.member.ap2in.UserProfile)
	 */
	@Override
	public String saveMessage(SegmentMessageVo entity, UserProfile userProfile, String status) {
		try {
			time = Timestamp.valueOf(entity.getSendTime());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			logger.warn(
					"A MessageApproveInfo has been Create MessageApproveId="
							+ seqId, e);
			return "时间格式不正确";
		}
		if(TIJIAO.endsWith(status)){
			setStatus(entity, userProfile, entity.getApproveStatus());
			getJdbcTemplate().update(SAVE_TIJIAO_MESSAGE, entity.getApproveStatus(),
					entity.getContent(), time, userProfile.getId(),
					new Timestamp(new Date().getTime()), getJingLi(userProfile), entity.getSegmMessageId());
		}else if(BAOCUN.endsWith(status)){
			getJdbcTemplate().update(SAVE_BAOCUN_MESSAGE, entity.getApproveStatus(),
					entity.getContent(), time, userProfile.getId(),
					new Timestamp(new Date().getTime()), entity.getSegmMessageId());
		}
		
		logger.info("A SegmMessageInfo has been update SegmMessageId="
				+ entity.getSegmMessageId());
		return "DONE";
	}

	/**
	 * @Title: setStatus
	 * @Description: 为审批状态添加值
	 * @param @param entity
	 * @param @param userProfile 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private SegmentMessageVo setStatus(SegmentMessageVo entity,
			UserProfile userProfile, String approveStatus) {
		status = entity.getApproveStatus();
		if (status == null||
				status.equals(BACK_C_APPROVE)||
				status.equals(BACK_R_APPROVE)||
				status.equals(BACK_G_APPROVE)) {
			if (UserLevel.CINEMA.equals(userProfile.getLevel())) {
				entity.setApproveStatus(WAIT_C_APPROVE);
			} else if (UserLevel.REGION.equals(userProfile.getLevel())) {
				entity.setApproveStatus(WAIT_R_APPROVE);
			} else {
				entity.setApproveStatus(WAIT_G_APPROVE);
			}
		} else if (WAIT_C_APPROVE.equals(status)) {
			if (APPROVE_PIZHUN.equals(approveStatus)) {
				entity.setApproveStatus(WAIT_G_APPROVE);
			} else if (APPROVE_TUIHUI.equals(approveStatus)) {
				entity.setApproveStatus(BACK_C_APPROVE);
			} else if (APPROVE_CHEXIAO.equals(approveStatus)) {
				entity.setApproveStatus(NO_PASS_APPROVE);
			}
		} else if (WAIT_R_APPROVE.equals(status)) {
			if (APPROVE_PIZHUN.equals(approveStatus)) {
				entity.setApproveStatus(WAIT_G_APPROVE);
			} else if (APPROVE_TUIHUI.equals(approveStatus)) {
				entity.setApproveStatus(BACK_R_APPROVE);
			} else if (APPROVE_CHEXIAO.equals(approveStatus)) {
				entity.setApproveStatus(NO_PASS_APPROVE);
			}
		} else if (WAIT_G_APPROVE.equals(status)) {
			if (APPROVE_PIZHUN.equals(approveStatus)) {
				entity.setApproveStatus(PASS_APPROVE);
			} else if (APPROVE_TUIHUI.equals(approveStatus)) {
				entity.setApproveStatus(BACK_G_APPROVE);
			} else if (APPROVE_CHEXIAO.equals(approveStatus)) {
				entity.setApproveStatus(NO_PASS_APPROVE);
			}
		}
		return entity;
	}

	/*
	 * (非 Javadoc) <p>Title: approve</p> <p>Description: </p>
	 * 
	 * @param entity
	 * 
	 * @param userProfile
	 * 
	 * @param status
	 * 
	 * @return
	 * 
	 * @see
	 * com.wanda.ccs.member.segment.service.SegmentMessageService#approve(com
	 * .wanda.ccs.member.segment.vo.SegmentMessageVo,
	 * com.wanda.ccs.member.ap2in.UserProfile, java.lang.String)
	 */
	@Override
	public String approve(MessageApproveVo entity, UserProfile userProfile,
			String approveStatus) {
		SegmentMessageVo vo = get(entity.getSegmMessageId());
		setStatus(vo, userProfile, approveStatus);
		getJdbcTemplate().update(UPDATE_APPROVE_STATUS, vo.getApproveStatus(),getGroupJingLi(),
				entity.getSegmMessageId());
		setApproveStatus(entity, status, approveStatus);
		insertApprove(entity, userProfile);
		logger.info("A SegmMessageInfo's approveStatus has been update "
				+ "SegmMessageId=" + entity.getSegmMessageId()
				+ "ApproveStatus=" + vo.getApproveStatus());
		return "DONE";
	}

	/**
	 * @Title: setApproveStatus
	 * @Description: 设置审批表审批状态
	 * @param @param entity 审批对象
	 * @param @param status 审批现有状态
	 * @param @param approveStatus 审批状态 是否通过等
	 * @param @return 设定文件
	 * @return MessageApproveVo 返回类型
	 * @throws
	 */
	private MessageApproveVo setApproveStatus(MessageApproveVo entity,
			String status, String approveStatus) {
		if (APPROVE_PIZHUN.equals(approveStatus)) {
			entity.setStatus(Long.valueOf(status) + 1);
		} else if (APPROVE_TUIHUI.equals(approveStatus)) {
			entity.setStatus(Long.valueOf(status) + 999);
		} else if (APPROVE_CHEXIAO.equals(approveStatus)) {
			entity.setStatus(9999L);
		}
		return entity;
	}

	/*
	 * (非 Javadoc) <p>Title: queryList</p> <p>Description: </p>
	 * 
	 * @param queryParam
	 * 
	 * @param criteria
	 * 
	 * @param userinfo
	 * 
	 * @return
	 * 
	 * @see
	 * com.wanda.ccs.member.segment.service.SegmentMessageService#queryList(
	 * com.google.code.pathlet.vo.QueryParamVo, java.util.List,
	 * com.wanda.ccs.member.ap2in.UserProfile)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public QueryResultVo<Map<String, Object>> queryList(
			QueryParamVo queryParam, List<ExpressionCriterion> criteria,
			UserProfile userinfo) {

		criteria.add(new ArrayExpCriterion("orderby", null, null, null,
				new String[] { queryParam.getSortName(),
						queryParam.getSortOrder() }));

		Clause segmentTable = newPlain()
				.in("from")
				.output("T_SEGMENT s")
				.depends(
						newPlain().in("where").output(
								"e.SEGMENT_ID=s.SEGMENT_ID"))
				.depends(newPlain().in("orderby").output("e.create_date desc"));

		Clause segmMessageTable = newPlain().in("from")
				.output("SEGM_MESSAGE e");

		Map<Condition, Clause> clauseMap = new LinkedHashMap<Condition, Clause>();

		clauseMap.put(
				notEmpty("code"),
				newExpression().in("where")
						.output("s.CODE", DataType.STRING, Operator.EQUAL)
						.depends(segmentTable));
		clauseMap.put(
				notEmpty("name"),
				newExpression().in("where")
						.output("s.NAME", DataType.STRING, Operator.LIKE)
						.depends(segmentTable));
		clauseMap.put(
				notEmpty("createBy"),
				newExpression().in("where")
						.output("e.CREATE_BY", DataType.STRING, Operator.EQUAL)
						.depends(segmMessageTable));
		clauseMap.put(notEmpty("approveStatus"), newExpression().in("where")
				.output("e.APPROVE_STATUS", DataType.LONG, Operator.EQUAL)
				.depends(segmMessageTable));

		CriteriaParser countParser = newParser(SELECT_PARAGRAPHS)
				.add(newPlain().in("select").output("count(e.SEGM_MESSAGE_ID)"))
				.add(segmentTable).add(segmMessageTable).add(clauseMap);

		CriteriaResult countResult = countParser.parse(criteria);
		CriteriaParser listParser = newParser(SELECT_PARAGRAPHS)
				.add(newPlain()
						.in("select")
						.output("e.SEGM_MESSAGE_ID, s.SEGMENT_ID, s.CODE, s.NAME, s.CAL_COUNT, s.STATUS , e.CREATE_BY, e.APPROVE_STATUS , e.NO_SEND_CAL,e.SEND_STATUS,e.APPROVER"))
				.add(segmentTable).add(segmMessageTable).add(clauseMap);

		CriteriaResult listResult = listParser.parse(criteria);
		List<Map<String, Object>> listQueryResult = getJdbcTemplate().query(
				queryParam.getStartIndex(), queryParam.getFetchSize(),
				listResult.getComposedText(),
				listResult.getParameters().toArray(), new ColumnMapRowMapper());

		Long rowCount = getJdbcTemplate().queryForLong(
				countResult.getComposedText(),
				countResult.getParameters().toArray());

		return new QueryResultVo<Map<String, Object>>(rowCount, listQueryResult);
	}

	/*
	 * (非 Javadoc) <p>Title: get</p> <p>Description: </p>
	 * 
	 * @param segmentMessageId
	 * 
	 * @return
	 * 
	 * @see
	 * com.wanda.ccs.member.segment.service.SegmentMessageService#get(java.lang
	 * .Long)
	 */
	@Override
	public SegmentMessageVo get(Long segmentMessageId) {
		SegmentMessageVo segment = (SegmentMessageVo) getJdbcTemplate()
				.queryForObject(
						"select * from SEGM_MESSAGE where SEGM_MESSAGE_ID=?",
						new Object[] { segmentMessageId },
						new EntityRowMapper<SegmentMessageVo>(
								SegmentMessageVo.class));
		return segment;
	}

	/**
	 * @Title: getJingLi
	 * @Description: 由操作人信息找到审批人
	 * @param @param userProfile
	 * @param @return
	 * @param @throws IOException 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String getJingLi(UserProfile userProfile){
		if (UserLevel.CINEMA.equals(userProfile.getLevel())) {
			return getJdbcTemplate().queryForObject(CINEMA_SQL,
					new Object[] { userProfile.getCinemaId() }, String.class);
		} else if (UserLevel.REGION.equals(userProfile.getLevel())) {
			return getJdbcTemplate().queryForObject(REGION_SQL,
					new Object[] { userProfile.getRegionCode() }, String.class);
		} else if (UserLevel.GROUP.equals(userProfile.getLevel())) {
			return getGroupJingLi();
		}
		return "";
	}

	/**
	 * @Title: getGroupJingLi
	 * @Description: 由操作人信息找到院线级审批人
	 * @param @param userProfile
	 * @param @return
	 * @param @throws IOException 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String getGroupJingLi() {
			return getJdbcTemplate().queryForObject(GROUP_SQL, new Object[] {},
					String.class);
	}
//	 /**
//	 * @Title: getThisPackageFile
//	 * @Description: 获取当前包路径下文件方法
//	 * @param @param fileName
//	 * @param @return
//	 * @param @throws IOException 设定文件
//	 * @return File 返回类型
//	 * @throws
//	 */
//	 private File getThisPackageFile(String fileName) throws IOException {
//	 ClassLoader cl = this.getClass().getClassLoader();
//	 String packagePath = ClassUtils.getPackageName(this.getClass())
//	 .replace('.', '/');
//	 return (new ClassPathResource(packagePath + "/" + fileName, cl))
//	 .getFile();
//	 }
//	
//	/**
//	 * 获取配置的json对象
//	 *
//	 */
//	public static class JsonDo {
//
//		private String cinema;
//
//		private String region;
//
//		private String group;
//
//		public JsonDo() {
//		}
//
//		public String getCinema() {
//			return cinema;
//		}
//
//		public void setCinema(String cinema) {
//			this.cinema = cinema;
//		}
//
//		public String getRegion() {
//			return region;
//		}
//
//		public void setRegion(String region) {
//			this.region = region;
//		}
//
//		public String getGroup() {
//			return group;
//		}
//
//		public void setGroup(String group) {
//			this.group = group;
//		}
//
//	}

	/*
	 * (非 Javadoc) <p>Title: sendMessage 客群短信定时任务</p> <p>Description: </p>
	 * @param messageSendId
	 * @return
	 * @see
	 * com.wanda.ccs.member.segment.service.SegmentMessageService#sendMessage
	 * (java.lang.String)
	 */
	@Override
	public void sendMessage(Long messageSendId, UserProfile userProfile) {
		SegmentMessageVo messageSendVo = this.get(messageSendId);
		List<String> moibleList = this.getMoible(messageSendVo.getSegmentId());
		MessageSendJob sendJob = null;
		try {
			sendJob = new MessageSendJob(messageSendVo, moibleList,
					dataSource.getConnection());
		} catch (SQLException e) {
			logger.warn("A SQLException at com.wanda.ccs.member.segment.service.impl.SegmentMessageServiceImpl#sendMessage() at row 605 !");
			e.printStackTrace();
		}
		time = Timestamp.valueOf(messageSendVo.getSendTime());
		Long timec = new Date().getTime();
		if (time.getTime() <= timec) {// 如果当前时间大于设定时间，立即调用
			time.setTime(timec);
		}
		timer.schedule(sendJob, time);// 以time为参数，指定某个时间点执行线程
	}
}
