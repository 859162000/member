<%@ page import="com.wanda.ccs.member.ap2in.*" %>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%
boolean testFlag = ("true".equals(request.getParameter("test")));//用户测试的标志
boolean fromAp2 = AuthUserHelper.isFromAp2(request);//是否为AP2发来的请求
UserProfile user = AuthUserHelper.getUser();
boolean viewRight = user.getRights().contains("member.extpointruleconditon.view");
boolean editRight = user.getRights().contains("member.extpointruleconditon.edit");

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
<title>特殊积分条件管理</title>

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
<script type="text/javascript" src="<%=context%>/segment/segment.settings.extpoint.js"></script>

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

var extPointAddRule = {};
/*
extPointAddRule.tsale = 0;		//会员基本信息
extPointAddRule.conSale = 0;	//记录其他标签添加的数量

extPointAddRule.addRuleValid = function(groupId) {
	//alert(extPointAddRule.tsale + "=="+ extPointAddRule.conSale);
	if(groupId == "tsale") {	// 添加票类
		if(extPointAddRule.conSale == 0) {
			extPointAddRule.tsale+=1;
			return true;
		}
	} else if(groupId == "conSale") {	// 添加卖品
		if(extPointAddRule.tsale == 0) {
			extPointAddRule.conSale+=1;
			return true;
		}
	} else {
		return true;
	}
	
	$.msgBox('warn', '', '票类和卖品不能同时存在！');
	return false;
}

extPointAddRule.removeRuleValid = function(groupId) {
	if(groupId == "tsale") {	// 添加会员基本信息
		extPointAddRule.tsale-=1;
	} else if(groupId == "conSale") {	// 添加其他信息
		extPointAddRule.conSale-=1;
	}
}

extPointAddRule.initData = function(groupId) {
	if(groupId == "tsale") {	// 添加会员基本信息
		extPointAddRule.tsale+=1;
	} else if(groupId == "conSale") {	// 添加其他信息
		extPointAddRule.conSale+=1;
	}
}*/

