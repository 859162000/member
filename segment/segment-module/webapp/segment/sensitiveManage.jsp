<%@ page import="com.wanda.ccs.member.ap2in.*" %>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%
boolean testFlag = ("true".equals(request.getParameter("test")));//用户测试的标志
boolean fromAp2 = AuthUserHelper.isFromAp2(request);//是否为AP2发来的请求
UserProfile user = AuthUserHelper.getUser();
boolean viewRight = user.getRights().contains("member.sensit.view");
boolean editRight = user.getRights().contains("member.sensit.edit");
System.out.println("viewRight:"+viewRight+"editRight:"+editRight);

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
<script type="text/javascript">
	var context = '<%=context%>';
</script>
<script type="text/javascript" src="<%=context%>/segment/criteria-scheme.js"></script>
<script type="text/javascript" src="<%=context%>/segment/segment.wrender.ext.js"></script>
<script type="text/javascript" src="<%=context%>/segment/segment.settings.member.js"></script>
<script type="text/javascript" src="<%=context%>/segment/js/combineSegment.js"></script>
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
var isNew = true;

var commonMothed = {};

$(function() {
	var commitControlCount = true;
	var testFlag = <%=testFlag%>;
	var inputDialog = $('#inputDialog');
	var resultList = $('#resultList');
	var inputForm = $('#inputForm');
	var viewDialog = $('#viewDialog');
	var viewForm = $('#viewForm');
	var downloadDialog = $('#downloadDialog');
	//var downloadForm = $('#downloadForm');
	var searchForm = $('#searchForm');
	//var calCountDialog = $('#calCountDialog');
	var selectInputDialog = $('#selectInputDialog');
	var controlCountDialog = $('#controlCountDialog');
	
	var queryUrl = '<%=context%>/segment/SensitiveWordAction/query.do';
	var getUrl = '<%=context%>/segment/SensitiveWordAction/get.do';
	var insertUrl = '<%=context%>/segment/SensitiveWordAction/insert.do';
	var updateUrl = '<%=context%>/segment/SensitiveWordAction/update.do';
	var deleteUrl = '<%=context%>/segment/SensitiveWordAction/delete.do';
	var criteriaResultUrl = '<%=context%>/segment/SensitiveWordAction/getCriteriaResult.do';
	var calCountTimerId = null; //calculating timer id
	
	var schemeChanged = false; //标示当前打开的修改窗口中的条件是否有更改。
	
	var validator = inputForm.validate({
		rules:{
			'wordTitle':{required:true, maxlength:52},
			'sortName':{required:true}
		}
    });

	var girdColumns = [
						{name:'WORD_ID', label:'敏感词编码', hidden:true},
						{name:'WORD_ID', label:'敏感词编码', width:70, sortable:true},
						{name:'WORD_TITLE', label:'敏感词标题', width:100, sortable:true},
						{name:'WORD_CONTENT', label:'敏感词内容', width:50, sortable:false},
						{name:'CREATE_BY', label:'创建人', width:60, sortable:false},
						{name:'UPDATE_BY', label:'更新人', width:60, sortable:false},
						{name:'UPDATE_DATE', label:'更新时间', width:70, sortable:true},
						{name:'ACTIONS', label:'操作', width:90, align:'center'}
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
			<% if(editRight|| viewRight) { %>
				var cellHtml = '<button name="onViewBtn" key=' + rowdata['WORD_ID']+' type="button" wrType="button" wrParam="icon:ui-icon-zoomin;text:false" style="height:22px;" title="查看"/>&nbsp;';
				<%} if(editRight){%>cellHtml += '<button name="onModifyBtn" key=' + rowdata['WORD_ID'] +' rowid=' + rowid + ' type="button" wrType="button" wrParam="icon:ui-icon-pencil;text:false" style="height:22px;" title="修改"/>&nbsp;';
				cellHtml += '<button name="onDeleteBtn" key=' + rowdata['WORD_ID'] + ' type="button" wrType="button" wrParam="icon:ui-icon-trash;text:false" style="height:22px;" title="删除"/>';
				<%} %>
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
	
	$('[wrType]', searchForm).wrender();
	$('[wrType]', inputForm).wrender();
	$('[wrType]', viewForm).wrender();
	
	$('#onSearchBtn').click(reloadSearch);
	
	$('#onNewBtn').click(function (){
		/* selectInputDialog.dialog('open'); */
		onNewCommon();
	});
	
	//打开普通客群的新建界面
	function onNewCommon() {
		isNew = true;
		inputForm.clearForm();
	//	$('[wrType]', inputForm).wrender('setValue', {code:'', sortName:'00',controlCountRate:'10',calCount:'',controlCount:'',calCountTime:'', sortOrder:'asc', maxCount:'100000',compAllowModifier: null});
	//	sortNameChange('00');
	//	setCalCount(0, '', '10', 0, '20', false); //因为新建后自动执行后台计算，这里隐藏计算数量行, 状态被设为'20'表示‘计算中’
		schemeAction.applyScheme($.segment.settings.member, null);//创建初始化客群列表
		
		inputDialog.dialog('open');
	}
	
	$(resultList).on('click', 'button[name=onModifyBtn]', function (){
		isNew = false;
		var wordId = $(this).attr('key');
			var rowid = $(this).attr('rowid');
			var CREATE_BY = resultList.jqGrid('getCell', rowid, 'CREATE_BY');
	        var queryString = 'wordId=' + wordId;
			$.ajax({
				url: getUrl,
				data: queryString,
				success: function(result) {
					if(result.allowModify == false && testFlag == false) {
					    $.msgBox('info', '', '对不起，您是未授权用户，不能进行修改！');
					    return;
					}
					
					sortNameChange(result.sortName);
					//viewStatus(result);
					$('[wrType]', inputForm).wrender('setValue', result);
					
					var schemeObj = $.parseJSON(result.criteriaScheme);	
					schemeChanged = false;
					schemeAction.applyScheme($.segment.settings.member, schemeObj, schemeChange);
					
					inputDialog.dialog('open');
				}
			});
	});
	
	$(resultList).on('click', 'button[name=onViewBtn]', function () {
		var wordId = $(this).attr('key');
        var queryString = 'wordId=' + wordId;
			$.ajax({
			   url: getUrl,
			   data: queryString,
			   success: function(result) {
					$('[wrType]', viewForm).wrender('toReadOnly', result, true);
					
					//隐藏或显示sortOrder录入域
					var sortOrderElm = $('select[name="sortOrder"]', viewForm).prev('span[name=readlabel]');
					var schemeObj = $.parseJSON(result.criteriaScheme);
					
					schemeAction.applySchemeView($.segment.settings.member, schemeObj);
					viewDialog.dialog('open');
			   }
			});
	});
	
	$(resultList).on('click', 'button[name=onDeleteBtn]', function (){
		var wordId = $(this).attr('key');
		$.msgBox('confirm', '',  '是否要删除选中的敏感词信息？',  function(result) { 
			if(result == true) {
        		var queryString = 'wordId=' + wordId;
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
	
	
	function schemeChange(value) {
		schemeChanged = true;
	}
	
	function sortNameChange(val) {
		if(val == '00') {
			$('select[name="sortOrder"]', inputForm).hide();
			$('select[name="sortOrder"]', inputForm).wrender('setValue', {sortOrder:'asc'});
		}
		else {
			$('select[name="sortOrder"]', inputForm).show();
		}
		//标示客群数据已变化，需要重新保存和计算数量。
		schemeChanged = true;
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
	
	
	function reloadSearch(){
		var queryData =JSON.stringify( $('input[wrType],select[wrType]', searchForm).wrender('getValue') );
		var param = resultList.getGridParam();
		resultList.setGridParam({datatype:'json', postData:{'queryData' :queryData}, page:param.page}).trigger("reloadGrid");
		return false;
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
		// 如果排序方式为【10 可兑换积分余额 exchangePoint】
		var sortVal = $('select[name="sortName"]', inputForm).val();
		var scheme = schemeAction.getSchemeData();
		
		if(sortValidate(sortVal, scheme) == false) {
			return null;
		}
		
		var postData = 'json=' + voData + '&criteriaScheme=' + scheme;
		return postData;
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
	jqgridAutoResize($('#contentArea'), resultList);
	$('button[wrType=button]').wrender();
	$('#contentArea').show();

	commonMothed.closeDialog = function(message, version) {
		schemeChanged = false; //清除修改更新标示位
		$('input[name="version"]', inputForm).wrender('setValue', {version: version});
		if(isNew) {
			$.msgBox('info', '', message, function(result) {
				reloadSearch();
				combineAction.inputCombineSegmentDialog.dialog('close');
			});
		} else {
			$.msgBox('yesno', '确认', message + "<br/>选择'是'关闭该窗口；'否'保留该窗口继续编辑。", function(result) {
				reloadSearch();
				if(result == true) {
					combineAction.inputCombineSegmentDialog.dialog('close');
				}
			});
		}
	}

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
				<td width="20%" align="right">敏感词编码: </td>
				<td width="30%" align="left"><input type="text" wrType="text" name="wordId"/></td>
				<td width="15%" align="right">敏感词标题: </td>
				<td width="35%" align="left"><input type="text" wrType="text" name="wordTitle"/></td>
			</tr>
			<tr>
				<td width="20%" align="right">创建人: </td>
				<td width="30%" align="left">
					<input type="text" wrType="text" name="createBy" />
			  	</td>
				<td width="15%" align="right"> </td>
				<td width="35%" align="left">
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
		<% } %>
	</div>
	<table id="resultList"></table>
	<div id="resultPager" style="text-align:center;"></div>
	<div align="center" id="inputDialog" title="敏感词信息">
	   	<form id="inputForm">
	   		<input name="wordId" type="hidden" wrType="text"/>
	   		<input name="version" type="hidden" wrType="text"/>
	   		<input name="status" type="hidden" wrType="text"/>
	   		
	   		<table width="100%">
	   			<tbody>
				<tr>
				  <td width="25%" align="right">敏感词标题: </td>
				  <td width="50%" align="left"><input name="wordTitle" type="text" wrType="text" size="32"/>
				  </td>
				</tr>
				<tr>
				  <td width="25%" align="right">敏感词内容: </td>
				  <td width="50%" align="left"><textarea name="wordContent" type="text" wrType="text" style="width:300px;height:100px;"></textarea>
					<span style="color:red">敏感词之间用逗号','分割</span></td>
				</tr>
				</tbody>
	   		</table>
	   	</form>
	</div>
	
	
	<div align="center" id="viewDialog" title="查看敏感词信息">
		<form id="viewForm">
	   		<table width="100%">
	   			<tbody>
				<tr>
				  <td width="10%" align="right">敏感词编码: </td>
				  <td width="30%" align="left"><div name="wordId" wrType="readtext"></div></td>
				  <td width="15%" align="right">敏感词标题: </td>
				  <td width="45%" align="left"><div name="wordTitle" wrType="readtext"></div></td>
				</tr>
				<tr>
				   <td width="10%" align="right">创建人: </td>
				  <td width="30%" align="left">
					  <input name="createBy" type="text" wrType="text"/>
				  </td>
				  <td width="10%" align="right">创建时间: </td>
				  <td width="50%" align="left">
					  <input name="createDate" type="text" wrType="text"/>
				  </td>
				</tr>
				<tr id="calCountRow">
				  <td width="20%" align="right">敏感词内容: </td>
				  <td width="40%" align="left">
					  <span name="wordContent" wrType="readtext"></span>
				  </td>
				  <td width="5%" align="right"> </td>
				  <td width="25%" align="left">
					
				  </td>
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