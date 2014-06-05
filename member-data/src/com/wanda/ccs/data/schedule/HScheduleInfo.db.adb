<?xml version='1.0' encoding='UTF-8'?>
<!DOCTYPE adb SYSTEM "adb_db.dtd">
<adb table="T_H_SCHEDULE_INFO" timestamp="UPDATETIME">
	<propdef property="seqId" primary="yes" sequence="S_T_H_SCHEDULE_INFO" />
	<propdef property="approved" />
	<propdef property="approvedBy" />
	<propdef property="approvedTime" />
	<propdef property="createBy" />
	<propdef property="createTime" />
	<!-- <propdef property="deleted" /> -->
	<propdef property="month" />
	<propdef property="status" />
	<propdef property="submit" />
	<propdef property="submitBy" />
	<propdef property="submitTime" />
	<propdef property="updateBy" />
	<propdef property="updateTime" />
	<propdef property="version" />

	<retrievemulti id="loadByCondition">
		<key>
			<property name="month" />
			<property name="status" flag="status" />
			<property name="version" flag="version" />
		</key>
		<order value="month asc" id="0" />
		<order value="month desc" id="1" />
		<order value="status asc" id="2" />
		<order value="status desc" id="3" />
		<order value="version asc" id="4" />
		<order value="version desc" id="5" />
		<order value="submitTime asc" id="6" />
		<order value="submitTime desc" id="7" />
		<order value="submitBy asc" id="8" />
		<order value="submitBy desc" id="9" />
		<order value="approvedTime asc" id="10" />
		<order value="approvedTime desc" id="11" />
		<order value="approvedBy asc" id="12" />
		<order value="approvedBy desc" id="13" />
	</retrievemulti>
</adb>