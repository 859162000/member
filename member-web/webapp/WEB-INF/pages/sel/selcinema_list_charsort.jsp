<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.aggrepoint.com/adk" prefix="adk"%>
<%@ taglib tagdir="/WEB-INF/tags/ex" prefix="ex"%>
<%@ taglib tagdir="/WEB-INF/tags/adk/html/input" prefix="input"%>


<adk:form name="frmCloseSelcinemas" action="doClose">
	<adk:func name="doClose" submit="yes" />
</adk:form>


<adk:form name="frmSaveSelcinemas" action="doSave">

	<div id="div_box" class="box_border"
		style="overflow: auto; max-height: 500px; max-width: 1000px; padding: 10px">
		<c:if test="${u.levelName eq 'GROUP'}">
		<div id="div_chkchar" style="float: left; width: 100%">
			<b>按区域首字母缩写选择：</b> 
			<input type='checkbox' id='chk_allchar' value="all" checked="checked"/>所有 
			<input type='checkbox' id='chk_all1' class="chk_chars" value="ABCDE" checked="checked"/>ABCDE
			<input type='checkbox' id='chk_all2' class="chk_chars" value="FGHIJ" checked="checked"/>FGHIJ 
			<input type='checkbox' id='chk_all3' class="chk_chars" value="KLMNO" checked="checked"/>KLMNO 
			<input type='checkbox' id='chk_all4' class="chk_chars" value="PQRST" checked="checked"/>PQRST 
			<input type='checkbox' id='chk_all5' class="chk_chars" value="UVWXYZ" checked="checked"/>UVWXYZ
		</div>
		</c:if>
		
		<c:forEach items="${m.charRegionCodeMap}" var="py">
			<div id="div_area${py.key}">
			<c:if test="${u.levelName eq 'GROUP'}">
			<div id="div_char${py.key}" style="float: left;padding-left: 10px; padding-top: 5px;">
			<a href="#" class="a_char">${py.key}:</a>
			</div>
			</c:if>
			<c:forEach items="${py.value}" var="area">
				<c:if test="${fn:length(m.rMap[area]) > 0}">
					<div style="float: left; width: 96%; margin-left: 30px; margin-top: 5px;background-color: #F8F8F8;">
						<span>
							<b> 
								<a href="#" class='a_rgn char${py.key}'>${DIMS[m.DIMTYPE_AREA][area]}区域:</a>
							</b>
							<c:forEach items="${m.rMap[area]}" var="cinema">
								<c:choose>
									<c:when test="${not empty m.selMap[area] and fn:contains(m.selMap[area],cinema)}">
										<input type='checkbox' name="selcienmas" value="${cinema.id}" checked="checked"/>
										<a href="#" class="a_cinema" style="color: red;">${cinema.shortName}</a>
									</c:when>
									<c:otherwise>
										<input type='checkbox' name="selcienmas" value="${cinema.id}"/>
										<a href="#" class="a_cinema">${cinema.shortName}</a>
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</span>
					</div>
				</c:if>
			</c:forEach>
			</div>
		</c:forEach>
	</div>
	<div align="center">
		<button type="submit" class="btn save">保存</button>
		<button type="button" class="btn close" onclick="${adk:func('doClose')}()">取消</button>
	</div>
</adk:form>
<script language="javascript">
$(".a_char").each(function(){
	$(this).click(function(){
		var checked = $(this).hasClass('checked') ? false : true;
		$(this).parent().parent().find(':checkbox').each(function(){
			$(this).attr('checked',checked);
		});
		
		$(this).parent().parent().find('a').css("color",checked?'red':'');
		
		if(checked)
			$(this).addClass('checked');
		else
			$(this).removeClass('checked');
	});
});
$(".a_cinema").each(function(){
	$(this).click(function(){
		var chk = $(this).prev();
		chk.attr('checked',!chk.attr('checked'));
		$(this).css("color",chk.is(':checked') ? 'red':'');
	});
});
$("[name='selcienmas']").each(function(){
	$(this).click(function(){
		$(this).next('a').css("color",$(this).is(':checked') ? 'red':'');
	});
});
$(".a_rgn").each(function(){
	$(this).click(function(){
		var checked = $(this).hasClass('checked') ? false : true;
		$(this).parent().parent().find(':checkbox').each(function(){
			$(this).attr('checked',checked);
		});
		
		$(this).parent().parent().find('a').css("color",checked?'red':'');
		$(this).css("color",checked?'red':'');
		
		if(checked)
			$(this).addClass('checked');
		else
			$(this).removeClass('checked');
	});
});

$("#chk_allchar").click(function(){
	var chked = $(this).is(':checked');
	$(".chk_chars").each(function(){
		$(this).attr('checked',chked);
		$(this).change();
	});
});
$(".chk_chars").each(function(){
	$(this).change(function(){
		var checked = $(this).is(':checked') ? true : false;
		var chars = $(this).val();
		for(i = 0;i < chars.length;i++){
			if(checked){
				$("#div_area"+chars.charAt(i)).slideDown();
			}else{
				$("#div_area"+chars.charAt(i)).slideUp();
			}
			
		}
	});
});
</script>
