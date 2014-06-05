
var schemeAction = { };
// 自动添加筛选条件所需变量
var autoAddScheme ={ };

$(function(){
	
	var _t = {
		init:  function(setting, scheme, change) {
			//Create runtime scheme object; It will binding one setting object and values data.
			var rt = {
				setting : setting,
				cache : _t.initCache(setting),
				scheme : scheme || setting.defaultScheme,
				change: change
			};
			
			return rt;
		},
		initCache: function(setting) {
			//create cache. Below is the cache object instance: 
			//{"member":{
			//	"groups": {
			//		"base":{"id":"base","label":"会员基本属性","desc":"...."},
			//		"sale":{"id":"sale","label":"会员交易","desc":"....."}
			//	},
			//	"inputs":{
			//		"noDisturb":{"id":"noDisturb", groupLabel:"会员基本属性", "label":"是否希望被联络","desc":"....","type":"DIM_SEL","allowedOperators":"eq","repeatable":false,"groupId":"base"},
			//		"manageCinema":{"id":"manageCinema", groupLabel:"会员基本属性", "label":"管理影城","desc":"....","type":"COMPOSITE","compositeId":"cinema","allowedOperators":"in","repeatable":false,"groupId":"base"}
			//	}
			//}
			var cache = {groups: {}, inputs: {}};
			for(index in setting.inputsGroups) {
				//clone the inputsGroup object, 
				var group = $.extend({}, setting.inputsGroups[index]);
				
				//copy the input objects and add it into cache
				var inputs = group.inputs;
				for(iIndex in inputs) {
					var input = inputs[iIndex];
					input['groupId'] = group.id; //add the groupId property in each input
					input['groupLabel'] = group.label; //add the groupLabel property in each input
					cache.inputs[input.id] = input;
				}
				
				delete group.inputs;
				cache.groups[group.id] = group;
			}
			return cache;
		},
		addCriterion: function(grid, inputId, criterion, change) {
			var i = rtScheme.cache.inputs[inputId];
			if(i == null) {
				//效验是否input配置还存在，有可能配置变更导致其缺失
				alert("Failed to found the input definition which inputId=" + inputId + " in scheme definition!");
				return;
			}
			
			var g = rtScheme.cache.groups[i.groupId];
			var rowId = grid.attr('id') + 'row' + gridRowIndex;
			
			var addedInputIds = grid.getCol('inputId');
			
			if(i.repeatable == false && $.inArray(i.id, addedInputIds) >= 0) {
				$.msgBox('warn', '', '该条件不能被再次添加，其只能被添加一次！');
				return false;
			}
			
			var ops = i.allowedOperators.split(',');
			
			//取得表达式，如果没有给出值，则把一个表达式作为当前表达式
			var op = (criterion != null && criterion.operator != undefined) ? criterion.operator : ops[0].replace(/(^\s*)|(\s*$)/g,''); 
			//取得录入框的值
			var value = (criterion != null && criterion.value != undefined) ? criterion.value : i.defaultValue;
			
			var rowData = {
				groupId: i.groupId,
				inputId: i.id,
				groupLabel: g.label,
				inputLabel: i.label,
				operator: $.wrender.getOperatorHtml('operatorWidget', ops),
				inputValue: ($.inArray('between', ops) >= 0) ? $.wrender.getHtml('inputWidget', i.type, i.param) + '<span name="inputWidgetAltSpace" style="display:none;"> ~  ' + $.wrender.getHtml('inputWidgetAlt', i.type, i.param) + '</span>' 
								: $.wrender.getHtml('inputWidget', i.type, i.param),
				action: (i.required == true) ? '' : '<button name="delete" type="button" value="' + rowId + '"  class="wr_button" style="width:30px;height:24px;"> - </button>'
			};
			
			grid.addRowData(rowId, rowData, 'first');
			
			_t.renderCriterion(rowId, op, value, change);
			
			gridRowIndex ++;
		},
		renderCriterion: function(rowId, op, value, change) {
			var row = $('#' + rowId);
			$('button.wr_button', row).button();//Render the jquery button style
			var input1 = row.find('[name=inputWidget]').wrender();
			var input2 = row.find('[name=inputWidgetAlt]').wrender();
			
			
			if(op == 'between') {
				if(value != null) {
					if(!(value instanceof Array) || value.length != 2) alert('Error value format! rowId=' + rowId);
					input1.wrender('setValue', {'inputWidget' : value[0]});
					input2.wrender('setValue', {'inputWidgetAlt' : value[1]});
				}
				
				row.find('span[name=inputWidgetAltSpace]').show();
			}
			else {
				if(value != null) {
					input1.wrender('setValue', {'inputWidget' : value});
				}
				
				row.find('span[name=inputWidgetAltSpace]').hide();
			}
			
			var opw = row.find('select[name=operatorWidget]'); //get operation widget 
			opw.wrender('setValue', {'operatorWidget': op})
				.data('previousValue', op); //保存表达式当前值，绑在表达式下拉控件上
			
			//加载change事件函数
			if(change != null && (typeof change == 'function')) {
				input1.wrender('change', change);
				input2.wrender('change', change);
				opw.wrender('change', change);
			}
			
		},
		createGrid: function(tableId, readOnly) {
			var grid = $("#" + tableId);
			grid.jqGrid({
				datatype: 'local',
				width: 700,
				height: 335,
				rowNum: 200,
				viewrecords: true,
				scroll: false,
				sortname: 'groupLabel',
				sortorder: 'desc',
				colModel: [ 
		        	{name:'groupId', hidden: true}, 
		    	    {name:'inputId', hidden: true}, 
		    	    {name:'groupLabel', label:'组别', width:80, sortable:true}, 
		    	    {name:'inputLabel', label:'条件名', width:120, sortable:true}, 
		    	    {name:'operator', label:'表达式', width:90, sortable:false}, 
		    	    {name:'inputValue', label:'取值', width:374, sortable:false}, 
		    	    {name:'action',label:'操作', width:36, sortable:false, hidden: readOnly}
				],
				onSortCol: function(index, iCol, sortorder) {
					//排序后列中的控件将被重写，需要在排序前保存当前各控件的值
					//并在排序后重新加载控件并赋值。
					if($(this).data('curValueCache') == null) {
						valueCache = {};
						var rowIds = $(this).getDataIDs();

						for(var i=0; i<rowIds.length ; i++) {
							var rowId = rowIds[i];
							var row = $('#' + rowId);
							var op = row.find('select[name=operatorWidget]').val();
							var value;
							if(op == 'between') {
								var value1 = row.find('[name=inputWidget]').wrender('getValue');
								var value2 = row.find('[name=inputWidgetAlt]').wrender('getValue');
								value = [value1['inputWidget'], value2['inputWidgetAlt']];
							}
							else {
								value = row.find('[name=inputWidget]').wrender('getValue')['inputWidget'];
							}
							
							valueCache[rowId] = {'op': op, 'value': value};
						};	
						$(this).data('curValueCache', valueCache);
					}

				},
				loadComplete: function() {
					var valueCache = $(this).data('curValueCache');
					if(valueCache != null) {
						for(var rowId in valueCache) {
							var o = valueCache[rowId];
							_t.renderCriterion(rowId, o.op, o.value, rtScheme.change);
						}
						
						if(readOnly == true) {
							$('[wrType]', this).wrender('toReadOnly', null, true);
						}

						$(this).removeData('curValueCache');
					}
				}
			});
			
			return grid;
		},
		
		/**
		 * convert string value into designated type.
		 * @param sValue  source string value
		 * @param sDataType the return value type
		 * @return converted value
		 */
		convert: function(sValue,sDataType)
		{
		   switch(sDataType)
		   {
		    case "int":
		     if (isNaN(parseInt(sValue)))  return parseInt('0');
		     else   return parseInt(sValue);
		     break;
		    case "float":
		     if (isNaN(parseFloat(sValue.split(",").join("")))) return parseFloat('0');
		     else  return parseFloat(sValue.split(",").join(""));
		     break;
		    case "date":
		     return new Date(Date.parse(sValue));
		     break;
		    default:
		     return sValue.toString();
		   }
		},
		/**
		 * Get the value empty level
		 * @param val
		 * @return a integer of empty level.  0:fully empty, 1:partly empty, 2:none empty
		 */
		getEmptyLevel: function(val) {
			var level = 2;
			if(val == undefined || val == null) {
				level = 0;
			}
			else if(val instanceof Array) {
				if(val.length == 0) {
					level = 0;
				}
				else {
					var hasNoneEmpty = false;
					var hasEmpty = false;
					for(var validx=0 ; validx < val.length ; validx ++) {
						var elemLevel = _t.getEmptyLevel(val[validx]);
						if(hasNoneEmpty == false && (elemLevel == 2 || elemLevel == 1)) {
							hasNoneEmpty = true;
						}
						else if(hasEmpty == false && (elemLevel == 0 || elemLevel == 1)) {
							hasEmpty = true;
						}
					}
					
					if(hasEmpty == true && hasNoneEmpty == true) {
						level = 1; //partly empty
					}
					else if(hasEmpty == true && hasNoneEmpty == false) {
						level = 0; //fully empty
					}
				}
			}
			else if(val instanceof Object) {
				if(val.selTarget == undefined) {
					level = 0;
					alert('Not support the object value type other than "composite"');
				}
				else if(val.selTarget == true) {
					var len = val.selections.value.length; 
					if(len <= 0) {
						level = 0;
					}
					else {
						level = _t.getEmptyLevel(val.selections.value);
					}
				}
				else if(val.selTarget == false) {
					if(val.criteria.length <= 0) {
						level = 0;
					}
				}
			}
			else {
				if(typeof(val) == 'string') {
					if(val.length <= 0) { 
						level = 0;
					}
				}
				else {
					level = 0;
					alert('Not support the single value type other than "string"! But current type is "' + typeof(val) + '"');
				}
			}
			return level;
		},
		validateSingleValue: function(value, validate, schemeRow) {
			var trimedValue = $.trim(value);
			switch(validate.type) {
			case 'date':
				if(/^\d{4}-\d{1,2}-\d{1,2}$/.test(trimedValue) == false) {
					_t.validateMsg(schemeRow, '日期格式错误');
					return false;
				}
				break;
			case 'datetime':
				if(/^\d{4}-\d{1,2}-\d{1,2} ([0-1][0-9]|[2][0-3])(:)([0-5][0-9])(:)([0-5][0-9])$/.test(trimedValue) == false) {
					_t.validateMsg(schemeRow, '日期时间格式错误');
					return false;
				}
				break;
			case 'number':
				if(/^[0-9]\d*$/.test(trimedValue) == false) {
					_t.validateMsg(schemeRow, '正整数');
					return false;
				}
				break;
			case 'integer':
				if(/^[1-9]\d*$/.test(trimedValue) == false) {
					_t.validateMsg(schemeRow, '大于0的整数');
					return false;
				}
				break;
			default:
				alert("The validate type=" + validate.type + " not supported! Please check the input definiton inputId=" + schemeRow.input.id)
				return false;
			}
			return true;
		},
		validateRow: function(schemeRow) {
			//  {
			//		'label':inputLabel, 
		    //		'operator':operator, 
		    //		'value':value, 
		    //		'rowId': rowId, 
		    //		'input': {
			//			id : "birthday",
			//          groupId: "base", 
			//			label : "出生日期",
			//			desc : "出生日期",
			//			type : "date",
			//			allowedOperators : "between,eq,lt,gt,le,ge",
			//			repeatable : false,
			//			validate : {type:'date'}
			//		}
			//	}
			var val = schemeRow.value;
			var validate = schemeRow.input.validate;
			var emptyLevel = _t.getEmptyLevel(val);
			if(emptyLevel == 0){
				_t.validateMsg(schemeRow, '不能为空');
				return false;
			}
			else if(emptyLevel == 1){
				_t.validateMsg(schemeRow, '未填写完整');
				return false;
			}
			
			//进行类型验证
			if(validate != undefined) {
				if(val instanceof Array) {
					for(var vaidx=0 ; vaidx < val.length ; vaidx ++) {
						//验证数组中的每一个值
						var passedVali = _t.validateSingleValue(val[vaidx], validate, schemeRow);
						if(passedVali == false) {
							return false;
						}
					}
				}
				else if(val instanceof Object) {
					alert("Object value do not support the validate defintioin!");
				}
				else {
					return _t.validateSingleValue(val, validate, schemeRow);
				}
			}
			
			//验证区间操作符时， 值的有效性
			if(schemeRow.operator == 'between') {
				//验证格式
				if(val instanceof Array && val.length == 2 && typeof(val[0]) == 'string' &&  typeof(val[1]) == 'string') {
					if(validate != undefined) {
						switch(validate.type) {
						case 'date':
						case 'datetime':
							if(val[0] > val[1]) {
								_t.validateMsg(schemeRow, '前值必须小于等于后值');
								return false;
							}
							break;
						case 'number':
						case 'integer':
							var numberVal1 = _t.convert(val[0], 'float');
							var numberVal2 = _t.convert(val[1], 'float');
							if(numberVal1> numberVal2) {
								_t.validateMsg(schemeRow, '前值必须小于等于后值');
								return false;
							}
							break;
						default:
							alert('The validate type=' + validate.type + ' not supported! Please check the input definiton inputId=' + schemeRow.input.id);
							return false;
						}
					}
					else {
						alert('The between operator missing the validate.type option, Plean check the input definiton inputId=' + schemeRow.input.id);
						return false;
					}
				}
				else {
					alert('Error between value type. The allowed type should be : string array with length 2!');
					return false;
				}
			}

			return true;
		},
		validateMsg: function(schemeRow, msg) {
			$.msgBox('error', '条件验证失败', '条件“' + schemeRow.input.groupLabel + ' > ' + schemeRow.input.label + '”的取值:' + msg + '，请修正后再进行保存!');
		}
	};
	

	var rtScheme;  //当前配置的scheme对象，用于暂存当前scheme配置和变量。
	var gridRowIndex = 0;
	var inputsTreeObj;
	var schemeGrid;
	var schemeViewGrid;
	var configArea = $('#criteriaSchemeConfig');
	var addBtn = $('button[name=add]', configArea);
	
	var zTreeSetting = {
		view: {
			showTitle: true
		},
		data: {
			key: { title: 'title' }
		},
		callback: {
			onDblClick: function(event, treeId, treeNode) {
				// 这个变量在extPointCriteria.jsp文件里定义
				if(treeNode != null) {
					if(typeof(CampaignAddRule) != 'undefined') {
						if(CampaignAddRule.addRuleValid(treeNode.getParentNode().groupId) == false) {
							return ;
						}
					}
					if(_t.addCriterion(schemeGrid, treeNode.inputId, rtScheme.change) == false) {
						if(typeof(CampaignAddRule) != 'undefined') {
							CampaignAddRule.removeRuleValid(treeNode.getParentNode().groupId);
						}
					}
					
					//_t.addCriterion(schemeGrid, treeNode.inputId, rtScheme.change);
				}
			},
			beforeClick: function(treeId, treeNode, clickFlag) {
				return (treeNode != null && treeNode.isParent === false); //限制只有叶子节点可以选中
			}
		}
	};
	
	inputsTreeObj = $.fn.zTree.init($('#inputsTree'), zTreeSetting);
	
	autoAddScheme.addBtn = addBtn;
	autoAddScheme.inputsTreeObj = inputsTreeObj;
	
    schemeGrid = _t.createGrid('scheme', false);
    
    schemeViewGrid = _t.createGrid('schemeView', true);
    
	addBtn.click(function() {
		var nodes = inputsTreeObj.getSelectedNodes(inputsTreeObj);
		if(nodes.length == 0) {
			$.msgBox('info', '', '请在左侧的可选条件中选择所需的添加项！');
		} else {
			$.each(nodes, function(idx, node) { 
				// 这个变量在extPointCriteria.jsp文件里定义，主要用于积分规则添加判断
				if(typeof(CampaignAddRule) != 'undefined') {
					if(CampaignAddRule.addRuleValid(node.groupId) == false) {
						return ;
					}
				}
				
				if(_t.addCriterion(schemeGrid, node.inputId, rtScheme.change) == false) {
					// 这个变量在extPointCriteria.jsp文件里定义，主要用于积分规则添加判断
					if(typeof(CampaignAddRule) != 'undefined') {
						CampaignAddRule.removeRuleValid(node.groupId);
					}
				}
				//_t.addCriterion(schemeGrid, node.inputId, rtScheme.change);
			});
		}
		
		return false;
	});
	
	configArea.on('click', 'button[name=delete]', function(event) {
		if(typeof(CampaignAddRule) != 'undefined') {
			CampaignAddRule.removeRuleValid(schemeGrid.getRowData($(this).val()).groupId);
		}
		
		schemeGrid.delRowData($(this).val());
		event.stopPropagation();
	});
	
	//变更了类表中的表达式值，控制区间和非区间条件导致录入框的增减
	$('#criteriaSchemeConfig').on('change', 'select[name=operatorWidget]', function(event) {
		var $this = $(this);
		var prevValue = $this.data('previousValue');//get previous value
		var value = $this.val();
		
		var row = $this.parent().parent();
		if(prevValue == 'between') {
			row.find('span[name=inputWidgetAltSpace]').hide();
		}
		else if(value == 'between') {
			row.find('span[name=inputWidgetAltSpace]').show();
		}
		
		$this.data('previousValue', value);  //store the new previous value.
	});
	
 	schemeAction = {
 		//把scheme对象转换为json字符串，并进行URI encode. 
 		getSchemeData: function() {
 			return encodeURI(JSON.stringify(schemeAction.getScheme()));
 		},
 		getScheme : function() {
 			var rowIds = schemeGrid.getDataIDs();
 			var schemeData = new Array();
			for(var i=0; i<rowIds.length ; i++) {
				var rowId = rowIds[i];
				var row = $('#' + rowId);
				var op = row.find('select[name=operatorWidget]').val();
				var value;
				if(op == 'between') {
					var value1 = row.find('[name=inputWidget]').wrender('getValue');
					var value2 = row.find('[name=inputWidgetAlt]').wrender('getValue');
					value = [value1['inputWidget'], value2['inputWidgetAlt']];
				}
				else {
					value = row.find('[name=inputWidget]').wrender('getValue')['inputWidget'];
				}
				var inputId = schemeGrid.getCell(rowId, 'inputId');
				var inputLabel = schemeGrid.getCell(rowId, 'inputLabel');
				
				var input = rtScheme.cache.inputs[inputId];
				schemeData.push({'inputId':inputId, 'groupId':input.groupId, 'label':inputLabel, 'groupLabel':input.groupLabel, 'operator':op, 'value':value});
			};
			
 			return schemeData;
 		},
 		validateScheme : function() {
 			var rowIds = schemeGrid.getDataIDs();
 			var schemeData = new Array();
			//[
			//  {
			//		'label':inputLabel, 
		    //		'operator':op, 
		    //		'value':value, 
		    //		'rowId': rowId, 
		    //		'input': {
			//			id : "birthday",
			//          groupId: "base", 
			//			label : "出生日期",
			//			desc : "出生日期",
			//			type : "date",
			//			allowedOperators : "between,eq,lt,gt,le,ge",
			//			repeatable : false,
			//			validate : {dataType:'date'}
			//		}
			//	}
			//]
			for(var i=0; i<rowIds.length ; i++) {
				var rowId = rowIds[i];
				var row = $('#' + rowId);
				var op = row.find('select[name=operatorWidget]').val();
				var value;
				if(op == 'between') {
					var value1 = row.find('[name=inputWidget]').wrender('getValue');
					var value2 = row.find('[name=inputWidgetAlt]').wrender('getValue');
					value = [value1['inputWidget'], value2['inputWidgetAlt']];
				}
				else {
					value = row.find('[name=inputWidget]').wrender('getValue')['inputWidget'];
				}
				
				var inputId = schemeGrid.getCell(rowId, 'inputId');
				var inputLabel = schemeGrid.getCell(rowId, 'inputLabel');
				var input = rtScheme.cache.inputs[inputId];
				
				var schemeRow = {'operator':op, 'value':value, 'rowId': rowId, 'input': input};
				
				//Do the default row validate
				//It will be stop on the first validation failure
				if(_t.validateRow(schemeRow)) {
					schemeData.push(schemeRow);	//Add the row data for next extension validation.
				}
				else {
					return false;
				}

			};
			
			//Add the row data for next extension validation.
 			return rtScheme.setting.validate(schemeData);
 		},
 		/**
 		 * @param setting 配置信息
 		 * @param scheme 当前使用的scheme对象
 		 * @param change change事件的回调函数，可以接受一个参数或零参数函数，一个参数时会传递控件当时的值
 		 */
 		applyScheme : function(setting, scheme, change) {

 			rtScheme = _t.init(setting, scheme, change);
 			gridRowIndex = 0;
 			
 			//重建树控件
 			inputsTreeObj.destroy();
 			inputsTreeObj = $.fn.zTree.init($('#inputsTree'), zTreeSetting);
 			
 			//加载树控件数据
 			for(gidx in setting.inputsGroups) {
 				var group = setting.inputsGroups[gidx];
 				var gnode = inputsTreeObj.addNodes(null, {name: group.label, title: group.desc, groupId: group.id}, true);
 				
 				for(iidx in group.inputs) {
 					var i = group.inputs[iidx]; //get input object from group 
 					var d = {name: i.label, title: i.desc, inputId: i.id};
 					if(i.repeatable == false) {
 						d.icon = $.wrender.context + '/images/add-once.png';
 					}
 					
 					var inode = inputsTreeObj.addNodes(gnode[0], d, true);
 				}
 			}
 			
 			//清空schemeGrid对象
 			schemeGrid.clearGridData();
 			schemeGrid.setGridParam({sortorder:'desc'});
 			schemeGrid.sortGrid('groupLabel', true);
 			
 			//加载schemeGrid对象中的数据
 			for(sidx in rtScheme.scheme) {
 				var criterion = rtScheme.scheme[sidx];
 				if(typeof(CampaignAddRule) != 'undefined') {
 					CampaignAddRule.initData(criterion.groupId);
 				}
 				_t.addCriterion(schemeGrid, criterion.inputId, criterion, change);
 			}
 		},
 		applySchemeView : function(setting, scheme) {
 			rtScheme = _t.init(setting, scheme);
 			gridRowIndex = 0;
 			schemeViewGrid.clearGridData();
 			schemeViewGrid.setGridParam({sortorder:'desc'});
 			schemeViewGrid.sortGrid('groupLabel', true);
 			
 			//加载schemeGrid对象中的数据
 			for(sidx in rtScheme.scheme) {
 				var criterion = rtScheme.scheme[sidx];
 				_t.addCriterion(schemeViewGrid, criterion.inputId, criterion);
 			}
 			
 	 		$('[wrType]', schemeViewGrid).wrender('toReadOnly', null, true);
 		}
 	}
});
	