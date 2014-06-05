<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Restful Example--User Management</title>

<jsp:include page="../header.jsp"><jsp:param name="groups" value="jquery,jquery-ui,validation,common"/></jsp:include>

<script type="text/javascript">
	$(document).ready(function(){

		var getCurrentConfigURL = 'DataSourceAction/getCurrentConfig.do';
		var getH2DefaultConfigURL = 'DataSourceAction/getH2DefaultConfig.do';
		var getMySQLDefaultConfigURL = 'DataSourceAction/getMySQLDefaultConfig.do';
		var testConnectionURL = 'DataSourceAction/testConnection.do';
		var applyConfigURL = 'DataSourceAction/applyConfig.do';
		var initialzeDatabaseURL = 'DataSourceAction/initialzeDatabase.do';
		
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
				'Reset': resetConfig,
			  	'Test Connection': testConnection,
				'Apply': applyConfig,
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
			$.msgBox('confirm', 'Confirmation', 'This database data will be initialized and all data exists will be deleted! <br/>Do you want continue?',  function(result) { 
				if(result == true) {
					$.ajax({
						url: initialzeDatabaseURL,
						success: function(result){
							if(result.level == 'INFO') {
								$('#dataInitialStatus').html('Initialized');
								$.msgBox('info', '', result.message );
							}
							else {
								$.msgBox('error', '', result.message );
							}
						}
					});
				}
				
			});
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
			else {
				$.msgBox('error', '', 'Do not support the database type: ' + type );
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
						$('#dataInitialStatus').html('Uninitialized');
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
<div style="text-align:left">
	<h1>Current Data Source Setting</h1>
	
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
	<button id="onInputDataSource">Change Data Source Setting</button>
	<br/>
	<br/>
	<br/>
	<h1>Database data</h1>
	<table class="ui-widget ui-widget-content" width="400" cellspacing="0">
		<tr>
			<td class="ui-widget-content" width="120">Database initial data status: </td>
			<td class="ui-widget-content" id="dataInitialStatus"></td>
		</tr>
	</table>
	<button id="onInitialize">Initialize Database</button>
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
				<option value="H2">H2</option>
				<option value="MySQL">MySQL</option>
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

	