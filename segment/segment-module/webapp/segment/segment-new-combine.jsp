<%@ page import="com.wanda.ccs.member.ap2in.*" %>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%
boolean testFlag = ("true".equals(request.getParameter("test")));//用户测试的标志
boolean fromAp2 = AuthUserHelper.isFromAp2(request);//是否为AP2发来的请求
UserProfile user = AuthUserHelper.getUser();
boolean viewRight = user.getRights().contains("member.segment.view");
boolean editRight = user.getRights().contains("member.segment.edit");

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
<title>客群管理</title>

<jsp:include page="include.jsp">
<jsp:param name="context" value="<%=context%>"/>
<jsp:param name="groups" value="ap2-jquery,ap2-jquery-ui,jqgrid,validation,ztree,datetime,multiselect,common,wrender"/>
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
	var inputDialog = $('#inputDialog');
	var resultList = $('#resultList');
	var inputForm = $('#inputForm');
	var viewDialog = $('#viewDialog');
	var viewForm = $('#viewForm');
	var downloadDialog = $('#downloadDialog');
	var downloadForm = $('#downloadForm');
	var searchForm = $('#searchForm');
	var calCountDialog = $('#calCountDialog');
	var selectInputDialog = $('#selectInputDialog');
	
	var queryUrl = '<%=context%>/segment/SegmentAction/query.do';
	var getUrl = '<%=context%>/segment/SegmentAction/get.do';
	var insertUrl = '<%=context%>/segment/SegmentAction/insert.do';
	var insertDownloadUrl = '<%=context%>/segment/SegmentExportAction/insert.do';
	var updateUrl = '<%=context%>/segment/SegmentAction/update.do';
	var deleteUrl = '<%=context%>/segment/SegmentAction/delete.do';
	var calCountUrl = '<%=context%>/segment/SegmentAction/calCount.do';
	var getCountUrl = '<%=context%>/segment/SegmentAction/getCount.do';
	var criteriaResultUrl = '<%=context%>/segment/SegmentAction/getCriteriaResult.do';
	
	var calCountTimerId = null; //calculating timer id
	
	var schemeChanged = false; //标示当前打开的修改窗口中的条件是否有更改。
	
	var validator = inputForm.validate({
		rules:{
			'name':{required:true, maxlength:20},
			'sortName':{required:true}
		}
    });

	//To indicate the open user input dialog is new creation or not. 
	var isNew = true;
	var girdColumns = [
						{name:'SEGMENT_ID', label:'客群ID', hidden:true},
						{name:'EDITABLE', label:'EDITABLE', hidden:true},
						{name:'EXPORTABLE', label:'EXPORTABLE', hidden:true},
						{name:'CODE', label:'编码', width:70, sortable:true},
						{name:'NAME', label:'名称', width:110, sortable:true},
						{name:'CAL_COUNT', label:'实际数量', width:70, sortable:false},
						{name:'CREATE_BY', label:'创建人', width:60, sortable:false},
						{name:'UPDATE_BY', label:'更新人', width:60, sortable:false},
						{name:'UPDATE_DATE', label:'更新时间', width:70, sortable:true},
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
		sortname: 'UPDATE_DATE',
		sortorder: "desc",
		afterInsertRow: function(rowid, rowdata, rowelem) {
			//加入每行后的操作按钮
			var cellHtml = '<button name="onViewBtn" key=' + rowdata['SEGMENT_ID'] + ' type="button" wrType="button" wrParam="icon:ui-icon-zoomin;text:false" style="height:22px;" title="查看"/>&nbsp;';
			if(rowdata['EXPORTABLE']){
				cellHtml += '<button name="onDownloadBtn" key=' + rowdata['SEGMENT_ID'] + ' type="button" wrType="button" wrParam="icon:ui-icon-arrowthickstop-1-s;text:false" style="height:22px;" title="导出"/>&nbsp;';
			}
			
			
			if(rowdata['EDITABLE']) {
				cellHtml += '<button name="onModifyBtn" key=' + rowdata['SEGMENT_ID'] + ' rowid=' + rowid + ' type="button" wrType="button" wrParam="icon:ui-icon-pencil;text:false" style="height:22px;" title="修改"/>&nbsp;' + 
					'<button name="onDeleteBtn" key=' + rowdata['SEGMENT_ID'] + ' type="button" wrType="button" wrParam="icon:ui-icon-trash;text:false" style="height:22px;" title="删除"/>';
			}
			
			$(this).setCell(rowid, 'ACTIONS', cellHtml);
			$('button[wrType=button]', this).wrender();
		}
	});
	
	inputDialog.dialog({
		width: 940,
		buttons: [
			{text:'保存', click: doSave},
			{text:'关闭', click: function() { $(this).dialog('close'); }}
		],
		beforeClose: function(event, ui) { 
			//Clear timer and reset the calCountTimerId to be null
			if(calCountTimerId != null) {
				clearInterval(calCountTimerId);
				calCountTimerId = null;
			}
			
			validator.resetForm(); //After dialog closed, clean the validation error style and prompt text.
			inputForm.resetForm();
		}
	});
	
	viewDialog.dialog({
		width: 730,
		buttons: [
			{text:'关闭', click: function() { $(this).dialog('close'); }}
		]
	});
	
	calCountDialog.dialog({
		width: 500,
		buttons: [
			{text:'关闭后台执行', click: function() { $(this).dialog('close'); }}
		]
	});
	
	downloadDialog.dialog({
		width: 730,
		buttons: [
			{text:'开始导出', click: doDownload},
			{text:'取消', click: function() { $(this).dialog('close'); }}
		]
	});
	
	selectInputDialog.dialog({
		width: 500,
		buttons: [
            {text:'下一步', click: function() { 
            	$(this).dialog('close');
            	if($('input[name=combineSegment]:checked', selectInputDialog).val() == 'true') {
            		combineAction.openDialog();//打开复合客群窗口
            	}
            	else {
            		onNewCommon();
            	}
			}},
			{text:'取消', click: function() { $(this).dialog('close'); }}
		]
	});
	

	
	$('[wrType]', searchForm).wrender();
	$('[wrType]', inputForm).wrender();
	$('[wrType]', viewForm).wrender();
	
	$('#onSearchBtn').click(reloadSearch);
	
	$('#onNewBtn').click(function (){
		selectInputDialog.dialog('open');
	});
	
	//打开普通客群的新建界面
	function onNewCommon() {
		isNew = true;
		inputForm.clearForm();
		$('[wrType]', inputForm).wrender('setValue', {code:'', sortName:'00', sortOrder:'asc', maxCount:'100000',compAllowModifier: null});
		sortNameChange('00');
		setCalCount(-1, '', false); //因为新建后自动执行后台计算，这里隐藏计算数量行
		schemeAction.applyScheme($.segment.settings.member, null);//创建初始化客群列表
		
		inputDialog.dialog('open');
	}
	
	
	$(resultList).on('click', 'button[name=onModifyBtn]', function (){
		isNew = false;
		var segmentId = $(this).attr('key');
		var rowid = $(this).attr('rowid');
		var CREATE_BY = resultList.jqGrid('getCell', rowid, 'CREATE_BY');
        var queryString = 'segmentId=' + segmentId;
		$.ajax({
			url: getUrl,
			data: queryString,
			success: function(result) {
				if(result.allowModify == false) {
				    $.msgBox('info', '', '对不起，您是未授权用户，不能进行修改！');
				    return;
				}
				
				sortNameChange(result.sortName);
				$('[wrType]', inputForm).wrender('setValue', result);
				setCalCount(result.calCount, result.calCountTime, true);
				if(CREATE_BY=="${USER.loginId}"){
					$('#isCanModify').show();
				}else{
					$('#isCanModify').hide();
				}
				var schemeObj = $.parseJSON(result.criteriaScheme);	
				schemeChanged = false;
				schemeAction.applyScheme($.segment.settings.member, schemeObj, schemeChange);
				
				inputDialog.dialog('open');
			}
		});
	});
	
	
	$(resultList).on('click', 'button[name=onViewBtn]', function () {
		var segmentId = $(this).attr('key');
        var queryString = 'segmentId=' + segmentId;
           
		$.ajax({
		   url: getUrl,
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
		var segmentId = $(this).attr('key');
        $('input[name="segmentId"]', downloadForm).val(segmentId);
        downloadDialog.dialog('open');
	});
	
	$(resultList).on('click', 'button[name=onDeleteBtn]', function (){
		var segmentId = $(this).attr('key');
		$.msgBox('confirm', '',  '是否要删除选中的客群？',  function(result) { 
			if(result == true) {
        		var queryString = 'deletes=' + segmentId;
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
	
	$('select[name="sortName"]', inputForm).change(function() {
		sortNameChange(this.value);
	});
	
	$('#onCalCountBtn', inputForm).click(function() {
		if(schemeChanged == true) {
			//如果发现客群中的条件有变化，先对客群进行保存
			var updateScucceeded = false;
			var schemeData = getValidatedSchemeData();
			if(schemeData != null) {
				$.ajax({
					async: false,
					url: updateUrl,
					data: schemeData,
					success: function(result){
						if(result.level == 'INFO') {
							updateScucceeded = true;
							schemeChanged = false;
							$('input[name="version"]', inputForm).wrender('setValue', {version: result.version});
						}
						else {
							$.msgBox('error', '', result.message);
						}
					}
				});
			}
			
			//如果更新失败不再进行后继操作
			if(updateScucceeded == false) {
				return;
			}
		}
		
		var formVal = $('input[name="segmentId"],span[name=calCount],div[name=calCountTime]', inputForm).wrender('getValue');
		
		calCountDialog.dialog('open');
		setCalCount(-1, '', true);
		
		$.ajax({
			async: false,
			url: calCountUrl,
			data: 'segmentId=' + formVal.segmentId,
			success: function(result){
				if(result.level == 'INFO') {
					calCountTimerId = setInterval(retrieveCalCount, 1000);
				}
				else {
					setCalCount(formVal.calCount, formVal.calCountTime, true);
					calCountDialog.dialog('close');
					
					$.msgBox('error', '', result.message);
				}
			}
		});
	});
	
	function schemeChange(value) {
		schemeChanged = true;
	}
	
	function retrieveCalCount() {
		var segmentId = $('input[name="segmentId"]', inputForm).val();	
		$.ajax({
			url: getCountUrl,
			data: 'segmentId=' + segmentId,
			success: function(result){
				if(result.calCount >= 0) {
					//Clear timer and reset the calCountTimerId to be null
					if(calCountTimerId != null) {
						clearInterval(calCountTimerId);
						calCountTimerId = null;
					}
					
					setCalCount(result.calCount, result.calCountTime, true);
					calCountDialog.dialog('close');
					$.msgBox('info', '', '客群数量计算完成！ 客群数量为：' + result.calCount);
				}
			}
		});
	}
	

	function setCalCount(count, calCountTime, showRow) {
		var calCountRow = $('#calCountRow');
		if(showRow == true) {
			var calCountElm = $('#calCountRow span[name=calCount]');
			var calCountTimeElm = $('#calCountRow div[name=calCountTime]');
			var onCalCountBtnElm = $('#onCalCountBtn');

			if(count == -1) {
				calCountElm.wrender('setValue', {calCount: '计算中...'});
				calCountTimeElm.wrender('setValue', {calCountTime: ''});
				onCalCountBtnElm.hide();
			}
			else {
				calCountElm.wrender('setValue', {calCount: count});
				calCountTimeElm.wrender('setValue', {calCountTime: calCountTime});
				onCalCountBtnElm.show();
			}
			calCountRow.show();//显示计算列
		}
		else {
			calCountRow.hide();//显示计算列
		}
	}
	
	function sortNameChange(val) {
		if(val == '00') {
			$('select[name="sortOrder"]', inputForm).hide();
			$('select[name="sortOrder"]', inputForm).wrender('setValue', {sortOrder:'asc'});
		}
		else {
			$('select[name="sortOrder"]', inputForm).show();
		}
	}

	
	function reloadSearch(){
		var queryData =JSON.stringify( $('input[wrType]', searchForm).wrender('getValue') );
		resultList.setGridParam({datatype:'json', postData:{'queryData' :queryData}, page:1}).trigger("reloadGrid");
		return false;
	}

	function doSave() {
		var schemeData = getValidatedSchemeData();
		if(schemeData != null) {
			$.ajax({
				url: (isNew) ? insertUrl : updateUrl,
				data: schemeData,
				success: function(result){
					if(result.level == 'ERROR') {
						$.msgBox('error', '', result.message);
					}
					else {
						schemeChanged = false; //清除修改更新标示位
						$('input[name="version"]', inputForm).wrender('setValue', {version: result.version});
						if(isNew) {
							$.msgBox('info', '', result.message, function(result) {
								
								reloadSearch();
								inputDialog.dialog('close');
							});
						}
						else { 
							$.msgBox('yesno', '确认', result.message + "<br/>选择'是'关闭该窗口；'否'保留该窗口继续编辑。", function(result) {
								reloadSearch();
								if(result == true) {
									inputDialog.dialog('close');
								}
							});
						}
					}
					
					
				}
			});
		}
	}
	
	function doDownload() {
		var segmentId = $('input[name="segmentId"]', downloadForm).val();
		
		var colArray = new Array();
		$('input[name="columns"]:checked', downloadForm).each(function(idx, elem) {
			colArray.push({name:this.value, label:$(this).parent().text()});
		});
		
		var colJSon = JSON.stringify(colArray);
		//alert(colJSon);
		var postData = 'segmentId=' + segmentId + '&columnSetting=' + encodeURI(colJSon);

		$.ajax({
			url: insertDownloadUrl,
			data: postData,
			success: function(result) {
				if(result.level == 'INFO') {
					$.msgBox('info', '', result.message, function(){ 
						downloadDialog.dialog('close');
					});
				}
				else {
					$.msgBox('error', '', result.message, function(){ 
						downloadDialog.dialog('close');
					});
				}
			}
		});
	}
	
	//对条件进行验证，并返回条件对应json数据，这个数据已经进行了编码可以直接作为提交数据。
	function getValidatedSchemeData() {
		if(validator.form() == false) {
	    	validator.focusInvalid();
	        return null;
	    }
		if(schemeAction.validateScheme() == false) {
			return null;
		}
		
		var voData =$('[wrType]:not([wrType=readtext]):not([wrType=button])', inputForm).wrender('getValueData');
		var postData = 'json=' + voData + '&criteriaScheme=' + schemeAction.getSchemeData();
		return postData;
	}
	
	reloadSearch();
	jqgridAutoResize($('#contentArea'), resultList);
	$('button[wrType=button]').wrender();
	$('#contentArea').show();

//测试时得到对应的SQL
<% if(testFlag) { %>
	$('#onGetSqlBtn').click(function () {
		var rowId = resultList.jqGrid('getGridParam', 'selrow');
		if(rowId != null) {
       		var segmentId = resultList.jqGrid('getCell', rowId, 'SEGMENT_ID');
       		var queryString = 'segmentId=' + segmentId;
			$.ajax({
				url: criteriaResultUrl,
				data: queryString,
				success: function(result) {
					$.msgBox('info', '查询SQL', '<pre>' + result.parameterizeText + '</pre>');
				}
			});
		}
		else {
			$.msgBox('warn', '', '请选一行客群记录！');
			return false;
		} 
	});
<% } %>
	
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
				<td width="15%" align="right">更新时间: </td>
				<td width="35%" align="left">
				<input type="text" wrType="datetime" name="updateTimeFrom"/>
				~ <input type="text" wrType="datetime" name="updateTimeTo"/>
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
	<div style="text-align:left;">
		<% if(editRight) { %>
		<button id="onNewBtn" type="button" wrType="button" wrParam="icon:ui-icon-plusthick;">新增</button>
		<% } if(testFlag) { %>
		<button id="onGetSqlBtn" type="button" wrType="button" wrParam="icon:ui-icon-zoomin;">得到SQL</button>
		<% } %>
	</div>
	<table id="resultList"></table>
	<div id="resultPager" style="text-align:center;"></div>
	
	<div align="center" id="selectInputDialog" title="选择客群类型">
		<table width="100%">
		<tbody>
			<tr>
				<td colspan="4" align="left"><strong>请选择您即将创建的可群类型</strong></td>
			</tr>
			<tr>
				<td width="50%" align="center"><label><input name="combineSegment" type="radio" value="false" checked="checked"/> 普通</label></td>
				<td width="50%" align="center"><label><input name="combineSegment" type="radio" value="true" /> 复合</label></td>
			</td>
		</tr>
		</tbody>
		</table>
	</div>
	
	<div align="center" id="inputDialog" title="客群信息">
	   	<form id="inputForm">
	   		<input name="segmentId" type="hidden" wrType="text"/>
	   		<input name="version" type="hidden" wrType="text"/>
	   		
	   		<table width="100%">
	   			<tbody>
				<tr>
				  <td width="25%" align="right">编码: </td>
				  <td width="10%" align="left"><div name="code" wrType="readtext"></div></td>
				  <td width="25%" align="right">名称: </td>
				  <td width="40%" align="left"><input name="name" type="text" wrType="text"/>
				  </td>
				  
				</tr>
				<tr>
				  <td width="25%" align="right">排序: </td>
				  <td width="10%" align="left" nowrap="nowrap">
				  <%-- <select name="sortName" wrType="select" wrParam="sourceId:dimdef;typeId:2005"></select> --%>
				  	<select name="sortName" wrType="select">
					  	<option value="00">不指定排序</option>
						<option value="40">会员级别</option>
				  	</select>
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
					  <button id="onCalCountBtn" type="button" wrType="button">计算数量</button>
				  </td>
				  <td width="25%" align="right">计算时间: </td>
				  <td width="350%" align="left">
					  <div name="calCountTime" wrType="readtext"></div>
				  </td>
				</tr>
				<%--
				<tr id="isCanModify">
				  <td width="25%" align="right">授权修改人: </td>
				 <td width="10%" align="left" colspan="3">
					  <input name="allowModifier" type="text" wrType="text"/> 
					  <div name="compAllowModifier" wrType="composite" wrParam="compositeId:authuser;"></div>
				 </td>
				</tr> 
				 --%>
				</tbody>
	   		</table>
	   	</form>

		<div id="criteriaSchemeConfig">
			<table width="100%" border="0" cellingspace="0">
				<tr>
					<td width="168"><strong>可选条件：</strong></td>
					<td width="32"></td>
					<td width="700"><strong>已选择条件:</strong></td>
				</tr>
				<tr>
					<td valign="top" >
						<div class="ui-widget ui-widget-content ui-corner-all" style="width:170px; height:356px; overflow:auto;">
						   <ul id="inputsTree" class="ztree"></ul>
						</div>
					</td>
					<td>
						<!-- 添加搜索添加的"+"按钮 -->
						<button name="add" type="button" wrType="button" wrParam="icon:ui-icon-plusthick;text:false;" style="height:30px;"/>
						</td>
						<td valign="top">
							<table id="scheme"></table>
						</td>
					</tr>
			</table>
		</div>
	</div>
	
	<div id="calCountDialog">
		<div>正在计算....</div>
	</div>
	
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
	
	<div align="center" id="downloadDialog" title="客群导出配置">
		<form id="downloadForm">
			<input name="segmentId" type="hidden" wrType="text"/>
	   		<table width="100%">
	   			<tbody>		
				<tr>
				  <td width="20%" align="left"><label><input id="download.MEMBER_ID" type="checkbox" name="columns" value="MEMBER_ID" checked="checked"/>会员ID</label></td>
				  <td width="20%" align="left"><label><input id="download.MEMBER_NO" type="checkbox" name="columns" value="MEMBER_NO" checked="checked"/>会员序列号</label></td>
				</tr>
				<tr>
				  <td width="20%" align="left"><label><input id="download.MOBILE" type="checkbox" name="columns" value="MOBILE" checked="checked"/>手机号</label></td>
				  <td width="20%" align="left"><label><input id="download.NAME" type="checkbox" name="columns" value="NAME" checked="checked"/>姓名</label></td>
				</tr>
				<tr>
				  <td width="20%" align="left"><label><input id="download.GENDER" type="checkbox" name="columns" value="GENDER"/>性别</label></td>
				  <td width="20%" align="left"><label><input id="download.BIRTHDAY" type="checkbox" name="columns" value="BIRTHDAY"/>生日</label></td>
				</tr>
				<tr>
				  <td width="20%" align="left"><label><input id="download.REGIST_TYPE" type="checkbox" name="columns" value="REGIST_TYPE"/>注册类型</label></td>
				  <td width="20%" align="left"><label><input id="download.REGIST_CHNID" type="checkbox" name="columns" value="REGIST_CHNID"/>招募渠道</label></td>
				</tr>
				<tr>
				  <td width="20%" align="left"><label><input id="download.REGIST_CINEMA_NAME" type="checkbox" name="columns" value="REGIST_CINEMA_NAME"/>注册影城</label></td>
				  <td width="20%" align="left"><label><input id="download.MANAGE_CINEMA_NAME" type="checkbox" name="columns" value="MANAGE_CINEMA_NAME"/>管理影城</label></td>
				</tr>
				<tr>
				  <td width="20%" align="left"><label><input id="download.REGIST_AREA" type="checkbox" name="columns" value="REGIST_AREA"/>注册所属区域</label></td>
				  <td width="20%" align="left"><label><input id="download.REGIST_DATE" type="checkbox" name="columns" value="REGIST_DATE"/>注册时间</label></td>
				</tr>
				</tbody>
	   		</table>
   		</form>
	</div>
	
	<jsp:include page="combineSegment.jsp"/>
</div>

<% if(fromAp2 == false) { %>
</center>
</body>
</html>
<% } %>