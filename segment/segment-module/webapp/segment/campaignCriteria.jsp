<%@ page import="com.wanda.ccs.member.ap2in.*" %>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%
boolean testFlag = ("true".equals(request.getParameter("test")));//用户测试的标志
boolean fromAp2 = AuthUserHelper.isFromAp2(request);//是否为AP2发来的请求
UserProfile user = AuthUserHelper.getUser();
String id = user.getId();
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
<title>营销活动管理</title>

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

<style type="text/css">
.ui-jqgrid tr.jqgrow td {  
    height: 25px;
	text-overflow: ellipsis;
}

/*自动扩展行高 */
#criteriaSchemeConfig .jqgrow td {
  white-space: normal !important;
  height: 25px;
  /*vertical-align:text-top;*/
  padding-top:2px;
}

#criteriaSchemeView .jqgrow td {
  white-space: normal !important;
  height: 25px;
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

.ui-jqgrid tr.jqgrow td {
    height: 25px;
	text-overflow: ellipsis;
}

#seachBody td {
	height: 30px;
}

#editBody td {
	height: 34px;
}

</style>

<script type="text/javascript" src="<%=context%>/segment/criteria-scheme.js"></script>
<script type="text/javascript" src="<%=context%>/segment/segment.wrender.ext.js"></script>
<script type="text/javascript" src="<%=context%>/segment/segment.settings.campaign.js"></script>

<script type="text/javascript">
var prevOpera;//,code,status,segmentName,segmentNumber,segmentTime,create,createBy,createDate,updateBy,updateDate,name,description,cinemaRange;
var prevEndDate;

var CampaignAction = {};
// 活动增加规则
var CampaignAddRule = {};

CampaignAddRule.tsale = 0;		//会员基本信息
CampaignAddRule.conSale = 0;	//记录其他标签添加的数量

CampaignAddRule.addRuleValid = function(groupId) {
	//alert(CampaignAddRule.tsale + "=="+ CampaignAddRule.conSale);
	if(groupId == "tsale") {	// 添加票类
		if(CampaignAddRule.conSale == 0) {
			CampaignAddRule.tsale+=1;
			return true;
		}
	} else if(groupId == "conSale") {	// 添加卖品
		if(CampaignAddRule.tsale == 0) {
			CampaignAddRule.conSale+=1;
			return true;
		}
	} else {
		return true;
	}
	
	$.msgBox('warn', '', '票房交易和卖品交易不能同时存在！');
	return false;
}

CampaignAddRule.removeRuleValid = function(groupId) {
	if(groupId == "tsale") {	// 添加会员基本信息
		CampaignAddRule.tsale-=1;
	} else if(groupId == "conSale") {	// 添加其他信息
		CampaignAddRule.conSale-=1;
	}
}

CampaignAddRule.initData = function(groupId) {
	if(groupId == "tsale") {	// 添加会员基本信息
		CampaignAddRule.tsale+=1;
	} else if(groupId == "conSale") {	// 添加其他信息
		CampaignAddRule.conSale+=1;
	}
}

// 初始化编辑界面
function initEditForm() {
	var editForm = $('#editForm');
	var editDialog = $('#editDialog');
	var editBody = $('#editBody');
	var ciriteriaConfig = $('#criteriaSchemeConfig');
	
	//创建初始化积分条件列表
	schemeAction.applyScheme($.segment.settings.campaign, null);
	$("[name=endDate]", editForm).wrender('change', function(value) {
		if($('input[name=operaType]', editBody).val() == 'editEndDate') {
			if(prevEndDate != '') {
				var prev = prevEndDate.split('-');
				var curr = value.split('-');
				if(parseInt(prev[0]+""+prev[1]+""+prev[2]) > parseInt(curr[0]+""+curr[1]+""+curr[2])) {
					$('[name=endDate]', editForm).wrender('setValue', {endDate: prevEndDate});
					$.msgBox('error', '', '结束时间不能小于初始值！');
				}
			} else {
				$('[name=endDate]', editForm).wrender('setValue', {endDate: ''});
				$.msgBox('error', '', '结束初始时间不正确！');
			}
		}
	});
	/* $('[name=startDate]', editBody).datepicker({
		minDate: '',
		onSelect:function(dateText,inst){
			$('[name=endDate]', editBody).datepicker("option", "minDate", dateText);  
	    }
	});  */
	
	/* $('[name=endDate]', editBody).datetimepicker({
	    showTimePicker: false
	}); */
	
	/* $('[name=endDate]', editBody).datepicker({
		minDate: '',
		onSelect:function(dateText,inst) {
			$('[name=startDate]', editBody).datepicker("option", "maxDate", dateText);  
	    } 
	}); */
	// 渲染Jquery控件
	$('[wrType]', editForm).wrender();
	$('[wrType]', editDialog).wrender();
	$('[wrType]', ciriteriaConfig).wrender();
	// tab页
    $("#tabs").tabs();
	//$('.ui-button-text').css('padding', '0');
}

