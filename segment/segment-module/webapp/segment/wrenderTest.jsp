<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Widget Render Example</title>


<link rel="stylesheet" type="text/css" href="../jslib/multiselect/jquery.multiselect.css"></link>
<link rel="stylesheet" type="text/css" href="../jslib/multiselect/jquery.multiselect.filter.css"></link>
<link rel="stylesheet" type="text/css" href="../jslib/multiselect/multiselect_prettify.css"></link>
<link rel="stylesheet" type="text/css" href="../jslib/multiselect/multiselect_style.css"></link>

<jsp:include page="../header.jsp"><jsp:param name="groups" value="jquery,jquery-ui,jqgrid,validation,ztree,common"/></jsp:include>
<script type="text/javascript" src="../jslib/jquery-ui/jquery.ui.datepk.js"></script>
<script type="text/javascript" src="../jslib/jquery-ui/jquery.ui.datepicker-zh-CN.js"></script>

<script type="text/javascript" src="../jslib/jquery-ui-timepicker-addon.js"></script>
<script type="text/javascript" src="../jslib/multiselect/multiselect_prettify.js"></script>
<script type="text/javascript" src="../jslib/multiselect/jquery.multiselect.min.js"></script>
<script type="text/javascript" src="../jslib/multiselect/jquery.multiselect.filter.min.js"></script>
<script type="text/javascript" src="../jslib/multiselect/multiselect_link_ajax.js"></script>

<script type="text/javascript" src="../jslib/wrender/wrender.js"></script>
<script type="text/javascript" src="../jslib/wrender/wrender.common.js"></script>
<script type="text/javascript" src="../jslib/wrender/wrender.date.js"></script>
<script type="text/javascript" src="../jslib/wrender/wrender.datetime.js"></script>
<script type="text/javascript" src="../jslib/wrender/wrender.select.js"></script>
<script type="text/javascript" src="../jslib/wrender/wrender.composite.js"></script>
<script type="text/javascript" src="../jslib/wrender/wrender.tree.js"></script>

<script type="text/javascript" src="segment.wrender.ext.js"></script>

<script type="text/javascript">
$(document).ready(function(){

	var inputForm = $('#inputForm');
	var viewDialog = $('#viewDialog'); 
	//重置缺省值
	var initValues = {
		readtextInput: "007",
		textInput: "008",
		selectInput: "11",
		multiselectInput1: ["11","12"],
		multiselectInput2: ["1","2","3","4"],
		treeInput: ["38","39","40"],
		dateInput : "2013-05-10",
		datetimeInput : "2013-05-10 12:10:50",
		textareaInput: "hahahaha \n hahahaha \n 中文",
		compositeFilm: {
			"selTarget":true, 
			"criteria":[
			    {"inputId":"showSet","label":"放映制式","operator":"in","value":["1","2"],"valueLabel":"胶片;数字2D"}
			],
			"selections":{
				"value":["002101022012","001100012011"],
				"valueLabel":["2012喜上加喜（数字）","37"]
			}
	    },
		compositeCinema: {
			selTarget: false,  //是否选择明细目标
			criteria: [
				{inputId:"innerName", label:"影城内部名称", operator:"like", value:"北京", valueLabel:"北京"}
		    ],
		    selections: {
			    value:[],
			    valueLabel:[]
		    }
		},
		compositeConItem: {
			selTarget: false,  //是否选择明细目标
			criteria: [
				{inputId:"itemName", label:"名称", operator:"like", value:"可乐", valueLabel:"可乐"}
		    ],
		    selections: {
			    value:[],
			    valueLabel:[]
		    }
		},
		payMethodInput: "BOC"
	};
	
	
	var nullValues = {
		readtextInput: null,
		textInput: null,
		selectInput: null,
		multiselectInput1: null,
		multiselectInput2: null,
		treeInput: null,
		dateInput : null,
		datetimeInput : null,
		textareaInput: null,
		compositeCinema: null,
		compositeConItem: null,
		payMethodInput: null
	};
	
	
	$('button[name=onReset]', inputForm).click(function() {
		$('[wrType]', inputForm).wrender('setValue', initValues); //render with initialized values
	});
	
	$('button[name=onResetNull]', inputForm).click(function() {

		
		$('[wrType]', inputForm).wrender('setValue', nullValues); //render with initialized values
	});
	
	$('button[name=onSave]', inputForm).click(function() {
		var wrValue = $('[wrType]', inputForm).wrender('getValue');
		alert(JSON.stringify(wrValue));
	});
	
	$('button[name=onGetValueLabel]', inputForm).click(function() {
		var wrLabel = $('[wrType]', inputForm).wrender('getValueLabel');
		alert(JSON.stringify(wrLabel));
	});
	
	$('button[name=onReadOnly]', inputForm).click(function() {
		$('[wrType]', inputForm).wrender('toReadOnly', null, true);
	});
	
	$('button[name=onNotReadOnly]', inputForm).click(function() {
		$('[wrType]', inputForm).wrender('toReadOnly', null, false);
	});
	
	
	$('button[name=onAddChangeEvent]', inputForm).click(function() {
		alert('接下来会给每一个可输入控价加入一个 change 事件，并在火狐firebug的控制台上输出每次事件的响应内容');
		$('[wrType]', inputForm).wrender('change', function(value) {
			var elemName = $(this).attr('name');
			console.log("Value Changed1 - " + elemName + "=" + value);
		});
	});
	
	viewDialog.dialog({
		width: 500,
		buttons: [
			{id:'onCloseBtn', text:'关闭', click: function() { $(this).dialog('close'); }}
		]
	});
	
	$('tbody[name=viewBody]', viewDialog).append($('tbody[name=inputBody]', inputForm).html());
	
	$('button[name=onPopView]', inputForm).click(function() {
		viewDialog.dialog('open');
	});
	
	$('[wrType]', inputForm).wrender();
	$('[wrType]', viewDialog).wrender();
	$('[wrType]', viewDialog).wrender('toReadOnly', initValues, true);
	

	$('.contentArea')[0]['wrValue']= ["1", "2"];
	
	$('.contentArea').show();
	
});