$(function(){
	var inputDialog = $('#inputDialog');
	var viewDialog = $('#viewDialog');
	var viewForm = $('#viewForm');
	var resultList = $('#resultList');
	var inputForm = $('#inputForm');
	var searchForm = $('#searchForm');
	
	var queryUrl = '<%=context%>/segment/ExtPointCriteriaAction/query.do';
	var getUrl = '<%=context%>/segment/ExtPointCriteriaAction/get.do';
	var insertUrl = '<%=context%>/segment/ExtPointCriteriaAction/insert.do';
	var updateUrl = '<%=context%>/segment/ExtPointCriteriaAction/update.do'; 
	var deleteUrl = '<%=context%>/segment/ExtPointCriteriaAction/delete.do';
	var conItemCriteriaResultUrl = '<%=context%>/segment/ExtPointCriteriaAction/getConItemCriteriaResult.do';
	var ticketCriteriaResultUrl = '<%=context%>/segment/ExtPointCriteriaAction/getTicketCriteriaResult.do';
	
	var validator = inputForm.validate({
		rules:{
			"name":{required:true, maxlength:20}
		}
    });

	//To indicate the open user input dialog is new creation or not. 
	var isNew = true;
	var girdColumns = [
						{name:'EXT_POINT_CRITERIA_ID', label:'ID', hidden:true},
						{name:'EDITABLE', label:'EDITABLE', hidden:true},
						{name:'CODE', label:'编码', width:60, sortable:true},
						{name:'NAME', label:'名称', width:130, sortable:true},
						{name:'CREATE_BY', label:'创建人', width:50, sortable:false},
						{name:'UPDATE_BY', label:'更新人', width:50, sortable:false},
						{name:'UPDATE_DATE', label:'更新时间', width:60, sortable:true},
						{name:'ACTIONS', label:'操作', width:60, align:'center'}
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
			var cellHtml = '<button name="onViewBtn" key=' + rowdata['EXT_POINT_CRITERIA_ID'] + ' type="button" wrType="button" wrParam="icon:ui-icon-zoomin;text:false" style="height:22px;" title="查看"/>&nbsp;';
			if(rowdata['EDITABLE']) {
				cellHtml += '<button name="onModifyBtn" key=' + rowdata['EXT_POINT_CRITERIA_ID'] + ' type="button" wrType="button" wrParam="icon:ui-icon-pencil;text:false" style="height:22px;" title="修改"/>&nbsp;' + 
					'<button name="onDeleteBtn" key=' + rowdata['EXT_POINT_CRITERIA_ID'] + ' type="button" wrType="button" wrParam="icon:ui-icon-trash;text:false" style="height:22px;" title="删除"/>';
			}
			
			$(this).setCell(rowid, 'ACTIONS', cellHtml);
			$('button[wrType=button]', this).wrender();
		}
	});
	

	inputDialog.dialog({
		width: 940,
		buttons: [
			{id:'onSaveBtn', text:'保存', click: doSave},
			{id:'onCloseBtn', text:'关闭', click: function() { $(this).dialog('close'); }}
		],
		beforeClose: function(event, ui) { 
			validator.resetForm(); //After dialog closed, clean the validation error style and prompt text.
			inputForm.resetForm();
		}
	});
	
	viewDialog.dialog({
		width: 730,
		buttons: [
			{id:'onCloseBtn', text:'关闭', click: function() { $(this).dialog('close'); }}
		]
	});
	
	$('[wrType]', searchForm).wrender();
	$('[wrType]', inputForm).wrender();
	
	$('#onSearchBtn').click(reloadSearch);
	
	$('#onNewBtn').click(function (){
		extPointAddRule.memberLevel = 0;
		extPointAddRule.sale = 0;
		
		isNew = true;
		inputForm.clearForm();
		$('[wrType]', inputForm).wrender('setValue', {code:''});
		allowModify(true);
		schemeAction.applyScheme($.segment.settings.extpoint, null);//创建初始化积分条件列表
		
		inputDialog.dialog('open');
	});
	
	$(resultList).on('click', 'button[name=onModifyBtn]', function (){
		extPointAddRule.memberLevel = 0;
		extPointAddRule.sale = 0;
		isNew = false;
        var queryString = 'extPointCriteriaId=' + $(this).attr('key');
		$.ajax({
			url: getUrl,
			data: queryString,
			success: function(result) {	   
				if(result.allowModify == false) {
				    $.msgBox('info', '', '对不起，您不是创建用户本人，不能进行修改！');
				    return;
				}
				
				$('[wrType]', inputForm).wrender('setValue', result);
				
				var schemeObj = $.parseJSON(result.criteriaScheme);	
				schemeAction.applyScheme($.segment.settings.extpoint, schemeObj);
				
				inputDialog.dialog('open');
			}
		});
			
	});
	
	
	$(resultList).on('click', 'button[name=onViewBtn]', function () {
        var queryString = 'extPointCriteriaId=' + $(this).attr('key');
		$.ajax({
			   url: getUrl,
			   data: queryString,
			   success: function(result) {	   
					$('[wrType]', viewForm).wrender('toReadOnly', result, true);
					var schemeObj = $.parseJSON(result.criteriaScheme);	
					schemeAction.applySchemeView($.segment.settings.extpoint, schemeObj);
					viewDialog.dialog('open');
			   }
		});
	});
	
	$(resultList).on('click', 'button[name=onDeleteBtn]', function (){
		var extPointCriteriaId = $(this).attr('key');
		$.msgBox('confirm', '', '是否要删除选中的积分条件？',  function(result) { 
			if(result == true) {
        		var queryString = 'deletes=' + extPointCriteriaId;
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
	
	function allowModify(val) {
		if(val) {
			$('#onSaveBtn').show(); 
			$('#notAllowModifyRow').hide();
		}
		else {
			$('#onSaveBtn').hide();
			$('#notAllowModifyRow').show();
		}
	}
	
	function reloadSearch() {
		var queryData =JSON.stringify( $('input[wrType]', searchForm).wrender('getValue') );
		resultList.setGridParam({datatype:'json', postData:{'queryData' :queryData}, page:1}).trigger("reloadGrid");
		return false;
	}

	
	function doSave() {
		if(validator.form() == false) {
	         validator.focusInvalid();
	         return false;
	    }
		if(schemeAction.validateScheme() == false) {
			return false;
		}
		
		var voData =$('[wrType]', inputForm).wrender('getValueData');
		var postData = 'json=' + voData + '&criteriaScheme=' + schemeAction.getSchemeData();
		
		$.ajax({
		   url: (isNew) ? insertUrl : updateUrl,
		   data: postData,
		   success: function(result){
			   if(result.level == 'ERROR') {
				   $.msgBox('error', '', result.message);
			   }
			   else {
				   $('input[name="version"]', inputForm).wrender('setValue', {version: result.version});
					$.msgBox('info', '', result.message, function() { 
						inputDialog.dialog('close');
						reloadSearch();
					});
			   }
		   }
		});
	}
	
	// 排序字段校验
	function sortValidate(sortVal, scheme) {

		if(parseInt(sortVal) == 10) {	//判断删选条件中是否存在exchangePoint条件，如果不存在不能提交
			var success = false;
			var schemeObj = eval(decodeURI(scheme));
			for(var i=0,len=schemeObj.length;i<len;i++) {
				if(schemeObj[i].inputId == 'exchangePoint') {
					success = true;
					break;
				}
			}
			
			if(success == false) {
				$.msgBox('error', '', '选中排序字段需要筛选条件“可兑换积分余额”支持');
				// 选中指定的ztree节点
				var node = autoAddScheme.inputsTreeObj.getNodeByParam("inputId",'exchangePoint', null);
				autoAddScheme.inputsTreeObj.selectNode(node);
				// 触发ztree节点的click事件
				autoAddScheme.addBtn.trigger('click');
				
				return false;
			}
		}
		
		return true;
	}
	
	reloadSearch();
	jqgridAutoResize($('#contentArea'), resultList);//跟随窗口大小自动调整列表宽度
	$('button[wrType=button]').wrender();
	$('#contentArea').show();
	
	
//测试时得到对应的SQL
<% if(testFlag) { %>
	$('#onGetConItemSqlBtn').click(function () {
		getSql(conItemCriteriaResultUrl);
	});
	
	$('#onGetTicketSqlBtn').click(function () {
		getSql(ticketCriteriaResultUrl);
	});
	
	function getSql(url) {
		var rowId = resultList.jqGrid('getGridParam', 'selrow');
		if(rowId != null) {
       		var extPointCriteriaId = resultList.jqGrid('getCell', rowId, 'EXT_POINT_CRITERIA_ID');
       		var queryString = 'extPointCriteriaId=' + extPointCriteriaId;
			$.ajax({
				url: url,
				data: queryString,
				success: function(result) {
					if(result != null) {
						$.msgBox('info', '查询SQL', '<pre>' + result.parameterizeText + '</pre>');
					}
					else {
						$.msgBox('info', '查询SQL', '返回结果为空！');
					}
				}
			});
		}
		else {
			$.msgBox('warn', '', '请选一行记录！');
			return false;
		} 
	}
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
			<button id="onGetConItemSqlBtn" type="button" wrType="button" wrParam="icon:ui-icon-zoomin;">得到卖品交易SQL</button>
			<button id="onGetTicketSqlBtn" type="button" wrType="button" wrParam="icon:ui-icon-zoomin;">得到票交易SQL</button>
		<% } %>
	</div>
	<table id="resultList"></table>
	<div id="resultPager" style="text-align:center;"></div>
	
	<div align="center" id="inputDialog" title="积分条件信息">
	   	<form id="inputForm">
	   		<input name="extPointCriteriaId" type="hidden" wrType="text"/>
	   		<input name="version" type="hidden" wrType="text"/>
	   		
	   		<table width="100%">
	   			<tbody>
				<tr>
				  <td width="15%" align="right">编码: </td>
				  <td width="15%" align="left"><div name="code" wrType="readtext"></div></td>
				  <td width="10%" align="right">名称: </td>
				  <td width="60%" align="left"><input name="name" type="text" wrType="text"/></td>
				</tr>
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
	
	<div align="center" id="viewDialog" title="查看积分条件信息">
		<form id="viewForm">
	   		<table width="100%">
	   			<tbody>
				<tr>
				  <td width="20%" align="right">编码: </td>
				  <td width="30%" align="left"><div name="code" wrType="readtext"></div></td>
				  <td width="20%" align="right">名称: </td>
				  <td width="30%" align="left"><div name="name" wrType="readtext"></div></td>
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
	
</div>

<% if(fromAp2 == false) { %>
</center>
</body>
</html>
<% } %>