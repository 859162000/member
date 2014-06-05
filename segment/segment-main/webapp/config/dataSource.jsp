<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%
	boolean initialize = false;
	if(request.getParameter("initialize") != null) {
		initialize = true;
	}
	String context = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Restful Example--User Management</title>

<jsp:include page="../header.jsp"><jsp:param name="groups" value="jquery,jquery-ui,validation,common"/></jsp:include>

<script type="text/javascript">
	$(document).ready(function(){
		
		var getCurrentConfigURL = '<%=context%>/config/DataSourceAction/getCurrentConfig.do';
		var getH2DefaultConfigURL = '<%=context%>/config/DataSourceAction/getH2DefaultConfig.do';
		var getMySQLDefaultConfigURL = '<%=context%>/config/DataSourceAction/getMySQLDefaultConfig.do';
		var getOracleDefaultConfigURL = '<%=context%>/config/DataSourceAction/getOracleDefaultConfig.do';
		var testConnectionURL = '<%=context%>/config/DataSourceAction/testConnection.do';
		var applyConfigURL = '<%=context%>/config/DataSourceAction/applyConfig.do';
		var initialzeDatabaseURL = '<%=context%>/config/DataSourceAction/initialzeDatabase.do';
		var homeURL = '<%=context%>/signin.jsp';
		
		showConfig();

		$('button').button();
		
		
		var validator = $('#inputDataSourceForm').validate({
			 rules:{
	              "config.driverClass":{required:true},
	              "config.jdbcUrl":{required:true},
	              "config.username":{required:true}
	         }
	    });

		$('#inputDataSource').dialog({
			width: 645,
			buttons: {
				<% if(initialize == false) { %>'Reset': resetConfig,<% }  %>
			  	'Test Connection': testConnection,
				'Save & Apply': applyConfig,
				'Close': function() { $(this).dialog('close'); }
			},
			beforeClose: function(event, ui) { 
				validator.resetForm(); //After dialog closed, clean the validation error style and prompt text.
				$('#inputDataSourceForm').resetForm();
			}
		});
		
		$('#onInputDataSource').click(function() {
			resetConfig();
			$('#inputDataSource').dialog('open');
		});
		
		$('#onInitialize').click(function() {
			$.msgBox('confirm', 'Confirmation', 'It is going to create new tables into database! Be attention, the all existed tables will be removed in the process! <br/>Do you want continue?',  function(result) {
				if(result == true) {
					$.ajax({
						url: initialzeDatabaseURL,
						success: function(result){
							if(result.level == 'INFO') {
								$('#dataInitialStatus').html('Initialized');
								$.msgBox('info', '', result.message
									<% if(initialize == true) { %>
										,function(result) {
											$.msgBox('confirm', 'Confirmation','Do you want go to example home page now?', function(result) {
												if(result == true) {
													window.location.href = homeURL;
												}
											});
										}
									<% }  %>
								);
							}
							else {
								$.msgBox('error', '', result.message );
							}
						}
					});
				}
				
			});
		});
		
		$('#onStart').click(function() {
			window.location.href = homeURL;
		});
		
		
		
		$('#inputDataSourceForm select[name="config.databaseType"]').change(function() {
			var type = this.value;
			var url = "";
			if(type == "H2") {
				url += getH2DefaultConfigURL;
			}
			else if(type == "MySQL") {
				url += getMySQLDefaultConfigURL;
			}
			else if(type == "Oracle") {
				url += getOracleDefaultConfigURL;
			}
			else if(type == "") {
				//empty the fields.
				$('#inputDataSourceForm input[name="config.driverClass"]').val('');
				$('#inputDataSourceForm input[name="config.jdbcUrl"]').val('');
				$('#inputDataSourceForm input[name="config.username"]').val('');
				$('#inputDataSourceForm input[name="config.password"]').val('');
				return;
			}
			else {
				$.msgBox('error', '', 'Do not support the database type: ' + type );
				return;
			}
			
			url += '?config.databaseType=' + type;
			
			$.ajax({
				url: url,
				success: function(result){
					$('#inputDataSourceForm').deserialize(result, {inputPrefix:'config.'});
				}
			});
		})
		
		//Load and show the current DataSourceConfig.
		function showConfig() {
			$.ajax({
				url: getCurrentConfigURL,
				success: function(result){
					$('#databaseTypeText').html(result.databaseType);
					$('#driverClassText').html(result.driverClass);
					$('#jdbcUrlText').html(result.jdbcUrl);
					$('#usernameText').html(result.username);
					$('#passwordText').html(result.password);
					
					if(result.dataInitialized == true) {
						$('#dataInitialStatus').html('Initialized');
					}
					else {
						$('#dataInitialStatus').html('Uninitialize');
					}
				}
			});

		}
		
		function resetConfig() {
			$.ajax({
				url: getCurrentConfigURL,
				success: function(result){
					$('#inputDataSourceForm').deserialize(result, {inputPrefix:'config.'});
				}
			});
		}
		
		function applyConfig() {
			
			$.msgBox('confirm', 'Confirmation', 'This DataSource configuration will be applied now! <br/>Do your want continue?',  function(result) { 
				if(result == true){  
					var postData = $('#inputDataSourceForm').serialize();
					
					$.ajax({
						url: applyConfigURL,
						data: postData,
						success: function(result){
							if(result.level == 'INFO') {
								showConfig();
								$.msgBox('info', '', result.message );
							}
							else {
								$.msgBox('error', '', result.message );
							}
						}
					});
				}
			});

		}
		
		function testConnection() {
			var postData = $('#inputDataSourceForm').serialize();
			
			$.ajax({
				url: testConnectionURL,
				data: postData,
				success: function(result){
					if(result.level == 'INFO') {
						$.msgBox('info', '', result.message );
					}
					else {
						$.msgBox('error', '', result.message );
					}
				}
			});
		}
		
	});

