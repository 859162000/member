/**
 * 下拉框列表联动
 */
$.fn.linkSelects = function(src, url, noEmptyOption, vpro, value,
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
												var opl = (isUndefined(value)
														|| value == '' ? '<option value="" selected="selected">'
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
																
																if(value != null && value != "null" && value != ""){
																	var values = eval("("+value+")");
																	$.each(values, function(idx, value) { 
																		if (v == value) {
																			opt.attr("selected",
																							"selected");
																			return ;
																		}
																	} );
																}
																
																
																opt
																		.html(item.label);
																container
																		.append(opt);
															});

											container.multiselect({
												selectedList : 3
											});
											container.multiselect("refresh");
											container.change();
											container.data('changing', true);
										});
					});
	$("#" + src).change();
};