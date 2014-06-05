<?xml version='1.0' encoding='UTF-8'?>
<!DOCTYPE adb SYSTEM "adb_db.dtd">

<adb table="T_CINEMA" timestamp="UPDATE_DATE" debug="yes">
	<propdef property="seqId" primary="yes" sequence="S_T_CINEMA" />
	<propdef property="pin_Code" />
	<propdef property="inner_Code" />
	<propdef property="code" />
	<propdef property="short_Name" />
	<propdef property="name" />
	<propdef property="isOpen" />
	<propdef property="open_Date" />
	<propdef property="contract_Begin" />
	<propdef property="contract_End" />
	<propdef property="hall_Count" />
	<propdef property="seat_Count" />
	<propdef property="address" />
	<propdef property="post_Code" />
	<propdef property="cinema_Level" />
	<propdef property="cinema_Attr" />
	<propdef property="supp_Type" />
	<propdef property="mkt_Type" />
	<propdef property="province" />
	<propdef property="city" />
	<propdef property="area" />
	<propdef property="cinema_Type" />
	<propdef property="searchArea" column="area" r="no" rm="no" c="no"
		u="no" d="no" />
	<propdef property="searchName" column="name" r="no" rm="no" c="no"
		u="no" d="no" />

	<retrieve id="checkCode" ondefault="no" debug="yes">
		<key>
			
			<property name="code" />
		</key>
		<get>
			<property name="seqId" />
		</get>
	</retrieve>
	
	<retrievemulti id="loadByType">
		<key>
			<property name="searchArea" flag="area" />
			<property name="searchName" flag="name" />
		</key>
		<order value="code asc" id="0" />
		<order value="code desc" id="1" />
		<order value="name asc" id="2" />
		<order value="name desc" id="3" />
		<order value="province asc" id="4" />
		<order value="province desc" id="5" />
		<order value="cinema_Level asc" id="6" />
		<order value="cinema_Level desc" id="7" />
		<order value="cinema_Attr asc" id="8" />
		<order value="cinema_Attr desc" id="9" />
		<order value="cinema_Type asc" id="10" />
		<order value="cinema_Type desc" id="11" />
		<order value="hall_Count asc" id="12" />
		<order value="hall_Count desc" id="13" />
		<order value="seat_Count asc" id="14" />
		<order value="seat_Count desc" id="15" />
		
	</retrievemulti>

</adb>