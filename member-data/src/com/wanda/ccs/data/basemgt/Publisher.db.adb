<?xml version='1.0' encoding='UTF-8'?>
<!DOCTYPE adb SYSTEM "adb_db.dtd">

<adb table="T_PUBLISHER" timestamp="UPDATETIME">
	<propdef property="seqId" primary="yes" sequence="S_T_PUBLISHER" />
	<propdef property="publisherCode" />
	<propdef property="publisherName" />
	<propdef property="contact" />
	<propdef property="contactPhone" />
	<propdef property="contactMobile" />
	<propdef property="contactEmail" />
	<propdef property="publisherPhone" />
	<propdef property="publisherFax" />
	<propdef property="publisherEmail" />
	<propdef property="publisherAddress" />
	<propdef property="bank" />
	<propdef property="bankAccount" />
	<propdef property="contactEmail1" />
	<propdef property="contactEmail2" />
	<propdef property="contactEmail3" />
	<propdef property="contactEmail4" />
	<propdef property="isEmail" />
	<propdef property="isEmail1" />
	<propdef property="isEmail2" />
	<propdef property="isEmail3" />
	<propdef property="isEmail4" />
	<propdef property="shortName" />
	<propdef property="pinCode" />
	<propdef property="isDelete" />
	<propdef property="searchCode" column="publisherCode" r="no" rm="no" c="no"
		u="no" d="no" />
	<propdef property="searchName" column="publisherName" r="no" rm="no" c="no"
		u="no" d="no" />
	<propdef property="searchPinCode" column="pinCode" r="no" rm="no" c="no"
		u="no" d="no" />
	<retrieve id="checkCode" ondefault="no">
		<key>
			<property name="publisherCode" />
			<property name="isDelete"/>
		</key>
		<get>
			<property name="seqId" />
		</get>
	</retrieve>

	<retrievemulti id="loadByType" debug="yes">
		<key>
			<property name="searchPinCode" flag="pinCode" match="like" escape="/" />
			<property name="searchCode" flag="code" match="like" escape="/" />
			<property name="searchName" flag="name" match="like" escape="/" />
			<property name="isDelete" />
		</key>
		<order value="publisherCode asc" id="0" />
		<order value="publisherCode desc" id="1" />
		<order value="publisherName asc" id="2" />
		<order value="publisherName desc" id="3" />
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