<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.aggrepoint.com/adk" prefix="adk"%>
<%@ taglib tagdir="/WEB-INF/tags/adk/html/input" prefix="input"%>
<%@ taglib tagdir="/WEB-INF/tags/ex" prefix="ex"%>
<adk:form name="frmCancelTargetEdit" action="cancelTargetEdit">
	<adk:func name="cancelTargetEdit" submit="yes" />
</adk:form>
<adk:form name="frmSelSegment" action="selSegment">
	<input type="hidden" name="propertyName"/>
	<adk:func name="selSegment" submit="yes" param="propertyName"/>
</adk:form>
<adk:form name="frmLockedSegment" action="lockedSegment">
	<input type="hidden" name="maxCount" />
	<input type="hidden" name="controlCount" />
	<adk:func name="lockedSegment" param="maxCount,controlCount" submit="yes" />
</adk:form>
<adk:form name="frmUnLockedSegment" action="unlockedSegment">
	<adk:func name="unlockedSegment" submit="yes" />
</adk:form>
<adk:form name="frmResetSegmentCount" action="resetSegmentCount">
	<adk:func name="resetSegmentCount" submit="yes" />
</adk:form>
<adk:form name="frmRefreshSegment" action="refreshSegment">
	<adk:func name="refreshSegment" submit="yes" />
