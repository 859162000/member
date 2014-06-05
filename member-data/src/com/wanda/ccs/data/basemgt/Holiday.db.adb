<?xml version='1.0' encoding='UTF-8'?>
<!DOCTYPE adb SYSTEM "adb_db.dtd">
<adb table="T_HOLIDAYS" timestamp="UPDATETIME" debug="yes">
	<propdef property="seqId" primary="yes" sequence="S_T_DIMDEF"/>
	<propdef property="name"/>
	<propdef property="startDate"/>
	<propdef property="endDate"/>
	<propdef property="isDelete"/>
	<propdef property="holidayType"/>
	
	<propdef property="searchHolidayType" column="holidayType" r="no" rm="no" c="no"
		u="no" d="no" />
	<propdef property="searchYear" column="to_char(startDate,'yyyy')" r="no" rm="no" c="no"
		u="no" d="no" />
	<retrievemulti id="loadHoliday" debug="yes">
		<key>
			<property name="isDelete"/>
			<property name="searchHolidayType" flag="holidayType"/>
			<property name="searchYear" flag="holidayYear"/>
		</key>
	</retrievemulti>
	<retrievemulti id="loadHolidayByYear" debug="yes">
		<key>
			<property name="isDelete"/>
		</key>
	</retrievemulti>
	<update id="updateIsDelete" ondefault="no" debug="yes">
		<key>
			<property name="seqId" />
		</key>
		<set>
			<property name="isDelete" />
		</set>
	</update>
</adb>