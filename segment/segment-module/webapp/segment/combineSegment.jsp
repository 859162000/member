<%@ page import="java.util.*" %>
<%@ page import="com.wanda.ccs.member.ap2in.*" %>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%
boolean testFlag = ("true".equals(request.getParameter("test")));//用户测试的标志!!!!!!!!!!!!!
boolean fromAp2 = AuthUserHelper.isFromAp2(request);//是否为AP2发来的请求
UserProfile user = AuthUserHelper.getUser();
boolean viewRight = user.getRights().contains("member.segment.view");
boolean editRight = user.getRights().contains("member.segment.edit");

String context;
if(fromAp2) {
	context = "/ap2/proxy";
}
else {
	context = request.getContextPath();
}
%>

<script type="text/javascript">
<!--

//-->
</script>

<div id="inputCombineDialog" title="复合客群信息">
   	<form id="inputCombineForm">
   		<input name="segmentId" type="hidden" wrType="text"/>
   		<input name="version" type="hidden" wrType="text"/>
   		
   		<table width="100%">
   			<tbody>
			<tr>
			  <td width="20%" align="right">编码: </td>
			  <td width="30%" align="left"><div name="code" wrType="readtext"></div></td>
			  <td width="20%" align="right">名称: </td>
			  <td width="40%" align="left"><input name="name" type="text" wrType="text" />
			  </td>
			</tr>
			<tr id="calCountRow">
			  <td width="20%" align="right">实际数量: </td>
			  <td width="30%" align="left">
				  <span name="calCount" wrType="readtext"></span>
			  </td>
			  <td width="20%" align="right">计算时间: </td>
			  <td width="30%" align="left">
				  <div name="calCountTime" wrType="readtext"></div>
			  </td>
			</tr>
			<tr id="controlCountRow">
			  <td width="25%" align="right">对比组占比: </td>
			  <td width="15%" align="left">
				<select name="controlCountRate" wrType="select">
					<option value="0">0%</option>
					<option value="10">10%</option>
					<option value="20">20%</option>
				</select>
			  </td>
			  <td width="25%" align="right">对比组数量: </td>
			  <td width="350%" align="left">
				  <div name="controlCount" wrType="readtext"></div>
			  </td>
			</tr>
			<tr>
				  <td width="25%" align="right">敏感词编码: </td>
				  <td width="10%" align="left"><input name="wordId" type="text" wrType="text" /></td>
				  <td width="25%" align="right"> </td>
				  <td width="40%" align="left">
				  </td>
				  
				</tr>
			</tbody>
   		</table>
   		<table>
   		<tr><td>
   		<div id="compositeSegmentItem" wrType="composite" wrParam="compositeId:segment;"></div>
   		</td>
   		<td><button id="calSubChangeBtn" type="button" wrType="button">计算获得失去</button></td>
   		</tr>
   		</table>
		<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" style="width: 692px;">
			<div style="width: 692px;" class="ui-state-default ui-jqgrid-hdiv">
				<div class="ui-jqgrid-hbox">
					<table cellspacing="0" cellpadding="0" border="0"
						aria-labelledby="gbox_scheme" role="grid" style="width: 673px"
						class="ui-jqgrid-htable">
						<thead>
							<tr role="rowheader" class="ui-jqgrid-labels">
								<th class="ui-state-default ui-th-column ui-th-ltr" style="width: 110px;" >编码</th>
								<th class="ui-state-default ui-th-column ui-th-ltr" style="width: 140px;" >名称</th>
								<th class="ui-state-default ui-th-column ui-th-ltr" style="width: 90px;" >实际数量</th>
								<th class="ui-state-default ui-th-column ui-th-ltr" style="width: 100px;" >模式</th>
								<th class="ui-state-default ui-th-column ui-th-ltr" style="width: 90px;" >获得/失去</th>
								<th class="ui-state-default ui-th-column ui-th-ltr" style="width: 100px;" >操作</th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
			<div class="ui-jqgrid-bdiv" style="height: 200px; width: 692px;">
				<table cellspacing="0" cellpadding="0" border="0" class="ui-jqgrid-btable" style="width: 673px;" id="segments">
					<tr class="ui-widget-content jqgrow ui-row-ltr" tabindex="-1" style="display: none;" role="row" id="template">
						<td name="segmentId" style="display: none;"></td>
						<td name="code">&nbsp;</td>
						<td name="name">&nbsp;</td>
						<td name="calCount">&nbsp;</td>
						<td name="type" title="" style="text-align: left;">
						<!-- 
						<select><option value="UNION">合集</option><option value="INTERSECT">交集</option><option value="MINUS">差集</option></select>
						 -->
						<select name="combineType" wrType="select" wrParam="sourceId:dimdef;typeId:2017">
					  		<option value="--">--</option>
					  	</select>
					  	
					  	<img src="" height="25" width="35" style="display: none;"></img></td>
					  	<!-- 
						<img src="../images/set-union.png" height="25" width="35"></img></td>
						 -->
						<td name="countalter"></td>
						<td >
							<button name="onSubUpBtn"  type="button" wrType="button" wrParam="icon:ui-icon-arrowthick-1-n;text:false" style="height:22px;" title="上移"/>
							<button name="onSubDownBtn"  type="button" wrType="button" wrParam="icon:ui-icon-arrowthick-1-s;text:false" style="height:22px;" title="下移"/>
							<button name="onSubDeleteBtn"  type="button" wrType="button" wrParam="icon:ui-icon-trash;text:false" style="height:22px;" title="删除"/>
							&nbsp;
						</td>
					</tr>
					<tr style="height: auto" role="row" class="jqgfirstrow" id="title">
						<td style="height: 0px; width: 110px;"></td>
						<td style="height: 0px; width: 140px;"></td>
						<td style="height: 0px; width: 90px;"></td>
						<td style="height: 0px; width: 100px;"></td>
						<td style="height: 0px; width: 90px;"></td>
						<td style="height: 0px; width: 100px;"></td>
					</tr>
				</table>
			</div>
		</div>
	</form>
</div>

<div id="dialog_cal" title="计算获得失去">
  <p>
    <span style="font-size: 15px;font: bold;">复合客群计算中请耐心等待...</span>
  </p>
</div>

