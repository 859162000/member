<?xml version='1.0' encoding='UTF-8'?>
<!DOCTYPE adb SYSTEM "adb_db.dtd">

<adb table="T_HALL" timestamp="UPDATETIME" debug="yes">
	<propdef property="seqId" primary="yes" sequence="S_T_HALL" />
	<propdef property="cinemaId" />
	<propdef property="name" />
	<propdef property="hallType" />
	<propdef property="belongsTo" />
	<propdef property="seatCount" />
	<propdef property="disabledSeatCount" />
	<propdef property="audioType" />
	<propdef property="projectType" />
	<propdef property="serverBrand" />
	<propdef property="projectBrand" />
	<propdef property="isDigital" />
	<propdef property="isRealD" />
	<propdef property="is3D" />
	<propdef property="isIMAX" />
	<propdef property="hallId" />
	<propdef property="isDelete" />
	
	<propdef property="searchName" column="name" r="no" rm="no" c="no" u="no" d="no" />
	<propdef property="searchCinemaId" column="cinemaId" r="no" rm="no" c="no" u="no" d="no" />

	<retrievemulti id="retrieveHalls" debug="yes">
		<key>
			<property name="searchCinemaId" />
			<property name="searchName" flag="name" />
			<property name="isDelete" />
		</key>
		<order value="name asc" id="0" />
		<order value="name desc" id="1" />
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