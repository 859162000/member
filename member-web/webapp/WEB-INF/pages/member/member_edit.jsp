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
<c:set var="editMobile" value="${adk:exec1(u, 'hasRight', 'member.mgmember.info.mobile')}" />
<c:set var="editBirthday" value="${adk:exec1(u, 'hasRight', 'member.mgmember.info.birthday')}" />
<c:set var="editCinema" value="${adk:exec1(u, 'hasRight', 'member.mgmember.info.cinema')}" />
<script language="javascript" src="${adk:resurl('/js/My97DatePicker/WdatePicker.js')}">
	function p() {
	}
</script>
<div style="min-width: 650px">
<adk:form name="frmSaveEdit" action="saveEdit" vaction="saveEdit" resetref="${m.resetFlag}">
	<adk:func name="saveEdit" submit="yes" />
	<div class="adk_tab2">
		<ul id="tab" style="cursor: pointer;">
			<li name="base" id="current">
				<a id="tab_base"><span>基本信息</span></a>
			</li>
			<li name="address">
				<a id="tab_address"><span>联系信息</span></a>
			</li>
			<li name="info">
				<a id="tab_info"><span>概况</span></a>
			</li>
		</ul>
	</div>
				<!-- 基本信息 -->
			<div id="div_base" style="min-width: 300px" class="adk_tab2box">
				<table id="basetable" width="100%" bordercolor="#CCCCCC" border="1" style=" background-color:#fbfbfb">
					<tbody>
						<tr>
							<td align="right" nowrap="nowrap">会员编号:</td>
							<td align="left" nowrap="nowrap">
								${adk:ifelse(empty m.tModel.memberNo, '系统自动生成',m.tModel.memberNo)}
							</td>
							<td align="right"  nowrap="nowrap">姓名:</td>
							<td align="left" nowrap="nowrap">
								<c:choose>
									<c:when test="${ CREATEMEMBER or EDITMEMBER}">
									<input:text class="txtinput_wid80" name="name" property="tModel.name"  validate="validate" maxlength="20" />
									</c:when>
									<c:otherwise>
										${ model.name}
									</c:otherwise>
								</c:choose>
								
							</td>
						</tr>
						<tr>
							<td align="right"  nowrap="nowrap">手机号:</td>
							<td align="left" nowrap="nowrap">
								<c:choose>
									<c:when test="${ CREATEMEMBER}">
										<input:text class="txtinput_wid80" name="mobile" property="tModel.mobile" validate="validate" mandatory="yes" maxlength="11"  />
									</c:when>
									<c:when test="${EDITMEMBER and editMobile}">
										<input:text class="txtinput_wid80" name="mobile" property="tModel.mobile" validate="validate" mandatory="yes" maxlength="11"  />
									</c:when>
									<c:otherwise>
										${model.mobile }
									</c:otherwise>
								</c:choose>
							</td>
							<td align="right">电子邮件:</td>
							<td align="left">
								<c:choose>
									<c:when test="${EDITMEMBER or CREATEMEMBER}">
										<input:text class="txtinput_wid80" name="email" property="tModel.email" validate="validate" />
									</c:when>
									<c:otherwise>
										${model.email }
									</c:otherwise>
								</c:choose>
							</td>
						</tr>						
						<tr>
							<td align="right"  nowrap="nowrap">性别:</td>
							<td >
							<c:choose>
									<c:when test="${EDITMEMBER or CREATEMEMBER}">
										<input:select name="gender" class="txtinput_wid80" validate="validate" property="tModel.gender">
											<c:forEach items="${DIMS['2008']}" var="item">
												<input:option value="${item.key}">${item.value}</input:option>
											</c:forEach>
										</input:select> 
								</c:when>
								<c:otherwise>
									<c:forEach items="${DIMS['2008']}" var="item">
										<c:if test="${item.key eq model.gender }">${item.value}</c:if>
									</c:forEach>
								</c:otherwise>
							</c:choose>
							</td>
						<td align="right"  nowrap="nowrap">生日日期:</td>
							<td>
								<c:choose>
									<c:when test="${ CREATEMEMBER}">
									<input:text id="birthday" name="birthday" class="Wdate" property="tModel.strBackDate" readOnly="${true}" onClick="WdatePicker({startDate:'1985-05-01',skin:'whyGreen',dateFmt:'yyyy-MM-dd',autoPickDate:true,maxDate:'%y-%M-%d'})" />
									</c:when>
									<c:when test="${EDITMEMBER and editBirthday }">
									<input:text id="birthday" name="birthday" class="Wdate" property="tModel.strBackDate" readOnly="${true}" onClick="WdatePicker({startDate:'1985-05-01',skin:'whyGreen',dateFmt:'yyyy-MM-dd',autoPickDate:true,maxDate:'%y-%M-%d'})" />
									</c:when>
									<c:otherwise>
										<fmt:formatDate value="${model.birthday }" pattern="yyyy-MM-dd"/>
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
						<tr>
							<td align="right" >注册影城:</td>
							<td align="left">
								<c:choose>
									<c:when test="${ CREATEMEMBER}">
										<input:select name="registCinemaId" class="txtinput_wid80" property="tModel.registCinemaId" validate="validate" mandatory="yes">
											<input:option value="">--请选择注册影城--</input:option>
											<c:forEach items="${cinemaMap}" var="item">
												<input:option value="${item.id}">${item.innerName}</input:option>
											</c:forEach>
										</input:select> 
									</c:when>
									<c:when test="${EDITMEMBER and editCinema }">
										<input:select name="registCinemaId" class="txtinput_wid80" property="tModel.registCinemaId" validate="validate" mandatory="yes">
											<input:option value="">--请选择注册影城--</input:option>
											<c:forEach items="${cinemaMap}" var="item">
												<input:option value="${item.id}">${item.innerName}</input:option>
											</c:forEach>
										</input:select> 
									</c:when>
									<c:otherwise>
										<c:forEach items="${allCinemaMap}" var="item">
											<c:if test="${item.id eq model.registCinemaId }">${item.innerName}</c:if>
										</c:forEach>
									</c:otherwise>
								</c:choose>
								
							</td>
							
							<td align="right"  nowrap="nowrap">注册方式:</td>
							<td >
							<c:choose>
								<c:when test="${EDITMEMBER or CREATEMEMBER}">
									<input:select name="registType" class="txtinput_wid80" property="tModel.registType" validate="validate">
										
										<input:option value="1">主动注册</input:option>
										<input:option value="2">自动转换</input:option>
										<input:option value="3">批量导入</input:option>
										<input:option value="4">安客诚导入</input:option>
									</input:select> 
								</c:when>
								<c:otherwise>
									<c:forEach items="${DIMS['201']}" var="item">
										<c:if test="${item.key eq model.registType }">${item.value}</c:if>
									</c:forEach>
								</c:otherwise>
							</c:choose>
							</td>
						</tr>
						<tr>
							<td align="right" >招募员工号:</td>
							<td align="left">
										${model.registOpName }
							</td>
							<td align="right"  nowrap="nowrap">招募渠道:</td>
							<td>
								<c:choose>
									<c:when test="${EDITMEMBER or CREATEMEMBER}">
										<input:select  name="channelId" class="txtinput_wid80" property="tModel.channelId" validate="validate" mandatory="yes">
											<input:option value="">--请选择渠道--</input:option>
										<c:forEach items="${DIMS['216']}" var="item">
										<input:option value="${item.key}">${item.value}</input:option>
										</c:forEach>
										</input:select> 
									</c:when>
									<c:otherwise>
										<c:forEach items="${DIMS['216']}" var="item">
											<c:if test="${item.key eq model.channelId }">${item.value}</c:if>
										</c:forEach>
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
						<tr>
							<td align="right" >是否希望被联络:</td>
							<td align="left">
								<c:choose>
									<c:when test="${EDITMEMBER or CREATEMEMBER}">
										<input:select  name="contactable" class="txtinput_wid80" property="tModel.contactable" validate="validate">
										<c:forEach items="${DIMS['260']}" var="item">
										<input:option value="${item.key}">${item.value}</input:option>
										</c:forEach>
										</input:select> 
									</c:when>
									<c:otherwise>
										<c:forEach items="${DIMS['260']}" var="item">
											<c:if test="${item.key eq model.contactable }">${item.value}</c:if>
										</c:forEach>
									</c:otherwise>
								</c:choose>
							</td>
							<td align="right"  nowrap="nowrap">固定电话</td>
							<td align="left">
								<c:choose>
									<c:when test="${EDITMEMBER or CREATEMEMBER}">
										<input:text class="txtinput_wid80" name="phone"  property="tModel.phone" validate="validate"  maxlength="13"  />
									</c:when>
									<c:otherwise>
										${model.phone }
									</c:otherwise>
								</c:choose>
							</td>
						</tr>	
						<c:choose>
							<c:when test="${EDITMEMBER or CREATEMEMBER}">
							</c:when>
							<c:otherwise>
								<td align="right"  nowrap="nowrap">调整原因:</td>
								<td>
									${model.changeStatusResion }
								</td>
							</c:otherwise>
						</c:choose>
				</tbody>
				</table>
		</div>
			<!-- 联系信息 -->
			<div id="div_address" style="max-height: 500px;overflow: auto; display: none" class="adk_tab2box">
				<table id="baseaddresstable" width="100%" bordercolor="#CCCCCC" border="1" style=" background-color:#fbfbfb">
					<tbody>
						<tr>
							<td  align="right"  nowrap="nowrap">邮寄地址1</td>
							<td colspan=3 align="left"  nowrap="nowrap">
								<c:choose>
									<c:when test="${EDITMEMBER or CREATEMEMBER}">
										<input:text  name="address1" object="${m.tModel.tMemberAddrs}" property="address1" validete="validate" size="100"/>
									</c:when>
									<c:otherwise>
										${model.tMemberAddrs.address1 }
									</c:otherwise>
								</c:choose>
								
							</td>
						</tr>
						<tr>
							<td  align="right"  nowrap="nowrap">邮寄地址2</td>
							<td colspan=3 align="left" nowrap="nowrap">
							<c:choose>
								<c:when test="${EDITMEMBER or CREATEMEMBER}">
									<input:text  name="address2" object="${m.tModel.tMemberAddrs}" property="address2" validete="validate" size="100"/>
								</c:when>
									<c:otherwise>
										${model.tMemberAddrs.address2 }
									</c:otherwise>
							</c:choose>
							</td>
						</tr>
						<tr>
							<td  align="right"  nowrap="nowrap">邮寄地址3</td>
							<td colspan=3 align="left" nowrap="nowrap">
							<c:choose>
								<c:when test="${EDITMEMBER or CREATEMEMBER}">
									<input:text  name="address3"  object="${m.tModel.tMemberAddrs}" property="address3" validete="validate" size="100"/>
								</c:when>
									<c:otherwise>
										${model.tMemberAddrs.address3 }
									</c:otherwise>
							</c:choose>
							</td>
						</tr>
						<tr>
							<td  align="right"  nowrap="nowrap">邮寄地址4</td>
							<td colspan=3 align="left" nowrap="nowrap">
							<c:choose>
								<c:when test="${EDITMEMBER or CREATEMEMBER}">
									<input:text  name="address4"  object="${m.tModel.tMemberAddrs}" property="address4" validete="validate" size="100"/>
								</c:when>
									<c:otherwise>
										${model.tMemberAddrs.address4 }
									</c:otherwise>
							</c:choose>
							</td>
						</tr>
						<tr>
							<td  align="right"  nowrap="nowrap">省份:</td>
							<td align="left" width="100px" nowrap="nowrap">
								<c:choose>
									<c:when test="${EDITMEMBER or CREATEMEMBER}">
										<input:select id="provinceId" name="provinceId" object="${m.tModel.tMemberAddrs}" property="provinceId" class="txtinput_wid80"  validete="validate">
										<c:forEach items="${provinceMap}" var="item">
											<input:option value="${item.key}">${item.value}</input:option>
										</c:forEach>
										</input:select> 
									</c:when>
									<c:otherwise>
										<c:forEach items="${provinceMap}" var="item">
											<c:if test="${item.key eq model.tMemberAddrs.provinceId }">${item.value}</c:if>
										</c:forEach>
									</c:otherwise>
								</c:choose>
							</td>
							<td  align="right"  nowrap="nowrap">城市:</td>							

							<td align="left" width="100px" nowrap="nowrap">
							 <c:choose>
							 	<c:when test="${EDITMEMBER or CREATEMEMBER}">
							 		<input:select id="cityId" name="cityId" property="tModel.tMemberAddrs.cityId"  class="txtinput_wid80" validete="validate">
										<c:forEach items="${cityMap}" var="item">
												<input:option value="${item.key}" selected="${item.key eq model.tMemberAddrs.cityId }">${item.value}</input:option>
										</c:forEach>
								</input:select> 
							 	</c:when>
							 	<c:otherwise>
							 		<c:forEach items="${cityMap}" var="item">
							 			<c:if test="${item.key eq model.tMemberAddrs.cityId }">${item.value}</c:if>
									</c:forEach>
							 	</c:otherwise>
							 </c:choose>
							</td>
						</tr>
						<tr>
							<td align="right" nowrap="nowrap">邮政编码:</td>

							<td>
							<c:choose>
								<c:when test="${EDITMEMBER or CREATEMEMBER}">
								<input:text object="${m.tModel.tMemberAddrs}" class="txtinput_wid80" name="zipcode" property="zipcode" validate="validate"/>
								</c:when>
								<c:otherwise>
								${model.tMemberAddrs.zipcode }
								</c:otherwise>
							</c:choose>
							</td>
							
						</tr>						
				</tbody>
				</table>
			</div>
			<!-- 概况 -->
			<div id="div_info" style="min-width: 300px;overflow: auto; display: none" class="adk_tab2box">
				<table id="baseinfotable" width="100%" bordercolor="#CCCCCC" border="1" style=" background-color:#fbfbfb">
					<tbody>
						<tr>
							<td align="right"  nowrap="nowrap">证件类型:</td>
							<td width="250px">
								<c:choose>
									<c:when test="${EDITMEMBER or CREATEMEMBER}">
										<input:select name="idCardType" class="txtinput_wid80" property="idCardType" object="${m.tModel.tMemberInfo}" validate="validate">
										<c:forEach items="${dims['200']}" var="item">
											<input:option value="${item.key}">${item.value}</input:option>
										</c:forEach>
										</input:select> 
									</c:when>
									<c:otherwise>
										<c:forEach items="${DIMS['200']}" var="item">
											<c:if test="${item.key eq model.tMemberInfo.idCardType }">${item.value}</c:if>
										</c:forEach>
									</c:otherwise>
								</c:choose>
								
							</td>
							<td align="right"  nowrap="nowrap">证件号码</td>
							<td >
							<c:choose>
								<c:when test="${EDITMEMBER or CREATEMEMBER}">
									<input:text name="idCardNo" object="${m.tModel.tMemberInfo}" property="idCardNo" validate="validate"></input:text>
								</c:when>
								<c:otherwise>
									${model.tMemberInfo.idCardNo }
								</c:otherwise>
							</c:choose>
							</td>
						</tr>
						<tr>
							<td align="right"  nowrap="nowrap">教育程度:</td>
							<td>
							<c:choose>
								<c:when test="${EDITMEMBER or CREATEMEMBER}">
									<input:select name="education" class="txtinput_wid80" object="${m.tModel.tMemberInfo}" property="education" validate="validate">
										<c:forEach items="${dims['202']}" var="item">
											<input:option value="${item.key}">${item.value}</input:option>
										</c:forEach>
									</input:select> 
								</c:when>
								<c:otherwise>
									<c:forEach items="${DIMS['202']}" var="item">
										<c:if test="${item.key eq  model.tMemberInfo.education}">${item.value}</c:if>
									</c:forEach>
								</c:otherwise>
							</c:choose>
							</td>
							<td align="right">个人职位:</td>
							<td align="left">
							<c:choose>
								<c:when test="${EDITMEMBER or CREATEMEMBER}">
									<input:select name="occupation" class="txtinput_wid80" object="${m.tModel.tMemberInfo}" property="occupation" validate="validate">
										<c:forEach items="${dims['203']}" var="item">
										<input:option value="${item.key}">${item.value}</input:option>
										</c:forEach>
									</input:select> 
								</c:when>
								<c:otherwise>
									<c:forEach items="${DIMS['203']}" var="item">
										<c:if test="${item.key eq model.tMemberInfo.occupation }">${item.value}</c:if>
									</c:forEach>
								</c:otherwise>
							</c:choose>
							</td>
						</tr>						
						<tr>
							<td align="right" nowrap="nowrap">家庭月收入:</td>
							<td >
							<c:choose>
								<c:when test="${EDITMEMBER or CREATEMEMBER}">
									<input:select name="income" class="txtinput_wid80" object="${m.tModel.tMemberInfo}" property="income" validate="validate">
										<c:forEach items="${dims['205']}" var="item">
										<input:option value="${item.key}">${item.value}</input:option>
										</c:forEach>
									</input:select> 
								</c:when>
								<c:otherwise>
									<c:forEach items="${DIMS['205']}" var="item">
										<c:if test="${item.key eq model.tMemberInfo.income }">${item.value}</c:if>
									</c:forEach>
								</c:otherwise>
							</c:choose>
								
							</td>
						<td align="right"  nowrap="nowrap">婚姻状况:</td>
							<td >
								<c:choose>
									<c:when test="${EDITMEMBER or CREATEMEMBER}">
										<input:select name="marryStatus" class="txtinput_wid80" object="${m.tModel.tMemberInfo}" property="marryStatus" validate="validate">
											<c:forEach items="${dims['206']}" var="item">
											<input:option value="${item.key}">${item.value}</input:option>
											</c:forEach>
										</input:select> 
									</c:when>
									<c:otherwise>
										<c:forEach items="${DIMS['206']}" var="item">
											<c:if test="${item.key eq model.tMemberInfo.marryStatus }">${item.value}</c:if>
										</c:forEach>
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
						
						
						<tr>
							<td align="right">小孩人数:</td>
							<td  align="left" >
							<c:choose>
								<c:when test="${EDITMEMBER or CREATEMEMBER}">
									<input:select name="childNumber" class="txtinput_wid80" object="${m.tModel.tMemberInfo}" property="childNumber" validate="validate">
									<c:forEach items="${dims['207']}" var="item">
										<input:option value="${item.key}">${item.value}</input:option>
									</c:forEach>
									</input:select>
								</c:when>
								<c:otherwise>
									<c:forEach items="${DIMS['207']}" var="item">
										<c:if test="${item.key eq model.tMemberInfo.childNumber }">${item.value}</c:if>
									</c:forEach>
								</c:otherwise>
							</c:choose>
							
							</td>
							<td align="right">喜欢的沟通方式:</td>
							<td  align="left">
							<c:choose>
								<c:when test="${EDITMEMBER or CREATEMEMBER}">
									<c:forEach items="${DIMS['211']}" var="item">
									<input:checkbox name="contactMeans_${item.key}"  property="contactMeans"  validate="validate" value="${item.key }" />
											${item.value}
								</c:forEach>
								</c:when>
								<c:otherwise>
									<c:forEach items="${DIMS['211']}" var="item">
									<input:checkbox name="contactMeans_${item.key}" property="contactMeans" fdisabled="${true}" validate="validate" value="${item.key }" />
											${item.value}
								</c:forEach>
								</c:otherwise>
							</c:choose>
								
							</td>
						</tr>						
						
						<tr>
							<td align="right" nowrap="nowrap">微博:</td>
							<td >
								<c:choose>
								<c:when test="${EDITMEMBER or CREATEMEMBER}">
									<input:text name="weibo" object="${m.tModel.tMemberInfo}" property="weibo" validate="validate"></input:text>
								</c:when>
								<c:otherwise>
									${model.tMemberInfo.weibo }
								</c:otherwise>
							</c:choose>
								
							</td>
							<td align="right" nowrap="nowrap">QQ号码</td>
							<td >
							<c:choose>
								<c:when test="${EDITMEMBER or CREATEMEMBER}">
									<input:number name="qq" object="${m.tModel.tMemberInfo}" property="qq" validate="validate" error="请输入正确的QQ号" ></input:number>
								</c:when>
								<c:otherwise>
									${model.tMemberInfo.qq }
								</c:otherwise>
							</c:choose>
							</td>
						</tr>	
						<tr>
							<td align="right" nowrap="nowrap">豆瓣账号:</td>
							<td >
								<c:choose>
								<c:when test="${EDITMEMBER or CREATEMEMBER}">
									<input:text name="douban" object="${m.tModel.tMemberInfo}" property="douban" validate="validate"></input:text>
								</c:when>
								<c:otherwise>
									${model.tMemberInfo.douban }
								</c:otherwise>
							</c:choose>
								
							</td>
							<c:choose>
							<c:when test="${EDITMEMBER or CREATEMEMBER}">
							</c:when>
							<c:otherwise>
								<td align="right" >管理影城:</td>
								<td>
									<c:forEach items="${allCinemaMap}" var="item">
											<c:if test="${item.id eq model.tMemberInfo.manageCinema }">${item.innerName}</c:if>
										</c:forEach>
								</td>
							</c:otherwise>
						</c:choose>
						</tr>
						<tr>
						<td align="right">喜欢的电影类型:</td>
							<td align="left" colspan="3">
							<c:choose>
								<c:when test="${EDITMEMBER or CREATEMEMBER}">
									<c:set var="footsize" value="${0 }"/>
									<c:forEach items="${DIMS['131']}" var="item">
										<input:checkbox name="filmTypes_${item.key}" property="filmTypes"  validate="validate" value="${item.key }" />
												${item.value}
										<c:set var="footsize" value="${footsize + 1 }"/>
										<c:if test="${footsize == 10 }">
											<br />
											<c:set var="footsize" value="${0}" />
										</c:if>
								</c:forEach>
								</c:when>
								<c:otherwise>
									<c:set var="footsize" value="${0 }"/>
									<c:forEach items="${DIMS['131']}" var="item">
									<input:checkbox name="filmTypes_${item.key}" property="filmTypes" fdisabled="${true}" value="${item.key }" />
												${item.value}
									<c:set var="footsize" value="${footsize + 1 }"/>
									<c:if test="${footsize == 10 }">
										<br />
										<c:set var="footsize" value="${0}" />
									</c:if>
								</c:forEach>
								</c:otherwise>
							</c:choose>
								
							</td>							
						</tr>			
				</tbody>
				</table>
			</div>
		<div align="center" class="adk_tab2box">
			<c:if test="${EDITMEMBER or CREATEMEMBER}">
				<button type="button" class="btn save" onclick="${adk:func('saveEdit')}()">保存</button>
			</c:if>
			<button type="button" class="btn close"	onclick="${adk:func('cancelEdit')}()">关闭</button>
		</div>
		</adk:form>
		</div>