// 对话框初始化值
function initEditFormValue(type, cid) {
	var getUrl = $.wrender.context+'/segment/CampaignAction/get.do';
	
	var editForm = $('#editBody');
	var ciriteriaConfig = $('#criteriaSchemeConfig');
	
	if((prevOpera == 'view' || prevOpera == 'editEndDate') && (type != 'view')) {
		$('[wrType]:not([name=cinemaRange])', editForm).wrender('toReadOnly', null, false);
		$('#criteriaSchemeConfig').css('display', '');
		$('#criteriaSchemeView').css('display','none');
		$('[name=compositeCinema]', editForm).css('display','').next('div').html('').css('display', 'none');
		$('[name=compositeSegment]', editForm).css('display','');
		$('#onSaveBtn').css('display', '');
	}
	
	$('input[name=campaignId]', editForm).val('');
	$("#tab2_name").html('推荐响应配置');
	$("#tabs").tabs({ disabled: [] });
	
	// 编辑时回显页面信息
	if(type == 'editEndDate' && cid != '') {
		$('[wrType]:not([name=cinemaRange],[name=compositeCinema],[name=compositeSegment])', editForm).wrender('toReadOnly', null, true);
		$('[name=endDate]', editForm).wrender('toReadOnly', null, false);
		$('input[name=operaType]', editForm).val('editEndDate');
		prevEndDate = '';
		postCommit(getUrl, 'cid='+cid, function(result) {
			if(result != null) {
				prevEndDate = result.endDate;
				$('[name=compositeCinema]', editForm).css('display','none').next('div').css('display', '');
				$('[name=compositeSegment],[name=nbsp]', editForm).css('display','none');
				$('#criteriaSchemeConfig').css('display', 'none');
				$('#criteriaSchemeView').css('display','');
				
				var index = result.cinemaRange.length / 60;
				if(index < 1) {
					index = 1;
				} else if(index > 5) {
					index = 5;
				}
				$('[name=cinemaRange]', editForm).css('height',(index * 10) + 'px');
				$('[wrType]', editForm).wrender('setValue', result);
				$('[name=cinemaRange]', editForm).html(result.cinemaRange);
				var dates = result.endDate.split('-');
				//$("[name=endDate]", editForm).datepicker("option","minDate", new Date(dates[0], dates[1]-1, dates[2]));
					
				if(result.criteriaScheme == null) {
					$("#tab2_name").html('无推荐响应配置');
					$("#tabs").tabs({ disabled: [1] });
				} else {
					var schemeObj = $.parseJSON(result.criteriaScheme);
					schemeAction.applySchemeView($.segment.settings.campaign, schemeObj);
				}
			}
		});	
	}
	else if(type == 'edit' && cid != '') {
		$('input[name=operaType]', editForm).val('edit');
		
		postCommit(getUrl, 'cid='+cid, function(result) {
			if(result != null) {
				$('[wrType]:not([name=compositeCinema])', editForm).wrender('setValue', result);
				$("[name=compositeCinema]", editForm).wrender('setValue', eval('('+result.cinemaScheme+')'));
				var schemeObj = $.parseJSON(result.criteriaScheme);
				schemeAction.applyScheme($.segment.settings.campaign, schemeObj);
			}
		});
		
	} else if(type == 'create') {
		$('input[name=operaType]', editForm).val('create');
		var curr = new Date();
		var time = curr.getFullYear()+"-"+((curr.getMonth()+1)<10?"0"+(curr.getMonth()+1):(curr.getMonth()+1))+"-"+(curr.getDate()<10?"0"+curr.getDate():curr.getDate());
		
		var nullValues = {
			code: '系统自动生成',
			name: null,
			status: '草稿',
			startDate: null,
			endDate: null,
			description: null,
			segmentName: null,
			calCount: null,
			calCountTime: null,
			compositeCinema: null,
			compositeSegment: null,
			create: '<%= id %>',
			createBy: '<%= id %>',
			createDate: time+" "+curr.toLocaleTimeString(),
			updateBy: '',
			updateDate: ''
		};
		
		$('[wrType]', editForm).wrender('setValue', nullValues);
		$("[name=startDate]", editForm).datepicker("option","maxDate", '');
		$("[name=endDate]", editForm).datepicker("option","minDate", '');
		//创建初始化积分条件列表
		schemeAction.applyScheme($.segment.settings.campaign, null);
	} else if(type == 'view' && cid != '') {
		$('#onSaveBtn').css('display', 'none');
		$('[wrType]:not([name=cinemaRange],[name=compositeCinema],[name=compositeSegment])', editForm).wrender('toReadOnly', null, true);
		postCommit(getUrl, 'cid='+cid, function(result) {
			if(result != null) {
				$('[name=compositeCinema]', editForm).css('display','none').next('div').css('display', '');
				$('[name=compositeSegment],[name=nbsp]', editForm).css('display','none');
				$('#criteriaSchemeConfig').css('display', 'none');
				$('#criteriaSchemeView').css('display','');
				
				//result.cinemaRange = result.cinemaRange.replace('<span style="font-weight:bold;">影城名称: </span>','');
				var index = result.cinemaRange.length / 60;
				if(index < 1) {
					index = 1;
				} else if(index > 5) {
					index = 5;
				}
				$('[name=cinemaRange]', editForm).css('height',(index * 10) + 'px');
				$('[wrType]', editForm).wrender('setValue', result);
				$('[name=cinemaRange]', editForm).html(result.cinemaRange);
				
				if(result.criteriaScheme == null) {
					$("#tab2_name").html('无推荐响应配置');
					$("#tabs").tabs({ disabled: [1] });
				} else {
					var schemeObj = $.parseJSON(result.criteriaScheme);
					schemeAction.applySchemeView($.segment.settings.campaign, schemeObj);
				}
			}
		});
	}
	
	prevOpera = type;
}

