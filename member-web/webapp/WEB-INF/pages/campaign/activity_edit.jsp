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
<adk:form name="frmCancelEdit" action="cancelEdit">
	<adk:func name="cancelEdit" submit="yes" />
</adk:form>
<adk:form name="frmSelExtPointRule" action="selExtPointRule">
	<adk:func name="selExtPointRule" submit="yes" />
</adk:form>
<adk:form name="frmSelVoucherPool" action="selVoucherPool">
	<adk:func name="selVoucherPool" submit="yes" />
</adk:form>
<adk:form name="frmSelSegment" action="selSegment">
	<input type="hidden" name="propertyName"/>
	<adk:func name="selSegment" submit="yes" param="propertyName"/>
</adk:form>
<adk:form name="frmSxptExcl" action="exptExcl">
	<adk:func name="exptExcl" submit="yes" />
</adk:form>
<adk:form name="frmDoEdit" action="doEdit">
	<adk:func name="doEdit" submit="yes" />
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
<adk:form name="frmSendMessage" action="sendMessage">
	<input type="hidden" name="mobileNo" />
	<input type="hidden" name="msgContent" />
	<adk:func name="sendMessage" param="mobileNo,msgContent" submit="yes" />
</adk:form>
<c:set var="editable" value="${adk:exec1(u, 'hasRight', m.CAMPAIGN_EDIT) and not phaseIsEnd and campaign.creationLevel eq u.level and campaign.creationAreaId eq u.regionCode and campaign.creationCinemaId eq u.cinemaId}"/>
<c:set var="editing" value="${m.editCmnActivity.editing}"/>
<c:set var="publish" value="${adk:exec1(u, 'hasRight', m.ACTIVITY_PUBLISH) and not phaseIsEnd and campaign.creationLevel eq u.level and campaign.creationAreaId eq u.regionCode and campaign.creationCinemaId eq u.cinemaId}"/>
<adk:form name="frmSaveEdit" action="saveEdit" vaction="saveEdit"
	resetref="${m.editCmnActivity}">
	<input type="hidden" name="status" />
	<adk:func name="saveEdit" submit="yes" param="status"/>
		<div style="border-top:solid 1px #e7e7e7; border-left:solid 1px #e7e7e7; border-right:solid 1px #e7e7e7;" class="dhead">
		  <div style="background-color:#f8f8f8;  font-weight:bold;  padding:4px 10px;  " class="dtitle">
		  	<c:choose>
		  		<c:when test="${empty m.editCmnActivity.id}">新建波次</c:when>
		  		<c:otherwise>
		  			${m.editCmnActivity.tCampaign.name}>>${m.editCmnActivity.tCmnPhase.name}>>${m.editCmnActivity.name}
		  		</c:otherwise>
		  	</c:choose>
		  </div>
		</div>
		<div class="box_border" style="max-height: 500px;overflow: auto;">
		<table border="0" style="width: 100%;">
			<tbody>
				<tr>
					<td align="right" nowrap="nowrap" width="100px">编码:</td>
					<td align="left" nowrap="nowrap">
						${adk:ifelse(empty m.editCmnActivity.id,'系统自动生成',m.editCmnActivity.code)}
					</td>
					<td align="right" nowrap="nowrap" width="100px">名称:</td>
					<td align="left" nowrap="nowrap" colspan="3">
						<c:choose>
							<c:when test="${editing}">
								<input:text name="name" object="${m.editCmnActivity}" property="name" validate="validate" class="txtinput_wid80" mandatory="yes"/>
							</c:when>
							<c:otherwise>
								${m.editCmnActivity.name}
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td align="right" nowrap="nowrap">状态:</td>
					<td align="left" nowrap="nowrap" colspan="3">
						${DIMS[m.DIMTYPE_CAMPAIGN_STATUS][m.editCmnActivity.status]}
					</td>
				</tr>
				<tr>
					<td align="right" nowrap="nowrap">开始时间:</td>
					<td align="left" nowrap="nowrap">
						<c:choose>
							<c:when test="${editing}">
								<input:text id="startDtime" name="startDtime"
									object="${m.editCmnActivity}" property="strStartDtime"
									readOnly="${true}" validate="validate" mandatory="yes"
									onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',autoPickDate:true,onpicked:${adk:func('changeDate')},oncleared:${adk:func('changeDate')},minDate:'${startDate}',maxDate:'#F{$dp.$D(\\\'endDtime\\\')||\\\'${endDate}\\\'}'})" />
							</c:when>
							<c:otherwise>
								<fmt:formatDate value="${m.editCmnActivity.startDtime}" pattern="yyyy-MM-dd HH:mm:ss"/>
							</c:otherwise>
						</c:choose>	
					</td>
					<td align="right" nowrap="nowrap">结束时间:</td>
					<td align="left" nowrap="nowrap">
						<c:choose>
							<c:when test="${editing}">
								<input:text id="endDtime" name="endDtime"
									object="${m.editCmnActivity}" property="strEndDtime"
									readOnly="${true}" validate="validate" mandatory="yes"
									onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',autoPickDate:true,onpicked:${adk:func('changeDate')},oncleared:${adk:func('changeDate')},minDate:'#F{$dp.$D(\\\'startDtime\\\')||\\\'${startDate}\\\'}',maxDate:'${endDate}'})" />
							</c:when>
							<c:otherwise>
								<fmt:formatDate value="${m.editCmnActivity.endDtime}" pattern="yyyy-MM-dd HH:mm:ss"/>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td align="right" nowrap="nowrap">描述:</td>
					<td align="left" colspan="3" style="word-break:break-all">
						<c:choose>
							<c:when test="${editing}">
								<input:textarea name="description" object="${m.editCmnActivity}" property="description" validate="validate" class="txtarea_wid2"/>
							</c:when>
							<c:otherwise>
								${m.editCmnActivity.description}
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</tbody>
		</table>
		</div>
		<c:if test="${m.editCmnActivity.status eq m.CAMPAINGN_STATUS_FINISH}">
		<div class="table" style="max-height: 500px;overflow: auto;">
		<div style="border-top:solid 1px #e7e7e7; border-left:solid 1px #e7e7e7; border-right:solid 1px #e7e7e7;" class="dhead">
		  <div style="background-color:#f8f8f8;  font-weight:bold;  padding:4px 10px;  " class="dtitle">
		  	活动效果
		  </div>
		</div>
		
		<table>
		  	<thead>
		  		<th>发送人数</th>
		  		<th>推荐响应人数</th>
		  		<th>推荐响应率</th>
		  		<th>关联响应人数</th>
		  		<th>关联响应率</th>
		  		<th>控制组人数</th>
		  		<th>控制组响应人数</th>
		  		<th>控制组响应率</th>
		  	</thead>
		  	<c:choose>
		  		<c:when test="${empty m.editCmnActivity.tActResults}">
		  			<tr>
	  					<td colspan="8">未添加统计方式</td>
	  				</tr>
		  		</c:when>
		  		<c:otherwise>
		  			<c:forEach items="${m.editCmnActivity.tActResults}" var="actResult">
				  		<c:choose>
				  			<c:when test="${actResult.status eq m.ACTIVITY_ACT_RESULT_EXEXUTE}">
				  				<tr>
				  					<td colspan="8">统计中...</td>
				  				</tr>
				  			</c:when>
				  			<c:otherwise>
				  				<tr>
							  		<td style="word-break:break-all">${actResult.contactCount}</td>
							  		<td style="word-break:break-all">${actResult.resCount}</td>
							  		<td style="word-break:break-all">
										<c:choose>
											<c:when test="${empty actResult.resCount or actResult.resCount eq '0' or empty actResult.contactCount or actResult.contactCount eq '0'}">0</c:when>
											<c:otherwise>
												<fmt:formatNumber value="${actResult.alterResCount*100/actResult.contactCount}" pattern="#.##"/>%
											</c:otherwise>
										</c:choose>
							  		</td>
							  		<td style="word-break:break-all">${actResult.alterResCount}</td>
							  		<td style="word-break:break-all">
							  			<c:choose>
							  				<c:when test="${empty actResult.alterResCount or actResult.alterResCount eq '0' or empty actResult.contactCount or actResult.contactCount eq '0'}">0</c:when>
							  				<c:otherwise>
									  			<fmt:formatNumber value="${actResult.alterResCount*100/actResult.contactCount}" pattern="#.##"/>%
							  				</c:otherwise>
							  			</c:choose>
							  		</td>
							  		<td style="word-break:break-all">${actResult.controlCount}</td>
							  		<td style="word-break:break-all">${actResult.controlResCount}</td>
							  		<td style="word-break:break-all">
							  			<c:choose>
							  				<c:when test="${empty actResult.controlResCount or actResult.controlResCount eq '0' or empty actResult.controlCount or actResult.controlCount eq '0'}">0</c:when>
							  				<c:otherwise>
									  			<fmt:formatNumber value="${actResult.controlResCount*100/actResult.controlCount}" pattern="#.##"/>%
							  				</c:otherwise>
							  			</c:choose>
							  		</td>
							  	</tr>
				  			</c:otherwise>
				  		</c:choose>
				  	</c:forEach>
		  		</c:otherwise>
		  	</c:choose>
		  </table>
		</div>
		</c:if>
		<div class="table" style="max-height: 500px;overflow: auto;">
		<div style="border-top:solid 1px #e7e7e7; border-left:solid 1px #e7e7e7; border-right:solid 1px #e7e7e7;" class="dhead">
		  <div style="background-color:#f8f8f8;  font-weight:bold;  padding:4px 10px;  " class="dtitle">
		  	营销话术
		  </div>
		  
		</div>
		<table border="0" style="width: 100%;" >
			<tbody>
				<tr>
					<td align="right" nowrap="nowrap" width="100px">接触渠道:</td>
					<td align="left" nowrap="nowrap" colspan="2">
						<c:choose>
							<c:when test="${editing}">
								<input:radio name="offerChannel" object="${m.editCmnActivity.tOffer}" property="offerChannel" validate="validate" value="${m.OFFER_CHANNEL_SHORT_MESSAGE}" />${DIMS[m.DIMTYPE_OFFER_CHANNEL][m.OFFER_CHANNEL_SHORT_MESSAGE]}
								<%-- <input:radio name="offerChannel" object="${m.editCmnActivity.tOffer}" property="offerChannel" validate="validate" value="${m.OFFER_CHANNEL_MULTIMEDIA_MESSAGE}" />${DIMS[m.DIMTYPE_OFFER_CHANNEL][m.OFFER_CHANNEL_MULTIMEDIA_MESSAGE]}
								<input:radio name="offerChannel" object="${m.editCmnActivity.tOffer}" property="offerChannel" validate="validate" value="${m.OFFER_CHANNEL_EDM}" />${DIMS[m.DIMTYPE_OFFER_CHANNEL][m.OFFER_CHANNEL_EDM]}
								<input:radio name="offerChannel" object="${m.editCmnActivity.tOffer}" property="offerChannel" validate="validate" value="${m.OFFER_CHANNEL_WEB}" />${DIMS[m.DIMTYPE_OFFER_CHANNEL][m.OFFER_CHANNEL_WEB]}
								<input:radio name="offerChannel" object="${m.editCmnActivity.tOffer}" property="offerChannel" validate="validate" value="${m.OFFER_CHANNEL_OTHER}" />${DIMS[m.DIMTYPE_OFFER_CHANNEL][m.OFFER_CHANNEL_OTHER]} --%>
							</c:when>
							<c:otherwise>
								${DIMS[m.DIMTYPE_OFFER_CHANNEL][m.editCmnActivity.tOffer.offerChannel]}
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<c:if test="${editing}">
				<tr>
					<td align="right" nowrap="nowrap">插入变量:</td>
					<td align="left" nowrap="nowrap" colspan="2">
						<ex:select name="variable"  cssClass="select_wid"
							list="${DIMS[m.DIMTYPE_OFFER_VARIABLE]}" emptyOption="true" id="variable"/>
						<button type="button" class="btn movedown"
							onclick="${adk:func('insert')}()">插入</button>
					</td>
				</tr>
				</c:if>
				<tr>
					<td align="right" nowrap="nowrap">话术内容:</td>
					<td align="left" colspan="2" style="word-break:break-all">
						<c:choose>
							<c:when test="${editing}">
								<input:textarea id="content" name="content" object="${m.editCmnActivity.tOffer}" property="content" validate="validate" class="txtarea_wid2" mandatory="yes"/>
							</c:when>
							<c:otherwise>
								${m.editCmnActivity.tOffer.content}
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<c:if test="${editing}">
				<tr>
					<td align="right" nowrap="nowrap">手机号:</td>
					<td align="left" nowrap="nowrap" colspan="2">
						<input:text name="mobileNo" id="mobileNo" />
						<button type="button" class="btn "
							onclick="${adk:func('sendMessage')}($('#mobileNo').val(),$('#content').val())">测试短信</button>
					</td>
				</tr>
				</c:if>
				<tr>
					<td align="right" nowrap="nowrap">通知时间:</td>
					<td align="left" nowrap="nowrap" width="200px" colspan="3">
						<c:choose>
							<c:when test="${editing}">
								<input:text id="toldTime" name="toldTime" object="${m.editCmnActivity.tOffer}" property="strToldTime" readOnly="${true}" validate="validate" mandatory="yes" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',autoPickDate:true,onpicked:${adk:func('changeDate')},oncleared:${adk:func('changeDate')},minDate:'%y-%M-%d %H:%m:%s', maxDate:'#F{$dp.$D(\\\'startDtime\\\')}'})"/>
							</c:when>
							<c:otherwise>
								<fmt:formatDate value="${m.editCmnActivity.tOffer.toldTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</tbody>
		</table>
		</div>
		<c:if test="${not empty m.editCmnActivity.id and not empty m.editCmnActivity.tActTarget}">
		<div class="table" style="max-height: 500px;overflow: auto;">
		<div style="border-top:solid 1px #e7e7e7; border-left:solid 1px #e7e7e7; border-right:solid 1px #e7e7e7;" class="dhead">
		  <div style="background-color:#f8f8f8;  font-weight:bold;  padding:4px 10px;  " class="dtitle">
		  	目标选择 &nbsp;&nbsp;(${adk:ifelse(m.editCmnActivity.tActTarget.status eq m.ACTIVITY_ACT_TARGET_PLAN,'计划','冻结') })
		  </div>
		 ${DIMS[m.DIMTYPE_TARGET_TYPE][m.editCmnActivity.tActTarget.targetType]}
		</div>
		<div id="segment" style="display:${adk:ifelse(m.editCmnActivity.tActTarget.targetType eq m.TARGET_TYPE_SEGMENT,'block','none')}">
		<table border="0" style="width: 100%;" >
			<tbody>
				<tr>
					<td align="right" nowrap="nowrap" width="100">指定客群:</td>
					<td align="left" nowrap="nowrap"  width="290">
						${m.editCmnActivity.tActTarget.tSegment.name}
					</td>
					<td align="right" nowrap="nowrap"  width="100">总数:</td>
					<td align="left" nowrap="nowrap">
						<c:if test="${not empty m.editCmnActivity.tActTarget.tSegment}">
							${m.editCmnActivity.tActTarget.totalCount} (
							<fmt:formatDate value="${m.editCmnActivity.tActTarget.tSegment.calCountTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
							)
						</c:if>
					</td>
				</tr>
			</tbody>
		</table>
		</div>
		<div id="excel" style="display:${adk:ifelse(m.editCmnActivity.tActTarget.targetType eq m.TARGET_TYPE_EXCEL,'block','none')}">
		<table border="0" style="width: 100%;" >
			<tbody>
				<tr>
					<td align="right" nowrap="nowrap" width="100">上传导入文件:</td>
					<td align="left" nowrap="nowrap"  width="290">
						<c:choose>
							<c:when test="${(empty m.uploadFile or m.uploadFile.delete) and editing}">
				                <input id="fileupload" type="file" name="files[]" data-url="${adk:resproxy('doUpload')}" multiple/>
								<div id="fileuploadDiv"></div>
							</c:when>
							<c:otherwise>
								<input id="fileupload" type="file" name="files[]" data-url="${adk:resproxy('doUpload')}" multiple style="display:none"/>
								<div id="fileuploadDiv">
									<c:if test="${not empty m.uploadFile}">
									<a href="${adk:resproxy('doDownload')}" target="_blank">
										${m.uploadFile.fileName}&nbsp;&nbsp;[${m.uploadFile.fileSize}]
									</a>
									</c:if>
								</div>
							</c:otherwise>
						</c:choose>
					</td>
					<td align="right" nowrap="nowrap"  width="100">总数:</td>
					<td align="left" nowrap="nowrap">
						${m.editCmnActivity.tActTarget.totalCount}
					</td>
				</tr>
			</tbody>
		</table>
		</div>
		<table border="0" style="width: 100%;" >
			<tbody>
				<tr>
					<td align="right" nowrap="nowrap" width="100">受众数量:</td>
					<td align="left" nowrap="nowrap"  width="290">
						${m.editCmnActivity.tActTarget.maxCount}
					</td>
					<td align="right" nowrap="nowrap"  width="100">控制组数量:</td>
					<td align="left" nowrap="nowrap">
						${m.editCmnActivity.tActTarget.controlCount}
					</td>
				</tr>
			</tbody>
		</table>
		</div>
		</c:if>
		<div class="table" style="max-height: 500px;overflow: auto;">
		<div style="border-top:solid 1px #e7e7e7; border-left:solid 1px #e7e7e7; border-right:solid 1px #e7e7e7;" class="dhead">
		  <div style="background-color:#f8f8f8;  font-weight:bold;  padding:4px 10px;  " class="dtitle">
		  	活动礼遇
		  </div>
		<c:choose>
			<c:when test="${editing}">
				<input:radio name="offerType" object="${m.editCmnActivity.tActOffer}" property="offerType" validate="validate" value="" onclick="$('#integrate').css('display','none');$('#voucher').css('display','none')"/>无
				<input:radio name="offerType" object="${m.editCmnActivity.tActOffer}" property="offerType" validate="validate" value="${m.RES_CONFIG_TYPE_INTEGRATE}" onclick="$('#integrate').css('display','block');$('#voucher').css('display','none')"/>${DIMS[m.DIMTYPE_RES_CONFIG_TYPE][m.ACT_OFFER_TYPE_INTEGRATE]}
				<%-- <input:radio name="offerType" object="${m.editCmnActivity.tActOffer}" property="offerType" validate="validate" value="${m.RES_CONFIG_TYPE_VOUCHER}" onclick="$('#integrate').css('display','none');$('#voucher').css('display','block');$('#other').css('display','none')" />${DIMS[m.DIMTYPE_RES_CONFIG_TYPE][m.ACT_OFFER_TYPE_VOUCHER]} --%>
			</c:when>
			<c:otherwise>
				<c:choose>
					<c:when test="${m.editCmnActivity.tActOffer.offerType eq m.ACT_OFFER_TYPE_INTEGRATE}">积分活动</c:when>
					<c:when test="${m.editCmnActivity.tActOffer.offerType eq m.ACT_OFFER_TYPE_VOUCHER}">券发放</c:when>
					<c:otherwise>无</c:otherwise>
				</c:choose>
			</c:otherwise>
		</c:choose>
		</div>
		<div id="integrate" style="display:${adk:ifelse(m.editCmnActivity.tActOffer.offerType eq m.ACT_OFFER_TYPE_INTEGRATE, 'block','none')}">
			<table border="0" style="width: 100%;" >
			<tbody>
				<tr>
					<td align="right" nowrap="nowrap" width="100px">特殊积分规则:</td>
					<td align="left" nowrap="nowrap">
					<c:if test="${not empty m.editCmnActivity.tActOffer.tExtPointRule}">
						${m.editCmnActivity.tActOffer.tExtPointRule.name}
						(
						<fmt:formatDate value="${m.editCmnActivity.tActOffer.tExtPointRule.startDtime}" pattern="yyyy-MM-dd HH:mm:ss"/>
						~
						<fmt:formatDate value="${m.editCmnActivity.tActOffer.tExtPointRule.endDtime}" pattern="yyyy-MM-dd HH:mm:ss"/>
						)
					</c:if>
					<c:if test="${editing}">
					<button type="button" class="btn search notext" onclick="${adk:func('selExtPointRule')}()">选择特殊积分规则</button>
					</c:if>
					</td>
				</tr>
			</tbody>
			</table>
		</div>
		<div id="voucher" style="display:${adk:ifelse(m.editCmnActivity.tActOffer.offerType eq m.ACT_OFFER_TYPE_VOUCHER, 'block','none')}">
			<table border="0" style="width: 100%;" >
			<tbody>
				<tr>
					<td align="right" nowrap="nowrap" width="100px">券发放:</td>
					<td align="left" nowrap="nowrap">${m.editCmnActivity.tActOffer.tVoucherPool.name}
					<c:if test="${editing}">
					<button type="button" class="btn search notext" onclick="${adk:func('selVoucherPool')}()">选择券库</button>
					</c:if>
					</td>
				</tr>
			</tbody>
			</table>
		</div>
		</div>
