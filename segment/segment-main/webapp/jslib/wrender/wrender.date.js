/**
 * JQuery Widget Render plugin
 * 日期控件
 */ 
(function($){
	//set datepk default options
	$.datepk.setDefaults($.extend({
		regional: "zh-CN",
		showMonthAfterYear: false,
		changeMonth: true,
		changeYear: true,
		dateFormat: 'yy-mm-dd',
		showOn: "button",
		showButtonPanel: true,
		buttonImage: $.wrender.context + "/jslib/wrender/images/calendar.gif",
		buttonImageOnly: true
	}));
	
	$.extend($.wrender, {
		date: {
			render: function(elem) {
				$(elem).datepk();
			},
			setValue: function(elem, value) {
				$(elem).val($.wrender.filterOutNull(value));
			},
			getValue: function(elem) {
				return $(elem).val();
			},
			getValueLabel: function(elem) {
				return this.getValue(elem);
			},
			show: function(elem) {
				$(elem).next('img.ui-datepicker-trigger').show();
				$(elem).show();
			},
			hide: function(elem) {
				$(elem).next('img.ui-datepicker-trigger').hide();
				$(elem).hide();
			},
			change: function(elem, fun) {
				$(elem).change($.wrender.wrapChange(elem, fun));
			},
			getHtml: function(name, param) {
				var paramAttr = (param != null) ? 'wrParam="' + param + '"' : '';
				html = '<input type="text" wrType="date" ' + paramAttr + 'name="' + name + '"/>';
				return html;
			}
		}
	});

})(jQuery);