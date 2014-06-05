<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.aggrepoint.com/adk" prefix="adk"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<script language="javascript" src="${adk:resurl('/jstree/jquery.cookie.js')}">
	function a() {
	}
</script>
<script language="javascript" src="${adk:resurl('/jstree/jquery.jstree.js')}">
	function a() {
	}
</script>
<adk:form name="frmSelect" action="selectNode">
	<input type="hidden" name="id" />
	<input type="hidden" name="type" />
	<adk:func name="selectNode" param="id,type" submit="yes" />
</adk:form>
<adk:form name="frmdoClose" action="doClose">
	<adk:func name="doClose" submit="yes" />
</adk:form>
<div align="right">
	<button type="button" class="btn back2List" onclick="${adk:func('doClose')}()">返回</button>
</div>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr>
		<c:if test="${not empty campaign.id}">
		<td  style="vertical-align:top">
			<div id="${adk:encodens('campaigntree')}" style="max-width:800px;width:aoto; height: auto; margin: 0; overflow: auto; background-color:#FFF; border: solid 1px #e7e7e7; max-height:420px"></div>
		</td>
		</c:if>
		<td width="80%" style="vertical-align:top">
			<adk:include view="edit" var="edit">x</adk:include>
			${edit}
		</td>
	</tr>
</table>
<script type="text/javascript">
	$(function() {
		if ($("#${adk:encodens('campaigntree')}").jstree == undefined)
			window.setTimeout(arguments.callee, 300);
		else {
			$("#${adk:encodens('campaigntree')}").jstree({
				"json_data" : {
							"data" :[
								{"attr" : {"id" : "${campaign.id}Campaign",
										   "cid" : "${campaign.id}",
										   "type":"${m.editCampaignType}"
										  },
								 "data" : {"title" : "${campaign.name}",
										   "attr" : {"href" : "#"}
										  },
								 "state" : "closed"
								}
									],
							"ajax" :{
										"url" : "${adk:resproxy('content')}",
										"data" : function(n) {
														return {
															id : n.attr ? n.attr("cid") : 0,
															type : n.attr ? n.attr("type") : 0,
															rand : new Date().getTime()
														};
													}
									 }
				},
				"ui" : {
					"select_limit" : 1,
					"initially_select" : ["${campaign.id}"]
				},
				"plugins" : ["themes","json_data","ui","cookies"], 
				"cookies" : {save_opened : "${adk:encodens('campaigntree')}_opened",
							save_selected : "${adk:encodens('campaigntree')}_selected"},
			})
			.delegate("a","click.jstree", function(event) {
				var id = $(event.target).parents('li').attr('cid');
				var type = $(event.target).parents('li').attr('type');
				if(id != undefined)
					 ${adk:func("selectNode")}(id,type);
		    }).bind("loaded.jstree",function(event,data){
				if("${m.type}" == "${m.editCampaignType}" && "${editId}" != ""){
			    	$(".jstree-clicked").removeClass();
					$.jstree._focused().select_node("#"+"${editId}"+"Campaign");
					 
				}else if("${m.type}" == "${m.editPhaseType}" && "${editId}" != ""){
					$(".jstree-clicked").removeClass();
					$.jstree._focused().open_node("#"+"${openId}"+"Campaign");
					setTimeout(function () { $.jstree._focused().select_node("#"+"${editId}"+"Phase"); }, 500);
					
				}
		    }).one("reopen.jstree", function (event, data) { 
		    	if("${m.type}" == "${m.editActivityType}" && "${editId}" != ""){
		    		$(".jstree-clicked").removeClass();
					$.jstree._focused().open_node("#"+"${openId}"+"Phase");
					setTimeout(function () { $.jstree._focused().select_node("#"+"${editId}"+"Activity"); }, 600);
				}
		    });
		}
			
	})
</script>