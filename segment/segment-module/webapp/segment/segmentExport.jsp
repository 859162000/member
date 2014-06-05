<%@ page import="com.wanda.ccs.member.ap2in.*" %>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%
boolean fromAp2 = AuthUserHelper.isFromAp2(request);//是否为AP2发来的请求
String context;
if(fromAp2) {
	context = "/ap2/proxy";
}
else {
	context = request.getContextPath();
}
%>

<% if(fromAp2 == false) { %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>我的客群下载</title>

<jsp:include page="include.jsp">
<jsp:param name="context" value="<%=context%>"/>
<jsp:param name="groups" value="ap2-jquery,ap2-jquery-ui,,jqgrid,validation,ztree,datetime,multiselect,common,wrender"/>
</jsp:include>
<% } else { %>
<jsp:include page="include.jsp">
<jsp:param name="context" value="<%=context%>"/>
<jsp:param name="groups" value="jqgrid,validation,ztree,datetime,multiselect,common,wrender"/>
</jsp:include>
<% } %>

<script type="text/javascript" src="<%=context%>/segment/criteria-scheme.js"></script>
<script type="text/javascript" src="<%=context%>/segment/segment.wrender.ext.js"></script>
<script type="text/javascript" src="<%=context%>/segment/segment.settings.member.js"></script>


<style type="text/css">
/*自动扩展行高*/
.ui-jqgrid tr.jqgrow td {
  white-space: normal !important;
  height:auto;
  /*vertical-align:text-top;*/
  padding-top:2px;
 }
/*调整分页下拉框高度*/
.ui-jqgrid .ui-pg-selbox {
    display: block;
    font-size: 0.8em;
    height: 21px;
    line-height: 18px;
    margin: 0;
}
</style>

