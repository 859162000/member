
var combineAction = {};

$(function() {

	var countAlterTimer;	//客群计算差值查询
	//被选中的客群活动
	var selectedSegment = [];
	//var isNew = false;
	
	combineAction.inputCombineSegmentDialog = $('#inputCombineDialog');
	var dialogCal = $("#dialog_cal");	//计算获得失去对话框
	var inputCombineForm = $('#inputCombineForm');
	var segments = $("#segments");
	var rowTempl = $("#template");	//模板行
	
	/* 获取客群 */
	var getSegments = $.wrender.context+'/segment/SegmentAction/getSegments.do';
	/* 获取选中的客群 */
	var getSelectedSegment = $.wrender.context+'/segment/SegmentAction/getSelectedSegments.do';
	/* 客群计算 */
	var calCountUrl = $.wrender.context+'/segment/SegmentAction/calCount.do';
	/* 存储客群 */
	var insertUrl = $.wrender.context+'/segment/SegmentAction/insertSegments.do';
	/* 更新客群 */
	var updateUrl = $.wrender.context+'/segment/SegmentAction/updateSegments.do';
	/* 获取复合客群计算情况 */
	var updateCountAlter = $.wrender.context+'/segment/SegmentAction/updateCountAlter.do';
	/* 判断客群是否被占用 */
	var segmentOccupied = $.wrender.context+'/segment/SegmentAction/segmentOccupied.do';
	
	// 初始化
	init();
	
	function init() {
		renderBtn(inputCombineForm);
		
		$("#compositeSegmentItem", inputCombineForm).wrender();
		$("select[name=combineType]", inputCombineForm).wrender();
		
		$("#compositeSegmentItem", inputCombineForm).wrender('change', function(value) {
			if(value.selections.value) {
				ids = "";
				$.each(value.selections.value, function(i, item) {
					ids += (item+"|");
				});
				
				if(ids != "") {	//获取选中的客群活动
					$.ajax({
						url: getSelectedSegment,
						data: 'segmentIds=' + ids,
						success: function(result) {
							if(result && result.length > 0) {
								$.each(result, function(i, item) {
									addSegment(item, i, 'new');
								});
								// 渲染
								//$('button[wrType=button]', inputCombineForm).wrender();
							}
						}
					});
				}
			}
		});
	}
	
	
	$("#dialog_cal").dialog({
		width: 250,
		buttons: [
			{
				text:'关闭后台执行',
				click: function() {
					$(this).dialog('close'); 
				}
			}
		],
		beforeClose: function(event, ui) {
			closeTimer();
		}
	});
	
	$('#inputCombineDialog').dialog({});
	
	function viewDialog(){
		$('#inputCombineDialog').dialog({
			width: 710,
			buttons: [
				{text:'关闭', click: function() { $(this).dialog('close'); }}
			],
			
			beforeClose: function(event, ui) {
				inputCombineForm.resetForm();
				$('[wrType]', inputCombineForm).wrender('setValue', {code:'',name:'',calCount:'',calCountTime:'',segmentId:'',version:''});
				selectedSegment = [];
				$('tr:[id=ok]', segments).remove();
				//$('input[name=name]', inputCombineForm).removeAttr('disabled');
			}
		});
	}
	
	function inputDialog() {
		$('#inputCombineDialog').dialog({
			width: 710,
			buttons: [
				{text:'保存', click: function() { doSave(false) }},
				{text:'关闭', click: function() { $(this).dialog('close'); }}
			],
			
			beforeClose: function(event, ui) {
				closeTimer();
				
				//Clear timer and reset the calCountTimerId to be null
				//if(calCountTimerId != null) {
				//	clearInterval(calCountTimerId);
				//	calCountTimerId = null;
				//}
				
				//validator.resetForm(); //After dialog closed, clean the validation error style and prompt text.
				inputCombineForm.resetForm();
				$('[wrType]', inputCombineForm).wrender('setValue', {code:'',name:'',calCount:'',calCountTime:'',controlCountRate:'10',controlCount:'',segmentId:'',version:''});
				selectedSegment = [];
				$('tr:[id=ok]', segments).remove();
				//$('input[name=name]', inputCombineForm).removeAttr('disabled');
			}
		});
	}
	
	/**
	 * type  new新添加 edit编辑显示 view显示
	 */
	function addSegment(value, index, type) {
		var isExist = -1;	//存在
		
		isExist = $.inArray(value.subSegmentId, selectedSegment);
		//isExist = $.isArray(selectedSegment, value.subSegmentId);
		/*$.each(selectedSegment, function(i, item) {
			if(parseInt(selectedSegment[i]) == parseInt(value.subSegmentId)) {
				isExist = true;
				return ;
			}
		});*/
		
		if(isExist != -1) {	//当前客群没有添加到列表中
			return ;
		}
		
		var row = rowTempl.clone();
		$("td[name=segmentId]", row).html(value.subSegmentId);
		$("td[name=code]", row).html(value.code);
		$("td[name=name]", row).html(value.name);
		
		if(parseInt(value.calCount) < 0) {
			if(value.status == '40') {
				$("td[name=calCount]", row).html('计算失败');
			} else if(value.status == '10'){
				$("td[name=calCount]", row).html('未计算');
			} else {
				$("td[name=calCount]", row).html('计算中...');
			}
		} else {
			$("td[name=calCount]", row).html(value.calCount);
		}
		
		if(parseInt(value.countAlter) < 0) {
			$("td[name=countalter]", row).html('&nbsp;');
		} else {
			if(value.setRelation == 'UNION') {
				$("td[name=countalter]", row).html('获得 '+value.countAlter);
			} else if(value.setRelation == 'INTERSECT' || value.setRelation == 'MINUS') {
				$("td[name=countalter]", row).html('失去 '+value.countAlter);
			}
		}
		
		if(selectedSegment.length == 0) {	//主客群记录没有操作类型
			$("td[name=type]", row).html('&nbsp;');
		} else {
			if(type=='edit') {// 编辑时回显
				row.find("select").val(value.setRelation);
				$("select option[value="+value.setRelation+"]", row).attr("selected", true);
				if(value.setRelation == 'UNION') {
					row.find('img').css('display','').attr('src', $.wrender.context+'/images/set-union.png');
				} else if(value.setRelation == 'INTERSECT') {
					row.find('img').css('display','').attr('src', $.wrender.context+'/images/set-intersect.png');
				} else if(value.setRelation == 'MINUS') {
					row.find('img').css('display','').attr('src', $.wrender.context+'/images/set-minus.png');
				}
			} else if(type == 'view') {
				$('select', row).remove();
				if(value.setRelation == 'UNION') {
					$('td[name=type]', row).html('合集&nbsp;&nbsp;<img src="'+$.wrender.context+'/images/set-union.png" height="25" width="40">');
				} else if(value.setRelation == 'INTERSECT') {
					$('td[name=type]', row).html('交集&nbsp;&nbsp;<img src="'+$.wrender.context+'/images/set-intersect.png" height="25" width="40">');
				} else if(value.setRelation == 'MINUS') {
					$('td[name=type]', row).html('差集&nbsp;&nbsp;<img src="'+$.wrender.context+'/images/set-minus.png" height="25" width="40">');
				}
			}
		}
		
		if(type == 'view') {
			$('button', row).remove();	//移除所有的编辑按钮
		}
		
		segments.append("<tr class='ui-widget-content jqgrow ui-row-ltr' tabindex='-1' role='row' conType='"+type+"' id='ok'>"+row.html()+"</tr>");
		selectedSegment.push(value.subSegmentId);	//添加到客群选中列表中
	}
	
	/**
	 * 渲染编辑列表按钮
	 */
	function renderBtn(scope) {
		// 渲染
		$('button[wrType=button]', scope).wrender();
		// 向上按钮
		$("button[name=onSubUpBtn]",scope).live('click', function(){
			UpSequence(this);
		});
		// 向下按钮
		$("button[name=onSubDownBtn]",scope).live('click', function(){
			DownSequence(this);
		});
		// 删除按钮
		$("button[name=onSubDeleteBtn]",scope).live('click', function(){
			DelSequence(this);
		});
		// 选择按钮
		$("select", scope).live("change", function() {
			// 去掉选中项
			//$("option:selected", this).attr("selected",false);
			if($(this).val() == 'UNION') {
				$(this).next('img').css('display','').attr('src', $.wrender.context+'/images/set-union.png');
			} else if($(this).val() == 'INTERSECT') {
				$(this).next('img').css('display','').attr('src', $.wrender.context+'/images/set-intersect.png');
			} else if($(this).val() == 'MINUS') {
				$(this).next('img').css('display','').attr('src', $.wrender.context+'/images/set-minus.png');
			}else if($(this).val() == '--') {
				$(this).next('img').css('display','none');
			}
			// 设置选中项
			//$("option[value="+$(this).val()+"]", this).attr("selected",true);
		});
	}
	
	/**
	 * 向上移动
	 */
	function UpSequence(obj) {
		var tr = $(obj).parent('td').parent('tr');
		var val = $("select", tr).val();
		var prevTr = tr.prev('tr');	//获取前一条记录
		if(prevTr.attr("id") != 'title') {	//存在说明不是第一条记录
			if(prevTr.prev('tr').attr('id') == 'title') {	//第二行数据和第一行数据交换
				exchange(tr, prevTr);
				$("select", prevTr).val(val);	//设置模式类型
			}
			var row = $("<tr class='ui-widget-content jqgrow ui-row-ltr' tabindex='-1' conType='"+$(tr).attr('conType')+"' role='row' id='ok'>"+tr.html()+"</tr>");
			$("select", row).val(val);
			prevTr.before(row);
			tr.remove();
		}
		
		$('button[wrType=button]', inputCombineForm).wrender();
	}
	
	/**
	 * 向下移动
	 */
	function DownSequence(obj) {
		var tr = $(obj).parent('td').parent('tr');
		var val = $("select", tr).val();
		var nextTr = tr.next('tr');
		if(nextTr.html() != null) {	//存在，说明不是最后一行记录
			if(tr.prev('tr').attr('id') == 'title') {	// 第一行和第二行交换
				val = $("select", nextTr).val();	//获取模式类型
				exchange(tr, nextTr);
			}
			var row = $("<tr class='ui-widget-content jqgrow ui-row-ltr' tabindex='-1' conType='"+$(tr).attr('conType')+"' role='row' id='ok'>"+tr.html()+"</tr>");
			$("select", row).val(val);
			nextTr.after(row);
			tr.remove();
		}
		$('button[wrType=button]', inputCombineForm).wrender();
	}
	
	/**
	 * 交换类型
	 */
	function exchange(source, target) {
		// 交换类型
		var type = $("td[name=type]", source).html();
		$("td[name=type]", source).html($("td[name=type]", target).html());
		$("td[name=type]", target).html(type);
	}
	
	/**
	 * 删除数据
	 */
	function DelSequence(obj) {
		var tr = $(obj).parent('td').parent('tr');
		var prevTr = tr.prev('tr');
		if(prevTr.attr('id') == 'title') {	//如果删除第一条数据
			$("td[name=type]", tr.next('tr')).html("");
		}
		
		var id = $("td[name=segmentId]", tr).html();
		
		var temp = selectedSegment;	//创建一个临时的选中客群列表
		$.each(selectedSegment, function(index, item) {
			if(item == id) {
				temp = temp.splice(index,1);//从客群选中列表中制定的客群
				return ;
			}
		});
		tr.remove();
	}
	
	/**
	 * 开始查询任务
	 */
	$("#calSubChangeBtn", inputCombineForm).on("click", function(){
	    doSave(true);
	});
	
	function valid(rows) {
		var flag = true;
		
		if(rows.length < 2) {
			$.msgBox('info', '', "请选择至少两个客群活动进行计算！");
			flag = false;
		}
		
		if(rows.length > 5) {
			$.msgBox('info', '', "请选择最多五个客群活动进行计算！");
			flag = false;
		}
		
		$.each(rows, function(index, item) {
			if($(item).find("select[name=combineType]").val() == '--') {
				$.msgBox('info', '', "请选择复合操作！");
				flag = false;
				return ;
			}
		});
		
		return flag;
	}
	
	/**
	 * 存储客群活动
	 * isCal为true代表当前存储成功后进行轮询检查计算得失情况
	 */
	function doSave(isCal) {
		var rows = $("tr:not([id=title]):not([id=template])", segments);
		// 校验行数据
		if(!valid(rows)) {
			return ;
		}
		
		var schemeData = getValidatedData();
		
		//alert(schemeData);
		if(schemeData != null) {	//校验失败
			if(schemeData != "") {
				
    			$.ajax({
					url: (isNew) ? insertUrl : updateUrl,
					data: schemeData,
					success: function(result) {
						if(result.level == 'ERROR' || result.level == 'WARNING') {
							$.msgBox('error', '', result.message);
						} else {
							if(isCal) {		//开始计算复合客群差值
								//alert('timer');
								var rows = $("tr[conType=new]", segments);	//获取添加的客群记录
								if(rows && rows.length > 0) {
									$.each(rows, function(index, item) {
										$(item).attr('conType', 'edit');
									});
								}
								var segmentId = ""; 
								if(isNew) {	//新添加的复合客群，客群ID是新生成的在message中给出
									var obj = eval("("+result.message+")");
									segmentId = obj.segmentId;
									$('[name=segmentId]', inputCombineForm).val(segmentId);
								} else {	//重新编辑的已经有存在的复合客群ID
									var obj = eval('('+schemeData+')');
									segmentId = obj.segmentId;
								}
								countAlterCheck(segmentId);	//开始轮询
							} else {
								// 关闭编辑框
								var message = "";
								if(isNew) {	//新添加的复合客群，客群ID是新生成的在message中给出
									var obj = eval("("+result.message+")");
									message = obj.message;
								} else {
									message = result.message;
								}
								commonMothed.closeDialog(message, result.version);
							}
						}
					}
	    		});
    		} else {
    			$.msgBox('error', '', "操作失败");
    		}
		}
	}
	
	/**
	 * 获取子客群数据
	 */
	function getValidatedData() {
		if(combineAction.validator.form() == false) {
	    	combineAction.validator.focusInvalid();
	        return null;
	    }
		
		var schemeData = "";
		var voData = $('input[name=name]', inputCombineForm).val();
		var controlRate = $('select[name=controlCountRate]', inputCombineForm).val();
		var voSegmentId =$('input[name=segmentId]', inputCombineForm).val();
		var voVersion =$('input[name=version]', inputCombineForm).val();
		var rows = $("tr:not([id=title]):not([id=template])", segments);	//获取添加的客群记录
		if(rows && rows.length > 0) {
			var ids = "";		//客群计算ID
			var types = "";		//客群操作类型
			var conTypes = "";	//客群记录类型（新加或修改）
			$.each(rows, function(i, item) {
				var id = $("td[name=segmentId]", item).html();	//客群活动ID
				var type = $("select", item).val();		//操作类型
				if(!type) {
					type = "";
				}
				conTypes += ($(item).attr('conType')+"|");
				ids += (id+"|");
				types += (type+"|");
			});
			schemeData = '{"ids":"'+ids+'","types":"'+types+'","conTypes":"'+conTypes+'","segmentId":"'+voSegmentId+'","name":"'+voData+'","controlRate":"'+controlRate+'","version":"'+voVersion+'"}';
		}
		//alert("json="+schemeData);
		return "json="+schemeData;
	}
	
	/**
	 * 复合客群计算获得失去		采用计时器每隔2.5秒执行一次
	 */
	function countAlterCheck(segmentId) {
		// 确认关闭定时器
		closeTimer();
		
		if(isNew) {	// 如果是新创建的在点击保存后会自动提交进行运算
			// 如果是计算得失，保存按钮按编辑方式实现
			isNew = false;
			// 直接查询状态更新
			countAlterTimer = setInterval(countAlterInterval, 2500);
		} else {	// 如果是编辑时，调起执行状态
			$.ajax({
				async: false,
				url: calCountUrl,
				data: 'segmentId=' + segmentId,
				success: function(result) {
					if(result.level == 'INFO') {
						//发起客群计算任务成功
						$('[wrType]', inputCombineForm).wrender('setValue', {calCount:'计算中',calCountTime:''});
						countAlterTimer = setInterval(countAlterInterval, 2500);
					} else {
						//发起客群计算任务失败
						$('[wrType]', inputCombineForm).wrender('setValue', {calCount:'计算失败',calCountTime:''});
						showMsgBox('error', result.message);
					}
				}
			});
		}
	}
	
	function countAlterInterval() {
		dialogCal.dialog('open');
		
		var segmentId = $('[name=segmentId]', inputCombineForm).val();
		//alert(segmentId);
		$.ajax({
			url: updateCountAlter,
			data: 'segmentId=' + segmentId,
			error: function(result) {
				showMsgBox('error', "系统错误，计算失败！");
			},
			success: function(result) {
				if(result.level == 'ERROR') {	//复合客群计算失败
					showMsgBox('error', result.message);
				} else {
					var isStop = false;
					var obj = eval(result.message);
					// 子客群有执行失败的	失败
					$.each(obj, function(index, item) {
						// 获取子客群执行状态
						if(item.STATUS_SUB == '40') {	//计算失败
							showMsgBox('error', '有计算失败的子客群存在无法进行计算！');
							isStop = true;
							return ;
						} /*else if(item.STATUS_SUB == '10') {	//未计算
							showMsgBox('error', '有尚未开始的子客群存在无法进行计算！');
							isStop = true;
							return ;
						} */
						else if(item.STATUS_SUB != '30') {		//除上面的状态以外，如果不等于完成就不向下校验
							isStop = true;
							return ;
						}//if(item.STATUS_SUB != '20') {	//有没执行完的子客群等待，并不对主客群进行判断
					});
					
					// 只有所有子客群状态都为20->执行完成后，在进行下面的主客群方面的验证
					if(isStop == false) {
						//子客群全部执行成功过	再判断主客群是否成功
						//主客群成功	成功	更新差值
						//主客群失败	失败
						if(obj[0].STATUS == '40') {	//失败
							showMsgBox('error', '复合客群计算失败！');
						} /*else if(obj[0].STATUS == '10'){ //尚未计算
							showMsgBox('error', '复合客群尚未开始计算！');
						} */
						else if(obj[0].STATUS == '30') {	//成功
							closeTimer();
							
							var rows = $("tr:not([id=title]):not([id=template])", segments);
							$.each(rows, function(index, item) {
								for(var i=0,len=obj.length;i<len;i++) {
									if($('td[name=segmentId]', item).html() == obj[i].SUB_SEGMENT_ID) {
										if(index != 0 && obj[i].COUNT_ALTER != -1) {	//子客群第一个永远为-1,以它为基准进行复合计算
											if(obj[i].SET_RELATION == 'UNION') {
												$("td[name=countalter]", item).html('获得 '+obj[i].COUNT_ALTER);
											} else if(obj[i].SET_RELATION == 'INTERSECT' || obj[i].SET_RELATION == 'MINUS') {
												$("td[name=countalter]", item).html('失去 '+obj[i].COUNT_ALTER);
											}
											//$('td[name=countalter]', item).html(obj[i].COUNT_ALTER);
											break;
										}
									}
								}
							});
							$('[wrType]', inputCombineForm).wrender('setValue', {code: obj[0].CODE,calCount: obj[0].CAL_COUNT,controlCount: obj[0].CONTROL_COUNT, calCountTime: obj[0].CAL_COUNT_TIME});
							showMsgBox('info', '复合客群计算完成！');
						}
					}
				}
			}
		});
		
	}
	
	
	function showMsgBox(type, message) {
		// 关闭计算得失对话框
		dialogCal.dialog('close');
		$.msgBox(type, '', message);
		closeTimer();
	}
	
	function closeTimer() {
		// 清除计时器
		if(countAlterTimer != null) {
			clearInterval(countAlterTimer);
			countAlterTimer = null;
		}
	}
	
	// form表单校验
	combineAction.validator = inputCombineForm.validate({
		rules: {
			'name':{required:true, maxlength:20}
		}
	});
	
	function viewStatus(result) {
		if(result.status == '20') {
			result.calCount = '计算中...';
			result.calCountTime = '';
			result.controlCount = '0';
		}
		else if(result.status == '10') {//测试时使用，表示该客群计算被重置
			result.calCount = '未计算';
			result.calCountTime = '';
			result.controlCount = '0';
		}
		else if(result.status == '40') {
			result.calCount = '计算失败！';
			result.calCountTime = '';
			result.controlCount = '0';
		}
	}
	
	/**
	 * 编辑复合客群	type edit编辑方式打开	view查看方式打开
	 */
	combineAction.openDialog = function(segmentId, type) {
		$('select,input[name=name]', inputCombineDialog).wrender('toReadOnly', null, true);
		// 加载已有的客群活动
		if(segmentId != null) {
			//将复合客群名称编辑框定义为不可编辑
			isNew = false;
			$.ajax({
				url: getSegments,
				data: 'segmentId=' + segmentId,
				success: function(result) {
					viewStatus(result);
					//if(result.calCount == -1) {result.calCount='未计算'}
					/*if(result.calCount == -1 && result.segmentCalCount != -1) {	//说明子客群已经计算完成但是在复合客群中存储的子客群计算字段还没有更新，因此在此处回显示处理一下（注：实际更新操作在复合客群计算中完成）
						result.calCount = result.segmentCalCount;			
					}*/
					$('[wrType]', inputCombineForm).wrender('setValue', result);
					$.each(result.combineSegmentsList,function(index, item){
						addSegment(item, index, type);
					});
					renderBtn(inputCombineForm);
				}
			});
		} else {
			isNew = true;
			$('select,input[name=name]', inputCombineDialog).wrender('toReadOnly', null, false);
		}
		
		if('view' == type) {
			viewDialog();
		} else {
			$('select', inputCombineDialog).wrender('toReadOnly', null, false);
			inputDialog();
		}
		
		combineAction.inputCombineSegmentDialog.dialog('open');
	}
	
	/**
	 * 是否隐藏复合客群操作按钮
	 */
	combineAction.combineSegmentBtn = function(isHide) {
		$("#compositeSegmentItem", inputCombineForm).css('display',isHide?'none':'');	//隐藏客群活动按钮
		$("#calSubChangeBtn", inputCombineForm).css('display',isHide?'none':'');		//隐藏计算得失按钮
	}
	
});


	