</adk:form>
<adk:form name="frmSaveTargetEdit" action="saveTargetEdit" vaction="saveTargetEdit"
	resetref="${actTarget}">
	<input type="hidden" name="status" />
	<adk:func name="saveEdit" submit="yes" param="status"/>
		<div class="table" style="max-height: 500px;overflow: auto;">
		<div style="border-top:solid 1px #e7e7e7; border-left:solid 1px #e7e7e7; border-right:solid 1px #e7e7e7;" class="dhead">
		  <div style="background-color:#f8f8f8;  font-weight:bold;  padding:4px 10px;  " class="dtitle">
		  	目标选择 &nbsp;&nbsp;
		  	<c:choose>
		  		<c:when test="${actTarget.status eq m.ACTIVITY_ACT_TARGET_PLAN}">
						<span id="lockedButton"
							style="display:${adk:ifelse((actTarget.targetType eq m.TARGET_TYPE_SEGMENT and actTarget.tSegment.calCount gt 0) or (actTarget.targetType eq m.TARGET_TYPE_EXCEL and uploadFile.status eq 'S'),'','none')}">
							<button type="button" class="btn locked" id="lockedSegment">冻结客群</button>
						</span>
		  		</c:when>
		  		<c:when test="${actTarget.status eq m.ACTIVITY_ACT_TARGET_LOCKED}">
		  			<button type="button" class="btn unlocked" onclick="${adk:func('unlockedSegment')}()">解冻客群</button>
		  		</c:when>
		  	</c:choose>
		  </div>
		  	<c:choose>
				 <c:when test="${actTarget.status eq m.ACTIVITY_ACT_TARGET_PLAN}">
					  <input type="radio" name="targetType" validate="validate" value="${m.TARGET_TYPE_SEGMENT}" <c:if test="${actTarget.targetType eq m.TARGET_TYPE_SEGMENT}">checked="checked"</c:if>  />${DIMS[m.DIMTYPE_TARGET_TYPE][m.TARGET_TYPE_SEGMENT]}
					  <input type="radio" name="targetType" validate="validate" value="${m.TARGET_TYPE_EXCEL}" <c:if test="${actTarget.targetType eq m.TARGET_TYPE_EXCEL}">checked="checked"</c:if>/>${DIMS[m.DIMTYPE_TARGET_TYPE][m.TARGET_TYPE_EXCEL]}
				 </c:when>
				<c:otherwise>
					  ${DIMS[m.DIMTYPE_TARGET_TYPE][m.editCmnActivity.tActTarget.targetType]}
				</c:otherwise>
			</c:choose>
		</div>
		<div id="segment" style="display:${adk:ifelse(actTarget.targetType eq m.TARGET_TYPE_SEGMENT,'block','none')}">
		<table border="0" style="width: 100%;" >
			<tbody>
				<tr>
					<td align="right" nowrap="nowrap" width="100">指定客群:</td>
					<td align="left" nowrap="nowrap"  width="250">
						${actTarget.tSegment.name}
						<c:if test="${actTarget.status eq m.ACTIVITY_ACT_TARGET_PLAN}">
						<button type="button" class="btn search notext" onclick="${adk:func('selSegment')}('${m.targetSegment}')">选择客群</button>
						</c:if>
					</td>
					<td align="right" nowrap="nowrap"  width="100">总数:</td>
					<td align="left" nowrap="nowrap" width="250">
						<c:if test="${not empty actTarget.tSegment}">
						<c:choose>
							<c:when test="${actTarget.status eq m.ACTIVITY_ACT_TARGET_PLAN}">
								<input type="hidden" id="segmentCount" value="${actTarget.tSegment.calCount}">
								<span id="segmentCountHtml">
								${adk:ifelse(actTarget.tSegment.calCount lt 0,'未计算',actTarget.tSegment.calCount) }
								</span>
							</c:when>
							<c:otherwise>
								${actTarget.totalCount}
							</c:otherwise>
						</c:choose>
						 (计算时间:
						 <span id="calCountTime">
						 <fmt:formatDate value="${actTarget.tSegment.calCountTime}" pattern="yyyy-MM-dd HH:mm:ss" />
						 </span>
						 )
						</c:if>
						<c:if test="${not empty actTarget.tSegment and actTarget.status eq m.ACTIVITY_ACT_TARGET_PLAN}">
							<span id="calculateButton">
							<button type="button" class="btn reset" id="onCalCountBtn">重新计算</button>
							</span>
						</c:if>
					</td>
				</tr>
			</tbody>
		</table>
		</div>
		
		<div id="excel" style="display:${adk:ifelse(actTarget.targetType eq m.TARGET_TYPE_EXCEL,'block','none')}">
		<table border="0" style="width: 100%;" >
			<tbody>
				<tr>
					<td align="right" nowrap="nowrap" width="100">上传导入文件:</td>
					<td align="left" nowrap="nowrap"  width="250">
						<c:choose>
							<c:when test="${empty uploadFile or uploadFile.delete}">
				                <input id="fileupload" type="file" name="file" data-url="${adk:resproxy('doUpload')}" multiple/>
								<div id="fileuploadDiv">(文件的第一列为手机号，且不能有空行)</div>
							</c:when>
							<c:otherwise>
								<input id="fileupload" type="file" name="file" data-url="${adk:resproxy('doUpload')}" multiple style="display:none"/>
								<div id="fileuploadDiv">
									<c:if test="${not empty uploadFile}">
									<a href="${adk:resproxy('doDownload')}" target="_blank">
										${uploadFile.fileName}&nbsp;&nbsp;[${uploadFile.fileSize}]
									</a>
									</c:if>
									<c:if test="${uploadFile.status ne 'P' and actTarget.status eq m.ACTIVITY_ACT_TARGET_PLAN}">
										<button type="button" class="btn clear notext delfile">删除</button>
									</c:if>
									<c:choose>
										<c:when test="${uploadFile.status eq 'P'}">(处理中...)</c:when>
										<c:when test="${uploadFile.status eq 'E'}"><br/>(文件处理异常,错误信息请查看文件管理)</c:when>
									</c:choose>
								</div>
							</c:otherwise>
						</c:choose>
					</td>
					<td align="right" nowrap="nowrap"  width="100">总数:</td>
					<td align="left" nowrap="nowrap">
						<input type="hidden" id="excelMemberCount" value="${actTarget.totalCount}">
						<span id="excelCountHtml">
							${actTarget.totalCount}
						</span>
					</td>
				</tr>
			</tbody>
		</table>
		</div>
		<table border="0" style="width: 100%;" >
			<tbody>
				<tr>
					<td align="right" nowrap="nowrap" width="100">受众数量:</td>
					<td align="left" nowrap="nowrap"  width="250">
						<c:choose>
							<c:when test="${actTarget.status eq m.ACTIVITY_ACT_TARGET_PLAN}">
								<input:number id="maxCount" name="maxCount" error="请输入正确的数值" validate="validate" class="txtinput_wid80" value="${actTarget.maxCount}" mandatory="yes" />
							</c:when>
							<c:otherwise>
								${actTarget.maxCount}
							</c:otherwise>
						</c:choose>
					</td>
					<td align="right" nowrap="nowrap"  width="100">控制组数量:</td>
					<td align="left" nowrap="nowrap" width="250">
						<c:choose>
							<c:when test="${actTarget.status eq m.ACTIVITY_ACT_TARGET_PLAN}">
								<input:number id="controlCount" name="controlCount" error="请输入正确的数值" validate="validate" class="txtinput_wid80" value="${actTarget.controlCount}" mandatory="yes"/>
							</c:when>
							<c:otherwise>
								${actTarget.controlCount}
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</tbody>
		</table>
		</div>