</script>

</head>
<body>
<center>
<div class="contentArea">
   	<form id="inputForm">
   		<table width="100%">
   		<tbody name="inputBody">
			<tr>
			  <td width="110" align="right">只读显示框: </td>
			  <td><div name="readtextInput" wrType="readtext" ></div></td>
			  <td width="110" align="right">文本输入框: </td>
			  <td><input name="textInput" wrType="text" type="text"/></td>
			</tr>
			<tr>
			  <td align="right">多选类型(待过滤): </td>
			  <td>
			  	<select name="multiselectInput1" style="display:none;" wrType="multiselect" wrParam="sourceId:dimdef;typeId:179;filter:true;" multiple="multiple"></select>
			  </td>
			  <td align="right">多选类型(无过滤): </td>
			  <td>
			  	<select name="multiselectInput2" style="display:none;" wrType="multiselect" wrParam="sourceId:dimdef;typeId:1025;filter:false;" multiple="multiple"></select>
			  </td>
			</tr>
			<tr>
			  <td align="right">单选类型: </td>
			  <td>
			  	<select name="selectInput" wrType="select" wrParam="sourceId:dimdef;typeId:179">
			  		<option value="">请选择...</option>
			  	</select>
			  </td>
			  <td align="right">树控件类型: </td>
			  <td>
				  <div name="treeInput" wrType="tree" wrParam="treeId:concategory"></div>
			  </td>
			</tr>
			<tr>
			  <td align="right">日期类型: </td>
			  <td>
				  <input name="dateInput" type="text"  wrType="date"/>
			  </td>
			  <td align="right">日期时间类型: </td>
			  <td>
				<input name="datetimeInput" type="text" wrType="datetime"/>
			  </td>
			</tr>
			<tr>
			  <td align="right">复合弹出类型-电影: </td>
			  <td colspan="3">
				<div name="compositeFilm" wrType="composite" wrParam="compositeId:film;"></div>
			  </td>
			</tr>
			<tr>
			  <td align="right">复合弹出类型-影城: </td>
			  <td colspan="3">
				<div name="compositeCinema" wrType="composite" wrParam="compositeId:cinema;"></div>
			  </td>
			</tr>
			<tr>
			  <td align="right">复合弹出类型-卖品: </td>
			  <td colspan="3">
				<div name="compositeConItem" wrType="composite" wrParam="compositeId:conitem;"></div>
			  </td>
			</tr>
			<tr>
			  <td align="right">复合弹出类型-用户(最多选10个): </td>
			  <td colspan="3">
				<div name="compositeConItem" wrType="composite" wrParam="compositeId:authuser;"></div>
			  </td>
			</tr>
			<tr>
			  <td align="right">文本域: </td>
			  <td colspan="3">
				<textarea wrType="textarea" name="textareaInput" rows="3" cols="50"></textarea>
			  </td>
			</tr>
			<tr>
			  <td align="right">支付类型单选: </td>
			  <td>
			  	<select name="payMethodInput" wrType="multiselect"  wrParam="sourceId:paymethod;typeId:1">
			  		<option value="">请选择...</option>
			  	</select>
			  </td>
			  <td align="right"></td>
			  <td>
				  
			  </td>
			</tr>
		</tbody>
		<tfoot>
			<tr>
			  <td colspan="4">
				<button name="onSave" type="button">保存(getValue)</button>
				<button name="onReset" type="button">重置(setValue)</button>
				<button name="onResetNull" type="button">重置空(setValue)</button>
				<button name="onGetValueLabel" type="button">取得值标签(getValueLabel)</button>
				<button name="onReadOnly" type="button">只读</button>
				<button name="onNotReadOnly" type="button">非只读</button>
				<button name="onPopView" type="button">只读窗口</button>
				<button name="onAddChangeEvent" type="button">添加change事件</button>
			  </td>
			</tr>
		</tfoot>
   		</table>
   	</form>
   	
   	<div id="viewDialog">
   	   	<table width="100%">
   			<tbody name="viewBody"></tbody>
		</tbody>
   	</div>
</div>
</center>
</body>
</html>

	