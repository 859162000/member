package com.wanda.ccs.member.segment.web;

import static com.wanda.ccs.member.ap2in.AuthUserHelper.getUser;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataIntegrityViolationException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.code.pathlet.config.anno.InstanceIn;
import com.google.code.pathlet.util.ValueUtils;
import com.google.code.pathlet.vo.QueryResultVo;
import com.google.code.pathlet.web.widget.JqgridQueryConverter;
import com.google.code.pathlet.web.widget.JqgridQueryParamVo;
import com.google.code.pathlet.web.widget.JqgridQueryResult;
import com.google.code.pathlet.web.widget.ResponseLevel;
import com.google.code.pathlet.web.widget.ResponseMessage;
import com.google.code.pathlet.web.widget.ResultRowMapper;
import com.wanda.ccs.member.ap2in.AuthUserHelper;
import com.wanda.ccs.member.ap2in.UserLevel;
import com.wanda.ccs.member.ap2in.UserProfile;
import com.wanda.ccs.member.segment.service.CriteriaQueryResult;
import com.wanda.ccs.member.segment.service.CriteriaQueryService;
import com.wanda.ccs.member.segment.service.SegmentExportService;
import com.wanda.ccs.member.segment.service.SegmentService;
import com.wanda.ccs.member.segment.vo.CombineSegmentSubVo;
import com.wanda.ccs.member.segment.vo.SegmentVo;
import com.wanda.ccs.sqlasm.expression.CompositeValue;
import com.wanda.ccs.sqlasm.expression.ExpressionCriterion;
import com.wanda.ccs.sqlasm.expression.JsonCriteriaHelper;

public class SegmentAction {
	
	private static Log log = LogFactory.getLog(SegmentAction.class);
	
	@InstanceIn(path="SegmentService")
	private SegmentService segmentService;

	@InstanceIn(path="CriteriaQueryService")
	private CriteriaQueryService criteriaQueryService;
	
	@InstanceIn(path="SegmentExportService")
	private SegmentExportService segmentExportService;
	
	private Long segmentId;
	
	private String json; 

	private String criteriaScheme;
	
	private JqgridQueryParamVo queryParam;
	
	private String queryData;

	private String[] deletes;
	
	private String segmentIds;
	
