<?xml version='1.0' encoding='UTF-8'?>
<!DOCTYPE adb SYSTEM "adb_db.dtd">

<adb table="T_PROVINCE" timestamp="UPDATETIME">
	<propdef property="seqId" primary="yes" />
	<propdef property="code" />
	<propdef property="name" />
	<propdef property="area" />
	<propdef property="isDelete" />
	
	<retrievemulti id="loadByCode" debug="yes">
		<key>
			<property name="isDelete" />
		</key>
		<order value="code asc" id="0" />
		
	</retrievemulti>
</adb>