<script type="text/javascript">
$(function() {
	var resultList = $('#resultList');
	var viewDialog = $('#viewDialog');
	var viewForm = $('#viewForm');
	var downloadDialog = $('#downloadDialog');
	var downloadForm = $('#downloadForm');
	var searchForm = $('#searchForm');
	
	var getFilesUrl = '<%=context%>/segment/SegmentExportAction/getFiles.do';
	var queryUrl = '<%=context%>/segment/SegmentExportAction/query.do';
	var getSegmentUrl = '<%=context%>/segment/SegmentAction/get.do';
	var downloadFileUrl = '<%=context%>/segment/SegmentExportAction/downloadFile.do';
	var deleteUrl = '<%=context%>/segment/SegmentExportAction/delete.do';
	
	//To indicate the open user input dialog is new creation or not. 
	var girdColumns = [
						{name:'SEGMENT_EXPORT_ID', label:'客群导出ID', hidden:true},
						{name:'SEGMENT_ID', label:'客群ID', hidden:true},
						{name:'COLUMN_SETTING', label:'导出列设置', hidden:true},
						{name:'DOWNLOADABLE', label:'DOWNLOADABLE', hidden:true},
						{name:'CODE', label:'客群编码', width:70, sortable:true},
						{name:'NAME', label:'客群名称', width:110, sortable:true},
						{name:'CREATE_BY', label:'创建人', width:60, sortable:false},
						{name:'UPDATE_BY', label:'更新人', width:60, sortable:false},
						{name:'EXPORT_STATUS', label:'导出状态', width:70, sortable:true},
						{name:'EXPORT_TIME', label:'导出时间', width:70, sortable:true},
						{name:'ACTIONS', label:'操作', width:80, align:'center'}
						];
	
	resultList.jqGrid({
		datatype: 'local',
		ajaxGridOptions: {type:"POST"},
		jsonReader: {repeatitems: false},
		prmNames: {page:"queryParam.page", rows:"queryParam.rows", sort:"queryParam.sort", order: "queryParam.order"},
		height: "100%",
		rowNum: 10,
		rowList:[10,20,30],
		autowidth:true,
		shrinkToFit:true,
		multiselect: false,
		viewrecords: true,
		url:queryUrl,
		colModel: girdColumns,
		pager: $("#resultPager"),
		sortname: 'EXPORT_TIME',
		sortorder: "desc",
		afterInsertRow: function(rowid, rowdata, rowelem) {
			//加入每行后的操作按钮
			var cellHtml = '<button name="onViewBtn" key=' + rowdata['SEGMENT_ID'] + ' type="button" wrType="button" wrParam="icon:ui-icon-zoomin;text:false" style="height:22px;" title="查看"/>&nbsp;';
			cellHtml += '<button name="onDownloadBtn" key=' + rowid + ' type="button" wrType="button" wrParam="icon:ui-icon-arrowthickstop-1-s;text:false" style="height:22px;" title="下载"/>&nbsp;';

			//if(rowdata['EXPORT_STATUS'] != '正在导出') {
				cellHtml += '<button name="onDeleteBtn" key=' + rowdata['SEGMENT_EXPORT_ID'] + ' type="button" wrType="button" wrParam="icon:ui-icon-trash;text:false" style="height:22px;" title="删除"/>';
			//}
			
			$(this).setCell(rowid, 'ACTIONS', cellHtml);
			$('button[wrType=button]', this).wrender();
		}
	});
	

	viewDialog.dialog({
		width: 730,
		buttons: [
			{text:'关闭', click: function() { $(this).dialog('close'); }}
		]
	});
	
	downloadDialog.dialog({
		width: 730,
		buttons: [
			{text:'关闭', click: function() { $(this).dialog('close'); }}
		]
	});
	

	
	$('[wrType]', searchForm).wrender();
	$('[wrType]', viewForm).wrender();
	
	$('#onSearchBtn').click(reloadSearch);
	

	$(resultList).on('click', 'button[name=onViewBtn]', function () {
		var segmentId = $(this).attr('key');
        var queryString = 'segmentId=' + segmentId;
           
		$.ajax({
		   url: getSegmentUrl,
		   data: queryString,
		   success: function(result) {
				//如果是-1标示为'计算中...'
				if(result.calCount == -1) { 
					result.calCount = '计算中...';
				}
				
				$('[wrType]', viewForm).wrender('toReadOnly', result, true);
				
				//隐藏或显示sortOrder录入域
				var sortOrderElm = $('select[name="sortOrder"]', viewForm).prev('span[name=readlabel]');
				if(result.sortName == '00')  {
					sortOrderElm.hide();
				}
				else {
					sortOrderElm.show();
				}
				
				var schemeObj = $.parseJSON(result.criteriaScheme);
				schemeAction.applySchemeView($.segment.settings.member, schemeObj);
				viewDialog.dialog('open');
		   }
		});
	});

	$(resultList).on('click', 'button[name=onDownloadBtn]', function () {
		var rowid = $(this).attr('key');
		var rowData = resultList.getRowData(rowid);
		var segmentExportId = rowData['SEGMENT_EXPORT_ID'];
		
		//拼装导出列的内容
		var columns = $.parseJSON(rowData['COLUMN_SETTING']);
		var colLables = '';
		$.each(columns, function(idx, column) {
			if(idx != 0) {
				colLables += ', ';
			}
			colLables += column.label;
		});
		
		//在界面上赋值
		$('[wrType]', downloadForm).wrender('setValue',  {
			segmentExportId: segmentExportId,
			code: rowData['CODE'],
			name: rowData['NAME'],
			exportStatus: rowData['EXPORT_STATUS'],
			columnLabels: colLables
		});
		
		//得到下载列表
		$.ajax({
			url: getFilesUrl,
			data: 'segmentExportId=' + segmentExportId,
			success: function(result) {
				var out;
				if(result.length == 0) {
					out = '<td align="left" colspan="4"><strong>没有下载文件！</strong></td>';
				}
				else {
					out = '<td align="right">下载文件列表：</td><td align="left" colspan="3">';
					for(var i=0 ; i<result.length ; i++) {
						var f = result[i];
						out += '<a href="' + downloadFileUrl + '?fileAttachId=' + f.fileAttachId + '" target="_blank">' + f.fileName + '</a>&nbsp;&nbsp;&nbsp;';
					}
					out += '</td>';
				}
				$('#downloadFileList').html(out);
				
				downloadDialog.dialog('open');
			}
		});
		
		
        
	});
	
	$(resultList).on('click', 'button[name=onDeleteBtn]', function (){
		var segmentExportId = $(this).attr('key');
		$.msgBox('confirm', '',  '是否要删除选中的客群导出项？',  function(result) { 
			if(result == true) {
        		var queryString = 'deletes=' + segmentExportId;
				$.ajax({
					   url: deleteUrl,
					   data: queryString,
					   success: function(result) {
						   if(result.level == 'ERROR') {
							   $.msgBox('error', '', result.message);
						   }
						   else {
							   $.msgBox('info', '', result.message);
						   	   reloadSearch();   
						   }
					   }
				});
			}
		});
	});


	function reloadSearch(){
		var queryData =JSON.stringify( $('input[wrType]', searchForm).wrender('getValue') );
		resultList.setGridParam({datatype:'json', postData:{'queryData' :queryData}, page:1}).trigger("reloadGrid");
		return false;
	}

	function doDownload() {
		var colArray = new Array();
		$('input[name="columns"]:checked', downloadForm).each(function(idx, elem) {
			colArray.push({name:this.value, label:$(this).parent().text()});
		});
		var colJSon = JSON.stringify(colArray);
		$.msgBox('info', '', '系统已接收了您的下载请求，请进入“我的客群下载”中下载处理完成的客群导出文件！');
	}
	
	reloadSearch();
	jqgridAutoResize($('#contentArea'), resultList);
	$('button[wrType=button]').wrender();
	$('#contentArea').show();
});
</script>