</adk:form>

<div align="center" style="margin: 10px 0 0 0">
	<c:choose>
		<c:when test="${editing}">
			<button type="button" class="btn save"
				onclick="${adk:func('saveEdit')}('${m.CAMPAINGN_STATUS_PLAN }')">保存</button>
			<button type="button" class="btn cancel"
				onclick="${adk:func('cancelEdit')}()">取消</button>
		</c:when>
		<c:when test="${editable and m.editCmnActivity.status eq m.CAMPAINGN_STATUS_PLAN}">
			<button type="button" class="btn edit"
				onclick="${adk:func('doEdit')}()">编辑</button>
		</c:when>
	</c:choose>
	<c:choose>
		<c:when test="${publish and m.editCmnActivity.status eq m.CAMPAINGN_STATUS_PLAN and not empty m.editCmnActivity.tActTarget and m.editCmnActivity.tActTarget.status eq m.ACTIVITY_ACT_TARGET_LOCKED}">
			<button type="button" class="btn enabled"
				onclick="${adk:func('saveEdit')}('${m.CAMPAINGN_STATUS_PUBLISH }')">发布</button>
		</c:when>
		<c:when test="${publish and m.editCmnActivity.status eq m.CAMPAINGN_STATUS_PUBLISH and m.editCmnActivity.canCancelPublish}">
			<button type="button" class="btn disabled"
				onclick="${adk:func('saveEdit')}('${m.CAMPAINGN_STATUS_PLAN}')">解除发布</button>
		</c:when>
	</c:choose>
