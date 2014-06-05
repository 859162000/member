
	function getContextPath()
	{
		var pathName = window.location.pathname.substring(1);
		var contextPath = (pathName == '') ? '' : pathName.substring(0, pathName.indexOf('/'));
		return '/'+ contextPath;
	}
	
	//Automatic set JQGrid width following the width of parent element. 
	function jqgridAutoResize($gridParent, $grid) {
		$gridParent.resize(function(e) {
			var width = $(this).width() - 2;
			if(width < 200) {
				width = 200;
			}
			
			$grid.jqGrid("setGridWidth", width);
		});
	}



	
	(function($) {
		
		/**
		 * Usage of $msgBox
		 * @param type 'info', 'checked', 'error', 'warn', 
		 *             'confirm', 'yesno'
		 * 
		 */
		$.extend({
			msgBox : function(type, title, content, onCloseFunction) {
				var lcType = type.toLowerCase();
				var opt = $.msgBox.defaults;
				
				//If dialog isn't exist, append the dialog html and define it.
				if($('#msgBox_dialog').size() == 0) {
					$('body').append($.msgBox.dialogHtml);
					
					$('#msgBox_dialog').dialog({
						bgiframe: true,
						autoOpen: false,
						resizable: false,
						closeOnEscape: true,
						modal: true,
						width: opt.width,
						height: opt.height
					});
				}
				
				var mbDialog = $('#msgBox_dialog');
				var mbIcon = $('#msgBox_dialog_icon');
				var mbContent = $('#msgBox_dialog_content');
				
				mbIcon.removeClass().addClass(lcType + 'Icon');
				
				switch(lcType) {
				case 'info':
				case 'checked':
				case 'error':
				case 'warn':
					mbDialog.dialog('option', 'buttons', $.msgBox.buttonsOK);
					break;
				case 'confirm':
					mbDialog.dialog('option', 'buttons', $.msgBox.buttonsOKCancel);
					break;
				case 'yesno':
					mbDialog.dialog('option', 'buttons', $.msgBox.buttonsYesNo);
					break;
				}
				
				mbDialog.dialog('option', 'title',  title);
				mbContent.html(content);
				
				//Invocate the callback function onCloseFunction(returnvalue)
				if( onCloseFunction != null && (typeof onCloseFunction == 'function') ) {
					mbDialog.dialog('option', 'close', function() {
						onCloseFunction($(this).data('returnValue'));
					});
				}
				else {
					mbDialog.dialog('option', 'close', null);
				}
				
				
				$('#msgBox_dialog').dialog('open');
			}
		});
		
	    $.msgBox.defaults =
		{
		    //height: 200,
		    width: 400
		};
		
		$.msgBox.dialogHtml = '<div id="msgBox_dialog">' +      
			'<div id="msgBox_dialog_icon"></div>' +
			'<span id="msgBox_dialog_content"></span></div>';
		
		
		$.msgBox.buttonsOK = [{ text: "确定", click: function() { $(this).dialog("close"); } }];
		
		$.msgBox.buttonsOKCancel = [
			    { text: "确定", click: function() { 
			    	$(this).data('returnValue', true); 
			    	$(this).dialog("close");} 
			    },  
			    { text: "取消", click: function() {
			    	$(this).data('returnValue', false); 
			    	$(this).dialog("close"); } 
			    }
		    ];
		
		$.msgBox.buttonsYesNo = [
			    { text: "是", click: function() { 
			    	$(this).data('returnValue', true); 
			    	$(this).dialog("close");} 
			    },  
			    { text: "否", click: function() {
			    	$(this).data('returnValue', false); 
			    	$(this).dialog("close"); } 
			    }
			];
		
		
		/**
		 * The jqgrid post data recognized by format: { name1: value1, name2: value2 ...}
		 * The function is jquery extents function to serialize all value and name of input tags <br/> 
		 * which be contained one designated parent tag into jqgrid post data format.
		 * For example: <br/>
		 *  var params = $('#oneFormId').serializeJson(); <br/>
			$('#oneGridId').jqGrid('setPostData', params);<br/>
		 * 
		 * @returns jqGrid format post data js object.
		 */
		$.fn.serializeJqgrid = function() {
			var arr = this.serializeArray();
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
		
//		if($.jgrid != null) {
//			/**
//			 * Set default options for JQGrid
//			 */
//			$.jgrid.defaults = {
//						datatype: "json",
//						ajaxGridOptions: {type:"POST"},
//						jsonReader: {repeatitems: false},
//						loadtext: "Loading...",
//						prmNames: {page:"queryParam.page", rows:"queryParam.rows", sort:"queryParam.sort", order: "queryParam.order"},
//						height: "100%",
//						rowNum: 10,
//						rowList:[10,20,30],
//						autowidth: true,
//						multiselect: true,
//						viewrecords: true
//					};
//		}
	
		//Set the default jQuery AJAX parameters
	    $.ajaxSetup({
			type: "POST",
			dataType: 'json'
	    });
	    
	    if($.ui != null && $.ui.dialog != null) {
		    //Set Jquery UI dialog default options
		    $.extend($.ui.dialog.prototype.options, {
				autoOpen: false,
				resizable: false,
				modal: true,
				width: 500
		    });
	    }

	    $(function(){
			
			var contextPath = getContextPath();
			var url = window.location.href;
			$('body').ajaxError(function(event, xhr, settings) {
				try{
					//Whenever an Ajax request completes with an error, jQuery triggers the ajaxError event.
					//In server side error interceptor, the error message will be sent within a JSon object.  
					var resultObj = jQuery.parseJSON(xhr.responseText);
					
					var message = null;
					if(resultObj != null && resultObj.message != null) {
						message = resultObj.message;
						if(message == 'logout') { //判断放回消息中的特殊标志 'logout'
							$.msgBox('warn', '系统退出登录', '由于长时间未操作或者已进行退出操作，系统退出登录。请重新登录！', function() {
								window.location.reload(true);
							});
							return;
						}
					}

					alertError(message, xhr, settings);
				}
				catch(err){
					alertError(null, xhr, settings);
				}
				
			});
			
			function alertError(message, xhr, settings) {
				var outText = "Failed to parse the service response! \n";
				outText += " - AJAX Settings: {url=" + settings.url + ", dataType=" + settings.dataType + ", type=" + settings.type + "}\n";
				
				if(message != null) { //
					outText += " - Prompt information: \n" + message;
				}
				else if(xhr.responseText != null && (typeof xhr.responseText == "string")) {
					outText += "  Response data: \n" + xhr.responseText;
				}

				alert(outText);
			}

		});	
		
	})(jQuery);

	
	




		


