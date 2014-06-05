<?xml version='1.0' encoding='UTF-8'?>
<!DOCTYPE adb SYSTEM "adb_db.dtd">

<adb table="T_CITY" timestamp="UPDATETIME">
	<propdef property="seqId" primary="yes" sequence="S_T_CITY" />
	<propdef property="provinceId" />
	<propdef property="area" />
	<propdef property="code" />
	<propdef property="name" />
	<propdef property="cityLevel" />
	<propdef property="isDelete" />
	<propdef property="searchArea" column="area" r="no" rm="no" c="no"
		u="no" d="no" />
	<propdef property="searchProvince" column="provinceId" r="no"
		rm="no" c="no" u="no" d="no" />
	<propdef property="searchName" column="name" r="no" rm="no" c="no"
		u="no" d="no" />
	<propdef property="searchCode" column="code" r="no" rm="no" c="no"
		u="no" d="no" />

	<retrieve id="checkCode" ondefault="no" debug="yes">
		<key>
			<property name="code" />
			<property name="isDelete"/>
		</key>
		<get>
			<property name="seqId" />
		</get>
	</retrieve>

	<retrievemulti id="loadByType">
		<key>
			<property name="searchArea" flag="areaCode" />
			<property name="searchProvince" flag="province" />
			<property name="searchCode" flag="code" match="like"/>
			<property name="searchName" flag="name" match="like"/>
			<property name="isDelete" />
		</key>
		<order value="code asc" id="0" />
		<order value="code desc" id="1" />
		<order value="name asc" id="2" />
		<order value="name desc" id="3" />
		<order value="area asc" id="4" />
		<order value="area desc" id="5" />
		<order value="provinceId asc" id="6" />
		<order value="provinceId desc" id="7" />
		<order value="cityLevel asc" id="8" />
		<order value="cityLevel desc" id="9" />
	</retrievemulti>

	<update id="updateIsDelete" ondefault="no">
		<key>
			<property name="seqId" />
		</key>
		<set>
			<property name="isDelete" />
		</set>
	</update>
</adb>