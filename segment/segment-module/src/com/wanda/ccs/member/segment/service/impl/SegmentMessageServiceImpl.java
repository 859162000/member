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
import static com.wanda.ccs.sqlasm.CriteriaParserBuilder.equalsValue;
import static com.wanda.ccs.sqlasm.CriteriaParserBuilder.newParser;
import static com.wanda.ccs.sqlasm.CriteriaParserBuilder.notEmpty;
import static com.wanda.ccs.sqlasm.expression.ExpressionClauseBuilder.newExpression;
import static com.wanda.ccs.sqlasm.expression.ExpressionClauseBuilder.newPlain;
import static com.wanda.ccs.sqlasm.expression.ExpressionClauseBuilder.newValue;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

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
import com.wanda.ccs.member.segment.MessageSendConf;
import com.wanda.ccs.member.segment.MessageSendJob;
import com.wanda.ccs.member.segment.service.SegmentMessageService;
import com.wanda.ccs.member.segment.vo.MessageApproveVo;
import com.wanda.ccs.member.segment.vo.SegmentMessageVo;
import com.wanda.ccs.member.segment.vo.SendLogVo;
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
import com.wanda.ccs.sqlasm.expression.SingleExpCriterion;

/**
 * @ClassName: SegmentMessageServiceImpl
 * @Description: 客群短信发送服务类
 * @author 许雷
 * @date 2015年5月21日 上午11:12:37
 *
 */
public class SegmentMessageServiceImpl implements SegmentMessageService,MessageSendConf {

	private static Log logger = LogFactory.getLog(SegmentMessageAction.class);

	private static Timer timer = null; // 定时发送短信定时任务对象

	private ExtJdbcTemplate queryTemplate = null;

	
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
			queryTemplate
			.registerInsertEntity(new EntityInsertDef("insertLog",
					SendLogVo.class, "MESSAGE_SEND_LOG"));
		}

		return this.queryTemplate;
	}

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
		entity.setUpdateDate(new Timestamp(new Date().getTime()));
		if (entity.getSendTime() != null && entity.getSendTime() != "" && !"null".equals(entity.getSendTime())) {
			time = Timestamp.valueOf(entity.getSendTime());
		}
		getJdbcTemplate().update(INSERT_BAOCUN_MESSAGE, entity.getContent(), entity.getApproveStatus(), entity.getSegmentId(), time, entity.getCreateBy(),entity.getCinema(), 
				entity.getArea(), entity.getUpdateBy(), entity.getCreateDate(),entity.getUpdateDate(),entity.getNoSendCal(),entity.getVersion(),
				entity.getSend_status(),entity.getOccupied(),entity.getAllowModifier(),entity.getApprover(),entity.getBatchId());

//		getJdbcTemplate().insertEntity("insert", entity);
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
			if (entity.getSendTime() != null && entity.getSendTime() != "" && entity.getSendTime() != "null") {
				time = Timestamp.valueOf(entity.getSendTime());
			}
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
		getJdbcTemplate().update(UPDATE_APPROVE_STATUS, vo.getApproveStatus(),"院线会员经理",
				entity.getSegmMessageId());
		setApproveStatus(entity, status, approveStatus);
		insertApprove(entity, userProfile);
		this.sendMessage(entity.getSegmMessageId(), userProfile);
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

		criteria.add(new SingleExpCriterion("userLevel", userinfo.getLevel().name()));
		criteria.add(new ArrayExpCriterion("userInfo",
				new String[] {userinfo.getId(), Long.toString(userinfo.getCinemaId()), userinfo.getRegionCode()}));
		
		criteria.add(new ArrayExpCriterion("orderby", null, null, null,
				new String[] { queryParam.getSortName(),
						queryParam.getSortOrder() }));

		Clause segmentTable = newPlain()
				.in("from")
				.output("T_SEGMENT s")
				.depends(
						newPlain().in("where").output(
								"e.SEGMENT_ID=s.SEGMENT_ID and (e.ISDELETE is null or e.ISDELETE <> '1') "))
				.depends(newPlain().in("orderby").output("e.create_date desc"));

		Clause segmMessageTable = newPlain().in("from")
				.output("SEGM_MESSAGE e");

		Map<Condition, Clause> clauseMap = new LinkedHashMap<Condition, Clause>();

		clauseMap.put(equalsValue("userLevel", "REGION"), newValue().from("userInfo").in("where")
				.output(" e.AREA={2} ", DataType.STRING, false));
		clauseMap.put(equalsValue("userLevel", "CINEMA"), newValue().from("userInfo").in("where")
				.output(" e.CINEMA={1} ", DataType.STRING, false));
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
		clauseMap.put(
				notEmpty("batchId"),
				newExpression().in("where")
						.output("e.BATCH_ID", DataType.STRING, Operator.EQUAL)
						.depends(segmentTable));
		CriteriaParser countParser = newParser(SELECT_PARAGRAPHS)
				.add(newPlain().in("select").output("count(e.SEGM_MESSAGE_ID)"))
				.add(segmentTable).add(segmMessageTable).add(clauseMap);

		CriteriaResult countResult = countParser.parse(criteria);
		CriteriaParser listParser = newParser(SELECT_PARAGRAPHS)
				.add(newPlain()
						.in("select")
						.output("e.SEGM_MESSAGE_ID, s.SEGMENT_ID, s.CODE, s.NAME, s.CAL_COUNT, s.STATUS , e.CREATE_BY, e.APPROVE_STATUS , e.NO_SEND_CAL,e.SEND_STATUS,e.APPROVER,e.SEND_TIME,e.UPDATE_DATE"))
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
			return "影城经理";
		} else if (UserLevel.REGION.equals(userProfile.getLevel())) {
			return "区域经理";
		} else if (UserLevel.GROUP.equals(userProfile.getLevel())) {
			return "院线会员经理";
		}
		return "";
	}