// 初始化编辑界面事件
function initEditEvent() {
	var getSelectedSegment = $.wrender.context+'/segment/SegmentAction/getSelectedSegments.do';
	var saveUrl = $.wrender.context+'/segment/CampaignAction/save.do';
	var updateTimeUrl = $.wrender.context+'/segment/CampaignAction/updateTime.do';
		
	var editForm = $('#editForm');
	var editBody = $('#editBody');
	var ciriteriaConfig = $('#criteriaSchemeConfig');
	var tabs = $("#tabs");
	var editDialog = $('#editDialog');
	
	var validator = editForm.validate({
		rules: {
			"name":{required:true, maxlength:20},
			"startDate":{required:true},
			"endDate":{required:true}
		}
	});
	
	$('[name=compositeSegment]', editBody).wrender('change', function(value) {
		if(value.selections.value) {
			postCommit(getSelectedSegment, 'segmentIds=' + value.selections.value, function(result) {
				if(result && result.length == 1) {
					if(result[0].calCount > 0) {
						if(result[0].controlCount >= 0) {
							$('[name=segmentId]',editBody).val(result[0].subSegmentId);
							$('[name=segmentName]',editBody).html(result[0].name).css('padding-right', '5px');
							$('[name=calCount]',editBody).wrender('setValue',{calCount: result[0].calCount});
							$('[name=calCountTime]',editBody).wrender('setValue',{calCountTime: result[0].calCountTime});
						} else {
							$.msgBox('error', '', '指定客群对比组数量有误！');
						}
					} else if(result[0].calCount == -1) {
						$.msgBox('error', '', '指定客群计算数量有误！');
					} else {
						$.msgBox('error', '', '指定客群计算数量不能为零！');
					}
				}
			});
		}
	});
	
	CampaignAction.doSave = function() {
		var operaType = $('input[name=operaType]', editBody).val();
		if(operaType == 'edit' || operaType == 'create') { //更新操作
			// 校验必填项
			if($('input[name=name]', editBody).val() == '' || $('[name=startDate]', editBody).val() == '' || $('[name=endDate]', editBody).val() == '') {
				tabs.tabs('option', 'selected', 0);
				if(validator.form() == false) {
			    	validator.focusInvalid();
			    }
				return false;
			}
			if($('input[name=name]', editBody).val().length > 18) {
				$.msgBox('error', '', '活动名称不能超过18个字符！');
				return false;
			}
			if($('textarea[name=description]', editBody).val().length > 100) {
				$.msgBox('error', '', '描述信息长度不能超过100个字符！');
				return false;
			}
			
			var startDate = $('[name=startDate]', editBody).val();
			var startDates = startDate.split('-');
			var endDate = $('[name=endDate]', editBody).val();
			var endDates = endDate.split('-');
			if(parseInt(startDates[0]+""+startDates[1]+""+startDates[2]) > parseInt(endDates[0]+""+endDates[1]+""+endDates[2])) {
				$('[name=startDate]', editBody).val(endDate);
				$('[name=endDate]', editBody).val(startDate);
				
				var temp = startDates;
				startDates = endDates;
				endDates = temp;
			}
			
			/* var curr = new Date();
			var currTime = curr.getFullYear()+""+((curr.getMonth()+1)<10?"0"+(curr.getMonth()+1):curr.getMonth()+1)+""+(curr.getDate()<10?"0"+curr.getDate():curr.getDate());
			if(parseInt(currTime) - parseInt(startDates[0]+""+startDates[1]+""+startDates[2]) > 0) {
				//$('[name=startDate]', editBody).val('');
				//$('[name=startDate]', editBody).select();
				tabs.tabs('option', 'selected', 0);
				$.msgBox('error', '', '开始时间不能小于当前时间！');
				return false;
			} */
			
			var cinemaRange = $('span[name=valueLabel]').html();
			if(cinemaRange == '') {
				tabs.tabs('option', 'selected', 0);
				$.msgBox('error', '', '请选择影城范围！');
				return false;
			}
			
			// 判断客群
			var segmentId = $("input[name=segmentId]", editBody).val();
			if(segmentId == '') {
				tabs.tabs('option', 'selected', 0);
				$.msgBox('error', '', '请选择统计客群！');
				return false;
			}
			
			//获取表单和推荐响应规则
			var voData =$('[wrType]:not([wrType=readtext],[wrType=button],[wrType=composite])', editBody).wrender('getValueData');
			var criteriaScheme = schemeAction.getSchemeData();
			if(criteriaScheme == encodeURI('[]')) {
				$.msgBox('confirm', '', '尚未设置推荐响应规则，<font color="red">如果不设置将只按照客群中的会员进行统计</font>，是否继续？',  function(result) { 
					if(!result) {
						tabs.tabs('option', 'selected', 1);
						return false;
					} else {
						submit(voData, criteriaScheme);
					}
				});
			} else {
				submit(voData, criteriaScheme);
			}
		} else if(operaType == 'editEndDate') { // 更新结束时间
			$.msgBox('confirm', '', '是否要修改营销活动结束时间？',  function(result) { 
				if(result) {
					var voData = $('[name=endDate],[name=campaignId]', editBody).wrender('getValueData');
					var postData = 'json=' + voData;
					postCommit(updateTimeUrl, postData, function(result) {
						if(result) {
							if(result.level == 'ERROR') {
								$.msgBox('error', '', result.message);
							} else {
								editDialog.dialog("close");
								CampaignAction.reloadSearch();
							}
						}
					});
				}
			});
		}
	}
	
	$('button[name=onCancelBtn]').click(function() {
		editDialog.dialog("close");
	});
	
	function submit(voData, criteriaScheme) {
		var cinemaScheme = JSON.stringify($('[name=compositeCinema]', editBody).wrender('getValue'));
		var cinemaRange = $('span[name=valueLabel]', $('[name=compositeCinema]', editBody)).html().toLocaleLowerCase();
		if(cinemaRange.indexOf('条件') > 0) {
			cinemaRange = cinemaRange.substring(cinemaRange.indexOf('</span>')+('</span>'.length));
		}
		if(cinemaRange.indexOf('已选') > 0) {
			cinemaRange = ('<span style="font-weight:bold;">影城名称: </span>' + cinemaRange.substring(cinemaRange.indexOf('</span>')+('</span>'.length)));
		}
		if(cinemaRange.indexOf('影城名称') < 0 && cinemaRange.indexOf('</span>') > -1) {
			while(cinemaRange.indexOf('</span>') > -1) {
				cinemaRange = cinemaRange.replace('</span>',':$');
			}
			while(cinemaRange.indexOf('$') > -1) {
				cinemaRange = cinemaRange.replace('$',' </span>');
			}
		}
		
		var postData = ('json=' + voData + (criteriaScheme!=''?'&criteriaScheme=' + criteriaScheme:'') + (cinemaScheme!=''?"&cinemaScheme="+cinemaScheme:'')+(cinemaRange!=''?"&cinemaRange="+cinemaRange:''));
		//alert(postData);
		postCommit(saveUrl, postData, function(result) {
			if(result) {
				if(result.level == 'ERROR') {
					$.msgBox('error', '', result.message);
				} else {
					editDialog.dialog("close");
					CampaignAction.reloadSearch();
				}
			}
		});
	}
	
}