<script language="javascript">
<adk:func name="changeDate"/>(){
	document.forms["${adk:encodens('frmSaveEdit')}"].ajaxValidate($("#"+this.id)[0]);
}
$("#tab_base").click(function(){
	$("#div_base").css('display', 'block');
	$("#div_address").css('display', 'none');
	$("#div_info").css('display', 'none');
	$("#tab li[name='base']").attr("id","current");
	$("#tab li[name='address']").attr("id","");
	$("#tab li[name='info']").attr("id","");
});
$("#tab_address").click(function(){
	$("#div_base").css('display', 'none');
	$("#div_address").css('display', 'block');
	$("#div_info").css('display', 'none');
	$("#tab li[name='base']").attr("id","");
	$("#tab li[name='address']").attr("id","current");
	$("#tab li[name='info']").attr("id","");
});
$("#tab_info").click(function(){
	$("#div_base").css('display', 'none');
	$("#div_address").css('display', 'none');
	$("#div_info").css('display', 'block');
	$("#tab li[name='base']").attr("id","");
	$("#tab li[name='address']").attr("id","");
	$("#tab li[name='info']").attr("id","current");
});
$("#cityId").linkSelect("provinceId",
		"${adk:resproxy('doGetCity')}", false, "id", "${m.tModel.tMemberAddrs.cityId}");
</script>
