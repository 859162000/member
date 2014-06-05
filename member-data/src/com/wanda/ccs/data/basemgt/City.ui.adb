<?xml version='1.0' encoding='UTF-8'?>

<adb>
	<uiwrapper name="def" />
	<property name="seqId" id="sid" />
	<property name="searchCode" get="getCode" set="setSearchCode"
		id="code">
		<label lsid="CN">编码</label>
		<label lsid="EN">Code</label>

		<in>
			<text width="10" maxlength="20" />
		</in>
	</property>

	<property name="searchName" get="getName" set="setSearchName"
		id="name">
		<label lsid="CN">名称</label>
		<label lsid="EN">Name</label>

		<in>
			<text width="10" maxlength="20" />
		</in>
	</property>

	<property name="searchProvince" wrap="def" id="province"
		wrapname="provinceId">
		<label lsid="CN">所属省/直辖市</label>
		<label lsid="EN">Province</label>

		<in>
			<select />
		</in>
	</property>

	<property name="isDelete" wrap="def" wrapname="isDelete" id="isDelete">
		<label> 是否已删除</label>
		<in>
			<radio />
		</in>
	</property>

	<property name="searchArea" get="getArea" set="setSearchArea"
		wrap="def" wrapname="area" id="area">
		<label lsid="CN">所属区域</label>
		<label lsid="EN">AreaCode</label>

		<in>
			<select />
		</in>
	</property>

	<property name="code" id="code" mandatory="yes">
		<label>编码</label>

		<out>
			<align>left</align>
		</out>

		<in>
			<text width="10" maxlength="20" />

			<validator id="ne" skip="next">
				<msg>请输入编码。</msg>
			</validator>
			<validator skip="next" method="checkCode">
				<msg>该编码已经存在!</msg>
			</validator>
			<validator skip="next" method="checkCodeLenth">
				<msg>编码长度不能超过20!</msg>
			</validator>
		</in>
	</property>

	<property name="name" id="name" mandatory="yes">
		<label>名称</label>

		<out>
			<align>left</align>
		</out>

		<in>
			<text width="10" maxlength="20" />

			<validator id="ne" skip="next">
				<msg>请输入名称。</msg>
			</validator>
			<validator skip="next" method="checkNameLenth">
				<msg>名称长度不能超过20个字节!</msg>
			</validator>
		</in>
	</property>

	<property name="provinceId" wrap="def" wrapname="provinceId" id="province" mandatory="yes">
		<label>所属省/直辖市</label>

		<out>
			<align>left</align>
		</out>

		<in>
			<select />

			<validator skip="next" method="checkProvince">
				<msg>请选择所属省/直辖市。</msg>
			</validator>
		</in>
	</property>

	<property name="area" wrap="def" wrapname="area" id="area" mandatory="yes">
		<label>所属区域</label>

		<out>
			<align>left</align>
		</out>

		<in>
			<select />

			<validator skip="next" method="checkArea">
				<msg>请选择所属区域。</msg>
			</validator>
		</in>
	</property>

	<property name="cityLevel" wrap="def" wrapname="cityLevel" id="cityLevel" mandatory="yes">
		<label>级别</label>

		<out>
			<align>left</align>
		</out>

		<in>
			<select />

			<validator id="ne" skip="next">
				<msg>请选择级别。</msg>
			</validator>
		</in>
	</property>

	<group id="search">
		<propref name="searchArea" />
		<propref name="searchProvince" />
		<propref name="searchCode" />
		<propref name="searchName" />
		<propref name="isDelete" />
	</group>

	<group id="list">
		<propref name="code" sortasc="0" sortdesc="1" />
		<propref name="name" sortasc="2" sortdesc="3" />
		<propref name="area" sortasc="4" sortdesc="5" />
		<propref name="provinceId" sortasc="6" sortdesc="7" />
		<propref name="cityLevel" sortasc="8" sortdesc="9" />
	</group>

	<group id="edit">
		<propref name="area" />
		<propref name="provinceId" />
		<propref name="code" />
		<propref name="name" />
		<propref name="cityLevel" />
	</group>
</adb>