<?xml version='1.0' encoding='UTF-8'?>
<!DOCTYPE adb SYSTEM "adb_db.dtd">

<adb table="T_AUTH_USER_ACCESS">
	<propdef property="id" primary="yes" sequence="S_T_AUTH_USER_ACCESS" column="AUTH_USER_ACCESS_ID"/>
	<propdef property="serverName" column="SERVER_NAME"/>
	<propdef property="requestId" column="REQUEST_ID"/>
	<propdef property="processTime" column="PROCESS_TIME"/>
	<propdef property="requestIp" column="REQUEST_IP"/>
	<propdef property="markup" column="MARKUP"/>
	<propdef property="requestUri" column="REQUEST_URI"/>
	<propdef property="controlPath" column="CONTROL_PATH"/>
	<propdef property="requestPath" column="REQUEST_PATH"/>
	<propdef property="execute" column="EXECUTE"/>
	<propdef property="retCode" column="RET_CODE"/>
	<propdef property="retMethod" column="RET_METHOD"/>
	<propdef property="retUrl" column="RET_URL"/>
	<propdef property="logMessage" column="LOG_MESSAGE"/>
	<propdef property="requestParams" column="REQUEST_PARAMS"/>
	<propdef property="exception" column="EXCEPTION"/>
	<propdef property="exceptionDetail" column="EXCEPTION_DETAIL"/>
	<propdef property="cookies" column="COOKIES"/>
	<propdef property="requestTime" column="REQUEST_TIME"/>
	<propdef property="createTime" column="CREATE_TIME"/>
	<propdef property="userId" column="USER_ID"/>
	<propdef property="userName" column="USER_NAME"/>
	<propdef property="userLevel" column="USER_LEVEL"/>
	<propdef property="userRegion" column="USER_REGION"/>
	<propdef property="userCinema" column="USER_CINEMA"/>
	<propdef property="posType" column="POS_TYPE"/>
	<propdef property="opGroup" column="OP_GROUP"/>
	<propdef property="opStation" column="OP_STATION"/>
</adb>