<% if(fromAp2 == false) { %>
</head>
<body>
<center>
<% } %>

<div id="contentArea" style="width:100%;display:none;">
	<form id="searchForm">
	<table width="100%" align="center" class="ui-widget ui-widget-content">
		<thead>
			<tr class="ui-widget-header">
				<td colspan="4">查询条件</td>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td width="20%" align="right">编码: </td>
				<td width="30%" align="left"><input type="text" wrType="text" name="code"/></td>
				<td width="15%" align="right">名称: </td>
				<td width="35%" align="left"><input type="text" wrType="text" name="name"/></td>
			</tr>
			<tr>
				<td width="20%" align="right">创建人: </td>
				<td width="30%" align="left">
					<input type="text" wrType="text" name="createBy" />
			  	</td>
				<td width="15%" align="right">导出时间: </td>
				<td width="35%" align="left">
				<input type="text" wrType="datetime" name="exportTimeFrom"/>
				~ <input type="text" wrType="datetime" name="exportTimeTo"/>
				</td>
			</tr>
		</tbody>
		<tfoot>
			<tr><td colspan="4" align="center">
				<button type="button" id="onSearchBtn" wrType="button" wrParam="icon:ui-icon-search;">查询</button>
			</td></tr>
		</tfoot>
	</table>
	</form>
	<br/>
	<table id="resultList"></table>
	<div id="resultPager" style="text-align:center;"></div>

	<div align="center" id="viewDialog" title="查看客群信息">
		<form id="viewForm">
	   		<table width="100%">
	   			<tbody>
				<tr>
				  <td width="20%" align="right">编码: </td>
				  <td width="30%" align="left"><div name="code" wrType="readtext"></div></td>
				  <td width="20%" align="right">名称: </td>
				  <td width="30%" align="left"><div name="name" wrType="readtext"></div></td>
				</tr>
				<tr>
				  <td width="25%" align="right">排序: </td>
				  <td width="10%" align="left" nowrap="nowrap">
				  	<select name="sortName" wrType="select" wrParam="sourceId:dimdef;typeId:2005"></select>
					&nbsp;&nbsp;&nbsp;
					<select name="sortOrder" wrType="select">
						<option value="asc">升序</option>
						<option value="desc">降序</option>
					</select>
				  </td>
				  <td width="25%" align="right">最大数量: </td>
				  <td width="40%" align="left">
					  <input name="maxCount" type="text" wrType="text"/>
				  </td>
				</tr>
				<tr id="calCountRow">
				  <td width="25%" align="right">实际数量: </td>
				  <td width="15%" align="left">
					  <span name="calCount" wrType="readtext"></span>
				  </td>
				  <td width="25%" align="right">计算时间: </td>
				  <td width="350%" align="left">
					  <div name="calCountTime" wrType="readtext"></div>
				  </td>
				</tr>
				</tbody>
	   		</table>
   		</form>
		<div id="criteriaSchemeView">
			<table width="100%" border="0" cellingspace="0">
				<tr><td align="left"><strong>已选择条件:</strong></td></tr>
				<tr><td valign="top"><table id="schemeView"></table></td></tr>
			</table>
		</div>
	</div>
	
	<div align="center" id="downloadDialog" title="客群下载配置">
		<form id="downloadForm">
			<input name="segmentExportId" type="hidden" wrType="text"/>
	   		<table width="100%">
	   			<tbody>
				<tr>
				  <td width="20%" align="right">客群编码: </td>
				  <td width="30%" align="left"><div name="code" wrType="readtext"></div></td>
				  <td width="20%" align="right">客群名称: </td>
				  <td width="30%" align="left"><div name="name" wrType="readtext"></div></td>
				</tr>
				<tr>
				  <td align="right">导出状态: </td>
				  <td align="left"><div name="exportStatus" wrType="readtext"></div></td>
				  <td align="right">&nbsp;</td>
				  <td align="left">&nbsp;</td>
				</tr>
				<tr>
				  <td align="right">导出列: </td>
				  <td align="left" colspan="3"><div name="columnLabels" wrType="readtext"></div></td>
				</tr>
				<tr id="downloadFileList">
				</tr>
				</tbody>
	   		</table>
   		</form>
	</div>
	
</div>

<% if(fromAp2 == false) { %>
</center>
</body>
</html>
<% } %>