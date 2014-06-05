package com.wanda.ccs.member.segment.web;

import static com.wanda.ccs.sqlasm.expression.JsonCriteriaHelper.parseSimple;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.google.code.pathlet.util.ValueUtils;
import com.google.code.pathlet.vo.QueryResultVo;
import com.google.code.pathlet.web.widget.JqgridQueryConverter;
import com.google.code.pathlet.web.widget.JqgridQueryParamVo;
import com.google.code.pathlet.web.widget.JqgridQueryResult;
import com.google.code.pathlet.web.widget.ResponseLevel;
import com.google.code.pathlet.web.widget.ResponseMessage;
import com.google.code.pathlet.web.widget.ResultRowMapper;
import com.wanda.ccs.jobhub.client.JobScheduleService;
import com.wanda.ccs.member.ap2in.AuthUserHelper;
import com.wanda.ccs.member.ap2in.UserProfile;
import com.wanda.ccs.member.segment.service.FileAttachService;
import com.wanda.ccs.member.segment.service.SegmentExportService;
import com.wanda.ccs.member.segment.service.impl.CodeListServiceImpl;
import com.wanda.ccs.member.segment.vo.FileAttachVo;
import com.wanda.ccs.member.segment.vo.SegmentExportVo;
import com.wanda.ccs.sqlasm.expression.ExpressionCriterion;
import com.wanda.ccs.sqlasm.expression.SingleExpCriterion;

public class SegmentExportAction {
	
	private static Log log = LogFactory.getLog(SegmentExportAction.class);
	
	@InstanceIn(path="SegmentExportService")
	private SegmentExportService segmentExportService;
	
	@InstanceIn(path = "/jobScheduleService")
	JobScheduleService scheduleService;
	
	@InstanceIn(path="CodeListService")  
	private CodeListServiceImpl codeListService;
	
	@InstanceIn(path="FileAttachService")  
	private FileAttachService fileAttachService;
	
	@InstanceIn(path="/request")
	private HttpServletRequest request;
	
	@InstanceIn(path="/response")
	private HttpServletResponse response;

	private Long segmentExportId;
	
	private Long segmentId;
	
	private Long controlCount = -1L;
	
	private String[] deletes;
	
	private String columnSetting; 

	private JqgridQueryParamVo queryParam;
	
	private String queryData;
	
	private Long fileAttachId;

