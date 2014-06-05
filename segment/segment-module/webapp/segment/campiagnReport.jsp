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
} else {
	context = request.getContextPath();
}
%>

<% if(fromAp2 == false) { %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>营销活动添加</title>

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
<script type="text/javascript" src="<%=context%>/segment/segment.settings.campaign.js"></script>

<style type="text/css">
/*自动扩展行高 */
.ui-jqgrid tr.jqgrow td {  
    height: 25px;
	text-overflow: ellipsis;
}

#criteriaSchemeView .jqgrow td {
	white-space: normal !important;
	/*height:auto;
	vertical-align:text-top;*/
	height: 25px;    /* row 高度 */
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

#seachBody td {
	height: 30px;
}

#viewBody td {
	height: 30px;
}

</style>

<script type="text/javascript">

var detailDialog;
var detailDialogAction = {};

// 初始化明细对话框
function initDetailDialog() {
	var queryUrl = $.wrender.context+'/segment/CampaignAction/queryReport.do';
	var getUrl = $.wrender.context+'/segment/CampaignAction/get.do';
	
	// 初始化
	var viewForm = $('#viewForm');
	var viewBody = $('#viewBody');
	var resultList = $('#detailList');
	var tabs = $("#tabs");
	
	var girdColumns = [
			{name:'YMD', label:'统计日期', width:60, sortable:true, align:'center'},
			{name:'RECOMMEND', label:'推荐响应（率）', width:60, sortable:false, align:'center'},
			{name:'CONTROL', label:'对比组响应（率）', width:60, sortable:false, align:'center'},
			{name:'SUM', label:'总响应（率）', width:60, sortable:false, align:'center'},
			{name:'RECOMMEND_INCOME', label:'推荐响应收入', width:60, sortable:false, align:'center'},
			{name:'CONTROL_INCOME', label:'对比组响应收入', width:60, sortable:false, align:'center'},
			{name:'SUM_INCOME', label:'总响应收入', width:60, sortable:false, align:'center'}
		];
	
	resultList.jqGrid({
		datatype: 'local',
		ajaxGridOptions: {type:"POST"},
		jsonReader: {repeatitems: false},
		prmNames: {page:"queryParam.page", rows:"queryParam.rows", sort:"queryParam.sort", order: "queryParam.order"},
		height: "100%",
		rowNum: 5,
		rowList:[5],
		autowidth:true,
		shrinkToFit:true,
		multiselect: false,
		viewrecords: true,
		url:queryUrl,
		colModel: girdColumns,
		pager: $("#detailPager"),
		sortname: 'YMD',
		sortorder: "desc"
	});
	
	resultList.jqGrid('setGroupHeaders', {
		useColSpanStyle: true, // 没有表头的列是否与表头列位置的空单元格合并
		groupHeaders: [ 
		    {
				startColumnName: 'RECOMMEND', // 对应colModel中的name
				numberOfColumns: 3, // 跨越的列数
				titleText : '响应人数'
			},{
				startColumnName: 'RECOMMEND_INCOME', // 对应colModel中的name
				numberOfColumns: 3, // 跨越的列数
				titleText : '响应收入'
			}
		]
	});
	
	//加载列表
	detailDialogAction.reloadSearch = function(cid) {
		$("input[name=cid]", viewBody).val(cid);
		postCommit(getUrl, 'cid='+cid, function(result) {
			if(result != null) {
				result.cinemaRange = result.cinemaRange.replace('<span style="font-weight:bold;">影城名称: </span>','');
				var index = result.cinemaRange.length / 60;
				if(index < 1) {
					index = 1;
				} else if(index > 5) {
					index = 5;
				}
				$('[name=cinemaRange]', viewBody).css('height',(index * 14) + 'px');
				$('[wrType]', viewBody).wrender('setValue', result);
				setValue('segmentName', result.segmentName);
				/* setValue('code', result.code);
				setValue('name', result.name);
				setValue('startDate', result.startDate);
				setValue('endDate', result.endDate);
				setValue('status', result.status);
				setValue('cinemaRange', result.cinemaRange);
				setValue('segmentName', result.segmentName);
				setValue('segmentNumber', result.calCount);
				setValue('segmentTime', result.calCountTime);
				setValue('createBy', result.createBy);
				setValue('createDate', result.createDate);
				setValue('updateBy', result.updateBy);
				setValue('updateDate', result.updateDate);
				setValue('description', result.description); */
				
				
				if(result.criteriaScheme == null) {
					$("#tab2_name").html('无推荐响应配置');
					$("#tabs").tabs({ disabled: [1] });
				} else {
					var schemeObj = $.parseJSON(result.criteriaScheme);
					schemeAction.applySchemeView($.segment.settings.campaign, schemeObj);
				}
			}
		});
		
		var queryData =JSON.stringify( $('input[name=type],input[name=cid]', viewBody).wrender('getValue'));
		resultList.setGridParam({datatype:'json', postData:{'queryData' :queryData}, page:1}).trigger("reloadGrid");
		
		return false;
	}
	
	//reloadSearch();
	jqgridAutoResize($('#contentArea'), resultList);//跟随窗口大小自动调整列表宽度
	//渲染查询框
	$('[wrType=button]').wrender();
	
	$('#detailTable').css('background', '#EEEEEE');
	$("#gbox_schemeView").css('height','280px');
	
	tabs.tabs(/* {event: "mouseover"} */);
	
	detailDialog = $('#detailDialog');
	
	$('[wrType]', detailDialog).wrender();
	
	detailDialog.dialog({
		 autoOpen: false,
		 height: 680,
		 width: 900,
		 modal: true,
		 buttons: [
		 	{id:'onCloseBtn', text:'关闭', click: function() { $(this).dialog('close'); }}
		 ],
		 beforeclose: function(event, ui) {
			 tabs.tabs('option', 'selected', 0);
		 }
	});
	
	jqgridAutoResize(detailDialog, resultList);//跟随窗口大小自动调整列表宽度
	
	$('.ui-tabs-nav').removeClass('ui-widget-header').css({'padding':'0','background':'#EEEEEE'});
	$('.ui-dialog').css('background','#EEEEEE');
	
	//detailDialog.dialog("open");
}

