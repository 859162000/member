/**
 * JQuery Widget Render plugin
 * 下拉表单控件，包含两个插件单选和多选。
 */ 
(function($){
	
	var _t = {
		codeListUrl: $.wrender.context + '/segment/CodeListAction/getCodeList.do',
		cache: {},
		multiOptions: {
			selectedList : 50,
			minWidth: 270,
			selectedText : '已选择#项',
			noneSelectedText : '请选择...'
		},
		multiFilterOptions: {
 			label : '过滤',
 			width : 60,
 			placeholder : '输入关键字' 
		},
		renderOptions: function($wrObj, codeList) {
			var html = '';
			$.each(codeList, function() {
				html += '<option value="' + this.code + '">' + this.name + '</option>';
			});
			$wrObj.append(html);
		},
		render: function(wrObj, param) {
			var $wrObj = $(wrObj);
			if(param != null && param.sourceId != null && param.typeId != null) {
				//Try to retrieve from the cache
				var codeList = _t.cache[param.sourceId + '_' + param.typeId];
				if(codeList != null) {
					_t.renderOptions($wrObj, codeList);
				}
				else {
					//If can't retrieve from cache, try to get by AJAX request.
					$.ajax({
						async:false, 
						type: 'GET',
						dataType: 'json',
						url: _t.codeListUrl + '?sourceId=' + param.sourceId + '&typeId=' + param.typeId,
						success: function(result) {
							if(result != null) {
								_t.renderOptions($wrObj, result);
								_t.cache[param.sourceId + '_' + param.typeId] = result;
							}
						}
					});
				}

			}
		}
	};

	$.extend($.wrender, {
		//单选下拉框
		select: {
			render: function(wrObj) {
				var param = $.wrender.getParam(wrObj);
				_t.render(wrObj, param);
			},
			setValue: function(wrObj, value) {
				for(var i=0;i<wrObj.options.length;i++) {
					wrObj.options[i].selected = (wrObj.options[i].value == value);
				}
			},
			getValue: function(wrObj) {
				return $(wrObj).val();
			},
			getValueLabel: function(wrObj) {
				return $(wrObj).find(':selected').text();
			},
			change: function(elem, fun) {
				$(elem).change($.wrender.wrapChange(elem, fun));
			},
			getHtml: function(name, param) {
				var paramAttr = (param != null) ? 'wrParam="' + param + '"' : '';
				html = '<select wrType="select" ' + paramAttr + 'name="' + name + '"></select>';
				return html;
			}
		},
		
		//多选下拉框
		multiselect: {
			render: function(wrObj) {
				var $wrObj = $(wrObj);
				var param = $.wrender.getParam(wrObj);
				_t.render(wrObj, param);
				$wrObj.multiselect(_t.multiOptions);
				if(param != null && param.filter == 'true') {
					$wrObj.multiselectfilter(_t.multiFilterOptions);
				}
			},
			setValue: function(wrObj, value) {
				var $wrObj = $(wrObj);
				$wrObj.multiselect('uncheckAll');
				for (var idx in value) {
					$wrObj.find('[value="' + value[idx] + '"]').attr('selected',true);
				}
				$wrObj.multiselect('refresh');
			},
			getValue: function(wrObj) {
				var value = new Array();
				var checkedOptions = $(wrObj).multiselect('getChecked');
				if(checkedOptions.length > 0) {
					for (var i = 0; i < checkedOptions.length; i++)  {
						value.push(checkedOptions[i].value);
					}
				}
				return value;
			},
			getValueLabel: function(wrObj) {
				var value = this.getValue(wrObj);
				
				//get match value label and build them into array
				var label = '';
				var param = $.wrender.getParam(wrObj);
				var codeList = _t.cache[param.sourceId + '_' + param.typeId];
				if(codeList != null) {
					var map = {};
					$.each(codeList, function(idx, obj){
						map[obj.code] = obj.name;
					});
					
					var firstLabel = true; 
					$.each(value, function(idx, val){
						if(map[val] != null) {
							if(firstLabel == false) {
								label += ';';
							}
							label += map[val];
							firstLabel = false;
						}
					});
				}

				return label;
			},
			show: function(wrObj) {
				$(wrObj).next('button.ui-multiselect').show();
			},
			hide: function(wrObj) {
				$(wrObj).next('button.ui-multiselect').hide();
			},
			change: function(elem, fun) {
				$(elem).change($.wrender.wrapChange(elem, fun));
			},
			getHtml: function(name, param) {
				var paramAttr = (param != null) ? 'wrParam="' + param + '"' : '';
				html = '<select wrType="multiselect" multiple="multiple" style="display:none;" ' + paramAttr + 'name="' + name + '"></select>';
				return html;
			}
		}
	});

})(jQuery);