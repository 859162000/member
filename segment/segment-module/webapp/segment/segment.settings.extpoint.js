(function($) {
	// 定义$.segment.setting.member{}对象，用于会员类客群配置
	$.segment = $.segment || {};

	$.extend($.segment, {
		settings : {
			extpoint : {
				version : '1.0',
				validate: function(schemeData) {
					var len = schemeData.length;
					var foundCinemaRow = false;//Flag, whether found the cinema condition row.
					var foundRegisterCinema = false;	//是否存在注册影城
					var foundRegisterDate = false;		//是否添加入会时间
					var foundTsaleGroupRow = false; 
					var foundWatchTradeCinema = false;
					var foundCsaleGroupRow = false;
					var foundConSaleCinema = false;
					
					for(var i=0 ; i<len ; i++) {
						var rowInput = schemeData[i].input;
						if(foundCinemaRow == false && 
							(rowInput.id == 'registerCinema' || rowInput.id == 'manageCinema' 
							|| rowInput.id == 'watchTradeCinema' || rowInput.id == 'conSaleCinema')  ) {
							foundCinemaRow = true;
						}
						
						if(foundTsaleGroupRow == false && rowInput.groupId == 'tsale') {
							foundTsaleGroupRow = true;
						}
						if(foundWatchTradeCinema == false && rowInput.id == 'watchTradeCinema') {
							foundWatchTradeCinema = true;
						}
						if(foundCsaleGroupRow == false && rowInput.groupId == 'conSale') {
							foundCsaleGroupRow = true;
						}
						if(foundConSaleCinema == false && rowInput.id == 'conSaleCinema') {
							foundConSaleCinema = true;
						}
						if(foundRegisterCinema==false && rowInput.id == 'registerCinema') {
							foundRegisterCinema = true;
						}
						if(foundRegisterDate == false && rowInput.id == 'registerDate') {
							foundRegisterDate = true;
						}
					}
					
					if(foundCinemaRow == false) {
						$.msgBox('error', '条件效验失败', '没有出现影城相关条件！请至少使用下面四种条件中的一种：<br/>"注册影城","管理影城","观影影城","卖品交易影城"');
						return false;
					}
					else if(foundTsaleGroupRow == true && foundWatchTradeCinema == false) {
						$.msgBox('error', '条件效验失败', '请加入“观影影城”条件！<br/>如果要对票房交易进行条件筛选，就必须存在“观影影城”条件');
						return false;
					}
					else if(foundCsaleGroupRow == true && foundConSaleCinema == false) {
						$.msgBox('error', '条件效验失败', '请加入“卖品交易影城”条件！<br/>如果要对卖品交易进行条件筛选，就必须存在“卖品交易影城”条件');
						return false;
					} else if (foundTsaleGroupRow == false && foundCsaleGroupRow == false) { // 如果没有选择影票和卖品
						var error = false;
						var msg = '';
						
						if(foundRegisterCinema == false) {
							// 添加注册影城
							var node = autoAddScheme.inputsTreeObj.getNodeByParam("inputId",'registerCinema', null);
							autoAddScheme.inputsTreeObj.selectNode(node);
							autoAddScheme.addBtn.trigger('click');
							msg = '注册影城';
							error = true;
						}
						
						if(foundRegisterDate == false) {
							// 添加入会时间
							var node = autoAddScheme.inputsTreeObj.getNodeByParam("inputId",'registerDate', null);
							autoAddScheme.inputsTreeObj.selectNode(node);
							autoAddScheme.addBtn.trigger('click');
							msg = msg+(msg != '' ? '和':'')+'入会时间';
							error = true;
						}
						
						if(error) {
							$.msgBox('error', '条件效验失败', '如果要对会员基本信息进行条件筛选，请加入“'+msg+'”条件！');
							return false;
						}
					} else {
						return true;
					}
					
					
					//[
					//  {
				    //		'operator':op, 
				    //		'value':value, 
				    //		'rowId': rowId, 
				    //		'input': {
					//			id : "birthday",
					//          groupId: "base", 
					//          groupLabel: "会员基本信息", 
					//			label : "出生日期",
					//			desc : "出生日期",
					//			type : "date",
					//			allowedOperators : "between,eq,lt,gt,le,ge",
					//			repeatable : false,
					//			validate : {type:'date'}
					//		}
					//	}, 
					//  {
					//  ...
					//  }, 
					//  ...
					//  ...
					//]
				}
				,/*defaultScheme : [ // 默认查询条件
				   				{
									inputId : "nowMemberLevel",
									label : "当前级别",
									type : "multiselect",
									param : "sourceId:dimdef;typeId:1025;filter:false;",
									allowedOperators : "in",
									repeatable : false
								}, ],*/
				inputsGroups : [ // 输入类型定义
				{
					id : "memberLevel",
					label : "会员基本信息",
					desc : "会员基本信息",
					inputs : [ {
						id : "nowMemberLevel",
						label : "当前级别",
						desc : "当前级别",
						type : "multiselect",
						param : "sourceId:dimdef;typeId:1025",
						allowedOperators : "in",
						repeatable : false,
						required : false
					},
					{
						id : "registerCinema",
						label : "注册影城",
						desc : "会员注册是对应的影城；如果是网上注册则为选择区域的主店影城。",
						type : "composite",
						param : "compositeId:cinema",
						allowedOperators : "in",
						repeatable : false
					},{
						id : "manageCinema",
						label : "管理影城",
						desc : "会员常去影城",
						type : "composite",
						param : "compositeId:cinema",
						allowedOperators : "in",
						repeatable : false
					}
					, {
						id : "birthday",
						label : "出生日期",
						desc : "出生日期",
						type : "date",
						allowedOperators : "between,eq,le,ge",
						repeatable : false,
						validate : {type:'date'}
					}
					, {
						id : "registerDate",
						label : "入会时间",
						desc : "入会时间",
						type : "datetime",
						allowedOperators : "between",
						repeatable : false,
						required : false,
						validate : {type:'datetime'}
					}, {
						id : "gender",
						label : "性别",
						desc : "性别",
						type : "multiselect",
						param : "sourceId:dimdef;typeId:2008;filter:false",
						defaultValue : [ "F", "M" ],
						allowedOperators : "in",
						repeatable : false,
						required : false
					}, {
						id : "registerType",
						label : "注册方式",
						desc : "注册方式",
						type : "multiselect",
						param : "sourceId:dimdef;typeId:201;filter:false",
						allowedOperators : "in",
						repeatable : false,
						required : false
					}
					]
				}, {
					id : "tsale",
					label : "票房交易",
					desc : "票房交易",
					inputs : [ {
						id : "transFilm",
						label : "观影影片",
						desc : "观影影片",
						type : "composite",
						param : "compositeId:film",
						allowedOperators : "in",
						repeatable : false,
						required : false
					}, {
						id : "watchTradeCinema",
						label : "观影影城",
						desc : "观影影城",
						type : "composite",
						param : "compositeId:cinema",
						allowedOperators : "in",
						repeatable : false,
						required : false
					}, {
						id : "watchTradeTime",
						label : "观影时段",
						desc : "观影时段",
						type : "multiselect",
						param : "sourceId:dimdef;typeId:2007",
						allowedOperators : "in",
						repeatable : false,
						required : false
					}, {
						id : "watchTradeHour",
						label : "观影小时",
						desc : "观影小时",
						type : "select",
						param : "sourceId:dimdef;typeId:2003",
						allowedOperators : "between,eq",
						defaultValue : [ "01", "24" ],
						repeatable : false,
						required : false,
						validate : {type:'number'}
					}, {
						id : "transWeeks",
						label : "交易星期",
						desc : "交易星期",
						type : "multiselect",
						param : "sourceId:dimdef;typeId:1020",
						allowedOperators : "in",
						repeatable : false,
						required : false
					}, {
						id : "holidayWatchTrade",
						label : "节假日观影",
						desc : "节假日观影",
						type : "select",
						param : "sourceId:dimdef;typeId:2006;filter:false",
						allowedOperators : "eq",
						repeatable : false,
						required : false,
						validate : {type:'integer'}
					}, 
					/*{
						id : "watchTradeDay",
						label : "观影日期",
						desc : "观影日期",
						type : "date",
						allowedOperators : "between,eq",
						repeatable : false,
						validate : {type:'date'}
					}, */
					{
						id : "birthdayFilm",
						label : "生日当天观影",
						desc : "生日当天观影",
						type : "select",
						param : "sourceId:dimdef;typeId:2009;filter:false",
						allowedOperators : "eq",
						repeatable : false,
						required : false,
						validate : {type:'integer'}
						
					}, {
						id : "watchPayMethod",
						label : "观影支付方式",
						desc : "观影支付方式",
						type : "multiselect",
						param : "sourceId:paymethod;typeId:1",
						allowedOperators : "in",
						repeatable : false,
						required : false
					} ]
				}, {
					id : "conSale",
					label : "卖品交易",
					desc : "卖品交易",
					inputs : [ 
					/*{
						id : "conSaleDate",
						label : "卖品交易日期",
						desc : "卖品交易日期",
						type : "date",
						allowedOperators : "between,eq",
						repeatable : false,
						validate : {type:'date'}
					}, */
					{
						id : "conSaleHoliday",
						label : "节假日卖品交易",
						desc : "节假日卖品交易",
						type : "select",
						param : "sourceId:dimdef;typeId:2006;filter:false",
						allowedOperators : "eq",
						repeatable : false,
						required : false,
						validate : {type:'integer'}
					}, {
						id : "conSaleHourPeriod",
						label : "卖品交易时段",
						desc : "卖品交易时段",
						type : "multiselect",
						param : "sourceId:dimdef;typeId:2007",
						allowedOperators : "in",
						repeatable : false,
						required : false
					}, {
						id : "conSaleHour",
						label : "卖品交易小时",
						desc : "卖品交易小时",
						type : "select",
						param : "sourceId:dimdef;typeId:2003",
						allowedOperators : "between,eq",
						defaultValue : [ "1", "23" ],
						repeatable : false,
						required : false,
						validate : {type:'number'}
					}, {
						id : "conSaleCinema",
						label : "卖品交易影城",
						desc : "卖品交易影城",
						type : "composite",
						param : "compositeId:cinema", //return is INNER_CODE
						allowedOperators : "in",
						repeatable : false,
						required : false
					}, {
						id : "conSaleAmount",
						label : "卖品品项金额",
						desc : "卖品品项金额",
						type : "text",
						allowedOperators : "between,eq,lt,gt,le,ge",
						repeatable : false,
						required : false,
						validate : {type:'number'}
					}, {
						id : "conSaleItem",
						label : "卖品品项",
						desc : "卖品品项",
						type : "composite",
						param : "compositeId:conitem",
						allowedOperators : "in",
						repeatable : false,
						required : false
					}, {
						id : "conSaleCategory",
						label : "卖品品类",
						desc : "卖品品类",
						type : "tree",
						param : "treeId:concategory",
						allowedOperators : "in",
						repeatable : false,
						required : false
					}, {
						id : "conSalePayMethod",
						label : "卖品支付方式",
						desc : "卖品支付方式",
						type : "multiselect",
						param : "sourceId:paymethod;typeId:1",
						allowedOperators : "in",
						repeatable : false,
						required : false
					} 
					]
				} ]
			}
		}
	});
})(jQuery);
