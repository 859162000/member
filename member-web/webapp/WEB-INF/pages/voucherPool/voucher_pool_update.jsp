<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.aggrepoint.com/adk" prefix="adk"%>
<%@ taglib tagdir="/WEB-INF/tags/ex" prefix="ex"%>
<%@ taglib tagdir="/WEB-INF/tags/adk/html/input" prefix="input"%>

<adk:form name="frmSelectInfo"
	action="doSelect">
	<adk:func name="doSelect" submit="yes" />
</adk:form> 
<adk:form name="frmDownLoad" action="doDownLoadTemp">
	<adk:func name="doDownLoadTemp" submit="yes"/>
</adk:form>
<adk:form name="frmCancelEdit" action="cancelEdit">
	<adk:func name="cancelEdit" submit="yes" />
</adk:form>
<adk:form name="frmUpLoadFile" action="saveUpLoadFile">
	<adk:func name="saveUpLoadFile" submit="yes" />
</adk:form>
<adk:form name="frmSelectTicket" action="doSelectTicket">
	<adk:func name="doSelectTicket" submit="yes" />
</adk:form>
<adk:include var="list" view="ticketList" winlet="${m.voucherPoolDetailLet}">t</adk:include>
<adk:form name="frmSaveEdit" action="saveEdit" vaction="saveEdit" resetref="${m.tVoucherPool}">
	<adk:func name="saveEdit" submit="yes" />
<div style="min-width: 650px">
	<div class="adk_tab2">
		<ul id="tab" style="cursor: pointer;">
			<li name="base" id="current">
				<a id="tab_base"><span>库券信息</span></a>
			</li>
		</ul>
	</div>
				<!-- 券库信息 -->
			<div id="div_base" style="max-height: 500px;overflow: auto;" class="adk_tab2box">
				<table id="basetable" width="100%" bordercolor="#CCCCCC" border="1" style=" background-color:#fbfbfb">
					<tbody>
						<tr>
							<td align="right" width="25%" nowrap="nowrap">券库名称:</td>
							<td align="left" width="25%" nowrap="nowrap">
								<input:text class="txtinput_wid80" name="name" property="tVoucherPool.name"  validate="validate" mandatory="yes"/>
							</td>
							<td align="right" width="25%" nowrap="nowrap">券库编码:</td>
							<td align="left" width="25%" nowrap="nowrap">
								${adk:ifelse(empty m.tVoucherPool.id, '系统自动生成',m.tVoucherPool.id)}
							</td>
						</tr>
						<tr>
							<td align="right" nowrap="nowrap">发送锁定:</td>
							<td align="left" nowrap="nowrap">
								${adk:ifelse(empty m.tVoucherPool.sendLock, '',m.tVoucherPool.sendLock)}
							</td>
						</tr>
					</tbody>
				</table>
		</div>
		</div>
