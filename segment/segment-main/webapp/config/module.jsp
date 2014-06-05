<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Restful Example--User Management</title>

<jsp:include page="../header.jsp"><jsp:param name="groups" value="jquery,jquery-ui,common"/></jsp:include>

<script type="text/javascript">
	$(document).ready(function(){

		var getModulesURL = 'ModuleAction/getModules.do';
		var reloadModuleURL = 'ModuleAction/reloadModule.do?moduleId=';
		var reloadContainerURL ='ModuleAction/reloadContainer.do';
		
		//Load the modules, and list module informations into table "#moduleListTable"
		$.ajax({
			url: getModulesURL,
			success: function(result){
				for (var i = 0 ; i < result.length; i++) {
					var rmpString = '{includes:"';
					var includes = result[i].resourcePattern.includes;
					var excludes = result[i].resourcePattern.excludes;
					for(var j=0 ; j < includes.length ; j++) {
						rmpString += includes[j];
						if(j != includes.length -1) {
							rmpString += ',';
						}
					}
					if(excludes != null && excludes.length > 0) {
						rmpString += '", excludes:"';
						for(var k=0 ; k < excludes.length ; k++) {
							rmpString += excludes[k];
							if(k != excludes.length -1) {
								rmpString += ',';
							}
						}
					}
					
					rmpString += '"}';
					
					$('#moduleListTbody').append('<tr><td>'
							+ result[i].id +'</td><td>'
							+ result[i].autoReload +'</td><td>'
							+ rmpString + '</td><td>'
							+ result[i].classPaths  + '</td><td>'
							+ '<button class="onModuleReload" moduleId="' + result[i].id + '">Reload</button></td>'
							+ '</td></tr>');
					$('.onModuleReload[moduleId="' + result[i].id +'"]').button();
				}
			}
		});
		
		$(".onModuleReload").live("click", function(){
			var moduleId = $(this).attr('moduleId');
			
			$.ajax({
				type: 'GET',
				url: reloadModuleURL + moduleId,
				success: function(result){
					if(result != null && result.level == 'INFO') {
						$.msgBox('info', 'Information', result.message);
					}
					else {
						$.msgBox('warn', 'Waring', result.message);
					}
				}
			});
			
			return false;
		});
		
		$("#onContainerReload").click(function(){
			$.ajax({
				type: 'GET',
				url: reloadContainerURL,
				success: function(result){
					if(result != null && result.level == 'INFO') {
						$.msgBox('info', 'Information', result.message);
					}
					else {
						$.msgBox('warn', 'Waring', result.message);
					}
				}
			});
			
			return false;
		});
		

		$('button').button();
	});

</script>

</head>
<body>
<center>
<div class="contentArea">
	<div  style="text-align:left">
		<h1>Pathlet Container:</h1>
		<table class="ui-widget ui-widget-content">
			<tr>
				<td><button id="onContainerReload" style="text-align:left">Reload Container</button></td>
				<td>To load configuration and resources in container.</td>
			</tr>
		</table>
		<p></p>
		<p></p>
		<h1>Pathlet modules:</h1>
		<div>- List all running modules in container. <div>
		<div>&nbsp;&nbsp;Click "Reload" button to reload designated module, all resources and classes defined with this module will be reloaded.</div>
	</div>
	<table id="moduleListTable" class="ui-widget ui-widget-content" width="100%">
		<thead>
			<tr class="ui-widget-header ">
				<th>ModuleID</th>
				<th>AutoReload</th>
				<th>ResourcePattern</th>
				<th>ClassPaths</th>
				<th>Operation</th>
			</tr>
		</thead>
		<tbody id="moduleListTbody">
		</tbody>
	</table>

</div>
</center>
</body>
</html>

	