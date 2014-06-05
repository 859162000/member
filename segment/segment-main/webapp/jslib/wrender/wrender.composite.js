/**
 * JQuery Widget Render plugin
 * 组合查询选择控件
 */ 
(function($){

	var _t = {
		defCache: {},  //composite definitions cache
		popDialogCache: {}, 
		getInnerHtml: function(popupLabel) {
			var html = '<span name="valueLabel"></span><button name="popup" type="button" class="wr_button">' + popupLabel + '</button>';
			return html;
		},
		getPopDialogHtml: function(compositeId, selTarget, hideSwitchLabel, switchLabel) {
			var html = 
			'<div id="composite_' + compositeId + '">' +
			'<table width="100%" align="center" class="ui-widget ui-widget-content">' +
				'<tr><td>' +
					'<form id="composite_form_' + compositeId + '"><table name="criteria" width="100%"></table></form>' +
				'</td><td width="100"  valign="top" align="center">' +
					'<button name="onSearch" class="wr_button" type="button">搜索</button><br/>' +
					'<label ' + ((hideSwitchLabel) ? 'style="display:none;"' : '') + '>' +
					'<input type="checkbox" name="selTarget" ' + ((selTarget) ? 'checked="checked"' : '' ) + '/>' + switchLabel + '</label>' + 
				'</td></tr>' +
			'</table><br/><div style="width:100%">' +
				'<table id="composite_list_' + compositeId + '"></table>' +
				'<div id="composite_pager_' + compositeId + '" style="text-align:center;"></div>' +
			'</div></div>';
			return html;
		},
		toLabelString: function(value) { //Convert the value(json object) into label string
			var label = '';
			if(value != null) {

				if(value.selTarget) {
					
					var len = value.selections.value.length; 
					if(len != value.selections.valueLabel.length) {
						alert("Error convert the value to label from composite! (selections.valueLabel.length != selections.value.length)");
					}
					if(len > 0) {
						label += '<span style="font-weight:bold;">已选:</span>';
						for(var i=0 ; i<len ; i++) {
							var l = value.selections.valueLabel[i];
							if(i != 0)   label += '; ' + l;
							else         label += l;
						}
					}
				}
				else {
					var clen = value.criteria.length;
						
					var first = true;
					for(var i=0 ; i<clen ; i++) {
						var c = value.criteria[i];
						var isEmpty = false;
						if(c.value instanceof Array) {
							for(var j=0,len=c.value.length;j<len;j++) {
								if(c.value[j] == null || c.value[j] === '') {
									isEmpty = true;
									break;
								}
							}
						}
						
						if(c.value != null && c.value !== '' && !isEmpty/*c.value !== []*/) {
							var op = $.wrender.operatorLabelMap[c.operator];
							var v = '';
							if(op === 'between' && (c.valueLabel[0] != null || c.valueLabel[1] != null)) {
								v = c.valueLabel[0] + '~' + c.valueLabel[1];
							}
							else if(c.valueLabel instanceof Array) {
								var vllen = c.valueLabel.length;
								var firstal = true;
								for(var j=0 ; j<vllen ; j++) {
									var vl = c.valueLabel[j];
									if(vl != null) {
										if(firstal)    v += vl;
										else           v += '; ' + vl;
										
										firstal = false;
									}
								}
							}
							else {
								v = c.valueLabel;
							}
							
							if(v.length > 0) {
								if(first == false)  label += '; ';
								label += '<span style="font-weight:bold;">' + c.label + '</span>' + op + '[' + v +']';
								first = false;
							}
						}
					}
					
					if(label.length > 0) {
						label =  '<span style="font-weight:bold;">条件:</span>' + label;
					}
					
				}
			}

			return label;
		},
		retrieveCriteria: function(def) {  //get criteria from composite form, criteria table
			var criteria = new Array();
 			for(var idx in def.inputs) {
 				var i = def.inputs[idx]; //get input def
				var prefix = 'queryParam.' + i.id;
				var listTable = $('#composite_form_' + def.id + ' table[name=criteria]');
				var inputTag1 = listTable.find('[name="' + prefix + '"]');
				var inputTag2 = listTable.find('[name="' + prefix + 'Alt"]');
				var operatorTag = listTable.find('select[name="' + prefix + 'Operator"]');
				
				var op = operatorTag.wrender('getValue')[prefix + 'Operator'];
				var v;
				var vlabel;
				if(op === 'between') {
					var v1 = inputTag1.wrender('getValue')[prefix];
					var v2 = inputTag2.wrender('getValue')[prefix + 'Alt'];
					v = [v1, v2];
					
					var vlabel1 = inputTag1.wrender('getValueLabel')[prefix];
					var vlabel2 = inputTag2.wrender('getValueLabel')[prefix + 'Alt'];
					vlabel = [vlabel1, vlabel2];
				}
				else {
					v = inputTag1.wrender('getValue')[prefix];
					vlabel = inputTag1.wrender('getValueLabel')[prefix];
				}
				
				criteria.push({inputId : i.id, label: i.label, operator: op, value: v, valueLabel: vlabel});
 			}
 			return criteria;
		}
		
	}
	

	var getPopDialog = function (definition) {
		var popdlg = _t.popDialogCache[definition.id];
		if(popdlg == null) {
			//Create the composite popup dialog object.
			popdlg = {
				def: null,
				defaultDef: {
					id: null, //required string identifier 
					title: "Query Target",
					selTarget: true,	//显示复选框
					hideSwitchLabel: false,//隐藏复选框选项
					hideSpan: false,	//隐藏span内容
					clearCache: false, 	//显示是否清楚缓存
					switchLabel: "指定目标", //切换checkbox的显示名称。
					popupLabel: "选择目标",  //触发弹出选择的按钮名称
					queryUrl:  null,
					selValue: null, //选择返回值的列名
					selLabel: null, //选择返回显示名称的列名
					sortName: null, //排序列名
					sortOrder: "asc", //排序顺序, 可选值："asc", "desc"
					maxSelections: 200, //最大可选目标数量，缺省为200
					width: 700,
					height: 500,
					inputs: [
					    //Example: 
						//{id:"itemCode", label:"编码", type:"text", allowedOperators:"like,eq"},
					    //{id:"conCategoryId", label:"品类", type:"tree", param:"treeId:concategory;", allowedOperators:"in"},
				    ], 
				    columns: [
						//Example: 
				        //{name:'ITEM_CODE', label:'编码', width:90, hide:true},
				        //{name:'ITEM_NAME', label:'名称', width:90, sortable:true},
					]
				},
				dialog: null,  //Store the jQuery dialog object
				grid: null,
				elem: null, //The html tag, which store the value label.
				init: function(definition) {
					
					var def = this.def = $.extend({}, this.defaultDef, definition);
					
					
					var dialogId = '#composite_' + def.id;
					var self = this;
	
					if($(dialogId).size() == 0) {
						//If this composite HTML is not existed, append the new one at the tail of body tag.
						$('body').append(_t.getPopDialogHtml(def.id, def.selTarget, def.hideSwitchLabel, def.switchLabel));
						$(dialogId).dialog({
							title: def.title,
							bgiframe:true,
							autoOpen:false,
							width: def.width,
							height: def.height,
							resizable:false,
							modal:true,
							buttons:{
								"选定": function(){
									if(self._applyValue.apply(self)) {
										$(this).dialog('close');
									}
								},
								"取消": function(){ $(this).dialog('close'); }
							},
							close: function() {
								
							}
						});
						this.dialog = $(dialogId);
						
						//Add criteria
			 			for(var idx in def.inputs) {
			 				this._addCriterion(def.inputs[idx])
			 			}
			 			
			 			var grid = $('#composite_list_' + def.id);
			 			var gridPager = $('#composite_pager_' + def.id);
			 			var gridWidth = def.width - 26;
	
			 			//To store and load the selected rows in grid while paging
			 			//selections format : {value1:label1, value2:label2 } 
			 			grid.data('selections', {});
			 			grid.jqGrid({
			 				datatype: 'local',
			 				ajaxGridOptions: {type:'POST'},
			 				jsonReader: {repeatitems: false},
			 				prmNames: {page:'queryParam.page', rows:'queryParam.rows', sort:'queryParam.sort', order: 'queryParam.order'},
			 				height: '100%',
			 				width: gridWidth, //deduce the dialog inner border space. 
			 				rowNum: 10,
			 				viewrecords: true,
			 				multiselect:true,
			 				pager: gridPager,
			 				url: def.queryUrl, 
			 				colModel: def.columns,
			 				sortname: def.sortName,
			 				sortorder: def.sortOrder,
			 				onSelectRow: function(rowid, status, e){ 
			 					var sels = $(this).data('selections');
			 					
			 					var rd = $(this).getRowData(rowid);
			 					var val = rd[def.selValue];
			 					var lab = rd[def.selLabel];
	
			 					if(status) {
			 						sels[val] = lab;
			 					}
			 					else {
			 						delete sels[val];
			 					}
	
			 			    },
			 			    onSelectAll: function(rowids, status){ 
								var sels = $(this).data('selections');
			 			    	
			 				    for(var i in rowids) {
			 					    var rowid = rowids[i];
				 					var rd = $(this).getRowData(rowid);
				 					var val = rd[def.selValue];
				 					var lab = rd[def.selLabel];
	
				 					if(status) {
				 						sels[val] = lab;
				 					}
				 					else {
				 						delete sels[val];
				 					}
			 			   		 }
			 				    
			 			    },
			 			    gridComplete: function() {
			 			    	//当grid加载完成后，通过 grid中data('selections')中记录的选中项对grid中的记录进行勾选。
			 			    	var $this = $(this);
			 			    	var sels = $this.data('selections');
			 		 			var rowIds = $this.getDataIDs();
			 					for(var i=0 ; i < rowIds.length ; i++) {
			 						var rowId = rowIds[i];
			 						var rowData = $this.getRowData(rowId);
			 						var valueCell = rowData[def.selValue];
			 						if(valueCell != null && valueCell != '' && sels[valueCell]) {
			 							$this.setSelection(rowId, false);
			 						}
			 					};
			 			    }
			 			});
			 			
			 			$(dialogId + ' button.wr_button').button(); //Render buttons
			 			$(dialogId + ' button[name=onSearch]').click(function() {//点击搜索事件
			 				self._search(def, grid);
			 			});    
			 			
			 			this.selTargetChange(grid, def.selTarget);

			
			 			$(dialogId + ' input[name=selTarget]').change(function() {  //更改是否指定明细
			 				self.selTargetChange(grid, this.checked);
						});
					}
					
					this.grid = $('#composite_list_' + def.id)
					this.dialog = $(dialogId);
				},
				selTargetChange: function(grid, selTarget) {  //更改是否指定明细
					var gridWidth = grid.getGridParam('width');//store the grid width
					
	 				if(selTarget) { grid.jqGrid('showCol', 'cb'); }
	 				else { grid.jqGrid('hideCol', 'cb');  }
	 				
	 				//reset the grid width into the original width, if its width has been changed.
	 				if( gridWidth != grid.getGridParam('width')) {
		 				grid.jqGrid("setGridWidth", gridWidth);   
	 				}
	 				
				},
				open: function(value, elem) {
					//清除grid中的数据
					this.grid.clearGridData();
					this.grid.data('selections', {}); 
					this.elem = elem;
					//alert("open");
					if(value != null) {
						//1. set the criterion values
						var len = value.criteria.length;
						var first = true;
						for(var i=0 ; i<len ; i++) {
							var c = value.criteria[i];
							var prefix = 'queryParam.' + c.inputId;
							var listTable = $('#composite_form_' + this.def.id + ' table[name=criteria]');
							var input1 = listTable.find('[name="' + prefix + '"]');
							var input2 = listTable.find('[name="' + prefix + 'Alt"]');
							var operatorTag = listTable.find('select[name="' + prefix + 'Operator"]');
							var row = input1.parent().parent();
							if(c.operator === 'between') {
								if(c.value != null) {
									if(!(c.value instanceof Array) || c.value.length != 2) alert('Error composite criterion value format! compositeId=' + this.def.id + ', inputId=' + c.inputId);
									var v1 = {};
									v1[prefix] = c.value[0];
									input1.wrender('setValue', v1);
									var v2 = {};
									v2[prefix + 'Alt'] = c.value[1];
									input2.wrender('setValue', v2);
								}
								
								row.find('span[name=inputWidgetAltSpace]').show();
							} else {
								if(c.value != null) {
									var v = {};
									v[prefix] = c.value;
									input1.wrender('setValue', v);
								}
								//alert("open--inputWidgetAltSpace");
								row.find('span[name=inputWidgetAltSpace]').hide();
							}
							
							var operatorVal = {};
							operatorVal[prefix + 'Operator'] = c.operator;
							operatorTag.wrender('setValue', operatorVal);
						}
						
						//2. Set selection rows
						if(value.selTarget && !this.def.clearCache) {
							var gridSels = {};
							for(var i in value.selections.value) {
								var v = value.selections.value[i];
								gridSels[v] = value.selections.valueLabel[i];
							}
							this.grid.data('selections', gridSels);
						}
						
						//3. set and apply selTarget
						$('input[name=selTarget]', this.dialog).attr('checked', value.selTarget);
						this.selTargetChange(this.grid, value.selTarget);
					}
					
					//重新进行查询
					this._search(this.def, this.grid);
	
					return this.dialog.dialog('open');
				},
				_applyValue : function() { //点击选定后的响应函数，把当前界面的选择正式生效为控件值
					
					//create the return object
					var retValue = {
						selTarget: this.dialog.find('input[name=selTarget]')[0].checked,
						criteria : null,
						selections : {value:[], valueLabel:[]}
					};
					
					//get criteria 
					retValue.criteria = _t.retrieveCriteria(this.def);
		 			
		 			//get value from current grid
					if(retValue.selTarget == true) {
			 			var gridSelections = this.grid.data('selections'); 
			 			
			 			if(gridSelections != null) {
			 				var selCount = 0;
			 				for(var v in gridSelections) {
			 					var l = gridSelections[v];
			 					retValue.selections.value.push(v);
			 					retValue.selections.valueLabel.push(l);
			 					selCount ++;
			 				}
			 				if(selCount > this.def.maxSelections) {
			 					$.msgBox('warn', '选择错误', '列表中的选择项过多！目前选择了'+selCount+'项数据，而最大限制是' + this.def.maxSelections);
			 					return false;
			 				} 
			 			}
					}
					else {
						this.grid.data('selections', {});//Clean the selections value 
					}
		 			
					$.wrender.setValueProp(this.elem, retValue); //set value
					$('span[name=valueLabel]', this.elem).html(_t.toLabelString(retValue)); //set lable
					// 是否隐藏span内容显示
					if(this.def.hideSpan) {
						$('span[name=valueLabel]', this.elem).css('display', 'none');
					}
					
					//触发change事件，如果有定义
					if(this.elem.wrChange != undefined) {
						var t = this.elem;
						var len = t.wrChange.length;
						//alert("apply");
						for(var fidx=0 ; fidx < len ; fidx ++) {
							var changeFun = t.wrChange[fidx];
							if(changeFun.length == 0) {
								changeFun.apply(t);
							}
							else {
								changeFun.apply(t, [retValue]);
							}
						}
					}
					
		 			return true;
		 		},
				_addCriterion: function (input) {
					var i = input;
					var ops = i.allowedOperators.split(',');
					var hasbetween = ($.inArray('between', ops) >= 0);
					var prefix = 'queryParam.' + i.id;
					var opHtml = $.wrender.getOperatorHtml(prefix + 'Operator', ops);
					var iHtml = (hasbetween) ? 
							$.wrender.getHtml(prefix, i.type, i.param) + 
							'<span name="inputWidgetAltSpace" style="display:none;"> ~  ' + 
							$.wrender.getHtml(prefix + 'Alt',  i.type, i.param) + '</span>' 
							: $.wrender.getHtml(prefix, i.type, i.param);
					var html = '<tr><td align="right"  width="100">' + i.label + '</td><td width="100">' + opHtml + '</td><td>' + iHtml + '</td></tr>';
					var listTable = $('#composite_form_' + this.def.id + ' table[name=criteria]');
					listTable.append(html);
					
					var input1 = listTable.find('[name="' + prefix + '"]').wrender();
					listTable.find('[name="' + prefix + 'Alt"]').wrender();
					
					var altSpaceTD = input1.parent().parent();
					var altSpace = altSpaceTD.find('span[name=inputWidgetAltSpace]');
					//alert("------------"+altSpace.length);
					//alert("aaaa"+input1.parent().parent().html());
					
					if(ops[0] === 'between') {
						altSpace.show();
					}
					
					//alert("bbbb"+listTable.html());
					//表达式变更事件, 控制 区间和非区间条件导致录入框的增减
					altSpaceTD.on('change', 'select[operator="true"]', function(event) {
						if($(this).val() == 'between') {
							altSpace.show();
						} else {
							altSpace.hide();
						}
					});
//					listTable.on('change', 'select[operator="true"]', function(event) {
//						if($(this).val() == 'between') {
//							altSpace.show();
//						} else {
//							altSpace.hide();
//						}
//					});
				},
				_search: function(def, grid) {
	 				var criteriaStr = JSON.stringify(_t.retrieveCriteria(def));
	 				grid.setGridParam({datatype:'json', postData:{'criteria': criteriaStr}, page:1}).trigger("reloadGrid");
				}
			}
			popdlg.init(definition);
			
			_t.popDialogCache[definition.id] = popdlg; //Stored the new initialized one into cache
		}
		
		return _t.popDialogCache[definition.id];
	}
	
	$.extend($.wrender, {
		composite: {
			render: function(elem) {
				var $elem = $(elem);    
				var param = $.wrender.getParam(elem);
				var compId = param.compositeId;
				var def = _t.defCache[compId];
				//check the definition existing
				if(def == undefined) {
					alert('Failed to find the composite definition of "compositeId=' + param.compositeId + '"!');
					return $(elem);
				}
				
				var popDialog = getPopDialog(def);
				
				$elem.append(_t.getInnerHtml(def.popupLabel));
				$('button.wr_button', elem).button();//Render the jquery button style
				$('button[name=popup]', elem).click(function() {
					
					popDialog.open($.wrender.getValueProp(elem), elem);
				});
				
			},
			setValue: function(elem, value) {
				//该控件的值已经通过外层的调用函数设置($.wrender('setValue', value))，无需在这里设置。
				//这里指需要设置其显示的值
				var vl = _t.toLabelString(value);
				$('span[name=valueLabel]', elem).html(vl);
			},
			getValue: function(elem) {
				return $.wrender.getValueProp(elem);
			},
			getValueLabel: function(elem) {
				return _t.toLabelString(this.getValue(elem));
			},
			change: function(elem, fun) {
				//change时间存放为元素的'wrChange'属性，并以数组方式存在，每调用一次本函数在数组中叠加一个成员。
				if(elem.wrChange == undefined) {
					elem.wrChange = [fun];
				}
				else {
					elem.wrChange.push(fun);
				}
			},
			getHtml: function(name, param) {
				var paramAttr = (param != null) ? 'wrParam="' + param + '"' : '';
				html = '<div wrType="composite" ' + paramAttr + 'name="' + name + '"></div>';
				return html;
			}
		},
		addComposite: function(def) {
			_t.defCache[def.id] = def;
		}
	});

})(jQuery);