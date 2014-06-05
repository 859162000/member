<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Restful Example--User Management</title>

<jsp:include page="../header.jsp"><jsp:param name="groups" value="jquery,jquery-ui,jqgrid,validation,ztree,common"/></jsp:include>
<script type="text/javascript" src="segment.base.js"></script>
<script type="text/javascript" src="segment.settings.member.js"></script>


<style type="text/css">
/*自动扩展行高*/
/*
.ui-jqgrid tr.jqgrow td {
  white-space: normal !important;
  height:auto;
  vertical-align:text-top;
  padding-top:2px;
 }*/
</style>
<script type="text/javascript">
$(document).ready(function(){

	var inputDialog = $("#inputDialog");
	var resultList = $("#resultList");
	var inputForm = $("#inputForm");
	var searchForm = $("#searchForm");

	var queryUrl = 'SegmentAction/query.do';
	var getUrl = 'SegmentAction/get.do';
	var saveUrl = 'SegmentAction/save.do';
	var updateUrl = 'SegmentAction/update.do';
	var deleteUrl = 'SegmentAction/delete.do';

	var validator = inputForm.validate({
		rules:{"segment.name":{required:true, minlength:3} }
    });

	//To indicate the open user input dialog is new creation or not. 
	var isNew = true;
	var girdColumns = [ 
						{name:'segmentId', label:'客群ID', hidden:true},
						{name:'code', label:'编码', width:90, sortable:true},
						{name:'name', label:'名称', width:80, sortable:false},
						{name:'count', label:'数量', width:90, sortable:false},
						{name:'combine', label:'复合', width:80, sortable:false}
						];

	
	inputDialog.dialog({
		width: 940,
		buttons: {
			'保存': doSave,
			'关闭': function() { $(this).dialog('close'); }
		},
		beforeClose: function(event, ui) { 
			validator.resetForm(); //After dialog closed, clean the validation error style and prompt text.
			inputForm.resetForm();
		}
	});
	 
	
	resultList.jqGrid({
		datatype: 'local',
		ajaxGridOptions: {type:"POST"},
		jsonReader: {repeatitems: false},
		prmNames: {page:"queryParam.page", rows:"queryParam.rows", sort:"queryParam.sort", order: "queryParam.order"},
		height: "100%",
		rowNum: 10,
		rowList:[10,20,30],
		autowidth: true,
		multiselect: true,
		viewrecords: true,
		url:queryUrl,
		colModel: girdColumns,
		pager: $("#resultPager"),
		sortname: 'name',
		sortorder: "asc"
	});
	
	$('#onRecalculate').click(function (){
		$.msgBox('info', '', '计算中......');
	});
	
	$('button.onSearch').click(reloadSearch);
	
	$('#toolbar button.onNew').click(function (){
		isNew = true;
		inputDialog.dialog('open');
	});
	
	$('#toolbar button.onModify').click(function (){
		isNew = false;
		var rowId = resultList.jqGrid('getGridParam','selrow');
		if(rowId != null) {
			var segmentId = resultList.jqGrid('getCell', rowId, 'segmentId');
            var queryString = 'segment.segmentId=' + segmentId;
			$.ajax({
				   url: getUrl,
				   data: queryString,
				   success: function(result){
						inputForm.deserialize(result, {inputPrefix:'segment.'});
						var schemeObj = eval('(' + result.queryConditions + ')');
						$.search.openSchemeConfig($.search.settings, schemeObj);
				   }
			});
			
			inputDialog.dialog('open');
		}
		else {
			$.msgBox('warn', '', '请选中一行客群记录进行修改！');
			return false;
		}
	});
	
	$('#toolbar button.onDelete').click(function () {
		var ids = resultList.jqGrid('getGridParam', 'selarrrow');

		if(ids.length>0){
			$.msgBox('confirm', '', 
				'是否要删除选中的客群？', 
				function(result) { 
					if(result == true) {
					 	var queryString = '';
			        	for(i=0;i<ids.length;i++){
			        		var segmentId = resultList.jqGrid('getCell', ids[i], 'segmentId');
			        		queryString += ((i==0) ? '' : '&') + 'deletes=' + segmentId;
						}
						
						$.ajax({
							   url: deleteUrl,
							   data: queryString,
							   success: function(result) {
							   		$.msgBox('info', '', result.message);
							   		reloadSearch();
							   }
						});
					}
				});
		}
		else {
			$.msgBox('warn', '', '请选一行或多行客群记录进行修改！');
			return false;
		} 
	});
	
	function reloadSearch(){
		var postData = searchForm.serializeJqgrid();
		resultList.setGridParam({datatype:'json', postData:postData, page:1}).trigger("reloadGrid");
		return false;
	}
	
	function doSave() {
		if(validator.form() == false) { 
	         validator.focusInvalid();
	         return false;
	    }
		
		if($.search.validateSchemeConfig(inputForm) == false) {
			return false;
		}
		
		$.search.beforeSubmitInputs(inputForm);
		
		var postData = inputForm.serialize();
		
		$.ajax({
			   url: (isNew) ? saveUrl : updateUrl,
			   data: postData,
			   success: function(result){
					$.msgBox('info', '', result.message, function() { 
						inputDialog.dialog('close');
						reloadSearch();
					});
			   }
		});
	}
	
	

	
	reloadSearch();
	
	
	
	//$.search.initSchemeConfig();
	var applySettingName = 'member';
	var setting = $.segment.init(applySettingName);
	var cache = $.segment.settingsCache[applySettingName];
	var criterias = setting.defaultCriterias;
	//var cacheObjJson = JSON.stringify(cache)
	//alert(cacheObjJson);
	
	
	
	var zTreeSetting = {	
		view: {
			showTitle: true
		},
		data: {
			key: {
				title: 'title'
			}
		},
		callback: {
			onDblClick: function(event, treeId, treeNode) {
				addCriteria(treeNode.inputId);
			},
			beforeClick: function(treeId, treeNode, clickFlag) {
				return (treeNode.isParent === false); //限制只有叶子节点可以选中
			}
		}
	};
	
	var inputsTreeObj = $.fn.zTree.init($('#inputsTree'), zTreeSetting);
	
	//添加树节点
	for(gidx in setting.inputsGroups) {
		var group = setting.inputsGroups[gidx];
		var gnode = inputsTreeObj.addNodes(null, {name: group.label, title: group.desc, groupId: group.id}, true);
		for(iidx in group.inputs) {
			var i = group.inputs[iidx]; //get input object from group 
			var inode = inputsTreeObj.addNodes(gnode[0], {name: i.label, title: i.desc, inputId: i.id}, true);
		}
	}
	
	var criteriasListColumns = [ 
     	{name:'groupId', hidden: true}, 
	    {name:'inputId', hidden: true}, 
	    {name:'groupLabel', label:'组别', width:80, sortable:true}, 
	    {name:'inputLabel', label:'条件名', width:120, sortable:true}, 
	    {name:'operator', label:'表达式', width:90, sortable:false}, 
	    {name:'inputValue', label:'取值', width:374, sortable:false}, 
	    {name:'action',label:'操作', width:36, sortable:false}
	];
    
    var criteriasList = $("#criteriasList").jqGrid({
		datatype: 'local',
		width: 700,
		height: 380,
		viewrecords: true,
		scroll: true,
		url:queryUrl,
		colModel: criteriasListColumns,
		sortname: 'groupLabel',
		sortorder: "asc"
	});

	
	$('#criteriaConfig button[name=add]').click(function() {
		var nodes = inputsTreeObj.getSelectedNodes(inputsTreeObj);
		if(nodes.length == 0) {
			$.msgBox('info', '', '请在左侧的可选条件中选择所需的添加项！');
		}
		else {
			$.each(nodes, function(idx, node) { addCriteria(node.inputId); });
		}
		
		return false;
	});
	
	$('#criteriaConfig button[name=delete]').live('click', function() {
		criteriasList.delRowData($(this).val());
		return false;
	});
	
	var criteriaListRowId = 0;
	function addCriteria(inputId, criteria) {
		var i = cache.inputs[inputId];
		var g = cache.groups[i.groupId];
		var rowId = 'cid' + criteriaListRowId;
		
		
		var rowData = {
				groupId: i.groupId,
				inputId: i.inputId,
				groupLabel: g.label,
				inputLabel: i.label,
				operator: getOperatorHtml(i.allowedOperators, criteria),
				inputValue: getInputHtml(i, criteria),
				action: '<button name="delete" value="' + rowId + '" style="width:25px;height:20px;">-</button>'
		};

		criteriasList.addRowData(rowId, rowData, 'first');
		criteriaListRowId ++;
	}
	
	function getInputHtml(input, criteria) {
 		// TODO根据不同的录入类型得到相应的html代码
		return '<input type="text" class="input" name="inputTag" value=""></input>';
	}

 	//加入一行搜索条件 in SchemeConfig dialog
 	function getOperatorHtml(allowedOperators, criteria) {
 		html =	'<select name="operator">';
 		var sel = 'selected="selected"';
 		var ops = allowedOperators.split(',');
 		var selOp = null;
 		if(criteria != null) { selOp = criteria.operator; }
 		
		$.each(ops, function(idx, op) {
			var label = $.segment.operatorLabelMap[op];
			if(label != null) {
				html += '<option value="' + op + '" ' + ((op==selOp)?sel:'') + '>' + label + '</option>';
			}
			else {
				alert('Operator "' + op + '" not exists! Please check configuration!');
			}
		})
		html +=	'</select>';
		return html;
	}
	
	
	//unifyJqgridWidth($('.contentArea'));
	$('button').button();
	
});

