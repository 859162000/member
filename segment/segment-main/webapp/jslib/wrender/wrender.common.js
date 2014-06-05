/**
 * JQuery Widget Render plugin
 * 日期控件
 */ 
(function($){

	$.extend($.wrender, {
		text: {
			render: function(elem) {
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
			change: function(elem, fun) {
				$(elem).change($.wrender.wrapChange(elem, fun));
			},
			getHtml: function(name, param) {
				var paramAttr = (param != null) ? 'wrParam="' + param + '"' : '';
				html = '<input type="text" wrType="text" ' + paramAttr + 'name="' + name + '"></input>';
				return html;
			}
		},
		textarea: {
			render: function(elem) {
				
			},
			setValue: function(elem, value) {
				$(elem).attr('value', $.wrender.filterOutNull(value));
			},
			getValue: function(elem) {
				return $(elem).attr('value');
			},
			getValueLabel: function(elem) {
				return this.getValue(elem);
			},
			change: function(elem, fun) {
				$(elem).change($.wrender.wrapChange(elem, fun));
			},
			getHtml: function(name, param) {
				var paramAttr = (param != null) ? 'wrParam="' + param + '"' : '';
				html = '<textarea wrType="textarea" ' + paramAttr + 'name="' + name + '" rows="3" cols="50"></textarea>';
				return html;
			}
		},
		readtext: {
			render: function(elem) {

			},
			setValue: function(elem, value) {
				$(elem).text($.wrender.filterOutNull(value));
			},
			getValue: function(elem) {
				return $(elem).text();
			},
			getValueLabel: function(elem) {
				return this.getValue(elem);
			},
			change: function(elem, fun) {
				$(elem).change($.wrender.wrapChange(elem, fun));
			},
			getHtml: function(name, param) {
				var paramAttr = (param != null) ? 'wrParam="' + param + '"' : '';
				html = '<div wrType="readtext" ' + paramAttr + 'name="' + name + '"></div>';
				return html;
			}
		},
		button: {
			render: function(elem) {
				var $elem = $(elem);
				var params = $.wrender.getParam(elem);
				var btnOptions = {};
				if(params != null) {
					if(params.icon != undefined) {
						btnOptions.icons = {"primary": params.icon};
					}
					else {
						if(params.icons-primary != undefined) {
							btnOptions.icons = {"primary":params.icon};
						}
						if(params.icons-secondary != undefined) {
							btnOptions.icons = btnOptions.icons || {};
							btnOptions.icons.secondary = params.icon;
						}
					}
					
					if(params.text != undefined) {
						btnOptions.text = (params.text == 'true' || params.text == 'yes') ? true : false;
					}
					
					if(params.label != undefined) {
						btnOptions.label = params.label;
					}
					
					if(params.disabled != undefined) {
						btnOptions.disabled = (params.disabled == 'true' || params.disabled == 'yes') ? true : false;
					}
				}

				$elem.button(btnOptions);
			},
			getHtml: function(name, param) {
				var paramAttr = (param != null) ? 'wrParam="' + param + '"' : '';
				html = '<button type="button" wrType="button" ' + paramAttr + 'name="' + name + '"/>';
				return html;
			}
		}
	});
})(jQuery);