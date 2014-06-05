(function($){
	
	$.extend($.search,{
		
		//得到搜索条件表单HTML in search div and SchemeConfig dialog
	 	getInputHtml: function(name, value, inputDef) {
	 		//根据不同的录入类型得到相应的html代码
	 		
			//搜索条件输入框html串
			var html = "";
			//definition已定义,并根据类型拼相应的html代码
			if(inputDef!=undefined){
				switch(inputDef.type) {
				case 'TEXT':
					html = '<input type="text" class="input" name="' + name + '" value="' + ((value == undefined) ? '' : value) + '" ' + '></input>';
					break;
				case 'TEXTAREA':
					html = '<textarea name="' + name + '" rows="3" cols="50" >' + ((value == undefined) ? '' : value) + '</textarea>';
					break;
				case 'DATE':
					html = '<input type="text" class="input_date date" name="' + name + '" value="' + ((value == undefined) ? '' : value) + '" />';
					break;
				case 'DATE_RANGE':
					html = '<input type="text" class="start_date date" valueName="' + name + '"/>'
					+ '~'
					+ '<input type="text" class="end_date date" />'
					+ '<button type="button" class="btn notext clrdate">清空日期</button>'
					+ '<input type="hidden" name="' + name + '" value="' + ((value == undefined) ? '' : value) + '" />';
					break;
				case "CONITEM":
					html = '<input type="hidden" name="' + name + '" value="' + ((value == undefined) ? '' : value) + '" />'  
					+ '<input type="text" valueName="' + name + '" ' + ' class="input_conitem"  />'
					+ '<span class="label_conitem" style=""></span>';
					break;
				case "CONCATEGORY":
					html = '<input type="hidden" name="' + name + '"  value="' + ((value == undefined) ? '' : value) + '" />'
					+ '<input type="text" valueName="' + name + '" ' + ' class="input_concategory"  />';
					break;
				case "CONPROVIDER":
					html = '<input type="hidden" name="' + name + '"  />' 
					+ '<input type="text" name="label_' + name + '" ' + ' class="input_conprovider"  />';
					break;
				case "DIM":
					html = '<select class="select_dim" name="' + name + '" selectedValue="' + ((value == undefined) ? '' : value) + '">' + $("#"+inputDef.dimElementId).html() + '</select>';
					break;
				case "CINEMA_MULTI_SELECT":
					html = '<select multiple="multiple" class="select_multiple area" name="area_' + name + '">' 
					 + $("#"+inputDef.areaDimElementId).html() + '</select>'
					 + '<select multiple="multiple" class="select_multiple cinema" valueName="' + name + '" />'
					 + '<input type="hidden" class="input" name="' + name + '" value="' + ((value == undefined) ? '' : value) + '" />';
					break;
				}
			}
			return html;
		},
		

	 	//加入一行搜索条件 in SchemeConfig dialog
	 	getInputRowHtml: function(index, schemeInput, inputDef) {
	 		if(inputDef == undefined) {
	 			return;  //用户在配置中已经把某一类型input设置删除后，但保存的方案中还存有相应引用的情况。
	 		}
	 		
	 		var name = schemeInput.name;
	 		var label = schemeInput.label;
	 		var relation = schemeInput.relation;
	 		var operator = schemeInput.operator;
	 		var order = schemeInput.order;
	 		var required = schemeInput.required;
	 		var value = schemeInput.value;
	 		
	 		var def_allowedOperators = inputDef.allowedOperators;
	 		
	 			
			var sel = 'selected="selected"';
			var html = '<tr><td>' +
				'<input type="hidden" name="inputs.name_' + index + '" value="' + name + '"/>';
				
			//第一行不用插入"关系"列
			if(index != 0) {
				html +=	'<select name="inputs.relation_' + index + '">' +
						'<option value="and" ' + ((relation=='and')?sel:'') + '>与</option>';
						//+ '<option value="or" ' + ((relation=='or')?sel:'') + '>或</option></select>';
			}
			
			html +=	'</td>' +
				'<td><input type="text" style="width:90px;" name="inputs.label_' + index + '" value="' + label + '"/></td>' +
				'<td>'+
				'<select name="inputs.operator_' + index + '">';
			
			if(def_allowedOperators != undefined && def_allowedOperators != null && def_allowedOperators.length > 0){
				var def_operators_array = def_allowedOperators.split(',');
				for ( var i = 0; i < def_operators_array.length; i++) {
					var opr = def_operators_array[i];
					if(opr == 'dateRange') {
						html +=	'<option value="dateRange" ' + ((operator=='dateRange')?sel:'') + '>日期范围</option>';
					} else if(opr == 'between') {
						html +=	'<option value="between" ' + ((operator=='between')?sel:'') + '>区间</option>';
					} else if(opr == 'in') {
						html +=	'<option value="in" ' + ((operator=='in')?sel:'') + '>包含多选</option>';
					} else if(opr == 'like') {
						html +=	'<option value="like" ' + ((operator=='like')?sel:'') + '>模糊查询</option>';
					} else if(opr == '<=') {
						html +=	'<option value="<=" ' + ((operator=='<=')?sel:'') + '>&lt;=</option>';
					} else if(opr == '>=') {
						html +=	'<option value=">=" ' + ((operator=='>=')?sel:'') + '>&gt;=</option>';
					} else if(opr == '>') {
						html +=	'<option value=">" ' + ((operator=='>')?sel:'') + '>&gt;</option>';
					} else if(opr == '!=') {
						html +=	'<option value="!=" ' + ((operator=='!=')?sel:'') + '>!=</option>';
					} else if(opr == '=') {
						html +=	'<option value="=" ' + ((operator=='=')?sel:'') + '>=</option>';
					}
				}
			} else {
				html +=	'<option value="=" ' + ((operator=='=')?sel:'') + '>=</option>' +
					'<option value="!=" ' + ((operator=='!=')?sel:'') + '>!=</option>' +
					'<option value=">" ' + ((operator=='>')?sel:'') + '>&gt;</option>' +
					'<option value="<" ' + ((operator=='<')?sel:'') + '>&lt;</option>' +
					'<option value=">=" ' + ((operator=='>=')?sel:'') + '>&gt;=</option>' +
					'<option value="<=" ' + ((operator=='<=')?sel:'') + '>&lt;=</option>' +
					'<option value="like" ' + ((operator=='like')?sel:'') + '>模糊查询</option>' +
					'<option value="between" ' + ((operator=='between')?sel:'') + '>区间</option>'+
					'<option value="dateRange" ' + ((operator=='dateRange')?sel:'') + '>日期范围</option>'+
					'<option value="in" ' + ((operator=='in')?sel:'') + '>包含多选</option>';
			}
			
			html +=	'</select></td>' +
				'<td>' + $.search.html.getInputHtml('inputs.value_' + index, value, inputDef)  + '</td>' + 
				'<td><input type="text" name="inputs.order_' + index + '" value="' + order + '" style="width:30px;"/></td>' + 
				'<td>' + 
			    ((inputDef.required != true) ?  '<button name="deleteInputButton" value="' + name + '" style="width:30px;height:25px;">-</button>' : '&nbsp;') +
			    '</td></tr>';
			return html;
		}
			
	});

})(jQuery);

