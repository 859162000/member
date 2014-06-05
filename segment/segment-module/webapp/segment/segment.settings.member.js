(function($) {
	// 定义$.segment.setting.member{}对象，用于会员类客群配置
	$.segment = $.segment || {};

	$.extend($.segment, {
		settings : {
			member : {
				version : '1.0',
				validate : function(schemeData) {
					// [
					// {
					// 'inputId':inputId,
					// 'label':inputLabel,
					// 'operator':op,
					// 'value':value,
					// 'rowId': rowId,
					// 'input': {
					// id : "birthday",
					// groupId: "base",
					// groupLabel: "会员基本信息",
					// label : "出生日期",
					// desc : "出生日期",
					// type : "date",
					// allowedOperators : "between,eq,lt,gt,le,ge",
					// repeatable : false,
					// validate : {type:'date'}
					// }
					// }
					// ]

					return true;
				},
				defaultScheme : [ // 默认查询条件
				{
					inputId : "noDisturb",
					label : "是否希望被联络",
					type : "multiselect",
					param : "sourceId:dimdef;typeId:260;filter:false;",
					allowedOperators : "in",
					defaultValue : [ "1", "2" ],
					repeatable : false
				}, ],
				inputsGroups : [ // 输入类型定义
				{
					id : "base",
					label : "会员基本信息",
					desc : "会员基本信息",
					inputs : [ {
						id : "noDisturb",
						label : "是否希望被联络",
						desc : "是否希望被联络",
						type : "multiselect",
						param : "sourceId:dimdef;typeId:260;filter:false;",
						allowedOperators : "in",
						defaultValue : [ "1", "2", "3" ],
						repeatable : false,
						required : true
					}, {
						id : "registerCinema",
						label : "注册影城",
						desc : "会员注册是对应的影城；如果是网上注册则为选择区域的主店影城。",
						type : "composite",
						param : "compositeId:cinema",
						allowedOperators : "in",
						repeatable : false
					}, {
						id : "manageCinema",
						label : "管理影城",
						desc : "会员常去影城",
						type : "composite",
						param : "compositeId:cinema",
						allowedOperators : "in",
						repeatable : false
					}, {
						id : "birthday",
						label : "出生日期",
						desc : "出生日期",
						type : "date",
						allowedOperators : "between,eq,le,ge",
						repeatable : false,
						defaultValue : [ "1980-01-01", "1990-01-01" ],
						validate : {
							type : 'date'
						}
					}
					/*
					 * , { id : "birthmonth", label : "出生月", desc : "出生月", type :
					 * "select", param : "sourceId:dimdef;typeId:2001",
					 * allowedOperators : "between,eq", defaultValue : [ "1",
					 * "12" ], repeatable : false, required : false, validate :
					 * {type:'integer'} }
					 */
					, {
						id : "registerDate",
						label : "入会时间",
						desc : "入会时间",
						type : "datetime",
						allowedOperators : "between",
						repeatable : false,
						required : false,
						validate : {
							type : 'datetime'
						}
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
						id : "registerChannel",
						label : "招募渠道",
						desc : "招募渠道",
						type : "multiselect",
						param : "sourceId:dimdef;typeId:216",
						defaultValue : [ "1", "2", "3", "4", "5" ],
						allowedOperators : "in",
						repeatable : false,
						required : false
					}, /*
						 * { id : "isChangeCard", label : "是否储值卡转换", desc :
						 * "是否储值卡转换", type : "select", param :
						 * "sourceId:dimdef;typeId:139", allowedOperators :
						 * "eq", repeatable : false, required : false }, { id :
						 * "isUseChannel", label : "是否使用电子渠道", desc :
						 * "是否使用电子渠道", type : "select", param :
						 * "sourceId:dimdef;typeId:2002", allowedOperators :
						 * "eq", repeatable : false, required : false }, { id :
						 * "facFilmType", label : "喜欢的电影类型", desc : "喜欢的电影类型",
						 * type : "multiselect", param :
						 * "sourceId:dimdef;typeId:131;filter:true",
						 * allowedOperators : "in", repeatable : false, required :
						 * false }, { id : "facConType", label : "喜欢的联系方式", desc :
						 * "喜欢的联系方式", type : "multiselect", param :
						 * "sourceId:dimdef;typeId:211", allowedOperators :
						 * "in", repeatable : false, required : false },
						 */
					{
						id : "education",
						label : "教育程度",
						desc : "教育程度",
						type : "multiselect",
						param : "sourceId:dimdef;typeId:202",
						allowedOperators : "in",
						repeatable : false,
						required : false
					}, {
						id : "memberJob",
						label : "会员个人职业",
						desc : "会员个人职业",
						type : "multiselect",
						param : "sourceId:dimdef;typeId:203",
						allowedOperators : "in",
						repeatable : false,
						required : false
					}, {
						id : "familyIncome",
						label : "家庭收入",
						desc : "家庭收入",
						type : "multiselect",
						param : "sourceId:dimdef;typeId:205",
						allowedOperators : "in",
						repeatable : false,
						required : false
					}, {
						id : "marry",
						label : "婚姻状况",
						desc : "婚姻状况",
						type : "multiselect",
						param : "sourceId:dimdef;typeId:206",
						allowedOperators : "in",
						repeatable : false,
						required : false
					}, {
						id : "childrenNum",
						label : "小孩个数",
						desc : "小孩个数",
						type : "multiselect",
						param : "sourceId:dimdef;typeId:207",
						allowedOperators : "in",
						repeatable : false,
						required : false
					},
					/*
					 * { id : "distanceFromHome", label : "常驻地到万达影城的距离", desc :
					 * "常驻地到万达影城的距离", type : "multiselect", param :
					 * "sourceId:dimdef;typeId:208;filter:true",
					 * allowedOperators : "in", repeatable : false, required :
					 * false }, { id : "timeFromHome", label : "常驻地到万达影城的时间",
					 * desc : "常驻地到万达影城的时间", type : "multiselect", param :
					 * "sourceId:dimdef;typeId:209;filter:true",
					 * allowedOperators : "in", repeatable : false, required :
					 * false }, { id : "mobileNews", label : "手机报订阅标识", desc :
					 * "手机报订阅标识", type : "multiselect", param :
					 * "sourceId:dimdef;typeId:210;filter:true",
					 * allowedOperators : "in", repeatable : false, required :
					 * false }, { id : "lastTransTime", label : "会员最近一次消费时间",
					 * desc : "会员最近一次消费时间", type : "datetime", allowedOperators :
					 * "between", repeatable : false, required : false }, { id :
					 * "lastFilmTime", label : "会员最近一次观影时间", desc :
					 * "会员最近一次观影时间", type : "datetime", allowedOperators :
					 * "between", repeatable : false, required : false }, { id :
					 * "payMethedType", label : "会员最近一次支付方式", desc :
					 * "会员最近一次支付方式", type : "select", param :
					 * "sourceId:paymethod;typeId:pay", allowedOperators : "eq",
					 * repeatable : false, required : false },
					 */
					{
						id : "recruitType",
						label : "会员注册方式",
						desc : "会员注册方式",
						type : "multiselect",
						param : "sourceId:dimdef;typeId:201;filter:false",
						allowedOperators : "in",
						repeatable : false,
						required : false
					}/*, {
						id : "lastTicketShowTime",
						label : "最近一次观影时间",
						desc : "最近一次观影时间",
						type : "datetime",
						allowedOperators : "between",
						repeatable : false,
						required : false,
						validate : { type:'datetime' }
					}, {
						id : "lastConRransTime",
						label : "最近一次卖品交易时间",
						desc : "最近一次卖品交易时间",
						type : "datetime",
						allowedOperators : "between",
						repeatable : false,
						required : false,
						validate : { type:'datetime' }
					}*/ ]
				},
				{
					id : "memberCount",
					label : "会员统计属性",
					desc : "会员统计属性",
					inputs : [
						{
							id : "priceConscious",
							label : "价格敏感",
							desc : "价格敏感",
							type : "select",
							allowedOperators : "eq",
							defaultValue : [1],
							param : "sourceId:dimdef;typeId:136;filter:false",
							repeatable : false,
							required : false
						},
						{
							id : "familyNumber",
							label : "家庭构成",
							desc : "家庭构成",
							type : "multiselect",
							allowedOperators : "in",
							param : "sourceId:dimdef;typeId:1018;filter:false",
							repeatable : false,
							required : false
						},
						{
							id : "tradeAbnormal",
							label : "异常消费",
							desc : "异常消费",
							type : "select",
							allowedOperators : "eq",
							defaultValue : [1],
							param : "sourceId:dimdef;typeId:136;filter:false;",
							repeatable : false,
							required : false
						},
						{
							id : "activityLevel",
							label : "会员活跃度",
							desc : "会员活跃度",
							type : "multiselect",
							allowedOperators : "in",
							param : "sourceId:dimdef;typeId:1019;filter:false;",
							repeatable : false,
							required : false
						},
						{
							id : "onlineTradeType",
							label : "电子渠道偏好",
							desc : "电子渠道偏好",
							type : "multiselect",
							allowedOperators : "in",
							param : "sourceId:dimdef;typeId:1003;filter:false;",
							repeatable : false,
							required : false
						},
						{
							id : "favFilmType",
							label : "影片偏好",
							desc : "影片偏好",
							type : "multiselect", 
							param : "sourceId:dimdef;typeId:131;filter:false;", 
							allowedOperators : "in",
							repeatable : false,
							required : false
						}
					]
				}
				, {
					id : "memberPoint",
					label : "会员积分",
					desc : "会员积分",
					inputs : [ {
						id : "exchangePoint",
						label : "可兑换积分余额",
						desc : "可兑换积分余额",
						type : "text",
						allowedOperators : "between,eq,lt,gt,le,ge",
						repeatable : false,
						required : false,
						validate : {
							type : 'number'
						}
					},
					 { 
						id : "lastChangeTime", 
						label : "最近一次兑换时间", 
						desc : "最近一次兑换时间",
						type : "datetime",
						allowedOperators : "between", 
						repeatable : false, 
						required : false, 
						validate : {type:'datetime'} 
					 },
					 { 
						 id : "lastPointBalance", 
						 label : "最近一次积分累计时间", 
						 desc : "最近一次积分累计时间", 
						 type : "datetime",
						 allowedOperators : "between", 
						 repeatable : false,
						 required : false, 
						 validate : { type:'datetime' }
					 },
					{
						id : "getPointTime",
						label : "累计积分日期",
						desc : "累计积分日期",
						type : "date",
						allowedOperators : "between,eq",
						repeatable : false,
						required : false,
						validate : {
							type : 'date'
						}
					}, {
						id : "exchangePointCount",
						label : "累计可兑换积分总数",
						desc : "累计可兑换积分总数",
						type : "text",
						allowedOperators : "between,eq,lt,gt,le,ge",
						repeatable : false,
						required : false,
						validate : {
							type : 'integer'
						}
					}, {
						id : "levelPointCount",
						label : "累计定级积分总数",
						desc : "累计定级积分总数",
						type : "text",
						allowedOperators : "between,eq,lt,gt,le,ge",
						repeatable : false,
						required : false,
						validate : {
							type : 'integer'
						}
					}, {
						id : "activityPointCount",
						label : "累计非定级积分总数",
						desc : "累计非定级积分总数",
						type : "text",
						allowedOperators : "between,eq,lt,gt,le,ge",
						repeatable : false,
						required : false,
						validate : {
							type : 'integer'
						}
					}

					]
				}, {
					id : "memberLevel",
					label : "会员级别",
					desc : "会员级别",
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
						id : "nowChangeLevelTime", 
						label : "当前级别变更时间", 
						desc : "当前级别变更时间", 
						type : "datetime", 
						allowedOperators : "between", 
						repeatable : false, 
						required : false,
						validate : {
							type : 'datetime'
						}
					 }, {
						 id : "changeLevelType", 
						 label : "当前级别变更类型", 
						 desc : "当前级别变更类型",
						 type : "select", 
						 param : "sourceId:dimdef;typeId:2015;filter:false", 
						 allowedOperators : "eq",
						 repeatable : false, 
						 required : false
					 },
					 {
						 id : "memberLevelChange", 
						 label : "变更级别", 
						 desc : "变更级别", 
						 type : "multiselect",
						 param : "sourceId:dimdef;typeId:1025;filter:false",
						 allowedOperators : "in", 
						 repeatable : false, 
						 required : false 
					 },
					 {
						id : "upTargetLevel",
						label : "将升级目标级别",
						desc : "将升级目标级别",
						type : "multiselect",
						param : "sourceId:dimdef;typeId:1025",
						allowedOperators : "in",
						repeatable : false,
						required : false
					}, {
						id : "targetLevelGap",
						label : "距离目标级别积分差",
						desc : "距离目标级别积分差",
						type : "text",
						allowedOperators : "between,eq,lt,gt,le,ge",
						repeatable : false,
						required : false,
						validate : {
							type : 'integer'
						}
					}, {
						id : "levelTickCount",
						label : "距离目标级别票数差",
						desc : "距离目标级别票数差",
						type : "text",
						allowedOperators : "between,eq,lt,gt,le,ge",
						repeatable : false,
						required : false,
						validate : {
							type : 'integer'
						}
					}]
				},/*{
					id : "watchFilmCount",
					label : "观影频次",
					desc : "观影频次",
					inputs : [ 
						{
							id : "temp10",
							label : "V观影时段",
							desc : "V观影时段",
							type : "multiselect",
							allowedOperators : "in",
							param : "sourceId:dimdef;typeId:2012;filter:false",
							repeatable : false,
							required : false
						},
						{
							id : "temp11",
							label : "V观影影城",
							desc : "V观影影城",
							type : "composite",
							allowedOperators : "in",
							param : "compositeId:cinema",
							repeatable : false,
							required : false
						},
						{
							id : "temp12",
							label : "V观影消费次数",
							desc : "V观影消费次数",
							type : "text",
							allowedOperators : "between,eq,lt,gt,le,ge",
							repeatable : false,
							required : false,
							validate : {
								type : 'integer'
							}
						},
						{
							id : "temp13",
							label : "V观影购票数",
							desc : "V观影购票数",
							type : "text",
							allowedOperators : "between,eq,lt,gt,le,ge",
							repeatable : false,
							required : false,
							validate : {
								type : 'integer'
							}
						},
						{
							id : "temp14",
							label : "观影消费次数占比(%)",
							desc : "观影消费次数占比(%)",
							type : "text",
							allowedOperators : "between,eq,lt,gt,le,ge",
							repeatable : false,
							required : false,
							validate : {
								type : 'number'
							}
						},
						{
							id : "temp15",
							label : "V观影消费金额",
							desc : "V观影消费金额",
							type : "text",
							allowedOperators : "between,eq,lt,gt,le,ge",
							repeatable : false,
							required : false,
							validate : {
								type : 'number'
							}
						},
						{
							id : "temp16",
							label : "观影消费金额占比(%)",
							desc : "观影消费金额占比(%)",
							type : "text",
							allowedOperators : "between,eq,lt,gt,le,ge",
							repeatable : false,
							required : false,
							validate : {
								type : 'number'
							}
						}
					]
				},*/
				{
					id : "activity",
					label : "活动",
					desc : "活动",
					inputs : [
						{
							id : "cmnActivity",
							label : "参加波次",
							desc : "参加波次",
							type : "composite",
							allowedOperators : "in",
							param : "compositeId:activity",
							repeatable : false,
							required : false
						},
						{
							id : "cmnResponse",
							label : "波次响应类型",
							desc : "波次响应类型",
							type : "multiselect",
							allowedOperators : "in",
							param : "sourceId:dimdef;typeId:2013;filter:false",
							repeatable : false,
							required : false
						},
						{
							id : "responseCount",
							label : "响应次数",
							desc : "响应次数",
							type : "text",
							allowedOperators : "between,eq,lt,gt,le,ge",
							repeatable : false,
							required : false,
							validate : {
								type : 'integer'
							}
						}
					 ]
				},
				{
					id : "transSalesCount",
					label : "总交易信息",
					desc : "总交易信息",
					inputs : [ {
						id : "transNumber",
						label : "消费次数",
						desc : "消费次数",
						type : "text",
						allowedOperators : "between,eq,lt,gt,le,ge",
						repeatable : false,
						validate : {
							type : 'integer'
						}
					}, {
						id : "transMoney",
						label : "消费金额",
						desc : "消费金额",
						type : "text",
						allowedOperators : "between,eq,lt,gt,le,ge",
						repeatable : false,
						validate : {
							type : 'number'
						}
					}, {
						id : "transCinema",
						label : "交易影城",
						desc : "交易影城",
						type : "composite",
						param : "compositeId:cinema",
						allowedOperators : "in",
						repeatable : false,
						required : false
					}, {
						id : "transTime",
						label : "交易时段",
						desc : "交易时段",
						type : "multiselect",
						param : "sourceId:dimdef;typeId:2007",
						allowedOperators : "in",
						repeatable : false,
						required : false
					}, {
						id : "transHour",
						label : "交易小时",
						desc : "交易小时",
						type : "select",
						param : "sourceId:dimdef;typeId:2003",
						allowedOperators : "between,eq",
						defaultValue : [ "1", "23" ],
						repeatable : false,
						required : false,
						validate : {
							type : 'number'
						}
					},/* {
						id : "holidayTrans",
						label : "节假日交易",
						desc : "节假日交易",
						type : "select",
						param : "sourceId:dimdef;typeId:2006;filter:false",
						allowedOperators : "eq",
						repeatable : false,
						required : false
					},*/ {
						id : "transHoliday",
						label : "交易节日",
						desc : "交易节日",
						type : "multiselect",
						param : "sourceId:dimdef;typeId:2014;filter:false",
						allowedOperators : "in",
						repeatable : false,
						required : false
					},{
						id : "transDay",
						label : "交易日期",
						desc : "交易日期",
						type : "date",
						allowedOperators : "between,eq",
						repeatable : false,
						validate : {
							type : 'date'
						}
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
						id : "transCards",
						label : "曾经持卡消费",
						desc : "曾经持卡消费",
						type : "select",
						param : "sourceId:dimdef;typeId:2006;filter:false",
						allowedOperators : "eq",
						repeatable : false,
						required : false
					}, {
						id : "transElectronic",
						label : "是否电子渠道交易",
						desc : "是否电子渠道交易",
						type : "select",
						param : "sourceId:dimdef;typeId:2006;filter:false",
						allowedOperators : "eq",
						repeatable : false,
						required : false
					}, {
						id : "transPayMethod",
						label : "观影支付方式",
						desc : "观影支付方式",
						type : "multiselect",
						param : "sourceId:paymethod;typeId:1",
						allowedOperators : "in",
						repeatable : false,
						required : false
					} ]
				}, {
					id : "tsale",
					label : "票房交易",
					desc : "票房交易",
					inputs : [ {
						id : "ticketType",
						label : "万达票类分类",
						desc : "万达票类分类",
						type : "multiselect",
						param : "sourceId:dimdef;typeId:169;filter:false",
						defaultValue : [ '01' ],
						allowedOperators : "in",
						repeatable : false,
						required : false
					},

					{
						id : "ticketNumber",
						label : "观影购票数",
						desc : "观影购票数",
						type : "text",
						allowedOperators : "between,eq,lt,gt,le,ge",
						repeatable : false,
						validate : {
							type : 'integer'
						}
					}, {
						id : "transFilm",
						label : "观影影片",
						desc : "观影影片",
						type : "composite",
						param : "compositeId:film",
						allowedOperators : "in",
						repeatable : false,
						required : false
					}, {
						id : "filmPlanType",
						label : "首映场观影",
						desc : "首映场观影",
						type : "select",
						param : "sourceId:dimdef;typeId:139;filter:false", 
						allowedOperators : "eq",
						repeatable : false,
						required : false
					}, {
						id : "purchaseNumber",
						label : "观影消费次数",
						desc : "观影消费次数",
						type : "text",
						allowedOperators : "between,eq,lt,gt,le,ge",
						repeatable : false,
						validate : {
							type : 'integer'
						}
					}, {
						id : "purchaseMoney",
						label : "观影消费金额",
						desc : "观影消费金额",
						type : "text",
						allowedOperators : "between,eq,lt,gt,le,ge",
						repeatable : false,
						validate : {
							type : 'number'
						}
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
						defaultValue : [ "1", "23" ],
						repeatable : false,
						required : false,
						validate : {
							type : 'number'
						}
					}, /*{
						id : "holidayWatchTrade",
						label : "节假日观影",
						desc : "节假日观影",
						type : "select",
						param : "sourceId:dimdef;typeId:2006;filter:false",
						allowedOperators : "eq",
						repeatable : false,
						required : false
					}, */
					{
						id : "watchTradeHoliday",
						label : "观影节日",
						desc : "观影节日",
						type : "multiselect",
						param : "sourceId:dimdef;typeId:2014;filter:false",
						allowedOperators : "in",
						repeatable : false,
						required : false
					}, {
						id : "watchTradeDay",
						label : "观影日期",
						desc : "观影日期",
						type : "date",
						allowedOperators : "between,eq",
						repeatable : false,
						validate : {
							type : 'date'
						}
					}, {
						id : "watchTradeChannel",
						label : "观影交易渠道",
						desc : "观影交易渠道",
						type : "multiselect",
						param : "sourceId:dimdef;typeId:2010",
						allowedOperators : "in",
						repeatable : false,
						required : false
					}, {
						id : "watchTradeWeeks",
						label : "交易星期",
						desc : "交易星期",
						type : "multiselect",
						param : "sourceId:dimdef;typeId:1020",
						allowedOperators : "in",
						repeatable : false,
						required : false
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
					inputs : [ {
						id : "conSaleDate",
						label : "卖品交易日期",
						desc : "卖品交易日期",
						type : "date",
						allowedOperators : "between,eq",
						repeatable : false,
						validate : {
							type : 'date'
						}
					}, /*{
						id : "conSaleHoliday",
						label : "节假日卖品交易",
						desc : "节假日卖品交易",
						type : "select",
						param : "sourceId:dimdef;typeId:2006;filter:false",
						allowedOperators : "eq",
						repeatable : false,
						required : false
					},*/{
						id : "holidayConSale",
						label : "节日卖品交易",
						desc : "节日卖品交易",
						type : "multiselect",
						param : "sourceId:dimdef;typeId:2014;filter:false",
						allowedOperators : "in",
						repeatable : false,
						required : false
					},{
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
						validate : {
							type : 'number'
						}
					}, {
						id : "conSaleCinema",
						label : "卖品交易影城",
						desc : "卖品交易影城",
						type : "composite",
						param : "compositeId:cinema", // return is INNER_CODE
						allowedOperators : "in",
						repeatable : false,
						required : false
					}, {
						id : "conSaleAmount",
						label : "卖品消费金额",
						desc : "卖品消费金额",
						type : "text",
						allowedOperators : "between,eq,lt,gt,le,ge",
						repeatable : false,
						required : false,
						validate : {
							type : 'number'
						}
					}, {
						id : "conSaleConsumeTime",
						label : "卖品消费次数",
						desc : "卖品消费次数",
						type : "text",
						allowedOperators : "between,eq,lt,gt,le,ge",
						repeatable : false,
						required : false,
						validate : {
							type : 'integer'
						}
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
						id : "conSaleWeeks",
						label : "交易星期",
						desc : "交易星期",
						type : "multiselect",
						param : "sourceId:dimdef;typeId:1020",
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
					} ]
				}, {
					id : "tsaleNot",
					label : "票房交易-未交易",
					desc : "票房交易-未交易",
					inputs : [ {
						id : "ticketTypeNot",
						label : "万达票类分类",
						desc : "万达票类分类",
						type : "multiselect",
						param : "sourceId:dimdef;typeId:169;filter:false",
						defaultValue : [ '01' ],
						allowedOperators : "in",
						repeatable : false,
						required : false
					}, {
						id : "transFilmNot",
						label : "观影影片",
						desc : "观影影片",
						type : "composite",
						param : "compositeId:film",
						allowedOperators : "in",
						repeatable : false,
						required : false
					}, {
						id : "watchTradeCinemaNot",
						label : "观影影城",
						desc : "观影影城",
						type : "composite",
						param : "compositeId:cinema",
						allowedOperators : "in",
						repeatable : false,
						required : false
					}, {
						id : "watchTradeTimeNot",
						label : "观影时段",
						desc : "观影时段",
						type : "multiselect",
						param : "sourceId:dimdef;typeId:2007",
						allowedOperators : "in",
						repeatable : false,
						required : false
					}, {
						id : "watchTradeHourNot",
						label : "观影小时",
						desc : "观影小时",
						type : "select",
						param : "sourceId:dimdef;typeId:2003",
						allowedOperators : "between,eq",
						defaultValue : [ "1", "23" ],
						repeatable : false,
						required : false,
						validate : {
							type : 'number'
						}
					}, /*{
						id : "holidayWatchTradeNot",
						label : "节假日观影",
						desc : "节假日观影",
						type : "select",
						param : "sourceId:dimdef;typeId:2006;filter:false",
						allowedOperators : "eq",
						repeatable : false,
						required : false
					},*/{
						id : "watchTradeNotHoliday",
						label : "观影节日",
						desc : "观影节日",
						type : "multiselect",
						param : "sourceId:dimdef;typeId:2014;filter:false",
						allowedOperators : "in",
						repeatable : false,
						required : false
					}, {
						id : "watchTradeDayNot",
						label : "观影日期",
						desc : "观影日期",
						type : "date",
						allowedOperators : "between,eq",
						repeatable : false,
						validate : {
							type : 'date'
						}
					}, {
						id : "watchTradeChannelNot",
						label : "观影交易渠道",
						desc : "观影交易渠道",
						type : "multiselect",
						param : "sourceId:dimdef;typeId:2010",
						allowedOperators : "in",
						repeatable : false,
						required : false
					}, {
						id : "watchTradeWeeksNot",
						label : "交易星期",
						desc : "交易星期",
						type : "multiselect",
						param : "sourceId:dimdef;typeId:1020",
						allowedOperators : "in",
						repeatable : false,
						required : false
					}, {
						id : "watchPayMethodNot",
						label : "观影支付方式",
						desc : "观影支付方式",
						type : "multiselect",
						param : "sourceId:paymethod;typeId:1",
						allowedOperators : "in",
						repeatable : false,
						required : false
					} ]
				}

				, {
					id : "conSaleNot",
					label : "卖品交易-未交易",
					desc : "卖品交易-未交易",
					inputs : [ {
						id : "conSaleDateNot",
						label : "卖品交易日期",
						desc : "卖品交易日期",
						type : "date",
						allowedOperators : "between,eq",
						repeatable : false,
						validate : {
							type : 'date'
						}
					},/* {
						id : "conSaleHolidayNot",
						label : "节假日卖品交易",
						desc : "节假日卖品交易",
						type : "select",
						param : "sourceId:dimdef;typeId:2006;filter:false",
						allowedOperators : "eq",
						repeatable : false,
						required : false
					}, */{
						id : "holidayConSaleNot",
						label : "节日卖品交易",
						desc : "节日卖品交易",
						type : "multiselect",
						param : "sourceId:dimdef;typeId:2014;filter:false",
						allowedOperators : "in",
						repeatable : false,
						required : false
					}, {
						id : "conSaleHourPeriodNot",
						label : "卖品交易时段",
						desc : "卖品交易时段",
						type : "multiselect",
						param : "sourceId:dimdef;typeId:2007",
						allowedOperators : "in",
						repeatable : false,
						required : false
					}, {
						id : "conSaleHourNot",
						label : "卖品交易小时",
						desc : "卖品交易小时",
						type : "select",
						param : "sourceId:dimdef;typeId:2003",
						allowedOperators : "between,eq",
						defaultValue : [ "1", "23" ],
						repeatable : false,
						required : false,
						validate : {
							type : 'number'
						}
					}, {
						id : "conSaleCinemaNot",
						label : "卖品交易影城",
						desc : "卖品交易影城",
						type : "composite",
						param : "compositeId:cinema", // return is INNER_CODE
						allowedOperators : "in",
						repeatable : false,
						required : false
					}, {
						id : "conSaleItemNot",
						label : "卖品品项",
						desc : "卖品品项",
						type : "composite",
						param : "compositeId:conitem",
						allowedOperators : "in",
						repeatable : false,
						required : false
					}, {
						id : "conSaleCategoryNot",
						label : "卖品品类",
						desc : "卖品品类",
						type : "tree",
						param : "treeId:concategory",
						allowedOperators : "in",
						repeatable : false,
						required : false
					}, {
						id : "conSaleWeeksNot",
						label : "交易星期",
						desc : "交易星期",
						type : "multiselect",
						param : "sourceId:dimdef;typeId:1020",
						allowedOperators : "in",
						repeatable : false,
						required : false
					}, {
						id : "conSalePayMethodNot",
						label : "卖品支付方式",
						desc : "卖品支付方式",
						type : "multiselect",
						param : "sourceId:paymethod;typeId:1",
						allowedOperators : "in",
						repeatable : false,
						required : false
					} ]
				},
				{
					id : "transSalesCountNot",
					label : "总交易信息-未交易",
					desc : "总交易信息-未交易",
					inputs : [ {
						id : "transCinemaNot",
						label : "交易影城",
						desc : "交易影城",
						type : "composite",
						param : "compositeId:cinema",
						allowedOperators : "in",
						repeatable : false,
						required : false
					}, {
						id : "transTimeNot",
						label : "交易时段",
						desc : "交易时段",
						type : "multiselect",
						param : "sourceId:dimdef;typeId:2007",
						allowedOperators : "in",
						repeatable : false,
						required : false
					}, {
						id : "transHourNot",
						label : "交易小时",
						desc : "交易小时",
						type : "select",
						param : "sourceId:dimdef;typeId:2003",
						allowedOperators : "between,eq",
						defaultValue : [ "1", "23" ],
						repeatable : false,
						required : false,
						validate : {
							type : 'number'
						}
					}, {
						id : "transHolidayNot",
						label : "节假日交易",
						desc : "节假日交易",
						type : "multiselect",
						param : "sourceId:dimdef;typeId:2014;filter:false",
						allowedOperators : "in",
						repeatable : false,
						required : false
					},/* {
						id : "holidayTransNot",
						label : "节假日交易",
						desc : "节假日交易",
						type : "select",
						param : "sourceId:dimdef;typeId:2006;filter:false",
						allowedOperators : "eq",
						repeatable : false,
						required : false
					},*/ {
						id : "transDayNot",
						label : "交易日期",
						desc : "交易日期",
						type : "date",
						allowedOperators : "between,eq",
						repeatable : false,
						validate : {
							type : 'date'
						}
					}, {
						id : "transWeeksNot",
						label : "交易星期",
						desc : "交易星期",
						type : "multiselect",
						param : "sourceId:dimdef;typeId:1020",
						allowedOperators : "in",
						repeatable : false,
						required : false
					}, {
						id : "transPayMethodNot",
						label : "观影支付方式",
						desc : "观影支付方式",
						type : "multiselect",
						param : "sourceId:paymethod;typeId:1",
						allowedOperators : "in",
						repeatable : false,
						required : false
					} ]
				}]
			}
		}
	});
})(jQuery);