//
//	/**
//	 * @Title: getGroupJingLi
//	 * @Description: 由操作人信息找到院线级审批人
//	 * @param @param userProfile
//	 * @param @return
//	 * @param @throws IOException 设定文件
//	 * @return String 返回类型
//	 * @throws
//	 */
//	public String getGroupJingLi() {
//			return getJdbcTemplate().queryForObject(GROUP_SQL, new Object[] {},
//					String.class);
//	}
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
		MessageSendJob sendJob = null;
		SegmentMessageVo messageSendVo = this.get(messageSendId);
		if (messageSendVo.getSendTime() != null && !"".equals(messageSendVo.getSendTime()) && !"null".equals(messageSendVo.getSendTime())) {
			time = Timestamp.valueOf(messageSendVo.getSendTime());
		} else {
			time = new Timestamp(new Date().getTime());
		}
		
		List<String> moibleList = this.getMoible(messageSendVo.getSegmentId());
		BlockingQueue<String> que = new ArrayBlockingQueue<String>(moibleList.size(), false, moibleList);
		Long timec = new Date().getTime();
		if (time.getTime() <= timec) {// 如果当前时间大于设定时间，立即调用
			time.setTime(timec);
		}
			try {
				sendJob = new MessageSendJob(messageSendVo, que,
						dataSource.getConnection());
			} catch (SQLException e) {
				logger.warn("A SQLException at com.wanda.ccs.member.segment.service.impl.SegmentMessageServiceImpl#sendMessage() at row 723 !");
				e.printStackTrace();
			}
			if(timer==null){
				timer = new Timer();
			}
			timer.schedule(sendJob, time);// 以time为参数，指定某个时间点执行线程
	}

	/* (非 Javadoc)
	* <p>Title: insertSendLog</p>
	* <p>Description: </p>
	* @param entity
	* @param userProfile
	* @return
	* @see com.wanda.ccs.member.segment.service.SegmentMessageService#insertSendLog(com.wanda.ccs.member.segment.vo.SendLogVo, com.wanda.ccs.member.ap2in.UserProfile)
	*/
	@Override
	public String insertSendLog(SendLogVo entity) {
		seqId = getJdbcTemplate().queryForLong(NEXT_MESSAGE_SEND_LOG);
		entity.setSendLogId(seqId);
		entity.setCreateDate(new Timestamp(new Date().getTime()));
		getJdbcTemplate().insertEntity("insertLog", entity);
		logger.info("A SendLogVo has been Create SendLogId="
				+ seqId);
		return "DONE";
	}

	/**
	 * 根据客群短信批次ID查询客群短信列表
	 */
	@Override
	public List<SegmentMessageVo> getSegmentMessageByBatchId(String batchId) {
		StringBuilder sql = new StringBuilder("select * from SEGM_MESSAGE where (ISDELETE is null or ISDELETE <> '1')  and BATCH_ID = ?");
		List<SegmentMessageVo> list = getJdbcTemplate().query(sql.toString(), new Object[] { batchId }, new RowMapper<SegmentMessageVo>() {
			public SegmentMessageVo mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				SegmentMessageVo segmentMessageVo = new SegmentMessageVo();
				segmentMessageVo.setSegmMessageId(rs.getLong("SEGM_MESSAGE_ID"));
				segmentMessageVo.setContent(rs.getString("CONTENT"));
				segmentMessageVo.setApproveStatus(rs.getString("APPROVE_STATUS"));
				segmentMessageVo.setSegmentId(rs.getString("SEGMENT_ID"));
				segmentMessageVo.setSendTime(rs.getString("SEND_TIME"));
				segmentMessageVo.setCreateBy(rs.getString("CREATE_BY"));
				segmentMessageVo.setCinema(rs.getString("CINEMA"));
				segmentMessageVo.setArea(rs.getString("AREA"));
				segmentMessageVo.setUpdateBy(rs.getString("UPDATE_BY"));
				segmentMessageVo.setUpdateDate(rs.getTimestamp("UPDATE_DATE"));
				segmentMessageVo.setCreateDate(rs.getTimestamp("CREATE_DATE"));
				segmentMessageVo.setNoSendCal(rs.getLong("NO_SEND_CAL"));
				segmentMessageVo.setVersion(rs.getString("VERSION"));
				segmentMessageVo.setSend_status(rs.getLong("SEND_STATUS"));
				segmentMessageVo.setOccupied(rs.getString("OCCUPIED"));
				segmentMessageVo.setAllowModifier(rs.getString("ALLOW_MODIFIER"));
				segmentMessageVo.setApprover(rs.getString("APPROVER"));
				segmentMessageVo.setBatchId(rs.getString("BATCH_ID"));
				return segmentMessageVo;
			}
		});
		return list;
	}

	@Override
	public void logicDelete(String[] deletes) {
		StringBuffer sbf = new StringBuffer("");
		for (String segmentIdStr : deletes) {
			Long segmentId = new Long(segmentIdStr.trim());
			sbf.append(segmentId).append(",");
		}
		if (sbf.length() > 0) {
			sbf = sbf.deleteCharAt(sbf.length()-1);
		}
		String ids = sbf.toString();
		getJdbcTemplate().update(DELETE_MESSAGE, ids);
	}
}