	public Long getSegmentId() {
		return segmentId;
	}
	public void setSegmentId(Long segmentId) {
		this.segmentId = segmentId;
	}
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
	public String getCriteriaScheme() {
		return criteriaScheme;
	}
	public void setCriteriaScheme(String criteriaScheme) {
		this.criteriaScheme = criteriaScheme;
	}
	public String[] getDeletes() {
		return deletes;
	}
	public void setDeletes(String[] deletes) {
		this.deletes = deletes;
	}
	public JqgridQueryParamVo getQueryParam() {
		return queryParam;
	}
	public void setQueryParam(JqgridQueryParamVo queryParam) {
		this.queryParam = queryParam;
	}
	public String getQueryData() {
		return queryData;
	}
	public void setQueryData(String queryData) {
		this.queryData = queryData;
	}
	public String getSegmentIds() {
		return segmentIds;
	}
	public void setSegmentIds(String segmentIds) {
		this.segmentIds = segmentIds;
	}
	public JqgridQueryResult<Map<String, Object>> query() throws Exception {

		//modified by fuby 20130728 start
		final UserProfile userProfile = AuthUserHelper.getUser();
		
		List<ExpressionCriterion> criterionList = JsonCriteriaHelper.parseSimple(this.queryData);
		
		JqgridQueryConverter converter = new JqgridQueryConverter();

		QueryResultVo<Map<String, Object>> result = segmentService.queryList(converter.convertParam(queryParam), criterionList, userProfile);
		//modified by fuby 20130728 end		
		
		final boolean editRight = userProfile.getRights().contains("member.segment.edit");
		final boolean exportRight = userProfile.getRights().contains("member.segment.export");

		return converter.convertResult(result, new ResultRowMapper<Map<String, Object>>() {
			public Map<String, Object> convert(Map<String, Object> row) {
				//设置该行记录是否可以编辑的标志
				boolean editable = false;
				boolean exportAble = false;
				if(editRight == true && userProfile.getId().equals(row.get("CREATE_BY"))) {
					editable = true;
				}
				else {
					String allowModifier = (String)row.get("ALLOW_MODIFIER");
					if(ValueUtils.notEmpty(allowModifier)) {
						List<String> users = Arrays.asList(allowModifier.split(","));
						if(users.contains(userProfile.getId())) {
							editable = true;
						}
					}
				}
				if(exportRight == true){
					exportAble = true;
				}else{
					exportAble = false;
				}
				row.put("EDITABLE", editable);
				row.put("EXPORTABLE", exportAble);
				
				//当STATUS为下边三种时，“实际数量”列显示状态，而非数量。
				String status = (String)row.get("STATUS");
				if(SegmentVo.STATUS_CALCULATING.equals(status)) {
					row.put("CAL_COUNT", "计算中...");
				}
				else if(SegmentVo.STATUS_NONE.equals(status)) {
					row.put("CAL_COUNT", "未计算");
				}
				else if(SegmentVo.STATUS_FAILED.equals(status)) {
					row.put("CAL_COUNT", "计算失败");
				}
				
				if("1".equals(row.get("COMBINE_SEGMENT")) ) {
					row.put("COMBINE_SEGMENT", "复合");
				}
				else {
					row.put("COMBINE_SEGMENT", "普通");
				}
				
				return row;
			}
		});
	}
	
	public ResponseMessage segmentOccupied() throws Exception {
		String occupied = segmentService.segmentOccupied(this.segmentId);
		if(occupied != null && !"0".equals(occupied)) {
			return new ResponseMessage(ResponseLevel.ERROR, "客群正在被占用不能进行计算操作，请稍重试！");
		}
		return new ResponseMessage(ResponseLevel.INFO, "开始进行客群数量计算，请稍后！");
	}
	
	public ResponseMessage calCount() throws Exception {
		try {
			segmentService.updateStartCalCount(this.segmentId);
			return new ResponseMessage(ResponseLevel.INFO, "开始进行客群数量计算，请稍后！");
		}
		catch(Throwable t) {
			log.error("Start calculate count error! segmentId=" + this.segmentId, t);
			return new ResponseMessage(ResponseLevel.WARNING, "客群数量计算失败！请与系统管理员联系。");
		}
	}
	
	public SegmentVo getCount() throws Exception {
		return segmentService.getCount(this.segmentId);
	}
	
	public List<CombineSegmentSubVo> getSelectedSegments() throws Exception {
		List<CombineSegmentSubVo> list = null;
		if(segmentIds != null && !"".equals(segmentIds)) {
			String[] temp = segmentIds.split("\\|");
			Long[] ids = new Long[temp.length];
			for(int i=0,len=temp.length;i<len;i++) {
				ids[i] = Long.parseLong(temp[i]);
			}
			list = segmentService.getSelectedSegment(ids);
		}
		
		return list;
	}
	
	public CombineSegmentDo getSegments() throws Exception {
		Map<String, Object> map = segmentService.getCombineSegments(this.segmentId);
		List<CombineSegmentSubVo> list = (List<CombineSegmentSubVo>) map.get("sub");
		SegmentVo segmentVo = (SegmentVo) map.get("vo");
		CombineSegmentDo combineSegmentDo = new CombineSegmentDo(segmentVo);
		combineSegmentDo.setControlCount(segmentVo.getControlCount());
		combineSegmentDo.setControlCountRate(segmentVo.getControlCountRate());
		combineSegmentDo.setCombineSegmentsList(list);
//		combineSegmentDo.setCode(segmentVo.getCode());
//		combineSegmentDo.setName(segmentVo.getName());
//		combineSegmentDo.setCalCount(segmentVo.getCalCount());
//		combineSegmentDo.setCalCountTime(segmentVo.getCalCountTime());
//		combineSegmentDo.setSegmentId(this.segmentId);
//		combineSegmentDo.setVersion(segmentVo.getVersion());
		
		return combineSegmentDo;
	}
	
