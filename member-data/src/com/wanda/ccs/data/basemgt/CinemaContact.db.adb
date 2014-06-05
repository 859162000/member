<?xml version='1.0' encoding='UTF-8'?>
<!DOCTYPE adb SYSTEM "adb_db.dtd">
<adb table="T_CINEMA_CONTACT" timestamp="UPDATE_TIME" debug="yes">
	<propdef property="seqId" primary="yes" sequence="S_T_CINEMACONTACT" />
	<propdef property="cinema_Id" />
	<propdef property="name" />
	<propdef property="phone" />
	<propdef property="email" />
	<propdef property="duty" />
	<propdef property="address" />
	<propdef property="post_Code" />
	<propdef property="isDelete" />
	<propdef property="sex" />
	<propdef property="mobile" />
	

	
	<retrievemulti id="loadByType">
		<key>
			<property name="seqId" />
		</key>
		<order value="name asc" id="0" />
		<order value="name desc" id="1" />
		<order value="sex asc" id="2" />
		<order value="sex desc" id="3" />
		<order value="duty asc" id="4" />
		<order value="duty desc" id="5" />
		<order value="phone asc" id="6" />
		<order value="phone desc" id="7" />
		<order value="mobile asc" id="8" />
		<order value="mobile desc" id="9" />
		<order value="email asc" id="10" />
		<order value="email desc" id="11" />
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