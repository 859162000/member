/**
 * 下拉框列表联动
 */
$.fn.linkSelects = function(src, url, noEmptyOption, vpro, defaultValue,
		emptyOptionLabel) {
	var id = $(this).attr("id");
	var container = $(this);
	$("#" + src)
			.change(
					function() {
						// if (container.data('changing')) {
						// return;
						// }
						container.data('changing', true);
						var srcValue;
						if ($("#" + src).val() != null) {
							srcValue = ($("#" + src).val()).join(",");
						}

						container.html('');
						$
								.getJSON(
										url,
										{
											param : srcValue,
											ajax : 'true'
										},
										function(data) {

											if (!noEmptyOption) {
												var opl = (isUndefined(defaultValue)
														|| defaultValue == '' ? '<option value="" selected="selected">'
														: '<option value="">');
												opl = opl
														+ (isUndefined(emptyOptionLabel) ? ''
																: emptyOptionLabel);
												opl = opl + '</option>';
												container.append(opl);
											}
											$
													.each(
															data,
															function(i, item) {
																var opt = $('<option/>');
																var v = (isUndefined(vpro) ? item.id
																		: eval("item."
																				+ vpro));
																opt.val(v);
																for ( var i = 0; i < defaultValue.length; i++) {
																	if (v == defaultValue[i]) {
																		opt
																		.attr(
																				"selected",
																		"selected");
																	}
																}
																opt
																		.html(item.label);
																container
																		.append(opt);
															});

											container.multiselect({
												selectedList : 2
											});
											container.multiselect("refresh");
											container.change();
											container.data('changing', true);
										});
					});
	$("#" + src).change();
};

/**
 * 自动提示输入框
 */
$.fn.multiAutocompl = function(options) {
	var o = {
		url : '',
		data : {},
		minLenght : 2,
		id : '',
		label : '',
		inputId : ''
	};
	$.extend(o, options);
	var c = $(this);

	var info = $('<span class="info ui-icon ui-icon-help" style="display:inline-block; vertical-align: middle;"></span>');
	info.attr("title", "最少输入2个字符或汉字，按方向键系统将提供输入帮助.");
	$(this).after(info);
	c.autocomplete({
		source : function(request, response) {
			var d = c.val();
			$.ajax({
				url : o.url,// 结果为json返回的url
				data : {
					'like' : d
				},// 请求参数，json格式
				success : function(data) {
					response($.map(data, function(item) {
						return {
							id : item.id,
							value : item.value,
							label : item.label,
							description : item.description
						};
					}));
				}
			});
		},
		minLength : o.minLenght,//
		focus : function(event, ui) {
			return false;
		},
		blur : function(event, ui) {
			$("#" + o.inputId).val("");
			return false;
		},
		select : function(event, ui) {
			var itemidstr = $("#" + o.id).val();
			if(itemidstr != '') {
				itemidstr += ',' + ui.item.id;
			} else {
				itemidstr += ui.item.id;
			}
			$("#" + o.id).val(itemidstr);
			
			$("#" + o.inputId).val("");
			var lab = ui.item.label;
			$("#" + o.label).attr("title",lab);
			$("#" + o.label).html($("#" + o.label).html() 
					+ "<a href='#' id='"+ui.item.id+"'>," + lab + "</a>");
			$("#" + o.label).append("<br/>");
			
			$("a","#" + o.label).each(function(){
				$(this).live("click",function(){
					var id = $(this).attr("id");
					
					var arryId1 = $("#" + o.id).val().split(",");
					var arryId2 = new Array();
					var j = 0;
					for ( var i = 0; i < arryId1.length; i++) {
						if(arryId1[i] != id) 
							arryId2[j++] = arryId1[i];
					}
					
					$("#" + o.id).val(arryId2.join(','));
					
					$(this).next().remove();
					$(this).remove();
				});
			});
			
			return false;
		},
		change : function(event, ui) {
//			if ($.trim($("#" + o.label).val()) == '') {
//				$("#" + o.id).val('');
//			}
//			if ($.trim($("#" + o.id).val()) == '') {
//				$("#" + o.label).val('');
//			}
		}
	});
};