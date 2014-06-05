/**
 * JQuery Widget Render plugin
 * 日期时间控件
 */ 
(function($){
	
	$.datepicker.setDefaults($.extend({
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
	
	var options = {
		showSecond: true,
		timeFormat: 'HH:mm:ss',
		timeText: '时间：',
		hourText: '时：',
		minuteText: '分：',
		secondText: '秒：',
		currentText: '当前时间',
		closeText: '确定'
	};
	
	$.extend($.wrender, {
		datetime: {
			render: function(elem) {
				$(elem).datetimepicker(options);
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
				html = '<input type="text" wrType="datetime" ' + paramAttr + 'name="' + name + '"/>';
				return html;
			}
		}
	});

	
	
})(jQuery);