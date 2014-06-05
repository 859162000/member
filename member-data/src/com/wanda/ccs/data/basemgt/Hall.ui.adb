<?xml version='1.0' encoding='UTF-8'?>

<adb>
	<uiwrapper name="def" />
	<property name="seqId" id="sid" />

	<property name="searchCinemaId"  wrap="def" wrapname="cinemaId" id="cinemaId">
		<label lsid="CN">影院名称</label>
		<label lsid="EN">cinemaId</label>
		<in>
			<select />
		</in>
	</property>
	
	<property name="searchName"  id="name">
		<label lsid="CN">名称</label>
		<label lsid="EN">name</label>
		<in>
			<text width="10" maxlength="10" />
		</in>
	</property>
 
	<property name="name" id="name" mandatory="yes">
		<label lsid="CN">影厅名称</label>
		<label lsid="CN">name</label>
		<out>
			<align>left</align>
		</out>
		<in>
			<text width="10" maxlength="10" />

			<validator id="ne" skip="next">
				<msg lsid="CN">请输入名称。</msg>
				<msg lsid="EN">The field NAME must be inputted</msg>
			</validator>
		</in>
	</property>
	
	<property name="belongsTo" wrap="def" wrapname="belongsTo" id="belongsTo" mandatory="yes">
		<label lsid="CN">设备归属</label>
		<label lsid="EN">belongsTo</label>
		<in>
			<radio />
			<validator id="ne" skip="next">
				<msg lsid="CN">请选择设备归属。</msg>
				<msg lsid="EN">The field BELONGSTO must be selected</msg>
			</validator>
		</in>
	</property>
	
	<property name="hallType" wrap="def" wrapname="hallType" id="hallType" >
		<label lsid="CN">影厅类型</label>
		<label lsid="EN">hallType</label>
		<in>
			<select />
		</in>
	</property>
	
	<property name="audioType" wrap="def" wrapname="audioType" id="audioType" >
		<label lsid="CN">音响类型</label>
		<label lsid="EN">audioType</label>
		<in>
			<select />
		</in>
	</property>
	
	<property name="projectType" wrap="def" wrapname="projectType" id="projectType" >
		<label lsid="CN">放映制式</label>
		<label lsid="EN">projectType</label>
		<in>
			<select />
		</in>
	</property>
	
	<property name="seatCount"  id="seatCount" >
		<label lsid="CN">座位数</label>
		<label lsid="EN">seatCount</label>
		<in>
			<text width="10" />
			<validator id="ge" skip="next">
				<arg>0</arg>
				<msg lsid="CN">座位数应至少为0</msg>
				<msg lsid="EN">The field SEATCOUNT must greater or equal 0</msg>
			</validator>
		</in>
	</property>
	
	<property name="disabledSeatCount"  id="disabledSeatCount" >
		<label lsid="CN">疾障人座位数</label>
		<label lsid="EN">disabledSeatCount</label>
		<in>
			<text width="10" />
			<validator id="ge" skip="next">
				<arg>0</arg>
				<msg lsid="CN">疾障人座位数应至少为0</msg>
				<msg lsid="EN">The field DISABLEDSEATCOUNT must greater or equal 0</msg>
			</validator>
		</in>
	</property>
	
	<property name="serverBrand"  id="serverBrand" >
		<label lsid="CN">服务器品牌</label>
		<label lsid="EN">serverBrand</label>
		<in>
			<text width="50" maxlength="50" />
		</in>
	</property>
	
	<property name="projectBrand"  id="projectBrand" >
		<label lsid="CN">放映机品牌</label>
		<label lsid="EN">projectBrand</label>
		<in>
			<text width="50" maxlength="50" />
		</in>
	</property>
	
	<property name="isIMAX"  id="isIMAX" wrap="def" wrapname="isIMAX">
		<label lsid="CN">IMAX</label>
		<label lsid="EN">isIMAX</label>
		<in>
			<radio></radio>
		</in>
	</property>
	
	<property name="isRealD"  id="isRealD" wrap="def" wrapname="isRealD">
		<label lsid="CN">RealD</label>
		<label lsid="EN">isRealD</label>
		<in>
			<radio></radio>
		</in>
	</property>
	
	<property name="isDigital"  id="isDigital" wrap="def" wrapname="isDigital">
		<label lsid="CN">胶片</label>
		<label lsid="EN">isDigital</label>
		<in>
			<radio></radio>
		</in>
	</property>
	
	<property name="is3D"  id="is3D" wrap="def" wrapname="is3D">
		<label lsid="CN">3D</label>
		<label lsid="EN">is3D</label>
		<in>
			<radio></radio>
		</in>
	</property>
	
	<group id="search">
		<propref name="searchCinemaId" />
		<propref name="searchName" />
	</group>

	<group id="list">
		<propref name="searchCinemaId" sortasc="0" sortdesc="1"/>
		<propref name="name"  sortasc="2" sortdesc="3"/>
		<propref name="hallType" />
		<propref name="audioType" />
		<propref name="seatCount" />
		<propref name="disabledSeatCount" />
		<propref name="is3D" />
		<propref name="isDigital" />
		<propref name="isRealD" />
		<propref name="isIMAX" />
	</group>

	<group id="edit">
		<propref name="searchCinemaId" />
		<propref name="name" />
		<propref name="belongsTo" />
		<propref name="hallType" />
		<propref name="audioType" />
		<propref name="projectType" />
		<propref name="seatCount" />
		<propref name="disabledSeatCount" />
		<propref name="serverBrand" />
		<propref name="projectBrand" />
		<propref name="is3D" />
		<propref name="isDigital" />
		<propref name="isRealD" />
		<propref name="isIMAX" />
	</group>
</adb>