function showDialog(editDialog, operaType, type, cid) {
	$("#tabs").tabs('option', 'selected', 0);
	$('.ui-tabs-nav').removeClass('ui-widget-header').css({'padding':'0','background':'#EEEEEE'});
	$('.ui-dialog').css('background','#EEEEEE');
	//$('.ui-tabs-nav')
	//$("#tabs").css('border','0px');
	
	if(type == 'create') {
		initEditFormValue(type, '');
	} else if(type == 'edit') {
		initEditFormValue(type, cid);
	} else if(type == 'view') {
		initEditFormValue(type, cid);
	} else if(type == 'editEndDate') {
		initEditFormValue(type, cid);
	}
	$(editDialog).dialog(operaType);
}

$(function(){
	
	var queryUrl = $.wrender.context+'/segment/CampaignAction/query.do';
	var deleteUrl = $.wrender.context+'/segment/CampaignAction/delete.do';
	var statusUrl = $.wrender.context+'/segment/CampaignAction/status.do';
	
	initEditForm();
	initEditEvent();
	
	// 初始化
	var contentArea = $('#contentArea');
	
	var editDialog = $('#editDialog');
	editDialog.dialog({
		 autoOpen: false,
		 height: 560,
		 width: 1000,
		 modal: true,
		 buttons: [
				{id:'onSaveBtn', text:'保存', click: CampaignAction.doSave },
				{id:'onCloseBtn', text:'关闭', click: function() { $(this).dialog('close'); }}
			]
	});
	
	var searchForm = $('#searchForm');
	var resultList = $('#resultList');
	
  	//渲染查询框
	$('[wrType]', searchForm).wrender();
	$('#onNewBtn').wrender();
		
	var girdColumns = [
				{name:'CAMPAIGN_ID', label:'ID', hidden:true},
				{name:'EDITABLE', label:'EDITABLE', hidden:true},
				{name:'STATUS', label:'STATUS', hidden:true},
				{name:'CODE', label:'编码', width:50, sortable:true},
				{name:'NAME', label:'名称', width:50, sortable:true},
				{name:'START_DATE', label:'开始时间', width:50, sortable:false},
				{name:'END_DATE', label:'结束时间', width:50, sortable:false},
				{name:'STATUS_INFO', label:'状态', width:30, sortable:false},
				{name:'CREATE_BY', label:'创建人', width:50, sortable:false},
				{name:'CREATE_DATE', label:'创建时间', width:60, sortable:false},
				{name:'CINEMA_RANGE', label:'活动范围', width:140, sortable:false},
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
			var cellHtml = '<button name="onViewBtn" key=' + rowdata['CAMPAIGN_ID'] + ' type="button" wrType="button" wrParam="icon:ui-icon-zoomin;text:false" style="height:22px;" title="查看"/>&nbsp;';
			if(rowdata['EDITABLE']) {
				if(rowdata['STATUS'] == 10) { //草稿状态
					cellHtml += '<button name="onCommitBtn" key=' + rowdata['CAMPAIGN_ID'] + ' startDate='+rowdata['START_DATE']+' type="button" wrType="button" wrParam="icon:ui-icon-locked;text:false" style="height:22px;" title="生效"/>&nbsp;';
					cellHtml += '<button name="onModifyBtn" key=' + rowdata['CAMPAIGN_ID'] + ' type="button" wrType="button" wrParam="icon:ui-icon-pencil;text:false" style="height:22px;" title="修改"/>&nbsp;';
				}
				if(rowdata['STATUS'] == 20) { //提交状态
					cellHtml += '<button name="onCancelBtn" key=' + rowdata['CAMPAIGN_ID'] + ' type="button" wrType="button" wrParam="icon:ui-icon-unlocked;text:false" style="height:22px;" title="撤销"/>&nbsp;';
				}
				if(rowdata['STATUS'] == 10 || rowdata['STATUS'] == 20) {
					cellHtml +=	'<button name="onDeleteBtn" key=' + rowdata['CAMPAIGN_ID'] + ' type="button" wrType="button" wrParam="icon:ui-icon-trash;text:false" style="height:22px;" title="删除"/>';
				}
				if(rowdata['STATUS'] == 30) {
					cellHtml += '<button name="onModifyDateBtn" key=' + rowdata['CAMPAIGN_ID'] + ' type="button" wrType="button" wrParam="icon:ui-icon-pencil;text:false" style="height:22px;" title="修改"/>&nbsp;';
				}
			}
			$(this).setCell(rowid, 'ACTIONS', cellHtml);
			$('button[wrType=button]', this).wrender();
		}/* ,
		loadComplete: function() {
	        var ids = resultList.getDataIDs();
	        for (var i = 0,len=ids.length; i < len; i++) {
	        	//resultList.setRowData(ids[i], false, {'height': '50px'} );
	        	//resultList.setCell(ids[i], 'CINEMA_RANGE', resultList.getCell(ids[i], 'CINEMA_RANGE'), {'text-overflow': 'ellipsis'});
	        }
	    } */
	});
	
	$('#onNewBtn').click(function() {
		CampaignAddRule.tsale = 0;
		CampaignAddRule.conSale = 0;
		
		showDialog(editDialog, 'open', 'create', '');
	});
	
	// ================= 注册事件 ===================
	$('#onSearchBtn', searchForm).click(function() {
		reloadSearch();
	});
	
	var nullValue = {
		code: null,
		name: null,
		startDateFrom: null,
		startDateTo: null,
		endDateFrom: null,
		endDateTo: null,
		status: ''
	};
	
	$('#onResetBtn', searchForm).click(function() {
		$('[wrType]', searchForm).wrender('setValue', nullValue);
	});
	
	//修改
	$(resultList).on('click', 'button[name=onModifyBtn]', function (){
		showDialog(editDialog, 'open', 'edit', $(this).attr('key'));
	});
	
	// 修改结束时间
	$(resultList).on('click', 'button[name=onModifyDateBtn]', function () {
		showDialog(editDialog, 'open', 'editEndDate', $(this).attr('key'));
	});
		
	//显示
	$(resultList).on('click', 'button[name=onViewBtn]', function () {
		showDialog(editDialog, 'open', 'view', $(this).attr('key'))
	});
	
	//撤销
	$(resultList).on('click', 'button[name=onCancelBtn]', function () {
		var id = $(this).attr('key');
		$.msgBox('confirm', '', '是否要撤销选中的营销活动？',  function(result) { 
			if(result) {
				postCommit(statusUrl, 'cid='+id+'&status=10', function(result) {
					if(result.level == 'ERROR') {
						$.msgBox('error', '', result.message);
					} else {
						$.msgBox('info', '', result.message);
						reloadSearch();
					}
				});
			}
		});
	});
	
	//提交
	$(resultList).on('click', 'button[name=onCommitBtn]', function () {
		/* var startDate = $(this).attr('startDate').split('-');
		var curr = new Date();
		if(parseInt(curr.getFullYear()+""+((curr.getMonth()+1)<10?"0"+(curr.getMonth()+1):(curr.getMonth()+1))+""+curr.getDay()) > parseInt(startDate[0]+""+startDate[1]+""+startDate[2])) {
			$.msgBox('error', '', '提交活动失败，开始时间不能小于当前时间！');
			return false;
		} */
		
		postCommit(statusUrl, 'cid='+$(this).attr('key')+'&status=20', function(result) {
			if(result.level == 'ERROR') {
				$.msgBox('error', '', result.message);
			} else {
				$.msgBox('info', '', result.message);
				reloadSearch();
			}
		});
	});

	//删除
	$(resultList).on('click', 'button[name=onDeleteBtn]', function (){
		var id = $(this).attr('key');
		$.msgBox('confirm', '', '是否要删除选中的营销活动？',  function(result) { 
			if(result) {
				postCommit(deleteUrl, 'cid='+id, function(result) {
					if(result.level == 'ERROR') {
						$.msgBox('error', '', result.message);
					} else {
						$.msgBox('info', '', result.message);
						reloadSearch();
					}
				});
			}
		});
	});
	
	//加载列表
	function reloadSearch() {
		var queryData =JSON.stringify( $('input[wrType],select[wrType]', searchForm).wrender('getValue'));
		resultList.setGridParam({datatype:'json', postData:{'queryData' :queryData}, page:1}).trigger("reloadGrid");
		
		return false;
	}
	
	CampaignAction.reloadSearch = function() {
		reloadSearch();
	};
	
	jqgridAutoResize(contentArea, resultList);//跟随窗口大小自动调整列表宽度
	
	reloadSearch();
	
	contentArea.show();
});

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
	$(name).html(value);
	//document.getElementsByName('code').innerHTML= 'zzzzzzzzzzzz';
}
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
				<tr>
					<td width="15%" align="right">编码: </td>
					<td width="35%" align="left"><input type="text" wrType="text" name="code"/></td>
					<td width="15%" align="right">活动名称: </td>
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
					  	 <select name="status" wrType="select" style="width: 155px;">
							<option value="">-- 请选择 --</option>
							<option value="10">草稿</option>
							<option value="20">生效</option>
							<option value="30">执行</option>
							<option value="40">结束</option>
						</select>
					</td>
					<td width="15%" align="right"></td>
					<td width="35%" align="left"></td>
				</tr>
			</tbody>
			<tfoot>
				<tr><td colspan="4" align="center">
					<button type="button" id="onSearchBtn" wrType="button" wrParam="icon:ui-icon-search;">查询</button>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<button type="button" id="onResetBtn" wrType="button" wrParam="icon:ui-icon-search;">重置</button>
				</td></tr>
			</tfoot>
		</table>
	</form>
	<br/>
	
	<div style="text-align:left;margin-bottom: 4px;">
		<button id="onNewBtn" type="button" wrType="button" wrParam="icon:ui-icon-plusthick;">添加活动</button>
	</div>
	<table id="resultList"></table>
	<div id="resultPager" style="text-align:center;"></div>