</script>

</head>
<body>
<center>
<div class="contentArea">
<% if(initialize) { %>
<div style="text-align:left; font-size:14px;">
	In this example, we support two type databases: H2, MySQL and Oracle. 
<div>
<% } %>

<div style="text-align:left">
	<h1>Data Source Setting</h1>
  	<table class="ui-widget ui-widget-content" width="600" cellspacing="0">
		<tr>
			<td class="ui-widget-content" width="120">Database Type: </td>
			<td class="ui-widget-content" id="databaseTypeText"></td>
		</tr>
		<tr>
			<td class="ui-widget-content">Driver Class: </td>
			<td class="ui-widget-content" id="driverClassText"></td>
		</tr>
		<tr>
			<td class="ui-widget-content">JDBC URL: </td>
			<td class="ui-widget-content" id="jdbcUrlText"></td>
		</tr>
		<tr>
			<td class="ui-widget-content">Username: </td>
			<td class="ui-widget-content" id="usernameText"></td>
		</tr>
		<tr>
			<td class="ui-widget-content">Password:</td>
			<td class="ui-widget-content" id="passwordText"></td>
		</tr>
  	</table>
	<button id="onInputDataSource"><% if(initialize) { %> Step1: <% } %>Set Data Source</button>
	<br/>
	<br/>
	<br/>
	<h1>Database data Status</h1>
	<table class="ui-widget ui-widget-content" width="500" cellspacing="0">
		<tr>
			<td class="ui-widget-content" width="220">Database Data initial status: </td>
			<td class="ui-widget-content" id="dataInitialStatus"></td>
		</tr>
	</table>
	<button id="onInitialize"><% if(initialize) { %> Step2: <% } %>Initialize Database</button>
	
	<% if(initialize) { %>
	<h1>Start Tutorial!</h1>
	<button id="onStart"> Step3: Go to Sample Start Page</button>
	<% } %>
</div>
</div>
</center>

<div align="center" id="inputDataSource" title="Data Source Setting">
<form id="inputDataSourceForm">
<table class="ui-widget ui-widget-content"  width="630" align="center">
	<tr>
		<td width="120">Database Type: </td>
		<td align="left">
			<select name="config.databaseType">
				<option value="">Please select ...</option>
				<option value="H2">H2</option>
				<option value="MySQL">MySQL</option>
				<option value="MySQL">Oracle</option>
			</select>
		</td>
	</tr>
	<tr>
		<td>Driver Class: </td>
		<td align="left"><input name="config.driverClass" type="text" size="80"/></td>
	</tr>
	<tr>
		<td>JDBC URL: </td>
		<td align="left"><input name="config.jdbcUrl" type="text" size="80"/></td>
	</tr>
	<tr>
		<td>Username: </td>
		<td align="left"><input name="config.username" type="text" size="20"/></td>
	</tr>
	<tr>
		<td>Password:</td>
		<td align="left"><input name="config.password" type="text" size="20"/></td>
	</tr>
</table>
</form>
</div>

</body>
</html>

	