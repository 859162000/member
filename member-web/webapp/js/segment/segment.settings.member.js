(function($){
	//定义$.segment.setting.member{}对象，用于会员类客群配置 
	$.segment = $.segment || {};
	
	$.extend($.segment, {
		settings: {
			member: {
				version: '1.0',
				defaultCriterias: [ //默认查询条件
					{inputId:"noDisturb", label:"是否希望被联络", operator:"e", relation:"and", value:"01"},
					{inputId:"manageCinema",label:"管理影城", operator:"in", relation:"and", children:[
					    {inputId:"cinemaArea", label:"区域", operator:"e", value:"01", valueDisplay:"北京"},
					    {inputId:"cinemaCityLevel", label:"城市级别", operator:"e", value:"01", valueDisplay:"三级"},
					    {inputId:"cinemaCity", label:"城市名称", operator:"like", value:"北京"},
					    {inputId:"cinemaName", label:"影院简称", operator:"like", value:"北京"}
					]},
					{inputId:"registerCinema",label:"注册影城", operator:"in", relation:"and", children:[
			            {inputId:"cinemaCode", label:"影城编码", operator:"e", value:"01", valueDisplay:"北京CBD"},
			            {inputId:"cinemaCode", label:"影城编码", operator:"e", value:"01", valueDisplay:"北京石景山"},
			            {inputId:"cinemaCode", label:"影城编码", operator:"e", value:"01", valueDisplay:"北京天通苑"},
			            {inputId:"cinemaCode", label:"影城编码", operator:"e", value:"01", valueDisplay:"廊坊"}
					]},
					{inputId:"conAmount", groupId:"sale", label:"卖品消费金额", operator:"=", value:"30,40"}
		        ],

				inputsGroups: [ //输入类型定义
					{id: "base", label:"会员基本", desc:"....", inputs: [
			 			{id:"noDisturb", label:"是否希望被联络", desc:"....", type:"DIM_SEL",allowedOperators:"e", repeatable: false, required: true},
			 			{id:"manageCinema", label:"管理影城", desc:"....", type:"COMPOSITE", compositeId: "cinema", allowedOperators:"in", repeatable: false, required: true},
			 			{id:"registerCinema", label:"注册影城", desc:"会员注册是对应的影城；如果是网上注册则为选择区域的主店影城。", type:"COMPOSITE", compositeId: "cinema", allowedOperators:"in", repeatable: false},
			 			{id:"birthday", label:"出生日期", desc:"....", type:"DATE_RANGE",allowedOperators:"range,e,lt,gt,lte,gte", repeatable: false, validate: null},
			 			{id:"birthmonth", label:"出生月", desc:"....", type:"DIM_SEL", dimTypeId:"179", allowedOperators:"range,e,lt,gt,lte,gte", repeatable: false, validate: null},
			 			{id:"registerDtime", label:"入会时间", desc:"....", type:"DTIME_RANGE", dimTypeId:"179", allowedOperators:"range", repeatable: false, validate: null},
			 			{id:"gender", label:"性别", desc:"....", type:"DIM_MULTI", dimTypeId:"179", defaultValue:"30", allowedOperators:"in", repeatable: false, validate: null}
			 		]},
			 		{id: "sale", label:"会员交易", desc:".....", inputs: [
			 		    {id:"conItem", label:"品项", desc:"....", type:"COMPOSITE", dimTypeId:"179", allowedOperators:"in", repeatable: false, validate: null},
			 		    {id:"conCategory", label:"品类", desc:"....", type:"TREE_MULTI", treeLink:"", allowedOperators:"in", repeatable: false, validate: null},
			 		    {id:"conAmount", label:"卖品消费金额", desc:"....", type:"TEXT_RANGE", dimTypeId:"179", allowedOperators:"range", repeatable: false, validate: null}
			 		]}
			 	]
			}
		}
 	});

})(jQuery);




