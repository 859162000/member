(function($){
	
	$.segment = $.segment || {};
	
	$.extend($.segment, {
		composites: [
 			{
 				id: "cinema", 
 				lable: "影院查询",
 				specifyTarget: "指定影院",
 				sortName: 'CODE',
 				sortOrder: "desc",
 				inputs: [
 				    {id:"cinemaArea", label:"区域", desc:"", type:"DIM_MULTI", dimTypeId:"179", allowedOperators:"in", repeatable: false, validate: null},
 				    {id:"cinemaCityLevel", label:"城市级别", desc:"", type:"DIM_MULTI", dimTypeId:"179", allowedOperators:"in", repeatable: false, validate: null},
 				    {id:"cinemaCity", label:"城市名称", desc:"", type:"TEXT", allowedOperators:"like", repeatable: false, validate: null},
 				    {id:"cinemaName", label:"影院简称", desc:"", type:"TEXT", allowedOperators:"like", repeatable: false, validate: null}
 			    ], 
 				results: [ //查询结果列
 		   			{name:"CODE", label:"编码", width:100, sortable:true, isKey: true},
 		   			{name:"SHORT_NAME", label:"简称", width:100, sortable:true, isKeyName: true},
 		   			{name:"OUT_NAME", label:"外部名称", width:100, sortable:true},
 		   			{name:"AREA", label:"区域", width:100, sortable:true},
 		   			{name:"CITY", label:"城市", width:100, sortable:true},
 		   			{name:"CITY_LEVEL", label:"城市级别", width:100, sortable:true},
 		   			{name:"HALL_COUNT", label:"厅数", width:100, sortable:true},
 		   			{name:"CINEMA_TYPE", label:"类别", width:100, sortable:true}
 				]
 			},
 			
 			{
 				id: "conItem",
 				lable: "卖品品项",
 				specifyTarget: "指定品项",
 				sortName: 'CODE',
 				sortOrder: "desc",
 				inputs: [
 				    {id:"itemCode", label:"编码", desc:"", type:"TEXT", allowedOperators:"in", repeatable: false, validate: null},
 				    {id:"shortName", label:"简称", desc:"", type:"TEXT", allowedOperators:"in", repeatable: false, validate: null},
 				    {id:"itemName", label:"名称", desc:"", type:"TEXT", allowedOperators:"like", repeatable: false, validate: null},
 				    {id:"itemType", label:"类型", desc:"", type:"DIM_MULTI", dimTypeId:"179", allowedOperators:"like", repeatable: false, validate: null}
 			    ], 
 				results: [ //查询结果列
 		   			{name:"CODE", label:"编码", width:100, sortable:true, isKey: true},
 		   			{name:"SHORT_NAME", label:"简称", width:100, sortable:true, isKeyName: true},
 		   			{name:"ITEM_NAME", label:"名称", width:100, sortable:true},
 		   			{name:"ITEM_TYPE", label:"类型", width:100, sortable:true},
 		   			{name:"CATEGORY_BREADCRUMBS", label:"分类", width:100, sortable:true}
 				]
 			}
 			
 		]
 	});

})(jQuery);
