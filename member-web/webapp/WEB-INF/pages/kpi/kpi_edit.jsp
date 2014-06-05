<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.aggrepoint.com/adk" prefix="adk"%>
<%@ taglib tagdir="/WEB-INF/tags/ex" prefix="ex"%>
<%@ taglib tagdir="/WEB-INF/tags/adk/html/input" prefix="input"%>

<adk:form name="frmCancelEdit" action="cancelEdit">
	<adk:func name="cancelEdit" submit="yes" />
</adk:form>
<div  id="basedata">
			<adk:form name="frmSaveEdit" action="saveEdit" vaction="saveEdit" >
			<input type="hidden" name="id" />
			<adk:func name="saveEdit" param="id" submit="yes" />

				<table width="100%" border="1" >
					<tbody>
						<tr>
							<td width="100" align="right">上传文件:</td>
							<td width="60%" >
						       <c:choose>
									<c:when test="${empty m.uploadFile}">
						                <input id="fileupload" type="file" name="files[]" data-url="${adk:resproxy('doUpload')}" multiple/>
										<div id="fileuploadDiv"></div>
									</c:when>
									<c:otherwise>
										<input id="fileupload" type="file" name="files[]" data-url="${adk:resproxy('doUpload')}" multiple style="display:none"/>
										<div id="fileuploadDiv">
											<button type="button" class="btn clear notext delfile">删除</button>
										</div>
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
			</tbody>
		</table>
		</adk:form>
</div>
<div align="center" style="margin:10px 0 0 0">
	<button type="button" class="btn save" onclick="${adk:func('saveEdit')}()">保存</button>
	<button type="button" class="btn close"	onclick="${adk:func('cancelEdit')}()">关闭</button>
</div>
<script language="javascript">
$('#fileupload').fileupload({
  dataType: 'json',
  add: function (e, data) {
  	$("#fileuploadDiv p").remove();
      data.context = $('<p/>').text('Uploading...Please wait a moment ...').appendTo($("#fileuploadDiv"));
      data.submit();
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