</adk:form>

<div align="center" style="margin: 10px 0 0 0">
	<c:if test="${actTarget.status eq m.ACTIVITY_ACT_TARGET_PLAN}">
	<span id="save">
		<button type="button" class="btn save"
			onclick="${adk:func('saveEdit')}('${m.CAMPAINGN_STATUS_PLAN }')">保存</button>
	</span>
	</c:if>
		<button type="button" class="btn cancel"
			onclick="${adk:func('cancelTargetEdit')}()">取消</button>
</div>
<div id="calCountDialog" style="display:none">
	<div>正在计算....</div>
</div>
<script type="text/javascript">
$(function(){
	var inputForm = document.forms["frmSaveTargetEdit${adk:encodens(frmSaveTargetEdit)}"];
	var calCountDialog = $('#calCountDialog');
	var calCountUrl = "${adk:resproxy('resetSegmentCount')}";
	var getCountUrl = "${adk:resproxy('refreshSegment')}";
	var calCountTimerId = null; //calculating timer id
	
	calCountDialog.dialog({
		width: 500,
		autoOpen:false,
		buttons: [
			{id:'onCloseBtn', text:'关闭后台执行', click: function() { $(this).dialog('close'); }}
		],
		beforeClose: function(event, ui) {
			//Clear timer and reset the calCountTimerId to be null
			if(calCountTimerId != null) {
				clearInterval(calCountTimerId);
				calCountTimerId = null;
			}
		}
	});
	$('#onCalCountBtn', inputForm).click(function() {
		setCalCount(-1, "", false);
		calCountDialog.dialog('open');
		$.ajax({
			   async: false,
			   url: calCountUrl,
			   success: function(result){
				   //$('span[name=calCount]', inputForm).wrender('setValue', {calCount: '计算中...'});
				   //$('span[name=calCountTime]', inputForm).wrender('setValue', {calCountTime: result.calCountTime});
				   calCountTimerId = setInterval(retrieveCalCount, 1000);
			   }
		});
	});
	
	function retrieveCalCount() {
		$.ajax({
			url: getCountUrl,
			success: function(result){
				if(result.calCount >= 0) {
					//Clear timer and reset the calCountTimerId to be null
					if(calCountTimerId != null) {
						clearInterval(calCountTimerId);
						calCountTimerId = null;
					}
					setCalCount(result.calCount, result.calCountTime, true);
					calCountDialog.dialog('close');
					/* $.msgBox('info', '', '客群数量计算完成！ 客群数量为：' + result.calCount); */
				}
			}
		});
	}
	
	function setCalCount(count, calCountTime, showRow) {
		$("#segmentCount").val(count);
		if(count == -1){
			$("#segmentCountHtml").html("计算中...");
		}else{
			$("#segmentCountHtml").html(count);
		}
		$("#calCountTime").html(calCountTime);
		
		if(showRow){
			$("#calculateButton").css("display","");
			if(count > 0){
				$("#lockedButton").css("display","");
			}
		}else{
			$("#calculateButton").css("display","none");
			
			$("#lockedButton").css("display","none");
		}
	}
	
	
	$('#fileupload').fileupload({
		dataType: 'json',
		add: function(e, data) {
			$('#fileupload').hide();
			$("#excelCountHtml").html("");
			$("#fileuploadDiv p").remove();
			$("#fileuploadDiv").html("");
			data.context = $('<p/>').text('Uploading...').appendTo($("#fileuploadDiv"));
			data.submit();
		},
		fail: function(e, data) {
			$("#fileuploadDiv p").remove();
			data.context = $('<p/>').text('Upload failed:' + data.errorThrown).appendTo($("#fileuploadDiv"));
		},
		done: function(e, data) {
			//data.context.text('Upload finished.');
			$("#fileuploadDiv p").remove();
			$("#fileuploadDiv").html(data.result[0].name);
			if(data.result[0].error){
				$('#fileupload').show();
				$('#save').hide();
			}else{
				$('#fileupload').hide();
				$("#fileuploadDiv").append('<button type="button" class="btn clear notext delfile">删除</button>');
				if(data.result[0].number > 0){
					$('#save').show();
					$("#fileuploadDiv").append("<button type='button' class='btn moveup notext upfile'>上传</button>");
				}else{
					$('#save').hide();
					$("#fileuploadDiv").append("<br/>(请重新选择文件，该文件中没有联系人)");
				}
				
			}
			$("#excelCountHtml").html(data.result[0].number);
			$("#excelMemberCount").val(data.result[0].number);
			$('.delfile').click(function() {
				$("#excelCountHtml").html("");
				$("#lockedButton").css("display","none");
				$.ajax({
					url: "${adk:resproxy('doUploadDel')}",
					data: {
						'fileId': ''
					},
					// 请求参数，json格式
					success: function(data) {
						$("#fileuploadDiv").html("(文件的第一列为手机号，且不能有空行)");
						$('#fileupload').show();
					}
				});
			});
			$('.upfile').click(function() {
				var delbtn = $(this);
				uploadFile();
			});
			data.context = $("#fileuploadDiv").html();
			renderButtons();
		}
	});
	$('.delfile').click(function() {
		$("#excelCountHtml").html("");
		$("#lockedButton").css("display","none");
		$.ajax({
			url: "${adk:resproxy('doUploadDel')}",
			success: function(data) {
				$("#fileuploadDiv").html("(文件的第一列为手机号，且不能有空行)");
				$('#fileupload').show();
			}
		});
	});
	$('.upfile', inputForm).click(function() {
		uploadFile();
	});
	//上传文件
	function uploadFile(){
		$('.delfile').hide();
		$('.upfile').remove();
		calCountDialog.html("正在处理...");
		calCountDialog.dialog('open');
		$.ajax({
			   async: false,
			   url: "${adk:resproxy('doUploadFile')}",
			   success: function(result){
				   calCountTimerId = setInterval(retrieveExcelCount, 1000);
			   }
		});
	}
	function retrieveExcelCount() {
		$.ajax({
			url: "${adk:resproxy('getUploadFileStatus')}",
			success: function(result){
				if(result.status != "P") {
					//Clear timer and reset the calCountTimerId to be null
					$('.delfile').show();
					if(calCountTimerId != null) {
						clearInterval(calCountTimerId);
						calCountTimerId = null;
					}
					if(result.status == "S"){
						$("#lockedButton").css("display","");
						if(result.calCount < $("#excelMemberCount").val()){
							$("#excelCountHtml").html(result.calCount+"(文件中有重复手机号，少于预期值"+$("#excelMemberCount").val()+")");
							$("#excelMemberCount").val(result.calCount);
						}
					}else if(result.status == "E"){
						$("#fileuploadDiv").append("<br/>(文件处理异常,错误信息请查看文件管理)");
					}
					calCountDialog.dialog('close');
					/* $.msgBox('info', '', '客群数量计算完成！ 客群数量为：' + result.calCount); */
				}
			}
		});
	}
	
	
	$("#lockedSegment").click(function(){
		var maxCount = $("#maxCount").val();
		var controlCount = $("#controlCount").val();
		var totalCount ;
		var targetType = $("input[type=radio][name=targetType][checked=checked]",inputForm).val();
		if(targetType == "${m.TARGET_TYPE_SEGMENT}"){
			totalCount = $("#segmentCount").val();
		}else if(targetType == "${m.TARGET_TYPE_EXCEL}"){
			totalCount = $("#excelMemberCount").val();
		}
		
		var suggestCount = Math.round(maxCount/10);
		if(Number(maxCount) <= 0){
			$("<div title='导出数据提示'>您设置的受众数量小于等于0，请重新设置。</div>").dialog({
				resizable: false,
				height:140,
				width:480,
				modal: true,
				buttons: 
				     {
					"确定": function() {
						$( this ).dialog( "close" );
					}
				}
			});	
		}else if(Number(controlCount) <0){
			$("<div title='导出数据提示'>您设置的控制组数量小于0，请重新设置。</div>").dialog({
				resizable: false,
				height:140,
				width:480,
				modal: true,
				buttons: 
				     {
					"确定": function() {
						$( this ).dialog( "close" );
					}
				}
			});	
		}else if(Number(maxCount)+Number(controlCount) > totalCount){
			$("<div title='导出数据提示'>您设置的受众数量和控制组数量超过筛选数量总和，请重新设置。</div>").dialog({
				resizable: false,
				height:140,
				width:480,
				modal: true,
				buttons: 
				     {
					"确定": function() {
						$( this ).dialog( "close" );
					}
				}
			});	
		}else if(Number(maxCount)+Number(controlCount) <= totalCount){
			if(controlCount == suggestCount){
				${adk:func('lockedSegment')}(maxCount,controlCount);
			}else if(controlCount < suggestCount){
				$("<div title='冻结提示'>您设置的控制组会员数量过少，不足受众群体的10%，样本数量较少会影响到活动对比效果的评估。<font color='red'>请确认是否需要重新设置?</font></div>").dialog({
					resizable: false,
					height:140,
					width:480,
					modal: true,
					buttons: 
					     {
						"冻结": function() {
							$( this ).dialog( "close" );
							${adk:func('lockedSegment')}(maxCount,controlCount);
						},
						"重新设置": function() {
							$( this ).dialog( "close" );
						}
						
					}
				});	
			}else if(controlCount > suggestCount){
				$("<div title='冻结提示'>您设置的控制组会员数量过多，已经超过受众群体的10%，这样会影响到活动对比效果的评估。<font color='red'>请确认是否需要重新设置?</font></div>").dialog({
					resizable: false,
					height:140,
					width:480,
					modal: true,
					buttons: 
					     {
						"冻结": function() {
							$( this ).dialog( "close" );
							${adk:func('lockedSegment')}(maxCount,controlCount);
						},
						"重新设置": function() {
							$( this ).dialog( "close" );
						}
						
					}
				});	
			}
		}else if (maxCount == totalCount && (controlCount == null || controlCount == 0)){
			$("<div title='冻结提示'>您设置活动控制组数量为0，这将会影响到活动结束后的对比效果的评估，请问您是否冻结</div>").dialog({
				resizable: false,
				height:140,
				width:480,
				modal: true,
				buttons: 
				     {
					"冻结": function() {
						$( this ).dialog( "close" );
						${adk:func('lockedSegment')}(maxCount,controlCount);
					},
					"重新设置": function() {
						$( this ).dialog( "close" );
					}
				}
			});	
		}else {
			${adk:func('lockedSegment')}(maxCount,controlCount);
		}
	});
	
	$("input[type=radio][name=targetType]",inputForm).click(function(){
		if($(this).val() == "${m.TARGET_TYPE_SEGMENT}"){
			$('#segment').css('display','block');
			$('#excel').css('display','none');
		}else if($(this).val() == "${m.TARGET_TYPE_EXCEL}"){
			$('#segment').css('display','none');
			$('#excel').css('display','block');
		}
		$.ajax({
			url: "${adk:resproxy('setTargetType')}",
			data: {
				'targetType': $(this).val()
			}
		});
	});
});
</script>