</div>
<script type="text/javascript">
//在光标位置插入内容
(function($) {
	$.fn.extend({
		insertContent: function(myValue, t) {
			var $t = $(this)[0];
			if (document.selection) { //ie
				this.focus();
				var sel = document.selection.createRange();
				sel.text = myValue;
				this.focus();
				sel.moveStart('character', -l);
				var wee = sel.text.length;
				if (arguments.length == 2) {
					var l = $t.value.length;
					sel.moveEnd("character", wee + t);
					t <= 0 ? sel.moveStart("character", wee - 2 * t - myValue.length) : sel.moveStart("character", wee - t - myValue.length);
					sel.select();
				}
			} else if ($t.selectionStart || $t.selectionStart == '0') {
				var startPos = $t.selectionStart;
				var endPos = $t.selectionEnd;
				var scrollTop = $t.scrollTop;
				$t.value = $t.value.substring(0, startPos) + myValue + $t.value.substring(endPos, $t.value.length);
				this.focus();
				$t.selectionStart = startPos + myValue.length;
				$t.selectionEnd = startPos + myValue.length;
				$t.scrollTop = scrollTop;
				if (arguments.length == 2) {
					$t.setSelectionRange(startPos - t, $t.selectionEnd + t);
					this.focus();
				}
			} else {
				this.value += myValue;
				this.focus();
			}
		}
	})
})(jQuery)
<adk:func name="insert"/>(){
	$("#content").insertContent("\$\{" +$("#variable").val()+"\}");
	document.forms["${adk:encodens('frmSaveEdit')}"].ajaxValidate($("#content")[0]);
}
<adk:func name="changeDate"/>(){
	document.forms["${adk:encodens('frmSaveEdit')}"].ajaxValidate($("#"+this.id)[0]);
}

