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
<title>客群信息发送及审批</title>

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
$(function() {
	var resultList = $('#resultList');
	var viewDialog = $('#viewDialog');
	var viewForm = $('#viewForm');
	var approveDialog = $('#approveDialog');
	var searchForm = $('#searchForm');
	var messageForm = $('#messageForm');
	var messageDialog = $('#messageDialog');
	var showForm = $('#showForm');
	var approveForm = $('#approveForm');
	var sendDialog = $('#sendDialog');
	var sendForm = $('#sendForm');
	
	var toApproveUrl = '<%=context%>/segment/SegmentMessageAction/toApprove.do';
	var toSendUrl = '<%=context%>/segment/SegmentMessageAction/toSend.do';
	var toSaveUrl = '<%=context%>/segment/SegmentMessageAction/toSave.do';
	var queryUrl = '<%=context%>/segment/SegmentMessageAction/query.do';
	var approveUrl = '<%=context%>/segment/SegmentMessageAction/approve.do';
	var getSegmentMessageUrl = '<%=context%>/segment/SegmentMessageAction/get.do';
	var deleteUrl = '<%=context%>/segment/SegmentMessageAction/delete.do';
	var checkWordUrl = '<%=context%>/segment/SegmentMessageAction/checkWord.do';

	//To indicate the open user input dialog is new creation or not. 
	var girdColumns = [
						{name:'SEGM_MESSAGE_ID', label:'客群短信ID', hidden:true},
						{name:'SEGMENT_ID', label:'客群ID', hidden:true},
						{name:'APPROVEABLE', label:'是否已提交', hidden:true},
						{name:'EDITABLE', label:'是否可以修改信息', hidden:true},
						{name:'CODE', label:'客群编码', width:70, sortable:false},
						{name:'NAME', label:'客群名称', width:110, sortable:false},
						{name:'CAL_COUNT', label:'客群人数', width:70, sortable:false},
						{name:'APPROVE_STATUS', label:'审批状态', width:50, sortable:false},
						{name:'SEND_STATUS', label:'短信发送状态', width:70, sortable:false},
						{name:'CREATE_BY', label:'创建人', width:50, sortable:false},
						{name:'UPDATE_DATE', label:'更新时间', width:50, sortable:false},
						{name:'ACTIONS', label:'操作', width:50, align:'center'}
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
		sortname: 'SEGM_MESSAGE_ID',
		sortorder: "desc",
		afterInsertRow: function(rowid, rowdata, rowelem) {
			//加入每行后的操作按钮
			if(rowdata['EDITABLE']&&('审批不通过'==rowdata['APPROVE_STATUS'] || '未提交审批'==rowdata['APPROVE_STATUS'])){
			    var cellHtml = '<button name="onViewBtn" key=' + rowdata['SEGM_MESSAGE_ID'] + ' approveStatus=' + rowdata['APPROVE_STATUS'] + ' approveAble=' + rowdata['APPROVEABLE'] + ' type="button" wrType="button" wrParam="icon:ui-icon-pencil;text:false" style="height:22px;" title="编辑客群短信"/>&nbsp;';
			    cellHtml +=	'<button name="onDeleteBtn" key=' + rowdata['SEGM_MESSAGE_ID'] + ' type="button" wrType="button" wrParam="icon:ui-icon-trash;text:false" style="height:22px;" title="删除"/>';
			}else{
			    var cellHtml = '<button name="onShowBtn" key=' + rowdata['SEGM_MESSAGE_ID'] + ' type="button" wrType="button" wrParam="icon:ui-icon-zoomin;text:false" style="height:22px;" title="查看客群短信"/>&nbsp;';
			}
			if (rowdata['APPROVEABLE']){
				cellHtml += '<button name="onApproveBtn" key=' + rowdata['SEGM_MESSAGE_ID'] + ' type="button" wrType="button" wrParam="icon:ui-icon-arrowthickstop-1-s;text:false" style="height:22px;" title="审批"/>&nbsp;';
			}
			if(rowdata['APPROVEABLE'] && '审批完成'==rowdata['APPROVE_STATUS']){
			    cellHtml += '<button name="onSendBtn" key=' + rowdata['SEGM_MESSAGE_ID'] + ' type="button" wrType="button" wrParam="icon:ui-icon-pencil;text:false" style="height:22px;" title="客群短信发送"/>&nbsp;';
			}
			$(this).setCell(rowid, 'ACTIONS', cellHtml);
			$('button[wrType=button]', this).wrender();
		}
	});
	

	viewDialog.dialog({
		width: 730,
		buttons: [
            {text:'提交', click: toApprove},
            {text:'保存', click: toSave},
			{text:'关闭', click: function() { $('span:contains("提交")').show();$(this).dialog('close'); }}
		]
	});
	
	approveDialog.dialog({
		width: 730,
		buttons: [
			{text:'批准', click: toPizhun},
            {text:'退回', click: toTuihui},
            {text:'撤消申请', click: toChexiao},
			{text:'关闭', click: function() { $(this).dialog('close'); }}
		]
	});
	
	messageDialog.dialog({
		width: 730,
		buttons: [
			{text:'关闭', click: function() { $(this).dialog('close'); }}
		]
	});
	
	sendDialog.dialog({
		width: 730,
		buttons: [
            {text:'发送', click: toSend},
			{text:'关闭', click: function() { $(this).dialog('close'); }}
		]
	});
	$('[wrType]', searchForm).wrender();
	
	$('#onSearchBtn').click(reloadSearch);
	
	$(resultList).on('click', 'button[name=onViewBtn]', function () {
        var segmMessageId = $(this).attr('key');
        var approveAble = $(this).attr('approveAble');
        var approveStatus = $(this).attr('approveStatus');
        var queryString = 'segmMessageId=' + segmMessageId;
	    $.ajax({
			   url: getSegmentMessageUrl,
			   data: queryString,
			   success: function(result) {
					$('[wrType]', viewForm).wrender('toReadOnly', result, true);
					$('[wrType]',messageForm).wrender();
					$('[wrType]', messageForm).wrender('setValue', result);
					if(approveStatus!='未提交审批'&&approveStatus.indexOf("退回修改")==-1){
						$('span:contains("提交")').hide();
					}
					viewDialog.dialog('open');
			   }
			});
	});

	$(resultList).on('click', 'button[name=onShowBtn]', function () {
        var segmMessageId = $(this).attr('key');
        var queryString = 'segmMessageId=' + segmMessageId;
	    $.ajax({
			   url: getSegmentMessageUrl,
			   data: queryString,
			   success: function(result) {
					$('[wrType]', showForm).wrender('toReadOnly', result, true);
					messageDialog.dialog('open');
					//combineAction.openDialog();
			   }
			});
	});
	
	$(resultList).on('click', 'button[name=onApproveBtn]', function () {
		 var segmMessageId = $(this).attr('key');
			var approveAble = $(this).attr('approveAble');
			var editAble = $(this).attr('editAble');
	        var queryString = 'segmMessageId=' + segmMessageId;
		//在界面上赋值
		 $.ajax({
			   url: getSegmentMessageUrl,
			   data: queryString,
			   success: function(result) {
					$('[wrType]', approveForm).wrender('setValue', result);
					approveDialog.dialog('open');
			   }
			});
	});

	$(resultList).on('click', 'button[name=onSendBtn]', function () {
		 var segmMessageId = $(this).attr('key');
	     var queryString = 'segmMessageId=' + segmMessageId;
		//在界面上赋值
		 $.ajax({
			   url: getSegmentMessageUrl,
			   data: queryString,
			   success: function(result) {
					$('[wrType]', sendForm).wrender('setValue', result);
					sendDialog.dialog('open');
			   }
			});
	});
	
	$(resultList).on('click', 'button[name=onDeleteBtn]', function (){
		var segmMessageId = $(this).attr('key');
		$.msgBox('confirm', '',  '是否要删除选中的客群短信？',  function(result) { 
			if(result == true) {
        		var queryString = 'deletes=' + segmMessageId;
				$.ajax({
					   url: deleteUrl,
					   data: queryString,
					   success: function(result) {
						   if(result.level == 'ERROR') {
							   $.msgBox('error', '', result.message);
						   }
						   else {
						   	reloadSearch();
						   }
					   }
				});
			}
		});
	});
	function reloadSearch(){
		var queryData =JSON.stringify( $('input[wrType],select[wrType]', searchForm).wrender('getValue'));
		resultList.setGridParam({datatype:'json', postData:{'queryData' :queryData}, page:1}).trigger("reloadGrid");
		return false;
	}

	reloadSearch();
	jqgridAutoResize($('#contentArea'), resultList);
	$('button[wrType=button]').wrender();
	$('#contentArea').show();

	function toApprove(){
		var voData =$('[wrType]:not([wrType=readtext]):not([wrType=button])', messageForm).wrender('getValueData');
		var scheme = schemeAction.getSchemeData();
		var postData = 'json=' + voData + '&criteriaScheme=' + scheme;
			$.ajax({
				url: toApproveUrl,
				data: postData,
				success: function(result){
				viewDialog.dialog('close');
				reloadSearch();
				}
			});
	}

	function toSave(){
		var content = $("#content").val();
		var word = $("#wordContent").val();
		var postData = 'json='+content + '-W!O@R#D-' + word;
		$.ajax({
			url: checkWordUrl,
			data: postData,
			success: function(result){
				if(result.level == 'WARNING') {
					$.msgBox('error', '', result.message);
				} else {
					var voData =$('[wrType]:not([wrType=readtext]):not([wrType=button])', messageForm).wrender('getValueData');
					var scheme = schemeAction.getSchemeData();
					var postData = 'json=' + voData + '&criteriaScheme=' + scheme;
					$.ajax({
						url: toSaveUrl,
						data: postData,
						success: function(result){
						viewDialog.dialog('close');
						reloadSearch();
						}
					});
				}
			}
		});
		
	}

	function toPizhun(){
		var voData =$('[wrType]:not([wrType=readtext]):not([wrType=button])', approveForm).wrender('getValueData');
		var scheme = schemeAction.getSchemeData();
		var postData = 'json=' + voData + '&criteriaScheme=' + scheme + '&approve=1';
		$.ajax({
			url: approveUrl,
			data: postData,
			success: function(result){
		    approveDialog.dialog('close');
		    reloadSearch();
			}
		});
	}
	
	function toTuihui(){
		var voData =$('[wrType]:not([wrType=readtext]):not([wrType=button])', approveForm).wrender('getValueData');
		var scheme = schemeAction.getSchemeData();
		var postData = 'json=' + voData + '&criteriaScheme=' + scheme + '&approve=0';
		$.ajax({
			url: approveUrl,
			data: postData,
			success: function(result){
		    alert(result.message);
		    approveDialog.dialog('close');
		    reloadSearch();
			}
		})
	}
	
	function toChexiao(){
		var voData =$('[wrType]:not([wrType=readtext]):not([wrType=button])', approveForm).wrender('getValueData');
		var scheme = schemeAction.getSchemeData();
		var postData = 'json=' + voData + '&criteriaScheme=' + scheme + '&approve=9';
		$.ajax({
			url: approveUrl,
			data: postData,
			success: function(result){
		    alert(result.message);
		    approveDialog.dialog('close');
		    reloadSearch();
			}
		})
	}
	
	function toSend(){
		var voData =$('[wrType]:not([wrType=readtext]):not([wrType=button])', sendForm).wrender('getValueData');
		var scheme = schemeAction.getSchemeData();
		var postData = 'json=' + voData + '&criteriaScheme=' + scheme;
		$.ajax({
			url: toSendUrl,
			data: postData,
			success: function(result){
		    alert(result.message);
		    sendDialog.dialog('close');
		    reloadSearch();
			}
		})
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
				<td width="15%" align="right">审批状态: </td>
				<td width="35%" align="left">
				<select name="approveStatus" wrType="select">
						<option value="">-- 请选择 --</option>
						<option value="1000">待影城经理审批</option>
						<option value="2000">待区域经理审批</option>
						<option value="3000">待院线会员经理审批</option>
						<option value="9999">审批不通过</option>
						<option value="9000">审批完成</option>
					</select>
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

	<div align="center" id="viewDialog" title="客群信息编辑">
	
		<form id="viewForm">
	   		<table width="100%">
	   			<tbody>
				<tr>
				  <td width="20%" align="right">客群编码: </td>
				  <td width="30%" align="left"><div name="code" wrType="readtext"></div></td>
				  <td width="20%" align="right">客群名称: </td>
				  <td width="30%" align="left"><div name="name" wrType="readtext"></div></td>
				</tr>
				<tr id="calCountRow">
				 <td width="20%" align="right">客群人数: </td>
				  <td width="30%" align="left">
					  <input name="calCountStr" type="text" wrType="text"/>
				  </td>
				  <td width="20%" align="right">不希望被联络人数: </td>
				  <td width="30%" align="left">
					  <span name="noSendCal" wrType="readtext"></span>
				  </td>
				</tr>
				</tbody>
	   		</table>
	   		 </form>
	  <form id="messageForm">
	    <input name="segmMessageId" type="hidden" wrType="text"/>
	    <input name="approveStatus" type="hidden" wrType="text"/>
		<div id="criteriaSchemeView">
			<table width="100%" border="0" cellingspace="0">
				<tr><td width="25%" align="right"><strong>编辑短信信息</strong></td><td></td></tr>
				<tr><td width="25%" align="right">短信发送时间:</td><td><input type="text" wrType="datetime" name="sendTime"/></td></tr>
				<tr><td width="25%" align="right">短信发送内容:</td><td><textarea id="content" name="content" wrType="text"></textarea></td>
					<td >
							<font color='red'>请注意敏感字如下:</font><br/>
							<textarea id="wordContent" name="wordContent" wrType="text" readonly="readonly"></textarea>
						</td></tr>
			</table>
		</div>
		</form>
	</div>
	
	<div align="center" id="approveDialog" title="客群审批">
		<form id="approveForm">
			    <input name="segmMessageId" type="hidden" wrType="text"/>
	   		<table width="100%">
	   			<tbody>
				<tr>
				  <td width="20%" align="right">客群编码: </td>
				  <td width="30%" align="left"><div name="code" wrType="readtext"></div></td>
				  <td width="20%" align="right">客群名称: </td>
				  <td width="30%" align="left"><div name="name" wrType="readtext"></div></td>
				</tr>
				<tr>
				  <td width="20%" align="right">客群人数: </td>
				  <td width="30%" align="left"><div name="calCountStr" wrType="readtext"></div></td>
				  <td width="20%" align="right">不希望联系人数: </td>
				  <td width="30%" align="left"><div name="noSendCal" wrType="readtext"></div></td>
				</tr>
				<tr>
				  <td width="20%" align="right">短信内容: </td>
				  <td width="30%" align="left"><div name="content" wrType="readtext"></div></td>
				  <td width="20%" align="right"></td>
				  <td width="30%" align="left"></td>
				</tr>
				<tr>
				  <td width="20%" align="right">短信发送时间: </td>
				  <td width="30%" align="left"><div name="sendTime" wrType="readtext"></div></td>
				  <td width="20%" align="right">申请人: </td>
				  <td width="30%" align="left"><div name="createBy" wrType="readtext"></div></td>
				</tr>
				<tr>
				  <td width="20%" align="right">审批意见: </td>
				  <td width="30%" align="left"><input type="text" name="approveSugg"  wrType="text"></input></td>
				  <td width="20%" align="right"> </td>
				  <td width="30%" align="left"></td>
				</tr>
				</tbody>
	   		</table>
   		</form>
	</div>
	<div align="center" id="messageDialog" title="客群信息查看">
		<form id="showForm">
	   		<table width="100%">
	   			<tbody>
				<tr>
				  <td width="20%" align="right">信息内容: </td>
				  <td width="30%" align="left"><div name="code" wrType="readtext"></div></td>
				  <td width="20%" align="right">发送人数: </td>
				  <td width="30%" align="left"><div name="name" wrType="readtext"></div></td>
				</tr>
				<tr>
				  <td width="20%" align="right">客群人数: </td>
				  <td width="30%" align="left"><div name="calCountStr" wrType="readtext"></div></td>
				  <td width="20%" align="right">不希望联系人数: </td>
				  <td width="30%" align="left"><div name="noSendCal" wrType="readtext"></div></td>
				</tr>
				<tr>
				  <td width="20%" align="right">短信内容: </td>
				  <td width="30%" align="left"><div name="content" wrType="readtext"></div></td>
				  <td width="20%" align="right"></td>
				  <td width="30%" align="left"></td>
				</tr>
				<tr>
				  <td width="20%" align="right">短信发送时间: </td>
				  <td width="30%" align="left"><div name="sendTime" wrType="readtext"></div></td>
				  <td width="20%" align="right">申请人: </td>
				  <td width="30%" align="left"><div name="createBy" wrType="readtext"></div></td>
				</tr>
				</tbody>
	   		</table>
   		</form>
	</div>
	<div align="center" id="sendDialog" title="客群信息发送">
		<form id="sendForm">
					    <input name="segmMessageId" type="hidden" wrType="text"/>
	   		<table width="100%">
	   			<tbody>
				<tr>
				  <td width="20%" align="right">信息内容: </td>
				  <td width="30%" align="left"><div name="code" wrType="readtext"></div></td>
				  <td width="20%" align="right">发送人数: </td>
				  <td width="30%" align="left"><div name="name" wrType="readtext"></div></td>
				</tr>
				<tr>
				  <td width="20%" align="right">客群人数: </td>
				  <td width="30%" align="left"><div name="calCountStr" wrType="readtext"></div></td>
				  <td width="20%" align="right">不希望联系人数: </td>
				  <td width="30%" align="left"><div name="noSendCal" wrType="readtext"></div></td>
				</tr>
				<tr>
				  <td width="20%" align="right">短信内容: </td>
				  <td width="30%" align="left"><div name="content" wrType="readtext"></div></td>
				  <td width="20%" align="right"></td>
				  <td width="30%" align="left"></td>
				</tr>
				<tr>
				  <td width="20%" align="right">短信发送时间: </td>
				  <td width="30%" align="left"><div name="sendTime" wrType="readtext"></div></td>
				  <td width="20%" align="right">申请人: </td>
				  <td width="30%" align="left"><div name="createBy" wrType="readtext"></div></td>
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