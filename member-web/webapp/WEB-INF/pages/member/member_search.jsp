<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.aggrepoint.com/adk" prefix="adk"%>
<%@ taglib tagdir="/WEB-INF/tags/adk/html/input" prefix="input"%>
<%@ taglib tagdir="/WEB-INF/tags/ex" prefix="ex"%>

<script language="javascript" src="${adk:resurl('/js/My97DatePicker/WdatePicker.js')}">
	function a() {
	}
</script>
<adk:form name="frmSearch" action="search">
<div style="overflow: auto;">
	<table border="0" width="100%" >
		<tbody>
			<tr>
				<td align="right" nowrap="nowrap" valign="top">手机号:</td>
				<td align="left" nowrap="nowrap" valign="top">
					<input:text name="mobile" ></input:text>
				</td>
				<td align="right" nowrap="nowrap" valign="top">姓名:</td>
				<td align="left" nowrap="nowrap" valign="top">
					<input:text name="name" ></input:text>
				</td>
				
				<td align="right" nowrap="nowrap" valign="top">注册时间:</td>
				<td align="left" nowrap="nowrap" valign="top">
				<input:text id="query_start" name="query_start"  readOnly="${true}" class="Wdate"
									onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',autoPickDate:true,maxDate:'#F{$dp.$D(\\\'query_end\\\')}'})" />~
				<input:text id="query_end" name="query_end" readOnly="${true}" class="Wdate"
									onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',autoPickDate:true,minDate:'#F{$dp.$D(\\\'query_start\\\')}'})" />
				</td>
				
				<%-- <td align="right" nowrap="nowrap" valign="top">会员状态:</td>
				<td align="left" nowrap="nowrap" >
					<input type="radio" name="memberstatus" value="1" ${adk:ifelse(query.memberstatus ==1, 'checked', '')}/>有效
					<input type="radio" name="memberstatus" value="0" ${adk:ifelse(query.memberstatus ==0, 'checked', '')}/>禁用
				</td> --%>
				
			</tr>
			
			<tr>
				<td align="right" nowrap="nowrap" valign="top">万人迷卡号:</td>
				<td align="left" nowrap="nowrap" valign="top">
					<input:text name="tMackDaddyCardNo"></input:text>
				</td>
				
				<td align="right" nowrap="nowrap" valign="top">电子邮件:</td>
				<td align="left" nowrap="nowrap" valign="top">
					<input:text name="email" ></input:text>
				</td>
				
				<td align="right" nowrap="nowrap" valign="top">可兑换积分余额:</td>
				<td align="left" nowrap="nowrap" valign="top">
					<input:text name="minExgPointBalance" class="hasDatepicker" size="20" ></input:text>~
					<input:text name="maxExgPointBalance" class="hasDatepicker" size="20" ></input:text>
				</td>
				
				<%-- <td align="right" nowrap="nowrap" valign="top">是否删除:</td>
				<td valign="top" align="left" nowrap="nowrap" >
					<input type="radio" name="memberisdeleted" value="1" ${adk:ifelse(query.memberisdeleted ==1, 'checked', '')}/>是&nbsp;&nbsp;&nbsp;
					<input type="radio" name="memberisdeleted" value="0" ${adk:ifelse(query.memberisdeleted ==0, 'checked', '')}/>否
				</td> --%>
			</tr>
			<tr>
				<td align="right" nowrap="nowrap" valign="top">招募渠道:</td>
				<td valign="top" align="left" nowrap="nowrap">
					<input:select  name="channelId" >
						<input:option value="">--请选择招募渠道--</input:option>
						<c:forEach items="${DIMS['216']}" var="item">
							<input:option value="${item.key}">${item.value}</input:option>
						</c:forEach>
					</input:select> 
				</td>
				
				<td align="right" nowrap="nowrap" valign="top">注册影城:</td>
				<td valign="top" align="left" nowrap="nowrap">
					<input:select name="registCinemaId" >
							<input:option value="">--请选择注册影城--</input:option>
								<c:forEach items="${allCinemaMap}" var="item">
									<input:option value="${item.id}">${item.innerName}</input:option>
								</c:forEach>
					</input:select> 
				</td>
				
				<td align="right" nowrap="nowrap" valign="top">管理影城:</td>
				<td valign="top" align="left" nowrap="nowrap">
					<input:select name="manageCinema" >
							<input:option value="">--请选择管理影城--</input:option>
								<c:forEach items="${allCinemaMap}" var="item">
									<input:option value="${item.id}">${item.innerName}</input:option>
								</c:forEach>
					</input:select> 
				</td>
			</tr>
			
			<tr>
				<td align="right" nowrap="nowrap" valign="top">招募渠道扩展:</td>
				<td valign="top" align="left" nowrap="nowrap">
					<input:select  name="channelExtId" >
						<input:option value="">--请选择招募渠道扩展--</input:option>
						<c:forEach items="${DIMS['264']}" var="item">
							<input:option value="${item.key}">${item.value}</input:option>
						</c:forEach>
					</input:select> 
				</td>
			</tr>
			
			<tr>
				<td colspan="6" align="right">
					<button class="btn search" value="查询" type="submit">查询</button>
					<button type="button" value="清除日期" name="reset" class="btn clear" onclick="${adk:func('clearDate')}();">清除日期</button> 
				</td>
			</tr>
		</tbody>
	</table>
	</div>
</adk:form>
<script language="javascript">
<adk:func name="clearDate"/>(){
	$("input[name='query_start']").val('');
	$("input[name='query_end']").val('');
}
</script>