(function($){
		
	$.wrender.addComposite({
		id: "cinema",
		title: "影城查询",
		switchLabel: "指定影城",
		popupLabel: "选择影城",
		queryUrl:  $.wrender.context + '/segment/CompositeQueryAction/queryCinemas.do',
		selValue: "INNER_CODE", //选择返回值的列名
		selLabel: "INNER_NAME", //选择返回显示名称的列名
		sortName: 'INNER_NAME',
		sortOrder: "asc",
		width: 780,
		height: 550,
		inputs: [
			{id:"innerName", label:"影城内部名称", type:"text", allowedOperators:"eq,like"},
		    {id:"area", label:"区域", type:"multiselect", param:"sourceId:dimdef;typeId:104;filter:true;", allowedOperators:"in"},
		    {id:"cityLevel", label:"城市级别", type:"multiselect", param:"sourceId:dimdef;typeId:105;filter:false;", allowedOperators:"in"},
		    {id:"cityName", label:"城市名称", type:"text", allowedOperators:"eq,like", validate: null}
	    ],
	    columns: [ //查询结果列
	        {name:'INNER_CODE', label:'影城内码', hidden:true},
	        {name:'INNER_NAME', label:'影城内部名称', width:120, sortable:true},
	        {name:'OUT_NAME', label:'影城外部名', width:120, sortable:false},
	        {name:'AREA', label:'区域', width:60, sortable:false},
			{name:'CITY_LEVEL', label:'城市级别', width:50, sortable:false},
			{name:'CITY_NAME', label:'城市名称', width:90, sortable:false}
		]
	});
	
	$.wrender.addComposite({
		id: "segment",
		title: "选择普通客群",
		hideSwitchLabel: true,
		hideSpan: true,
		clearCache: true,
		switchLabel: "选择普通客群",
		popupLabel: "选择普通客群",
		queryUrl:  $.wrender.context + '/segment/CompositeQueryAction/querySegment.do',
		selValue: "SEGMENT_ID", //选择返回值的列名
		selLabel: "NAME", //选择返回显示名称的列名
		sortName: 'UPDATE_DATE',
		sortOrder: "desc",
		width: 760,
		height: 520,
		inputs: [
			{id:"code", label:"编码", type:"text", allowedOperators:"eq,like"},
		    {id:"segmentName", label:"客群名称", type:"text", allowedOperators:"like"},
		    {id:"createBy", label:"创建人", type:"text", allowedOperators:"eq,like"},
		    {id:"updateTime", label:"更新时间", type:"datetime", allowedOperators:"between", validate : { type : 'datetime' }}
	    ],
	    columns: [ //查询结果列
	        {name:'SEGMENT_ID', label:'客群ID', hidden:true},
	        {name:'CODE', label:'编码', width:140, sortable:false},
	        {name:'NAME', label:'客群名称', width:125, sortable:false},
	        {name:'CAL_COUNT', label:'计算数量', width:120, sortable:false},
			{name:'CREATE_BY', label:'创建人', width:90, sortable:false},
			{name:'UPDATE_BY', label:'更新人', width:90, sortable:false},
			{name:'UPDATE_DATE', label:'更新时间', width:90, sortable:true}
		]
	});
	
	$.wrender.addComposite({
		id: "segmentCampaign",
		title: "客群查询",
		singleSelect: true,
		hideSwitchLabel: true,
		hideSpan: true,
		switchLabel: "选择客群",
		popupLabel: "选择客群",
		queryUrl:  $.wrender.context + '/segment/CompositeQueryAction/querySegment.do?segmentType=0',
		selValue: "SEGMENT_ID", //选择返回值的列名
		selLabel: "NAME", //选择返回显示名称的列名
		sortName: 'UPDATE_DATE',
		sortOrder: "desc",
		width: 760,
		height: 520,
		inputs: [
			{id:"code", label:"编码", type:"text", allowedOperators:"eq,like"},
		    {id:"segmentName", label:"客群名称", type:"text", allowedOperators:"like"}
	    ],
	    columns: [ //查询结果列
	        {name:'SEGMENT_ID', label:'客群ID', hidden:true},
	        {name:'CODE', label:'编码', width:140, sortable:false},
	        {name:'NAME', label:'客群名称', width:125, sortable:false},
	        {name:'CAL_COUNT', label:'计算数量', width:120, sortable:false},
	        {name:'CONTROL_COUNT', label:'对比组数量', width:80, sortable:false}
		]
	});
	
	$.wrender.addComposite({
		id: "activity",
		title: "活动查询",
		switchLabel: "指定活动",
		popupLabel: "选择活动",
		queryUrl:  $.wrender.context + '/segment/CompositeQueryAction/queryActivitys.do',
		selValue: "INNER_CODE", //选择返回值的列名
		selLabel: "INNER_NAME", //选择返回显示名称的列名
		sortName: 'INNER_NAME',
		sortOrder: "asc",
		width: 760,
		height: 520,
		inputs: [
			{id:"cmnName", label:"波次名称", type:"text", allowedOperators:"eq,like"},
		    {id:"cmnStartDate", label:"波次开始时间", type:"date", allowedOperators:"between", validate : { type : 'date' }},
		    {id:"cmnEndDate", label:"波次结束时间", type:"date", allowedOperators:"between", validate : { type : 'date' }},
		    {id:"sengentName", label:"目标客群名称", type:"text", allowedOperators:"eq,like", validate: null},
		    {id:"phaseName", label:"阶段名称", type:"text", allowedOperators:"eq,like", validate: null},
		    {id:"campaignName", label:"活动名称", type:"text", allowedOperators:"eq,like", validate: null}
	    ],
	    columns: [ //查询结果列
	        {name:'INNER_CODE', label:'波次编码', hidden:true},
	        {name:'INNER_NAME', label:'波次名称', width:120, sortable:true},
	        {name:'START_DTIME', label:'波次开始时间', width:120, sortable:false},
	        {name:'END_DTIME', label:'波次结束时间', width:120, sortable:false},
			{name:'SENGENT_NAME', label:'目标客群名称', width:90, sortable:false},
			{name:'PHASE_NAME', label:'阶段名称', width:90, sortable:false},
			{name:'CAMPAIGN_NAME', label:'活动名称', width:90, sortable:false}
		]
	});
	
	$.wrender.addComposite({
		id: "film",
		title: "影片查询",
		switchLabel: "指定影片",
		popupLabel: "选择影片",
		queryUrl:  $.wrender.context + '/segment/CompositeQueryAction/queryFilms.do',
		selValue: "FILM_CODE", //选择返回值的列名
		selLabel: "FILM_NAME", //选择返回显示名称的列名
		sortName: 'FILM_NAME',
		sortOrder: "asc",
		width: 810,
		height: 600,
		inputs: [
			{id:"filmName", label:"影片名称", type:"text", allowedOperators:"eq,like"},
		    {id:"showSet", label:"放映制式", type:"multiselect", param:"sourceId:dimdef;typeId:134;filter:false;", allowedOperators:"in"},
		    {id:"filmTypes", label:"影片类型", type:"multiselect", param:"sourceId:dimdef;typeId:131;filter:false;", allowedOperators:"in"},
		    {id:"filmCate", label:"类别", type:"multiselect", param:"sourceId:dimdef;typeId:130;filter:false;", allowedOperators:"in"},
		    {id:"country", label:"国家", type:"multiselect", param:"sourceId:dimdef;typeId:148;filter:false;", allowedOperators:"in"},
		    {id:"director", label:"导演", type:"text", allowedOperators:"like,eq", validate: null},
		    {id:"mainActors", label:"演员", type:"text", allowedOperators:"like,eq", validate: null}
	    ], 
	    columns: [ 
	        {name:'FILM_CODE', label:'影片编码', hidden:true},
	        {name:'FILM_NAME', label:'影片名称', width:170, sortable:true},
	        {name:'SHOW_SET', label:'放映制式', width:90, sortable:false},
	        {name:'FILM_CATE', label:'类别', width:40, sortable:false},
	        {name:'FILM_TYPES', label:'影片类型', width:60, sortable:false}, //T_FT_TYPE
	        {name:'COUNTRY', label:'国家', width:40, sortable:false},
			{name:'DIRECTOR', label:'导演', width:80, sortable:false},
			{name:'MAIN_ACTORS', label:'演员', width:120, sortable:false},
			{name:'PREMIERE_DATE', label:'首映日期', width:90, sortable:true, formatter: function(cellvalue, options, rowObject) {return cellvalue.substring(0,10);} },
			{name:'END_DATE', label:'落幕日期', width:90, sortable:false, formatter: function(cellvalue, options, rowObject) {return cellvalue.substring(0,10);} }
		]
	});
	
	$.wrender.addComposite({
		id: "conitem",
		title: "卖品品项查询",
		switchLabel: "指定品项",
		popupLabel: "选择品项",
		queryUrl:  $.wrender.context + '/segment/CompositeQueryAction/queryConItems.do',
		selValue: "ITEM_CODE", //选择返回值的列名
		selLabel: "SHORT_NAME", //选择返回显示名称的列名
		sortName: 'ITEM_CODE',
		sortOrder: "asc",
		width: 710,
		height: 570,
		inputs: [
			{id:"itemCode", label:"编码", type:"text", allowedOperators:"eq"},
			{id:"itemName", label:"名称", type:"text", allowedOperators:"like,eq"},
		    {id:"conCategoryId", label:"品类", type:"tree", param:"treeId:concategory;", allowedOperators:"in"},
		    {id:"unit", label:"计量单位", type:"multiselect", param:"sourceId:dimdef;typeId:179;filter:true;", allowedOperators:"in"}
	    ],
	    columns: [
	        {name:'ITEM_CODE', label:'编码', width:90, sortable:true},
	        {name:'ITEM_NAME', label:'名称', width:90, sortable:true},
	        {name:'SHORT_NAME', label:'简称', width:90, sortable:false},
	        {name:'ITEM_TYPE', label:'类型', width:40, sortable:false},
	        {name:'UNIT', label:'计量单位', width:40, sortable:false},
	        {name:'CATEGORY_BREADCRUMBS', label:'物料分类', width:90, sortable:false}
		]
	});
	
	$.wrender.addComposite({
		id: "authuser",
		title: "用户查询",
		selTarget: true,
		hideSwitchLabel: true,
		maxSelections: 10,
		//switchLabel: "指定用户",
		popupLabel: "选择用户",
		queryUrl:  $.wrender.context + '/segment/CompositeQueryAction/queryAuthUsers.do',
		selValue: "LOGIN_ID", //选择返回值的列名
		selLabel: "LOGIN_ID", //选择返回显示名称的列名
		sortName: 'UPDATE_DATE',
		sortOrder: "desc",
		width: 780,
		height: 500,
		inputs: [
			//{id:"loginId", label:"用户账号", type:"text", allowedOperators:"eq"},
			{id:"userName", label:"用户名", type:"text", allowedOperators:"like,eq"},
			{id:"cmnStartDate", label:"波次开始时间", type:"date", allowedOperators:"between", validate : { type : 'date' }}
		], 
	    columns: [
	        {name:'LOGIN_ID', label:'用户账号', width:80, sortable:true},
	        {name:'USER_NAME', label:'用户名', width:80, sortable:true},
	        {name:'USER_EMAIL', label:'电子邮件', width:80, sortable:false},
	        {name:'USER_LEVEL', label:'级别', width:25, sortable:false},
	        {name:'REGION_NAME', label:'所属区域', width:80, sortable:false},
	        {name:'CINEMA_NAME', label:'所属影城', width:80, sortable:false},
	        {name:'UPDATE_DATE', label:'更新时间', width:100, sortable:false}
		]
	});
	
	$.wrender.addTree({
		id: 'concategory', 
		title: '选择卖品品项分类',
		popupLabel: '选择',
		url:  $.wrender.context + '/segment/ConCategoryAction/getAllCategories.do',
		width: 300,
		height: 500,
		leafValueOnly: true //表示只有叶子节点可以被选中作为值保存，其实上级 节点即使被选中不作为值保存
	});
	
})(jQuery);
