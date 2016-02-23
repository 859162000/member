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
	var searchForm = $('#searchForm');
	var messageDialog = $('#messageDialog');
	
	var queryUrl = '<%=context%>/segment/SegmentMessageAction/query.do';
	var getSegmentMessageUrl = '<%=context%>/segment/SegmentMessageAction/get.do';
	
	//To indicate the open user input dialog is new creation or not. 
	var girdColumns = [
						{name:'SEGM_MESSAGE_ID', label:'客群短信ID', hidden:true},
						{name:'SEGMENT_ID', label:'客群ID', hidden:true},
						{name:'APPROVEABLE', label:'是否已提交', hidden:true},
						{name:'EDITABLE', label:'是否可以修改信息', hidden:true},
						{name:'CODE', label:'客群编码', width:70, sortable:false},
						{name:'NAME', label:'客群名称', width:110, sortable:false},
						{name:'CAL_COUNT', label:'客群人数', width:70, sortable:false},
						{name:'CREATE_BY', label:'创建人', width:50, sortable:false},
						{name:'NO_SEND_CAL', label:'不希望联络人数', width:50, sortable:false},
						{name:'SEND_STATUS', label:'短信发送状态', width:70, sortable:false},
						{name:'SEND_TIME',label:'短信发送时间',width:50,sortable:false},
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
			var cellHtml = '<button name="onShowBtn" key=' + rowdata['SEGM_MESSAGE_ID'] + ' type="button" wrType="button" wrParam="icon:ui-icon-zoomin;text:false" style="height:22px;" title="查看客群短信"/>&nbsp;';
			$(this).setCell(rowid, 'ACTIONS', cellHtml);
			$('button[wrType=button]', this).wrender();
		}
	});
	messageDialog.dialog({
		width: 730,
		buttons: [
			{text:'关闭', click: function() { $(this).dialog('close'); }}
		]
	});
	
	
	$('[wrType]', searchForm).wrender();
	
	$('#onSearchBtn').click(reloadSearch);

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

	function reloadSearch(){
		var queryData =JSON.stringify( $('input[wrType],select[wrType]', searchForm).wrender('getValue'));
		resultList.setGridParam({datatype:'json', postData:{'queryData' :queryData}, page:1}).trigger("reloadGrid");
		return false;
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
</div>

<% if(fromAp2 == false) { %>
</center>
</body>
</html>
<% } %>