<?xml version='1.0' encoding='UTF-8'?>

<adb>
	<uiwrapper name="def" />
	<property name="seqId" id="sid" />

	<property name="searchArea" wrap="def" wrapname="area" id="area">
		<label lsid="CN">区域</label>
		<label lsid="EN">Area</label>
		<in>
			<select />
		</in>
	</property>
	


	<property name="searchName" get="getName" set="setSearchName"
		id="name">
		<label lsid="CN">名称</label>
		<label lsid="EN">Name</label>

		<in>
			<text width="10" maxlength="10" />
		</in>
	</property>

	<property name="code" id="code" mandatory="yes">
		<label>编码</label>

		<out>
			<align>left</align>
		</out>

		<in>
			<text width="10" maxlength="10" />

			<validator id="ne" skip="next">
				<msg>请输入编码。</msg>
			</validator>
			<!-- <validator skip="next" method="checkCode">
				<msg>该编码已经存在!</msg>
			</validator>
			 -->
		</in>
	</property>

	<property name="inner_Code" id="innerCode" mandatory="yes">
		<label>内码</label>

		<out>
			<align>left</align>
		</out>

		<in>
			<text width="10" maxlength="10" />

			<validator id="ne" skip="next">
				<msg>请输入内码。</msg>
			</validator>
		</in>
	</property>
	
	<property name="pin_Code" id="pinCode" mandatory="yes">
		<label>拼码</label>

		<out>
			<align>left</align>
		</out>

		<in>
			<text width="10" maxlength="10" />

			<validator id="ne" skip="next">
				<msg>请输入编码。</msg>
			</validator>
		</in>
	</property>
	<property name="short_Name" id="shortName" mandatory="yes">
		<label>简称</label>
		
		<out>
			<align>left</align>
		</out>

		<in>
			<text width="10" maxlength="10" />

			<validator id="ne" skip="next">
				<msg>请输入简称。</msg>
			</validator>
		</in>
	</property>
	<property name="name" id="name" mandatory="yes">
		<label>名称</label>

		<out>
			<align>left</align>
		</out>

		<in>
			<text width="10" maxlength="10" />

			<validator id="ne" skip="next">
				<msg>请输入名称。</msg>
			</validator>
		</in>
	</property>

	<property name="cinema_Attr" wrap="def" wrapname="cinemaAttr" id="cinemaAttr" mandatory="yes">
		<label>属性</label>

		<out>
			<align>left</align>
		</out>

		<in>
			<select />

			<validator id="ne" skip="next">
				<msg>请选择影院属性。</msg>
			</validator>
		</in>
	</property>
	
	<property name="cinema_Level" wrap="def" wrapname="cinemaLevel" id="cinemaLevel" mandatory="yes">
		<label>级别</label>

		<out>
			<align>left</align>
		</out>

		<in>
			<select />

			<validator id="ne" skip="next">
				<msg>请选择影院级别。</msg>
			</validator>
		</in>
	</property>
	
	<property name="cinema_Type" wrap="def" wrapname="cinemaType" id="cinemaType" mandatory="yes">
		<label>类别</label>

		<out>
			<align>left</align>
		</out>

		<in>
			<select />

			<validator id="ne" skip="next">
				<msg>请选择物业类别。</msg>
			</validator>
		</in>
	</property>
	


	<property name="isOpen" wrap="def" wrapname="isOpen" id="isOpen" mandatory="yes">
		<label>是否开业</label>

		<out>
			<align>left</align>
		</out>

	</property>
	
	<property name="province" wrap="def" wrapname="province" id="province" mandatory="yes">
		<label>省/直辖市</label>

		<out>
			<align>left</align>
		</out>

		<in>
			<select />
		</in>
	</property>
	
	<property name="city" wrap="def" wrapname="city" id="city" mandatory="yes">
		<label>市</label>

		<out>
			<align>left</align>
		</out>

		<in>
			<select />
		</in>
	</property>
	
	<property name="area" wrap="def" wrapname="area" id="area" mandatory="yes">
		<label>区域</label>

		<out>
			<align>left</align>
		</out>

		<in>
			<select />

			<validator id="ne" skip="next">
				<msg>请选择区域。</msg>
			</validator>
		</in>
	</property>
	<property name="address" id="address" mandatory="yes">
		<label>详细地址</label>

		<out>
			<align>left</align>
		</out>

		<in>
			<text width="10" maxlength="10" />

			<validator id="ne" skip="next">
				<msg>请输入详细地址。</msg>
			</validator>
		</in>
	</property>
	
	<property name="post_Code" id="postCode" mandatory="yes">
		<label>邮政编码</label>

		<out>
			<align>left</align>
		</out>

		<in>
			<text width="10" maxlength="10" />

			<validator id="ne" skip="next">
				<msg>请输入邮政编码。</msg>
			</validator>
		</in>
	</property>
	
	<property name="cinema_Type" id="cinemaType" mandatory="yes">
		<label>类别</label>

		<out>
			<align>left</align>
		</out>

		<in>
			<text width="10" maxlength="10" />

			<validator id="ne" skip="next">
				<msg>请输入名称。</msg>
			</validator>
<!--			<validator skip="next" method="checkCode">-->
<!--				<msg>该名称已经存在!</msg>-->
<!--			</validator>-->
		</in>
	</property>
	
	<property name="hall_Count" id="hallCount" mandatory="yes">
		<label>厅数</label>

		<out>
			<align>left</align>
		</out>

		<in>
			<text width="10" maxlength="10" />

			<validator id="ne" skip="next">
				<msg>请输入名称。</msg>
			</validator>
<!--			<validator skip="next" method="checkCode">-->
<!--				<msg>该名称已经存在!</msg>-->
<!--			</validator>-->
		</in>
	</property>
	
	<property name="seat_Count" id="seatCount" mandatory="yes">
		<label>座位数</label>

		<out>
			<align>left</align>
		</out>

		<in>
			<text width="10" maxlength="10" />

			<validator id="ne" skip="next">
				<msg>请输入名称。</msg>
			</validator>
<!--			<validator skip="next" method="checkCode">-->
<!--				<msg>该名称已经存在!</msg>-->
<!--			</validator>-->
		</in>
	</property>
	<group id="search">
		<propref name="searchArea" />
		
		
		<propref name="searchName" />
	</group>

	<group id="list">
		<propref name="province" sortasc="0" sortdesc="1" />
		<propref name="cinema_Level" sortasc="6" sortdesc="7" />
		<propref name="cinema_Attr" sortasc="8" sortdesc="9" />
		<propref name="code" sortasc="4" sortdesc="5" />
		<propref name="name" sortasc="2" sortdesc="3" />
		<propref name="cinema_Type" sortasc="10" sortdesc="11" />
		<propref name="hall_Count" sortasc="12" sortdesc="13" />
		<propref name="seat_Count" sortasc="14" sortdesc="15" />
	</group>
	
	

	<group id="edit">
		<propref name="code" />
		<propref name="inner_Code" />
		
		<propref name="pin_Code" />
		<propref name="short_Name" />
		<propref name="name" />

		<propref name="cinema_Attr" />
		<propref name="cinema_Level" />
		<propref name="cinema_Type" />
<!--	单位名称	-->
<!--		<propref name="code" />-->
<!--	销售系统	-->
<!--		<propref name="code" />-->
<!--  税率	-->
<!--		<propref name="name" />-->
		
		<propref name="isOpen" />
		<propref name="province" />
		<propref name="city" />
		<propref name="area" />
		<propref name="address" />
		<propref name="post_Code" />
	</group>
</adb>