	public ResponseMessage resetStatus() throws Exception {
		boolean succeed = segmentService.updateResetStatus(this.segmentId);
		if(succeed) {
			return new ResponseMessage(ResponseLevel.INFO, "该客群状态成功重置为'未计算'状态！");
		} else {
			return new ResponseMessage(ResponseLevel.ERROR, "该客群状态成功重置失败！可能该客群状态已经变更为'使用中'或'计算完成'");
		}
	}


	public ResponseMessage delete() throws Exception {
		for(String segmentIdStr : deletes) {
			Long segmentId = Long.parseLong(segmentIdStr);
			
			SegmentVo vo = segmentService.get(segmentId);
			UserProfile userProfile = AuthUserHelper.getUser();
			if(userProfile != null) {
				if(userProfile.getId().equals(vo.getCreateBy()) == false) {
					return new ResponseMessage(ResponseLevel.ERROR, "您不是创建人，不能删除客群:'" + vo.getName() + "'");
				}
			}
			
			if(segmentService.hasReferenceSegment(segmentId)) {
				return new ResponseMessage(ResponseLevel.ERROR, "客群:'" + vo.getName() + "' 已经被引用不能删除！");
			}
		}
		
		segmentService.logicDelete(deletes);
		return new ResponseMessage(ResponseLevel.INFO, "客群删除成功！");
	}

	public SegmentDo get() throws Exception {
		SegmentVo vo = segmentService.get(segmentId);
		if(vo != null) {
			SegmentDo segment = new SegmentDo(vo);
			
			//设置权限控制标准位allowModify，如果为true表示允许修改。
			//目前控制为创建人，和授权修改人可以进行修改，其他人只能查看。
			UserProfile userProfile = AuthUserHelper.getUser();
			if(userProfile != null) {
				if(userProfile.getId().equals(segment.getCreateBy())) {
					segment.setAllowModify(true);
				}
				else {
					List<String> allowedUsers = segment.getCompAllowModifier().getSelections().getValue();
					for(String userLoginId : allowedUsers) {
						if(userProfile.getId().equals(userLoginId)) {
							segment.setAllowModify(true);
							break;
						}
					}
				}
			}
			else if(ValueUtils.isEmpty(segment.getCreateBy())){
				segment.setAllowModify(true); //用于开发测试使用，当创建用户为空时的可群也可以被修改。
			}
			
			return segment;
		}
		else {
			return null;
		}
	}
	
	public CriteriaQueryResult getCriteriaResult() throws Exception {
		SegmentVo vo = segmentService.get(segmentId);
		CriteriaQueryResult result = criteriaQueryService.getSegmentQuery(vo.getCriteriaScheme(), vo.getSortName(), vo.getSortOrder());
		return result;
	}
	
	/**
	 * 客群符合处理
	 * 
	 * @return
	 * @throws Exception
	 */
	public ResponseMessage insertSegments() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		Map map = mapper.readValue(json, Map.class);
		