<div style="min-width: 650px">
	<div class="adk_tab2">
		<ul id="tab" style="cursor: pointer;">
			<li name="address">
				<a id="tab_address"><span>券信息</span></a>
			</li>
			<li name="info">
				<a id="tab_info"><span>导入券信息</span></a>
			</li>
		</ul>
	</div>
	<!-- 券信息 -->
			<div id="div_address" style="max-height: 500px;overflow: auto;" class="adk_tab2box">
				<table id="baseaddresstable" width="100%" bordercolor="#CCCCCC" border="1" style=" background-color:#fbfbfb">
					<tbody>
						<tr>
							<td align="center" nowrap="nowrap">
								<button type="button" class="btn save" id="dowmLoadTemp">导入模板下载</button>
							</td>
							<td align="center" nowrap="nowrap">
								<c:choose>
									<c:when test="${empty m.uploadFile}">
										<input id="fileupload" type="file" name="files[]" data-url="${adk:resproxy('doUpload')}" multiple onclick="return confirm('本次操作将导致您上次导入内容被删除,是否继续?')"/>
									<div id="fileuploadDiv"></div>
									</c:when>
									<c:otherwise>
										<input id="fileupload" type="file" name="files[]" data-url="${adk:resproxy('doUpload')}" multiple style="display:none" onclick="return confirm('本次操作将导致您上次导入内容被删除,是否继续?')"/>
										<div id="fileuploadDiv">
										<button type="button" class="btn clear notext delfile">删除</button>
									</div>
									</c:otherwise>
								</c:choose>
								<button type="button" id="import" class="btn save">导入</button>
							</td>
						</tr>
						<tr>
							<td align="center" nowrap="nowrap" id="importNumber">
								<strong>导入券数量:</strong>
							</td>
						</tr>
				</tbody>
				</table>
			</div>
		</div>
		</adk:form>
		<c:if test="${m.importOk == true}">
			<div id="div_info" style="max-height: 500px;overflow: auto; display : none" class="adk_tab2box">
				${list}
			</div>
		</c:if>
		<div align="center" style="margin:10px 0 0 0">
			
			<button type="button" class="btn save" onclick="${adk:func('saveEdit')}()">保存</button>
		
			<button type="button" class="btn close"	onclick="${adk:func('cancelEdit')}()">取消</button>
		</div>

<script language="javascript"><!--
$("#tab_address").click(function(){
	$("#div_address").css('display', 'block');
	$("#div_info").css('display', 'none');
	$("#tab li[name='address']").attr("id","current");
	$("#tab li[name='info']").attr("id","");
});
$("#tab_info").click(function(){
	$("#div_address").css('display', 'none');
	$("#div_info").css('display', 'block');
	$("#tab li[name='address']").attr("id","");
	$("#tab li[name='info']").attr("id","current");
});
$("#import").click(function() {
	$("b").hide();
	$("#fileuploadDiv").after("<b>处理中,请稍后...</b>");
	${adk:func('saveUpLoadFile')}();
	$("b").hide();
	$("#fileuploadDiv").after("<b>文件导入成功</b>");
	
});
$("#dowmLoadTemp").click(function() {
	var url = "${adk:resproxy('doDownLoadTemp')}";
	//window.open(url);火狐浏览器可以使用
	window.location = url;
});
$("#import").hide();
$('#fileupload').fileupload({
	  dataType: 'json',
	  add: function (e, data) {
	  	$("#fileuploadDiv p").remove();
	      data.context = $('<p/>').text('文件读取中，请稍后 ...').appendTo($("#fileuploadDiv"));
	      data.submit();
	  },
	  success: function(e, data) {
		$("#import").show();
		$("#fileuploadDiv").after("<b>该文件等待处理...</b>");
	  },
	  fail: function (e, data) {
	  	$("#fileuploadDiv p").remove();
	  	data.context = $('<p/>').text('Upload failed:'+data.errorThrown).appendTo($("#fileuploadDiv"));
	  },
	  done: function (e, data) {
	      //data.context.text('Upload finished.');
	      $("#fileuploadDiv p").remove();
	      $("#fileuploadDiv").append($('<a></a>').text(data.result[0].name)).append('<button type="button" class="btn clear notext delfile">删除</button>');
	      $('.delfile').click(function(){
	      	$.ajax({
					url : "${adk:resproxy('doUploadDel')}",
					data : {
						'fileId' : ''
					},// 请求参数，json格式
					success : function(data) {
						$("#fileuploadDiv").html("");
			        	$('#fileupload').show();
					}
				});
	      	$("#import").hide();
	      	$("b").hide();
	      });
	      data.context = $("#fileuploadDiv").html();
	      renderButtons();
	      $('#fileupload').hide();
	  }
	});
	$('.delfile').click(function(){
		$.ajax({
			url : "${adk:resproxy('doUploadDel')}",
			success : function(data) {
				$("#fileuploadDiv").html("");
	      	$('#fileupload').show();
			}
		});
	});
</script>
