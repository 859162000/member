/**
 * JQuery Widget Render plugin
 * 组合查询选择控件
 */ 
(function($){

	
	var _t = {
		defCache: {},  //tree definitions cache
		popDialogCache: {}, 
		getInnerHtml: function(popupLabel) {
			var html = '<span name="valueLabel"></span><button name="popup" class="wr_button" type="button">' + popupLabel + '</button>';
			return html;
		},
		getPopDialogHtml: function(treeId) {
			var html = 
			'<div id="tree_' + treeId + '"><ul id="tree_ul_' + treeId + '" class="ztree"></ul></div>';
			return html;
		},
		toLabelString: function(treeId, value) { //Convert the value(json object) into label string
			var label = '';
			if(value != null) {
				var tree = _t.popDialogCache[treeId].tree;
				
				var firstLabel = true;
				for(var i=0 ; i<value.length ; i++) {
					var v = value[i];
					var ns = tree.getNodesByParam("id", v, null);
					if(ns != null && ns.length == 1) {
						if(firstLabel == false) {
							label += '; ';
						}
						label += ns[0].name;
						firstLabel = false;
					}
					else {
						alert('Failed to find the tree node id=' + v + ',treeId=' + treeId + ', which was existed in value.');
					}
				}
			}
			
			return label;
		}
	};
	
	var popdlg = null;
	var getPopDialog = function (definition) {
		var popdlg = _t.popDialogCache[definition.id];
		if(popdlg == null) {
			//Create the tree popup dialog object.
			popdlg = {
				def: null,
				dialog: null,  //Store the jQuery dialog object
				grid: null,
				elem: null, //The html tag, which store the value label.
				init: function(definition) {
					var def = this.def = definition;
					var dialogId = '#tree_' + def.id;
					var self = this;
	
					if($(dialogId).size() == 0) {
						//If this tree HTML is not existed, append the new one at the tail of body tag.
						$('body').append(_t.getPopDialogHtml(def.id));
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
									self._applyValue.apply(self);
									$(this).dialog('close');
								},
								"取消": function(){ $(this).dialog('close'); }
							},
							close: function() {
								
							}
						});
						this.dialog = $(dialogId);

						//var treeWidth = def.width - 26;
						this.tree = $.fn.zTree.init($('#tree_ul_' + def.id), {
		 					async: {
		 						enable: true,
		 						async: false, //设为同步加载模式，保证数据加载完成后再使用。
		 						url:def.url,
		 						autoParam:['id']
		 					},
		 					check: {
		 						enable: true
		 					},				
		 					data: {
		 						simpleData: {
		 							enable: true,
		 							idKey: "id",
		 							pIdKey: "pid",
		 							rootPId: null
		 						}
		 					}
		 				});
	
			 			$(dialogId + ' button.wr_button').button(); //Render buttons
			 			

					}
					
					this.tree =  $.fn.zTree.getZTreeObj('tree_ul_' + def.id);
					this.dialog = $(dialogId);
				},
				open: function(value, elem) {
					//清除grid中的数据
					this.tree.checkAllNodes(false);
					this.elem = elem;
					if(value != null && value.length > 0) {
						//为树状控件进行勾选
						for(var i=0 ; i<value.length ; i++) {
							var v = value[i];
							var ns = this.tree.getNodesByParam("id", v, null);
							if(ns != null && ns.length == 1) {
								this.tree.checkNode(ns[0]);
							}
							else {
								alert('Failed to find the tree node id=' + v + ', treeId=' + treeId + ',  which was existed in value.');
							}
						}
					}

					return this.dialog.dialog('open');
				},
				_applyValue : function() { //点击选定后的响应函数，把当前界面的选择正式生效为控件值
					//create the return object
					var retValue = [];
					
					var nodes = this.tree.getCheckedNodes(true);
					for (var i=0; i<nodes.length; i++) {
						var n = nodes[i];
						
						if(this.def.leafValueOnly) {
							if(n.isParent == false) {
								retValue.push(n.id);
							}
						}
						else {
							retValue.push(n.id);
						}
						
					}
		
					//设置值
		 			$.wrender.setValueProp(this.elem, retValue);

		 			//设置label
					var param = $.wrender.getParam(this.elem); 
					var treeId = param.treeId;
					var label = _t.toLabelString(treeId, retValue);
					$('span[name=valueLabel]', this.elem).html(label);
					
		 			//触发change事件，如果有定义
					if(this.elem.wrChange != undefined) {
						var t = this.elem;
						var len = t.wrChange.length;
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
		 		}
			};
			
			popdlg.init(definition);
			_t.popDialogCache[definition.id] = popdlg; //Stored the new initialized one into cache
		}
		
		return _t.popDialogCache[definition.id];
	};
	
	$.extend($.wrender, {
		tree: {
			render: function(elem) {
				var $elem = $(elem);    
				var param = $.wrender.getParam(elem);
				var treeId = param.treeId;
				var def = _t.defCache[treeId];                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             
				//check the definition existing
				if(def == undefined) {
					alert('Failed to find the tree definition of "treeId=' + param.treeId + '"!');
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
				$('span[name=valueLabel]', elem).html(this.getValueLabel(elem, value));
			},
			getValue: function(elem) {
				return $.wrender.getValueProp(elem);
			},
			getValueLabel: function(elem) {
				var value = this.getValue(elem);
				var param = $.wrender.getParam(elem);
				var treeId = param.treeId;
				return _t.toLabelString(treeId, value);
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
				html = '<div wrType="tree" ' + paramAttr + 'name="' + name + '"></div>';
				return html;
			}
		},
		addTree: function(def) {
			_t.defCache[def.id] = def;
		}
	});

})(jQuery);