$('#fileupload').fileupload({
	dataType: 'json',
	add: function(e, data) {
		$("#fileuploadDiv p").remove();
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
		$("#fileuploadDiv").append($('<a></a>').text(data.result[0].name)).append('<button type="button" class="btn clear notext delfile">删除</button>').append("<button type='button' class='btn moveup notext upfile'>上传</button>");
		$('.delfile').click(function() {
			$.ajax({
				url: "${adk:resproxy('doUploadDel')}",
				data: {
					'fileId': ''
				},
				// 请求参数，json格式
				success: function(data) {
					$("#fileuploadDiv").html("");
					$('#fileupload').show();
				}
			});
		});
		$('.upfile').click(function() {
			var delbtn = $(this);
			$.ajax({
				url: "${adk:resproxy('doUploadFile')}",
				success: function(data) {
					$("#fileuploadDiv").append("等待处理");
					delbtn.remove();
					
				}
			});
		});
		data.context = $("#fileuploadDiv").html();
		renderButtons();
		$('#fileupload').hide();
	}
});
$('.delfile').click(function() {
	$.ajax({
		url: "${adk:resproxy('doUploadDel')}",
		success: function(data) {
			$("#fileuploadDiv").html("");
			$('#fileupload').show();
		}
	});
});
$('.upfile').click(function() {
	var delbtn = $(this);
	$.ajax({
		url: "${adk:resproxy('doUploadFile')}",
		success: function(data) {
			$("#fileuploadDiv").append("等待处理");
			delbtn.remove();
		}
	});
});
$("#exportTmpl").click(function(){
	var url = "${adk:resproxy('exptExcl')}";
	window.location = url;
});
$("#lockedSegment").click(function(){
	var maxCount = $("#maxCount").val();
	var controlCount = $("#controlCount").val();
	var totalCount = '${m.editCmnActivity.tActTarget.totalCount}';
	var suggestCount = Math.round(maxCount/10);
	if(Number(maxCount)+Number(controlCount) > totalCount){
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
		if(maxCount == suggestCount){
			${adk:func('lockedSegment')}(maxCount,controlCount);
		}else if(maxCount < suggestCount){
			$("<div title='冻结提示'>您设置的控制组会员数量过少，不足受众群体的10%，样本数量较少会影响到活动对比效果的评估。<font color='red'>请确认是否需要重新设置?</font></div>").dialog({
				resizable: false,
				height:140,
				width:480,
				modal: true,
				buttons: 
				     {
					"是": function() {
						$( this ).dialog( "close" );
					},
					"否": function() {
						$( this ).dialog( "close" );
						${adk:func('lockedSegment')}(maxCount,controlCount);
					}
				}
			});	
		}else if(maxCount > suggestCount){
			$("<div title='冻结提示'>您设置的控制组会员数量过多，已经超过受众群体的10%，这样会影响到活动对比效果的评估。<font color='red'>请确认是否需要重新设置?</font></div>").dialog({
				resizable: false,
				height:140,
				width:480,
				modal: true,
				buttons: 
				     {
					"是": function() {
						$( this ).dialog( "close" );
					},
					"否": function() {
						$( this ).dialog( "close" );
						${adk:func('lockedSegment')}(maxCount,controlCount);
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
				"是": function() {
					$( this ).dialog( "close" );
					${adk:func('lockedSegment')}(maxCount,controlCount);
				},
				"否": function() {
					$( this ).dialog( "close" );
				}
			}
		});	
	}else {
		${adk:func('lockedSegment')}(maxCount,controlCount);
	}
});
</script>