</div>

<div id="editDialog" title="活动编辑" style="display: none;">
	<div id="tabs" style="border: 0px;background-color: #EEEEEE;padding: 0;">
	  <ul>
	    <li><a id="tab1_name" href="#campaignBaseInfo">活动基本信息</a></li>
	    <li><a id="tab2_name" href="#campaignCriteria">推荐响应规则配置</a></li>
	  </ul>
	  
	  <div id="campaignBaseInfo" style="border: 1px solid #FBD850; background: #EEEEEE;">
	    <p id='tab1'>
	    	<form id="editForm">
				<table width="100%" align="center">
					<tbody id="editBody">
					<input type="hidden" name="operaType"/>
					<input type="hidden" wrType="text" name="campaignId"/>
						<tr>
							<td width="10%" align="right">编码: </td>
							<td width="40%" align="left"><div wrType="readtext" name="code"></div></td>
							<td width="10%" align="right">名称: </td>
							<td width="40%" align="left"><input type="text" wrType="text" name="name"/></td>
						</tr>
						<tr>
							<td width="15%" align="right">开始时间: </td>
							<td width="35%" align="left">
								<input type="text" readonly="readonly" wrType="date" name="startDate"/>
							</td>
							<td width="15%" align="right">结束时间: </td>
							<td width="35%" align="left">
								<input type="text" readonly="readonly" wrType="date" name="endDate" id="endDate"/>
							</td>
						</tr>
						<tr>
							<td width="15%" align="right">影城范围: </td>
							<td width="35%" colspan="3" align="left">
								<div name="compositeCinema" wrType="composite" wrParam="compositeId:cinema;"></div><div name="cinemaRange" wrType="readtext" style="display: none; overflow-y: auto; height: 14px;width: 700px;"></div>
							</td>
						</tr>
						<tr>
							<td width="15%" align="right">目标客群: </td>
							<td width="35%" align="left">
								<input type="hidden" wrType="text" name="segmentId"/>
								<span wrType="readtext" name="segmentName" style="float: left;padding-top: 5px;"></span>
								<div name="compositeSegment" wrType="composite" wrParam="compositeId:segmentCampaign;"></div>
							</td>
							<td width="15%" align="right">客群总数: </td>
							<td width="35%" align="left"><span wrType="readtext" name="calCount"></span>&nbsp;&nbsp;&nbsp;计算于:<span wrType="readtext" name="calCountTime"></span></td>
						</tr>
						<tr>
							<td width="15%" align="right">状态: </td>
							<td width="35%" align="left"><div name="status" wrType="readtext"></div></td>
							<td width="15%" align="right"></td>
							<td width="35%" align="left"></td>
						</tr>
						<tr>
							<td width="15%" align="right">描述: </td>
							<td width="35%" colspan="3" align="left">
								<textarea wrType="textarea" name="description" rows="3" cols="100"></textarea>
							</td>
						</tr>
						<tr>
							<td width="15%" align="right">创建方: </td>
							<td width="35%" align="left"><div name="createBy" wrType="readtext"></div></td>
							<td width="15%" align="right"></td>
							<td width="35%" align="left"></td>
						</tr>
						<tr>
							<td width="15%" align="right">创建信息: </td>
							<td width="35%" align="left"><span name="createBy" wrType="readtext"></span>&nbsp;&nbsp;<span name="createDate" wrType="readtext"></span></td>
							<td width="15%" align="right">更新信息: </td>
							<td width="35%" align="left"><span name="updateBy" wrType="readtext"></span>&nbsp;&nbsp;<span name="updateDate" wrType="readtext"></span></td>
						</tr>
					</tbody>
					<!-- 
					<tfoot>
						<tr>
							<td colspan="4" align="center">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="4" align="center">
								<button type="button" name="onSaveBtn" wrType="button" wrParam="icon:ui-icon-save;">保存</button>
								<span name='nbsp'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
								<button type="button" name="onCancelBtn" wrType="button" wrParam="icon:ui-icon-cancel;">关闭</button>
							</td>
						</tr>
					</tfoot>
					 -->
				</table>
			</form>
	    </p>
	  </div>
	  
	  <div id="campaignCriteria" style="border: 1px solid #FBD850; background: #EEEEEE;">
	    <p id='tab2'>
			<div id="criteriaSchemeConfig" name="criteriaSchemeConfig">
				<table width="100%" border="0" cellingspace="0">
					<tr>
						<td style="width: 1%"><strong>可选条件：</strong></td>
						<td style="width: 1%"></td>
						<td><strong>已选择条件:</strong></td>
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
			
			<div id="criteriaSchemeView" name="criteriaSchemeView" style="display: none;">
				<table width="100%" border="0" cellingspace="0">
					<tr><td align="left"><strong>已选择条件:</strong></td>
					<td valign="top"><table id="schemeView"></table></td></tr>
				</table>
			</div>
	    </p>
	  </div>
	</div>
</div>

<% if(fromAp2 == false) { %>
</center>
</body>
</html>
<% } %>