</script>

</head>
<body>
<center>
<div class="contentArea">
	<form id="searchForm">
	<table width="100%" align="center" class="ui-widget ui-widget-content">
		<thead>
			<tr class="ui-widget-header">
				<td colspan="6">查询条件</td>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td align="right">编码: </td>
				<td><input type="text" name="queryParam.code"/></td>
				<td align="right">名称: </td>
				<td><input type="text" name="queryParam.name"/></td>
				<td align="right">更新时间: </td>
				<td> <input type="text" name="queryParam.updateTimeFrom"/>
				~ <input type="text" name="queryParam.updateTimeTo"/>
				</td>
			</tr>
		</tbody>
		<tfoot>
			<tr><td colspan="6" align="center">
				<button class="onSearch">查询</button>
			</td></tr>
		</tfoot>
	</table>
	</form>
	<br/>
	<div id="toolbar" style="text-align:left;">
		<button class="onNew">新增</button>
		<button class="onModify">修改</button>
		<button class="onDelete">删除</button>
	</div>
	<table id="resultList"></table>
	<div id="resultPager" style="text-align:center;"></div>
	
	<div align="center" id="inputDialog" title="客群信息">
	   	<form id="inputForm">
	   		<input name="maxInputIndex" type="hidden" />
	   		<input name="segment.segmentId" type="hidden"/>
	   		<table width="100%">
	   			<tbody>
				<tr>
				  <td width="90" align="right">编码: </td>
				  <td>00123</td>
				  <td width="90" align="right">名称: </td>
				  <td><input name="segment.name" type="text"/></td>
				</tr>
				<tr>
				  <td align="right">类型: </td>
				  <td>
				  	<select name="segment.segmentType" >
						<option value="会员客群">会员客群</option>
						<option value="销售客群">销售客群</option>
					</select>
				  </td>
				  <td align="right">排序: </td>
				  <td>
				  	<select name="segment.sortName" >
						<option value="注册时间">注册时间</option>
						<option value="最近消费时间">最近消费时间</option>
						<option value="级别">级别</option>
						<option value="剩余积分">剩余积分</option>
					</select>
					&nbsp;&nbsp;&nbsp;
					<input name="segment.sortOrder" type="radio" value="升序" checked="true">升序</input>
					<input name="segment.sortOrder" type="radio" value="降序">降序</input>
				  </td>
				</tr>
				<tr>
				  <td align="right">最大数量: </td>
				  <td>
					  <input name="segment.maxTotal" type="text"/>
				  </td>
				  <td align="right">实际数量: </td>
				  <td>
					  <span style="font-weight: bold;" name="segment.memberTotal">5081</span>
					  (<span name="segment.countTime">2013-05-30 12:31:23</span>)
					  <button id="onRecalculate">重新计算</button>
				  </td>
				</tr>
				</tbody>
	   		</table>

	   		<div id="criteriaConfig">
				<table width="100%" height="400" border="0" cellingspace="0">
					<tr>
						<td width="168"><strong>可选条件：</strong></td>
						<td width="32"></td>
						<td width="700"><table width="100%"><tr><td align="left"><strong>已选择条件:</strong></td><td align="right">常用方案：<select name="commonSchemes"><option value="1">价格敏感会员</option><option value="2">卖品喜好</option></select></td></tr></table></td>
					</tr>
					<tr>
						<td valign="top" >
							<div class="ui-widget ui-widget-content ui-corner-all" style="width:170px; height:400px; overflow:auto;">
							   <ul id="inputsTree" class="ztree"></ul>
							</div>
						</td>
						<td>
							<!-- 添加搜索添加的"+"按钮 -->
							<button name="add" style="width:32px;height:30px;"><strong>+</strong></button>
						</td>
						<td valign="top">
							<table id="criteriasList"></table>
						</td>
					</tr>
				</table>
			</div>
	   		
	   	</form>
	</div>
</div>
</center>
</body>
</html>

	