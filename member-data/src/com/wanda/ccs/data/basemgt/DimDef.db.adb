<?xml version='1.0' encoding='UTF-8'?>
<!DOCTYPE adb SYSTEM "adb_db.dtd">

<adb table="T_DIMDEF" timestamp="UPDATETIME" debug="yes">
	<propdef property="seqId" primary="yes" sequence="S_T_DIMDEF" />
	<propdef property="code" />
	<propdef property="name" />
	<propdef property="typeId" />
	<propdef property="parent" />
	<propdef property="validStart" />
	<propdef property="validEnd" />
	<propdef property="isDelete" />
	<propdef property="create_Date" />
	<propdef property="update_Date" />
	<propdef property="searchName" column="name" r="no" rm="no" c="no"
		u="no" d="no" />
	
	<propdef property="searchCode" column="code" r="no" rm="no" c="no"
		u="no" d="no" />
	<propdef property="searchTypeId" column="typeId" r="no" rm="no" c="no"
		u="no" d="no" />

	<retrieve id="checkCode" ondefault="no" debug="yes">
		<key>
			<property name="typeId" />
			<property name="code" />
		</key>
		<get>
			<property name="seqId" />
		</get>
	</retrieve>
	<retrievemulti id="loadByTypeId" debug="yes">
		<key>
			<property name="typeId" />
			
		</key>
	</retrievemulti>
	<retrievemulti id="loadByType" debug="yes">
		<key>
<!--			<property name="typeId" />-->
			<property name="isDelete" />
			<property name="searchTypeId" />
			<property name="searchCode" flag="code" match="like"/>
			<property name="searchName" flag="name" match="like"/>
		</key>
		<order value="code asc" id="0" />
		<order value="code desc" id="1" />
		<order value="name asc" id="2" />
		<order value="name desc" id="3" />
		
	</retrievemulti>

	<retrievemulti id="loadByTypeName" sql="">
	
	</retrievemulti>
	<retrievemulti id="loadNodeleteByTypeId" debug="yes">
		<key>
			<property name="typeId" />
			<property name="isDelete" />
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