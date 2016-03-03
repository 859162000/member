<%@ page import="com.wanda.ccs.member.ap2in.*" %>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%
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
<title>客群短息发送</title>

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
$(document).ready(function(){ 
    var limitNum = 61; 
    var pattern = '还可以输入<font color="red">' + limitNum + '</font>字符'; 
    $('#contentwordage').html(pattern); 
    $('#content').keyup( 
    function(){ 
        var remain = $(this).val().length; 
        if(remain > 61){ 
        		$(this).val($(this).val().substring(0,61));
            }else{ 
                var result = limitNum - remain; 
                pattern = '还可以输入<font color="red">' + result + '</font>字符'; 
            } 
            $('#contentwordage').html(pattern); 
        } 

    ); 

}); 
var isNew = true;

var commonMothed = {};
$(function() {
	var commitControlCount = true;
	var resultList = $('#resultList');
	var searchForm = $('#searchForm');
	var messageForm = $('#messageForm');
	var resultList2 = $('#resultList2');
	var viewDialog = $('#viewDialog');
	var messageEditForm = $('#messageEditForm');
	
	var queryUrl = '<%=context%>/segment/SegmentAction/query.do';
	var toSaveUrl = '<%=context%>/segment/SegmentMessageAction/saveMessages.do';
	var queryMessageUrl = '<%=context%>/segment/SegmentMessageAction/query.do';	
	var toApproveUrl = '<%=context%>/segment/SegmentMessageAction/toApproveMassages.do';
	var getSegmentMessageUrl = '<%=context%>/segment/SegmentMessageAction/get.do';
	var saveUrl = '<%=context%>/segment/SegmentMessageAction/toSave.do';
	var deleteUrl = '<%=context%>/segment/SegmentMessageAction/delete.do';
	var checkWordUrl = '<%=context%>/segment/SegmentMessageAction/checkWord.do';
	var schemeChanged = false; //标示当前打开的修改窗口中的条件是否有更改。
	
	//To indicate the open user input dialog is new creation or not. 
	
	var girdColumns = [
						{name:'SEGMENT_ID', label:'客群ID', hidden:true},
						{name:'WORD_ID', label:'敏感字ID', hidden:true},
						{name:'WORD_CONTENT', label:'敏感字内容', hidden:true},
						{name:'EDITABLE', label:'EDITABLE', hidden:true},
						{name:'EXPORTABLE', label:'EXPORTABLE', hidden:true},
						{name:'CODE', label:'编码', width:70, sortable:true},
						{name:'WORD_TITLE', label:'敏感字标题', width:70, sortable:true},
						{name:'NAME', label:'名称', width:100, sortable:true},
						{name:'CAL_COUNT', label:'实际数量', width:50, sortable:false},
						{name:'CONTROL_COUNT', label:'对比组数量', width:50, sortable:false, 
							formatter: function(cellvalue, options, rowObject) { 
								if(cellvalue == -1 || cellvalue == null) {
									return '0';
								}
								return cellvalue;
							}
						},
						{name:'COMBINE_SEGMENT', label:'类型', width:50, sortable:false},
						{name:'UPDATE_DATE', label:'更新时间', width:70, sortable:true},
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
		multiselect:true,
		viewrecords: true,
		url:queryUrl,
		colModel: girdColumns,
		pager: $("#resultPager"),
		sortname: 'UPDATE_DATE',
		sortorder: "desc",
		onSelectRow: function(id) {			
			var newWordId = resultList.jqGrid('getCell', id, 'WORD_ID');
			var ids = resultList.jqGrid('getGridParam', 'selarrrow');
			var isChecked = $.inArray(id, ids);
			var wordContents = new Array();
			if (isChecked != -1) {
				var calCount = resultList.jqGrid('getCell', id, 'CAL_COUNT');
				if ("计算中..." == calCount) {
					resultList.jqGrid('setSelection',id);
					$.msgBox('error', '', "该客群人数正在计算中，请维护客群信息！");
					return;
				}
				if ("未计算" == calCount) {
					resultList.jqGrid('setSelection',id);
					$.msgBox('error', '', "该客群人数未计算，请维护客群信息！");
					return;
				} 
				if ("计算失败" == calCount) {
					resultList.jqGrid('setSelection',id);
					$.msgBox('error', '', "该客群人数计算失败，请维护客群信息！");
					return;
				} 
				var oldWordId = "";
				if(ids.length>1){
					oldWordId = resultList.jqGrid('getCell', ids[0], 'WORD_ID');
					if (newWordId != oldWordId) {
						  if(confirm("您所选择的客群对应的敏感字信息不一致，是否进行敏感字合并,如不合并则需重新选择客群信息！")){  
							  for (var i = 0; i < ids.length; i++) {
									var word = resultList.jqGrid('getCell', ids[i], 'WORD_CONTENT');
									if (word != 0 && word != "0") {
										wordContents = $.merge(wordContents, word.split("，"));
									}
								}
								var words = $.unique(wordContents);
							  	$("#word").html("" + words);
						  }else{  
							  resultList.jqGrid('setSelection',id);
						  }
					}
				} else {
					$("#word").html(resultList.jqGrid('getCell', id, 'WORD_CONTENT'));
				}
				
			} else {
				if(ids.length>0){
					 for (var i = 0; i < ids.length; i++) {
						var word = resultList.jqGrid('getCell', ids[i], 'WORD_CONTENT');
						if (word != 0 && word != "0") {
							wordContents = $.merge(wordContents, word.split("，"));
						}
					}
					var words = $.unique(wordContents);
				  	$("#word").html("" + words);
				} else {
					$("#word").html("");
				}
			}
			
		}
	});

	$('[wrType]', searchForm).wrender();
	$('[wrType]',messageForm).wrender();
	$('#onSearchBtn').click(reloadSearch);
	var validator = messageForm.validate({
		rules:{
			'sendTime':{required:true},
			'content':{required:true, maxlength:61}
		}
    });

	function reloadSearch(){
		var queryData =JSON.stringify( $('input[wrType],select[wrType]', searchForm).wrender('getValue') );
		var param = resultList.getGridParam();
		resultList.setGridParam({datatype:'json', postData:{'queryData' :queryData}, page:param.page}).trigger("reloadGrid");
		return false;
	}
	
	reloadSearch();
	jqgridAutoResize($('#contentArea'), resultList);
	$('button[wrType=button]').wrender();
	$('#contentArea').show();
	
	
	var girdColumns2 = [
						{name:'SEGM_MESSAGE_ID', label:'客群短信ID', hidden:true},
						{name:'SEGMENT_ID', label:'客群ID', hidden:true},
						{name:'APPROVEABLE', label:'是否已提交', hidden:true},
						{name:'EDITABLE', label:'是否可以修改信息', hidden:true},
						{name:'CODE', label:'客群编码', width:70, sortable:false},
						{name:'NAME', label:'客群名称', width:110, sortable:false},
						{name:'CAL_COUNT', label:'客群人数', width:70, sortable:false},
						{name:'NO_SEND_CAL', label:'不希望联络人数', width:50, sortable:false},
						{name:'SEND_STATUS', label:'短信发送状态', width:70, sortable:false},
						{name:'ACTIONS', label:'操作', width:50, align:'center'}
					];
	
	resultList2.jqGrid({
		datatype: 'local',
		ajaxGridOptions: {type:"POST"},
		jsonReader: {repeatitems: false},
		prmNames: {page:"queryParam.page", rows:"queryParam.rows", sort:"queryParam.sort", order: "queryParam.order"},
		height: "100%",
		rowNum: 10,
		rowList:[10,20,30],
		autowidth:true,
		shrinkToFit:true,
		multiselect:false,
		viewrecords: true,
		url:queryMessageUrl,
		colModel: girdColumns2,
		pager: $("#resultPager2"),
		sortname: 'UPDATE_DATE',
		sortorder: "desc",
		afterInsertRow: function(rowid, rowdata, rowelem) {
			var cellHtml = '<button name="onViewBtn" key=' + rowdata['SEGM_MESSAGE_ID'] + ' type="button" wrType="button" wrParam="icon:ui-icon-pencil;text:false" style="height:22px;" title="编辑"/>&nbsp;';
			cellHtml +=	'<button name="onDeleteBtn" key=' + rowdata['SEGM_MESSAGE_ID'] + ' type="button" wrType="button" wrParam="icon:ui-icon-trash;text:false" style="height:22px;" title="删除"/>';
			$(this).setCell(rowid, 'ACTIONS', cellHtml);
			$('button[wrType=button]', this).wrender();
		}
	});
	jqgridAutoResize($('#contentArea'), resultList2);
	jQuery("#resultList2").jqGrid('setCaption',"客群短信列表").trigger('reloadGrid');
	
	viewDialog.dialog({
		width: 730,
		buttons: [
            {text:'保存', click: toSave},
			{text:'关闭', click: function() { $('span:contains("提交")').show();$(this).dialog('close'); }}
		]
	});
	$(resultList2).on('click', 'button[name=onViewBtn]', function () {
        var segmMessageId = $(this).attr('key');
        var queryString = 'segmMessageId=' + segmMessageId;
	    $.ajax({
			   url: getSegmentMessageUrl,
			   data: queryString,
			   success: function(result) {
					$('[wrType]', viewForm).wrender('toReadOnly', result, true);
					$('[wrType]',messageEditForm).wrender();
					$('[wrType]', messageEditForm).wrender('setValue', result);
					viewDialog.dialog('open');
			   }
			});
	});
	$(resultList2).on('click', 'button[name=onDeleteBtn]', function (){
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
						   	var batchId = result.message;
							$("#batchId").val(batchId);
							jQuery("#resultList2").jqGrid('setGridParam',{url:queryMessageUrl,page:1});
							var queryData =JSON.stringify( $('input[wrType],select[wrType]', messageForm).wrender('getValue'));
							resultList2.setGridParam({datatype:'json', postData:{'queryData' :queryData}, page:1}).trigger("reloadGrid");
						   }
					   }
				});
			}
		});
	});
	$("#toSave").click(function(){
		if(validator.form() == false) {
	    	validator.focusInvalid();
	        return null;
	    }
		var content = $("#content").val();
		var word = $("#word").html();
		var postData = 'json='+content + '-W!O@R#D-' + word;
		$.ajax({
			url: checkWordUrl,
			data: postData,
			success: function(result){
				if(result.level == 'WARNING') {
					$.msgBox('error', '', result.message);
				} else {
					$("#wordContent").val($("#word").html());
					var ids = resultList.jqGrid('getGridParam', 'selarrrow');
					var segmentIds = "";
					if(ids.length>0){
						for(i=0;i<ids.length;i++){
			        		var segmentId = resultList.jqGrid('getCell', ids[i], 'SEGMENT_ID');
			        		segmentIds += "," + segmentId;
						}
					}
					var voData =$('[wrType]:not([wrType=readtext]):not([wrType=button])', messageForm).wrender('getValueData');
					var scheme = schemeAction.getSchemeData();
					var postData = 'json=' + voData + '&criteriaScheme=' + scheme + '&segmentIds='+segmentIds;
					$.ajax({
						url: toSaveUrl,
						data: postData,
						success: function(result){
							if(result.level == 'INFO') {
								reloadSearch();
								var batchId = result.message;
								$("#batchId").val(batchId);
								jQuery("#resultList2").jqGrid('setGridParam',{url:queryMessageUrl,page:1});
								var queryData =JSON.stringify( $('input[wrType],select[wrType]', messageForm).wrender('getValue'));
								resultList2.setGridParam({datatype:'json', postData:{'queryData' :queryData}, page:1}).trigger("reloadGrid");
							} else {
								$.msgBox('error', '', result.message);
							}
						
						}
					});
				}
			}
		});
		
	});
	$("#onSubmitButton").click(function(){
		var batchId = $("#batchId").val();
		if (batchId == null || batchId == "" || batchId == "null") {
			alert("您好，请先保存在提交审批！");
			return;
		}
		if(confirm("确定要提交审批？")){  
			var voData =$('[wrType]:not([wrType=readtext]):not([wrType=button])', messageForm).wrender('getValueData');
			var scheme = schemeAction.getSchemeData();
			var postData = 'json=' + voData + '&criteriaScheme=' + scheme;
			$.ajax({
				url: toApproveUrl,
				data: postData,
				success: function(result){
					$.msgBox('success', '', result.message);
					$("#batchId").val("");
					$("#content").val("");
					window.location.reload();//刷新当前页面.
				}
			});
		} else {
			return;
		}
		
	});
	
	$("#timely").click(function(){
		$("#timely").attr("checked",true);
		$("#timing").attr("checked",false);
		$("#sendTime").val("");
		$("#sendTime" ).rules( "remove" );
		$("#time").hide();
	});
	$("#timing").click(function(){
		$("#timely").attr("checked",false);
    	$("#timing").attr("checked",true);
    	$("#time").show();
    	$('input[type="text"][id="sendTime"]').each(function() {
    		$(this).rules('add', 'required');
    	});
	});
	function toSave(){
		var voData =$('[wrType]:not([wrType=readtext]):not([wrType=button])', messageEditForm).wrender('getValueData');
		var scheme = schemeAction.getSchemeData();
		var postData = 'json=' + voData + '&criteriaScheme=' + scheme;
			$.ajax({
				url: saveUrl,
				data: postData,
				success: function(result){
				viewDialog.dialog('close');
				reloadSearch();
				}
			});
	}
	
	$("#content").blur(function(){
		var content = $("#content").val();
		var word = $("#word").html();
		var postData = 'json='+content + '-W!O@R#D-' + word;
		$.ajax({
			url: checkWordUrl,
			data: postData,
			success: function(result){
				if(result.level == 'WARNING') {
					$.msgBox('error', '', result.message);
				}
			}
		});
	});
	
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
				<td colspan="8">筛选客群</td>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td width="10%" align="right">编码: </td>
				<td width="15%" align="left"><input type="text" wrType="text" name="code"/></td>
				<td width="10%" align="right">名称: </td>
				<td width="15%" align="left"><input type="text" wrType="text" name="name"/></td>
				<td width="10%" align="right">类型: </td>
				<td width="15%" align="left">
					<select name="segment_type" wrType="select" style="width: 138px;">
						<option value="-1">-- 请选择 --</option>
						<option value="1">普通客群</option>
						<option value="0">复合客群</option>
					</select>
			  	</td>
				<td colspan="2" align="center">
				<button type="button" id="onSearchBtn" wrType="button" wrParam="icon:ui-icon-search;">查询</button>
			</td>
			</tr>
		</tbody>
		
	</table>
	</form>
	<br/>
	<table id="resultList"></table>
	<div id="resultPager" style="text-align:center;"></div>
	<br/>
	<div>
	  <form id="messageForm">
	    <input id="batchId" name="batchId" type="hidden" wrType="text"/>
		<div id="criteriaSchemeView">
			<table width="100%" border="0" cellingspace="0">
				<thead>
					<tr class="ui-widget-header">
						<td colspan="2">短信内容</td>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td colspan="2">短信发送方式：
							<input type="radio" id="timely"/>及时发送
							<input type="radio" id="timing"  checked="checked"/>定时发送</td>
					</tr>
					<tr id="time">
						<td colspan="2">短信发送时间:<input type="text" wrType="datetime" id="sendTime" name="sendTime"/></td>
					</tr>
					<tr><td align="left">短信内容:</td></tr>
					<tr>
						<td ><textarea name="content" id="content" wrType="text" cols="100" rows="4"></textarea><div id="contentwordage"></div></td>
						<td >
							<input type="hidden" name="wordContent" wrType="text" id="wordContent" />
							<font color='red'>请注意敏感字如下:</font><br/><div id="word" style="width: 240px;"></div>
						</td>
					</tr>
					<tr>
						<td colspan="2" align="center">
							<button type="button" id="toSave" wrType="button">保存</button>
							<button type="button" id="onSubmitButton" wrType="button">提交</button>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		</form>
	</div>
	<br/>
	<table id="resultList2"></table>
	<div id="resultPager2" style="text-align:center;"></div>
	
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
	  <form id="messageEditForm">
	    <input name="segmMessageId" type="hidden" wrType="text"/>
	    <input name="approveStatus" type="hidden" wrType="text"/>
		<div id="criteriaSchemeView">
			<table width="100%" border="0" cellingspace="0">
				<tr><td width="25%" align="right"><strong>编辑短信信息</strong></td><td></td></tr>
				<tr><td width="25%" align="right">短信发送时间:</td><td><input type="text" wrType="datetime" name="sendTime"/></td></tr>
				<tr><td width="25%" align="right">短信发送内容:</td><td><textarea name="content" wrType="text"></textarea></td></tr>
			</table>
		</div>
		</form>
	</div>
</div>

<% if(fromAp2 == false) { %>
</center>
</body>
</html>
<% } %>