(function($){
	
	$.segment = $.segment || {};
	
	$.extend($.segment, {
		
		settingsCache: {}, 
		
		init: function(applyName) { //对应一套配置进行初始化,  $.segment.settings['SomeApplyName']
			
			var applySetting = $.segment.settings[applyName];
			
			//create cache. Below is the cache object instance: 
			//
			//{"member":{
			//	"groups": {
			//		"base":{"id":"base","label":"会员基本属性","desc":"...."},
			//		"sale":{"id":"sale","label":"会员交易","desc":"....."}
			//	},
			//	"inputs":{
			//		"noDisturb":{"id":"noDisturb","label":"是否希望被联络","desc":"....","type":"DIM_SEL","allowedOperators":"=","repeatable":false,"groupId":"base"},
			//		"manageCinema":{"id":"manageCinema","label":"管理影城","desc":"....","type":"COMPOSITE","compositeId":"cinema","allowedOperators":"in","repeatable":false,"groupId":"base"},
			//		"registerCinema":{"id":"registerCinema","label":"注册影城","desc":"...","type":"COMPOSITE","compositeId":"cinema","allowedOperators":"in","repeatable":false,"groupId":"base"}
			//	}
			//}
			//
			if($.segment.settingsCache[applyName] == undefined) {
				var cache = {groups: {}, inputs: {}};
				for(index in applySetting.inputsGroups) {
					//clone the inputsGroup object, 
					var group = $.extend({}, applySetting.inputsGroups[index]);
					
					//copy the input objects and add it into cache
					var inputs = group.inputs;
					for(iIndex in inputs) {
						var input = inputs[iIndex];
						input['groupId'] = group.id; //add the groupId property in each input
						cache.inputs[input.id] = input;
					}
					
					delete group.inputs;
					cache.groups[group.id] = group;
				}
				
				$.segment.settingsCache[applyName] = cache;
			}
			
			return applySetting;
		},
		operatorLabelMap: {
			'e': '=',  
			'ne': '!=', 
			'gt': '&gt;',  
			'lt': '&lt;',  
			'gte': '&gt;=',  
			'lte': '&lt;=',  
			'like': '模糊匹配',  
			'range': '区间',  
			'in': '包含'
		}

	
	
	});

	

	
	
	
	/**
	 * The jqgrid post data recognized by format: { name1: value1, name2: value2 ...}
	 * The function is to serialize all inputs from one html 'form' tags, and then convert them into the jqgrid post data format.<br/> 
	 * For example: <br/>
	 *  var params = $('#oneFormId').serializeJson(); <br/>
		$('#oneGridId').jqGrid('setPostData', params);<br/>
	 * 
	 * @returns jqGrid format post data object.
	 */
	function serializeJqgrid(searchForm) {
		//serialize form into array format: [{name:'name1', value:'value1'}, {name:'name2', value:'value2'}  ...]
		var arr = searchForm.serializeArray();
		var ret = {};
		if(arr.length > 0) {		
			for (var i = 0; i < arr.length; i++)  {
				if(arr[i].name != null && arr[i].name.length > 0) {
					ret[arr[i].name] = arr[i].value;
				}
			}
		}
		
		return ret;
	};



 	
	//获取Web Application上下文root名称
	function getContextPath() {
	    var pathName = document.location.pathname;
	    var index = pathName.substr(1).indexOf("/");
	    var result = pathName.substr(0,index+1);
	    return result;
	};
	
	
})(jQuery);