		// 客群活动
		SegmentVo vo = new SegmentVo();
		UserProfile user = getUser();
		if(user == null) {
			throw new DataIntegrityViolationException("Failed to found the user profile in current thread!");
		}
		vo.setCreateBy(user.getId());
		vo.setUpdateBy(user.getId());
		vo.setName(map.get("name").toString());	//客群名称
		vo.setCombineSegment(true);				//复合客群
		vo.setControlCountRate(Integer.parseInt(map.get("controlRate").toString()));
		vo.setMaxCount(99999999L);
		vo.setSortName("00");
		vo.setSortOrder("acs");
		vo.setCriteriaScheme("combine");
		SegmentDo segment = new SegmentDo(vo);
		segment.convertAllowModifier();
		if(segmentService.hasSameName(segment.getName(), null)) {
			return new ResponseMessage(ResponseLevel.ERROR, "已有相同名称的客群，请修改名称！");
		} else {
			UserProfile userProfile = AuthUserHelper.getUser();
			if(userProfile != null) {
				//新建用户时保存用户的级别，区域，所属影城信息
				segment.setOwnerLevel(userProfile.getLevelName());
				if(UserLevel.CINEMA == userProfile.getLevel()) {
					segment.setOwnerRegion((userProfile.getRegionCode()));
					segment.setOwnerCinema(userProfile.getCinemaId());
				}
				else if(UserLevel.REGION == userProfile.getLevel()) {
					segment.setOwnerRegion((userProfile.getRegionCode()));
				}
			}
			segment.setCalCount(-1L);
			
			String[] ids = map.get("ids").toString().split("\\|");
			Long[] segmentIds = new Long[ids.length];
			for(int i=0,len=ids.length;i<len;i++) {
				segmentIds[i] = Long.parseLong(ids[i]);
			}
			String[] types = map.get("types").toString().split("\\|");
			List<CombineSegmentSubVo> list = segmentService.getSelectedSegment(segmentIds);
			if(list != null && list.size() > 0) {
				CombineSegmentSubVo combineSegment = null;
				for(int i=0,len=list.size();i<len;i++) {
					combineSegment = list.get(i);
					combineSegment.setSortIndex(i);								//子客群排序
					combineSegment.setSetRelation(types[i]);					//设置操作关系
					combineSegment.setCountAlter(-1L);							//差值
					combineSegment.setCreateBy(user.getId());					//创建人
					combineSegment.setUpdateBy(user.getId());					//更新人
					Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
					combineSegment.setCreateDate(timeStamp);
					combineSegment.setUpdateDate(timeStamp);
					combineSegment.setVersion(0L);
					combineSegment = null;
				}
				segmentService.insertCombineSegment(segment, list);
				
				try {
					segmentService.updateStartCalCount(segment.getSegmentId());
					return new VersionResponseMessage(ResponseLevel.INFO, "{segmentId:'"+segment.getSegmentId()+"',message:'客群保存成功！'}", segment.getVersion());
				}
				catch(Throwable t) {
					log.error("Start calculate count error! segmentId=" + this.segmentId, t);
					return new VersionResponseMessage(ResponseLevel.WARNING, "客群已保存成功，但计算客群任务未能正确执行！请与系统管理员联。", segment.getVersion());
				}
			} else {
				return new ResponseMessage(ResponseLevel.ERROR, "子客群保存失败！");
			}
		}
	}
	
	public ResponseMessage updateSegments() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			Map obj = mapper.readValue(json, Map.class);
			UserProfile user = getUser();
			if(user == null) {
				throw new DataIntegrityViolationException("Failed to found the user profile in current thread!");
			}
			
			// 复合客群活动
			Object segmentId = obj.get("segmentId");
			if(segmentId != null) {
				SegmentVo vo = segmentService.get(Long.parseLong(segmentId.toString()));
				if(vo.getOccupied() != null && !"0".equals(vo.getOccupied())) {	//该客群正在被占用
					return new VersionResponseMessage(ResponseLevel.ERROR, "客群正在被占用,不能进行修改！", vo.getVersion());
				}
				
				Map<String, Object> map = segmentService.getCombineSegments(Long.parseLong(segmentId.toString()));
				//SegmentVo vo = (SegmentVo) map.get("vo");	//主客群
				List<CombineSegmentSubVo> sub = (List<CombineSegmentSubVo>) map.get("sub");	//子客群
				Map<String, CombineSegmentSubVo> subMap = new HashMap<String, CombineSegmentSubVo>();
				for(int i=0,len=sub.size();i<len;i++) {
					subMap.put(sub.get(i).getSubSegmentId()+"", sub.get(i));
				}
				sub = null;
				List<CombineSegmentSubVo> newSub = new ArrayList<CombineSegmentSubVo>();	//修改后的新子客群
				String[] ids = obj.get("ids").toString().split("\\|");
				String[] types = obj.get("types").toString().split("\\|");
				String[] conTypes = obj.get("conTypes").toString().split("\\|");
				
				CombineSegmentSubVo subVo = null;
				boolean calFlag = false;	//后面的数据是否需要重新计算
				for(int i=0,len=ids.length;i<len;i++) {
					subVo = subMap.get(ids[i]);
					if(subVo != null) { //找到以前添加过的客群
						if("new".equals(conTypes[i])) {	//用户将以前的子客群删除后又添加了相同的子客群，这时需要重新添加子客群
							subVo = null;
						} else {
							subMap.remove(ids[i]);	//移除掉子客群并进行修改
							if(!calFlag) {
								if(!types[i].equals(subVo.getSetRelation()==null?"":subVo.getSetRelation()) || i != subVo.getSortIndex()) {	//客群操作关系或客群顺序发生变化
									calFlag = true;
								}
							}
							subVo.setSortIndex(i);				//更新顺序
							subVo.setSetRelation(types[i]);		//更新客群操作
							if(calFlag) {
								subVo.setCountAlter(-1L);		//复合后数据变化重新计算差值
							}
							subVo.setUpdateBy(user.getId());	//更新人
							subVo.setUpdateDate(new Timestamp(System.currentTimeMillis()));	//更新时间
							subVo.setVersion(subVo.getVersion()+1);
							subVo.setControlStatus("edit");
						}
					}
					
					if(subVo == null) {
						calFlag = true;		//后面的客群都需要重新计算
						//查询新添加的子客群
						SegmentVo segmentVo = segmentService.get(Long.parseLong(ids[i]));
						subVo = new CombineSegmentSubVo();
						subVo.setSegmentId(Long.parseLong(segmentId.toString()));
						subVo.setSubSegmentId(segmentVo.getSegmentId());	//子客群ID
						subVo.setSortIndex(i);								//子客群排序
						subVo.setCalCount(segmentVo.getCalCount());			//子客群计算数量
						subVo.setCalCountTime(segmentVo.getCalCountTime());	//子客群计算时间
						subVo.setSegmentVersion(segmentVo.getVersion());	//对应的客群版本
						subVo.setSetRelation(types[i]);						//设置操作关系
						subVo.setCountAlter(-1L);							//复合后数据变化重新计算差值
						subVo.setCreateBy(user.getId());					//创建人
						subVo.setUpdateBy(user.getId());					//更新人
						Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
						subVo.setCreateDate(timeStamp);
						subVo.setUpdateDate(timeStamp);
						subVo.setVersion(0L);								//子客群版本
						subVo.setControlStatus("new");
					}
						
					// 添加到新的子客群活动中
					newSub.add(subVo);
					subVo = null;
				}
				
				// 判断是否有需要删除的子客群
				if(subMap.size() > 0) {	//传过来的segmentId不存在，说明有有需要删除的子客群
					for(Map.Entry<String, CombineSegmentSubVo> entry : subMap.entrySet()) {
						subVo  = entry.getValue();
						subVo.setControlStatus("delete");
						newSub.add(subVo);
						subVo = null;
					}
				}
				
				//String version = (String) obj.get("version");
				// 更新主客群版本号
				//vo.setVersion(Long.parseLong(version)+1);
				if(calFlag) {	//修改子客群有变化情况，重新计算主客群数量
					vo.setCalCount(-1L);
					vo.setCalCountTime(null);
//				vo.setStatus(SegmentVo.STATUS_NONE);
				}
				//vo.setUpdateBy(user.getId());
				//vo.setUpdateDate(new Timestamp(System.currentTimeMillis()));
				CombineSegmentDo segmentDo = new CombineSegmentDo(vo);
				Long oldControlNum = segmentDo.getControlCount();
				int rate = Integer.parseInt(obj.get("controlRate").toString());
				segmentDo.setControlCountRate(rate);
				Long controlNum = segmentDo.getCalCount() / 100 * rate;
				if(controlNum < 1) {
					controlNum = 0L;
				}
				segmentDo.setControlCount(controlNum);
				segmentDo.setCombineSegmentsList(newSub);
				segmentService.updateCombineSegment(segmentDo);
				
				segmentExportService.updateControl(Long.parseLong(segmentId.toString()), oldControlNum, controlNum);
				
				return new ResponseMessage(ResponseLevel.INFO, "客群更新成功！");
			} else {
				return new ResponseMessage(ResponseLevel.ERROR, "客群更新失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseMessage(ResponseLevel.ERROR, "客群更新失败！");
		}
	}
	
	public ResponseMessage updateCountAlter() throws Exception {
		ByteArrayOutputStream out = null;
		try {
			List<Map<String, Object>> list = segmentService.getCountAlter(segmentId);
			if(list != null && list.size() > 0) {
				ObjectMapper mapper = new ObjectMapper();
				out = new ByteArrayOutputStream();
				mapper.writeValue(out, list);
				out.close();
				String json = new String(out.toByteArray());
				System.out.println(json);
				return new ResponseMessage(ResponseLevel.INFO, json);
			} else {
				return new ResponseMessage(ResponseLevel.ERROR, "计算获得失去异常！");
			}
		} finally {
			if(out != null) out.close();
		}
	}

	public ResponseMessage insert() throws Exception {
		SegmentDo segment = JsonCriteriaHelper.parseSimple(json, SegmentDo.class);
		segment.setCriteriaScheme(criteriaScheme);
		
		segment.convertAllowModifier();

		if(segmentService.hasSameName(segment.getName(), null)) {
			return new ResponseMessage(ResponseLevel.ERROR, "已有相同名称的客群，请修改名称！");
		}
		else {
			UserProfile userProfile = AuthUserHelper.getUser();
			if(userProfile != null) {
				//added by fuby 20130728 start
				//新建用户时保存用户的级别，区域，所属影城信息
				segment.setOwnerLevel(userProfile.getLevelName());
				if(UserLevel.CINEMA == userProfile.getLevel()) {
					segment.setOwnerRegion((userProfile.getRegionCode()));
					segment.setOwnerCinema(userProfile.getCinemaId());
				}
				else if(UserLevel.REGION == userProfile.getLevel()) {
					segment.setOwnerRegion((userProfile.getRegionCode()));
				}
				//added by fuby 20130728 end
			}
			segment.setCalCount(-1L);
			segmentService.insert(segment);
			
			try {
				segmentService.updateStartCalCount(segment.getSegmentId());
				return new VersionResponseMessage(ResponseLevel.INFO, "客群保存成功！", segment.getVersion());
			}
			catch(Throwable t) {
				log.error("Start calculate count error! segmentId=" + this.segmentId, t);
				return new VersionResponseMessage(ResponseLevel.WARNING, "客群已保存成功，但计算客群任务未能正确执行！请与系统管理员联。", segment.getVersion());
			}
			
		}
	}
	
	public ResponseMessage update() throws Exception {
		SegmentDo segment = JsonCriteriaHelper.parseSimple(json, SegmentDo.class);
		segment.setCriteriaScheme(criteriaScheme);
		segment.convertAllowModifier();
		
		if(segmentService.hasSameName(segment.getName(), segment.getSegmentId())) {
			return new ResponseMessage(ResponseLevel.ERROR, "已有相同名称的客群，请修改名称！");
		}
		else {
			SegmentVo old = segmentService.get(segment.getSegmentId());
			Long controlNum = old.getCalCount() / 100 *  segment.getControlCountRate();
			if(controlNum < 1) {
				controlNum = 0L;
			}
			segment.setControlCount(controlNum);
			segmentService.update(segment);
			if(old.getControlCount() != controlNum) {
				segmentExportService.updateControl(segment.getSegmentId(), old.getControlCount(), controlNum);
			}
			
			return new VersionResponseMessage(ResponseLevel.INFO, "客群更新成功！", segment.getVersion());
		}
	}
	

	/**
	 * 用于和界面交互时的客群对象
	 * @author Charlie Zhang
	 *
	 */
	public static class SegmentDo extends SegmentVo {
		
		private static final long serialVersionUID = -7945272703287772550L;

		private CompositeValue compAllowModifier;
		
		private boolean allowModify = false;
		
		public SegmentDo() {
			this.compAllowModifier = new CompositeValue();
		}

		public SegmentDo(SegmentVo vo) throws Exception {
			super();
			PropertyUtils.copyProperties(this, vo);
			String usersStr = vo.getAllowModifier();
			
			if(ValueUtils.notEmpty(usersStr)) {
				List<String> users = Arrays.asList(usersStr.split(","));
				this.compAllowModifier = new CompositeValue(users, users);
			}
			else {
				this.compAllowModifier = new CompositeValue();
			}
		}

		
		//compAllowModifier 
		public void convertAllowModifier () {
			List<String> sels = compAllowModifier.getSelections().getValue();
			if(sels != null && sels.size() > 0) {
				boolean first = true;
				StringBuilder buf = new StringBuilder();
				for(String user : sels) {
					if(first) {
						buf.append(user);
						first = false;
					}
					else {
						buf.append(",").append(user);
					}
				}
				this.setAllowModifier(buf.toString());
			}
		}

		public CompositeValue getCompAllowModifier() {
			return compAllowModifier;
		}

		public void setCompAllowModifier(CompositeValue compAllowModifier) {
			this.compAllowModifier = compAllowModifier;
		}

		public boolean isAllowModify() {
			return allowModify;
		}

		public void setAllowModify(boolean allowModify) {
			this.allowModify = allowModify;
		}

	}
	
	
	/**
	 * 用于和界面交互时的复合客群对象
	 * @author
	 *
	 */
	public static class CombineSegmentDo {
		/* 主客群ID */
		private Long segmentId;
		/* 主客群名称 */
		private String name;
		/* 主客群编码 */
		private String code;
		/* 主客群计算数量 */
		private Long calCount;
		/* 主客群计算时间 */
		private Timestamp calCountTime;
		private Long controlCount = -1L;
		private Integer controlCountRate = 10;
		private String status;
		/* 更新人 */
		private String updateBy;
		/* 更新时间 */
		private Timestamp updateDate;
		/*  */
		private String allowModifier;
		/* 复合客群 */
		private boolean combineSegment;
		private String configVersion;
		private String criteriaScheme = "";
		/* 最大数量 */
		private Long maxCount;
		/*  */
		private String sortName;
		/*  */
		private String sortOrder;
		/* 版本号 */
		private Long version;
		/* 包含的子客群 */
		private List<CombineSegmentSubVo> combineSegmentsList;
		
		public CombineSegmentDo() {}
		
		public CombineSegmentDo(SegmentVo vo) {
			this.segmentId = vo.getSegmentId();
			this.name = vo.getName();
			this.code = vo.getCode();
			this.calCount = vo.getCalCount();
			this.calCountTime = vo.getCalCountTime();
			this.controlCount = vo.getControlCount();
			this.controlCountRate = vo.getControlCountRate();
			this.status = vo.getStatus();
			this.updateBy = vo.getUpdateBy();
			this.updateDate = vo.getUpdateDate();
			this.version = vo.getVersion();
			this.criteriaScheme = vo.getCriteriaScheme();
			this.allowModifier= vo.getAllowModifier();
			this.configVersion = vo.getConfigVersion();
			this.combineSegment = vo.getCombineSegment();
			this.maxCount = vo.getMaxCount();
			this.sortName = vo.getSortName();
			this.sortOrder = vo.getSortOrder();
		}
		
		/**
		 * 获取主客群实体
		 * 
		 * @return
		 */
		public SegmentVo getSegmentVo() {
			SegmentVo vo = new SegmentVo();
			vo.setSegmentId(segmentId);
			vo.setName(name);
			vo.setCriteriaScheme(criteriaScheme);
			vo.setAllowModifier(allowModifier);
			vo.setConfigVersion(configVersion);
			vo.setCombineSegment(combineSegment);
			vo.setMaxCount(maxCount);
			vo.setSortName(sortName);
			vo.setSortOrder(sortOrder);
			vo.setCalCount(calCount);
			vo.setCalCountTime(calCountTime);
			vo.setControlCount(controlCount);
			vo.setControlCountRate(controlCountRate);
			vo.setStatus(status);
			vo.setUpdateBy(updateBy);
			vo.setUpdateDate(updateDate);
			vo.setVersion(version);
			return vo;
		}
		
		public Long getSegmentId() {
			return segmentId;
		}
		public void setSegmentId(Long segmentId) {
			this.segmentId = segmentId;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public Long getCalCount() {
			return calCount;
		}
		public void setCalCount(Long calCount) {
			this.calCount = calCount;
		}
		public java.sql.Timestamp getCalCountTime() {
			return calCountTime;
		}
		public void setCalCountTime(java.sql.Timestamp calCountTime) {
			this.calCountTime = calCountTime;
		}
		public List<CombineSegmentSubVo> getCombineSegmentsList() {
			return combineSegmentsList;
		}
		public void setCombineSegmentsList(List<CombineSegmentSubVo> combineSegmentsList) {
			this.combineSegmentsList = combineSegmentsList;
		}
		public Long getVersion() {
			return version;
		}
		public void setVersion(Long version) {
			this.version = version;
		}

		public String getUpdateBy() {
			return updateBy;
		}

		public void setUpdateBy(String updateBy) {
			this.updateBy = updateBy;
		}

		public Timestamp getUpdateDate() {
			return updateDate;
		}

		public void setUpdateDate(Timestamp updateDate) {
			this.updateDate = updateDate;
		}

		public String getAllowModifier() {
			return allowModifier;
		}

		public void setAllowModifier(String allowModifier) {
			this.allowModifier = allowModifier;
		}

		public boolean isCombineSegment() {
			return combineSegment;
		}

		public void setCombineSegment(boolean combineSegment) {
			this.combineSegment = combineSegment;
		}

		public String getConfigVersion() {
			return configVersion;
		}

		public void setConfigVersion(String configVersion) {
			this.configVersion = configVersion;
		}

		public String getCriteriaScheme() {
			return criteriaScheme;
		}

		public void setCriteriaScheme(String criteriaScheme) {
			this.criteriaScheme = criteriaScheme;
		}

		public Long getMaxCount() {
			return maxCount;
		}

		public void setMaxCount(Long maxCount) {
			this.maxCount = maxCount;
		}

		public String getSortName() {
			return sortName;
		}

		public void setSortName(String sortName) {
			this.sortName = sortName;
		}

		public String getSortOrder() {
			return sortOrder;
		}

		public void setSortOrder(String sortOrder) {
			this.sortOrder = sortOrder;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public Long getControlCount() {
			return controlCount;
		}

		public void setControlCount(Long controlCount) {
			this.controlCount = controlCount;
		}

		public Integer getControlCountRate() {
			return controlCountRate;
		}

		public void setControlCountRate(Integer controlCountRate) {
			this.controlCountRate = controlCountRate;
		}
		
	}
	
}