function postCommit(url, postData, callback) {
	$.ajax({
		url: url,
		data: postData,
		success: function(result) {
			callback(result);
		}
	});
}

function setValue(name, value) {
	$('[name='+name+']', viewBody).html(value);
}

$(function(){
	var queryUrl = $.wrender.context+'/segment/CampaignAction/queryReport.do';
	
	// 初始化
	var searchForm = $('#searchForm');
	var searchBody = $('#seachBody');
	var resultList = $('#resultList');
	
	var formatSize = function(cellvalue) {
		if(cellvalue.indexOf('影城名称') > 0) {
			var index = cellvalue.lastIndexOf('</span>');
			return cellvalue.substring(0, index) + cellvalue.substring(index, index+10);
		} else {
			return cellvalue.substring(0, index);
		}
	}
	
	var girdColumns = [
					{name:'CAMPAIGN_ID', label:'CAMPAIGN_ID', hidden:true},
					{name:'READABLE', label:'READABLE', hidden:true},
					{name:'NAME', label:'活动名称', width:50, sortable:false, align:'center'},
					{name:'START_DATE', label:'开始时间', width:45, sortable:false, align:'center'},
					{name:'END_DATE', label:'结束时间', width:45, sortable:false, align:'center'},
					{name:'CINEMA_RANGE', label:'影城范围', width:60, sortable:false},
					{name:'STATUS_INFO', label:'状态', width:40, sortable:false, align:'center'},
					{name:'RECOMMEND_COUNT', label:'推荐人数', width:45, sortable:false, align:'center'},
					{name:'CONTROL_COUNT', label:'对比组人数', width:45, sortable:false, align:'center'},
					{name:'RECOMMEND', label:'推荐响应（率）', width:60, sortable:false, align:'center'},
					{name:'CONTROL', label:'对比组推荐响应（率）', width:60, sortable:false, align:'center'},
					{name:'SUM', label:'总响应（率）', width:60, sortable:false, align:'center'},
					{name:'RECOMMEND_INCOME', label:'推荐响应收入', width:50, sortable:false, align:'center'},
					{name:'CONTROL_INCOME', label:'对比组响应收入', width:50, sortable:false, align:'center'},
					{name:'SUM_INCOME', label:'总收入', width:50, sortable:false, align:'center'},
					{name:'ACTIONS', label:'操作', width:40, align:'center', sortable:false}
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
		sortname: 's.CAMPAIGN_ID',
		sortorder: "desc",
		afterInsertRow: function(rowid, rowdata, rowelem) {
			//加入每行后的操作按钮
			//if(rowdata['READABLE']) {
			var cellHtml = '<button name="onDetailBtn" key=' + rowdata['CAMPAIGN_ID'] + ' type="button" wrType="button" wrParam="icon:ui-icon-zoomin;text:false" style="height:22px;" title="明细"/>';
			$(this).setCell(rowid, 'ACTIONS', cellHtml);
			$('button[wrType=button]', this).wrender();
			//}
		}
	});
	
	resultList.jqGrid('setGroupHeaders', {
		useColSpanStyle: true, // 没有表头的列是否与表头列位置的空单元格合并
		groupHeaders: [ 
		    {
				startColumnName: 'NAME', // 对应colModel中的name
				numberOfColumns: 7, // 跨越的列数
				titleText : '活动基本信息'
			},{
				startColumnName: 'RECOMMEND', // 对应colModel中的name
				numberOfColumns: 3, // 跨越的列数
				titleText : '响应人数'
			},{
				startColumnName: 'RECOMMEND_INCOME', // 对应colModel中的name
				numberOfColumns: 3, // 跨越的列数
				titleText : '响应收入'
			}
		]
	});

	$('#onSearchBtn').click(function() {
		reloadSearch();
	});
	
	$('#onResetBtn').click(function() {
		var nullValue = {
			code : null,
			name : null,
			startDateFrom : null,
			startDateTo : null,
			endDateFrom : null,
			endDateTo : null,
			status : ''
		};
		
		$('[wrType]', searchForm).wrender('setValue', nullValue);
	});
	
	//修改
	$(resultList).on('click', 'button[name=onDetailBtn]', function() {
		detailDialogAction.reloadSearch($(this).attr('key'));
		detailDialog.dialog('open');
	});
	
	//加载列表
	function reloadSearch() {
		var queryData =JSON.stringify( $('input[wrType],select[wrType]', searchBody).wrender('getValue'));
		resultList.setGridParam({datatype:'json', postData:{'queryData' :queryData}, page:1}).trigger("reloadGrid");
		
		return false;
	}
	
	reloadSearch();
	jqgridAutoResize($('#contentArea'), resultList);//跟随窗口大小自动调整列表宽度
	//渲染查询框
	$('[wrType]', searchForm).wrender();
	
	// 显示控件
	$("#contentArea").show();
	
	initDetailDialog();
	
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
					<td colspan="4" height="23px">查询条件</td>
				</tr>
			</thead>
			<tbody id="seachBody">
				<input type="hidden" wrType="text" name="type" value="1"/>
				<tr>
					<td width="15%" align="right">编码: </td>
					<td width="35%" align="left"><input type="text" wrType="text" name="code"/></td>
					<td width="15%" align="right">名称: </td>
					<td width="35%" align="left"><input type="text" wrType="text" name="name"/></td>
				</tr>
				<tr>
					<td width="15%" align="right">开始时间: </td>
					<td width="35%" align="left">
						<input type="text" wrType="date" name="startDateFrom"/> ~ <input type="text" wrType="date" name="startDateTo"/>
					</td>
					<td width="15%" align="right">结束时间: </td>
					<td width="35%" align="left">
						<input type="text" wrType="date" name="endDateFrom"/> ~ <input type="text" wrType="date" name="endDateTo"/>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right">状态: </td>
					<td width="35%" align="left">
						<!-- 
						<select name="selectInput" wrType="select" wrParam="sourceId:dimdef;typeId:179">
					  		<option value="">-- 请选择 --</option>
					  	</select>
					  	 -->
					  	 <select name="status" wrType="select" style="width: 138px;">
							<option value="">-- 请选择 --</option>
							<option value="30">执行</option>
							<option value="40">结束</option>
						</select>
					</td>
					<td width="15%" align="right"></td>
					<td width="35%" align="left"></td>
				</tr>
			</tbody>
			<tfoot>
				<tr>
					<td colspan="4" align="center">
						<button type="button" id="onSearchBtn" wrType="button" wrParam="icon:ui-icon-search;">查询</button>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<button type="button" id="onResetBtn" wrType="button" wrParam="icon:ui-icon-search;">重置</button>
					</td>
				</tr>
			</tfoot>
		</table>
	</form>
	<br/>
	
	<!-- 明细列表 -->
	<table id="resultList"></table>
	<div id="resultPager" style="text-align:center;"></div>
</div>

<div id="detailDialog" title="活动统计明细" style="width:100%; display:none;">
	<div id="tabs" style="border: 0px;background-color: #EEEEEE;padding: 0;padding-bottom: 4px;">
	  <ul>
	    <li><a id="tab1_name" href="#campaignBaseInfo">活动基本信息</a></li>
	    <li><a id="tab2_name" href="#campaignCriteria">推荐响应规则配置</a></li>
	  </ul>
			<div id="campaignBaseInfo" style="border: 1px solid #FBD850; background: #EEEEEE;height: 300px;">
			  <p id='tab1'>
					<form id="viewForm">
					<table width="100%" align="center" id="detailTable">
						<tbody id="viewBody">
							<input type="hidden" wrType="text" name="cid" value=""/>
							<input type="hidden" wrType="text" name="type" value="0"/>
							<tr>
								<td width="10%" align="right" >编码: </td>
								<td width="40%" align="left"><span wrType="readtext" name="code"></span></td>
								<td width="10%" align="right">名称: </td>
								<td width="40%" align="left"><span wrType="readtext" name="name"></span></td>
							</tr>
							<tr>
								<td width="15%" align="right" >开始时间: </td>
								<td width="35%" align="left">
									<span wrType="readtext" name="startDate"></span>
								</td>
								<td width="15%" align="right">结束时间: </td>
								<td width="35%" align="left">
									<span wrType="readtext" name="endDate"></span>
								</td>
							</tr>
							<tr>
								<td width="15%" align="right">影城范围: </td>
								<td width="35%" colspan="3" align="left"><div wrType="readtext" name="cinemaRange" style="overflow-y:auto;height: 14px;width: 650px;"></div></td>
							</tr>
							<tr>
								<td width="15%" align="right" >状态: </td>
								<td width="35%" align="left"><span wrType="readtext" name="status"></span></td>
								<td width="15%" align="right"></td>
								<td width="35%" align="left"></td>
							</tr>
							<tr>
								<td width="15%" align="right" >描述: </td>
								<td width="35%" colspan="3" align="left">
									<span wrType="readtext" name="description"></span>
								</td>
							</tr>
							<tr>
								<td width="15%" align="right">指定客群: </td>
								<td width="35%" align="left">
									<span wrType="readtext" name="segmentName" style="float: left;padding-right: 5px;"></span>
								</td>
								<td width="15%" align="right">客群总数: </td>
								<td width="35%" align="left"><span wrType="readtext" name="calCount"></span>&nbsp;&nbsp;&nbsp;计算于:<span wrType="readtext" name="calCountTime"></span></td>
							</tr>
							<tr>
								<td width="15%" align="right">创建信息: </td>
								<td width="35%" align="left"><span wrType="readtext" name="createBy"></span>&nbsp;&nbsp;<span wrType="readtext" name="createDate"></span></td>
								<td width="15%" align="right">更新信息: </td>
								<td width="35%" align="left"><span wrType="readtext" name="updateBy"></span>&nbsp;&nbsp;<span wrType="readtext" name="updateDate"></span></td>
							</tr>
						</tbody>
					</table>
				</form>
			</p>
		</div>
	
		<div id="campaignCriteria" style="border: 1px solid #FBD850; background: #EEEEEE;height: 300px;">
			<p id='tab2'>
				<div id="criteriaSchemeView">
					<table width="100%" border="0" cellingspace="0">
						<tr><td align="left"><strong>已选择条件:</strong></td>
						<td valign="top"><table id="schemeView"></table></td></tr>
					</table>
				</div>
			</p>
		</div>
		<!-- 
		<p>
			<button type="button" id="onBackBtn" wrType="button" wrParam="icon:ui-icon-search;">返回</button>
		</p>
		 -->
	</div>
	<!-- 明细列表 -->
	<table id="detailList"></table>
	<div id="detailPager" style="text-align:center;"></div>

</div>

<% if(fromAp2 == false) { %>
</center>
</body>
</html>
<% } %>