<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.aggrepoint.com/adk" prefix="adk"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib tagdir="/WEB-INF/tags/ex" prefix="ex"%>
<div class="dcontent">
<adk:form name="frmCreateInfo"
	action="doCreate">
	<adk:func name="doCreate" submit="yes" />
</adk:form> 
<adk:form name="frmChangeStatus"
	action="doChangeStatus">
	<input type="hidden" name="str">
	<adk:func name="doChangeStatus" param="str" submit="yes" />
</adk:form> 
<adk:form name="frmEditInfo" action="doEdit">
	<input type="hidden" name="id">
	<adk:func name="doEdit" param="id" submit="yes" />
</adk:form> 
<adk:form name="frmPaging" action="search">
	<adk:func name="search" submit="yes" />
	<input type="hidden" name="pagemember" id="page" />
	<input type="hidden" name="sort" id="sort" />
	<input type="hidden" name="dir" id="dir" />
</adk:form>
<adk:form name="frmView" action="doView">
	<input type="hidden" name="id">
	<adk:func name="doView" param="id" submit="yes" />
</adk:form>
<c:set var="editmem" value="${adk:exec1(u, 'hasRight', 'member.mgmember.info.edit')}" />
<c:set var="deletedmem" value="${adk:exec1(u, 'hasRight', 'member.mgmember.info.deleted')}" />
<c:set var="changestatusmem" value="${adk:exec1(u, 'hasRight', 'member.mgmember.info.status')}" />
<adk:form name="frmMemberList" action="doDelete">
	<input type="hidden" name="id">
	<adk:func name="doDelete" param="id" submit="yes" />
	
	<div>
	<table border="0">
		<tbody>
			<tr>
				<td>
				<c:if test="${editmem }">
				<button type="button" onclick="${adk:func('doCreate')}();"
					class="btn add">添加会员</button>
				</c:if>
				<c:if test="${deletedmem }">
				<button type="submit"
					onclick="if(isSelected('listTable','请选择需要进行删除的数据记录!')){return confirm('确定要删除选中的数据记录吗?')}else{return false;};"
					class="btn del">删除</button>
				</c:if>
				<c:if test="${changestatusmem }">
					<button type="button" id="changeStatusBtn" class="btn edit">修改会员状态</button>
				</c:if>
				</td>
			</tr>
		</tbody>
	</table>
	</div>
	<div class="table" id="listTable"><display:table
		name="pageResult" id="row" htmlId="idTable" cellspacing="0"
		cellpadding="0" style="width:100%" export="false" requestURI="">
		<display:column title="No" style="width:20px;" headerClass="ar"
			class="ar">${pageResult.startIndex+row_rowNum}</display:column>
		<display:column title="<input type='checkbox' id='selectAll'>"
			style="width:20px;" headerClass="ac" class="ac">
			<c:choose>
			<c:when test="${u.levelName eq 'GROUP' and (row.isDeleted == 0)}">
				<input type="checkbox" class="checkbox" name="ids" value="${row.id}" />
			</c:when>
			<c:when test="${u.levelName eq 'REGION' and u.regionCode eq row.tCinema.area and (row.isDeleted == 0)}">
				<input type="checkbox" class="checkbox" name="ids" value="${row.id}" />
			</c:when>
			<c:when test="${u.levelName eq 'CINEMA' and u.cinemaId == row.tCinema.id and (row.isDeleted == 0)}">
				<input type="checkbox" class="checkbox" name="ids" value="${row.id}" />
			</c:when>
			<c:otherwise>
				
			</c:otherwise>
		</c:choose>
		</display:column>
		<display:column title="会员编号" sortProperty="memberNo" sortable="true" class="ac" >
			<a href="javascript:${adk:func('doView')}(${row.id});">${row.memberNo}</a>
		</display:column>
		<display:column sortProperty="c.mobile" sortable="true"  class="ac" title="手机号" value="${row.smobile}"/>
		<display:column sortProperty="c.name" sortable="true"  class="ac" title="会员名称" value="${row.name}"/>
		<display:column  class="ac" title="会员级别" value="${row.tMemberLevel.memLevel}星"/>
		<display:column  sortProperty="c.tMemberPoint.exgPointBalance" sortable="true" class="ac" title="积分余额" value="${row.tMemberPoint.exgPointBalance}"/>
		<display:column class="ac" title="注册影城" value="${row.tCinema.innerName}"/>
		<display:column class="ac" title="注册方式" >
			<c:choose>
				<c:when test="${row.registType == 1 }">
				主动注册
				</c:when>
				<c:when test="${row.registType == 2 }">
				自动转换
				</c:when>
				<c:when test="${row.registType == 3 }">
				批量导入
				</c:when>
				<c:when test="${row.registType == 4 }">
				安客诚导入
				</c:when>
			</c:choose>
		</display:column>
		<display:column sortProperty="c.createdDate" sortable="true"  class="ac" title="注册时间" value="${row.createdDate}" format="{0,date,yyyy-MM-dd HH:mm:ss }"/>
		<display:column class="ac" title="招募渠道" >
			${DIMS['216'][row.channelId]}
		</display:column>
		<display:column title="会员状态" class="ac">
			<c:choose>
				<c:when test="${row.status == 1 }">
					有效
				</c:when>
				<c:otherwise>禁用</c:otherwise>
			</c:choose>
			
		</display:column>
		<display:column title="操作" headerClass="ac" class="ac" style="width:100px;">
		<c:choose>
			<c:when test="${u.levelName eq 'GROUP' }">
					<c:choose>
					<c:when test="${editmem and (row.status == 1) and deletedmem and (row.isDeleted == 0)}">
						&nbsp;
							<button type="button" class="btn edit"
								onclick="${adk:func('doEdit')}(${row.id});">编辑</button>&nbsp;&nbsp;
							<button type="button" class="btn del"
								onclick="if(confirm('确定要删除该数据记录吗?')){${adk:func('doDelete')}(${row.id});}">删除</button>
						
					</c:when>
					<c:when test="${deletedmem and (row.isDeleted == 0)}">
							&nbsp;&nbsp;
							<button type="button" class="btn del"
								onclick="if(confirm('确定要删除该数据记录吗?')){${adk:func('doDelete')}(${row.id});}">删除</button>
					</c:when>
					<c:when test="${ editmem and (row.status == 1) and not deletedem and (row.isDeleted == 0)}">
							&nbsp;&nbsp;
							<button type="button" class="btn edit"
								onclick="${adk:func('doEdit')}(${row.id});">编辑</button>
					</c:when>
					<c:otherwise>
					</c:otherwise>
				</c:choose>
			</c:when>
			<c:when test="${u.levelName eq 'REGION' and u.regionCode eq row.tCinema.area}">
				<c:choose>
						<c:when test="${editmem and (row.status == 1) and deletedmem and (row.isDeleted == 0)}">
							&nbsp;
								<button type="button" class="btn edit"
									onclick="${adk:func('doEdit')}(${row.id});">编辑</button>&nbsp;&nbsp;
								<button type="button" class="btn del"
									onclick="if(confirm('确定要删除该数据记录吗?')){${adk:func('doDelete')}(${row.id});}">删除</button>
						</c:when>
						<c:when test="${deletedmem and (row.isDeleted == 0)}">
								&nbsp;&nbsp;
								<button type="button" class="btn del"
									onclick="if(confirm('确定要删除该数据记录吗?')){${adk:func('doDelete')}(${row.id});}">删除</button>
						</c:when>
						<c:when test="${ editmem and (row.status == 1) and not deletedem and (row.isDeleted == 0)}">
								&nbsp;&nbsp;
								<button type="button" class="btn edit"
									onclick="${adk:func('doEdit')}(${row.id});">编辑</button>
						</c:when>
						<c:otherwise>
						</c:otherwise>
					</c:choose>
			</c:when>
			
			<c:when test="${u.levelName eq 'CINEMA' and u.cinemaId == row.tCinema.id}">
				<c:choose>
						<c:when test="${editmem and (row.status == 1) and deletedmem and (row.isDeleted == 0)}">
								&nbsp;
								<button type="button" class="btn edit"
									onclick="${adk:func('doEdit')}(${row.id});">编辑</button>&nbsp;&nbsp;
								<button type="button" class="btn del"
									onclick="if(confirm('确定要删除该数据记录吗?')){${adk:func('doDelete')}(${row.id});}">删除</button>
						</c:when>
						<c:when test="${deletedmem and (row.isDeleted == 0)}">
								&nbsp;&nbsp;
								<button type="button" class="btn del"
									onclick="if(confirm('确定要删除该数据记录吗?')){${adk:func('doDelete')}(${row.id});}">删除</button>
						</c:when>
						<c:when test="${ editmem and (row.status == 1) and not deletedem and (row.isDeleted == 0)}">
								&nbsp;&nbsp;
								<button type="button" class="btn edit"
									onclick="${adk:func('doEdit')}(${row.id});">编辑</button>
						</c:when>
						<c:otherwise>
						</c:otherwise>
					</c:choose>
			</c:when>
		</c:choose>
		</display:column>
	</display:table></div>
</adk:form></div>
<!-- 使用JS处理Displaytag的排序与翻页处理 -->
<script language="javascript">

var inputForm = document.forms["frmMemberList${adk:encodens(frmMemberList)}"];

$("#listTable").displayTagAjax({
	form : '${adk:encodens("frmPaging")}'
});
$("#selectAll").selectAll({parentId:'listTable'});

$("#changeStatusBtn").click(function(){
	if(isSelected('listTable','请选择需要进行修改的数据记录!')){
		var strs = new Array();
		$('input:checked', inputForm).each(function(i){
			strs.push(this.value);
		});
		if(strs.length == 0){
			alert("请选择您要处理的数据!");
			return ;
		}else{
			${adk:func('doChangeStatus')}(strs);
		}
		return true;
	}else{
		return false;
	};
});
</script>
