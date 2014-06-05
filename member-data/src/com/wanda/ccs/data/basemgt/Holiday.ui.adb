<?xml version='1.0' encoding='UTF-8'?>

<adb>
	<uiwrapper name="def" />
	<property name="seqId" id="sid" />
	<property name="searchYear" id="searchYear" wrap="def" wrapname="year">
		<label lsid="CN">年度</label>
		<label lsid="EN">Year</label>
		<in>
			<select />
		</in>
	</property>
	<property name="searchHolidayType"  id="searchHolidayType" wrap="def" wrapname="holidayType">
		<label lsid="CN">类型</label>
		<label lsid="EN">HolidayType</label>
		<in>
			<select />
		</in>
	</property>
	<property name="name" id="name" mandatory="yes">
		<label lsid="CN">名称</label>
		<label lsid="EN">Name</label>
		<in>
			<text width="20" />
			<validator id="ne" skip="next">
				<msg>请输入名称。</msg>
			</validator>
			<validator id="maxlen" skip="next">
				<arg>20</arg>
				<msg>名称长度不能超过20个字节。</msg>
			</validator>
		</in>
		
	</property>
	<property name="holidayType" id="holidayType" wrap="def" wrapname="holidayType" mandatory="yes">
		<label lsid="CN">类型</label>
		<label lsid="EN">HolidayType</label>
		<in>
			<select />
			<validator id="ne" skip="next">
				<msg>请选择类型。</msg>
			</validator>
		</in>
	</property>
	<property name="year" id="year" mandatory="yes" >
		<label lsid="CN">年度</label>
		<label lsid="EN">Year</label>
	</property>
	<property name="startDate" id="startDate" mandatory="yes" >
		<label lsid="CN">开始日期</label>
		<label lsid="EN">StartDate</label>
		<in>
			<validator id="ne" skip="next">
				<msg>请输入开始日期。</msg>
			</validator>
			<date format="yyyy-MM-dd" width="10" maxlength="10">
				<error>请输入日期，格式如2010-12-01</error>
			</date>
			<validator  method="checkStartDate">
				<msg>开始日期应该小于等于结束日期</msg>
			</validator>
		</in>
		<out>
			<deco type="format">yyyy-MM-dd</deco>
		</out>
	</property>
	<property name="endDate" id="endDate" mandatory="yes">
		<label lsid="CN">结束日期</label>
		<label lsid="EN">EndDate</label>
		<in>
			<validator id="ne" skip="next">
				<msg>请输入结束日期。</msg>
			</validator>
			<date format="yyyy-MM-dd" width="10" maxlength="10">
				<error>请输入日期，格式如2010-12-01</error>
			</date>
			<validator  method="checkEndDate">
				<msg>结束日期应该大于等于开始日期</msg>
			</validator>
		</in>
		<out>
			<deco type="format">yyyy-MM-dd</deco>
		</out>
	</property>
	
	
	<group id="search">
		<propref name="searchYear" />
		<propref name="searchHolidayType" />
	</group>
	<group id="list">
		<propref name="year" />
		<propref name="name"/>
		<propref name="holidayType" />
		<propref name="startDate" />
		<propref name="endDate" />
	</group>
	<group id="edit">
		<propref name="name"/>
		<propref name="holidayType" />
		<propref name="startDate" />
		<propref name="endDate" />
	</group>

</adb>