	public Long getSegmentExportId() {
		return segmentExportId;
	}
	public void setSegmentExportId(Long segmentExportId) {
		this.segmentExportId = segmentExportId;
	}
	public Long getSegmentId() {
		return segmentId;
	}
	public void setSegmentId(Long segmentId) {
		this.segmentId = segmentId;
	}
	public String[] getDeletes() {
		return deletes;
	}
	public void setDeletes(String[] deletes) {
		this.deletes = deletes;
	}
	public String getColumnSetting() {
		return columnSetting;
	}
	public void setColumnSetting(String columnSetting) {
		this.columnSetting = columnSetting;
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
	public Long getFileAttachId() {
		return fileAttachId;
	}
	public void setFileAttachId(Long fileAttachId) {
		this.fileAttachId = fileAttachId;
	}
	
	public Long getControlCount() {
		return controlCount;
	}
	public void setControlCount(Long controlCount) {
		this.controlCount = controlCount;
	}
	public JqgridQueryResult<Map<String, Object>> query() throws Exception {

		final UserProfile user = AuthUserHelper.getUser();
		
		List<ExpressionCriterion> criterionList = parseSimple(this.queryData);
		criterionList.add(new SingleExpCriterion("exportUserId", null, null, null, user.getId()));
		
		JqgridQueryConverter converter = new JqgridQueryConverter();
		
		QueryResultVo<Map<String, Object>> result = segmentExportService.queryList(converter.convertParam(queryParam), criterionList);
		
		return converter.convertResult(result, new ResultRowMapper<Map<String, Object>>() {
			public Map<String, Object> convert(Map<String, Object> row) {
				if("1".equals(row.get("COMBINE_SEGMENT"))) {
					row.put("COMBINE_SEGMENT", "复合");
				} else {
					row.put("COMBINE_SEGMENT", "普通");
				}
				
				//设置是否可下载的状态。
				boolean downloadable = false;
				if(SegmentExportVo.STATUS_EXPORTED.equals(row.get("EXPORT_STATUS"))) {
					downloadable = true;
				}
				row.put("DOWNLOADABLE", downloadable);
				toCodeLabel(row, "EXPORT_STATUS", "1029");
				return row;
			}
		});
	}
	
	public List<FileAttachVo> getFiles() throws Exception {
		return segmentExportService.getFiles(segmentExportId);
	}
	
	/**
	 * 下载客群对应的文件
	 * @throws Exception
	 */
	public void downloadFile() throws Exception {
		fileAttachService.downloadFile(request, response, fileAttachId, "UTF-8");
	}
	
	/**
	 * 更新控制组数量
	 * 
	 * @return
	 * @throws Exception
	 */
	/*public ResponseMessage updateControl() throws Exception {
		try {
			if(segmentId != null) {
				controlCount();
				return new ResponseMessage(ResponseLevel.INFO, "更新客群控制组成功！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseMessage(ResponseLevel.ERROR, "更新客群控制组失败！请与系统管理员联系。");
	}*/
	
	/*private void controlCount() throws Exception {
		if(this.controlCount != null && this.controlCount > -1) {
			segmentExportService.updateControl(segmentId, controlCount);
		} else {
			throw new Exception("客群控制组数量格式有错误");
		}
	}*/

	public ResponseMessage insert() throws Exception {
		final UserProfile user = AuthUserHelper.getUser();
		
		if(segmentExportService.getExportingCount(segmentId, user.getId()) > 0) {
			return new ResponseMessage(ResponseLevel.ERROR, "该客群正在导出，请不要重复下载！您可在‘我的客群导出’查找到对应客群的导出状态！");
		}
		
		// 添加控制组数量
		/*try {
			controlCount();
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseMessage(ResponseLevel.ERROR, "该客群导出失败！请与系统管理员联系。");
		}*/
		
		SegmentExportVo vo = new SegmentExportVo();
		vo.setSegmentId(segmentId);
		vo.setColumnSetting(columnSetting);
		vo.setRowCount(0L);
		vo.setExportStatus(SegmentExportVo.STATUS_EXPORTING);
		vo.setFileType("xlsx");
		vo.setExportUserId(user.getId());
		vo.setExportTime(new Timestamp(System.currentTimeMillis()));
		segmentExportService.insert(vo);
		
		boolean succeed = startExportJob(vo.getSegmentExportId());//启动计算客群数量的后台任务
		if(succeed) {
			return new ResponseMessage(ResponseLevel.INFO, "系统已接收了您的导出请求，请进入“我的客群导出”中查看导出状态和下载！");
		} else {
			//删除启动失败客群导出
			segmentExportService.delete(new String[]{vo.getSegmentExportId().toString()});
			return new ResponseMessage(ResponseLevel.ERROR, "该客群导出失败！请与系统管理员联系。");
		}

	}
	
	public ResponseMessage delete() throws Exception {
		for(String segmentExportIdStr : deletes) {
			Long segmentExportId = Long.parseLong(segmentExportIdStr);
			
			SegmentExportVo vo = segmentExportService.get(segmentExportId);
			UserProfile userProfile = AuthUserHelper.getUser();
			if(userProfile != null) {
				if(userProfile.getId().equals(vo.getExportUserId()) == false) {
					return new ResponseMessage(ResponseLevel.ERROR, "您不是创建人，不能删除该客群导出项！'");
				}
			}
			
			if(vo.getExportStatus() == SegmentExportVo.STATUS_EXPORTING) {
				return new ResponseMessage(ResponseLevel.ERROR, "不能删除正在导出的客群导出项！");
			}
		}
		
		segmentExportService.delete(deletes);
		return new ResponseMessage(ResponseLevel.INFO, "客群删除成功！");
	}
	
	/**
	 * 启动导出客群的后台任务
	 * @param segmentId
	 */
	private boolean startExportJob(Long segmentExportId) {
		UserProfile userProfile = AuthUserHelper.getUser();

		Map<String, String> params = new HashMap<String, String>();
		params.put("id", segmentExportId.toString());
		try {
			scheduleService.scheduleJob("SegmentExportJob", "member-jobs", params);
		}
		catch(Exception e) {
			log.error("Failed to start the job segmentExportJob!", e);
			return false;
		}
		
		return true;
	}
	
	private boolean toCodeLabel(Map<String, Object> row, String column, String typeId) {
		boolean changed = false;
		String value = (String)row.get(column);
		if(value != null) {
			String label = codeListService.getCodeList("dimdef", typeId).getEntryNameByValue(value);
			if(ValueUtils.notEmpty(label)) {
				row.put(column, label);
				changed = true;
			}
		}
		return changed;
	}

}