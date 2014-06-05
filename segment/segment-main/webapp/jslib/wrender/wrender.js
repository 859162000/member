/**
 * JQuery Widget Render plugins
 * 提供统一的方式对应用界面上常用的录入控件进行加载、处理和扩展。
 * 如： 日期控件、对选控件、单选控件
 */ 
(function($){

	//工具函数定义
	var _t = {
		getContextPath: function () {
		    var pathName = document.location.pathname;
		    var i = pathName.substr(1).indexOf("/");
		    return pathName.substr(0, i + 1);
		},
		render: function(type, elem) {
			//render one object
			var w = $.wrender[type];
			if(w == null) { 
				alert(_c.failType + type + '"!');
				return $(elem);
			}
			
			//To prevent duplicate render, set rendered flag in object. 
			//If the flag already existed, it will skip render.
			if(true != elem[_c.rendered]) {
				elem[_c.rendered] = true;
				return w.render.apply(w, [elem]);
			}
		},
		validate: function(type, elem) {
			if($.wrender[type] == null) { 
				alert(_c.failType + type + '"!');
				return false;
			}
			else {
				return true;
			}
		},
		show: function(type, elem) {
			//get value from one render object
			var w = $.wrender[type];
			if(w == null) { 
				alert(_c.failType + type + '"!');
				return;
			}
			
			if(w.show != undefined && typeof w.show == 'function') {
				w.show.apply(w, [elem]);
			}
			else {
				$(elem).show();
			}
		},
		hide: function(type, elem) {
			//get value from one render object
			var w = $.wrender[type];
			if(w == null) { 
				alert(_c.failType + type + '"!');
				return;
			}
			
			if(w.hide != undefined && typeof w.hide == 'function') {
				w.hide.apply(w, [elem]);
			}
			else {
				$(elem).hide();
			}
		}
	};
	
	//常量定义
	var _c = {
		type: 'wrType',
		param: 'wrParam',
		name: 'name',
		value: 'wrValue',   //element property to store the value
		rendered: 'wrRendered', //element property indicated this element already rendered
		failType: 'Failed to recognized the wrType:"',
		failAction: 'Plugin wrender failed to found the action:'
	};
	
	
	$.wrender = $.wrender || {};
	
	//有可能之前已经定义的了$.wrender.context，如果未定义通过_t.getContextPath()来取得
	if($.wrender.context == undefined) {
		$.wrender.context = _t.getContextPath();
	}
	
	$.extend($.wrender, {
		filterOutNull: function(value) { //convert null value into empty string
			var val;
			if(value == null) {
				val = ''; //convert null value into empty string
			}
			else {
				val = value;
			}
			return val;
		},
		getValueProp: function(elem) { //取得控件的'wrValue'属性值，这个变量是保存在控件对象上
			return elem[_c.value];
		},
		setValueProp: function(elem, value) { //设置控件的'wrValue'属性值，这个变量是保存在控件对象上
			elem[_c.value] = value;
		},
		getValue: function(elem) {
			var $elem = $(elem);
			var type = $elem.attr(_c.type);
			var retValue;
			if(_t.validate(type, elem)) {
				var w = $.wrender[type];
				/*
				if(w.getValue == undefined) {
					console.log('getValue undefined! type=' + type);
					console.log(w);
					console.log(elem);
				}
				*/
				retValue = w.getValue.apply(w, [elem]);
				if(retValue == null) {
					retValue = '';
				}
				
				elem[_c.value] = retValue; //store the value into element's wrValue property.
			}
			else {
				retValue = null;
			}
			return retValue;
		},
		setValue: function(elem, value) {
			var $elem = $(elem);
			var type = $elem.attr(_c.type);
			if(value !== undefined) {
				if(_t.validate(type, elem)) {
					elem[_c.value] = value; //set value into the element "wrValue" property
					var w = $.wrender[type];	
					w.setValue.apply(w, [elem, value]);	
					
					//Test the element, which is in readonly or not
					var labelElm = $elem.prev('span[name=readlabel]');
					if(labelElm.size() > 0) {
						//set the readonly label display
						labelElm.html(w.getValueLabel.apply(w, [elem, value]));
					}
				}
			}
		},
		//包装变更事件函数，jquery的事件函数是function(event)
		//而wrender的变更事件函数是 function(value) this 函数是指向wrender的elem.
		wrapChange: function(elem, fun) {
			var wrap = function (event) {
				//定义函数参数为0，表示不接收value参数
				if(fun.length == 0) {
					fun.call(this);
				}
				else if(fun.length == 1) {
					var value = $.wrender.getValue(this);
					fun.apply(this, [value]);
				}
				else {
					alert('wrender event function change() only support 0 or 1 arguments!');
				}

			};
			
			return wrap;
		},
		getParam: function(elem) {
			var paramStr = $(elem).attr(_c.param);
			if(paramStr != null) {
				var paramArray = paramStr.split(';');
				if(paramArray != null && paramArray.length > 0) {
					var paramObj = {};
					$.each(paramArray, function() {
						var pair = this.split(':');
						if(pair != null && pair.length >= 2) {
							var pName = pair[0].replace(/(^\s*)|(\s*$)/g,'');
							
							var pValue = pair[1].replace(/(^\s*)|(\s*$)/g,'');
							paramObj[pName] = pValue;
						}
					});
					return paramObj;
				}
			}
			return null;
		},
		getHtml: function(name, type, param) {
			var w = $.wrender[type];
			if(w != null && w.getHtml != null) {
				return w.getHtml(name, param);
			}
			else {
				alert("Can't support wrender getHtml method for type:" + type + " !");
				return null;
			}
		},
		getOperatorHtml: function(name, allowedOperators) { //Get the operations select list HTML
			//operator="true" 是用来做过滤标志，和其他select标签区分开。
			// wrType="select"是用来在$('...').wrender('setValue', '...') 赋值时可以统一自动赋值用
	 		html =	'<select wrType="select" operator="true" name="' + name + '">';
			$.each(allowedOperators, function(idx, op) {
				var optrim = op.replace(/(^\s*)|(\s*$)/g,'');
				var label = $.wrender.operatorLabelMap[optrim];
				if(label != null) {
					html += '<option value="' + optrim + '">' + label + '</option>';
				}
				else {
					alert('Operator "' + optrim + '" not exists! Please check configuration!');
				}
			});
			
			html +=	'</select>';
			return html;
		},
		operatorLabelMap: {
			'eq': '=', 
			'ne': '!=',
			'gt': '&gt;', 
			'lt': '&lt;', 
			'ge': '&gt;=', 
			'le': '&lt;=', 
			'like': '匹配',
			'between': '区间',
			'nbetween': '区间外',
			'in': '包含',
			'nin': '不包含'
		}
	});
	
	//定义jquery插件
	$.fn.wrender = function(action, data, option) {
		action = action || 'render';
		switch(action) {
		case 'render':
			return this.each(function() {
				var wrType = $(this).attr(_c.type);
				_t.render(wrType, this);
			});
		case 'setValue':
			return this.each(function(idx, elem) {
				var name = $(elem).attr(_c.name);
				var value = data[name];
				$.wrender.setValue(elem, value);
			});
		case 'getValue':
			var retGetValue = {};
			this.each(function(idx, elem) {
				var name = $(elem).attr(_c.name);
				var value = $.wrender.getValue(elem);
				if(value != null) {
					retGetValue[name] = value;
				}
			});
			return retGetValue;
		case 'getValueData': //该操作和getValue基本相同，只是在返回值是进行对象到string的转换，并进行URI编码。
			var retGetValueData = {};
			this.each(function(idx, elem) {
				var name = $(elem).attr(_c.name);
				var value = $.wrender.getValue(elem);
				retGetValueData[name] = value;
			});
			return encodeURI(JSON.stringify(retGetValueData));
		case 'getValueLabel': 
			var retLabel = {};
			this.each(function(idx, elem) {
				var $elem = $(elem);
				var type = $elem.attr(_c.type);
				var name = $elem.attr(_c.name);
				
				if(_t.validate(type, elem)) {
					var w = $.wrender[type];
					var value = elem[_c.value];
					retLabel[name] = w.getValueLabel.apply(w, [elem, value]);
				}
				else {
					retLabel[name] = null;
				}
			});
			
			return retLabel;
		case 'toReadOnly':
			return this.each(function(idx, elem) {
				var $elem = $(elem);
				var type = $elem.attr(_c.type);
				var name = $elem.attr(_c.name);
				
				if(	$elem.is("input[type=hidden]") && 
						(type == "text" || type == "readtext")) {
					//skip the hidden input element
					return;
				}
				

				
				//If data not null, the setValue method will invoke first
				if(data != null) {
					$.wrender.setValue(elem, data[name]);
				}
				
				if(option === true) {
					var w = $.wrender[type];
					var value = elem[_c.value];
					var label = w.getValueLabel.apply(w, [elem, value]);
					var labelElm = $elem.prev('span[name=readlabel]');
					if(labelElm.size() > 0) {
						labelElm.show();
						labelElm.html(label);
					}
					else {
						$elem.before('<span name="readlabel">' + label + '</span>');
					}
					
					_t.hide(type, elem);
				}
				else {
					$elem.prev('span[name=readlabel]').hide();
					_t.show(type, elem);
				}
			});
		case 'change':
			if(data == null || (typeof data != 'function')) {
				alert('Error wrender "change" defintion! The second parameter must be a function!');
				return this;
			};
			
			return this.each(function(idx, elem) {
				var $elem = $(elem);
				var type = $elem.attr(_c.type);
				if(_t.validate(type, elem)) {
					var w = $.wrender[type];
					if(w.change != undefined && (typeof w.change == 'function')) {
						w.change.apply(w, [elem, data]);//invoke the change method in widget
					}
					else {
						alert('The wrender plugin type=' + type +' not support the change(elem, onChangeFunctoin) method!');
					}
				}
			});
		default : 
			alert(_c.failAction + action);
			return this;
		}

	};

	
	
})(jQuery);